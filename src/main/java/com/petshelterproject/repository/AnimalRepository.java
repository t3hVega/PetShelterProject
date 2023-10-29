package com.petshelterproject.repository;

import com.petshelterproject.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    /**
     * Метод возвращает всех животных по нужному типу
     * @return
     */
    List<Animal> findAllByIsCat(long isCat);
    List<Animal> findAllByIsMale(long isMale);
}
