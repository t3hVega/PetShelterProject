package com.petshelterproject.controller;

import com.petshelterproject.model.Volunteer;
import com.petshelterproject.service.volunteer.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {
    @Autowired
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping
    public Volunteer add(@RequestBody Volunteer volunteer) {
        return volunteerService.addVolunteer(volunteer);
    }

    @GetMapping("{id}")
    public Volunteer get(@PathVariable Long id) {
        return volunteerService.getVolunteer(id);
    }

    @PutMapping
    public ResponseEntity<Volunteer> edit(@RequestBody Volunteer volunteer) {
        Volunteer foundVolunteer = volunteerService.editVolunteer(volunteer);
        if (foundVolunteer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundVolunteer);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        volunteerService.deleteVolunteer(id);
    }

    public Object getAll() {
        return null;
    }

    public Object getVolunteer(long amount) {
        return null;
    }
}
