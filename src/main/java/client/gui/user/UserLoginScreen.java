package client.gui.user;

import client.User;
import client.gui.common.AppScreen;

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
        //set layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        username = new JTextField();
        username.setMaximumSize(new Dimension(500,50));
        username.setAlignmentX(Component.CENTER_ALIGNMENT);

        password = new JPasswordField();
        password.setMaximumSize(new Dimension(500,50));
        password.setAlignmentX(Component.CENTER_ALIGNMENT);

        login = new JButton("Login");
        login.setAlignmentX(Component.CENTER_ALIGNMENT);

        // temporary button to access admin
        JButton adminLogin = new JButton("Admin Login");
        adminLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(adminLogin);

        // user register button
        JButton register = new JButton("Register");
        register.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(register);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameLabel.setMaximumSize(new Dimension(500,50));

        add(usernameLabel);
        add(username);
        add(new JLabel("Password:"));
        add(password);
        add(login);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // show chat screen
                // user should be set after login
                User user = new User(1,"Asela");
                userScreen.showUserChatScreen(user);
            }
        });

        adminLogin.addActionListener((e)->{
            userScreen.showAdminChatScreen();
        });

        register.addActionListener((e)->{
            userScreen.showUserRegisterScreen();
        });
    }

}
