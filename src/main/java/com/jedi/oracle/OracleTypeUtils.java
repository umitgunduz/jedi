package com.jedi.oracle;

import oracle.jdbc.OracleTypes;

import java.math.BigDecimal;
import java.sql.*;

/**
 * Created by umit on 24/09/15.
 */
public class OracleTypeUtils {

    private final static OracleTypeValueFactory FACTORY = new OracleTypeUtils().new OracleTypeValueFactory();

    public static Object getValue(SQLInput stream, int oracleType) throws SQLException {
        IOracleTypeValue reader = FACTORY.getOracleTypeValue(oracleType);
        return reader.read(stream);
    }

    public static void setValue(SQLOutput stream, int oracleType, Object value) throws SQLException {
        IOracleTypeValue writer = FACTORY.getOracleTypeValue(oracleType);
        writer.write(stream, value);
    }

    private class OracleTypeValueFactory {
        public IOracleTypeValue getOracleTypeValue(int oracleType) {
            switch (oracleType) {
                case OracleTypes.CHAR:
                case OracleTypes.VARCHAR:
                case OracleTypes.LONGVARCHAR:
                    return new StringOracleTypeValue();
                case OracleTypes.NUMERIC:
                case OracleTypes.DECIMAL:
                    return new BigDecimalOracleTypeValue();
                case OracleTypes.BIT:
                    return new BooleanOracleTypeValue();
                case OracleTypes.TINYINT:
                    return new ByteOracleTypeValue();
                case OracleTypes.SMALLINT:
                    return new ShortOracleTypeValue();
                case OracleTypes.INTEGER:
                    return new IntegerOracleTypeValue();
                case OracleTypes.BIGINT:
                    return new LongOracleTypeValue();
                case OracleTypes.REAL:
                    return new FloatOracleTypeValue();
                case OracleTypes.FLOAT:
                case OracleTypes.DOUBLE:
                    return new DoubleOracleTypeValue();
                case OracleTypes.BINARY:
                case OracleTypes.VARBINARY:
                case OracleTypes.LONGVARBINARY:
                    return new BytesOracleTypeValue();
                case OracleTypes.DATE:
                    return new DateOracleTypeValue();
                case OracleTypes.TIME:
                    return new TimeOracleTypeValue();
                case OracleTypes.TIMESTAMP:
                    return new TimestampOracleTypeValue();
                case OracleTypes.BLOB:
                    return new BlobOracleTypeValue();
                case OracleTypes.CLOB:
                    return new ClobOracleTypeValue();
                case OracleTypes.STRUCT:
                    return new ObjectOracleTypeValue();
                case OracleTypes.ARRAY:
                    return new ArrayOracleTypeValue();
                case OracleTypes.CURSOR:
                    throw new UnsupportedOperationException("CURSOR" + " Oracle Types Unsupported");
                default:
                    throw new UnsupportedOperationException(oracleType + " Oracle Types Unsupported");

            }
        }
    }


    private interface IOracleTypeValue {
        Object read(SQLInput stream) throws SQLException;

        void write(SQLOutput stream, Object value) throws SQLException;
    }

    private class StringOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readString();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeString((String) value);
        }
    }

    private class BigDecimalOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readBigDecimal();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeBigDecimal((BigDecimal) value);
        }
    }

    private class BooleanOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readBoolean();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeBoolean((Boolean) value);
        }
    }

    private class ByteOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readByte();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeByte((Byte) value);
        }
    }

    private class ShortOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readShort();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeShort((Short) value);
        }
    }

    private class IntegerOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readInt();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeInt((Integer) value);
        }
    }

    private class LongOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readLong();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeLong((Long) value);
        }
    }

    private class FloatOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readFloat();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeFloat((Float) value);
        }
    }

    private class DoubleOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readDouble();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeDouble((Double) value);
        }
    }

    private class BytesOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readBytes();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeBytes((byte[]) value);
        }
    }

    private class DateOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readDate();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeDate((Date) value);
        }
    }

    private class TimeOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readTime();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeTime((Time) value);
        }
    }

    private class TimestampOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readTimestamp();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeTimestamp((Timestamp) value);
        }
    }

    private class BlobOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readBlob();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeBlob((Blob) value);
        }
    }

    private class ClobOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readClob();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeClob((Clob) value);
        }
    }

    private class ObjectOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readObject();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeObject((SQLData) value);
        }
    }

    private class ArrayOracleTypeValue implements IOracleTypeValue {

        @Override
        public Object read(SQLInput stream) throws SQLException {
            return stream.readArray();
        }

        @Override
        public void write(SQLOutput stream, Object value) throws SQLException {
            stream.writeArray((Array) value);
        }
    }


}
