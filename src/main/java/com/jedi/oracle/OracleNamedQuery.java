package com.jedi.oracle;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.jedi.oracle.parameter.OracleDatabaseParameterCollection;
import com.jedi.oracle.parameter.OracleParameterUtils;
import com.jedi.oracle.type.OracleObjectMapping;
import com.jedi.oracle.type.OracleTypeUtils;
import oracle.jdbc.OracleCallableStatement;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by umit on 26/09/15.
 */
public abstract class OracleNamedQuery<T extends OracleQueryParameters> implements OracleQuery<T> {
    @Override
    public T execute(T parameters) throws Exception {
        Connection connection = OracleConnectionManager.getInstance().getConnection();
        String sql = this.createSQL(this.getName(), parameters);
        Map customTypes = this.getTypeMap();
        if (customTypes != null && !customTypes.isEmpty()) {
            Map map = connection.getTypeMap();
            map.putAll(customTypes);
            connection.setTypeMap(map);
        }

        execute(connection, sql, parameters);

        return parameters;
    }

    private Map getTypeMap() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class parameterClass = (Class) parameterizedType.getActualTypeArguments()[0];
        return OracleTypeUtils.findCustomTypes(parameterClass);
    }

    public abstract String getName();

    private void execute(Connection connection, String sql, T parameters) throws Exception {
        OracleCallableStatement statement = (OracleCallableStatement) connection.prepareCall(sql);
        OracleDatabaseParameterCollection databaseParameters = OracleParameterUtils.convert(parameters);
        OracleParameterUtils.register(databaseParameters, statement);
        try {
            statement.execute();
        } catch (SQLException e) {
            if (reExecutionRequired(e)) {
                statement.execute();
            } else {
                throw e;
            }
        }

        OracleParameterUtils.bind(statement, databaseParameters);
        OracleParameterUtils.bind(databaseParameters, parameters);
    }

    private boolean reExecutionRequired(SQLException e) {
        return "72000".equals(e.getSQLState()) && e.getErrorCode() == 4068;
    }

    private String createSQL(String queryName, OracleQueryParameters parameters) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(parameters.getClass(), OracleObjectMapping.class);
        String retVal = "";
        String params = "";

        if (fields != null && !fields.isEmpty()) {
            List<Field> orderingFields = Ordering.natural().nullsFirst().onResultOf(new Function<Field, Integer>() {
                public Integer apply(Field field) {
                    OracleObjectMapping mapping = field.getAnnotation(OracleObjectMapping.class);
                    return mapping.index();
                }
            }).sortedCopy(fields);

            for (Field field : orderingFields) {
                OracleObjectMapping mapping = field.getAnnotation(OracleObjectMapping.class);
                switch (mapping.direction()) {
                    case ReturnValue:
                        retVal = "p_return_value => ?";
                        break;
                    default:
                        if (params.indexOf(',') == -1) {
                            params += mapping.name() + " => ?";
                        } else {
                            params += ", " + mapping.name() + " => ?";
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
        sb.append(")");
        sb.append("END;");

        return sb.toString();

    }
}
