package client.gui.shared;

import client.gui.common.AppScreen;
import client.gui.user.common.ChatHandler;
import common.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.Chat;
import models.ChatMessage;
import models.User;
import server.ServerInterface;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Date;

public class ChatScreen extends JPanel {
    // chats data
    private final ArrayList<Chat> chats = new ArrayList<>();
    private final ArrayList<ChatMessage> messages = new ArrayList<>();
    private final AppScreen screen;

    // swing component
    private JTextArea chatArea;
    private JScrollPane chatScrollPane;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel userMessageLabel;
    private JPanel rightPanel;

    public ChatScreen(AppScreen screen) {
        this.screen = screen;
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

        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        String query = "select c from Chat c";
        TypedQuery<Chat> q = em.createQuery(query, Chat.class);
        chats.addAll(q.getResultList());


        // Chat list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Chat chat : chats) {
            listModel.addElement(chat.getTitle());
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

        // create message object
        ChatMessage messageObj = new ChatMessage();
        messageObj.setMessage(message);

        // get user to set for chat

        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        User MessageSender = em.find(User.class, 1);
        System.out.println(MessageSender);
        messageObj.setUser(MessageSender);
        em.close();

        // get sample chat to assign
        EntityManager em2 = HibernateUtil.getEmf().createEntityManager();
        Chat chat = em2.find(Chat.class, 1);
        messageObj.setChat(chat);
        em2.close();

        //set time
        messageObj.setSentAt(new Date().toInstant());

        // save message on a database
        EntityManager em3 = HibernateUtil.getEmf().createEntityManager();
        EntityTransaction et = null;

        try {
            et = em3.getTransaction();
            et.begin();
            em3.persist(messageObj);
            et.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em3.close();

        }

        try {
            ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost:1099/server");
            server.sendBroadcastMessage(messageObj);
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
