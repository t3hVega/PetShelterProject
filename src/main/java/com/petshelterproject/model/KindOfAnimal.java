package com.petshelterproject.model;

public enum KindOfAnimal {
    MALE_CAT("Маркис"), FEMALE_CAT("Сима"), MALE_DOG("Тузик"), FEMALE_DOG("Данка");
    private final String code;
    public String getCode() {
        return code;
    }

    KindOfAnimal(String code){
        this.code = code;
    }
}
