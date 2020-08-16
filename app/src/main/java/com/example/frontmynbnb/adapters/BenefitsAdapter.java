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

public class BenefitsAdapter extends ArrayAdapter<String> {
    public BenefitsAdapter(Context ctx, ArrayList<String> benefitsList) {
        super(ctx, 0, benefitsList);
    }

    private String benefit;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.benefit_component, parent, false);
        }

        benefit = getItem(position);
        TextView mTextContent = view.findViewById(R.id.textview_benefit);
        mTextContent.setText(benefit);
        return view;
    }
}
