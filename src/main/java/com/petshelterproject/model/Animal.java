package com.petshelterproject.model;

import java.util.Objects;

public class Animal {
    public String typeOfAnimal;
    public String gender;

    public int age;

    public Animal(String typeOfAnimal, String gender, int age) {
        this.typeOfAnimal = typeOfAnimal;
        this.gender = gender;
        this.age = age;
    }

    public String getTypeOfAnimal() {
        return typeOfAnimal;
    }

    public void setTypeOfAnimal(String typeOfAnimal) {
        this.typeOfAnimal = typeOfAnimal;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return age == animal.age && Objects.equals(typeOfAnimal, animal.typeOfAnimal) && Objects.equals(gender, animal.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeOfAnimal, gender, age);
    }
}
