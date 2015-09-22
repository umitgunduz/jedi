/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import com.jedi.common.DbParameter;
import com.jedi.common.ParameterDirection;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author umitgunduz
 */
public final class OracleParameter extends DbParameter {

    private String name;
    private int oracleType = OracleTypes.VARCHAR;
    private ParameterDirection direction = ParameterDirection.INPUT;
    private Boolean isNullable;
    private byte precision;
    private byte scale;
    private int size;
    private Object value = null;

    public OracleParameter() {

    }

    @Override
    public void setIsNullable(Boolean isNullable) {
        this.isNullable = isNullable;
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
    public byte getPrecision() {
        return this.precision;
    }

    @Override
    public void setPrecision(byte precision) {
        this.precision = precision;
    }

    @Override
    public byte getScale() {
        return this.scale;
    }

    @Override
    public void setScale(byte scale) {
        this.scale = scale;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Boolean isNullable() {
        return this.isNullable;
    }

    public int getOracleTypes() {
        return this.oracleType;
    }

    public void setOracleTypes(int oracleTypes) {
        this.oracleType = oracleTypes;
    }

}

