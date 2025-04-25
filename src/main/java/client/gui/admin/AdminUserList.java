package client.gui.admin;

import client.gui.admin.common.AdminMenu;
import client.gui.common.AppScreen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminUserList extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;

    public AdminUserList(AppScreen appScreen) {
        setLayout(new BorderLayout());

        // Admin Menu
        AdminMenu menu = new AdminMenu(appScreen);
        add(menu.getMenuBar(), BorderLayout.NORTH);

        // Table and Scroll Pane
        String[] columnNames = {"User ID", "Username", "Email", "Role"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Sample data
        addUserRow("101", "Asela Priyadrshana", "asela@example.com", "User");
        addUserRow("102", "Ashfa Nisthar", "ashfa@example.com", "Admin");
        addUserRow("101", "Ravindu Harshana", "ravindu@example.com", "User");
        addUserRow("102", "Dilukshi Nimasha", "dilukshi@example.com", "User");

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton removeButton = new JButton("Remove Selected User");

        removeButton.addActionListener((ActionEvent e) -> removeSelectedUser());

        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addUserRow(String id, String username, String email, String role) {
        tableModel.addRow(new Object[]{id, username, email, role});
    }

    private void removeSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove this user?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to remove.");
        }
    }
}
