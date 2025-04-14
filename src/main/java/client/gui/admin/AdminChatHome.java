package client.gui.admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminChatHome extends JFrame {
    private JPanel mainPanel;
    private JTextField messageField;
    private JButton sendButton;

    AdminChatHome() {
        setTitle("Admin Chat Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setContentPane(mainPanel);
        setVisible(true);

        sendButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(messageField.getText());
            }
        });
    }

    public static void main(String[] args) {
        new AdminChatHome();
    }
}
