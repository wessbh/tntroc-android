package com.example.app.androidproject.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.androidproject.Entity.Annonce;
import com.example.app.androidproject.R;
import com.example.app.androidproject.activities.MainActivity;
import com.example.app.androidproject.fragments.FragmentDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AnnonceGridAdapter extends RecyclerView.Adapter<AnnonceGridAdapter.MyViewHolder> {

    private Context mContext;
    private List<Annonce> annonceList;
    private int id;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, categorie, prix;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            categorie = (TextView) view.findViewById(R.id.category);
            prix = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public AnnonceGridAdapter(Context mContext, List<Annonce> annonceList) {
        this.mContext = mContext;
        this.annonceList = annonceList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.annonce_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Annonce annonce = annonceList.get(position);
        holder.title.setText(annonce.getTitle());
        holder.categorie.setText(annonce.getCategorie());
        holder.prix.setText(annonce.getPrix()+" Dt");
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = annonce.getId();
            }
        });
        // loading album cover using Glide library
       // Glide.with(mContext).load(annonce.getImg()).into(holder.thumbnail);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width = displayMetrics.widthPixels;
        Picasso.get().load(Constants.ANNONCE_IMG_PATH+annonce.getImg())
                .resize(width, 170)
                .centerCrop()
                .error(R.drawable.error_img)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentDetails();
                changeFragment(fragment);
                Bundle args = new Bundle();
                args.putInt("postID", annonce.getId());
                fragment.setArguments(args);
            }
        });
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Ajouter aux favoris", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Voir détailles", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    public Annonce getUser(int position){
        return this.annonceList.get(position);
    }
    @Override
    public int getItemCount() {
        return annonceList.size();
    }

    public int getId() {
        return id;
    }

    public void changeFragment(Fragment fragment){
        MainActivity myact = (MainActivity) mContext;
        myact.getSupportFragmentManager().popBackStack();
        myact.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("toGrid").commit();
    }
}
