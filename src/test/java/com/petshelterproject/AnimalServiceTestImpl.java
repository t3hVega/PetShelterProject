package com.petshelterproject;


import com.petshelterproject.model.Animal;
import com.petshelterproject.service.animal.impl.AnimalServiceImpl;
import com.petshelterproject.service.volunteer.impl.VolunteerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTestImpl {
    @Mock
    private AnimalServiceImpl animalService;
    @InjectMocks
    private VolunteerServiceImpl volunteerService;

    @Test
    public void Service(){
//given
        int amount = 2;
        when(animalService.getAll()).thenReturn(Collections.emptyList());

        //then
        Assertions.assertNull(volunteerService.getVolunteer((long) amount));
    }
        }



