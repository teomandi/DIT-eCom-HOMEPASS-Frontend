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

import java.util.ArrayList;

public class RulesAdapter extends ArrayAdapter<String> {
    public RulesAdapter(Context ctx, ArrayList<String> rulesList) {
        super(ctx, 0, rulesList);
    }

    private String benefit;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.rule_component, parent, false);
        }

        benefit = getItem(position);
        TextView mTextContent = view.findViewById(R.id.textview_rule);
        mTextContent.setText(benefit);
        return view;
    }
}
