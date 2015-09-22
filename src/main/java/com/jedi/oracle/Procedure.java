/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import java.util.Map;
import oracle.jdbc.OracleConnection;

/**
 *
 * @author EXT0104423
 * @param <In>
 * @param <Out>
 */
public abstract class Procedure<In extends IProcedureInput, Out extends IProcedureOutput>
        implements IProcedure<In, Out> {

    @Override
    public Out execute(In input) throws Exception {
        Out result = null;
        OracleConnection connection = OracleConnectionManager.getInstance().getConnection();
        String commandText = null;

        Map map = connection.getTypeMap();
        map.putAll(this.getTypeMap());
        connection.setTypeMap(map);

        OracleCommand command = new OracleCommand(commandText, connection);
        command.execute();

        return result;
    }
}
