package client;

import java.io.Serializable;
import java.net.Socket;

public class UserClient implements Serializable {
    private static final long serialVersionUID = 2L;
    private int id;
    private transient Socket socket;

    public UserClient() {

    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
