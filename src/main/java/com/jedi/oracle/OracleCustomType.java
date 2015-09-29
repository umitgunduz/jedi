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
import com.jedi.common.CustomType;
import com.jedi.common.CustomTypeMapping;
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
public abstract class OracleCustomType implements SQLData, CustomType {

    @Override
    public String getSQLTypeName() throws SQLException {

        CustomTypeMapping mapping = this.getClass().getAnnotation(CustomTypeMapping.class);
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
            int oracleType = field.getAnnotation(OracleObjectMapping.class).oracleType();
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
                    field.setAccessible(true);
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
