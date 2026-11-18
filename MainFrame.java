package todoapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton addBtn, editBtn, delBtn, reloadBtn;

    private final TodoController todoController = new TodoController();
    private final TypeController typeController = new TypeController();

    public MainFrame() {
        setTitle("Todo List (MySQL 연동) - MVC");
        setSize(760, 460);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[] { "완료", "ID", "제목", "마감일", "중요도", "대분류", "세부분류" }, 0);
        table = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }
        };

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addBtn = new JButton("추가");
        editBtn = new JButton("수정");
        delBtn = new JButton("삭제");
        reloadBtn = new JButton("새로고침");

        topPanel.add(addBtn);
        topPanel.add(editBtn);
        topPanel.add(delBtn);
        topPanel.add(reloadBtn);
        add(topPanel, BorderLayout.NORTH);

        addBtn.addActionListener(e -> openAddDialog());
        editBtn.addActionListener(e -> openEditDialog());
        delBtn.addActionListener(e -> deleteSelected());
        reloadBtn.addActionListener(e -> reloadTable());

        // 초기 로드
        reloadTable();
    }

    private void openAddDialog() {
        TodoDialog dialog = new TodoDialog(this, "추가", typeController);
        dialog.getSaveButton().addActionListener(e -> {
            TodoItem item = dialog.getTodoItem();
            if (item != null) {
                todoController.addTodo(item);
                reloadTable();
                dialog.dispose();
            }
        });
        dialog.setVisible(true);
    }

    private void openEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "수정할 항목을 선택하세요.");
            return;
        }
        // model value: ID is at column 1
        int id = (int) model.getValueAt(row, 1);

        List<TodoItem> todos = todoController.getTodos();
        for (TodoItem t : todos) {
            if (t.getId() == id) {
                TodoDialog dialog = new TodoDialog(this, "수정", typeController);
                dialog.setFieldData(t);
                dialog.getSaveButton().addActionListener(ev -> {
                    TodoItem edited = dialog.getTodoItem();
                    if (edited != null) {
                        // preserve id
                        TodoItem updated = new TodoItem(id, edited.getTitle(), edited.getDescription(),
                                edited.getDueDate(), edited.getTypeId(), edited.getTypeName(),
                                edited.getSubtypeId(), edited.getSubtypeName(), edited.getPriority(),
                                edited.isCompleted());
                        todoController.updateTodo(updated);
                        reloadTable();
                        dialog.dispose();
                    }
                });
                dialog.setVisible(true);
                break;
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 항목을 선택하세요.");
            return;
        }
        int id = (int) model.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            todoController.deleteTodo(id);
            reloadTable();
        }
    }

    public void reloadTable() {
        model.setRowCount(0);
        List<TodoItem> todos = todoController.getTodos();
        for (TodoItem t : todos) {
            model.addRow(new Object[] {
                    t.isCompleted(),
                    t.getId(),
                    t.getTitle(),
                    t.getDueDate(),
                    String.valueOf(t.getPriority()),
                    t.getTypeName() == null ? "" : t.getTypeName(),
                    t.getSubtypeName() == null ? "" : t.getSubtypeName()
            });
        }
    }
}
