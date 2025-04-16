package client.gui.user;

import common.MessageObserver;
import server.ServerInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChatHome extends JFrame implements MessageObserver {
    private JPanel mainPanel;
    private JLabel messageField;
    private JTextField messageText;
    private JButton sendButton;

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

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost:1099/server");
                    server.sendBroadcastMessage(messageText.getText());

                } catch (NotBoundException ex) {
                    throw new RuntimeException(ex);
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }


    @Override
    public void messageReceived(String message) {
        messageField.setText(message);
    }
}
