package com.example.frontmynbnb.fragments;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.models.Place;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private EditText mEditSearch;
    LinearLayout targetView;
    Button miniButton, expandButton;

    private ListView placesContainer;
    private ArrayList<Place> placeList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mEditSearch = (EditText) view.findViewById(R.id.edittext_search);
        targetView = (LinearLayout) view.findViewById(R.id.moving_view);

        expandButton = (Button) view.findViewById(R.id.expand_button);
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(targetView.getVisibility() == View.GONE){
                    TransitionManager.beginDelayedTransition(targetView, new AutoTransition());
                    targetView.setVisibility(View.VISIBLE);
                }
            }
        });
        miniButton = (Button) view.findViewById(R.id.minimaze_button);
        miniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(targetView.getVisibility() == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(targetView, new AutoTransition());
                    targetView.setVisibility(View.GONE);
                }
            }
        });


        return view;
    }
}
