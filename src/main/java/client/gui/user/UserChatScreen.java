package client.gui.user;

import client.gui.common.AppScreen;
import client.gui.shared.ChatScreen;
import client.gui.user.common.UserMenu;

import java.awt.*;

public class UserChatScreen extends ChatScreen {
    public UserChatScreen(AppScreen appScreen) {
        super();
        UserMenu menu = new UserMenu(appScreen);
        add(menu.getMenuBar(), BorderLayout.NORTH);
    }
}
