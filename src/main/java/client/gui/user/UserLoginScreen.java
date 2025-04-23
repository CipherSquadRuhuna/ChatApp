package client.gui.user;

import Utility.UserService;
import client.gui.common.AppScreen;
import common.HibernateUtil;
import jakarta.persistence.EntityManager;
import models.User;

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
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//
//        username = new JTextField();
//        username.setMaximumSize(new Dimension(500,50));
//        username.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        password = new JPasswordField();
//        password.setMaximumSize(new Dimension(500,50));
//        password.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        login = new JButton("Login");
//        login.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//
//        // user register button
//        JButton register = new JButton("Register");
//        register.setAlignmentX(Component.CENTER_ALIGNMENT);
//        add(register);
//
//        JLabel usernameLabel = new JLabel("Username:");
//        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        usernameLabel.setMaximumSize(new Dimension(500,50));
//
//        add(usernameLabel);
//        add(username);
//        add(new JLabel("Password:"));
//        add(password);
//        add(login);

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

                // get sample user for testing purpose
                EntityManager em = HibernateUtil.getEmf().createEntityManager();
//                models.User SampleUser = em.find(models.User.class, 3);

                UserService userService = new UserService();

                String enteredUsername = username.getText();
                String enteredPassword = new String(password.getPassword());

                models.User loggedUser = userService.userlogin(enteredUsername, enteredPassword);


                System.out.println(loggedUser.getId() + " " + loggedUser.getNickName());

                if (loggedUser != null) {
                    JOptionPane.showMessageDialog(null, "Login successful! Welcome " + loggedUser.getUsername());

                    // Convert to client.User if needed (depending on your design)
//                    User user = new User(loggedUser.getId(), loggedUser.getNickName());

                    models.User logedUser = em.find(models.User.class, loggedUser.getId());


                    // set the login user status
                    userScreen.setUser(logedUser);
                    // show to chat screen

                    System.out.println(logedUser.getIsAdmin()+logedUser.getNickName());

                    if (Boolean.TRUE.equals(logedUser.getIsAdmin())) {
                        userScreen.showAdminChatScreen();
                    } else if (Boolean.FALSE.equals(logedUser.getIsAdmin())) {
                        userScreen.showUserChatScreen();
                    }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }

                    // show chat screen

            }
        });

//        adminLogin.addActionListener((e)->{
//            // get sample user for testing purpose
//            EntityManager em = HibernateUtil.getEmf().createEntityManager();
//            models.User SampleUser = em.find(models.User.class, 1);
//
//            // set the login user status
//            userScreen.setUser(SampleUser);
//
//            userScreen.showAdminChatScreen();
//        });

        register.addActionListener((e)->{
            userScreen.showUserRegisterScreen();
        });
    }

}
