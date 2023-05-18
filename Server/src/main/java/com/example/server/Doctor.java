package com.example.server;

public class Doctor {
    private String surname;
    private String name;
    private String patronymic;
    private String specialization;
    private String experience;

    public Doctor(String surname, String name, String patronymic, String specialization, String experience) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.specialization = specialization;
        this.experience = experience;
    }
}
