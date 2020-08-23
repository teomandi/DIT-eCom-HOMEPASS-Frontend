package com.example.frontmynbnb.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frontmynbnb.R;
import com.example.frontmynbnb.models.Place;
import java.util.ArrayList;


public class PlacesAdapter extends ArrayAdapter<Place> {

    public PlacesAdapter(Context ctx, ArrayList<Place> placeList){
        super(ctx, 0, placeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if( view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.place_component, parent, false);
        }

//        Place place = getItem(position);
//
//        ImageView placeImage = view.findViewById(R.id.imageview_place);
//        placeImage.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Fuck you <3", Toast.LENGTH_LONG).show();
//            }
//        });
//        TextView placeCountryCity = view.findViewById(R.id.textview_countrycity);
//        TextView placeName = view.findViewById(R.id.textview_name);
//        TextView placeCostPerDay = view.findViewById(R.id.textview_costperday);
//        TextView placeCostPerPerson = view.findViewById(R.id.textview_costperperson);
//        RatingBar placeRating = view.findViewById(R.id.rating);
//
//
//        assert place != null;
//        String cc = place.getCountry() + ", " + place.getCity();
//
//        placeImage.setImageResource(place.getImage());
//        placeCountryCity.setText(cc);
//        placeName.setText(place.getName());
//        placeCostPerDay.setText(String.valueOf(place.getCostPerDay()));
//        placeCostPerPerson.setText(String.valueOf(place.getGetCostPerPerson()));
//        placeRating.setRating(place.getRating());
//
//        Log.d("Place List", "Creating " + place.getCity());

        return view;

    }
}
