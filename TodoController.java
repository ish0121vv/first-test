package todoapp;

import java.util.List;

public class TodoController {
    private final TodoDAO todoDAO = new TodoDAO();

    public List<TodoItem> getTodos() {
        return todoDAO.getAll();
    }

    public void addTodo(TodoItem item) {
        todoDAO.insert(item);
    }

    public void updateTodo(TodoItem item) {
        todoDAO.update(item);
    }

    public void deleteTodo(int id) {
        todoDAO.delete(id);
    }
}
