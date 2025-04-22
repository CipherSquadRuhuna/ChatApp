package client.gui.user;

import Utility.UserService;
import client.gui.common.AppScreen;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class UserRegisterScreen extends JPanel {

    private final AppScreen screen;
    //chatfrom
    private JTextField emailField, usernameField, nicknameField;
    private JPasswordField passwordField;
    private JLabel profilePicLabel;
    private File selectedFile;

    public UserRegisterScreen(AppScreen screen) {
        this.screen = screen;
        initialize();

    }

    public void initialize() {
        screen.setTitle("User Registration");
        setSize(400, 500);
        screen.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); // use layout directly on frame

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // padding around components
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0; // row counter

        // Email
        add(new JLabel("Email:"), getConstraints(0, y));
        emailField = new JTextField(20);
        add(emailField, getConstraints(1, y++));

        // Username
        add(new JLabel("Username:"), getConstraints(0, y));
        usernameField = new JTextField(20);
        add(usernameField, getConstraints(1, y++));

        // Password
        add(new JLabel("Password:"), getConstraints(0, y));
        passwordField = new JPasswordField(20);
        add(passwordField, getConstraints(1, y++));

        // Nick Name
        add(new JLabel("Nick Name:"), getConstraints(0, y));
        nicknameField = new JTextField(20);
        add(nicknameField, getConstraints(1, y++));

        // Profile Picture
        add(new JLabel("Profile Picture:"), getConstraints(0, y));
        profilePicLabel = new JLabel("No file chosen");
        JButton browseButton = new JButton("Browse");

        JPanel profilePicPanel = new JPanel(new BorderLayout(5, 0));
        profilePicPanel.add(profilePicLabel, BorderLayout.CENTER);
        profilePicPanel.add(browseButton, BorderLayout.EAST);

        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                profilePicLabel.setText(selectedFile.getName());
            }
        });

        add(profilePicPanel, getConstraints(1, y++));

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(registerButton, gbc);

//        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Reusable method to generate constraints
    private GridBagConstraints getConstraints(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        return gbc;
    }


    private void registerUser() {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String nickname = nicknameField.getText();
        String profilePicPath = (selectedFile != null) ? selectedFile.getAbsolutePath() : "No picture";

        // You can now send this data to the server, store it using Hibernate, etc.
        JOptionPane.showMessageDialog(this, "Registered User:" +
                "\nEmail: " + email +
                "\nUsername: " + username +
                "\nNickname: " + nickname +
                "\nProfile Pic: " +
                profilePicPath);


        // Input validation
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields except Profile Picture are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //  Create User object
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password); // Consider hashing in production!
        user.setNickName(nickname);
        user.setProfilePicturePath(profilePicPath);

        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();







        JOptionPane.showMessageDialog(this, "User registered successfully!");









    }


}

