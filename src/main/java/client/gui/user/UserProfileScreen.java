package client.gui.user;

import models.User;
import client.gui.common.AppScreen;
import client.gui.user.common.UserMenu;
import common.HibernateUtil;

import javax.swing.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class UserProfileScreen extends UserMenu {
    private JTextField emailField;
    private JTextField usernameField;
    private JTextField nicknameField;

    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    private JLabel displayEmailLabel;
    private JLabel displayUsernameLabel;
    private JLabel displayNicknameLabel;

    private JLabel profilePicLabel;
    private File selectedImageFile;

    private final AppScreen screen;
    private User user;

    public UserProfileScreen(AppScreen screen) {
        super(screen);
        this.screen = screen;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        add(super.menuBar, BorderLayout.NORTH);

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Profile Picture
        profilePicLabel = new JLabel();
        profilePicLabel.setPreferredSize(new Dimension(120, 120));
        profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton changePicButton = new JButton("Change Picture");
        changePicButton.addActionListener(e -> changeProfilePicture());

        JPanel picPanel = new JPanel();
        picPanel.setLayout(new BoxLayout(picPanel, BoxLayout.Y_AXIS));
        picPanel.add(profilePicLabel);
        picPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        picPanel.add(changePicButton);

        containerPanel.add(picPanel);
        containerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Display Section
        JPanel displayPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        displayPanel.setBorder(BorderFactory.createTitledBorder("Current Profile"));

        displayEmailLabel = new JLabel("Email: ");
        displayUsernameLabel = new JLabel("Username: ");
        displayNicknameLabel = new JLabel("Nickname: ");

        displayPanel.add(displayEmailLabel);
        displayPanel.add(displayUsernameLabel);
        displayPanel.add(displayNicknameLabel);

        containerPanel.add(displayPanel);
        containerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Update Profile Section
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Update Profile"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        emailField = new JTextField(20);
        usernameField = new JTextField(20);
        nicknameField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Nickname:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nicknameField, gbc);

        JButton updateButton = new JButton("Update Profile");
        updateButton.addActionListener(e -> updateProfile());

        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(updateButton, gbc);

        containerPanel.add(formPanel);
        containerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Change Password Section
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Change Password"));
        gbc.anchor = GridBagConstraints.WEST;

        currentPasswordField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        passwordPanel.add(new JLabel("Current Password:"), gbc);
        gbc.gridx = 1;
        passwordPanel.add(currentPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        passwordPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        passwordPanel.add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        passwordPanel.add(new JLabel("Confirm New Password:"), gbc);
        gbc.gridx = 1;
        passwordPanel.add(confirmPasswordField, gbc);

        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.addActionListener(e -> changePassword());

        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        passwordPanel.add(changePasswordButton, gbc);

        containerPanel.add(passwordPanel);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        backButton.addActionListener(e -> {
            screen.setUser(user);
            screen.showUserChatScreen();
        });

        JPanel backPanel = new JPanel();
        backPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(backButton);

        containerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        containerPanel.add(backPanel);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadUser(User user) {
        this.user = user;

        displayEmailLabel.setText("Email: " + user.getEmail());
        displayUsernameLabel.setText("Username: " + user.getUsername());
        displayNicknameLabel.setText("Nickname: " + user.getNickName());

        emailField.setText(user.getEmail());
        usernameField.setText(user.getUsername());
        nicknameField.setText(user.getNickName());

        if (user.getProfilePicturePath() != null && !user.getProfilePicturePath().equals("No picture")) {
            try {
                BufferedImage img = ImageIO.read(new File(user.getProfilePicturePath()));
                Image scaledImg = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                profilePicLabel.setIcon(new ImageIcon(scaledImg));
                profilePicLabel.setText("");
            } catch (IOException e) {
                profilePicLabel.setText("Image not found");
            }
        } else {
            profilePicLabel.setText("No picture");
            profilePicLabel.setIcon(null);
        }

        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }

    private void updateProfile() {
        if (user == null) {
            JOptionPane.showMessageDialog(this, "No user loaded.");
            return;
        }

        String newEmail = emailField.getText().trim();
        String newUsername = usernameField.getText().trim();
        String newNickname = nicknameField.getText().trim();

        if (newEmail.isEmpty() || newUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and Username cannot be empty.");
            return;
        }

        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            User dbUser = em.find(User.class, user.getId());
            if (dbUser != null) {
                dbUser.setEmail(newEmail);
                dbUser.setUsername(newUsername);
                dbUser.setNickName(newNickname);
                em.merge(dbUser);
                tx.commit();

                user.setEmail(newEmail);
                user.setUsername(newUsername);
                user.setNickName(newNickname);

                displayEmailLabel.setText("Email: " + newEmail);
                displayUsernameLabel.setText("Username: " + newUsername);
                displayNicknameLabel.setText("Nickname: " + newNickname);

                JOptionPane.showMessageDialog(this, "Profile updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "User not found in the database.");
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating profile: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private void changePassword() {
        if (user == null) {
            JOptionPane.showMessageDialog(this, "No user loaded.");
            return;
        }

        String currentPass = new String(currentPasswordField.getPassword()).trim();
        String newPass = new String(newPasswordField.getPassword()).trim();
        String confirmPass = new String(confirmPasswordField.getPassword()).trim();

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All password fields are required.");
            return;
        }

        if (!currentPass.equals(user.getPassword())) {
            JOptionPane.showMessageDialog(this, "Current password is incorrect.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.");
            return;
        }

        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            User dbUser = em.find(User.class, user.getId());
            if (dbUser != null) {
                dbUser.setPassword(newPass);
                em.merge(dbUser);
                tx.commit();

                user.setPassword(newPass);
                JOptionPane.showMessageDialog(this, "Password changed successfully.");

                currentPasswordField.setText("");
                newPasswordField.setText("");
                confirmPasswordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "User not found in database.");
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error changing password: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private void changeProfilePicture() {
        if (user == null) {
            JOptionPane.showMessageDialog(this, "No user loaded.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            String imagePath = selectedImageFile.getAbsolutePath();

            EntityManager em = HibernateUtil.getEmf().createEntityManager();
            EntityTransaction tx = em.getTransaction();

            try {
                tx.begin();
                User dbUser = em.find(User.class, user.getId());
                if (dbUser != null) {
                    dbUser.setProfilePicturePath(imagePath);
                    em.merge(dbUser);
                    tx.commit();

                    user.setProfilePicturePath(imagePath);

                    BufferedImage img = ImageIO.read(selectedImageFile);
                    Image scaledImg = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    profilePicLabel.setIcon(new ImageIcon(scaledImg));
                    profilePicLabel.setText("");

                    JOptionPane.showMessageDialog(this, "Profile picture updated.");
                } else {
                    JOptionPane.showMessageDialog(this, "User not found.");
                    tx.rollback();
                }
            } catch (Exception ex) {
                if (tx.isActive()) tx.rollback();
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update profile picture: " + ex.getMessage());
            } finally {
                em.close();
            }
        }
    }
}
