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
import com.example.frontmynbnb.models.Rule;

import java.util.ArrayList;

public class RulesAdapter extends ArrayAdapter<Rule> {
    public RulesAdapter(Context ctx, ArrayList<Rule> rulesList) {
        super(ctx, 0, rulesList);
    }

    private Rule rule;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.rule_component, parent, false);
        }

        rule = getItem(position);
        TextView mTextContent = view.findViewById(R.id.textview_rule);
        mTextContent.setText(rule.getContent());
        return view;
    }
}
