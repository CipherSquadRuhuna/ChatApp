package client.gui.shared;

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
    // shared swing component among methods
    private final JTextArea chatArea = new JTextArea();
    private final JTextField messageField = new JTextField();
    private final JButton sendButton = new JButton("Send");
    private final JPanel rightPanel = new JPanel(new BorderLayout());
    private final JLabel userMessageLabel = new JLabel(" ");
    private Chat activeChat;
    private final User loginUser;
    // chat list
    private JList<String> chatList;

    public ChatScreen(User loginUser) {
        initialize();

        // make sure user is correct before start
        if (loginUser == null) {
            System.out.println("Login user is null");
            throw new IllegalArgumentException("Login user is null");
        }

        System.out.println("Login user is " + loginUser);

        this.loginUser = loginUser;
    }

    /**
     * Loading all necessary components for ChatScreen
     */
    private void initialize() {
        setLayout(new BorderLayout());

        // display chat list
        displayChats();

        // chat content display area
        showChatDisplayArea();

        // Message Input area
        showMessageSendArea();

        //  status label
        showChatStatusLabel();

        // create chat handler
        ChatHandler chat = new ChatHandler(chatArea, userMessageLabel);
        Thread chatThread = new Thread(chat);
        chatThread.start();

        // send message when press enter

        messageField.addActionListener((e) -> {
            sendMessage(messageField.getText().trim());
        });

        // Send message when press button
        sendButton.addActionListener((e) -> {
            sendMessage(messageField.getText().trim());
        });

        chatList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = chatList.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < chats.size()) {
                    Chat selectedChat = chats.get(selectedIndex);
                    int chatId = selectedChat.getId();
                    // get active chat from database
                    EntityManager em = HibernateUtil.getEmf().createEntityManager();
                    Chat chatEntity = em.find(Chat.class, chatId);
                    setActiveChat(chatEntity);


                    loadChatMessages(chatId);

                    // show to message send area if only chat is selected
//                    showMessageSendArea();
                }
            }
        });
    }

    public Chat getActiveChat() {
        return activeChat;
    }

    public void setActiveChat(Chat activeChat) {
        this.activeChat = activeChat;
    }

    private DefaultListModel<String> loadChatList() {

        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        String query = "select c from Chat c ";
        TypedQuery<Chat> q = em.createQuery(query, Chat.class);
        chats.addAll(q.getResultList());


        // Chat list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Chat chat : chats) {
            listModel.addElement(chat.getTitle());
        }

        return listModel;
    }

    private void displayChats() {
        // add right panel to the screen
        add(rightPanel, BorderLayout.CENTER);

        // load chats from database
        chatList = new JList<>(loadChatList());
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


        // set the user who send the message
        messageObj.setUser(loginUser);

        // set active chat to message owner
        messageObj.setChat(activeChat);

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

    private void showChatDisplayArea() {
        // Chat Display Area
//        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        rightPanel.add(chatScrollPane, BorderLayout.CENTER);
    }

    private void showChatStatusLabel() {
        rightPanel.add(userMessageLabel, BorderLayout.NORTH);
    }

    private void showMessageSendArea() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        rightPanel.add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadChatMessages(int chatId) {
        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        String query = "select m from ChatMessage m where m.chat.id=:chatId";
        TypedQuery<ChatMessage> q = em.createQuery(query, ChatMessage.class);
        System.out.println("Loading Chat Messages for " + chatId);
        q.setParameter("chatId", chatId);

        // clear old list
        messages.clear();
        messages.addAll(q.getResultList());

//        print for testing
        for (ChatMessage message : messages) {
            System.out.println(message.getMessage());
        }

        displayChatMessages();
    }

    private void displayChatMessages() {
        // clear chat ares
        chatArea.setText("");
        for (ChatMessage message : messages) {
            chatArea.append(message.getUser().getNickName() + ":" + message.getMessage() + "\n");
        }
    }

    private void showChatList() {
        chatList = new JList<>(loadChatList());
    }

}
