package com.petshelterproject.service;

import com.petshelterproject.model.Animal;


import java.util.Collection;

public interface PetshelterprojectService {


    /**
     * Метод возвращает список из животных по задаваемогу количеству
     * @param amount
     * @return
     */
    Collection<Animal> getAnimal(int amount);

}
