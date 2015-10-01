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

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Created by umit on 24/09/15.
 */
public class SqlTypeConverter {

    private final static SqlTypeConverterFactory FACTORY = new SqlTypeConverter().new SqlTypeConverterFactory();

    public static Object convert(Object value, Class<?> clazz) throws IOException, SQLException {
        ISqlTypeConverter converter = FACTORY.getConverter(value.getClass(), clazz);
        return converter.convert(value);
    }

    private interface ISqlTypeConverter<From, To> {
        To convert(From from) throws SQLException, IOException;
    }

    private class SqlTypeConverterFactory {
        ISqlTypeConverter getConverter(Class<?> from, Class<?> to) {
            if (Blob.class.isAssignableFrom(from) && String.class.isAssignableFrom(to)) {
                return new SqlBlobToStringConverter();
            } else if (Blob.class.isAssignableFrom(from) && byte[].class.isAssignableFrom(to)) {
                return new SqlBlobToByteArrayConverter();
            } else if (Clob.class.isAssignableFrom(from) && String.class.isAssignableFrom(to)) {
                return new SqlClobToStringConverter();
            } else if (BigDecimal.class.isAssignableFrom(from) && (int.class.isAssignableFrom(to) || Integer.class.isAssignableFrom(to))) {
                return new BigDecimalToIntegerConverter();

            } else {
                throw new UnsupportedOperationException(from.getName() + " " + to.getName() + " transformation is not supported");
            }

        }
    }

    private class SqlBlobToStringConverter implements ISqlTypeConverter<Blob, String> {

        @Override
        public String convert(Blob blob) throws SQLException {
            String result = null;
            byte[] data = blob.getBytes(1, (int) blob.length());
            if (data != null) {
                result = new String(data);
            }

            return result;
        }
    }

    private class SqlBlobToByteArrayConverter implements ISqlTypeConverter<Blob, byte[]> {

        @Override
        public byte[] convert(Blob blob) throws SQLException {
            return blob.getBytes(1, (int) blob.length());
        }
    }

    private class SqlClobToStringConverter implements ISqlTypeConverter<Clob, String> {

        @Override
        public String convert(Clob clob) throws SQLException, IOException {
            Reader reader = clob.getCharacterStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(reader, writer);
            return writer.toString();
        }
    }

    private class BigDecimalToIntegerConverter implements ISqlTypeConverter<BigDecimal, Integer> {

        @Override
        public Integer convert(BigDecimal bigDecimal) throws SQLException, IOException {
            return bigDecimal.intValueExact();
        }
    }


}
