package com.example.app.androidproject.Entity;

public class User {
    private int id;
    private String username;
    private String name;
    private String last_name;
    private String email;
    private String password;
    private String numtel;
    private String adresse;
    private String date_naissance;
    private String api_key;
    private String image;


    public User(int id, String username, String name, String last_name, String email, String numtel, String adresse, String date_naissance, String api_key, String image) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.numtel = numtel;
        this.adresse = adresse;
        this.date_naissance = date_naissance;
        this.api_key = api_key;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumtel() {
        return numtel;
    }

    public void setNumtel(String numtel) {
        this.numtel = numtel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
