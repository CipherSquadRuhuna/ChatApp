package client.gui.admin;

import server.MessageSubject;
import server.Server;
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

public class AdminChatHome extends JFrame {
    private JPanel mainPanel;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel MessageText;

    AdminChatHome() {
        setTitle("Admin Chat Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setContentPane(mainPanel);
        setVisible(true);

        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 3001);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while(true) {
                    String message = in.readLine();
                    MessageText.setText(message);
                    System.out.println("Got the message: " + message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        sendButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
//                MessageSubject.getInstance().setMessage(messageField.getText());

                //invoke server method using RMI
                try {
                    ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost:1099/server");
                    server.sendBroadcastMessage(messageField.getText());

                } catch (NotBoundException ex) {
                    throw new RuntimeException(ex);
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println(messageField.getText());
            }
        });
    }

    public static void main(String[] args) {
        new AdminChatHome();
    }
}
