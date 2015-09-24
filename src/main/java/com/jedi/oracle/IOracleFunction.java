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
public interface IOracleFunction<In extends IOracleFunctionInput, Out extends IOracleFunctionOutput>
        extends IExecutableComponent<In, Out> {

    public void afterExecute();

    public void beforeExecute();
    
    public Map getTypeMap();
}

