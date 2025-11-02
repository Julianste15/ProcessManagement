package co.unicauca.domain.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public interface iConnectionDB {

    void connect();

    void disconnect();

    PreparedStatement getStatement(String prmSql)throws SQLException;;

    Statement createStatement()throws SQLException;;
    Connection getConnection();
}
