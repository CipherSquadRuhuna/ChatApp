package client.gui.admin;

import client.gui.admin.common.AdminMenu;
import client.gui.common.AppScreen;
import client.gui.shared.ChatScreen;

import java.awt.*;

public class AdminChatScreen extends ChatScreen {
    public AdminChatScreen(AppScreen appScreen) {
        super();

        AdminMenu menu = new AdminMenu(appScreen);
        add(menu.getMenuBar(), BorderLayout.NORTH);
    }
}
