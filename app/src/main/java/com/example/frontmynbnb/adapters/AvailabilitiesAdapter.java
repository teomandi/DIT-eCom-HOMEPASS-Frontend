package com.example.frontmynbnb.adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.frontmynbnb.R;
import com.example.frontmynbnb.fragments.DatePickerFragment;
import com.example.frontmynbnb.models.Availability;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AvailabilitiesAdapter extends ArrayAdapter<Availability>  {

    public AvailabilitiesAdapter(Context ctx, ArrayList<Availability> availabilityList){
        super(ctx, 0, availabilityList);
        mContext = ctx;
    }

    Availability availability;
    Context mContext;

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
//
//    private void getDate(){
//        System.out.println("getDate");
//        DialogFragment datePicker = new DatePickerFragment();
//        datePicker.show(((FragmentActivity)mContext).getSupportFragmentManager(), "Date picker");
//    }
//
//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, month);
//        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        String selectedDate = DateFormat.getDateTimeInstance().format(c.getTime());
//
//    }
}




