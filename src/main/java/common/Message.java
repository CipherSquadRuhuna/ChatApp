package common;

import client.User;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private User user;
    private String messageText;
    private Date date;
    private Chat chat;

    public Message(int id, User user, String messageText, Date date, Chat chat) {
        this.id = id;
        this.user = user;
        this.messageText = messageText;
        this.date = date;
        this.chat = chat;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    //    @Override
//    public String toString() {
//        return "Message{" +
//                "id=" + id +
//                ", user=" + user +
//                ", messageText='" + messageText + '\'' +
//                ", date=" + date +
//                ", chat=" + chat +
//                '}';
//    }

    public String toString() {
        return user.getNickname() + " " + messageText;
    }
}
