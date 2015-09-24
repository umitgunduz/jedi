/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.common;

/**
 * @author umitgunduz
 */
public interface DataParameter {
    ParameterDirection getParameterDirection();

    String getParameterName();

    void setParameterName(String name);

    Object getValue();

    void setValue(Object value);
}

