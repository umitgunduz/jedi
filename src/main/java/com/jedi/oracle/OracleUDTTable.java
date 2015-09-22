package com.jedi.oracle;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Created by umit on 22/09/15.
 */
public class OracleUDTTable<T> implements SQLData, IOracleCustomType {
    @Override
    public String getSQLTypeName() throws SQLException {
        return null;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {

    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {

    }
}
