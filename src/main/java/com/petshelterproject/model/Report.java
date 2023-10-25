package com.petshelterproject.model;

import jakarta.persistence.*;

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
    @OneToMany
    @JoinColumn(name = "report_photo_id")
    private List<ReportPhoto> reportPhoto;

    public Report(Long id, Long chatId, String text, List<ReportPhoto> reportPhoto) {
        this.id = id;
        this.chatId = chatId;
        this.text = text;
        this.reportPhoto = reportPhoto;
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

    public List<ReportPhoto> getReportPhoto() {
        return reportPhoto;
    }

    public void setReportPhoto(List<ReportPhoto> reportPhoto) {
        this.reportPhoto = reportPhoto;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id) && Objects.equals(chatId, report.chatId) && Objects.equals(text, report.text) && Objects.equals(reportPhoto, report.reportPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, text, reportPhoto);
    }


    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", text='" + text + '\'' +
                ", reportPhoto=" + reportPhoto +
                '}';
    }
}
