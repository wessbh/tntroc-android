package com.example.app.androidproject.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.androidproject.Entity.Annonce;
import com.example.app.androidproject.Entity.Constants;
import com.example.app.androidproject.R;
import com.squareup.picasso.Picasso;
import java.util.List;
public class AnnonceListAdapter extends RecyclerView.Adapter<AnnonceListAdapter.MyViewHolder> {

    private List<Annonce> annoncesList;

    public AnnonceListAdapter(List<Annonce> annoncesList) {
        this.annoncesList = annoncesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titre, year;
        Button btn_postuler;
        ImageView img, fakeImg;

        public MyViewHolder(View view) {
            super(view);
            titre = view.findViewById(R.id.titre);
            img = view.findViewById(R.id.image_view);
            year = view.findViewById(R.id.year);
            btn_postuler = view.findViewById(R.id.postuler);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.annonce_liste_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Annonce annonce = annoncesList.get(position);
        String strPoste = annonce.getTitle();
        Context context = holder.img.getContext();
        Picasso.get().load(Constants.ANNONCE_IMG_PATH+annonce.getImg())
                .error(R.drawable.fb)
                .placeholder(R.drawable.placeholder)
                .into(holder.img);
        //Picasso.get().load(Constants.ANNONCE_IMG_PATH+annonce.getImg()).into(holder.img);
        if(strPoste.length()>=31){
            String str2 = strLimite(strPoste);
            holder.titre.setText(str2);
            holder.year.setText(annonce.getDate_exp());

        }
        else{
            holder.titre.setText(annonce.getTitle());
            holder.year.setText(annonce.getDate_exp());
        }
    }

    @Override
    public int getItemCount() {
        return annoncesList.size();
    }

    public String strLimite(String str){
        String str2 = str.substring(0,30)+"...";

        return str2;
    }
}
