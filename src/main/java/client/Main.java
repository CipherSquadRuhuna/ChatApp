package client;

import client.gui.user.UserScreen;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
       SwingUtilities.invokeLater(()->{

//           ChatHome chat = new ChatHome();
//           MessageSubject.getInstance().addObserver(chat);

//           Login login = new Login();
           UserScreen userScreen = new UserScreen();
       });
    }
}
