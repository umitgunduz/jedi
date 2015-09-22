/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.common;

/**
 *
 * @author umitgunduz
 */
public interface IDbDataParameter extends IDataParameter {

    public byte getPrecision();
    public void setPrecision(byte precision);

    public byte getScale();
    public void setScale(byte scale);

    public int getSize();
    public void setSize(int size);

}

