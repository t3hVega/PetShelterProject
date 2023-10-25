package com.petshelterproject.service.adopter.impl;

import com.petshelterproject.model.Adopter;
import com.petshelterproject.model.Animal;
import com.petshelterproject.repository.AdopterRepository;
import com.petshelterproject.repository.AnimalRepository;
import com.petshelterproject.service.adopter.AdopterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdopterServiceImpl implements AdopterService {

    @Autowired
    private final AnimalRepository animalRepository;

    @Autowired
    private final AdopterRepository adopterRepository;

    public AdopterServiceImpl(AnimalRepository animalRepository,
                              AdopterRepository adopterRepository) {
        this.animalRepository = animalRepository;
        this.adopterRepository = adopterRepository;
    }
    @Override
    public void assignAnimal(Long chatId, Long animalId) {
        Adopter adopter = adopterRepository.findByChatId(chatId);
        adopter.setAnimal(animalRepository.findById(animalId).get());
        adopterRepository.save(adopter);
    }

    @Override
    public Animal getAnimal(Long chatId) {
        return adopterRepository.findById(chatId).get().getAnimal();
    }
}
