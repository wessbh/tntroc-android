package com.example.app.androidproject.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.androidproject.utils.Constants;
import com.example.app.androidproject.Entity.User;
import com.example.app.androidproject.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    CallbackManager callbackManager;
    ProgressDialog mDialog;
    ProgressDialog progressDialog;

    EditText input_username, input_password, input_ip;
    Button btn_login;
    TextView link_signup;
    String username, password, email, birthday, responseName, responseApi_key;
    ImageView logo;
    User user;
    private RequestQueue mQueue;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(checkSharedPrefs()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        logo = findViewById(R.id.logo);
        callbackManager = CallbackManager.Factory.create();
        input_username = findViewById(R.id.input_username);
        input_ip = findViewById(R.id.input_ip);
        input_password = findViewById(R.id.input_password);
        btn_login = findViewById(R.id.btn_login);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(input_ip.getText().toString().equals("")){
                    Constants.WEBSERVICE_URL = "http://192.168.1.16";
                }
                else{

                    Constants.WEBSERVICE_URL = "http://"+input_ip.getText();
                }
                username = input_username.getText().toString().trim();
                password = input_password.getText().toString().trim();
                if (validate()) {
                    loginRequest(username, password);
                }
            }
        });
        link_signup = findViewById(R.id.link_signup);
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        final LoginButton loginButton = (LoginButton) findViewById(R.id.fb_btn);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    email = me.optString("name");
                                    birthday = me.optString("id");
                                    Toast.makeText(getApplicationContext(),email, Toast.LENGTH_SHORT).show();
                                    Log.d("responseFacebook", response.toString());
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }).executeAsync();
            }
            @Override
            public void onCancel() {
                // App code
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        if(AccessToken.getCurrentAccessToken() != null){
            email = AccessToken.getCurrentAccessToken().getUserId();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String username = input_username.getText().toString();
        String password = input_password.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            input_username.setError("at least 3 characters");
            valid = false;
        } else {
            input_username.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            input_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            input_password.setError(null);
        }

        return valid;
    }
    public void loginRequest (final String usernameLogin, final String passwordLogin ){
        progressDialog.show();
        user = new User();
        String url = Constants.WEBSERVICE_URL+"/mdw/v1/login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            user.setId(Integer.valueOf(jsonObject.get("id").toString()));
                            user.setUsername(jsonObject.get("username").toString());
                            user.setName(jsonObject.get("name").toString());
                            user.setLast_name(jsonObject.get("last_name").toString());
                            user.setEmail(jsonObject.get("email").toString());
                            user.setNumtel(jsonObject.get("num_tel").toString());
                            user.setAdresse(jsonObject.get("adresse").toString());
                            user.setDate_naissance(jsonObject.get("date_naissance").toString());
                            user.setApi_key(jsonObject.get("apiKey").toString());
                            user.setImage(jsonObject.get("image").toString());
                            user.setLast_login(jsonObject.get("last_login").toString());
                            String respName = user.getName()+" "+user.getLast_name();
                            setUserSharedPrefs(user);
                            loginSharedPrefs(respName, ""+jsonObject.get("apiKey").toString() );
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplication(),"Error ", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", usernameLogin);
                params.put("password", passwordLogin);

                return params;
            }
        };
        mQueue.add(postRequest);
    }
    public void loginFbSharedPrefs(final String name){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USERNAME, name);
        editor.apply();
    }
    public void loginSharedPrefs(final String name, final String api_key){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USERNAME, name);
        editor.putString(Constants.API_KEY, api_key);
        editor.apply();
    }

    public void setUserSharedPrefs(final User user){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(Constants.USER_STR, json);
        editor.apply();
    }
    public Boolean checkSharedPrefs(){
        String s;
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        s = sharedPreferences.getString(Constants.API_KEY, null);
        if (s != null ){
            return true;
        }
        else
        return false;
    }
    public void setUser(final User u){
        Constants.user = u;
    }

    public String getAPIKey (){
        String s;
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        s = sharedPreferences.getString(Constants.API_KEY, null);
        return s;
    }
}
