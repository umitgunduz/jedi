/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle.procedure;

import com.jedi.oracle.OracleCommand;
import com.jedi.oracle.OracleConnectionManager;
import oracle.jdbc.OracleConnection;

import java.util.Map;

/**
 * @author umitgunduz
 */
public abstract class OracleProcedureImpl<T extends OracleProcedureParameter>
        implements OracleProcedure<T> {

    @Override
    public T execute(T parameters) throws Exception {
        OracleConnection connection = OracleConnectionManager.getInstance().getConnection();
        String commandText = null;

        Map map = connection.getTypeMap();
        map.putAll(this.getTypeMap());
        connection.setTypeMap(map);

        OracleCommand command = new OracleCommand(commandText, connection);
        command.execute();

        return parameters;
    }
}

