/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import com.jedi.common.ConnectionString;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author umitgunduz
 */
public class OracleConnectionManager {

    private final String DEFAULT = "DEFAULT";

    private final Map<String, ConnectionString> connections = new HashMap<String, ConnectionString>();


    private static final Object lock = new Object();
    private static volatile OracleConnectionManager instance;

    public static OracleConnectionManager getInstance() {
        OracleConnectionManager r = instance;
        if (r == null) {
            synchronized (lock) {    // While we were waiting for the lock, another 
                r = instance;        // thread may have instantiated the object.
                if (r == null) {
                    r = new OracleConnectionManager();
                    instance = r;
                }
            }
        }
        return r;
    }


    public void registerConnectionString(ConnectionString connectionString) {
        connections.put(DEFAULT, connectionString);
    }

    public void registerConnectionString(String name, ConnectionString connectionString) {
        connections.put(name, connectionString);
    }

    public Connection getConnection() throws SQLException, NamingException {
        ConnectionString connectionString = connections.get(DEFAULT);
        return connectionString.getConnection();
    }
}

