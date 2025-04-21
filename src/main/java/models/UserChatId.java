package models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class UserChatId implements java.io.Serializable {
    private static final long serialVersionUID = -5463665516030491895L;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "chat_id", nullable = false)
    private Integer chatId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserChatId entity = (UserChatId) o;
        return Objects.equals(this.chatId, entity.chatId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userId);
    }

}