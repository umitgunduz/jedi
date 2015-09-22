/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import oracle.jdbc.OracleConnection;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author umitgunduz
 */
public class OracleConnectionManager {

    private final String DEFAULT="DEFAULT";
    
    private final Map<String, OracleConnection> connections = new HashMap<String, OracleConnection>();
    
    
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
    

    public void registerConnection(OracleConnection connection) {
        connections.put(DEFAULT, connection);
    }
    
    public void registerConnection(String name, OracleConnection connection) {
        connections.put(name, connection);
    }
    
    public OracleConnection getConnection(){
        return connections.get(DEFAULT);
    }
}

