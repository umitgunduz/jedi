/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import com.jedi.common.IExecutableComponent;

import java.util.Map;

/**
 *
 * @author umitgunduz
 * @param <In>
 * @param <Out>
 */
public interface IOracleProcedure<In extends IOracleProcedureInput, Out extends IOracleProcedureOutput>
        extends IExecutableComponent<In, Out> {

    public String getConnectionName();

    public String getName();

    public String getSchema();

    public String getPackage();

    public void afterExecute();

    public void beforeExecute();

    public Map getTypeMap();
}

