package com.esprit.app.tntroc.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.esprit.app.tntroc.Entity.Comment;
import com.esprit.app.tntroc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<Comment> commentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView created_at;
        public TextView body;
        public ImageView img;
        public RatingBar rating;

        public MyViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.user_name);
            created_at = view.findViewById(R.id.created_at);
            body = view.findViewById(R.id.comment_body);
            img = view.findViewById(R.id.imgv_profile);
            rating = view.findViewById(R.id.rating);

        }
    }


    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_row, parent, false);

        return new CommentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.MyViewHolder holder, final int position) {
        Comment comment = commentList.get(position);

        holder.username.setText(comment.getUsername());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width = displayMetrics.widthPixels;
        holder.created_at.setText(comment.getCreated_at());
        holder.body.setText(comment.getBody());
        // Formatting and displaying timestamp
        Picasso.get().load(Constants.USER_IMG_PATH+comment.getUser_img())
                .resize(300, 350)
                .centerInside()
                .error(R.drawable.error_img)
                .placeholder(R.drawable.placeholder)
                .into(holder.img);
        holder.rating.setNumStars(comment.getRating());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
