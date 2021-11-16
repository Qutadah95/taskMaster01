package com.example.taskmaster;

public class Task {
    public String name;
    public String body;
    public String state;

    public Task(String name, String body, String state) {
        this.name = name;
        this.body = body;
        this.state = state;
    }
}
