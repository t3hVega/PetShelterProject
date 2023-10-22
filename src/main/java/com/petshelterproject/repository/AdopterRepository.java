package com.petshelterproject.repository;

import com.petshelterproject.model.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdopterRepository extends JpaRepository<Adopter, Long> {
    Adopter findByChatId(Long chatId);
}
