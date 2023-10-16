package com.petshelterproject.model;

public enum KindOfAnimal {

    CAT("Марсик"), DOG("Тузик"), BIRD("Вуди");

    private final String code;

    public String getCode() {
        return code;
    }

    KindOfAnimal(String code){
        this.code = code;
    }
}
