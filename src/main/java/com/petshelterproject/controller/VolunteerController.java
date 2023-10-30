package com.petshelterproject.controller;

import com.petshelterproject.model.Volunteer;
import com.petshelterproject.service.volunteer.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {
    @Autowired
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("{id}")
    public Volunteer get(@PathVariable Long chatId) {
        return volunteerService.getVolunteer(chatId);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long chatId) {
        volunteerService.deleteVolunteer(chatId);
    }

}
