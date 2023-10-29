package com.petshelterproject;

import com.petshelterproject.controller.AnimalController;
import com.petshelterproject.service.animal.AnimalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalControllerTest {
    @Mock
    private AnimalController animalController;
    @Test
    public void Controller(){
//given
        int amount = 2;
        when(animalController.getAll()).thenReturn(Collections.emptyList());

        //then
        Assertions.assertNull(animalController.getAllByKind((long) amount));
        AnimalService animalService = null;
        Assertions.assertNull(animalService.getAllByGender((long) amount));
    }
}

