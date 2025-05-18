package client.gui.user.common;

import client.gui.shared.ChatArea;
import client.gui.utility.ChatUtility;
import common.MessageBroker.NetworkMessage;
import models.User;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Objects;

public class ChatHandler implements Runnable {

    JLabel userMessageLabel;
    ChatUtility chatUtility;
    User user;
    ChatArea chatArea;

    public ChatHandler(ChatArea chatArea,ChatUtility chatUtility, JLabel userMessageLabel, User user) {
        this.userMessageLabel = userMessageLabel;
        this.chatUtility = chatUtility;
        this.user = user;
        this.chatArea = chatArea;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 3001)) {
            while (true) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                // we have message object now
                NetworkMessage message = (NetworkMessage) in.readObject();
                System.out.println(message);

                switch (message.getType()) {
                    case CHAT -> chatUtility.displayUserMessage(message.getChatMessage());
                    case JOIN_MESSAGE -> {
                        if (!Objects.equals(message.getUser().getId(), user.getId())) {
                            chatUtility.displaySubscriberJoinMessage(message.getUser());
                        }
                    }
                    case CHAT_START -> {
                        //skip admin
                        if(Objects.equals(message.getChat().getAdmin().getId(), user.getId())) return;
                        chatArea.setChat(message.getChat());
                        chatArea.DisplaySelectedChat();
                        chatArea.loadChatMessages();
                    }
                    case LEAVE_MESSAGE -> {
                        if (!Objects.equals(message.getUser().getId(), user.getId())) {
                            chatUtility.displaySubscriberLeaveMessage(message.getUser());
                        }
                    }
                }


            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());

        }
    }


}
