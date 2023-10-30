package com.petshelterproject.repository;

import com.petshelterproject.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByChatId(Long chatId);
    List<Report> findAllByAnimalId(Long animalId);

}
