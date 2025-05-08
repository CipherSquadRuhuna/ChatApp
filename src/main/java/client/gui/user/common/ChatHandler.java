package client.gui.user.common;

import client.gui.utility.ChatUtility;
import models.ChatMessage;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ChatHandler implements Runnable {
    JTextPane chatArea;
    JLabel userMessageLabel;

    ChatUtility chatUtility;

    public ChatHandler(JTextPane chatArea, JLabel userMessageLabel, ChatUtility chatUtility) {
        this.chatArea = chatArea;
        this.userMessageLabel = userMessageLabel;
        this.chatUtility = chatUtility;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 3001)) {
            while (true) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                // we have message object now
                ChatMessage message = (ChatMessage) in.readObject();
                chatUtility.printMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());

        }
    }
}
