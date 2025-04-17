package client.gui.user;

import client.gui.admin.AdminChatScreen;

import javax.swing.*;
import java.awt.*;

public class UserScreen extends JFrame {

    // user screens
//    private final JPanel loginScreen;
//    private final JPanel chatScreen;
//    private final JPanel userProfileScreen;
//    private final JPanel adminScreen;

    // layout
    private final JPanel containerPanel;
    private final CardLayout cardLayout;

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
//        loginScreen = new UserLoginScreen(this);
//        chatScreen = new UserChatScreen(this);
//        userProfileScreen = new UserProfileScreen(this);
//        adminScreen = new AdminChatScreen();

        // add panel to container
//        containerPanel.add(loginScreen, "Login");
//        containerPanel.add(chatScreen, "Chat");
//        containerPanel.add(userProfileScreen, "Profile");
//        containerPanel.add(adminScreen, "Admin");

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

    public void showAdminScreen() {
        cardLayout.show(containerPanel, "Admin");
    }
}
