package client.gui.user;

import client.gui.common.AppScreen;
import client.gui.user.common.UserMenu;

import javax.swing.*;
import java.awt.*;

public class UserProfileScreen extends UserMenu {
    private JLabel nameLabel;
    private JLabel usernameLabel;
    private JLabel nicknameLabel;
    private final AppScreen screen;

    public UserProfileScreen(AppScreen screen) {
        super(screen);
        this.screen = screen;
        initialize();
    }

    private void initialize() {

        // set layout -- DON'T CHANGE THIS MAKES MENU MISS-PLACE
        // create new jpanel and set the layout instead
        setLayout(new BorderLayout());

        // add menu
        add(super.menuBar, BorderLayout.NORTH);

        // create panel to use another layout internally..
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        nameLabel = new JLabel("Name");
        usernameLabel = new JLabel("Username");
        nicknameLabel = new JLabel("Nickname");

        panel.add(nameLabel);
        panel.add(usernameLabel);
        panel.add(nicknameLabel);

        add(panel, BorderLayout.CENTER);


    }
}
