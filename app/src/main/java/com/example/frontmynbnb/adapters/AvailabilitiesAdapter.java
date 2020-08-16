package com.example.frontmynbnb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.models.Availability;

import java.util.ArrayList;

public class AvailabilitiesAdapter extends ArrayAdapter<Availability>  {

    public AvailabilitiesAdapter(Context ctx, ArrayList<Availability> availabilityList){
        super(ctx, 0, availabilityList);
    }

    private Availability availability;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if( view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.availability_component, parent, false);
        }
        availability = getItem(position);
        TextView mTextFrom = view.findViewById(R.id.textview_from);
        mTextFrom.setText(availability.getFrom());
        TextView mTextTo = view.findViewById(R.id.textview_to);
        mTextTo.setText(availability.getTo());

        return view;
    }
}




