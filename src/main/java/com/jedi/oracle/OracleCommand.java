/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import com.jedi.common.CommandType;
import com.jedi.common.DatabaseCommand;
import com.jedi.common.ParameterCollection;
import com.jedi.oracle.parameter.OracleDatabaseParameter;
import com.jedi.oracle.parameter.OracleDatabaseParameterCollection;
import com.jedi.oracle.parameter.OracleParameterUtils;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author umitgunduz
 */
public final class OracleCommand implements DatabaseCommand {

    String commandText;
    CommandType commandType;
    OracleConnection connection;
    OracleDatabaseParameterCollection parameters = new OracleDatabaseParameterCollection();

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
    public ParameterCollection getParameters() {
        return this.parameters;
    }

    public void AddParameter(OracleDatabaseParameter parameter) {
        this.parameters.add(parameter);
    }

    public void AddParameters(OracleDatabaseParameterCollection parameters) {
        this.parameters.addAll(parameters);
    }

    public void execute() throws SQLException, Exception {
        OracleCallableStatement statement = (OracleCallableStatement) connection.prepareCall(commandText);
        OracleParameterUtils.register(this.parameters, statement);
        try {
            statement.execute();
        } catch (SQLException e) {
            if (reExecutionRequired(e)) {
                statement.execute();
            } else {
                throw e;
            }
        }

        OracleParameterUtils.bind(parameters, statement);
    }

    private boolean reExecutionRequired(SQLException e) {
        return "72000".equals(e.getSQLState()) && e.getErrorCode() == 4068;
    }
}

