package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public void sendBroadcastMessage(String message) throws RemoteException;
}
