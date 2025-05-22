package server;

import client.UserClient;
import models.Chat;
import models.ChatMessage;
import models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void sendBroadcastMessage(ChatMessage message) throws RemoteException;

    void listenForUsers() throws RemoteException;

    void addOnlineUser(UserClient user) throws RemoteException;

    void sendUserJoinMessage(User joinedUser, Chat chat) throws RemoteException;

    void sendAdminStartChat(Chat chat) throws RemoteException;

    void sendUserLeaveChat(User joinedUser, Chat chat) throws RemoteException;
}
