/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import oracle.jdbc.OracleConnection;

import java.util.Map;

/**
 *
 * @author umitgunduz
 * @param <In>
 * @param <Out>
 */
public abstract class Function<In extends IFunctionInput, Out extends IFunctionOutput>
        implements IFunction<In, Out> {

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

