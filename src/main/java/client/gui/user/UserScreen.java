package client.gui.user;

import client.gui.common.ChatScreen;

import javax.swing.*;
import java.awt.*;

public class UserScreen extends JFrame  {

    // user screens
    private JPanel loginScreen;
    private JPanel chatScreen;
    private JPanel userProfileScreen;

    // layout
    private JPanel containerPanel;
    private CardLayout cardLayout;

    public UserScreen() throws HeadlessException {

        // set screens
        super("Ciphersquard - ChatApp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // set layout
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // set screens
        loginScreen = new UserLoginScreen(this);
//        chatScreen = new UserChatScreen(this);
        chatScreen = new ChatScreen();
        userProfileScreen = new UserProfileScreen(this);

        // add panel to container
        containerPanel.add(loginScreen, "Login");
        containerPanel.add(chatScreen, "Chat");
        containerPanel.add(userProfileScreen, "Profile");

        add(containerPanel);
        showLoginScreen();

    }

    public void showLoginScreen() {
        cardLayout.show(containerPanel, "Login");
    }

    public void showChatScreen() {
        cardLayout.show(containerPanel, "Chat");
    }
    public void showUserProfileScreen() {
        cardLayout.show(containerPanel, "Profile");
    }
}
