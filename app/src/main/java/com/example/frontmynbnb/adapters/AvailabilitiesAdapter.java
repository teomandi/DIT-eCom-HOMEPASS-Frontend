package com.example.frontmynbnb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.models.Availability;

import java.util.ArrayList;

public class AvailabilitiesAdapter extends ArrayAdapter<Availability> {

    public AvailabilitiesAdapter(Context ctx, ArrayList<Availability> availabilityList){
        super(ctx, 0, availabilityList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if( view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.availability_component, parent, false);
        }

        Availability availability = getItem(position);

        Button mButtonFrom = view.findViewById(R.id.button_from);
        Button mButtonTo = view.findViewById(R.id.button_to);
        TextView mTextFrom = view.findViewById(R.id.textview_from);
        TextView mTextTo = view.findViewById(R.id.textview_to);
        Button mButtonCancel = view.findViewById(R.id.button_av_close);


        return view;

    }
}
