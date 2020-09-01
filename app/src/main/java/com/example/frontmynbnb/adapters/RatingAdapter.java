package com.example.frontmynbnb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frontmynbnb.R;
import com.example.frontmynbnb.models.Rating;

import java.util.ArrayList;
import java.util.List;

public class RatingAdapter extends ArrayAdapter<Rating> {
    Context mContext;

    public RatingAdapter(Context ctx, List<Rating> ratingList){
        super(ctx, 0, ratingList);
        mContext = ctx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if( view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.rating_component, parent, false);
        }
        final Rating r = getItem(position);


        TextView mTextComment = (TextView) view.findViewById(R.id.textview_rating_comment);
        TextView mTextDegree = (TextView) view.findViewById(R.id.textview_rating_degree);
        RatingBar mRatingBar = (RatingBar) view.findViewById(R.id.textview_rating_stars);
        mTextComment.setText(r.getComment());
        mTextDegree.setText(String.valueOf(r.getDegree()));
        mRatingBar.setRating(r.getDegree());


        return view;
    }
}
