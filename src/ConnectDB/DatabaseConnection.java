package ConnectDB;
//Người làm Phạm Thanh Huy
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Database credentials
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyQuanCfs;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";

    // Private constructor để ngăn việc tạo instance trực tiếp
    private DatabaseConnection() {
        try {
            // Load SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Phương thức static để lấy instance của class
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Phương thức để lấy connection
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully");
            } catch (SQLException e) {
                System.err.println("Error connecting to database: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    // Phương thức đóng kết nối
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // Phương thức kiểm tra kết nối
    public boolean testConnection() {
        try {
            getConnection();
            return !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
}
