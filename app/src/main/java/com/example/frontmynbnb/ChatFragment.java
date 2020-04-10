package com.example.frontmynbnb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChatFragment extends Fragment {
    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";


    private int id;
    private String name;

    public static ChatFragment newInstance(int id, String name){
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_chat, container, false);

        if(getArguments() != null){
            id = getArguments().getInt(ARG_ID);
            name = getArguments().getString(ARG_NAME);
            System.out.println("id: " + id + " name: " + name);

        }

        return v;
    }
}
