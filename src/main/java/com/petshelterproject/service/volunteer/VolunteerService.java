package com.petshelterproject.service.volunteer;

import com.petshelterproject.model.Volunteer;

public interface VolunteerService {
    Volunteer addVolunteer(Volunteer volunteer);
    Volunteer getVolunteer(Long id);
    Volunteer editVolunteer(Volunteer volunteer);
    void deleteVolunteer(Long id);
}
