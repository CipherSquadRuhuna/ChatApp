package client.gui.shared;

import client.gui.common.AppScreen;

import javax.swing.*;

public class Menu extends JPanel {
    private final AppScreen screen;
    protected JMenuBar menuBar;

    public Menu(AppScreen appScreen) {
        this.screen = appScreen;

        menuBar = new JMenuBar();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    protected void createSettingsMenu() {

        //settings tab
        JMenu settingsMenu = new JMenu("Settings");
        JMenuItem profileItem = new JMenuItem("Profile");
        settingsMenu.add(profileItem);

        // add tab to menu
        menuBar.add(settingsMenu);

        // add action lister for profile
        profileItem.addActionListener((e) -> {
            screen.showUserProfileScreen();
        });


    }

    protected void createExitMenu() {
        // exit tab
        JMenu exitMenu = new JMenu("Exit");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitMenu.add(exitItem);

        menuBar.add(exitMenu);

        // add action lister for chat
        exitItem.addActionListener((e) -> {
            screen.showLoginScreen();
        });
    }
}
