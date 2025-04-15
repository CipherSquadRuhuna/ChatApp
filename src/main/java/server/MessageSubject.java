package server;

import common.MessageObserver;

import java.util.ArrayList;
import java.util.List;

public class MessageSubject {
    private static MessageSubject instance;
    private final List<MessageObserver> observers = new ArrayList<>();
    private String message;

    // no one can initialize - singletone
    private MessageSubject() {}

    // return instance
    public static MessageSubject getInstance() {
        if (instance == null) {
            instance = new MessageSubject();
        }
        return instance;
    }

    // add new observer
    public void addObserver(MessageObserver observer) {
        System.out.println(observer);
        observers.add(observer);
    }

    // remove observer
    public void removeObserver(MessageObserver observer) {
        observers.remove(observer);
    }

    // set message
    public void setMessage(String message) {
        this.message = message;
        notifyObservers();
    }

    // notify observers
    public void notifyObservers() {
        System.out.println("Observers: " + this.observers);
        for (MessageObserver observer : observers) {
            observer.messageReceived(this.message);
        }
    }

}
