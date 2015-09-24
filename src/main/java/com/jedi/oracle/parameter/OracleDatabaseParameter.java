/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle.parameter;

import com.jedi.common.DatabaseParameter;
import com.jedi.common.ParameterDirection;
import oracle.jdbc.OracleTypes;

/**
 * @author umitgunduz
 */
public final class OracleDatabaseParameter implements DatabaseParameter {

    private String name;
    private int index;
    private int oracleType = OracleTypes.VARCHAR;
    private ParameterDirection direction = ParameterDirection.Input;
    private Object value = null;

    public OracleDatabaseParameter() {

    }

    @Override
    public ParameterDirection getParameterDirection() {
        return this.direction;
    }

    @Override
    public String getParameterName() {
        return this.name;
    }

    @Override
    public void setParameterName(String name) {
        this.name = name;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
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

