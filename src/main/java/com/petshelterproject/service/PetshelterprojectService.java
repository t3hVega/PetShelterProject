package com.petshelterproject.service;

import com.petshelterproject.model.Animal;


import java.util.Collection;

public interface PetshelterprojectService {

    Collection<Animal> getAnimal(int amount);
}
