package com.petshelterproject.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "adopters")
public class Adopter {
    @Id
    private Long chatId;
    private String name;
    private String data;

    @OneToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    public Adopter(Long chatId, String name, String data, Animal animal) {
        this.chatId = chatId;
        this.name = name;
        this.data = data;
        this.animal = animal;
    }

    public Adopter() {

    }

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

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adopter adopter = (Adopter) o;
        return Objects.equals(chatId, adopter.chatId) && Objects.equals(name, adopter.name) && Objects.equals(data, adopter.data) && Objects.equals(animal, adopter.animal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, name, data, animal);
    }

    @Override
    public String toString() {
        return "Adopter{" +
                "chatId=" + chatId +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                ", animal=" + animal +
                '}';
    }
}
