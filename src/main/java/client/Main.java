package client;

import client.gui.common.AppScreen;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppScreen::new);
    }
}
