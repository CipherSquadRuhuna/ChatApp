package client.gui.admin;

import client.gui.admin.common.AdminMenu;
import client.gui.common.AppScreen;
import client.gui.shared.ChatScreen;
import common.HibernateUtil;
import jakarta.persistence.EntityManager;
import models.Chat;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;

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

        showChatDisplayArea();
        loadChatMessages();
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

                showChatDisplayArea();
                loadChatMessages();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        chatPanel.add(subscribePanel, BorderLayout.CENTER);

    }
}
