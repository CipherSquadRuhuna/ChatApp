package server;

import client.UserClient;
import common.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.ChatFile;
import models.ChatMessage;

import java.io.File;
import java.io.FileWriter;
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

                    UserClient user = new UserClient();
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
        saveChat(message);
        for (UserClient user : onlineUsers) {
            System.out.println("user: " + user.getSocket());

            // send message using socket
            try {
                Socket socket = user.getSocket();
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(message);
            } catch (IOException e) {
//                throw new RuntimeException(e);
                System.out.println("Other Exception: " + e.getMessage());
            }
        }
    }

    public void saveChat(ChatMessage chatMessage) {
        //find the chat file location
        EntityManager em = HibernateUtil.getEmf().createEntityManager();
        String query = "select m from ChatFile m where m.chat.id=:chatId";
        TypedQuery<ChatFile> chatFile = em.createQuery(query, ChatFile.class);
        chatFile.setParameter("chatId", chatMessage.getChat().getId());

        String filePath = chatFile.getSingleResult().getFilePath();
        if (filePath == null) return;

        File logFile = new File(filePath);
        try (FileWriter fw = new FileWriter(logFile, true)) {
            String messageLog = chatMessage.getMessage() + " :by " + chatMessage.getUser().getUsername();
            String messageLogDate = "\t at: " + chatMessage.getSentAt() + "\n";
            fw.append(messageLog + messageLogDate);
//            fw.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();

        }
    }


}
