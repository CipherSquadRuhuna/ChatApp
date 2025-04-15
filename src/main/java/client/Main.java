package client;

import client.gui.user.ChatHome;
import server.MessageSubject;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
       SwingUtilities.invokeLater(()->{
           ChatHome chat = new ChatHome();
           MessageSubject.getInstance().addObserver(chat);
       });


    }
}
