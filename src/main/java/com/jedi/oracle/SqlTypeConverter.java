package com.jedi.oracle;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
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

    private class SqlTypeConverterFactory {
        ISqlTypeConverter getConverter(Class<?> from, Class<?> to) {
            if (from.isAssignableFrom(Blob.class) && to.isAssignableFrom(String.class)) {
                return new SqlBlobToStringConverter();
            } else if (from.isAssignableFrom(Blob.class) && to.isAssignableFrom(byte[].class)) {
                return new SqlBlobToByteArrayConverter();
            } else if (from.isAssignableFrom(Clob.class) && to.isAssignableFrom(String.class)) {
                return new SqlClobToStringConverter();
            } else {
                throw new UnsupportedOperationException(from.getName() + " " + to.getName() + " transformation is not supported");
            }

        }
    }

    private interface ISqlTypeConverter<From, To> {
        To convert(From from) throws SQLException, IOException;
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


}
