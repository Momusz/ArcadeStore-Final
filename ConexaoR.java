import java.sql.*;

public class ConexaoR {
    private static final String url = "jdbc:mysql://localhost:3306/Projeto";
    private static final String user = "root";
    private static final String pass = "admin";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}