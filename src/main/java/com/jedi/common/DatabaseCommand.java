/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.common;

import java.sql.Connection;

/**
 * @author umitgunduz
 */
public interface DatabaseCommand {
    Connection getConnection();

    String getCommandText();

    void setCommandText(String commandText);

    CommandType getCommandType();

    void setCommandType(CommandType commandType);

    ParameterCollection getParameters();
}

