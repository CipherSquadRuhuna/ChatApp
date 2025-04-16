package client.gui.user;

import common.MessageObserver;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatHome extends JFrame implements MessageObserver {
    private JPanel mainPanel;
    private JLabel messageField;

    public ChatHome() {
        setTitle("Chat Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setSize(300, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        // connect to the server




            new Thread(() -> {
                try {
                Socket socket = new Socket("localhost", 3001);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while(true) {
                    String message = in.readLine();
                    messageField.setText(message);
                    System.out.println("Got the message: " + message);
                }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();




    }


    public JPanel getMainPanel() {
        return mainPanel;
    }


    @Override
    public void messageReceived(String message) {
        messageField.setText(message);
    }
}
