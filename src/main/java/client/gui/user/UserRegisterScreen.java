package client.gui.user;

import client.gui.utility.UserService;
import client.gui.common.AppScreen;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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
        screen.setTitle("Register Now");
        screen.setDefaultCloseOperation(EXIT_ON_CLOSE);
        screen.setSize(500, 600);
        setLayout(new BorderLayout());

        // Center panel to hold form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("Register to Chat App");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Email
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);

        // Username
        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Nickname
        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Nick Name:"), gbc);
        gbc.gridx = 1;
        nicknameField = new JTextField(20);
        formPanel.add(nicknameField, gbc);

        // Profile picture
        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Profile Picture:"), gbc);
        gbc.gridx = 1;
        JPanel picPanel = new JPanel(new BorderLayout(5, 0));
        profilePicLabel = new JLabel("No file chosen");
        JButton browseButton = new JButton("Browse");
        picPanel.add(profilePicLabel, BorderLayout.CENTER);
        picPanel.add(browseButton, BorderLayout.EAST);
        formPanel.add(picPanel, gbc);

        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                profilePicLabel.setText(selectedFile.getName());
            }
        });

        // Buttons
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(76, 175, 80)); // Green
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(120, 35));

        JButton exitButton = new JButton("Back to Login");
        exitButton.setBackground(new Color(33, 150, 243)); // Blue
        exitButton.setForeground(Color.WHITE);
        exitButton.setPreferredSize(new Dimension(120, 35));

        registerButton.addActionListener(e -> registerUser());
        exitButton.addActionListener(e -> screen.showLoginScreen());

        buttonPanel.add(exitButton);
        buttonPanel.add(registerButton);

        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void registerUser() {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String nickname = nicknameField.getText();
        String profilePicPath = (selectedFile != null) ? selectedFile.getAbsolutePath() : null;

        // Send this data to the server, store it using Hibernate, etc.
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
        user.setPassword(password);
        user.setNickName(nickname);
        user.setProfilePicturePath(profilePicPath);
        user.setIsAdmin(false);


        UserService userService =new UserService();
        userService.saveUser(user);

        ImageIcon setIcon=new ImageIcon("src/main/java/client/assets/check.png");
        Image scaledImage = setIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH); // Resize to 48x48
        ImageIcon tickIcon = new ImageIcon(scaledImage);
        JOptionPane.showMessageDialog(this, "User registered successfully!",
                "Success", // Title of the dialog
                JOptionPane.INFORMATION_MESSAGE,
                tickIcon
        );
          // directed back to login page
        screen.showLoginScreen();

    }




}



