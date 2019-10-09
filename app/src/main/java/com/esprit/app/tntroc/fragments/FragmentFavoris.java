package com.esprit.app.tntroc.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.esprit.app.tntroc.Entity.Annonce;
import com.esprit.app.tntroc.R;
import com.esprit.app.tntroc.utils.AnnonceAdapter;
import com.esprit.app.tntroc.utils.DatabaseHelper;
import com.esprit.app.tntroc.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFavoris extends Fragment {

    private List<Annonce> annoncesList = new ArrayList<>();
    private AnnonceAdapter mAdapter;
    private DatabaseHelper db;
    private RecyclerView recyclerView;
    public FragmentFavoris() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favoris, container, false);

        db = new DatabaseHelper(getContext());
        annoncesList.addAll(db.getAllPosts());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if(view instanceof ImageView){
                    Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "everywhere", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        return view;
    }
    private void showActionsDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Attention");
        builder.setMessage("Supprimer de la liste de favoris ?");
        builder.setPositiveButton("Confirmer",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}
