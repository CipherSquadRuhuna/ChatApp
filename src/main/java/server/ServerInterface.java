package server;

import client.UserClient;
import models.ChatMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void sendBroadcastMessage(ChatMessage message) throws RemoteException;

    void listenForUsers() throws RemoteException;

    void addOnlineUser(UserClient user) throws RemoteException;
}
