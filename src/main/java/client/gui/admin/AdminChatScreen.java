package client.gui.admin;

import client.gui.admin.common.AdminMenu;
import client.gui.common.AppScreen;
import client.gui.shared.ChatScreen;
import common.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Chat;
import models.User;
import models.UserChat;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AdminChatScreen extends ChatScreen {
    JPanel userPanel;

    public AdminChatScreen(AppScreen appScreen) {
        super(appScreen.getUser());

        AdminMenu menu = new AdminMenu(appScreen);
        add(menu.getMenuBar(), BorderLayout.NORTH);
    }

    public AdminChatScreen(AppScreen appScreen, Chat chat) {
        this(appScreen);
        // set selected chat
        setChat(chat);
    }

    @Override
    public void handleChatDisplayArea() {
        if (getChat().getStartTime() == null) {
            displayStartChatScreen();
            return;
        }

        DisplayChatSection();
        loadChatMessages();
        displayUserAddArea();
    }

    @Override
    protected void DisplayChatSection() {
        clearPanel();
        // get messages
        loadChatMessages();

        // make the scroll more smooth
        addChatAreaToPanel();

        // display chat messages
        displayChatMessages();

        // if chat stop hide the message send box
        if (chat.getEndTime() == null) {
            displayMessageSendArea();
        }

    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() throws Exception {
        try (EntityManager em = HibernateUtil.getEmf().createEntityManager()) {
            String query = "select u from User u";
            TypedQuery<User> q = em.createQuery(query, User.class);
            return q.getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Get all subscribers for selected chat
     */
    public List<UserChat> getAllSubscribers() throws Exception {
        try (EntityManager em = HibernateUtil.getEmf().createEntityManager()) {
            String query = "select s from UserChat s where s.chat = :chat";
            TypedQuery<UserChat> q = em.createQuery(query, UserChat.class);
            q.setParameter("chat", getChat());
            return q.getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Get user who are not subscribe to the current chat
     */
    public ArrayList<User> getAllUnsubscribers() {
        try {

            ArrayList<User> unsubscribers = new ArrayList<>();

            List<User> allUsers = getAllUsers();

            List<Integer> subscriberIds = new ArrayList<>();
            getAllSubscribers().forEach(sub -> subscriberIds.add(sub.getUser().getId()));

            for (User user : allUsers) {
                if (!subscriberIds.contains(user.getId())) {
                    unsubscribers.add(user);
                }
            }

            return unsubscribers;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void clearUserPanel() {
        // clear userpanel if any
        chatPanel.remove(userPanel);
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    public void displayUserAddArea() {
        userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel chatStartLabel = new JLabel("Add User to Chat");
        chatStartLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPanel.add(chatStartLabel);

        chatPanel.add(userPanel, BorderLayout.EAST);

        ArrayList<User> users = getAllUnsubscribers();

        if (users.isEmpty()) {
            JLabel message = new JLabel("All users are in the chat");
            message.setAlignmentX(Component.CENTER_ALIGNMENT);
            userPanel.add(message);
            return;
        }

        if (chat.getEndTime() != null) {
            JLabel message = new JLabel("User can't be added to the stopped chat");
            message.setAlignmentX(Component.CENTER_ALIGNMENT);
            userPanel.add(message);
            return;
        }

        // Convert users to strings for JList
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (User user : users) {
            listModel.addElement(user.getNickName()); // or user.toString()
        }

        JList<String> userList = new JList<>(listModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setVisibleRowCount(-1);

        // Wrap in JScrollPane
        JScrollPane listScroller = new JScrollPane(userList);
        listScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPanel.add(listScroller);

        JButton addButton = new JButton("Add to Chat");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> {
            int selectedIndex = userList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedUserName = userList.getSelectedValue();
                // Find corresponding User object
                for (User user : users) {
                    if (user.getNickName().equals(selectedUserName)) {
                        addToChat(user); // Replace with your actual method to add user to chat
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(userPanel, "Please select a user to add.");
            }
        });
        userPanel.add(addButton);

        chatPanel.add(userPanel, BorderLayout.EAST);
        chatPanel.revalidate(); // Refresh UI
        chatPanel.repaint();
    }

    private void addToChat(User user) {
        // Your logic here, e.g., add user to active chat session
        try {
            System.out.println("Adding user to chat: " + user.getNickName());
            subscribeToChat(user, chat);

            clearPanel();
            DisplayChatSection();
            clearUserPanel();

            // refresh user list
            displayUserAddArea();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void displayStartChatScreen() {
        clearPanel();

        JPanel subscribePanel = new JPanel();
        subscribePanel.setLayout(new BoxLayout(subscribePanel, BoxLayout.Y_AXIS));

        JLabel chatStartLabel = new JLabel("Start the chat");
        chatStartLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        subscribePanel.add(Box.createVerticalStrut(10)); // spacing
        subscribePanel.add(chatStartLabel);
        subscribePanel.add(Box.createVerticalStrut(10)); // spacing
        subscribePanel.add(startButton);

        startButton.addActionListener(e -> {
            try (EntityManager etm = HibernateUtil.getEmf().createEntityManager()) {
                etm.getTransaction().begin();
                Chat chat = etm.find(Chat.class, getChat().getId());
                chat.setStartTime(Instant.now());
                etm.persist(chat);
                etm.getTransaction().commit();

                // set current active chat as started chat
                setChat(chat);

                DisplayChatSection();
                loadChatMessages();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        chatPanel.add(subscribePanel, BorderLayout.CENTER);

    }
}
