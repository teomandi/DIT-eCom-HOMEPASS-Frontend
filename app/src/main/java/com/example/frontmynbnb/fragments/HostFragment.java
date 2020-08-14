package com.example.frontmynbnb.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frontmynbnb.activities.MainActivity;
import com.example.frontmynbnb.R;

import java.util.Objects;

public class HostFragment extends MyFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host, container, false);



        MainActivity.setBottomNavChecked(2);
        return view;
    }

//    @Override
//    public boolean onBackPressed() {
//        System.out.println("HOST");
//        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
//                R.id.fragment_container,
//                new HomeFragment()
//        ).addToBackStack(null).commit();
//        return true;
//    }
}
