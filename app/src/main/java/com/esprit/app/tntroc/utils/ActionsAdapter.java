package com.esprit.app.tntroc.utils;

import android.graphics.Movie;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.app.tntroc.Entity.Action;
import com.esprit.app.tntroc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.MyViewHolder> {
    private List<Action> actionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView title, price, type, status;

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.action_img);
            title = (TextView) view.findViewById(R.id.product_name);
            price = (TextView) view.findViewById(R.id.price);
            type = (TextView) view.findViewById(R.id.type);
            status = (TextView) view.findViewById(R.id.status);
        }
    }


    public ActionsAdapter(List<Action> moviesList) {
        this.actionList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actions_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Action action = actionList.get(position);
        String status_str= "";
        switch (action.getStatus()){
            case 0:
                status_str = "En attente";
            case 1:
                status_str = "Effectué";
            case 2:
                status_str = "Refusée";
            default:
                status_str = "En attente";
        }
        holder.title.setText(action.getAnnonce().getTitle());
        holder.price.setText(action.getAnnonce().getPrix()+"");
        holder.type.setText(action.getType());
        holder.status.setText(status_str);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width = displayMetrics.widthPixels;
        Picasso.get().load(Constants.ANNONCE_IMG_PATH+action.getAnnonce().getImg())
                .error(R.drawable.error_img)
                .placeholder(R.drawable.placeholder)
                .into(holder.img);
}

    @Override
    public int getItemCount() {
        return actionList.size();
    }
}
