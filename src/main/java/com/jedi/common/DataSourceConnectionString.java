package com.jedi.common;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by umit on 26/09/15.
 */
public class DataSourceConnectionString implements ConnectionString {
    private String dataSourceName;


    @Override
    public Connection getConnection() throws NamingException, SQLException {
        javax.naming.Context initialContext = new javax.naming.InitialContext();
        javax.sql.DataSource dataSource = (javax.sql.DataSource) initialContext.lookup(this.dataSourceName);
        return dataSource.getConnection();
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
}
