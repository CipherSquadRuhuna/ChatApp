package client.gui.user;

import client.User;
import client.gui.common.AppScreen;
import client.gui.user.common.UserMenu;

import javax.swing.*;
import java.awt.*;

public class UserProfileScreen extends UserMenu {
    private JTextField nameField;
    private JTextField usernameField;
    private JTextField nicknameField;
    private JButton updateButton;

    private final AppScreen screen;
    private User user;

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
        nameField = new JTextField(5);
        usernameField = new JTextField(5);
        nicknameField = new JTextField(5);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(Box.createRigidArea(new Dimension(0, 2)));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 2)));

        panel.add(new JLabel("Nickname:"));
        panel.add(nicknameField);
        panel.add(Box.createRigidArea(new Dimension(0, 2)));

        updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateProfile());
        panel.add(updateButton);

        add(panel, BorderLayout.CENTER);


    }


    public void loadUser(User user) {
        this.user = user;
        nameField.setText("User " + user.getId()); // Or user.getName() if available
        usernameField.setText("Socket: " + (user.getSocket() != null ? "Connected" : "Disconnected"));
        nicknameField.setText(user.getNickname());
    }

    private void updateProfile() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String nickname = nicknameField.getText();

        // You can call set methods on a User object here if needed.
        if (user != null) {
            try {
                String newNickname = nicknameField.getText().trim();

                // You could also parse/update ID or other fields if editable
                user.setNickname(newNickname);

                JOptionPane.showMessageDialog(this, "User profile updated!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
            }
        }
        // For now, let's just show a success message.
        JOptionPane.showMessageDialog(this, "Profile updated successfully!");


    }


}
