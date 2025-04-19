package client;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
    private int id;
    private String nickname;
    private transient Socket socket;

    private static final long serialVersionUID = 2L;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
