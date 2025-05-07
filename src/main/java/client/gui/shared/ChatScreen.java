package client.gui.shared;

import client.gui.user.common.ChatHandler;
import common.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.*;
import server.ServerInterface;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class ChatScreen extends JPanel {
    protected final JPanel rightPanel = new JPanel(new BorderLayout());
    // chats data
    private final ArrayList<Chat> chats = new ArrayList<>();
    private final ArrayList<ChatMessage> messages = new ArrayList<>();
    private final JTextField messageField = new JTextField();
    private final JButton sendButton = new JButton("Send");
    private final JLabel userMessageLabel = new JLabel(" ");
    private final User loginUser;
    // shared swing component among methods
    private JTextArea chatArea = new JTextArea();
    private Chat activeChat;

    // chat list
    private JList<String> chatList;

    public ChatScreen(User loginUser) {
        initialize();

        // make sure user is correct before start
        if (loginUser == null) {
            System.out.println("Login user is null");
            throw new IllegalArgumentException("Login user is null");
        }
        this.loginUser = loginUser;
    }

    /**
     * Loading all necessary components for ChatScreen
     */
    private void initialize() {
        setLayout(new BorderLayout());

        // add right panel to the screen
        add(rightPanel, BorderLayout.CENTER);

        // display chat list
        displayChats();

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

                    handleChatDisplayArea();
                }
            }
        });
    }

    public void handleChatDisplayArea() {
        // check if the user subscribe to the chat
        EntityManager em2 = HibernateUtil.getEmf().createEntityManager();
        Long count = em2.createQuery(
                        "SELECT count(uc) FROM UserChat uc WHERE uc.user.id = :userId and uc.chat.id = :chatId", Long.class)
                .setParameter("userId", loginUser.getId())
                .setParameter("chatId", activeChat.getId())
                .getSingleResult();

        // no any subscription found
        if (count == 0) {
            displaySubscribeToChat();
            return;
        }

        // check chat start
        if (activeChat.getStartTime() == null) {
            displayChatNotStartedMessage();
            return;
        }

        showChatDisplayArea();
        loadChatMessages();
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

    protected void showChatDisplayArea() {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();

        // Chat Display Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        rightPanel.add(chatScrollPane, BorderLayout.CENTER);

        showMessageSendArea();
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

    protected void loadChatMessages() {
        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        String query = "select m from ChatMessage m where m.chat.id=:chatId";
        TypedQuery<ChatMessage> q = em.createQuery(query, ChatMessage.class);
        System.out.println("Loading Chat Messages for " + activeChat.getId());
        q.setParameter("chatId", activeChat.getId());

        messages.clear();
        messages.addAll(q.getResultList());

        displayChatMessages();
    }

    protected void clearRightPanel() {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private void displayChatNotStartedMessage() {
        //clear right panel
        clearRightPanel();

        JPanel chatInfoPanel = new JPanel();
        chatInfoPanel.setLayout(new BoxLayout(chatInfoPanel, BoxLayout.Y_AXIS));

        JLabel messageLabel = new JLabel("Chat not started.. Wait until admin start the chat");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatInfoPanel.add(messageLabel);

        rightPanel.add(chatInfoPanel, BorderLayout.CENTER);
    }

    private void displaySubscribeToChat() {

        clearRightPanel();

        JPanel subscribePanel = new JPanel();
        subscribePanel.setLayout(new BoxLayout(subscribePanel, BoxLayout.Y_AXIS));

        JLabel subscribeLabel = new JLabel("Subscribe to chat");
        subscribeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton subscribeButton = new JButton("Subscribe");
        subscribeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        subscribePanel.add(Box.createVerticalStrut(10)); // spacing
        subscribePanel.add(subscribeLabel);
        subscribePanel.add(Box.createVerticalStrut(10)); // spacing
        subscribePanel.add(subscribeButton);


        rightPanel.add(subscribePanel, BorderLayout.CENTER);

        subscribeButton.addActionListener((_) -> {

            try (EntityManager et = HibernateUtil.getEmf().createEntityManager()) {

                EntityTransaction transaction = et.getTransaction();
                transaction.begin();

                UserChatId userChatId = new UserChatId();
                userChatId.setUserId(loginUser.getId());
                userChatId.setChatId(activeChat.getId());

                UserChat newChatSubscription = new UserChat();
                newChatSubscription.setId(userChatId);
                newChatSubscription.setChat(activeChat);
                newChatSubscription.setUser(loginUser);
                newChatSubscription.setSubscribedAt(Instant.now());

                et.persist(newChatSubscription);
                transaction.commit();

                if (activeChat.getStartTime() == null) {
                    displayChatNotStartedMessage();
                    return;
                }

                showChatDisplayArea();
                loadChatMessages();
            } catch (Exception ex) {
                System.out.println("Unable to subscribe to chat");
                System.out.println(ex.getMessage());
            }

        });

    }

    private void displayChatMessages() {
        // clear chat areas
        chatArea.setText("");
        // show chat start time
        chatArea.append("Chat started at:" + activeChat.getStartTime() + "\n");

        for (ChatMessage message : messages) {
            chatArea.append(message.getUser().getNickName() + ":" + message.getMessage() + "\n");
        }
    }

}
