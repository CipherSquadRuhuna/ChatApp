package client.gui.user;

import javax.swing.*;
import java.awt.*;

public class UserScreen extends JFrame {
    private JPanel loginScreen;
    private JPanel registerScreen;
    private JPanel chatScreen;
    private JPanel containerPanel;
    private CardLayout cardLayout;

    public UserScreen() throws HeadlessException {

        // set screens

        super("User Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // set layout
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // set screens
        loginScreen = new UserLoginScreen(this);
        chatScreen = new UserChatScreen(this);


        // add panel to container
        containerPanel.add(loginScreen, "Login");
        containerPanel.add(chatScreen, "Chat");

        add(containerPanel);
        showLoginScreen();

    }

    public void showLoginScreen() {
        cardLayout.show(containerPanel, "Login");
    }

    public void showChatScreen() {
        cardLayout.show(containerPanel, "Chat");
    }
}
