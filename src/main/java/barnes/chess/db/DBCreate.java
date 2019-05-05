package barnes.chess.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCreate {
    private final String HOST = "jdbc:postgresql://142.93.101.191:5432/dbs";
    private final String USERNAME = "dbs";
    private final String PASSWORD = "BkpKuHCEN9myIn3Q";
    private Connection connection;
    public DBCreate(){
        try {
            connection = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Connection getConnection(){
        return connection;
    }
}
