package client.gui.user;

import client.User;
import client.gui.common.AppScreen;
import client.gui.user.common.UserMenu;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class UserProfileScreen extends UserMenu {
    private JTextField nameField;
    private JTextField usernameField;
    private JTextField nicknameField;


    private JLabel displayNameLabel;
    private JLabel displayUsernameLabel;
    private JLabel displayNicknameLabel;

    private final AppScreen screen;
    private User user;



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

        nameField = new JTextField(20);
        usernameField = new JTextField(20);
        nicknameField = new JTextField(20);

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formPanel.add(new JLabel("Nickname:"));
        formPanel.add(nicknameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e ->updateProfile());
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

        // Set values in fields
        nameField.setText("");
        usernameField.setText("");
        nicknameField.setText("");
    }

    private void updateProfile() {
        if (user != null) {
            try {
                String newName = nameField.getText().trim();
                String newUsername = usernameField.getText().trim();
                String newNickname = nicknameField.getText().trim();

                user.setName(newName);
                user.setUsername(newUsername);
                user.setNickname(newNickname);

                // Update display labels
                displayNameLabel.setText("Name: " + newName);
                displayUsernameLabel.setText("Username: " + newUsername);
                displayNicknameLabel.setText("Nickname: " + newNickname);

                // Save to file
                saveUserToFile(user);

                // clear form fields
                nameField.setText("");
                usernameField.setText("");
                nicknameField.setText("");

                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
            }
        }
    }

    //Save data to a file
    private void saveUserToFile(User user) {
        try {
            // You can customize the path as needed
            FileWriter writer = new FileWriter("users.txt", true); // true = append mode

            // Format: Name, Username, Nickname
            writer.write("Name: " + user.getName() + ", ");
            writer.write("Username: " + user.getUsername() + ", ");
            writer.write("Nickname: " + user.getNickname() + "\n");

            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving user: " + e.getMessage());
        }
    }
}
