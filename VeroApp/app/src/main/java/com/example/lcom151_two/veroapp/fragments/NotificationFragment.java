package com.example.lcom151_two.veroapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lcom151_two.veroapp.R;

public class NotificationFragment extends Fragment {

//SELECT displayName FROM user WHERE userId IN(SELECT DISTINCT(likes.userId) FROM likes INNER JOIN posts ON likes.postId=posts.postId WHERE posts.userId=916523874591)
//SELECT user.displayName,posts.postText FROM user INNER JOIN posts on user.userId=posts.userId  WHERE user.userId IN(SELECT DISTINCT(likes.userId) FROM likes INNER JOIN posts ON likes.postId=posts.postId WHERE posts.userId=916523874591)
    public NotificationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

}
