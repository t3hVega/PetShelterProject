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
    private KindOfAnimal kind;
    private int age;
    private String notes;



    public Animal(String name, KindOfAnimal kind, int age, String notes) {
        this.name = name;
        this.age = age;
        this.kind = kind;
        this.notes = notes;
    }

    public Animal() {

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

    public KindOfAnimal getKind() {
        return kind;
    }

    public void setKind(KindOfAnimal kind) {
        this.kind = kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return age == animal.age && Objects.equals(id, animal.id) && Objects.equals(name, animal.name) && kind == animal.kind && Objects.equals(notes, animal.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, kind, notes);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", kind=" + kind +
                ", notes='" + notes + '\'' +
                '}';
    }
}
