/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.common;

import java.sql.Connection;

/**
 *
 * @author EXT0104423
 */
public interface IDbCommand {
    public Connection getConnection();
    public String getCommandText();
    public void setCommandText(String commandText);
    
    public CommandType getCommandType();
    public void setCommandType(CommandType commandType);
    public IDataParameterCollection getParameters();
}
