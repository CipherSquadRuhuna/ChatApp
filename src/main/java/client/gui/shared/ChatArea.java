package client.gui.shared;

import client.gui.user.common.ChatHandler;
import client.gui.utility.ChatUtility;
import common.HibernateUtil;
import common.MessageBroker.NetworkMessage;
import common.MessageBroker.NetworkMessageType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.*;
import server.ServerInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
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
    private final JPanel chatArea = new JPanel();
    private final JTextField messageField = new JTextField();
    private final JButton sendButton = new JButton("Send");
    private final ArrayList<ChatMessage> messages = new ArrayList<>();
    protected Chat chat;
    JPanel inputPanel;
    ChatUtility chatUtility;

    public ChatArea(User user) {
        this.user = user;

        // set chat area properties
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setBackground(Color.WHITE);

        // initialize chat utility
        chatUtility = new ChatUtility(chatArea);

        // initialize chat status label
        JLabel userMessageLabel = new JLabel(" ");

        // This thread make sure append message when the new message send by someone else.
        ChatHandler chat = new ChatHandler(this,chatUtility, userMessageLabel,user);
        Thread chatThread = new Thread(chat);
        chatThread.start();
    }

    /**
     * Get current selected chat
     *
     * @return Chat
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * Set the selecte chat
     *
     * @param chat
     */
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
    }

    /**
     * Check if the current login user is subscribed to the current active chat
     */
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

    /**
     * Check if the current login user leave from the chat
     */
    public Boolean isUnsubscribed() {
        return getUserChat().getUnsubscribedAt() != null;
    }

    /**
     * Display chat messages or relevant section base on state
     * This method control which section to show for user/admin base on the current intraction of chat
     * For Example: Subscriber Button for Not Subscriber users
     */
    public void handleChatDisplayArea() {
//        // set the chat property in chat utility
//        chatUtility.setChat(chat);

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

            // if nor barriers then show the chat screen
            DisplaySelectedChat();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * load chat messages into messages List
     */
    public void loadChatMessages() {
        // set the chat property in chat utility
        chatUtility.setChat(chat);

        try {
            EntityManager em = HibernateUtil.getEmf().createEntityManager();
            String query = "select m from ChatMessage m where m.chat.id=:chatId";
            TypedQuery<ChatMessage> q = em.createQuery(query, ChatMessage.class);
            System.out.println("Loading Chat Messages for " + chat.getId());
            q.setParameter("chatId", chat.getId());

            messages.clear();
            messages.addAll(q.getResultList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Display chat section with relevant action for users
     * This method make sure to load all chat message to the chat message
     * display area, also make sure not to show chat send input,
     * <ul>
     *     <li>If use leave from chat</li>
     *     <li>If chat stopped already</li>
     * </ul>
     */
    public void DisplaySelectedChat() {
        clearPanel();

        // get messages
        loadChatMessages();

        // display chat all chat messages on screen
        displayChatMessages();

        //display leave from chat, if user leave from chat
        if (isUnsubscribed()) {
            displayLeaveFromChatMessage();
            return;
        }

        // show the message send area only if not the chat stop
        if (chat.getEndTime() == null) {
            displayMessageSendArea();
        }
    }

    /**
     * Add chat area to the main panel
     */
    protected void addChatMessageDisplayAreaToPanel() {
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
    }

    /**
     * Display chat not started message for users, when chat is not started by admin
     */
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

    /**
     * Make the user subscriber for given chat
     */
    protected void subscribeToChat(User user, Chat chat) {
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

        }

    }

    /**
     * Display subscribe button for users when user not subscribe for the chat
     */
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

        // handle when user subscribe chat
        subscribeButton.addActionListener(this::handleUserSubscribeToChat);
    }

    /**
     * Handle when user click subscriber button on a chat
     * @param actionEvent
     */
    private void handleUserSubscribeToChat(ActionEvent actionEvent) {
        subscribeToChat(user, chat);

        if (chat.getStartTime() == null) {
            displayChatNotStartedMessage();
            return;
        }

        DisplaySelectedChat();
        loadChatMessages();

        try {
            ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost:1099/server");
            server.sendUserJoinMessage(user, chat);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.getStackTrace();
        }

    }

    /**
     * display message send area
     */
    protected void displayMessageSendArea() {
        inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        // send message when press enter
        messageField.addActionListener(this::sendMessage);

        // Send message when press send button
        sendButton.addActionListener(this::sendMessage);
    }

    /**
     * Hide message send area from user
     */
    private void hideMessageSendArea() {
        chatPanel.remove(inputPanel);
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    /**
     * Display that user left from chat
     */
    private void displayLeaveFromChatMessage() {
        JPanel LeaveMessagePanel = new JPanel(new BorderLayout());
        JLabel messageLabel = new JLabel("You Leave from chat");
        LeaveMessagePanel.add(messageLabel, BorderLayout.CENTER);
        LeaveMessagePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chatPanel.add(LeaveMessagePanel, BorderLayout.SOUTH);
    }

    /**
     * Get all subscribers from given chat, that not left chat
     */
    private List<UserChat> getSubscribers() throws Exception {
        try (EntityManager em = HibernateUtil.getEmf().createEntityManager()) {
            String query = "select s from UserChat s join fetch s.user u where s.chat.id=:chatId and s.unsubscribedAt is null";
            TypedQuery<UserChat> q = em.createQuery(query, UserChat.class);
            q.setParameter("chatId", chat.getId());
            return q.getResultList();
        } catch (Exception e) {
            System.out.println("Unable to get subscribers");
            throw e;
        }
    }

    /**
     * Display chat messages f
     */
    protected void displayChatMessages() {
        addChatMessageDisplayAreaToPanel();
        try {
            chatArea.removeAll();
            chatArea.revalidate();
            chatArea.repaint();

            chatUtility.displayChatStartedMessage(chat);
            List<UserChat> tempSubscriberList = getSubscribers();

            // scenario where no one send messages but still have some subscribers
            if (messages.isEmpty()) {
                chatUtility.showSubscriberJoinMessage(getSubscribers());
                return;
            }


            // Append each message
            for (ChatMessage message : messages) {

                // check is subscriber join meanwhile
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
                //display the message
                chatUtility.displayUserMessage(message);

            }

            // load user that join  after all messages
            for (UserChat subscriber : tempSubscriberList) {
                chatUtility.displaySubscriberJoinMessage(subscriber);
            }

            // show chat stop message is stopped
            if (chat.getEndTime() != null) {
                System.out.println("Chat ended");
                chatUtility.displayInfoMessage("Chat Stopped At: " + chat.getEndTime());
//                hideMessageSendArea();
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to display chat messages" + e.getMessage());
        }
    }

    /**
     * get current login user chat session
     */
    private UserChat getUserChat() {
        try (EntityManager em = HibernateUtil.getEmf().createEntityManager()) {

            UserChatId userChatId = new UserChatId();
            userChatId.setUserId(user.getId());
            userChatId.setChatId(chat.getId());

            return em.find(UserChat.class, userChatId);

        } catch (Exception ex) {
            System.out.println("Unable to find the chat");
            System.out.println(ex.getMessage());

            throw ex;
        }
    }

    /**
     * End current chat
     */
    private void endChat() {
        /**
         * Update database record that chat stopped
         */
        try (EntityManager em = HibernateUtil.getEmf().createEntityManager()) {
            em.getTransaction().begin();
            Chat chat = em.find(Chat.class, getChat().getId());
            System.out.println(chat);
            chat.setEndTime(Instant.now());
            em.persist(chat);
            em.getTransaction().commit();
        }

        /**
         * Update file record that chat stopped
         */
        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        String query = "select m from ChatFile m where m.chat.id=:chatId";
        TypedQuery<ChatFile> chatFile = em.createQuery(query, ChatFile.class);
        chatFile.setParameter("chatId", getChat().getId());

        String filePath = chatFile.getSingleResult().getFilePath();
        if (filePath == null) return;

        File logFile = new File(filePath);
        try (FileWriter fw = new FileWriter(logFile, true)) {
            String messageLog = "Chat Stopped At: " + Instant.now();
            fw.append(messageLog + messageLog);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();

        }
    }

    /**
     * make the current user left the chat
     */
    private void leaveUserFromChat() {
        try (EntityManager em = HibernateUtil.getEmf().createEntityManager()) {

            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            UserChatId userChatId = new UserChatId();
            userChatId.setUserId(user.getId());
            userChatId.setChatId(chat.getId());

            UserChat userChat = em.find(UserChat.class, userChatId);

            if (userChat == null) {
                System.out.println("UserChat not found");
                return;
            }

            userChat.setUnsubscribedAt(Instant.now());
            em.persist(userChat);

            transaction.commit();

        } catch (Exception ex) {
            System.out.println("Unable to subscribe to chat");
            System.out.println(ex.getMessage());

            throw ex;
        }
    }

    /**
     * Add new message to the chat by current user
     */
    private void sendMessage(ActionEvent e) {
        String message = messageField.getText().trim();
        // return if message empty
        if (message.isEmpty()) return;

        // if say bye
        if (message.equals("/bye")) {
            try {
                messageField.setText("");
                leaveUserFromChat();
                hideMessageSendArea();
                displayLeaveFromChatMessage();
                System.out.println(getSubscribers());

                try {
                    ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost:1099/server");
                    server.sendUserLeaveChat(user, getChat());

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.getStackTrace();
                }

                if (getSubscribers().isEmpty()) {
                    System.out.println("Subscriber list is empty");
                    chatUtility.displayInfoMessage("Chat Stopped at " + Instant.now());
                    endChat();
                }


            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            return;
        }


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
