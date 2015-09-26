/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import com.jedi.common.SQLQuery;

/**
 * @author umitgunduz
 */
public interface OracleQuery<T extends OracleQueryParameters>
        extends SQLQuery {

    T execute(T parameters) throws Exception;
}

