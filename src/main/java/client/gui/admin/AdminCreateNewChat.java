package client.gui.admin;

import client.gui.admin.common.AdminMenu;
import client.gui.common.AppScreen;
import common.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import models.Chat;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class AdminCreateNewChat extends JPanel {
    private JTextField chatNameField;
    private JList<User> subscriberList;
    private JButton addButton;
    private AppScreen appScreen;

    public AdminCreateNewChat(AppScreen appScreen) {
        this.appScreen = appScreen;

        //set border layout
        setLayout(new BorderLayout());

        // create admin menu and IT to screen
        AdminMenu menu = new AdminMenu(appScreen);
        add(menu.getMenuBar(), BorderLayout.NORTH);

        // create chatNameInput Screen
        createChatInputScreen();

    }

    private void createChatInputScreen() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Chat Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Chat Name:"), gbc);

        // ChatName input
        chatNameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(chatNameField, gbc);

        // create button
        gbc.gridx = 0;
        gbc.gridy = 1;
        addButton = new JButton("Add");
        formPanel.add(addButton, gbc);

        // add event lister for the creae button
        addButton.addActionListener(e -> {
            String chatName = chatNameField.getText();

            // show the error message in empty
            if (chatName.isEmpty()) {
                JOptionPane.showMessageDialog(appScreen, "Please enter a chat name.");
                return;
            }
            createChat(chatName);

        });

        add(formPanel, BorderLayout.CENTER);
    }

    private void createChat(String chatName) {
        Chat chat = new Chat();
        chat.setTitle(chatName);
        chat.setCreatedAt(new Date().toInstant());

        // get sample user from database
        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        User chatAdmin = em.find(User.class, 1);

        System.out.println(chatAdmin);
        chat.setAdmin(chatAdmin);
        em.close();


        EntityManager entityManager = HibernateUtil.getEmf().createEntityManager();

        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(chat);
            transaction.commit();
            System.out.println("Chat created");
            appScreen.showAdminChatScreen();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}
