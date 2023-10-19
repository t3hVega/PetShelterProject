package com.petshelterproject.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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
