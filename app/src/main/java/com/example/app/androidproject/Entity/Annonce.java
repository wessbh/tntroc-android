package com.example.app.androidproject.Entity;

public class Annonce {
    private int id;
    private String title;
    private String description;
    private int user_id;
    private String img;
    private String created_at;
    private String date_exp;
    private int status;
    private String updated_at;
    private String categorie;

    public Annonce() {
    }

    public Annonce(int id, String title, String description, int user_id, String img, String created_at, String date_exp, int status, String updated_at, String categorie) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user_id = user_id;
        this.img = img;
        this.created_at = created_at;
        this.date_exp = date_exp;
        this.status = status;
        this.updated_at = updated_at;
        this.categorie = categorie;
    }

    public Annonce(String title, String description, int user_id, String img, String created_at, String date_exp, int status, String updated_at, String categorie) {
        this.title = title;
        this.description = description;
        this.user_id = user_id;
        this.img = img;
        this.created_at = created_at;
        this.date_exp = date_exp;
        this.status = status;
        this.updated_at = updated_at;
        this.categorie = categorie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDate_exp() {
        return date_exp;
    }

    public void setDate_exp(String date_exp) {
        this.date_exp = date_exp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Annonce{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", user_id=" + user_id +
                ", img='" + img + '\'' +
                ", created_at='" + created_at + '\'' +
                ", date_exp='" + date_exp + '\'' +
                ", status=" + status +
                ", updated_at='" + updated_at + '\'' +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
