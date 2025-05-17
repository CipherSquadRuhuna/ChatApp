package client.gui.user.common;

import client.gui.common.AppScreen;
import client.gui.shared.Menu;

import javax.swing.*;


public class UserMenu extends Menu {
    private final AppScreen screen;
//    protected JMenuBar menuBar;

    public UserMenu(AppScreen screen) {
        super(screen);
        this.screen = screen;

        //add chat,settings,exit
        createChatMenu();
        createSettingsMenu();
        createExitMenu();
    }


    private void createChatMenu() {
        // chat tab
        JMenu chatMenu = new JMenu("Chat");
        JMenuItem chatItem = new JMenuItem("Chat");
        chatMenu.add(chatItem);

        // add menu to the item
        menuBar.add(chatMenu);

        // add action lister for exit
        chatItem.addActionListener((e) -> {
            screen.showUserChatScreen();
        });
    }

}
