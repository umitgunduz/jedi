/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.common;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author umitgunduz
 */
public class DataSourceManager {

    private final String DEFAULT = "DEFAULT";

    private final Map<String, DataSource> connections = new HashMap<String, DataSource>();


    private static final Object lock = new Object();
    private static volatile DataSourceManager instance;

    public static DataSourceManager getInstance() {
        DataSourceManager r = instance;
        if (r == null) {
            synchronized (lock) {    // While we were waiting for the lock, another 
                r = instance;        // thread may have instantiated the object.
                if (r == null) {
                    r = new DataSourceManager();
                    instance = r;
                }
            }
        }
        return r;
    }


    public void registerDataSource(DataSource dataSource) {
        connections.put(DEFAULT, dataSource);
    }

    public void registerDataSource(String name, DataSource dataSource) {
        connections.put(name, dataSource);
    }

    public DataSource getDataSource() throws SQLException, NamingException {
        return connections.get(DEFAULT);
    }

    public DataSource getDataSource(String name) throws SQLException, NamingException {
        return connections.get(name);
    }
}

