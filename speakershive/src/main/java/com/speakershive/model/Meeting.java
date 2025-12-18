package com.speakershive.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Meeting {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String theme;
    private String wordOfTheDay;
    private String chapterId;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getWordOfTheDay() { return wordOfTheDay; }
    public void setWordOfTheDay(String wordOfTheDay) { this.wordOfTheDay = wordOfTheDay; }
    public String getChapterId() { return chapterId; }
    public void setChapterId(String chapterId) { this.chapterId = chapterId; }
}
