package com.example.frontmynbnb.adapters;

import android.content.Context;
import android.os.Bundle;
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
import com.example.frontmynbnb.activities.MainActivity;
import com.example.frontmynbnb.fragments.DetailedPlaceFragment;
import com.example.frontmynbnb.models.Place;
import com.example.frontmynbnb.models.Rating;
import java.util.ArrayList;

public class PlacesAdapter extends ArrayAdapter<Place> {
    ImageView mainImageView;
    TextView addressView;
    TextView costPerPersonView;
    TextView ratingTextView;
    RatingBar ratingView;
    Context mContext;
    String mFrom, mTo;

    public PlacesAdapter(Context ctx, ArrayList<Place> placeList, String from, String to){
        super(ctx, 0, placeList);
        mContext = ctx;
        mFrom = from;
        mTo = to;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if( view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.place_component, parent, false);
        }

        final Place place = getItem(position);
//
        mainImageView = (ImageView) view.findViewById(R.id.imageview_pc_mainimage);
        addressView = (TextView) view.findViewById(R.id.textview_pc_address);
        costPerPersonView = (TextView) view.findViewById(R.id.textview_pc_costperperson);
        ratingTextView = (TextView) view.findViewById(R.id.textview_pc_ratingvalue);
        ratingView = (RatingBar) view.findViewById(R.id.rating_pc_rating);

        mainImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Clicked: " +
                        place.getAddress(), Toast.LENGTH_LONG).show();
                //pass the place id
                Bundle bundle = new Bundle();
                bundle.putString("from", mFrom);
                bundle.putString("to", mTo);
                bundle.putInt("place_id", place.getId());
                DetailedPlaceFragment fragment = new DetailedPlaceFragment();
                fragment.setArguments(bundle);
                ((MainActivity)mContext).getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragment_container, fragment
                ).commit();
            }
        });

        addressView.setText(place.getAddress());
        costPerPersonView.setText(String.valueOf(place.getCostPerPerson()));
        float ratingSum = 0;
        for(Rating r: place.getRatings())
            ratingSum += r.getDegree();
        float ratingMean = (float)ratingSum/place.getRatings().size();
        ratingTextView.setText(String.valueOf(ratingMean));
        ratingView.setRating(ratingMean);
        if(place.getMainBitmap() != null)
            mainImageView.setImageBitmap(place.getMainBitmap());
        return view;
    }
}
