package client.gui.user;

import client.gui.utility.UserService;
import client.gui.common.AppScreen;
import common.HibernateUtil;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserLoginScreen extends JPanel {
    private JTextField username;
    private JPasswordField password;
    private JButton login;
    private AppScreen userScreen;

    public UserLoginScreen(AppScreen userScreen) {
        this.userScreen = userScreen;
        initComponents();
    }

    private void initComponents() {
//        //set layout

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60)); // Padding

        // Title
        JLabel title = new JLabel("Chat App Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(33, 33, 33));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 30)));

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(usernameLabel);

        username = new JTextField(20);
        username.setMaximumSize(new Dimension(300, 40));
        username.setFont(new Font("SansSerif", Font.PLAIN, 16));
        username.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(username);

        add(Box.createRigidArea(new Dimension(0, 20)));

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(passwordLabel);

        password = new JPasswordField(20);
        password.setMaximumSize(new Dimension(300, 40));
        password.setFont(new Font("SansSerif", Font.PLAIN, 16));
        password.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(password);

        add(Box.createRigidArea(new Dimension(0, 30)));

        // Login Button
        login = new JButton("Login");
        login.setPreferredSize(new Dimension(150, 40));
        login.setMaximumSize(new Dimension(150, 40));
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        login.setBackground(new Color(33, 150, 243));
        login.setForeground(Color.WHITE);
        login.setFont(new Font("SansSerif", Font.BOLD, 16));
        login.setFocusPainted(false);
        add(login);

        add(Box.createRigidArea(new Dimension(0, 15)));

        // Register Button
        JButton register = new JButton("Register");
        register.setPreferredSize(new Dimension(150, 40));
        register.setMaximumSize(new Dimension(150, 40));
        register.setAlignmentX(Component.CENTER_ALIGNMENT);
        register.setBackground(new Color(76, 175, 80));
        register.setForeground(Color.WHITE);
        register.setFont(new Font("SansSerif", Font.BOLD, 16));
        register.setFocusPainted(false);
        add(register);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = username.getText();
                String enteredPassword = new String(password.getPassword());

                if (enteredUsername.trim().isEmpty() || enteredPassword.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both username and password.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                UserService userService = new UserService();
                models.User loggedUser = userService.userlogin(enteredUsername, enteredPassword);

                if (loggedUser != null) {
                    System.out.println(loggedUser.getId() + " " + loggedUser.getNickName());
//                    JOptionPane.showMessageDialog(
//                            null,
//                            "Login successful! Welcome " + loggedUser.getUsername(),
//                            "Success", // Title of the dialog
//                            JOptionPane.INFORMATION_MESSAGE // Message type for icon
//                    );


                    EntityManager em = HibernateUtil.getEmf().createEntityManager();
                    models.User logedUser = em.find(models.User.class, loggedUser.getId());

                    userScreen.setUser(logedUser);

                    if (Boolean.TRUE.equals(logedUser.getIsAdmin())) {
                        userScreen.showAdminChatScreen();
                    } else {
                        userScreen.showUserChatScreen();
                    }

                } else {
                    System.out.println("Login failed: invalid credentials");
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }

        // show chat screen

            }
        });


        register.addActionListener((e)->{
            userScreen.showUserRegisterScreen();
        });
    }

}
