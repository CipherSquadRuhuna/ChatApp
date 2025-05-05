package client.gui.common;

import client.User;
import client.gui.admin.AdminChatScreen;
import client.gui.admin.AdminCreateNewChat;
import client.gui.admin.AdminUserList;
import client.gui.user.UserChatScreen;
import client.gui.user.UserLoginScreen;
import client.gui.user.UserProfileScreen;
import client.gui.user.UserRegisterScreen;

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
    private final JPanel userRegisterScreen;

    //screens - admin
    private final JPanel adminChatScreen;
    private final JPanel adminUserListScreen;
    private final JPanel adminNewChatScreen;

    // user data
    private User user = null;

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
        userRegisterScreen = new UserRegisterScreen(this);
        containerPanel.add(userChatScreen, "UserChat");
        containerPanel.add(userRegisterScreen, "Register");

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
        // update user data
        this.user = user;

        cardLayout.show(containerPanel, "AdminChat");
    }

    public void showUserChatScreen(User user) {
        // update user data
        this.user = user;
        cardLayout.show(containerPanel, "UserChat");
    }

    public void showUserProfileScreen() {
        cardLayout.show(containerPanel, "Profile");
        UserProfileScreen profileScreen = new UserProfileScreen(this);
        profileScreen.loadUser(user);
        setContentPane(profileScreen);
        revalidate();
        repaint();
    }

    public void showUserRegisterScreen() {
        cardLayout.show(containerPanel, "Register");
    }

    public void showAdminUserListScreen() {
        cardLayout.show(containerPanel, "AdminUserList");
    }

    public void showAdminNewChatScreen() {
        cardLayout.show(containerPanel, "AdminNewChat");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
