package com.petshelterproject.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String text;
    private LocalDate date;
    private boolean approved;
    @OneToOne
    @JoinColumn(name = "report_photo_id")
    private ReportPhoto reportPhoto;
    @OneToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    public Report(Long id, Long chatId, String text, LocalDate date, ReportPhoto reportPhoto, Animal animal) {
        this.id = id;
        this.chatId = chatId;
        this.text = text;
        this.date = date;
        this.reportPhoto = reportPhoto;
        this.animal = animal;
    }

    public Report() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public ReportPhoto getReportPhoto() {
        return reportPhoto;
    }

    public void setReportPhoto(ReportPhoto reportPhoto) {
        this.reportPhoto = reportPhoto;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return approved == report.approved && Objects.equals(id, report.id) && Objects.equals(chatId, report.chatId) && Objects.equals(text, report.text) && Objects.equals(date, report.date) && Objects.equals(reportPhoto, report.reportPhoto) && Objects.equals(animal, report.animal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, text, date, approved, reportPhoto, animal);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", approved=" + approved +
                ", reportPhoto=" + reportPhoto +
                ", animal=" + animal +
                '}';
    }
}
