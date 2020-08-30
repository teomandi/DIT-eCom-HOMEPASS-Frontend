package com.example.frontmynbnb.fragments;

import android.app.DatePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.models.Availability;
import com.example.frontmynbnb.models.Place;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {

    private EditText mEditSearch, mEditNumPeople;
    private LinearLayout mProgressView, mSearchGroup, mBackground;
    private TextView mTextOnProgressBar;
    private RadioGroup mRadioType;
    private Button mButtonFrom, mButtonTo, mButtonSearch;

    private String mFrom, mTo;



    private ListView placesContainer;
    private ArrayList<Place> placeList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mEditSearch = (EditText) view.findViewById(R.id.edittext_search);
        mEditNumPeople = (EditText) view.findViewById(R.id.edittext_numofpeople);
        mProgressView = (LinearLayout) view.findViewById(R.id.layout_progresscomponent2);
        mSearchGroup = (LinearLayout) view.findViewById(R.id.layout_searchview);
        mBackground = (LinearLayout) view.findViewById(R.id.layout_background);
        mTextOnProgressBar = (TextView) view.findViewById(R.id.text_on_bar3);
        mRadioType = (RadioGroup) view.findViewById(R.id.radio_typesearch);
        mButtonFrom = (Button) view.findViewById(R.id.button_selectfrom);
        mButtonTo = (Button) view.findViewById(R.id.button_selectto);
        mButtonSearch = (Button) view.findViewById(R.id.button_searchplaces);

        // buttons listeners
        mEditSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchGroup.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(mSearchGroup, new AutoTransition());
                    mSearchGroup.setVisibility(View.VISIBLE);
                }
            }
        });
        //if clicked somewhere else then minimize it
        mBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchGroup.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(mSearchGroup, new AutoTransition());
                    mSearchGroup.setVisibility(View.GONE);
                }
            }
        });
        mButtonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dpf = new DatePickerFragment().newInstance();
                dpf.setCallBack(onDateFrom);
                dpf.show(getFragmentManager().beginTransaction(), "DatePickerFragment");
            }
        });
        mButtonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dpf = new DatePickerFragment().newInstance();
                dpf.setCallBack(onDateTo);
                dpf.show(getFragmentManager().beginTransaction(), "DatePickerFragment");
            }
        });
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = mEditSearch.getText().toString();
                String from = mButtonFrom.getText().toString();
                String to = mButtonTo.getText().toString();
                String type = mRadioType.getCheckedRadioButtonId() == R.id.radio_house2 ? "House" : "Room";
                String numOfPeople = mEditNumPeople.getText().toString();
                if(!validate(location, from, to, numOfPeople)){
                    Toast.makeText(getContext(), "Fields are missing!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int numPeople = Integer.parseInt(numOfPeople);
                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList.size() == 0) {
                    Toast.makeText(
                            getContext(),
                            "No location found.",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                System.out.println("~~> Search vars:");
                System.out.println("lat: " + latLng.latitude);
                System.out.println("long: " + latLng.longitude);
                System.out.println("From: " + from);
                System.out.println("To: " + to);
                System.out.println("NumberOfPeople: " + numOfPeople);
                System.out.println("Type: " + type);
                Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<ResponseBody> call = jsonPlaceHolderApi.searchPlaces(
                        type, from, to,latLng.latitude, latLng.longitude, numPeople);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(
                                    getContext(),
                                    "Not successful response " + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                            return;
                        }
                        String show = null;
                        try {
                            show = response.code() + " | " + response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(show);
                        Toast.makeText(
                                getContext(),
                                show,
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(
                                getContext(),
                                "Failure on search!",
                                Toast.LENGTH_LONG
                        ).show();
                        System.out.println("Error message:: " + t.getMessage());
                    }
                });

            }
        });

        return view;
    }


    DatePickerDialog.OnDateSetListener onDateFrom = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
             String from = String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1)
                    + "/" + String.valueOf(dayOfMonth);
            mButtonFrom.setText(from);

        }
    };
    DatePickerDialog.OnDateSetListener onDateTo = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String to = String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1)
                    + "/" + String.valueOf(dayOfMonth);
            mButtonTo.setText(to);
        }
    };

    private boolean validate(String location, String from, String to, String numOfPeople){
        if(location == null || location.equals(""))
            return false;
        if(from.equals(getResources().getString(R.string.empty_date)) ||
                to.equals(getResources().getString(R.string.empty_date)))
            return false;
        try {
            Integer.parseInt(numOfPeople);
        }catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

}
