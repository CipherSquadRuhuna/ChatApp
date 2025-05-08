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
    public AdminChatScreen(AppScreen appScreen) {
        super(appScreen.getUser());

        AdminMenu menu = new AdminMenu(appScreen);
        add(menu.getMenuBar(), BorderLayout.NORTH);
    }

    @Override
    public void handleChatDisplayArea() {
        if (getChat().getStartTime() == null) {
            displayStartChatScreen();
            return;
        }

        DisplayChats();
        loadChatMessages();
        displayUserAddArea();
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() throws Exception {
        try(EntityManager em = HibernateUtil.getEmf().createEntityManager()) {
            String query = "select u from User u";
            TypedQuery<User> q = em.createQuery(query, User.class);
           return q.getResultList();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Get all subscribers for selected chat
     */
    public List<UserChat> getAllSubscribers() throws Exception {
        try(EntityManager em = HibernateUtil.getEmf().createEntityManager()) {
            String query = "select s from UserChat s where s.chat = :chat";
            TypedQuery<UserChat> q = em.createQuery(query, UserChat.class);
            q.setParameter("chat", getChat());
            return q.getResultList();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Get user who are not subscribe to the current chat
     */
    public void getAllUnsubscribers(){
        try {
            System.out.println(getAllUsers());
            System.out.println(getAllSubscribers());
            ArrayList<User> unsubscribers = new ArrayList<>();

            List<User> allUsers = getAllUsers();
            List<UserChat> allSubscribers = getAllSubscribers();

            for (User user : allUsers) {
                if(!isSubscribed()){
                    unsubscribers.add(user);
                }
            }

            for (User u:unsubscribers){
                System.out.println(u.getNickName());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void displayUserAddArea(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel chatStartLabel = new JLabel("Add User to Chat");
        chatStartLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(chatStartLabel);

        chatPanel.add(panel, BorderLayout.EAST);

        getAllUnsubscribers();
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

                DisplayChats();
                loadChatMessages();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        chatPanel.add(subscribePanel, BorderLayout.CENTER);

    }
}
