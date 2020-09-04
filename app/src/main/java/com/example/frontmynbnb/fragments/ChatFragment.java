package com.example.frontmynbnb.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.R;

import java.util.Objects;

public class ChatFragment extends MyFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);
        if(getArguments() != null){
            int otherUserId = getArguments().getInt("other_user_id");
            System.out.println("otherUserId: " + otherUserId);
        }
        return view;
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("CHAT");
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                AppConstants.MODE.equals("GUEST") ? R.id.fragment_container : R.id.fragment_container2,
                AppConstants.MODE.equals("GUEST") ? new MessagesFragment() : new MessagesFragment()
        ).addToBackStack(null).commit();
        return true;
    }
}
