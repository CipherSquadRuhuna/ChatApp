package client.gui.admin.common;

import client.gui.admin.AdminScreen;

import javax.swing.*;

public class AdminMenu extends JPanel {
    private final AdminScreen screen;
    protected JMenuBar menuBar;

    public AdminMenu(AdminScreen screen) {
        this.screen = screen;
    }

//    private void createChatMenu() {
//        // chat tab
//        JMenu chatMenu = new JMenu("Chat");
//        JMenuItem chatItem = new JMenuItem("Chat");
//        chatMenu.add(chatItem);
//
//        // add menu to the item
//        menuBar.add(chatMenu);
//
//        // add action lister for exit
//        chatItem.addActionListener((e) -> {
//            screen.showChatScreen();
//        });
//    }
//
//    private void createSettingsMenu() {
//
//        //settings tab
//        JMenu settingsMenu = new JMenu("Settings");
//        JMenuItem profileItem = new JMenuItem("Profile");
//        settingsMenu.add(profileItem);
//
//        // add tab to menu
//        menuBar.add(settingsMenu);
//
//        // add action lister for profile
//        profileItem.addActionListener((e) -> {
//            screen.showUserProfileScreen();
//        });
//
//    }
//
//    private void createExitMenu() {
//        // exit tab
//        JMenu exitMenu = new JMenu("Exit");
//        JMenuItem exitItem = new JMenuItem("Exit");
//        exitMenu.add(exitItem);
//
//        menuBar.add(exitMenu);
//
//        // add action lister for chat
//        exitItem.addActionListener((e) -> {
//            screen.showLoginScreen();
//        });
//    }
}
