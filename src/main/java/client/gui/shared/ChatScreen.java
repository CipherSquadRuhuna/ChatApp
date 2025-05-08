package client.gui.shared;

import client.gui.user.common.ChatHandler;
import common.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.*;
import server.ServerInterface;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Date;

public class ChatScreen extends ChatArea {
    protected final JPanel rightPanel = new JPanel(new BorderLayout());
    // chats data
    private final ArrayList<Chat> chats = new ArrayList<>();
    private final ArrayList<ChatMessage> messages = new ArrayList<>();
    private final JTextField messageField = new JTextField();
    private final JButton sendButton = new JButton("Send");
    private final JLabel userMessageLabel = new JLabel(" ");
    private final User loginUser;

    // shared swing component among methods
    private JTextPane chatArea = new JTextPane();

    private Chat activeChat;

    // chat list
    private JList<String> chatList = new JList<>();

    public ChatScreen(User loginUser) {
        super(loginUser);

        // set current user data
        this.loginUser = loginUser;

        // initialize application
        initialize();
    }

    /**
     * Loading all necessary components for ChatScreen
     */
    private void initialize() {
        setLayout(new BorderLayout());

        // add chat panel to right
        add(chatPanel, BorderLayout.CENTER);

        // display chat list
        displayChats();

        //  status label
        showChatStatusLabel();

    }

    private void handleChatSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selectedIndex = chatList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < chats.size()) {
                Chat selectedChat = chats.get(selectedIndex);
                int chatId = selectedChat.getId();

                // get active chat from database
                EntityManager em = HibernateUtil.getEmf().createEntityManager();
                Chat chatEntity = em.find(Chat.class, chatId);
                setChat(chatEntity);

                handleChatDisplayArea();

            }
        }
    }

