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
public interface IDataParameter {
    public ParameterDirection getParameterDirection();
    
    public Boolean isNullable();
    public void setIsNullable(Boolean isNullable);
    
    public String getParameterName();
    public void setParameterName(String name);
    
    public Object getValue();
    public void setValue(Object value);
}

