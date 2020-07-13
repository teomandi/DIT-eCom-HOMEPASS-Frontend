package com.example.frontmynbnb.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.adapters.PlacesAdapter;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.models.Message;
import com.example.frontmynbnb.models.Place;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private ListView placesContainer;
    private ArrayList<Place> placeList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);




        placeList = new ArrayList<Place>();
        placeList.add(new Place(R.drawable.malibu, "California", "Malibu", "Awesome Palace", 24, 8.2f, 3.5f));
        placeList.add(new Place(R.drawable.tail, "Thailand", "Bangkok", "Nature home", 17, 6f, 2.2f));
        placeList.add(new Place(R.drawable.athens, "Greece", "Athens", "Xenios Zeus", 44, 13f, 4));
        placeList.add(new Place(R.drawable.budapest, "Hungary", "Budapest", "Red bed", 16, 9, 3));

        placesContainer = (ListView) view.findViewById(R.id.listview_placescontainer);
        PlacesAdapter adapter = new
                PlacesAdapter(getActivity(), placeList);

        placesContainer.setAdapter(adapter);

        return view;
    }
}
