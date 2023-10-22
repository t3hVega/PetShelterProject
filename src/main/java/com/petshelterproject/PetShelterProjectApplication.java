package com.petshelterproject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories
@EnableScheduling
@SpringBootApplication
@OpenAPIDefinition
public class PetShelterProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetShelterProjectApplication.class, args);
	}

}
