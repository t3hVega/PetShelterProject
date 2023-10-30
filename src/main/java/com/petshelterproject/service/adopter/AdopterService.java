package com.petshelterproject.service.adopter;

import com.petshelterproject.model.Animal;

public interface AdopterService {
    void assignAnimal(Long chatId, Long animalId);
    Animal getAnimal(Long chatId);
}
