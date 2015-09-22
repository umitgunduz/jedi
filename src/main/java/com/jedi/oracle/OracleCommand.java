/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import com.jedi.common.CommandType;
import com.jedi.common.DbCommand;
import com.jedi.common.IDataParameterCollection;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author umitgunduz
 */
public final class OracleCommand extends DbCommand {

    String commandText;
    CommandType commandType;
    OracleConnection connection;
    OracleParameterCollection parameters = new OracleParameterCollection();

    public OracleCommand(String commandText, OracleConnection connection) {
        this.commandText = commandText;
        this.connection = connection;
    }

    @Override
    public String getCommandText() {
        return this.commandText;
    }

    @Override
    public void setCommandText(String commandText) {
        this.commandText = commandText;
    }

    @Override
    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public IDataParameterCollection getParameters() {
        return this.parameters;
    }

    public OracleParameter createParameter() {
        return new OracleParameter();
    }

    public void execute() throws SQLException, Exception {
        OracleCallableStatement statement = (OracleCallableStatement) connection.prepareCall(commandText);
        OracleParameterManager.register(this.parameters, statement);
        try {
            statement.execute();
        } catch (SQLException e) {
            if (reExecutionRequired(e)) {
                statement.execute();
            } else {
                throw e;
            }
        }
        
        OracleParameterManager.bind(parameters, statement);
    }

    private boolean reExecutionRequired(SQLException e) {
        return "72000".equals(e.getSQLState()) && e.getErrorCode() == 4068;
    }
}

