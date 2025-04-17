package client.gui.common;

import client.gui.user.common.ChatHandler;
import common.Chat;
import server.ServerInterface;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.util.ArrayList;

public class ChatScreen extends JPanel {

    // chats data
    private final ArrayList<Chat> chats = new ArrayList<>();

    // swing component
    private JTextArea chatArea;
    private JScrollPane chatScrollPane;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel userMessageLabel;
    private JPanel rightPanel;

    public ChatScreen() {
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JList<String> chatList = new JList<>(getChatList());
        displayChats(chatList);

        // Right Panel for Chat
        rightPanel = new JPanel(new BorderLayout());

        // chat display ares
        showChatDisplayArea();

        // Message Input area
        showMessageSendArea();

        //  status label
        userMessageLabel = new JLabel(" ");
        rightPanel.add(userMessageLabel, BorderLayout.NORTH);
        add(rightPanel, BorderLayout.CENTER);

        // load the menubar
//        add(super.menuBar, BorderLayout.NORTH);

        // create chat handler
        ChatHandler chat = new ChatHandler(chatArea,userMessageLabel);
        Thread chatThread = new Thread(chat);
        chatThread.start();

        // send message when press enter
        messageField.addActionListener((e) -> {
            sendMessage(messageField.getText().trim());
        });

        // Send message when press button
        sendButton.addActionListener(( e) -> {
            sendMessage(messageField.getText().trim());
        });
    }

    private DefaultListModel<String> getChatList() {

        // dummy logic - data should retrieve from server
        chats.add(new Chat(1, "General"));
        chats.add(new Chat(2, "Server"));
        chats.add(new Chat(3, "Support"));

        // Chat list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Chat chat : chats) {
            listModel.addElement(chat.getName());
        }

        return listModel;
    }

    private void displayChats(JList<String> chatList) {
        JScrollPane chatListScrollPane = new JScrollPane(chatList);
        chatListScrollPane.setPreferredSize(new Dimension(150, 0));
        add(chatListScrollPane, BorderLayout.WEST);
    }

    private void sendMessage(String message) {
        // return if message empty
        if (message.isEmpty()) return;

        try {
            ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost:1099/server");
            server.sendBroadcastMessage(message);
//            chatArea.append("Me: " + message + "\n");
            messageField.setText("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showChatDisplayArea(){
        // Chat Display Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatScrollPane = new JScrollPane(chatArea);
        rightPanel.add(chatScrollPane, BorderLayout.CENTER);
    }

    private void showMessageSendArea(){
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        rightPanel.add(inputPanel, BorderLayout.SOUTH);
    }
}
