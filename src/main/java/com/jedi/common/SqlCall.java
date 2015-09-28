/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.common;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author umitgunduz
 */
public interface SqlCall<T extends SqlParameters> {

    T execute(T parameters) throws Exception;

    T execute(Connection connection, T parameters) throws Exception;

    T execute(DataSource dataSource, T parameters) throws Exception;
}

