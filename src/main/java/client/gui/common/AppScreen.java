package client.gui.common;

import client.gui.admin.AdminChatScreen;
import client.gui.admin.AdminCreateNewChat;
import client.gui.admin.AdminUserList;
import client.gui.user.UserChatScreen;
import client.gui.user.UserLoginScreen;
import client.gui.user.UserProfileScreen;

import javax.swing.*;
import java.awt.*;

public class AppScreen extends JFrame {
    // layout
    private final JPanel containerPanel;
    private final CardLayout cardLayout;

    //screens - common
    private final JPanel loginScreen;
    private final JPanel userProfileScreen;

    //screens - user
    private final JPanel userChatScreen;

    //screens - admin
    private final JPanel adminChatScreen;
    private final JPanel adminUserListScreen;
    private final JPanel adminNewChatScreen;


    public AppScreen() throws HeadlessException {
        // set screens
        changeTitle("Ciphersquard - ChatApp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // set layout
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // set & add common screens
        loginScreen = new UserLoginScreen(this);
        userProfileScreen = new UserProfileScreen(this);
        containerPanel.add(loginScreen, "Login");
        containerPanel.add(userProfileScreen, "Profile");

        // set & add user screens
        userChatScreen = new UserChatScreen(this);
        containerPanel.add(userChatScreen, "UserChat");

        //set & add admin screens
        adminChatScreen = new AdminChatScreen(this);
        adminUserListScreen = new AdminUserList(this);
        adminNewChatScreen = new AdminCreateNewChat(this);
        containerPanel.add(adminChatScreen, "AdminChat");
        containerPanel.add(adminUserListScreen, "AdminUserList");
        containerPanel.add(adminNewChatScreen, "AdminNewChat");

        add(containerPanel);

    }

    public void changeTitle(String title) {
        this.setTitle(title);
    }

    public void showLoginScreen() {
        cardLayout.show(containerPanel, "Login");
    }

    public void showAdminChatScreen() {
        cardLayout.show(containerPanel, "AdminChat");
    }

    public void showUserChatScreen() {
        cardLayout.show(containerPanel, "UserChat");
    }

    public void showUserProfileScreen() {
        cardLayout.show(containerPanel, "Profile");
    }

    public void showAdminUserListScreen() {
        cardLayout.show(containerPanel, "AdminUserList");
    }

    public void showAdminNewChatScreen() {
        cardLayout.show(containerPanel, "AdminNewChat");
    }
}
