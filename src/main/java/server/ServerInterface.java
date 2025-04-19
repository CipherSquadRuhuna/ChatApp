package server;

import common.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public void sendBroadcastMessage(Message message) throws RemoteException;
}
