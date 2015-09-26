package com.jedi.common;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by umit on 26/09/15.
 */
public interface ConnectionString {
    Connection getConnection() throws NamingException, SQLException;
}
