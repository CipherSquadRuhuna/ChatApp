package server;

import client.User;
import common.Chat;
import common.Message;

import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // create new server
        Server server = new Server();

        // create 5 users
        User Alpha = new User(1, "Alpha");
        User Bravo = new User(2, "Bravo");
        User Charlie = new User(3, "Charlie");
        User Delta = new User(4, "Delta");
        User Echo = new User(5, "Echo");

        // create 3 chats
        Chat level01_chat = new Chat(1,"level01_chat");
        Chat level02_chat = new Chat(2,"level02_chat");
        Chat level03_chat = new Chat(3,"level03_chat");

        //Alpha,Bravo,Charlie subscribe to level01_chat
        level01_chat.addSubscriber(Alpha);
        level01_chat.addSubscriber(Bravo);
        level01_chat.addSubscriber(Charlie);

        // Alpha say hi in level01_chat
        level01_chat.sendMessage(new Message(1,Alpha,"Hi",new Date(),level01_chat));
        level01_chat.sendMessage(new Message(1,Alpha,"Hi Again",new Date(),level01_chat));

        // Bravo say hello in level01_chat
        level01_chat.sendMessage(new Message(1,Bravo,"Hello",new Date(),level01_chat));
        level01_chat.sendMessage(new Message(1,Bravo,"Hello Again",new Date(),level01_chat));

        // Charlie say hey in level01_chat
        level01_chat.sendMessage(new Message(1,Charlie,"Hey",new Date(),level01_chat));
        level01_chat.sendMessage(new Message(1,Charlie,"Hey Again",new Date(),level01_chat));

        // test - get all level01_chat messages
        ArrayList<Message> messages = level01_chat.getMessages();

        for (Message m : messages) {
            System.out.println(m.getUser().getNickname() + ":"+m.getMessageText());
        }

        // print list of subscribers in a chat
        for (User u: level01_chat.getSubscribers()) {
            System.out.println(u.getNickname());
        }



    }
}
