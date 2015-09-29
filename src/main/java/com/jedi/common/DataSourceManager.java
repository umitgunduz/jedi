/*
 *
 *  * Copyright 2015 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
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

    private final Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();


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
        dataSourceMap.put(DEFAULT, dataSource);
    }

    public void registerDataSource(String name, DataSource dataSource) {
        dataSourceMap.put(name, dataSource);
    }

    public DataSource getDataSource() throws SQLException, NamingException {
        return dataSourceMap.get(DEFAULT);
    }

    public DataSource getDataSource(String name) throws SQLException, NamingException {
        return dataSourceMap.get(name);
    }
}

