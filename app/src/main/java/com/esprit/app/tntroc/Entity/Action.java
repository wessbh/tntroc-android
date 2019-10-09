package com.esprit.app.tntroc.Entity;

public class Action {
    private int id;
    private int id_annonceur;
    private int id_annonce;
    private int id_client;
    private int status;
    private String type;
    private enum type { Achat, Echange}
    private int id_annonce_echange;
    private Annonce annonce;
    private Annonce annonce_echange;

    public Action() {
    }

    public Action(int id, int id_annonceur, int id_annonce, int id_client, int status, String type, int id_annonce_echange, Annonce annonce, Annonce annonce_echange) {
        this.id = id;
        this.id_annonceur = id_annonceur;
        this.id_annonce = id_annonce;
        this.id_client = id_client;
        this.status = status;
        this.type = type;
        this.id_annonce_echange = id_annonce_echange;
        this.annonce = annonce;
        this.annonce_echange = annonce_echange;
    }

    public Action(int id_annonceur, int id_annonce, int id_client, int status, String type, int id_annonce_echange, Annonce annonce, Annonce annonce_echange) {
        this.id_annonceur = id_annonceur;
        this.id_annonce = id_annonce;
        this.id_client = id_client;
        this.status = status;
        this.type = type;
        this.id_annonce_echange = id_annonce_echange;
        this.annonce = annonce;
        this.annonce_echange = annonce_echange;
    }

    public Action(int id, int id_annonceur, int id_annonce, int id_client, int status, String type, int id_annonce_echange, Annonce annonce) {
        this.id = id;
        this.id_annonceur = id_annonceur;
        this.id_annonce = id_annonce;
        this.id_client = id_client;
        this.status = status;
        this.type = type;
        this.id_annonce_echange = id_annonce_echange;
        this.annonce = annonce;
    }

    public Action(int id, int id_annonceur, int id_annonce, int id_client, int status, String type, int id_annonce_echange) {
        this.id = id;
        this.id_annonceur = id_annonceur;
        this.id_annonce = id_annonce;
        this.id_client = id_client;
        this.status = status;
        this.type = type;
        this.id_annonce_echange = id_annonce_echange;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_annonceur() {
        return id_annonceur;
    }

    public void setId_annonceur(int id_annonceur) {
        this.id_annonceur = id_annonceur;
    }

    public int getId_annonce() {
        return id_annonce;
    }

    public void setId_annonce(int id_annonce) {
        this.id_annonce = id_annonce;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId_annonce_echange() {
        return id_annonce_echange;
    }

    public void setId_annonce_echange(int id_annonce_echange) {
        this.id_annonce_echange = id_annonce_echange;
    }

    public Annonce getAnnonce() {
        return annonce;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public Annonce getAnnonce_echange() {
        return annonce_echange;
    }

    public void setAnnonce_echange(Annonce annonce_echange) {
        this.annonce_echange = annonce_echange;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", id_annonceur=" + id_annonceur +
                ", id_annonce=" + id_annonce +
                ", id_client=" + id_client +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", id_annonce_echange=" + id_annonce_echange +
                ", annonce=" + annonce +
                ", annonce_echange=" + annonce_echange +
                '}';
    }
}
