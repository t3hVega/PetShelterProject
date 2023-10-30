package com.petshelterproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "volunteers")
public class Volunteer {
    @Id
    private Long chatId;
    private String name;
    private String data;
    private boolean active;
    private boolean reminderActive;
    private String lastMessage;
    private Long chatIdToSearch;
    private Long animalIdToSearch;
    private Long reviewedReportId;

    public Volunteer() {

    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isReminderActive() {
        return reminderActive;
    }

    public void setReminderActive(boolean reminderActive) {
        this.reminderActive = reminderActive;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getChatIdToSearch() {
        return chatIdToSearch;
    }

    public void setChatIdToSearch(Long chatIdToSearch) {
        this.chatIdToSearch = chatIdToSearch;
    }

    public Long getAnimalIdToSearch() {
        return animalIdToSearch;
    }

    public void setAnimalIdToSearch(Long animalIdToSearch) {
        this.animalIdToSearch = animalIdToSearch;
    }

    public Long getReviewedReportId() {
        return reviewedReportId;
    }

    public void setReviewedReportId(Long reviewedReportId) {
        this.reviewedReportId = reviewedReportId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return active == volunteer.active && reminderActive == volunteer.reminderActive && Objects.equals(chatId, volunteer.chatId) && Objects.equals(name, volunteer.name) && Objects.equals(data, volunteer.data) && Objects.equals(lastMessage, volunteer.lastMessage) && Objects.equals(chatIdToSearch, volunteer.chatIdToSearch) && Objects.equals(animalIdToSearch, volunteer.animalIdToSearch) && Objects.equals(reviewedReportId, volunteer.reviewedReportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, name, data, active, reminderActive, lastMessage, chatIdToSearch, animalIdToSearch, reviewedReportId);
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "chatId=" + chatId +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                ", active=" + active +
                ", reminderActive=" + reminderActive +
                ", lastMessage='" + lastMessage + '\'' +
                ", chatIdToSearch=" + chatIdToSearch +
                ", animalIdToSearch=" + animalIdToSearch +
                ", reviewedReportId=" + reviewedReportId +
                '}';
    }
}
