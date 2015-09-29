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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.commons.io.IOUtils;

import java.io.Reader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author umitgunduz
 */
public class OracleParameterUtils {

    private final static StatementParameterFactory FACTORY = new OracleParameterUtils().new StatementParameterFactory();

    public static void register(OracleCallableStatement statement, OracleParameterCollection parameters) throws Exception {
        for (OracleParameter parameter : parameters) {
            OracleParameterUtils.register(statement, parameter);
        }
    }

    private static void register(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
        StatementParameter binder = FACTORY.getBinder(parameter);

        switch (parameter.getDirection()) {
            case Input:
                binder.registerInputParameter(statement, parameter);
                break;
            case Output:
            case ReturnValue:
                binder.registerOutParameter(statement, parameter);
                break;
            case InputOutput:
                binder.registerInputParameter(statement, parameter);
                binder.registerOutParameter(statement, parameter);
                break;
        }
    }

    public static void bind(OracleParameterCollection parameters, OracleCallableStatement statement) throws Exception {
        for (OracleParameter parameter : parameters) {
            OracleParameterUtils.bind(parameter, statement);
        }
    }

    public static void bind(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
        StatementParameter binder = FACTORY.getBinder(parameter);
        switch (parameter.getDirection()) {
            case Output:
            case InputOutput:
            case ReturnValue:
                binder.bindOutParameter(parameter, statement);
        }
    }

    private interface StatementParameter {

        void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception;

        void registerOutParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception;

        void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception;
    }

    private class StatementParameterFactory {

        public StatementParameter getBinder(OracleParameter parameter) {
            switch (parameter.getOracleDbType()) {
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
                    throw new UnsupportedOperationException(parameter.getOracleDbType() + " Oracle Types Unsupported");

            }
        }
    }

    private abstract class AbstractStatementParameter implements StatementParameter {

        @Override
        public void registerOutParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            if (parameter.getCustomTypeName() != null && !parameter.getCustomTypeName().isEmpty()) {
                statement.registerOutParameter(index, parameter.getOracleDbType(), parameter.getCustomTypeName());
            } else {
                statement.registerOutParameter(index, parameter.getOracleDbType());
            }
        }
    }

    private class StringParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            String value = (String) parameter.getValue();
            statement.setString(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            String value = statement.getString(index);
            parameter.setValue(value);
        }
    }

    private class BigDecimalParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            BigDecimal value = (BigDecimal) parameter.getValue();
            statement.setBigDecimal(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            BigDecimal value = statement.getBigDecimal(index);
            parameter.setValue(value);
        }
    }

    private class BooleanParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            Boolean value = (Boolean) parameter.getValue();
            statement.setBoolean(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Boolean value = statement.getBoolean(index);
            parameter.setValue(value);
        }
    }

    private class ByteParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            Byte value = (Byte) parameter.getValue();
            statement.setByte(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Byte value = statement.getByte(index);
            parameter.setValue(value);
        }
    }

    private class ShortParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            Short value = (Short) parameter.getValue();
            statement.setShort(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Short value = statement.getShort(index);
            parameter.setValue(value);
        }
    }

    private class IntegerParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            Integer value = (Integer) parameter.getValue();
            statement.setInt(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Integer value = statement.getInt(index);
            parameter.setValue(value);
        }
    }

    private class LongParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            Long value = (Long) parameter.getValue();
            statement.setLong(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Long value = statement.getLong(index);
            parameter.setValue(value);
        }
    }

    private class FloatParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            Float value = (Float) parameter.getValue();
            statement.setFloat(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Float value = statement.getFloat(index);
            parameter.setValue(value);
        }
    }

    private class DoubleParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            Double value = (Double) parameter.getValue();
            statement.setDouble(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Double value = statement.getDouble(index);
            parameter.setValue(value);
        }
    }

    private class BytesParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            byte[] value = (byte[]) parameter.getValue();
            statement.setBytes(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            byte[] value = statement.getBytes(index);
            parameter.setValue(value);
        }
    }

    private class DateParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            Date value = (Date) parameter.getValue();
            statement.setDate(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Date value = statement.getDate(index);
            parameter.setValue(value);
        }
    }

    private class TimeParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            Time value = (Time) parameter.getValue();
            statement.setTime(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Time value = statement.getTime(index);
            parameter.setValue(value);
        }
    }

    private class TimestampParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            Timestamp value = (Timestamp) parameter.getValue();
            statement.setTimestamp(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Timestamp value = statement.getTimestamp(index);
            parameter.setValue(value);
        }
    }

    private class BlobParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            byte[] bytes = (byte[]) parameter.getValue();
            Blob value = statement.getConnection().createBlob();
            value.setBytes(1, bytes);
            statement.setBlob(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Blob blob = statement.getBlob(index);
            byte[] value = blob.getBytes(1, (int) blob.length());
            parameter.setValue(value);
        }
    }

    private class ClobParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            String stringValue = (String) parameter.getValue();
            Clob value = statement.getConnection().createClob();
            value.setString(1, stringValue);
            statement.setClob(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Clob clob = statement.getClob(index);
            Reader reader = clob.getCharacterStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(reader, writer);
            String value = writer.toString();
            parameter.setValue(value);
        }
    }

    private class ObjectParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            int index = parameter.getIndex();
            Object value = parameter.getValue();
            statement.setObject(index, value);
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Object value = statement.getObject(index);
            parameter.setValue(value);
        }
    }

    private class ArrayParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(OracleCallableStatement statement, OracleParameter parameter) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public void bindOutParameter(OracleParameter parameter, OracleCallableStatement statement) throws Exception {
            int index = parameter.getIndex();
            Array array = statement.getArray(index);
            ResultSet resultSet = array.getResultSet();
            List value = new ArrayList();
            while (resultSet.next()) {
                value.add(resultSet.getObject(2));
            }

            parameter.setValue(value);
        }
    }
}

