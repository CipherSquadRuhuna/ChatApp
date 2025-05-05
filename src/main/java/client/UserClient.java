package client;

import java.io.Serializable;
import java.net.Socket;

public class UserClient implements Serializable {
    private int id;
    private String name;
    private String username;
    private String nickname;
    private transient Socket socket;

    private static final long serialVersionUID = 2L;

    public UserClient(int id, String name, String username, String nickname) {
        this.id = id;
        this.name = name;
        this.username = username;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UserClient(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
