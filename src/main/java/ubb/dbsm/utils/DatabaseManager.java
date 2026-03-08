package ubb.dbsm.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    private final Properties properties = Config.getProperties();

    public Connection getConnection() throws SQLException {
        String url = properties.getProperty("DB_URL");
        String user = properties.getProperty("DB_USER");
        String password = properties.getProperty("DB_PASS");

        return DriverManager.getConnection(url, user, password);
    }
}