package com.example.app.androidproject.utils;

/**
 * Created by ravi on 20/02/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.androidproject.Entity.Annonce;
import com.example.app.androidproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnnonceAdapter extends RecyclerView.Adapter<AnnonceAdapter.MyViewHolder> {

    private Context context;
    private List<Annonce> annonceList;
    public DatabaseHelper db; ;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titre;
        public TextView prix;
        public ImageView img;
        public ImageView delete;

        public MyViewHolder(View view) {
            super(view);
            titre = view.findViewById(R.id.titre);
            prix = view.findViewById(R.id.prix);
            img = view.findViewById(R.id.image_view);
            delete = view.findViewById(R.id.delete);
            db =  new DatabaseHelper(context);
        }
    }


    public AnnonceAdapter(Context context, List<Annonce> annonceList) {
        this.context = context;
        this.annonceList = annonceList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favoris_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Annonce annonce = annonceList.get(position);

        holder.titre.setText(annonce.getTitle());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width = displayMetrics.widthPixels;
        String prixStr = annonce.getPrix()+" Dt";
        holder.prix.setText(prixStr);

        // Formatting and displaying timestamp
        Picasso.get().load(Constants.ANNONCE_IMG_PATH+annonce.getImg())
                .resize(300, 350)
                .centerInside()
                .error(R.drawable.error_img)
                .placeholder(R.drawable.placeholder)
                .into(holder.img);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNote(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return annonceList.size();
    }

    private void deleteNote(int position) {
        // deleting the note from db
        db.deleteNote(annonceList.get(position));

        // removing the note from the list
        annonceList.remove(position);
        notifyItemRemoved(position);
    }
}
