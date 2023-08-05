package com.example.assignmenttest.model;

public class PostModel {
    public int id;
    public String title;
    public String date;
    public String description;
    public String username;

    public PostModel() {
    }

    public PostModel(int id, String title, String date, String description, String username) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
        this.username = username;
    }
}
