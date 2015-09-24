/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle.function;

import com.jedi.common.ExecutableComponent;
import com.jedi.oracle.OracleParameters;

import java.util.Map;

/**
 * @author umitgunduz
 */
public interface OracleFunction<T extends OracleParameters>
        extends ExecutableComponent<T> {

    void afterExecute();

    void beforeExecute();

    Map getTypeMap();
}

