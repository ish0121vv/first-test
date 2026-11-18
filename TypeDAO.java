package todoapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TypeDAO {
    public List<TypeItem> getAllTypes() {
        List<TypeItem> list = new ArrayList<>();
        String sql = "SELECT type_id, type_name FROM task_type ORDER BY type_id";
        try (Connection con = DBUtil.getConnection();
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
}
