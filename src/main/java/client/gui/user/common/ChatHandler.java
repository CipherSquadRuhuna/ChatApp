package client.gui.user.common;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatHandler implements Runnable {
    JTextArea chatArea;
    JLabel userMessageLabel;

    public ChatHandler(JTextArea chatArea, JLabel userMessageLabel) {
        this.chatArea = chatArea;
        this.userMessageLabel = userMessageLabel;
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 3001);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String message = in.readLine();
                chatArea.append(message + "\n");
                userMessageLabel.setText("New message received");
            }
        } catch (IOException e) {
            e.printStackTrace();
            userMessageLabel.setText("Connection error");
        }
    }
}
