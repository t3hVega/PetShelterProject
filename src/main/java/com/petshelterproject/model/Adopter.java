package com.petshelterproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "adopters")
public class Adopter {
    @Id
    private Long chatId;
    private String name;
    private String data;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adopter adopter = (Adopter) o;
        return Objects.equals(chatId, adopter.chatId) && Objects.equals(name, adopter.name) && Objects.equals(data, adopter.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, name, data);
    }

    @Override
    public String toString() {
        return "Adopter{" +
                "chatId=" + chatId +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
