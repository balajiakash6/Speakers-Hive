package com.speakerhive.project2.model;

import java.util.List;

public class Session {
    private String title;
    private String date;
    private String time;
    private String theme;
    private String wordOfDay;
    private List<Role> roles;

    public Session() {}

    public Session(String title, String date, String time, String theme, String wordOfDay, List<Role> roles) {
        this.title = title; this.date = date; this.time = time;
        this.theme = theme; this.wordOfDay = wordOfDay; this.roles = roles;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getWordOfDay() { return wordOfDay; }
    public void setWordOfDay(String wordOfDay) { this.wordOfDay = wordOfDay; }
    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }
}
