package com.jedi.oracle;

import com.jedi.common.ParameterDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by umit on 29/09/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OracleParameterMapping {
    String name();

    int index();

    int oracleType();

    ParameterDirection direction();
}
