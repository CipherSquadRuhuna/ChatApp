package client.gui.admin;

import client.gui.admin.common.AdminMenu;
import client.gui.common.AppScreen;

import javax.swing.*;
import java.awt.*;

public class AdminUserList extends JPanel {
    public AdminUserList(AppScreen appScreen) {

        //set border layout
        setLayout(new BorderLayout());

        AdminMenu menu = new AdminMenu(appScreen);
        add(menu.getMenuBar(), BorderLayout.NORTH);
    }
}
