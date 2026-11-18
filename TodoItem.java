package todoapp;

public class TodoItem {
    private int id;
    private String title;
    private String description;
    private String dueDate; // "YYYY-MM-DD" or null/empty
    private int typeId;
    private String typeName;
    private int subtypeId;
    private String subtypeName;
    private int priority;
    private boolean completed;
    private String createdAt;

    public TodoItem(int id, String title, String description, String dueDate,
            int typeId, String typeName, int subtypeId, String subtypeName,
            int priority, boolean completed, String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.typeId = typeId;
        this.typeName = typeName;
        this.subtypeId = subtypeId;
        this.subtypeName = subtypeName;
        this.priority = priority;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    public TodoItem(int id, String title, String description, String dueDate,
            int typeId, String typeName, int subtypeId, String subtypeName,
            int priority, boolean completed) {
        this(id, title, description, dueDate, typeId, typeName, subtypeId, subtypeName, priority, completed, null);
    }

    // getters / setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getSubtypeId() {
        return subtypeId;
    }

    public String getSubtypeName() {
        return subtypeName;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
