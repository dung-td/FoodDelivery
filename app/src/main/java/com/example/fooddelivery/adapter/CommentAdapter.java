package com.example.fooddelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.model.Comment;
import com.example.fooddelivery.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Context context;
    private final List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_layout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment p = comments.get(position);
        holder.textViewUserName.setText(p.getUserName());
        holder.textViewComment.setText(p.getDetails());
        holder.textViewDate.setText(p.getDate());
        holder.textViewStar.setText(p.getRating() + "");
        //holder.imageViewUser.setImageResource(p.getUser_image());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewUser;
        TextView textViewUserName, textViewComment, textViewDate, textViewStar;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.tv_user_name);
            textViewComment = itemView.findViewById(R.id.tv_user_comment);
            textViewDate = itemView.findViewById(R.id.tv_user_comment_date);
            textViewStar = itemView.findViewById(R.id.tv_user_rating);
            imageViewUser = itemView.findViewById(R.id.img_user);
        }
    }
}
