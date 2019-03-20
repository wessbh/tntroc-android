package com.esprit.app.tntroc.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.app.tntroc.Entity.Annonce;
import com.esprit.app.tntroc.R;
import com.esprit.app.tntroc.fragments.FragmentDetails;
import com.squareup.picasso.Picasso;

import java.util.List;
public class    AnnonceListAdapter extends RecyclerView.Adapter<AnnonceListAdapter.MyViewHolder> {

    private List<Annonce> annoncesList;
    private Context mContext;
    private FragmentDetails fragmentDetails;
    private int selected_position = 0;


    public AnnonceListAdapter(FragmentDetails fragmentDetails, List<Annonce> annoncesList) {
        this.fragmentDetails = fragmentDetails;
        this.annoncesList = annoncesList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titre, prix;
        Button btn_postuler;
        ImageView img, fakeImg;

        private MyViewHolder(View view) {
            super(view);
            titre = view.findViewById(R.id.titre);
            prix = view.findViewById(R.id.prix);
            img = view.findViewById(R.id.image_view);
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Annonce annonce = annoncesList.get(position);
        String strPoste = annonce.getTitle();
        Context context = holder.img.getContext();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width = displayMetrics.widthPixels;
        Picasso.get().load(Constants.ANNONCE_IMG_PATH+annonce.getImg())
                .resize(230, 220)
                .centerCrop()
                .error(R.drawable.error_img)
                .placeholder(R.drawable.placeholder)
                .into(holder.img);
        String prixStr = annonce.getPrix()+" Dt";
        if(strPoste.length()>=31){
            String str2 = strLimite(strPoste);
            holder.titre.setText(str2);
            holder.prix.setText(prixStr);
        }
        else{
            holder.titre.setText(annonce.getTitle());
            holder.prix.setText(prixStr);
        }
        

    }

    @Override
    public int getItemCount() {
        return annoncesList.size();
    }

    private String strLimite(String str){
        return str.substring(0,30)+"...";
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


}


