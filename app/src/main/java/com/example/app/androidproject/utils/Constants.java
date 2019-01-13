package com.example.app.androidproject.utils;

import com.example.app.androidproject.Entity.User;

public class Constants {

    public static final String SHARED_PREFS = "sharefprefs";
    public static final String USERNAME = "username";
    public static final String API_KEY = "api_key";
    public static final String USER_STR = "user json";
    public static int post_id = 0;
    public static String WEBSERVICE_URL = "http://192.168.1.3";
    public static final String WEBSERVICE_URL_SERVER = "https://tntroc.herokuapp.com";
    public static User user = new User();
    public static final String ANNONCE_IMG_PATH = WEBSERVICE_URL+"/mdw/uploadimage/uploads/annonces/";
    public static final String USER_IMG_PATH = WEBSERVICE_URL+"/mdw/uploadimage/uploads/users/";
    public static final Boolean IN_USER_ANNONCES = false;

}
