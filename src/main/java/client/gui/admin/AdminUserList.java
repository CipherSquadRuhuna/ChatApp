package client.gui.admin;

import client.gui.admin.common.AdminMenu;
import client.gui.common.AppScreen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class AdminUserList extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/chat_application";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public AdminUserList(AppScreen appScreen) {
        setLayout(new BorderLayout());

        // Admin Menu
        AdminMenu menu = new AdminMenu(appScreen);
        add(menu.getMenuBar(), BorderLayout.NORTH);

        // Table and Scroll Pane
        String[] columnNames = {"User ID", "Email", "Username", "Nick Name"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Retrieve user data from the database and populate the table
        retrieveUserData();

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Remove Button
        JButton removeButton = new JButton("Remove Selected User");
        removeButton.addActionListener((ActionEvent e) -> removeSelectedUser());
        buttonPanel.add(removeButton);

        // Edit Button
        JButton editButton = new JButton("Edit Selected User");
        editButton.addActionListener((ActionEvent e) -> editSelectedUser());
        buttonPanel.add(editButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addUserRow(String id, String email, String username, String nickName) {
        tableModel.addRow(new Object[]{id, email, username, nickName});
    }

    private void removeSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Get user ID of the selected row
            String userId = (String) userTable.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove this user?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                // Delete user from the database
                boolean isDeleted = deleteUserFromDatabase(userId);
                if (isDeleted) {
                    // Remove row from table if deletion is successful
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "User deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting user from database.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to remove.");
        }
    }

    private void editSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Get user ID and current details of the selected row
            String userId = (String) userTable.getValueAt(selectedRow, 0);
            String currentEmail = (String) userTable.getValueAt(selectedRow, 1);
            String currentUsername = (String) userTable.getValueAt(selectedRow, 2);
            String currentNickName = (String) userTable.getValueAt(selectedRow, 3);

            // Show the Edit dialog
            JTextField emailField = new JTextField(currentEmail);
            JTextField usernameField = new JTextField(currentUsername);
            JTextField nickNameField = new JTextField(currentNickName);

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Username:"));
            panel.add(usernameField);
            panel.add(new JLabel("Nick Name:"));
            panel.add(nickNameField);

            int option = JOptionPane.showConfirmDialog(this, panel, "Edit User", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                // Retrieve the new values
                String newEmail = emailField.getText();
                String newUsername = usernameField.getText();
                String newNickName = nickNameField.getText();

                // Update user in the database
                boolean isUpdated = updateUserInDatabase(userId, newEmail, newUsername, newNickName);
                if (isUpdated) {
                    // Update the table row if the update is successful
                    userTable.setValueAt(newEmail, selectedRow, 1);
                    userTable.setValueAt(newUsername, selectedRow, 2);
                    userTable.setValueAt(newNickName, selectedRow, 3);
                    JOptionPane.showMessageDialog(this, "User updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating user in database.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        }
    }

    // Method to delete user from the database
    private boolean deleteUserFromDatabase(String userId) {
        String deleteQuery = "DELETE FROM `users` WHERE `user_id` = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setString(1, userId); // Set the user_id parameter for the query
            int rowsAffected = pstmt.executeUpdate();

            // Return true if the user was deleted successfully
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }

    // Method to update user in the database
    private boolean updateUserInDatabase(String userId, String newEmail, String newUsername, String newNickName) {
        String updateQuery = "UPDATE `users` SET `email` = ?, `username` = ?, `nick_name` = ? WHERE `user_id` = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, newEmail);
            pstmt.setString(2, newUsername);
            pstmt.setString(3, newNickName);
            pstmt.setString(4, userId); // Set the user_id parameter for the query
            int rowsAffected = pstmt.executeUpdate();

            // Return true if the user was updated successfully
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }

    // Method to retrieve user data from the database
    private void retrieveUserData() {
        String query = "SELECT `user_id`, `email`, `username`, `nick_name` FROM `users`"; // Corrected SQL query
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Loop through the result set and add each row to the table model
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String nickName = rs.getString("nick_name");

                addUserRow(userId, email, username, nickName);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
