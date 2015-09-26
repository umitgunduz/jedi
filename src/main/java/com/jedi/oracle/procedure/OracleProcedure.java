/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle.procedure;

import com.jedi.oracle.OracleNamedQuery;
import com.jedi.oracle.OracleQueryParameters;

/**
 * @author umitgunduz
 */
public abstract class OracleProcedure<T extends OracleQueryParameters> extends OracleNamedQuery<T> {

    @Override
    public String getName() {
        OracleNamedProcedure description = this.getClass().getAnnotation(OracleNamedProcedure.class);
        String result = "";
        if (!description.schemaName().isEmpty()) {
            result += description.schemaName() + ".";
        }

        if (!description.packageName().isEmpty()) {
            result += description.packageName() + ".";
        }

        if (!description.name().isEmpty()) {
            result += description.name();
        }

        return result;
    }
}

