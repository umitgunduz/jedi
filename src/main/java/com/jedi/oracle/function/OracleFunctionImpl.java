/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle.function;

import com.jedi.oracle.OracleCommand;
import com.jedi.oracle.OracleConnectionManager;
import com.jedi.oracle.parameter.OracleDatabaseParameterCollection;
import com.jedi.oracle.parameter.OracleParameterUtils;
import oracle.jdbc.OracleConnection;

import java.util.Map;

/**
 * @author umitgunduz
 */
public abstract class OracleFunctionImpl<T extends OracleFunctionParameter>
        implements OracleFunction<T> {

    @Override
    public T execute(T parameters) throws Exception {
        OracleConnection connection = OracleConnectionManager.getInstance().getConnection();
        String commandText = null;

        Map map = connection.getTypeMap();
        map.putAll(this.getTypeMap());
        connection.setTypeMap(map);

        OracleDatabaseParameterCollection databaseParameters = OracleParameterUtils.convert(parameters);
        OracleCommand command = new OracleCommand(commandText, connection);
        command.AddParameters(databaseParameters);
        command.execute();
        databaseParameters = (OracleDatabaseParameterCollection) command.getParameters();
        OracleParameterUtils.bind(databaseParameters, parameters);
        
        return parameters;
    }
}

