package server;

import models.ChatMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public void sendBroadcastMessage(ChatMessage message) throws RemoteException;
    public void listenForUsers() throws RemoteException;
}
