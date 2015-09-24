/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle.procedure;

import com.jedi.common.ExecutableComponent;

import java.util.Map;

/**
 * @author umitgunduz
 */
public interface OracleProcedure<T extends OracleProcedureParameter>
        extends ExecutableComponent<T> {

    String getConnectionName();

    String getName();

    String getSchema();

    String getPackage();

    void afterExecute();

    void beforeExecute();

    Map getTypeMap();
}

