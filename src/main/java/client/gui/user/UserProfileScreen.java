package client.gui.user;

import client.User;
import client.gui.common.AppScreen;
import client.gui.user.common.UserMenu;
import Utility.UserService;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class UserProfileScreen extends UserMenu {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nicknameField;

    private JLabel displayNameLabel;
    private JLabel displayUsernameLabel;
    private JLabel displayNicknameLabel;

    private final AppScreen screen;
    private models.User user;

    public UserProfileScreen(AppScreen screen) {
        super(screen);
        this.screen = screen;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        // Add menu bar
        add(super.menuBar, BorderLayout.NORTH);

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Display Section
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        displayPanel.setBorder(BorderFactory.createTitledBorder("Current Profile"));

        displayNameLabel = new JLabel("Name: ");
        displayUsernameLabel = new JLabel("Username: ");
        displayNicknameLabel = new JLabel("Nickname: ");

        displayPanel.add(displayNameLabel);
        displayPanel.add(displayUsernameLabel);
        displayPanel.add(displayNicknameLabel);

        containerPanel.add(displayPanel);
        containerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Update Form Section
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Update Profile"));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        nicknameField = new JTextField(20);

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formPanel.add(new JLabel("Nickname:"));
        formPanel.add(nicknameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateProfile());
        formPanel.add(updateButton);

        containerPanel.add(formPanel);

        add(containerPanel, BorderLayout.CENTER);
    }

    public void loadUser(User user) {
        this.user = user;

        // Display info
        displayNameLabel.setText("Name: " + user.getName());
        displayUsernameLabel.setText("Username: " + user.getUsername());
        displayNicknameLabel.setText("Nickname: " + user.getNickname());

        // Clear form fields
        usernameField.setText("");
        passwordField.setText("");
        nicknameField.setText("");
    }

    private void updateProfile() {
        if (user != null) {
            try {
                String newUsername = usernameField.getText().trim();
                String newPassword = new String(passwordField.getPassword()).trim();
                String newNickname = nicknameField.getText().trim();

                // Update user fields
                user.setUsername(newUsername);
                user.setPassword(newPassword);
                user.setNickname(newNickname);

                // Update labels
                displayUsernameLabel.setText("Username: " + newUsername);
                displayNicknameLabel.setText("Nickname: " + newNickname);

                // Update in database
                new UserService().updateUser(user);

//                // Optionally save to file
//                saveUserToFile(user);

                // Clear fields
                usernameField.setText("");
                passwordField.setText("");
                nicknameField.setText("");

                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Save data to a file
//    private void saveUserToFile(User user) {
//        try (FileWriter writer = new FileWriter("users.txt", true)) {
//            writer.write("Username: " + user.getUsername() + ", ");
//            writer.write("Password: " + user.getPassword() + ", ");
//            writer.write("Nickname: " + user.getNickname() + "\n");
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error saving user: " + e.getMessage());
//        }
    }

