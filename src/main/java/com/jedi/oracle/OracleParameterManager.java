/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author EXT0104423
 */
public class OracleParameterManager {

    private final static StatementParameterFactory FACTORY = new OracleParameterManager().new StatementParameterFactory();

    public static void register(OracleParameterCollection parameters, OracleCallableStatement statement) throws Exception {
        for (OracleParameter parameter : parameters) {
           OracleParameterManager.register(parameter, statement);
        }
    }

    public static void register(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
        IStatementParameter binder = FACTORY.getBinder(parameter);

        switch (parameter.getParameterDirection()) {
            case INPUT:
                binder.registerInputParameter(parameter, statement);
                break;
            case OUTPUT:
                binder.registerOutParameter(parameter, statement);
                break;
            case INPUTOUTPUT:
                binder.registerInputParameter(parameter, statement);
                binder.registerOutParameter(parameter, statement);
                break;
        }
    }
    
    public static void bind(OracleParameterCollection parameters, OracleCallableStatement statement) throws Exception {
        for (OracleParameter parameter : parameters) {
            OracleParameterManager.bind(parameter, statement);
        }
    }

    public static void bind(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
        IStatementParameter binder = FACTORY.getBinder(parameter);

        switch (parameter.getParameterDirection()) {
            case OUTPUT:
            case INPUTOUTPUT:
                binder.bindOutParameter(parameter, statement);
        }
    }

    private class StatementParameterFactory {

        public IStatementParameter getBinder(OracleParameter parameter) {
            switch (parameter.getOracleTypes()) {
                case OracleTypes.CHAR:
                case OracleTypes.VARCHAR:
                case OracleTypes.LONGVARCHAR:
                    return new StringParameter();
                case OracleTypes.NUMERIC:
                case OracleTypes.DECIMAL:
                    return new BigDecimalParameter();
                case OracleTypes.BIT:
                    return new BooleanParameter();
                case OracleTypes.TINYINT:
                    return new ByteParameter();
                case OracleTypes.SMALLINT:
                    return new ShortParameter();
                case OracleTypes.INTEGER:
                    return new IntegerParameter();
                case OracleTypes.BIGINT:
                    return new LongParameter();
                case OracleTypes.REAL:
                    return new FloatParameter();
                case OracleTypes.FLOAT:
                case OracleTypes.DOUBLE:
                    return new DoubleParameter();
                case OracleTypes.BINARY:
                case OracleTypes.VARBINARY:
                case OracleTypes.LONGVARBINARY:
                    return new BytesParameter();
                case OracleTypes.DATE:
                    return new DateParameter();
                case OracleTypes.TIME:
                    return new TimeParameter();
                case OracleTypes.TIMESTAMP:
                    return new TimestampParameter();
                case OracleTypes.BLOB:
                    return new BlobParameter();
                case OracleTypes.CLOB:
                    return new ClobParameter();
                case OracleTypes.STRUCT:
                    return new ObjectParameter();
                case OracleTypes.ARRAY:
                    return new ArrayParameter();
                case OracleTypes.CURSOR:
                    throw new UnsupportedOperationException("CURSOR" + " Oracle Types Unsupported");
                default:
                    throw new UnsupportedOperationException(parameter.getOracleTypes() + " Oracle Types Unsupported");

            }
        }
    }

    private interface IStatementParameter {

        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception;

        public void registerOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception;

        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception;
    }

    private abstract class AbstractStatementParameter implements IStatementParameter {

        @Override
        public void registerOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            statement.registerOutParameter(name, parameter.getOracleTypes());
        }
    }

    private class StringParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            String value = (String) parameter.getValue();
            statement.setString(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            String value = statement.getString(name);
            parameter.setValue(value);
        }
    }

    private class BigDecimalParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            BigDecimal value = (BigDecimal) parameter.getValue();
            statement.setBigDecimal(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            BigDecimal value = statement.getBigDecimal(name);
            parameter.setValue(value);
        }
    }

    private class BooleanParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Boolean value = (Boolean) parameter.getValue();
            statement.setBoolean(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Boolean value = statement.getBoolean(name);
            parameter.setValue(value);
        }
    }

    private class ByteParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Byte value = (Byte) parameter.getValue();
            statement.setByte(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Byte value = statement.getByte(name);
            parameter.setValue(value);
        }
    }

    private class ShortParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Short value = (Short) parameter.getValue();
            statement.setShort(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Short value = statement.getShort(name);
            parameter.setValue(value);
        }
    }

    private class IntegerParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Integer value = (Integer) parameter.getValue();
            statement.setInt(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Integer value = statement.getInt(name);
            parameter.setValue(value);
        }
    }

    private class LongParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Long value = (Long) parameter.getValue();
            statement.setLong(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Long value = statement.getLong(name);
            parameter.setValue(value);
        }
    }

    private class FloatParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Float value = (Float) parameter.getValue();
            statement.setFloat(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Float value = statement.getFloat(name);
            parameter.setValue(value);
        }
    }

    private class DoubleParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Double value = (Double) parameter.getValue();
            statement.setDouble(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Double value = statement.getDouble(name);
            parameter.setValue(value);
        }
    }

    private class BytesParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            byte[] value = (byte[]) parameter.getValue();
            statement.setBytes(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            byte[] value = statement.getBytes(name);
            parameter.setValue(value);
        }
    }

    private class DateParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Date value = (Date) parameter.getValue();
            statement.setDate(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Date value = statement.getDate(name);
            parameter.setValue(value);
        }
    }

    private class TimeParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Time value = (Time) parameter.getValue();
            statement.setTime(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Time value = statement.getTime(name);
            parameter.setValue(value);
        }
    }

    private class TimestampParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Timestamp value = (Timestamp) parameter.getValue();
            statement.setTimestamp(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Timestamp value = statement.getTimestamp(name);
            parameter.setValue(value);
        }
    }

    private class BlobParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Blob value = (Blob) parameter.getValue();
            statement.setBlob(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Blob value = statement.getBlob(name);
            parameter.setValue(value);
        }
    }

    private class ClobParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Clob value = (Clob) parameter.getValue();
            statement.setClob(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Clob value = statement.getClob(name);
            parameter.setValue(value);
        }
    }

    private class ObjectParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Object value = parameter.getValue();
            statement.setObject(name, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Object value = statement.getObject(name);
            parameter.setValue(value);
        }
    }
    
    private class ArrayParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            String name = parameter.getParameterName();
            Array value = statement.getArray(name);
            parameter.setValue(value);
        }
    }
}
