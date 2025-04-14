package common;

import client.User;

import java.util.ArrayList;

public class Chat {
    private int id;
    private String name;
    private boolean isStarted;
    private ArrayList<Message> messages;
    private ArrayList<User> subscribers;

    public Chat(int id, String name) {
        this.id = id;
        this.name = name;
        this.messages = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
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

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(ArrayList<User> subscribers) {
        this.subscribers = subscribers;
    }

    public void sendMessage(Message message) {
        messages.add(message);
    }

    public void addSubscriber(User user) {
        subscribers.add(user);
    }

    public void removeSubscriber(User user) {
        subscribers.remove(user);
    }
}
