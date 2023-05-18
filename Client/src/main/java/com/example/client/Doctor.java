package com.example.client;

public class Doctor {

    private String surname;
    private String name;
    private String patronymic;
    private String specialization;
    private String experience;

    public  String getSurname() {
        return surname;
    }
    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getExperience() {
        return experience;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
