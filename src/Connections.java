import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connections {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "Bank";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Vaibhav@19";

    public static Connection makeConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // Check if the database exists, create if not
            createDatabaseIfNotExists(connection);

            // Switch to the created database
            connection = DriverManager.getConnection(URL + DATABASE_NAME, USERNAME, PASSWORD);

            // Check if the required tables exist, create if not
            createTablesIfNotExists(connection);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void createDatabaseIfNotExists(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            statement.executeUpdate(createDatabaseQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTablesIfNotExists(Connection connection) {
        createTableIfNotExists(connection, "user",
                "sl_no INT AUTO_INCREMENT PRIMARY KEY, "
                        + "first_name VARCHAR(50), "
                        + "last_name VARCHAR(50), "
                        + "Adhar_no BIGINT, "
                        + "Account_No BIGINT, "
                        + "email VARCHAR(100), "
                        + "password VARCHAR(50), "
                        + "phone_No BIGINT, "
                        + "gender VARCHAR(10), "
                        + "Dob DATE");

        createTableIfNotExists(connection, "transaction",
                "sl_no INT AUTO_INCREMENT PRIMARY KEY, "
                        + "id INT, "
                        + "purpose VARCHAR(255), "
                        + "date DATETIME, "
                        + "debit DOUBLE, "
                        + "credit DOUBLE");

        createTableIfNotExists(connection, "total",
                "sl_no INT AUTO_INCREMENT PRIMARY KEY, "
                        + "id INT, "
                        + "total DOUBLE");
    }

    private static void createTableIfNotExists(Connection connection, String tableName, String columns) {
        try {
            Statement statement = connection.createStatement();
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columns + ")";
            statement.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
