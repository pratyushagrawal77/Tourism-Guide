import java.sql.*;

public class DatabaseConnector {
    private Connection connection;
    private String url;
    private String username;
    private String password;

    // Constructor to initialize database connection parameters
    public DatabaseConnector(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // Method to establish connection to the database
    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    // Method to retrieve the established connection
    public Connection getConnection() {
        return connection;
    }

    // Method to close database connection
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    // Method to execute SQL query and return ResultSet
    public ResultSet executeQuery(String sqlQuery) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlQuery);
    }
}
