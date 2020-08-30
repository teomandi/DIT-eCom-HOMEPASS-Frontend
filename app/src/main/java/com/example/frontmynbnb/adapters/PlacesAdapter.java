package com.example.frontmynbnb.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.models.Place;
import com.example.frontmynbnb.models.Rating;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PlacesAdapter extends ArrayAdapter<Place> {
    ImageView mainImageView;
    TextView addressView;
    TextView costPerPersonView;
    TextView ratingTextView;
    RatingBar ratingView;

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

        //fetch main image
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderApi.class));
        Call<ResponseBody> call = jsonPlaceHolderApi.getPlaceMainImage(place.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response on place: " + place.getId() + " || "  + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                byte[] mainImageBytes = new byte[0];
                try {
                    mainImageBytes = response.body().bytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert mainImageBytes != null;
                Bitmap mainImage = BitmapFactory.decodeByteArray(mainImageBytes, 0, mainImageBytes.length);
                mainImageView.setImageBitmap(mainImage);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure on main-image call!! place: " + place.getId(),
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });

        return view;

    }
}
