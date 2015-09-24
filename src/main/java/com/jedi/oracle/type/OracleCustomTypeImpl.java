package com.jedi.oracle.type;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.List;

/**
 * Created by umit on 22/09/15.
 */
public abstract class OracleCustomTypeImpl implements SQLData, OracleCustomType {

    @Override
    public String getSQLTypeName() throws SQLException {

        OracleCustomTypeMapping mapping = this.getClass().getAnnotation(OracleCustomTypeMapping.class);
        return mapping.name();
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(this.getClass(), OracleObjectMapping.class);
        List<Field> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<Field, Integer>() {
            public Integer apply(Field field) {
                OracleObjectMapping mapping = field.getAnnotation(OracleObjectMapping.class);
                return mapping.index();
            }
        }).sortedCopy(fields);

        for (Field field : ordering) {

            int oracleType = field.getDeclaredAnnotation(OracleObjectMapping.class).oracleType();
            Object value = OracleTypeUtils.getValue(stream, oracleType);
            if (!field.getType().isInstance(value)) {
                try {
                    value = SqlTypeConverter.convert(value, field.getType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            if (value != null) {
                try {
                    field.set(this, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {

    }
}
