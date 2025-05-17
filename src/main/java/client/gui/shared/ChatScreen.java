package client.gui.shared;

import common.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Chat;
import models.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.ArrayList;


public class ChatScreen extends ChatArea {
    protected final JPanel rightPanel = new JPanel(new BorderLayout());

    // chats data
    private final ArrayList<Chat> chats = new ArrayList<>();
    private final JLabel userMessageLabel = new JLabel(" ");


    // chat list
    private JList<String> chatList = new JList<>();

    public ChatScreen(User loginUser) {
        super(loginUser);

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

    private void showChatStatusLabel() {
        rightPanel.add(userMessageLabel, BorderLayout.NORTH);
    }

}