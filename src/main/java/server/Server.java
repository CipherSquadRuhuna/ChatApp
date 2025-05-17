package server;

import client.UserClient;
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
    private final List<UserClient> onlineUsers = Collections.synchronizedList(new ArrayList<>());

    // control server running status
    volatile boolean running = true;

    //Server socket
    ServerSocket serverSocket = null;

    public Server() throws RemoteException {
        super();
    }

    public void listenForUsers() throws RemoteException {
        try {
            // show server running log
            System.out.println("Listening for users...");
            serverSocket = new ServerSocket(3001);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();

                    UserClient user = new UserClient(2, "Asela");
                    user.setSocket(socket);
                    addOnlineUser(user);
                } catch (IOException e) {
                    if (!running) {
                        System.out.println("Server shutting down...");
                        break;
                    }
                    System.out.println("IOException: " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Other Exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            //System.out.println("Client connected.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // when new user login to the chat application
    public void addOnlineUser(UserClient user) throws RemoteException {
        onlineUsers.add(user);
        System.out.println("user added");
    }

    //send message to all online user
    public void sendBroadcastMessage(ChatMessage message) throws RemoteException {
        System.out.println("Sending broadcast message: " + message.getMessage());
        for (UserClient user : onlineUsers) {
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
