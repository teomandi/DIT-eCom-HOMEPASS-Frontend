package com.example.frontmynbnb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontmynbnb.R;
import com.example.frontmynbnb.models.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HostFragment extends MyFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host, container, false);
        return view;
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("HOST");
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container,
                new HomeFragment()
        ).addToBackStack(null).commit();
        return true;
    }
}
