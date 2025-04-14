package server;

import client.User;
import common.Chat;

import java.util.ArrayList;

public class Server {
    // list of online users
    private final ArrayList<User> onlineUsers = new ArrayList<User>();

    // list of chats with subscribers
    private final ArrayList<Chat> chatList = new ArrayList<>();

    // when new user login to the chat application
    public void addOnlineUser(User user) {
        onlineUsers.add(user);
    }

    // when user logout from the application
    public void removeOnlineUser(User user) {
        onlineUsers.remove(user);
    }

    // add new chat
    public void addChat(Chat chat) {
        chatList.add(chat);
    }

    // delete chat
    public void removeChat(Chat chat) {
        chatList.remove(chat);
    }

    // get all chats
    public ArrayList<Chat> getChatList() {
        return chatList;
    }




}
