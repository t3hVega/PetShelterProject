package com.petshelterproject.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    private Long chatId;
    private boolean isInCatShelter;
    private String lastMessage;

    public User(Long chatId, boolean isInCatShelter, String lastMessage) {
        this.chatId = chatId;
        this.isInCatShelter = isInCatShelter;
        this.lastMessage = lastMessage;
    }

    public User() {

    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public boolean getIsInCatShelter() {
        return isInCatShelter;
    }

    public void setIsInCatShelter(boolean isInCatShelter) {
        isInCatShelter = isInCatShelter;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", isInCatShelter=" + isInCatShelter +
                ", lastMessage='" + lastMessage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isInCatShelter == user.isInCatShelter && Objects.equals(chatId, user.chatId) && Objects.equals(lastMessage, user.lastMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, isInCatShelter, lastMessage);
    }
}
