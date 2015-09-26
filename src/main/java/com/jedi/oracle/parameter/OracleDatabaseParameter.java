/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle.parameter;

import com.jedi.common.DatabaseParameter;
import com.jedi.common.ParameterDirection;

/**
 * @author umitgunduz
 */
public final class OracleDatabaseParameter implements DatabaseParameter {

    private String name;
    private int index;
    private int oracleType;
    private ParameterDirection direction;
    private Object value = null;

    public OracleDatabaseParameter() {

    }


    public ParameterDirection getDirection() {
        return this.direction;
    }

    public void setDirection(ParameterDirection direction) {
        this.direction = direction;
    }


    public String getParameterName() {
        return this.name;
    }


    public void setParameterName(String name) {
        this.name = name;
    }


    public Object getValue() {
        return this.value;
    }


    public void setValue(Object value) {
        this.value = value;
    }


    public int getOracleTypes() {
        return this.oracleType;
    }

    public void setOracleTypes(int oracleTypes) {
        this.oracleType = oracleTypes;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


}

