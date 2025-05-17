package client.gui.admin;

import client.gui.admin.common.AdminMenu;
import client.gui.common.AppScreen;
import common.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AdminUserList extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;

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

    private void addUserRow(Integer id, String email, String username, String nickName) {
        tableModel.addRow(new Object[]{id.toString(), email, username, nickName});
    }

    private void removeSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String userIdStr = (String) userTable.getValueAt(selectedRow, 0);
            Integer userId = Integer.valueOf(userIdStr);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove this user?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                boolean isDeleted = deleteUserFromDatabase(userId);
                if (isDeleted) {
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
            String userIdStr = (String) userTable.getValueAt(selectedRow, 0);
            Integer userId = Integer.valueOf(userIdStr);
            String currentEmail = (String) userTable.getValueAt(selectedRow, 1);
            String currentUsername = (String) userTable.getValueAt(selectedRow, 2);
            String currentNickName = (String) userTable.getValueAt(selectedRow, 3);

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
                String newEmail = emailField.getText();
                String newUsername = usernameField.getText();
                String newNickName = nickNameField.getText();

                boolean isUpdated = updateUserInDatabase(userId, newEmail, newUsername, newNickName);
                if (isUpdated) {
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

    private boolean deleteUserFromDatabase(Integer userId) {
        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                em.remove(user);
                tx.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    private boolean updateUserInDatabase(Integer userId, String newEmail, String newUsername, String newNickName) {
        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setEmail(newEmail);
                user.setUsername(newUsername);
                user.setNickName(newNickName);
                em.merge(user);
                tx.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    private void retrieveUserData() {
        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            List<User> users = query.getResultList();
            for (User user : users) {
                addUserRow(user.getId(), user.getEmail(), user.getUsername(), user.getNickName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error retrieving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            em.close();
        }
    }
}
