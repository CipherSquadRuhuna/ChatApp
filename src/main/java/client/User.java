package client;

import client.gui.user.ChatHome;

import java.net.Socket;

public class User {
    private int id;
    private String nickname;
    private Socket socket;
    private ChatHome chatHome;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public ChatHome getChatHome() {
        return chatHome;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setChatHome(ChatHome chatHome) {
        this.chatHome = chatHome;
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
