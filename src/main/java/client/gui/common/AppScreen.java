package client.gui.common;


import client.gui.admin.AdminChatScreen;
import client.gui.admin.AdminCreateNewChat;
import client.gui.admin.AdminUserList;
import client.gui.user.UserChatScreen;
import client.gui.user.UserLoginScreen;
import client.gui.user.UserProfileScreen;
import client.gui.user.UserRegisterScreen;
import models.Chat;
import models.User;

import javax.swing.*;
import java.awt.*;

public class AppScreen extends JFrame {
    // layout
    private final JPanel containerPanel;
    private final CardLayout cardLayout;

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

        // show login screen
        showLoginScreen();

        add(containerPanel);

    }

    /**
     * Change title of the window
     *
     * @param title - new title that need to change
     */
    public void changeTitle(String title) {
        this.setTitle(title);
    }

    /**
     * Show login screen that users can add their credentials
     */
    public void showLoginScreen() {
        cardLayout.show(containerPanel, "Login");
        JPanel loginScreen = new UserLoginScreen(this);
        containerPanel.add(loginScreen, "Login");
    }

    /**
     * Show chat screen for users how login as admin
     */
    public void showAdminChatScreen() {
        JPanel adminChatScreen = new AdminChatScreen(this);
        containerPanel.add(adminChatScreen, "AdminChat");
        setTitle("Chat App");
        cardLayout.show(containerPanel, "AdminChat");
    }

    /**
     * Show chat screen for users how login as admin when chat seleted
     */
    public void showAdminChatScreen(Chat chat) {
        JPanel adminChatScreen = new AdminChatScreen(this, chat);
        containerPanel.add(adminChatScreen, "AdminChat");
        setTitle("Chat App");
        cardLayout.show(containerPanel, "AdminChat");
    }

    /**
     * Show chat screen for normal users
     */
    public void showUserChatScreen() {
        JPanel userChatScreen = new UserChatScreen(this);
        containerPanel.add(userChatScreen, "UserChat");
        setTitle("Chat App");
        cardLayout.show(containerPanel, "UserChat");
    }

    /**
     * Show profile screen for users that they can manage their data
     */
    public void showUserProfileScreen() {
        JPanel userProfileScreen = new UserProfileScreen(this);
        containerPanel.add(userProfileScreen, "Profile");
        cardLayout.show(containerPanel, "Profile");
        UserProfileScreen profileScreen = new UserProfileScreen(this);
        profileScreen.loadUser(user);
        setContentPane(profileScreen);
        revalidate();
        repaint();
    }

    /**
     * Show user self registration page
     */
    public void showUserRegisterScreen() {
        JPanel userRegisterScreen = new UserRegisterScreen(this);
        containerPanel.add(userRegisterScreen, "Register");
        setTitle("Register Now");
        cardLayout.show(containerPanel, "Register");
    }

    /**
     * Show page that admin can manage users
     */
    public void showAdminUserListScreen() {
        JPanel adminUserListScreen = new AdminUserList(this);
        containerPanel.add(adminUserListScreen, "AdminUserList");
        setTitle("User List");
        cardLayout.show(containerPanel, "AdminUserList");
    }

    /**
     * Show form that admin able to create new chat list
     */
    public void showAdminNewChatScreen() {
        JPanel adminNewChatScreen = new AdminCreateNewChat(this);
        containerPanel.add(adminNewChatScreen, "AdminNewChat");
        setTitle("Create New Chat");
        cardLayout.show(containerPanel, "AdminNewChat");
    }


    /**
     * Get the current application user instance
     *
     * @return User
     */
    public User getUser() {
        return user;
    }

    /**
     * Update current application user instance
     */
    public void setUser(User user) {
        this.user = user;
    }
}
