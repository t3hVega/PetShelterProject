package com.petshelterproject.controller;

import com.petshelterproject.model.Animal;
import com.petshelterproject.service.adopter.AdopterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adopter")
public class AdopterController {

    @Autowired
    private final AdopterService adopterService;

    public AdopterController(AdopterService adopterService) {
        this.adopterService = adopterService;
    }

    @PutMapping("{chatId}/{animalId}")
    public void assignAnimal(
            @PathVariable Long chatId,
            @PathVariable Long animalId) {
        adopterService.assignAnimal(chatId, animalId);
    }

    @GetMapping("{chatId}")
    public Animal getAnimal(Long chatId) {
        return adopterService.getAnimal(chatId);
    }
}
