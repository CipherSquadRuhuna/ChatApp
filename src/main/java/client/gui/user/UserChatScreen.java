package client.gui.user;

import javax.swing.*;

public class UserChatScreen extends JPanel {
    private JTextArea chatArea;
    private JScrollPane chatScrollPane;
    private JButton sendButton;
    private UserScreen userScreen;

    public UserChatScreen(UserScreen userScreen) {
        this.userScreen = userScreen;
        initialize();
    }

    private void initialize() {
        chatArea = new JTextArea();
        chatScrollPane = new JScrollPane(chatArea);
        sendButton = new JButton("Send");
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);

        add(chatScrollPane);
        add(sendButton);
        add(chatArea);
    }

}
