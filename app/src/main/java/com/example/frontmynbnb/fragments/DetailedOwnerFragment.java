package com.example.frontmynbnb.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontmynbnb.R;

import java.util.Objects;

public class DetailedOwnerFragment extends MyFragment {

    public DetailedOwnerFragment() {
        // Required empty public constructor
    }

    public static DetailedOwnerFragment newInstance(String param1, String param2) {
        DetailedOwnerFragment fragment = new DetailedOwnerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private int placeId, userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detailed_owner, container, false);

        if (getArguments() != null) {
            placeId = getArguments().getInt("place_id");
            userId = getArguments().getInt("user_id");
        }
        return view;
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("detailed owner fragment");
        Bundle bundle = new Bundle();
        bundle.putInt("place_id", placeId);

        DetailedPlaceFragment fragment = new DetailedPlaceFragment();
        fragment.setArguments(bundle);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container,
                fragment
        ).addToBackStack(null).commit();
        return true;
    }
}