//    public void handleChatDisplayArea() {
//        // check if the user subscribe to the chat
//        EntityManager em2 = HibernateUtil.getEmf().createEntityManager();
//        Long count = em2.createQuery(
//                        "SELECT count(uc) FROM UserChat uc WHERE uc.user.id = :userId and uc.chat.id = :chatId", Long.class)
//                .setParameter("userId", loginUser.getId())
//                .setParameter("chatId", activeChat.getId())
//                .getSingleResult();
//
//        // no any subscription found
//        if (count == 0) {
//            displaySubscribeToChat();
//            return;
//        }
//
//        // check chat start
//        if (activeChat.getStartTime() == null) {
//            displayChatNotStartedMessage();
//            return;
//        }
//
//        showChatDisplayArea();
//        loadChatMessages();
//    }

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

        // load chat messages when chatlist selected
        chatList.addListSelectionListener(this::handleChatSelection);
    }

    private void sendMessage(ActionEvent e) {
        String message = messageField.getText().trim();
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
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            em3.close();

        }

        try {
            ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost:1099/server");
            server.sendBroadcastMessage(messageObj);
            messageField.setText("");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

//    protected void showChatDisplayArea() {
//
//        rightPanel.removeAll();
//        rightPanel.revalidate();
//        rightPanel.repaint();
//
//        // Chat Display Area
//        chatArea = new JTextPane();
//         doc = chatArea.getStyledDocument();
//        doc.setCharacterAttributes(0, doc.getLength(), new SimpleAttributeSet(), false);
////        doc.remove(0, doc.getLength());
//        chatArea.setEditable(false);
////        chatArea.setLineWrap(true);
//        JScrollPane chatScrollPane = new JScrollPane(chatArea);
//        rightPanel.add(chatScrollPane, BorderLayout.CENTER);
//
//        showMessageSendArea();
//    }

    private void showChatStatusLabel() {
        rightPanel.add(userMessageLabel, BorderLayout.NORTH);
    }

//    private void showMessageSendArea() {
//        JPanel inputPanel = new JPanel(new BorderLayout());
//        inputPanel.add(messageField, BorderLayout.CENTER);
//        inputPanel.add(sendButton, BorderLayout.EAST);
//        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//        rightPanel.add(inputPanel, BorderLayout.SOUTH);
//
//        // send message when press enter
//        messageField.addActionListener(this::sendMessage);
//
//        // Send message when press send button
//        sendButton.addActionListener(this::sendMessage);
//    }

//    protected void loadChatMessages() {
//        EntityManager em = HibernateUtil.getEmf().createEntityManager();
//        String query = "select m from ChatMessage m where m.chat.id=:chatId";
//        TypedQuery<ChatMessage> q = em.createQuery(query, ChatMessage.class);
//        System.out.println("Loading Chat Messages for " + activeChat.getId());
//        q.setParameter("chatId", activeChat.getId());
//
//        messages.clear();
//        messages.addAll(q.getResultList());
//
//        displayChatMessages();
//    }

//    protected void clearRightPanel() {
//        rightPanel.removeAll();
//        rightPanel.revalidate();
//        rightPanel.repaint();
//    }

//    private void displayChatNotStartedMessage() {
//        //clear right panel
//        clearRightPanel();
//
//        JPanel chatInfoPanel = new JPanel();
//        chatInfoPanel.setLayout(new BoxLayout(chatInfoPanel, BoxLayout.Y_AXIS));
//
//        JLabel messageLabel = new JLabel("Chat not started.. Wait until admin start the chat");
//        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        chatInfoPanel.add(messageLabel);
//
//        rightPanel.add(chatInfoPanel, BorderLayout.CENTER);
//    }

//    private void displaySubscribeToChat() {
//
//        clearRightPanel();
//
//        JPanel subscribePanel = new JPanel();
//        subscribePanel.setLayout(new BoxLayout(subscribePanel, BoxLayout.Y_AXIS));
//
//        JLabel subscribeLabel = new JLabel("Subscribe to chat");
//        subscribeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JButton subscribeButton = new JButton("Subscribe");
//        subscribeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        subscribePanel.add(Box.createVerticalStrut(10)); // spacing
//        subscribePanel.add(subscribeLabel);
//        subscribePanel.add(Box.createVerticalStrut(10)); // spacing
//        subscribePanel.add(subscribeButton);
//
//
//        rightPanel.add(subscribePanel, BorderLayout.CENTER);
//
//        subscribeButton.addActionListener((_) -> {
//
//            try (EntityManager et = HibernateUtil.getEmf().createEntityManager()) {
//
//                EntityTransaction transaction = et.getTransaction();
//                transaction.begin();
//
//                UserChatId userChatId = new UserChatId();
//                userChatId.setUserId(loginUser.getId());
//                userChatId.setChatId(activeChat.getId());
//
//                UserChat newChatSubscription = new UserChat();
//                newChatSubscription.setId(userChatId);
//                newChatSubscription.setChat(activeChat);
//                newChatSubscription.setUser(loginUser);
//                newChatSubscription.setSubscribedAt(Instant.now());
//
//                et.persist(newChatSubscription);
//                transaction.commit();
//
//
//                if (activeChat.getStartTime() == null) {
//
//                    displayChatNotStartedMessage();
//                    return;
//                }
//
//                showChatDisplayArea();
//                loadChatMessages();
//
//            } catch (Exception ex) {
//                System.out.println("Unable to subscribe to chat");
//                System.out.println(ex.getMessage());
//            }
//
//        });
//
//    }

//    private void displayChatMessages() {
//        // Add "Chat started at" line
//        StyleContext sc = StyleContext.getDefaultStyleContext();
//        AttributeSet boldAttr = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Bold, Boolean.TRUE);
//
//        try {
//            String startTimeText = "Chat started at: " + activeChat.getStartTime() + "\n";
//            doc.insertString(doc.getLength(), startTimeText, boldAttr);
//
//            // Append each message
//            for (ChatMessage message : messages) {
//                String userLine = message.getUser().getNickName() + ": ";
//                String messageLine = message.getMessage() + "\n";
//                ImageIcon imageIcon = new ImageIcon(message.getUser().getProfilePicturePath());
//                Image image = imageIcon.getImage().getScaledInstance(20, -1, Image.SCALE_SMOOTH); // scale height to 20px
//                ImageIcon scaledIcon = new ImageIcon(image);
//
//                // Create a style for the image
//                Style imageStyle = chatArea.addStyle("ImageStyle", null);
//                StyleConstants.setIcon(imageStyle, scaledIcon);
//
//                doc.insertString(doc.getLength(), "  ", imageStyle); // two spaces as placeholder for image
//
//
//                // Apply bold to nickname
//                doc.insertString(doc.getLength(), userLine, boldAttr);
//
//                // Normal style for message
//                doc.insertString(doc.getLength(), messageLine, SimpleAttributeSet.EMPTY);
//            }
//        } catch (BadLocationException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
