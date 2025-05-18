package common.MessageBroker;

import models.Chat;
import models.ChatMessage;
import models.User;

import java.io.Serial;
import java.io.Serializable;

public class NetworkMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private NetworkMessageType type;
    private String message;
    private ChatMessage chatMessage;
    private Chat chat;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public NetworkMessage() {
    }

    public NetworkMessageType getType() {
        return type;
    }

    public void setType(NetworkMessageType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
