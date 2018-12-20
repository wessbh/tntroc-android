package com.example.app.androidproject.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.androidproject.Entity.Annonce;
import com.example.app.androidproject.Entity.Constants;
import com.example.app.androidproject.R;
import com.example.app.androidproject.utils.AnnonceGridAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetails extends Fragment {
    TextView text;
    private int id;
    private RequestQueue mQueue;
    ProgressDialog progressDialog;
    private Annonce annonce;
    public static String TAG = "FragmentDetails";
    public FragmentDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        text = view.findViewById(R.id.text);
        mQueue = Volley.newRequestQueue(getContext());
        id = getArguments().getInt("postID");
        jsonParse(id);
        return view;
    }

    public void jsonParse(int id) {
       final String url =  Constants.WEBSERVICE_URL+"/mdw/v1/post/"+id;
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving data...");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("post");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);
                                int id = post.getInt("id");
                                String titre = post.getString("title");
                                String desc = post.getString("description");
                                String strDate = post.getString("created_at");
                                String img = post.getString("img");
                                String categorie = post.getString("categorie");
                                String prix = post.getString("prix");
                                Annonce myAnnonce = new Annonce();
                                myAnnonce.setId(id);
                                myAnnonce.setTitle(titre);
                                myAnnonce.setDescription(desc);
                                myAnnonce.setCreated_at(strDate);
                                myAnnonce.setImg(img);
                                myAnnonce.setCategorie(categorie);
                                myAnnonce.setPrix(Integer.valueOf(prix));
                                annonce = myAnnonce;
                                text.setText(annonce.getTitle());
                                Toast.makeText(getContext(), annonce.getTitle()+"", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                            // progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Constants.user.getApi_key());
                return headers;
            }
        };
        mQueue.add(request);
    }
}
