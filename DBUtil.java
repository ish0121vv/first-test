package todoapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/todo_db?serverTimezone=Asia/Seoul";
    private static final String USER = "root";
    private static final String PW = "wnsgud15#";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PW);
    }
}
