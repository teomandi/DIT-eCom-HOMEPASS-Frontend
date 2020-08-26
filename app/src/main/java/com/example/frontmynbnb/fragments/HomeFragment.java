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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.models.Place;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private EditText mEditSearch, mEditNumPeople;
    private LinearLayout mProgressView, mSearchGroup, mBackground;
    private TextView mTextOnProgressBar;
    private RadioGroup mRadioType;
    private Button mButtonFrom, mButtonTo, mButtonSearch;


    private ListView placesContainer;
    private ArrayList<Place> placeList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mEditSearch = (EditText) view.findViewById(R.id.edittext_search);
        mEditNumPeople = (EditText) view.findViewById(R.id.edittext_numofpeople);
        mProgressView = (LinearLayout) view.findViewById(R.id.layout_progresscomponent2);
        mSearchGroup = (LinearLayout) view.findViewById(R.id.layout_searchview);
        mBackground = (LinearLayout) view.findViewById(R.id.layout_background);
        mTextOnProgressBar = (TextView) view.findViewById(R.id.text_on_bar3);
        mRadioType = (RadioGroup) view.findViewById(R.id.radio_typesearch);
        mButtonFrom = (Button) view.findViewById(R.id.button_selectfrom);
        mButtonTo = (Button) view.findViewById(R.id.button_selectto);
        mButtonSearch = (Button) view.findViewById(R.id.button_searchplaces);

        mEditSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSearchGroup.getVisibility() == View.GONE){
                    TransitionManager.beginDelayedTransition(mSearchGroup, new AutoTransition());
                    mSearchGroup.setVisibility(View.VISIBLE);
                }
            }
        });
        //if clicked somewhere else then minimize it
        mBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSearchGroup.getVisibility() == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(mSearchGroup, new AutoTransition());
                    mSearchGroup.setVisibility(View.GONE);
                }
            }
        });


        return view;
    }
}
