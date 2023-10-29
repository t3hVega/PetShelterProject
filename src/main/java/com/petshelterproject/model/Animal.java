package com.petshelterproject.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean isCat;
    private boolean isMale;
    private int age;
    private String notes;

    public Animal() {
        this.name = name;
        this.isCat = isCat;
        this.isMale = isMale;
        this.age = age;
        this.notes = notes;
    }

    public Animal(String грета, String female, int i, String dog) {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsCat() {
        return isCat;
    }

    public void setIsCat(boolean isCat) {
        this.isCat = isCat;
    }

    public boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(boolean male) {
        this.isMale = male;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return isCat == animal.isCat && isMale == animal.isMale && age == animal.age && Objects.equals(id, animal.id) && Objects.equals(name, animal.name) && Objects.equals(notes, animal.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isCat, isMale, age, notes);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isCat=" + isCat +
                ", isMale=" + isMale +
                ", age=" + age +
                ", notes='" + notes + '\'' +
                '}';
    }

    public String getKindOfAnimal() {
        return null;
    }

    public String getTypeOfAnimal() {
        return null;
    }
}
