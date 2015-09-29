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
import com.google.common.collect.Ordering;
import com.jedi.common.DataSourceManager;
import com.jedi.common.SqlCall;
import oracle.jdbc.OracleCallableStatement;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by umit on 26/09/15.
 */
public abstract class OracleCall<T extends OracleParameters> implements SqlCall<T> {

    @Override
    public T execute(T parameters) throws Exception {
        DataSource dataSource = DataSourceManager.getInstance().getDataSource();
        return execute(dataSource, parameters);
    }

    @Override
    public T execute(DataSource dataSource, T parameters) throws Exception {
        Connection connection = dataSource.getConnection();
        try {
            parameters = execute(connection, parameters);
        } finally {
            if (!connection.isClosed()) {
                connection.close();
            }
        }

        return parameters;
    }

    @Override
    public T execute(Connection connection, T parameters) throws Exception {
        if (connection == null) {
            DataSource dataSource = DataSourceManager.getInstance().getDataSource();
            if (dataSource == null) {
                throw new RuntimeException("Datasource is null");
            }

            connection = dataSource.getConnection();
        }

        String sql = this.createSQL(this.getName(), parameters);
        Map customTypes = this.getTypeMap();
        if (customTypes != null && !customTypes.isEmpty()) {
            Map map = connection.getTypeMap();
            map.putAll(customTypes);
            connection.setTypeMap(map);
        }

        OracleCallableStatement statement = (OracleCallableStatement) connection.prepareCall(sql);
        OracleParameterUtils.register(parameters, statement);
        try {
            statement.execute();
        } catch (SQLException e) {
            if (reExecutionRequired(e)) {
                statement.execute();
            } else {
                throw e;
            }
        }

        OracleParameterUtils.bind(statement, parameters);


        return parameters;
    }

    private Map getTypeMap() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class parameterClass = (Class) parameterizedType.getActualTypeArguments()[0];
        return OracleTypeUtils.findCustomTypes(parameterClass);
    }

    public abstract String getName();

    private boolean reExecutionRequired(SQLException e) {
        return "72000".equals(e.getSQLState()) && e.getErrorCode() == 4068;
    }

    private String createSQL(String queryName, OracleParameters parameters) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(parameters.getClass(), OracleParameterMapping.class);
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
