package com.petshelterproject;

import com.petshelterproject.service.animal.impl.AnimalServiceImpl;
import com.petshelterproject.service.volunteer.impl.VolunteerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VolunteerServiceImplTest {
    @Mock
    private VolunteerServiceImpl volunteerService;
    @InjectMocks
    private AnimalServiceImpl animalService;;

    @Test
    public void Service(){
//given
        int amount = 8;
        when(volunteerService.getAll()).thenReturn(Collections.emptyList());

        //then
        Assertions.assertNull(animalService.getAllByKind((long) amount));
        Assertions.assertEquals(null, animalService.getAllByGender((long) amount));
    }
}


