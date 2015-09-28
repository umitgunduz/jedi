/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import com.jedi.common.StoredFunction;

/**
 * @author umitgunduz
 */
public abstract class OracleFunction<T extends OracleCallParameters> extends OracleCall<T> {

    @Override
    public String getName() {
        StoredFunction storedFunction = this.getClass().getAnnotation(StoredFunction.class);
        String result = "";
        if (!storedFunction.schemaName().isEmpty()) {
            result += storedFunction.schemaName() + ".";
        }

        if (!storedFunction.packageName().isEmpty()) {
            result += storedFunction.packageName() + ".";
        }

        if (!storedFunction.name().isEmpty()) {
            result += storedFunction.name();
        }

        return result;
    }
}

