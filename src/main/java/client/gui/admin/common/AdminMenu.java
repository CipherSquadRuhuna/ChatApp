package client.gui.admin.common;

import client.gui.common.AppScreen;
import client.gui.shared.Menu;

import javax.swing.*;

public class AdminMenu extends Menu {
    private final AppScreen screen;

    public AdminMenu(AppScreen screen) {
        super(screen);
        this.screen = screen;

        //add chat,users,settings,exit
        createChatMenu();
        createUserMenu();
        createSettingsMenu();
        createExitMenu();
    }


    private void createChatMenu() {
        // chat tab
        JMenu chatMenu = new JMenu("Chat");
        JMenuItem chatItem = new JMenuItem("Chat");
        JMenuItem newChatItem = new JMenuItem("New Chat");
        chatMenu.add(chatItem);
        chatMenu.add(newChatItem);

        // add menu to the item
        menuBar.add(chatMenu);

        // add action lister for chat screen
        chatItem.addActionListener((e) -> {
            screen.showAdminChatScreen();
        });

        // add action for new chat creation
        newChatItem.addActionListener((e) -> {
            screen.showAdminNewChatScreen();
        });
    }

    private void createUserMenu() {
        // chat tab
        JMenu userMenu = new JMenu("Users");
        JMenuItem userMenuItem = new JMenuItem("List Users");
        userMenu.add(userMenuItem);


        // add menu to the item
        menuBar.add(userMenu);

        // add action lister for exit
        userMenuItem.addActionListener((e) -> {
            screen.showAdminUserListScreen();
        });
    }


}
