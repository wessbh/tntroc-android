package com.esprit.app.tntroc.utils;

import com.esprit.app.tntroc.Entity.Annonce;
import com.esprit.app.tntroc.Entity.User;

public class Constants {

    public static final String SHARED_PREFS = "sharefprefs";
    public static final String USERNAME = "username";
    public static final String API_KEY = "api_key";
    public static final String USER_STR = "user json";
    //public static String WEBSERVICE_URL = "http://192.168.1.13";
    public static String WEBSERVICE_URL = "https://tntroc.000webhostapp.com";
    public static final String WEBSERVICE_URL_SERVER = "https://tntroc.herokuapp.com";
    public static User user = new User();
    public static final String ANNONCE_IMG_PATH = WEBSERVICE_URL+"/mdw/uploadimage/uploads/annonces/";
    public static final String USER_IMG_PATH = WEBSERVICE_URL+"/mdw/uploadimage/uploads/users/";
    public static final Boolean IN_USER_ANNONCES = false;
    public static Annonce annonce_static = new Annonce();

}
