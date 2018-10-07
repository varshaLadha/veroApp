package com.example.lcom151_two.veroapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lcom151_two.veroapp.R;
import com.example.lcom151_two.veroapp.UserProfile;

public class SettingsFragment extends Fragment {

    Activity context;
    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_settings, container, false);
        context=getActivity();

        return view;
    }

}
