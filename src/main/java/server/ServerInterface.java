package server;

import client.User;
import models.ChatMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void sendBroadcastMessage(ChatMessage message) throws RemoteException;

    void listenForUsers() throws RemoteException;

    void addOnlineUser(User user) throws RemoteException;
}
