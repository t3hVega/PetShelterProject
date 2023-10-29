package com.petshelterproject;

import com.petshelterproject.model.Animal;
import com.petshelterproject.service.animal.impl.AnimalServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@SpringBootTest
class PetshelterprojectApplicationTests {

	@Mock
	private AnimalServiceImpl petshelterprojectService;
	private final List<Animal> Animals = new ArrayList<>() {{
		add(new Animal("Грета", "female", 1, "dog"));
		add(new Animal("Марсик", "male", 3, "cat"));
		add(new Animal("Вуди", "male", 2, "bird"));
	}};

	private Animal[] animals;


	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void contextLoads() {
		//given
		final int Animal = 1;
		final Map<String, Animal> animalMap = new HashMap<>();
		for (Animal animal : animals) {
			animalMap.put(animal.getKindOfAnimal() + animal.getTypeOfAnimal(), animal);
		}

		when(AnimalServiceImpl.get()).thenReturn((List<Animal>) animalMap);
		//when
		Animal animal = (Animal) AnimalServiceImpl.getAnimal((long) Animal);
		//then
		Assertions.assertEquals(Animal, animal);
	}

}




