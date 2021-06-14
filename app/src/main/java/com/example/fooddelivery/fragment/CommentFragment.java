package com.example.fooddelivery.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.adapter.CommentAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {

    List<Comment> commentList;

    public CommentFragment() {
//        commentList = new ArrayList<>();
//        commentList.add(new Comment(1, "Nguyễn Văn A", "Món ngon, gói đẹp, giá tốt.", R.drawable.male_user_96px, "29/04/2021" , 5));
//        commentList.add(new Comment(2, "Nguyễn Văn B", "Món ngon, gói đẹp, giá tốt.", R.drawable.male_user_96px, "20/04/2021" , 4));
//        commentList.add(new Comment(3, "Nguyễn Văn C", "Món ngon, gói đẹp, giá tốt.", R.drawable.male_user_96px, "12/04/2021" , 1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_comment);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        CommentAdapter commentAdapter = new CommentAdapter(getContext(), commentList);
        recyclerView.setAdapter(commentAdapter);
    }
}
