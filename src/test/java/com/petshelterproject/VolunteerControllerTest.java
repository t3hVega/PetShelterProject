package com.petshelterproject;

import com.petshelterproject.controller.AnimalController;
import com.petshelterproject.controller.VolunteerController;
import com.petshelterproject.service.animal.AnimalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VolunteerControllerTest {
    @Mock
    private VolunteerController volunteerController;
    @Test
    public void Controller(){
//given
        int amount = 2;
        when(volunteerController.getAll()).thenReturn(Collections.emptyList());

        //then
        Assertions.assertNull(volunteerController.getVolunteer((long) amount));

    }
}

