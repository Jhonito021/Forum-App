package com.example.model;

public class Utilisateur {
    private int id;
    private String nom;
    private int age;

    public Utilisateur() {
    }

    public Utilisateur(int id, String nom, int age) {
        this.id = id;
        this.nom = nom;
        this.age = age;
    }

    public Utilisateur(String nom, int age) {
        this.nom = nom;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return nom + " | " + age + " ans";
    }
}
