package com.petshelterproject.model;

public enum KindOfAnimal {
    MALE_CAT("Кот"), FEMALE_CAT("Кошка"), MALE_DOG("Пес"), FEMALE_DOG("Собака");
    private final String code;
    public String getCode() {
        return code;
    }

    KindOfAnimal(String code){
        this.code = code;
    }
}
