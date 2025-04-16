package client.gui.user;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserLoginScreen extends JPanel {
    private JTextField username;
    private JPasswordField password;
    private JButton login;
    private UserScreen userScreen;


    public UserLoginScreen(UserScreen userScreen) {
        this.userScreen = userScreen;
        initComponents();
    }

    private void initComponents() {
        username = new JTextField();
        password = new JPasswordField();
        login = new JButton("Login");

        add(username);
        add(password);
        add(login);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // show chat screen
                userScreen.showChatScreen();
            }
        });
    }

}
