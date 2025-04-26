package client;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
    private static final long serialVersionUID = 2L;
    private final int id;
    private final String nickname;
    private transient Socket socket;

    public User(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
