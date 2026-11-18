package todoapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SubtypeDAO {
    public List<SubtypeItem> getByTypeId(int typeId) {
        List<SubtypeItem> list = new ArrayList<>();
        String sql = "SELECT subtype_id, subtype_name FROM task_subtype WHERE type_id = ? ORDER BY subtype_id";
        try (Connection con = DBUtil.getConnection();
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
}
