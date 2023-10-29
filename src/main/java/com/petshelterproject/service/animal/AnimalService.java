package com.petshelterproject.service.animal;

import com.petshelterproject.model.Animal;


import java.util.List;

public interface AnimalService {


    /**
     * Метод возвращает список из животных по задаваемогу количеству
     * @param
     * @return
     */
    Animal addAnimal(Animal animal);
    Animal getAnimal(Long id);
    Animal editAnimal(Animal animal);
    void deleteAnimal(Long id);
    List<Animal> getAllByKind(long isCat);
    List<Animal> getAllByGender(long isMale);

    Object getAll();
}
