package com.example.se1406_database.dto;

import java.io.Serializable;

public class StudentDTO implements Serializable {
    private String id;
    private String name;
    private float mark;

    public StudentDTO() {
    }

    public StudentDTO(String id, String name, float mark) {
        this.id = id;
        this.name = name;
        this.mark = mark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return id + "-" + name + "-" + mark;
    }
}
