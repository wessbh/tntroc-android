package com.example.app.androidproject.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.androidproject.Entity.Annonce;
import com.example.app.androidproject.Entity.Constants;
import com.example.app.androidproject.activities.LoginActivity;
import com.example.app.androidproject.R;
import com.example.app.androidproject.activities.MainActivity;
import com.example.app.androidproject.utils.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {
    private static final String TAG = "FragmentHome";

    private List<Annonce> annoncesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AnnonceAdapter mAdapter;
    private RequestQueue mQueue;
    private Context context;
    TextView textTitle;
    String apiKey;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        apiKey = Constants.user.getApi_key();
        textTitle = view.findViewById(R.id.textTitle);

        TextView testLink = view.findViewById(R.id.testLink);
        testLink.setText("ClickMe!");
        testLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
            }
        });
        //------------------------- RecyclerView -----------------------------------\\

        recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        textTitle.setText("Vous n'avez aucun PDF");
        textTitle.setVisibility(View.GONE);
        mQueue = Volley.newRequestQueue(getContext());
        jsonParse();

        return view;
    }


    public void jsonParse() {
        String url =  Constants.WEBSERVICE_URL+"/mdw/v1/all_posts";
        String urlOffres1 ="https://api.myjson.com/bins/6wo6o";
        String urlOffres2="https://api.myjson.com/bins/kndlk";
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
                            Log.d("annonce", response.getJSONArray("post").getJSONObject(0).toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);
                                int id = post.getInt("id");
                                String titre = post.getString("title");
                                String desc = post.getString("description");
                                String strDate = post.getString("created_at");
                                String img = post.getString("img");
                                String categorie = post.getString("categorie");
                                Annonce annonce = new Annonce();
                                annonce.setId(id);
                                annonce.setTitle(titre);
                                annonce.setDescription(desc);
                                annonce.setCreated_at(strDate);
                                annonce.setImg(img);
                                annonce.setCategorie(categorie);
                                annoncesList.add(annonce);
                                Log.d("here", annoncesList.toString());
                            }
                            progressDialog.dismiss();
                            mAdapter = new AnnonceAdapter(annoncesList);
                            recyclerView.setAdapter(mAdapter);
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
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", apiKey);
                return headers;
            }
        };
        mQueue.add(request);
    }
    public Date convertDate(String strDate){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
