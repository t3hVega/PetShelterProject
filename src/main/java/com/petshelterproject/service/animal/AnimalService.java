package com.petshelterproject.service.animal;

import com.petshelterproject.model.Animal;
import com.petshelterproject.model.KindOfAnimal;


import java.util.Collection;
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
    List<Animal> getAllByKind(KindOfAnimal kind);
}
