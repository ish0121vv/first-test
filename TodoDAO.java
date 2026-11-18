package todoapp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

    // todos with type_name and subtype_name using LEFT JOIN
    public List<TodoItem> getAll() {
        List<TodoItem> list = new ArrayList<>();
        String sql = "SELECT t.todo_id, t.title, t.description, t.due_date, " +
                     "t.type_id, tt.type_name, t.subtype_id, st.subtype_name, " +
                     "t.priority_id, t.is_completed " +
                     "FROM todo t " +
                     "LEFT JOIN task_type tt ON t.type_id = tt.type_id " +
                     "LEFT JOIN task_subtype st ON t.subtype_id = st.subtype_id " +
                     "ORDER BY t.due_date ASC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Date d = rs.getDate("due_date");
                String due = (d == null) ? "" : d.toString();
                list.add(new TodoItem(
                        rs.getInt("todo_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        due,
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getInt("subtype_id"),
                        rs.getString("subtype_name"),
                        rs.getInt("priority_id"),
                        rs.getInt("is_completed") == 1
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(TodoItem item) {
        String sql = "INSERT INTO todo (title, description, due_date, type_id, subtype_id, priority_id, is_completed) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getTitle());
            ps.setString(2, item.getDescription());

            // dueDate may be empty -> setNull, else Date.valueOf
            if (item.getDueDate() == null || item.getDueDate().trim().isEmpty()) {
                ps.setNull(3, java.sql.Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(item.getDueDate()));
            }

            ps.setInt(4, item.getTypeId());
            ps.setInt(5, item.getSubtypeId());
            ps.setInt(6, item.getPriority());
            ps.setInt(7, item.isCompleted() ? 1 : 0);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(TodoItem item) {
        String sql = "UPDATE todo SET title=?, description=?, due_date=?, type_id=?, subtype_id=?, priority_id=?, is_completed=? " +
                     "WHERE todo_id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getTitle());
            ps.setString(2, item.getDescription());

            if (item.getDueDate() == null || item.getDueDate().trim().isEmpty()) {
                ps.setNull(3, java.sql.Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(item.getDueDate()));
            }

            ps.setInt(4, item.getTypeId());
            ps.setInt(5, item.getSubtypeId());
            ps.setInt(6, item.getPriority());
            ps.setInt(7, item.isCompleted() ? 1 : 0);
            ps.setInt(8, item.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM todo WHERE todo_id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
