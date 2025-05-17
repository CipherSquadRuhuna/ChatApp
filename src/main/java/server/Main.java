package server;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class Main {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        //create a registry
        LocateRegistry.createRegistry(1099);
        // create new server
        ServerInterface server = new Server();
        // bind the object
        Naming.rebind("server", server);

        // listen for users
        server.listenForUsers();
    }
}
