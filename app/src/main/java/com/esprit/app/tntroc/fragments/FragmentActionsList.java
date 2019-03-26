package com.esprit.app.tntroc.fragments;


import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.app.tntroc.Entity.Action;
import com.esprit.app.tntroc.Entity.Annonce;
import com.esprit.app.tntroc.R;
import com.esprit.app.tntroc.activities.MainActivity;
import com.esprit.app.tntroc.utils.ActionsAdapter;
import com.esprit.app.tntroc.utils.AnnonceGridAdapter;
import com.esprit.app.tntroc.utils.Constants;
import com.esprit.app.tntroc.utils.CustomRVItemTouchListener;
import com.esprit.app.tntroc.utils.RecyclerViewItemClickListener;
import com.esprit.app.tntroc.utils.SimpleFragmentPagerAdapter;

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
public class FragmentActionsList extends Fragment {
    private FloatingActionButton add;
    private RecyclerView recyclerView;
    private ActionsAdapter mAdapter;
    private List<Action> actionsList = new ArrayList<>();
    private RequestQueue mQueue;
    ProgressDialog progressDialog;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ROOT = "ROOTING";
    private int sectionNumber;
    private String root;
    private FragmentManager fm;
    public static String TAG = "FragmentActionsList";
    Annonce annonce;
    Annonce annonce_echange;

    public FragmentActionsList() {
        this.setRetainInstance(true);
    }


    public static FragmentActionsList newInstance(int sectionNumber, String root) {
        FragmentActionsList fragment = new FragmentActionsList();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ROOT, root);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actions_list, container, false);
        sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        root = getArguments().getString(ROOT);
        mQueue = Volley.newRequestQueue(getContext());
        recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mQueue = Volley.newRequestQueue(getContext());
        jsonParse(root);
        recyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(getContext(), recyclerView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    public void jsonParse(String root) {
        String url =  Constants.WEBSERVICE_URL+"/mdw/v1/"+root+"/"+Constants.user.getId();
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving data...");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("mriGel", "Scout message: I'm in !");
                            JSONArray jsonArray = response.getJSONArray("actions");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);
                                int id = post.getInt("id");
                                int id_annonceur = post.getInt("id_annonceur");
                                int id_annonce = post.getInt("id_annonce");
                                int id_client = post.getInt("id_client");
                                int status = post.getInt("status");
                                String type = post.getString("status");
                                int id_annonce_echange = post.getInt("id_annonce_echange");
                                Action action = new Action();
                                action.setId(id);
                                action.setId_annonceur(id_annonceur);
                                action.setId_annonce(id_annonce);
                                action.setId_client(id_client);
                                action.setStatus(status);
                                action.setType(type);
                                action.setId_annonce_echange(id_annonce_echange);
                                Log.d("testt", "before");
                                getAnnonce(id_annonce);
                                Log.d("testt", "after");
                                action.setAnnonce(Constants.annonce_static);
                                if(id_annonce_echange > 0){
                                    getAnnonce_echange(id_annonce_echange);
                                    action.setAnnonce_echange(Constants.annonce_static);
                                }
                                else action.setAnnonce_echange(null);
                                actionsList.add(action);
                            }
                            progressDialog.dismiss();
                            mAdapter = new ActionsAdapter(actionsList);
                            recyclerView.setAdapter(mAdapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public interface VolleyCallback{
        void getAnnonce(int post_id);
        void getAnnonce_echange(int post_id);
    }
    public void getAnnonce(int post_id) {
        final String url =  Constants.WEBSERVICE_URL+"/mdw/v1/post/"+post_id;
        final Annonce myAnnonce = new Annonce();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("testt", "inside");
                            JSONArray jsonArray = response.getJSONArray("post");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);
                                Log.d("mriGel", "Scout message: Get Annonce");
                                int id = post.getInt("id");
                                int user_id = post.getInt("user_id");
                                String titre = post.getString("title");
                                String desc = post.getString("description");
                                String strDate = post.getString("created_at");
                                String img = post.getString("img");
                                String categorie = post.getString("categorie");
                                String prix = post.getString("prix");
                                myAnnonce.setId(id);
                                myAnnonce.setUser_id(user_id);
                                myAnnonce.setTitle(titre);
                                myAnnonce.setDescription(desc);
                                myAnnonce.setCreated_at(strDate);
                                myAnnonce.setImg(img);
                                myAnnonce.setCategorie(categorie);
                                myAnnonce.setPrix(Integer.valueOf(prix));
                            }
                            annonce = myAnnonce;
                            Log.d("mriGel_barcha", "Scout message: Get Annonce Echange"+annonce.toString());
                        }
                        catch (JSONException e) {
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
    public void getAnnonce_echange(int post_id) {
        final String url =  Constants.WEBSERVICE_URL+"/mdw/v1/post/"+post_id;
        final Annonce myAnnonce = new Annonce();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ok("get annonce echange");
                            JSONArray jsonArray = response.getJSONArray("post");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d("mriGel", "Scout message: Get Annonce Echange");
                                JSONObject post = jsonArray.getJSONObject(i);
                                int id = post.getInt("id");
                                int user_id = post.getInt("user_id");
                                String titre = post.getString("title");
                                String desc = post.getString("description");
                                String strDate = post.getString("created_at");
                                String img = post.getString("img");
                                String categorie = post.getString("categorie");
                                String prix = post.getString("prix");
                                myAnnonce.setId(id);
                                myAnnonce.setUser_id(user_id);
                                myAnnonce.setTitle(titre);
                                myAnnonce.setDescription(desc);
                                myAnnonce.setCreated_at(strDate);
                                myAnnonce.setImg(img);
                                myAnnonce.setCategorie(categorie);
                                myAnnonce.setPrix(Integer.valueOf(prix));
                            }
                            annonce_echange = myAnnonce;
                        }
                        catch (JSONException e) {
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
    public void ok(String text){
        Log.d("mriGel", text);
    }
}
