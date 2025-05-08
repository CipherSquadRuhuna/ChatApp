package client.gui.shared;

import client.gui.user.common.ChatHandler;
import client.gui.utility.ChatUtility;
import common.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.*;
import server.ServerInterface;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.Naming;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatArea extends JPanel {
    protected final User user;
    /**
     * UI Elements for chat area
     */
    protected final JPanel chatPanel = new JPanel(new BorderLayout());
    private final JTextPane chatArea = new JTextPane();
    private final JTextField messageField = new JTextField();
    private final JButton sendButton = new JButton("Send");
    private final ArrayList<ChatMessage> messages = new ArrayList<>();
    protected Chat chat;
    StyledDocument doc;

    ChatUtility chatUtility;

    public ChatArea(User user) {
        this.user = user;

        // initialize chat utility
        chatUtility = new ChatUtility(chatArea);

        JLabel userMessageLabel = new JLabel(" ");
        ChatHandler chat = new ChatHandler(chatArea, userMessageLabel, chatUtility);
        Thread chatThread = new Thread(chat);
        chatThread.start();
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    /**
     * Clear chat content
     */
    public void clearPanel() {
        chatPanel.removeAll();
        chatPanel.revalidate();
        chatPanel.repaint();
        chatArea.setText("");
    }

    public Boolean isSubscribed() throws Exception {
        try (EntityManager em2 = HibernateUtil.getEmf().createEntityManager()) {

            Long count = em2.createQuery(
                            "SELECT count(uc) FROM UserChat uc WHERE uc.user.id = :userId and uc.chat.id = :chatId", Long.class)
                    .setParameter("userId", user.getId())
                    .setParameter("chatId", chat.getId())
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void handleChatDisplayArea() {
        // check if the user subscribe to the chat
        try {
            if (!isSubscribed()) {
                displaySubscribeToChat();
                return;
            }

            // check chat start
            if (chat.getStartTime() == null) {
                displayChatNotStartedMessage();
                return;
            }

            DisplayChats();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected void loadChatMessages() {
        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        String query = "select m from ChatMessage m where m.chat.id=:chatId";
        TypedQuery<ChatMessage> q = em.createQuery(query, ChatMessage.class);
        System.out.println("Loading Chat Messages for " + chat.getId());
        q.setParameter("chatId", chat.getId());

        messages.clear();
        messages.addAll(q.getResultList());

    }

    protected void DisplayChats() {
        clearPanel();

        // get messages
        loadChatMessages();

        // Chat Display Area
        doc = chatArea.getStyledDocument();
        doc.setCharacterAttributes(0, doc.getLength(), new SimpleAttributeSet(), false);
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        displayChatMessages();
        showMessageSendArea();
    }

    private void displayChatNotStartedMessage() {
        //clear right panel
        clearPanel();

        JPanel chatInfoPanel = new JPanel();
        chatInfoPanel.setLayout(new BoxLayout(chatInfoPanel, BoxLayout.Y_AXIS));

        JLabel messageLabel = new JLabel("Chat not started.. Wait until admin start the chat");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatInfoPanel.add(messageLabel);

        chatPanel.add(chatInfoPanel, BorderLayout.CENTER);
    }

    protected void subscribeToChat(User user, Chat chat) throws Exception {
        try (EntityManager et = HibernateUtil.getEmf().createEntityManager()) {

            EntityTransaction transaction = et.getTransaction();
            transaction.begin();

            UserChatId userChatId = new UserChatId();
            userChatId.setUserId(user.getId());
            userChatId.setChatId(chat.getId());

            UserChat newChatSubscription = new UserChat();
            newChatSubscription.setId(userChatId);
            newChatSubscription.setChat(chat);
            newChatSubscription.setUser(user);
            newChatSubscription.setSubscribedAt(Instant.now());

            et.persist(newChatSubscription);
            transaction.commit();

        } catch (Exception ex) {
            System.out.println("Unable to subscribe to chat");
            System.out.println(ex.getMessage());

            throw ex;
        }

    }

    private void displaySubscribeToChat() {

        clearPanel();

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


        chatPanel.add(subscribePanel, BorderLayout.CENTER);

        subscribeButton.addActionListener((_) -> {

            try (EntityManager et = HibernateUtil.getEmf().createEntityManager()) {

//                EntityTransaction transaction = et.getTransaction();
//                transaction.begin();
//
//                UserChatId userChatId = new UserChatId();
//                userChatId.setUserId(user.getId());
//                userChatId.setChatId(chat.getId());
//
//                UserChat newChatSubscription = new UserChat();
//                newChatSubscription.setId(userChatId);
//                newChatSubscription.setChat(chat);
//                newChatSubscription.setUser(user);
//                newChatSubscription.setSubscribedAt(Instant.now());
//
//                et.persist(newChatSubscription);
//                transaction.commit();

                subscribeToChat(user, chat);

                if (chat.getStartTime() == null) {
                    displayChatNotStartedMessage();
                    return;
                }

                DisplayChats();
                loadChatMessages();


            } catch (Exception ex) {
                System.out.println("Unable to subscribe to chat");
                System.out.println(ex.getMessage());
            }

        });

    }

    private void showMessageSendArea() {


        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        // send message when press enter
        messageField.addActionListener(this::sendMessage);

        // Send message when press send button
        sendButton.addActionListener(this::sendMessage);
    }

    private List<UserChat> getSubscribers() throws Exception {
        try (EntityManager em = HibernateUtil.getEmf().createEntityManager()) {
            String query = "select s from UserChat s join fetch s.user u where s.chat.id=:chatId ";
            TypedQuery<UserChat> q = em.createQuery(query, UserChat.class);
            q.setParameter("chatId", chat.getId());
            return q.getResultList();
        } catch (Exception e) {
            System.out.println("Unable to get subscribers");
            throw e;
        }
    }

    private void displayChatMessages() {

        try {
            chatUtility.displayChatStartedMessage(chat);
            List<UserChat> tempSubscriberList = getSubscribers();

            // scenario where no one send messages but still have some subscribers
            if (messages.isEmpty()) {
                chatUtility.showSubscriberJoinMessage(getSubscribers());
                return;
            }

            // Append each message
            for (ChatMessage message : messages) {
                // check is subscriber join
                try {
                    List<UserChat> toRemove = new ArrayList<>();
                    for (UserChat subscriber : tempSubscriberList) {
                        if (subscriber.getSubscribedAt().isBefore(message.getSentAt())) {
                            chatUtility.displaySubscriberJoinMessage(subscriber);
                            toRemove.add(subscriber);
                        }
                    }

                    tempSubscriberList.removeAll(toRemove);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Unable to display subscriber message");
                    e.printStackTrace();
                }
                chatUtility.printMessage(message);

            }

            // load user that join  after all messages
            for (UserChat subscriber : tempSubscriberList) {
                chatUtility.displaySubscriberJoinMessage(subscriber);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to display chat messages" + e.getMessage());
        }
    }

    private void sendMessage(ActionEvent e) {
        String message = messageField.getText().trim();
        // return if message empty
        if (message.isEmpty()) return;

        // create message object
        ChatMessage messageObj = new ChatMessage();
        messageObj.setMessage(message);


        // set the user who send the message
        messageObj.setUser(user);

        // set active chat to message owner
        messageObj.setChat(chat);

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
            ex.getStackTrace();
        }
    }

}
