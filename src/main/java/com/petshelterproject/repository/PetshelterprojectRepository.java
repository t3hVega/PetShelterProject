package com.petshelterproject.repository;

import com.petshelterproject.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetshelterprojectRepository extends JpaRepository<Animal, Long> {

    List<Animal> findAllByAnimal();
}
