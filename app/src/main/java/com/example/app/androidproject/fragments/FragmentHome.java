package com.example.app.androidproject.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.app.androidproject.Entity.Annonce;
import com.example.app.androidproject.R;
import com.example.app.androidproject.utils.AnnonceAdapter;
import com.example.app.androidproject.utils.Constants;
import com.example.app.androidproject.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    private static final String TAG = "FragmentHome";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int sectionNumber;
    private List<Annonce> annoncesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AnnonceAdapter mAdapter;
    private RequestQueue mQueue;
    TextView textTitle;
    String apiKey;
    private DatabaseHelper db;
    ProgressDialog progressDialog;


    public static FragmentHome newInstance(int sectionNumber) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoris, container, false);
        apiKey = Constants.user.getApi_key();
        //------------------------- RecyclerView -----------------------------------\\

        db = new DatabaseHelper(getContext());
        annoncesList.addAll(db.getAllPosts());
        mAdapter = new AnnonceAdapter(getContext(), annoncesList);
        recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mQueue = Volley.newRequestQueue(getContext());

        return view;
    }


}
