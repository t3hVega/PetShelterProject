package com.petshelterproject.repository;

import com.petshelterproject.model.ReportPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.telegram.telegrambots.meta.api.objects.InputFile;

public interface ReportPhotoRepository extends JpaRepository<ReportPhoto, Long> {
}
