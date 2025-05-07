package client.gui.user.common;

import models.ChatMessage;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ChatHandler implements Runnable {
    JTextArea chatArea;
    JLabel userMessageLabel;

    public ChatHandler(JTextArea chatArea, JLabel userMessageLabel) {
        this.chatArea = chatArea;
        this.userMessageLabel = userMessageLabel;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 3001);
            while (true) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                // we have message object now
                ChatMessage message = (ChatMessage) in.readObject();

                chatArea.append(message.getUser().getNickName() + ":"+message.getMessage() + "\n");
                userMessageLabel.setText("New message received");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            userMessageLabel.setText("Connection error");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
