package server;

import client.User;
import models.Chat;
import models.ChatMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server extends UnicastRemoteObject implements ServerInterface {
    // list of online users
    private final List<User> onlineUsers = Collections.synchronizedList(new ArrayList<User>());

    // list of chats with subscribers
    private final ArrayList<Chat> chatList = new ArrayList<>();

    //Server socket
    ServerSocket serverSocket = null;

    public Server() throws RemoteException {
        super();
    }

    public void listenForUsers() throws RemoteException {
        try {
            serverSocket = new ServerSocket(3001);

            while (true) {
                Socket socket = serverSocket.accept();
                // let new thread to handle the request

                User user = new User(2, "Asela");
                user.setSocket(socket);

                try {
                    addOnlineUser(user);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

            }

            //System.out.println("Client connected.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // when new user login to the chat application
    public void addOnlineUser(User user) throws RemoteException {
        onlineUsers.add(user);
        System.out.println("user added");
    }

    // when user logout from the application
    public void removeOnlineUser(User user) throws RemoteException {
        onlineUsers.remove(user);
    }

    // add new chat
    public void addChat(Chat chat) throws RemoteException {
        chatList.add(chat);
    }

    // delete chat
    public void removeChat(Chat chat) throws RemoteException {
        chatList.remove(chat);
    }

    // get all chats
    public ArrayList<Chat> getChatList() throws RemoteException {
        return chatList;
    }

    //send message to all online user
    public void sendBroadcastMessage(ChatMessage message) throws RemoteException {
        System.out.println("Sending broadcast message: " + message.getMessage());
        for (User user : onlineUsers) {
            System.out.println("user: " + user.getSocket());

            // send message using socket
            try {
                Socket socket = user.getSocket();
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
