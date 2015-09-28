/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import com.jedi.common.StoredProcedure;

/**
 * @author umitgunduz
 */
public abstract class OracleProcedure<T extends OracleParameters> extends OracleCall<T> {

    @Override
    public String getName() {
        StoredProcedure storedProcedure = this.getClass().getAnnotation(StoredProcedure.class);
        String result = "";
        if (!storedProcedure.schemaName().isEmpty()) {
            result += storedProcedure.schemaName() + ".";
        }

        if (!storedProcedure.packageName().isEmpty()) {
            result += storedProcedure.packageName() + ".";
        }

        if (!storedProcedure.name().isEmpty()) {
            result += storedProcedure.name();
        }

        return result;
    }
}

