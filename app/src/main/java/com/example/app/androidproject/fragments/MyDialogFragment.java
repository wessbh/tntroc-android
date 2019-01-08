package com.example.app.androidproject.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.androidproject.Entity.Annonce;
import com.example.app.androidproject.R;
import com.example.app.androidproject.utils.AnnonceListAdapter;
import com.example.app.androidproject.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDialogFragment extends DialogFragment {
    private RecyclerView recyclerView;
    private List<Annonce> annoncesList = new ArrayList<>();
    private AnnonceListAdapter listAdapter;
    private RequestQueue mQueue;

    public MyDialogFragment() {
        Log.d("dialogFragment", "in Construct: ");
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        //inflate layout with recycler view
//        View v = inflater.inflate(R.layout.fragment_home_layout, container, false);
//        recyclerView = v.findViewById(R.id.recycler_view);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        mQueue = Volley.newRequestQueue(getContext());
//
//
//        getUserPosts();
//        return v;
//    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linear= new LinearLayout(getContext());
        linear = (LinearLayout)layoutInflater.inflate(R.layout.fragment_home_layout, linear);
        recyclerView =linear.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mQueue = Volley.newRequestQueue(getContext());
        getUserPosts();
        String title ="Sélectionner une annonce ";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setView(recyclerView);
        alertDialogBuilder.setMessage("Are you sure?");

        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

        });

        return alertDialogBuilder.create();
    }

    public void getUserPosts() {
        String url =  Constants.WEBSERVICE_URL+"/mdw/v1/"+"posts_user/"+Constants.user.getId();
        Log.d("dialogFragment", "in function: ");
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
                                Annonce annonce = new Annonce();
                                annonce.setId(id);
                                annonce.setTitle(titre);
                                annonce.setDescription(desc);
                                annonce.setCreated_at(strDate);
                                annonce.setImg(img);
                                annonce.setCategorie(categorie);
                                annonce.setPrix(Integer.valueOf(prix));
                                annoncesList.add(annonce);
                            }
                            Toast.makeText(getContext(), ""+annoncesList.get(0).getTitle(), Toast.LENGTH_SHORT).show();
                            listAdapter = new AnnonceListAdapter(annoncesList);
                            recyclerView.setAdapter(listAdapter);
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
