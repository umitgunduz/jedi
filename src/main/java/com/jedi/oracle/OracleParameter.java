package com.jedi.oracle;

import com.jedi.common.ParameterDirection;

/**
 * Created by EXT0104423 on 9/29/2015.
 */
public class OracleParameter {
    private String name;
    private int index;
    private int oracleDbType;
    private int dbType;
    private ParameterDirection direction;
    private Object value;
    private String customTypeName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getOracleDbType() {
        return oracleDbType;
    }

    public void setOracleDbType(int oracleDbType) {
        this.oracleDbType = oracleDbType;
    }

    public int getDbType() {
        return dbType;
    }

    public void setDbType(int dbType) {
        this.dbType = dbType;
    }

    public ParameterDirection getDirection() {
        return direction;
    }

    public void setDirection(ParameterDirection direction) {
        this.direction = direction;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getCustomTypeName() {
        return customTypeName;
    }

    public void setCustomTypeName(String customTypeName) {
        this.customTypeName = customTypeName;
    }
}
