package todoapp;

import java.sql.Connection;
import java.sql.Date; // java.sql.Date만 import
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static final String URL = "jdbc:mysql://localhost:3306/todo_db?serverTimezone=Asia/Seoul";
    private static final String USER = "root";
    private static final String PW = "wnsgud15#";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PW);
    }

    // 대분류 로드
    public static List<TypeItem> getTypes() {
        List<TypeItem> list = new ArrayList<>();
        String sql = "SELECT type_id, type_name FROM task_type";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TypeItem(rs.getInt("type_id"), rs.getString("type_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 소분류 로드(대분류 기반)
    public static List<SubtypeItem> getSubtypesByType(int typeId) {
        List<SubtypeItem> list = new ArrayList<>();
        String sql = "SELECT subtype_id, subtype_name FROM task_subtype WHERE type_id = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new SubtypeItem(rs.getInt("subtype_id"), rs.getString("subtype_name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<TodoItem> getTodos() {
        List<TodoItem> list = new ArrayList<>();

        String sql = "SELECT todo_id, title, description, due_date, " +
                "type_id, subtype_id, priority_id, is_completed, created_at " +
                "FROM todo " +
                "ORDER BY due_date ASC";

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new TodoItem(
                        rs.getInt("todo_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("due_date") != null
                                ? rs.getDate("due_date").toString()
                                : null,
                        rs.getInt("type_id"),
                        null, // typeName (not fetched)
                        rs.getInt("subtype_id"),
                        null, // subtypeName (not fetched)
                        rs.getInt("priority_id"),
                        rs.getInt("is_completed") == 1,
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toString() : null));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Todo 추가
    public static void addTodo(TodoItem item) {
        String sql = "INSERT INTO todo (title, description, due_date, type_id, subtype_id, priority_id, is_completed) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getTitle());
            ps.setString(2, item.getDescription());

            // 문자열 → java.sql.Date
            ps.setDate(3, Date.valueOf(item.getDueDate()));

            ps.setInt(4, item.getTypeId());
            ps.setInt(5, item.getSubtypeId());
            ps.setInt(6, item.getPriority());
            ps.setInt(7, item.isCompleted() ? 1 : 0);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Todo 수정
    public static void updateTodo(TodoItem item) {
        String sql = "UPDATE todo SET title=?, description=?, due_date=?, type_id=?, subtype_id=?, priority_id=?, is_completed=? "
                + "WHERE todo_id=?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getTitle());
            ps.setString(2, item.getDescription());

            ps.setDate(3, Date.valueOf(item.getDueDate())); // 문자열 → java.sql.Date

            ps.setInt(4, item.getTypeId());
            ps.setInt(5, item.getSubtypeId());
            ps.setInt(6, item.getPriority());
            ps.setInt(7, item.isCompleted() ? 1 : 0);
            ps.setInt(8, item.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Todo 삭제
    public static void deleteTodo(int id) {
        String sql = "DELETE FROM todo WHERE todo_id=?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
