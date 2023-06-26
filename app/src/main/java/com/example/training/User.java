package com.example.training;

public class User {
    private int id;
    private String first_name;

    public User(int id, String first_name) {
        this.id = id;
        this.first_name = first_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
}
