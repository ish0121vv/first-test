package todoapp;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TodoDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descArea;
    private JTextField dueDateField; // "YYYY-MM-DD" or empty
    private JComboBox<TypeItem> typeCombo;
    private JComboBox<SubtypeItem> subtypeCombo;
    private JComboBox<String> priorityCombo;
    private JCheckBox completedCheck;
    private JButton saveBtn, cancelBtn;

    private final TypeController typeController;

    public TodoDialog(JFrame parent, String title, TypeController typeController) {
        super(parent, title, true);
        this.typeController = typeController;

        setLayout(new BorderLayout());
        setSize(420, 420);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        panel.add(new JLabel("제목"));
        titleField = new JTextField();
        panel.add(titleField);

        panel.add(new JLabel("설명"));
        descArea = new JTextArea(3, 20);
        panel.add(new JScrollPane(descArea));

        panel.add(new JLabel("마감일 (YYYY-MM-DD)"));
        dueDateField = new JTextField();
        panel.add(dueDateField);

        panel.add(new JLabel("대분류"));
        typeCombo = new JComboBox<>();
        panel.add(typeCombo);

        panel.add(new JLabel("소분류"));
        subtypeCombo = new JComboBox<>();
        panel.add(subtypeCombo);

        panel.add(new JLabel("중요도"));
        priorityCombo = new JComboBox<>(new String[]{"1", "2", "3"});
        panel.add(priorityCombo);

        panel.add(new JLabel("완료"));
        completedCheck = new JCheckBox();
        panel.add(completedCheck);

        add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveBtn = new JButton("저장");
        cancelBtn = new JButton("취소");
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        cancelBtn.addActionListener(e -> dispose());

        typeCombo.addActionListener(e -> {
            TypeItem selected = (TypeItem) typeCombo.getSelectedItem();
            if (selected != null) {
                loadSubtypes(selected.getId());
            }
        });

        loadTypes();
    }

    public JButton getSaveButton() {
        return saveBtn;
    }

    public void setFieldData(TodoItem item) {
        titleField.setText(item.getTitle());
        descArea.setText(item.getDescription());
        dueDateField.setText(item.getDueDate());
        completedCheck.setSelected(item.isCompleted());
        priorityCombo.setSelectedItem(String.valueOf(item.getPriority()));

        // set type selection
        for (int i = 0; i < typeCombo.getItemCount(); i++) {
            if (typeCombo.getItemAt(i).getId() == item.getTypeId()) {
                typeCombo.setSelectedIndex(i);
                break;
            }
        }

        loadSubtypes(item.getTypeId());
        for (int i = 0; i < subtypeCombo.getItemCount(); i++) {
            if (subtypeCombo.getItemAt(i).getId() == item.getSubtypeId()) {
                subtypeCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    public TodoItem getTodoItem() {
        String title = titleField.getText().trim();
        String desc = descArea.getText().trim();
        String due = dueDateField.getText().trim(); // can be empty
        boolean completed = completedCheck.isSelected();
        int priority = Integer.parseInt((String) priorityCombo.getSelectedItem());

        TypeItem type = (TypeItem) typeCombo.getSelectedItem();
        SubtypeItem subtype = (SubtypeItem) subtypeCombo.getSelectedItem();

        if (title.isEmpty() || type == null || subtype == null) {
            JOptionPane.showMessageDialog(this, "제목, 대분류, 소분류는 필수입니다.");
            return null;
        }

        // due may be empty string -> DAO handles conversion to NULL
        return new TodoItem(
                0,
                title,
                desc,
                due,
                type.getId(),
                type.getName(),
                subtype.getId(),
                subtype.getName(),
                priority,
                completed
        );
    }

    private void loadTypes() {
        typeCombo.removeAllItems();
        java.util.List<TypeItem> types = typeController.getAllTypes();
        for (TypeItem t : types) typeCombo.addItem(t);
        if (!types.isEmpty()) {
            loadSubtypes(types.get(0).getId());
        }
    }

    private void loadSubtypes(int typeId) {
        subtypeCombo.removeAllItems();
        List<SubtypeItem> subs = typeController.getSubtypesByType(typeId);
        for (SubtypeItem s : subs) subtypeCombo.addItem(s);
    }
}
