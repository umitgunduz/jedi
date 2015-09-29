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

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

/**
 * @author umitgunduz
 */
public class OracleParameterUtils {

    private final static StatementParameterFactory FACTORY = new OracleParameterUtils().new StatementParameterFactory();

    public static void register(Object parameters, OracleCallableStatement statement) throws Exception {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(parameters.getClass(), OracleParameterMapping.class);
        if (fields != null && !fields.isEmpty()) {
            return;
        }

        List<Field> orderingFields = Ordering.natural().nullsFirst().onResultOf(new Function<Field, Integer>() {
            public Integer apply(Field field) {
                OracleParameterMapping mapping = field.getAnnotation(OracleParameterMapping.class);
                return mapping.index();
            }
        }).sortedCopy(fields);

        for (Field field : orderingFields) {
            OracleParameterUtils.register(parameters, field, statement);
        }
    }

    private static void register(Object parameters, Field field, OracleCallableStatement statement) throws Exception {
        OracleParameterMapping mapping = field.getAnnotation(OracleParameterMapping.class);
        IStatementParameter binder = FACTORY.getBinder(mapping);

        switch (mapping.direction()) {
            case Input:
                binder.registerInputParameter(parameters, field, mapping, statement);
                break;
            case Output:
            case ReturnValue:
                binder.registerOutParameter(field, mapping, statement);
                break;
            case InputOutput:
                binder.registerInputParameter(parameters, field, mapping, statement);
                binder.registerOutParameter(field, mapping, statement);
                break;
        }
    }

    public static void bind(OracleCallableStatement statement, Object parameters) throws Exception {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(parameters.getClass(), OracleParameterMapping.class);
        if (fields != null && !fields.isEmpty()) {
            return;
        }

        List<Field> orderingFields = Ordering.natural().nullsFirst().onResultOf(new Function<Field, Integer>() {
            public Integer apply(Field field) {
                OracleParameterMapping mapping = field.getAnnotation(OracleParameterMapping.class);
                return mapping.index();
            }
        }).sortedCopy(fields);

        for (Field field : fields) {
            OracleParameterUtils.bind(statement, parameters, field);
        }
    }

    public static void bind(OracleCallableStatement statement, Object parameters, Field field) throws Exception {
        OracleParameterMapping mapping = field.getAnnotation(OracleParameterMapping.class);
        IStatementParameter binder = FACTORY.getBinder(mapping);

        switch (mapping.direction()) {
            case Output:
            case InputOutput:
            case ReturnValue:
                binder.bindOutParameter(statement, parameters, field, mapping);
        }
    }

    private class StatementParameterFactory {

        public IStatementParameter getBinder(OracleParameterMapping mapping) {
            switch (mapping.oracleType()) {
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
                    throw new UnsupportedOperationException(mapping.oracleType() + " Oracle Types Unsupported");

            }
        }
    }

    private interface IStatementParameter {

        void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception;

        void registerOutParameter(Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception;

        void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception;
    }

    private abstract class AbstractStatementParameter implements IStatementParameter {

        @Override
        public void registerOutParameter(Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            statement.registerOutParameter(name, mapping.oracleType());
        }
    }

    private class StringParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            String value = (String) field.get(parameters);
            statement.setString(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            String value = statement.getString(name);
            field.set(parameters, value);
        }
    }

    private class BigDecimalParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            BigDecimal value = (BigDecimal) field.get(parameters);
            statement.setBigDecimal(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            BigDecimal value = statement.getBigDecimal(name);
            field.set(parameters, value);
        }
    }

    private class BooleanParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            Boolean value = (Boolean) field.get(parameters);
            statement.setBoolean(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Boolean value = statement.getBoolean(name);
            field.set(parameters, value);
        }
    }

    private class ByteParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            Byte value = (Byte) field.get(parameters);
            statement.setByte(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Byte value = statement.getByte(name);
            field.set(parameters, value);
        }
    }

    private class ShortParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            Short value = (Short) field.get(parameters);
            statement.setShort(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Short value = statement.getShort(name);
            field.set(parameters, value);
        }
    }

    private class IntegerParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            Integer value = (Integer) field.get(parameters);
            statement.setInt(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Integer value = statement.getInt(name);
            field.set(parameters, value);
        }
    }

    private class LongParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            Long value = (Long) field.get(parameters);
            statement.setLong(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Long value = statement.getLong(name);
            field.set(parameters, value);
        }
    }

    private class FloatParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            Float value = (Float) field.get(parameters);
            statement.setFloat(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Float value = statement.getFloat(name);
            field.set(parameters, value);
        }
    }

    private class DoubleParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            Double value = (Double) field.get(parameters);
            statement.setDouble(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Double value = statement.getDouble(name);
            field.set(parameters, value);
        }
    }

    private class BytesParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            byte[] value = (byte[]) field.get(parameters);
            statement.setBytes(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            byte[] value = statement.getBytes(name);
            field.set(parameters, value);
        }
    }

    private class DateParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            Date value = (Date) field.get(parameters);
            statement.setDate(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Date value = statement.getDate(name);
            field.set(parameters, value);
        }
    }

    private class TimeParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            Time value = (Time) field.get(parameters);
            statement.setTime(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Time value = statement.getTime(name);
            field.set(parameters, value);
        }
    }

    private class TimestampParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            Timestamp value = (Timestamp) field.get(parameters);
            statement.setTimestamp(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Timestamp value = statement.getTimestamp(name);
            field.set(parameters, value);
        }
    }

    private class BlobParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            byte[] bytes = (byte[]) field.get(parameters);
            Blob value = statement.getConnection().createBlob();
            value.setBytes(1, bytes);
            statement.setBlob(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Blob blob = statement.getBlob(name);
            byte[] value = blob.getBytes(1, (int) blob.length());
            field.set(parameters, value);
        }
    }

    private class ClobParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            String stringValue = (String) field.get(parameters);
            Clob value = statement.getConnection().createClob();
            value.setString(1, stringValue);
            statement.setClob(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Clob clob = statement.getClob(name);
            Reader reader = clob.getCharacterStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(reader, writer);
            String value = writer.toString();
            field.set(parameters, value);
        }
    }

    private class ObjectParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            String name = mapping.name();
            Object value = field.get(parameters);
            statement.setObject(name, value);
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            String name = mapping.name();
            Object value = statement.getObject(name);
            field.set(parameters, value);
        }
    }

    private class ArrayParameter extends AbstractStatementParameter {

        @Override
        public void registerInputParameter(Object parameters, Field field, OracleParameterMapping mapping, OracleCallableStatement statement) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public void bindOutParameter(OracleCallableStatement statement, Object parameters, Field field, OracleParameterMapping mapping) throws Exception {
            throw new UnsupportedOperationException();
        }
    }
}

