package com.petshelterproject.service.volunteer.impl;

import com.petshelterproject.model.Volunteer;
import com.petshelterproject.repository.VolunteerRepository;
import com.petshelterproject.service.volunteer.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    @Autowired
    private final VolunteerRepository volunteerRepository;

    public VolunteerServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }
    @Override
    public Volunteer addVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    @Override
    public Volunteer getVolunteer(Long chatId) {
        return volunteerRepository.findByChatId(chatId);
    }

    @Override
    public Volunteer editVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    @Override
    public void deleteVolunteer(Long chatId) {
        volunteerRepository.deleteByChatId(chatId);
    }
}
