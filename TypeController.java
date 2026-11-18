package todoapp;

import java.util.List;

public class TypeController {
    private final TypeDAO typeDAO = new TypeDAO();
    private final SubtypeDAO subtypeDAO = new SubtypeDAO();

    public List<TypeItem> getAllTypes() {
        return typeDAO.getAllTypes();
    }

    public List<SubtypeItem> getSubtypesByType(int typeId) {
        return subtypeDAO.getByTypeId(typeId);
    }
}
