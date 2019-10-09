package com.esprit.app.tntroc.Entity;

public class Comment {
    private int id;
    private int user_id;
    private int post_id;
    private String body;
    private String created_at;
    private int rating;
    private String user_img;
    private String username;
    public Comment() {
    }

    public Comment(int id, int user_id, int post_id, String body, String created_at, int rating, String user_img, String username) {
        this.id = id;
        this.user_id = user_id;
        this.post_id = post_id;
        this.body = body;
        this.created_at = created_at;
        this.rating = rating;
        this.user_img = user_img;
        this.username = username;
    }

    public Comment(int user_id, int post_id, String body, String created_at, int rating, String user_img, String username) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.body = body;
        this.created_at = created_at;
        this.rating = rating;
        this.user_img = user_img;
        this.username = username;
    }

    public Comment(int user_id, int post_id, String body, int rating, String user_img, String username) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.body = body;
        this.rating = rating;
        this.user_img = user_img;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
