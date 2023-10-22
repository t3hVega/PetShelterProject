package com.petshelterproject.service.animal.impl;

import com.petshelterproject.model.Animal;
import com.petshelterproject.repository.AnimalRepository;
import com.petshelterproject.service.animal.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalServiceImpl implements AnimalService {

    @Autowired
    private final AnimalRepository animalRepository;

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }
    @Override
    public Animal addAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    @Override
    public Animal getAnimal(Long id) {
        return animalRepository.findById(id).get();
    }

    @Override
    public Animal editAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    @Override
    public void deleteAnimal(Long id) {
        animalRepository.deleteById(id);
    }

    @Override
    public List<Animal> getAllByKind(boolean isCat) {
        return animalRepository.findAllByIsCat(isCat);
    }

    @Override
    public List<Animal> getAllByGender(boolean isMale) {
        return animalRepository.findAllByIsMale(isMale);
    }

}
