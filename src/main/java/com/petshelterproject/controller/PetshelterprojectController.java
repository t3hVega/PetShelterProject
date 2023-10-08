package com.petshelterproject.controller;

import com.petshelterproject.model.Animal;
import com.petshelterproject.service.PetshelterprojectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

    @RestController
    @RequestMapping("/model")
    public class PetshelterprojectController {
        private final PetshelterprojectService PetshelterprojectService;

        public PetshelterprojectController(PetshelterprojectService PetshelterprojectService) {
            this.PetshelterprojectService = PetshelterprojectService;
        }

        @GetMapping("/get/{amount}")
        public Collection<Animal> getAnimal(@PathVariable int amount) {
            return PetshelterprojectService.getAnimal(amount);
        }
    }

