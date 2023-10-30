package com.petshelterproject.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    private Long chatId;
    private boolean inCatShelter;
    private String lastMessage;
    private boolean active;

    public User(Long chatId, boolean inCatShelter, String lastMessage, boolean active) {
        this.chatId = chatId;
        this.inCatShelter = inCatShelter;
        this.lastMessage = lastMessage;
        this.active = active;
    }

    public User() {

    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public boolean isInCatShelter() {
        return inCatShelter;
    }

    public void setInCatShelter(boolean inCatShelter) {
        this.inCatShelter = inCatShelter;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return inCatShelter == user.inCatShelter && active == user.active && Objects.equals(chatId, user.chatId) && Objects.equals(lastMessage, user.lastMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, inCatShelter, lastMessage, active);
    }

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", inCatShelter=" + inCatShelter +
                ", lastMessage='" + lastMessage + '\'' +
                ", active=" + active +
                '}';
    }
}
