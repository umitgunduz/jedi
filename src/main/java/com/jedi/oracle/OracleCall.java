/*
 *
 *  * Copyright 2015 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.jedi.oracle;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.jedi.common.DataSourceManager;
import com.jedi.common.SqlCall;
import oracle.jdbc.OracleCallableStatement;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by umit on 26/09/15.
 */
public abstract class OracleCall implements SqlCall {

    final private OracleParameterCollection parameters = new OracleParameterCollection();

    @Override
    public void execute() throws Exception {
        DataSource dataSource = DataSourceManager.getInstance().getDataSource();
        this.execute(dataSource);
    }

    @Override
    public void execute(DataSource dataSource) throws Exception {
        Connection connection = dataSource.getConnection();
        try {
            this.execute(connection);
        } finally {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
    }

    @Override
    public void execute(Connection connection) throws Exception {
        if (connection == null) {
            DataSource dataSource = DataSourceManager.getInstance().getDataSource();
            if (dataSource == null) {
                throw new RuntimeException("Datasource is null");
            }

            connection = dataSource.getConnection();
        }

        fillParametersFromFields();

        String sql = this.createSQL(this.getName());
        Map customTypes = this.getTypeMap();
        if (customTypes != null && !customTypes.isEmpty()) {
            Map map = connection.getTypeMap();
            map.putAll(customTypes);
            connection.setTypeMap(map);
        }

        OracleCallableStatement statement = (OracleCallableStatement) connection.prepareCall(sql);
        OracleParameterUtils.register(statement, this.parameters);
        try {
            statement.execute();
        } catch (SQLException e) {
            if (reExecutionRequired(e)) {
                statement.execute();
            } else {
                throw e;
            }
        }

        OracleParameterUtils.bind(this.parameters, statement);
        fillFieldValuesFromParameters();
    }

    private void fillParametersFromFields() throws IllegalAccessException {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(getClass(), OracleParameterMapping.class);
        if (fields == null || fields.isEmpty()) {
            return;
        }

        for (Field field : fields) {
            OracleParameterMapping mapping = field.getAnnotation(OracleParameterMapping.class);
            OracleParameter parameter = new OracleParameter();
            parameter.setDbType(mapping.dbType());
            parameter.setDirection(mapping.direction());
            parameter.setIndex(mapping.index());
            parameter.setName(mapping.name());
            parameter.setOracleDbType(mapping.oracleType());
            parameter.setCustomTypeName(mapping.customTypeName());
            switch (parameter.getDirection()) {
                case Input:
                case InputOutput:
                    field.setAccessible(true);
                    parameter.setValue(field.get(this));
                    break;
            }

            this.parameters.add(parameter);
        }
    }

    private void fillFieldValuesFromParameters() throws IllegalAccessException {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(getClass(), OracleParameterMapping.class);
        if (fields == null || fields.isEmpty()) {
            return;
        }


        for (final OracleParameter parameter : this.parameters) {
            switch (parameter.getDirection()) {
                case ReturnValue:
                case InputOutput:
                case Output:
                    Field field = Iterables.find(fields, new Predicate<Field>() {
                        public boolean apply(Field item) {
                            OracleParameterMapping mapping = item.getAnnotation(OracleParameterMapping.class);
                            return mapping.name().equals(parameter.getName());
                        }
                    });

                    field.setAccessible(true);
                    field.set(this, parameter.getValue());
            }
        }
    }


    private Map getTypeMap() {
        return OracleTypeUtils.findCustomTypes(getClass());
    }

    public abstract String getName();

    private boolean reExecutionRequired(SQLException e) {
        return "72000".equals(e.getSQLState()) && e.getErrorCode() == 4068;
    }

    private String createSQL(String queryName) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(getClass(), OracleParameterMapping.class);
        String retVal = "";
        String params = "";

        if (fields != null && !fields.isEmpty()) {
            List<Field> orderingFields = Ordering.natural().nullsFirst().onResultOf(new Function<Field, Integer>() {
                public Integer apply(Field field) {
                    OracleParameterMapping mapping = field.getAnnotation(OracleParameterMapping.class);
                    return mapping.index();
                }
            }).sortedCopy(fields);

            for (Field field : orderingFields) {
                OracleParameterMapping mapping = field.getAnnotation(OracleParameterMapping.class);
                switch (mapping.direction()) {
                    case ReturnValue:
                        retVal = "? :=";
                        break;
                    default:
                        if (params.indexOf(',') == -1) {
                            params += mapping.name() + "?";
                        } else {
                            params += ", " + mapping.name() + "?";
                        }
                        break;
                }
            }
        }


        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN");
        sb.append(" ");
        if (!retVal.isEmpty()) {
            sb.append(retVal);
            sb.append(" ");
        }
        sb.append(queryName);
        sb.append("(");
        if (!params.isEmpty()) {
            sb.append(params);
        }
        sb.append(");");
        sb.append(" ");
        sb.append("END;");

        return sb.toString();

    }
}
