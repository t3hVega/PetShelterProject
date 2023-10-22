package com.petshelterproject.controller;

import com.petshelterproject.model.Animal;
import com.petshelterproject.service.animal.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animal")
public class AnimalController {

    @Autowired
    private final AnimalService animalService;

    public AnimalController(AnimalService AnimalService) {
        this.animalService = AnimalService;
    }

    @PostMapping
    public Animal add(@RequestBody Animal animal) {
        return animalService.addAnimal(animal);
    }

    @GetMapping("{id}")
    public Animal get(@PathVariable Long id) {
        return animalService.getAnimal(id);
    }

    @PutMapping
    public ResponseEntity<Animal> edit(@RequestBody Animal animal) {
        Animal foundAnimal = animalService.editAnimal(animal);
        if (foundAnimal == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundAnimal);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        animalService.deleteAnimal(id);
    }

    @GetMapping("/by-kind/{isCat}")
    public List<Animal> getAllByKind(@PathVariable boolean isCat) {
        return animalService.getAllByKind(isCat);
    }

    @GetMapping("/by-gender/{isMale}")
    public List<Animal> getAllByGender(@PathVariable boolean isMale) {
        return animalService.getAllByGender(isMale);
    }

}

