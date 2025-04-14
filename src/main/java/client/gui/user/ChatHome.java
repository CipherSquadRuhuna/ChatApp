package client.gui.user;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatHome extends JFrame {
    private JPanel mainPanel;
    private JButton button1;
    private JButton button2;

    public ChatHome() {
        setTitle("Chat Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
