package com.example.frontmynbnb.fragments;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
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
import com.example.frontmynbnb.adapters.PlacesAdapter;
import com.example.frontmynbnb.models.Place;
import com.example.frontmynbnb.models.User;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

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

    private int pageNo;
    private final int pageSize = 10;
    private boolean fetchedAll, mNewSearch, mSearch, mAll;

    private ListView mPlacesContainer;
    private ArrayList<Place> mPlaceList;
    private PlacesAdapter mPlaceAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mEditSearch = (EditText) view.findViewById(R.id.edittext_search);
        mEditSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("appearing view");

                if (mSearchGroup.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(mSearchGroup, new AutoTransition());
                    mSearchGroup.setVisibility(View.VISIBLE);
                }
            }
        });
        mEditNumPeople = (EditText) view.findViewById(R.id.edittext_numofpeople);
        mProgressView = (LinearLayout) view.findViewById(R.id.layout_progresscomponent2);
        mSearchGroup = (LinearLayout) view.findViewById(R.id.layout_searchview);
        mBackground = (LinearLayout) view.findViewById(R.id.layout_background);
        mTextOnProgressBar = (TextView) view.findViewById(R.id.text_on_bar3);
        mRadioType = (RadioGroup) view.findViewById(R.id.radio_typesearch);
        mButtonFrom = (Button) view.findViewById(R.id.button_selectfrom);
        mButtonTo = (Button) view.findViewById(R.id.button_selectto);
        mButtonSearch = (Button) view.findViewById(R.id.button_searchplaces);
        mPlacesContainer = (ListView) view.findViewById(R.id.listview_result_placescontainer);
        mPlacesContainer.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && !fetchedAll) {
                    pageNo += 1;
                    if (mSearch)
                        searchPlaces();
                    else
                        fetchPlaces();
                    System.out.println("firstVisibleItem: " + firstVisibleItem);
                    System.out.println("visibleItemCount: " + visibleItemCount);
                    System.out.println("totalItemCount: " + totalItemCount);
                    System.out.println("totalItemCount: " + totalItemCount);
                    System.out.println("Fetching places!!!" + pageNo);
                }
            }
        });
        //if clicked somewhere else then minimize it
        mBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchGroup.getVisibility() == View.VISIBLE) {
                    System.out.println("disappearing view");
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
                fetchedAll = false;
                pageNo = 0;
                mSearch = true;
                mNewSearch = true;
                searchPlaces();
            }
        });
        mPlaceList = new ArrayList<>();
        mPlaceAdapter = new PlacesAdapter(getActivity(), mPlaceList, mButtonFrom.getText().toString(), mButtonTo.getText().toString());
        mPlacesContainer.setAdapter(mPlaceAdapter);
        pageNo = 0;
        fetchedAll = false;
        mSearch = false;
//        fetchPlaces();
//        checkForPreviousSearches();
        fetchUserDetails();
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

    private boolean validate(String location, String from, String to, String numOfPeople) {
        if (location == null || location.equals(""))
            return false;
        if (from.equals(getResources().getString(R.string.empty_date)) ||
                to.equals(getResources().getString(R.string.empty_date)))
            return false;
        try {
            Integer.parseInt(numOfPeople);
        } catch (NumberFormatException e) {
            return false;
        }
        return !numOfPeople.equals("");
    }

    private void fetchPlaces() {
//        mProgressView.setVisibility(View.VISIBLE);
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Place>> call = null;
        if(mAll){
            System.out.println("fetching all places");
            call = jsonPlaceHolderApi.getAllPlaces(pageNo, pageSize);
        }
        else{
            System.out.println("fetching searched places!");
            call = jsonPlaceHolderApi.getSearchedPlaces(pageNo, pageSize, AppConstants.USER.getId());
        }
        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                mProgressView.setVisibility(View.INVISIBLE);
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                Toast.makeText(
                        getContext(),
                        "Place fetched with success!",
                        Toast.LENGTH_SHORT
                ).show();
                List<Place> fetchedPlaces = response.body();
                fetchedAll = fetchedPlaces.size() < pageSize;
                for (Place p : fetchedPlaces) {
                    mPlaceList.add(p);
                    fetchMainImage(p);
                    mPlaceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "No place fetched!",
                        Toast.LENGTH_SHORT
                ).show();
                System.out.println("Error message:: " + t.getMessage());
                mProgressView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void fetchMainImage(final Place place) {
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
                            "Not successful response on place: " + place.getId() + " || " + response.code(),
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
                System.out.println("got image bro " + place.getId());
                assert mainImageBytes != null;
                Bitmap mainImage = BitmapFactory.decodeByteArray(mainImageBytes, 0, mainImageBytes.length);
                place.setMainBitmap(mainImage);
                mPlaceAdapter.notifyDataSetChanged();
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
    }

    public void searchPlaces() {
        String location = mEditSearch.getText().toString();
        String from = mButtonFrom.getText().toString();
        String to = mButtonTo.getText().toString();
        String type = mRadioType.getCheckedRadioButtonId() == R.id.radio_house2 ? "House" : "Room";
        String numOfPeople = mEditNumPeople.getText().toString();
        if (!validate(location, from, to, numOfPeople)) {
            Toast.makeText(getContext(), "Fields are missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressView.setVisibility(View.VISIBLE);
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
            mProgressView.setVisibility(View.INVISIBLE);
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
        Call<List<Place>> call = jsonPlaceHolderApi.searchPlaces(
                AppConstants.USER.getId(), pageNo, pageSize, type, from, to, latLng.latitude, latLng.longitude, numPeople);
        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                mProgressView.setVisibility(View.INVISIBLE);
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                if (mNewSearch) {
                    System.out.println("new search!!");
                    mPlaceList.removeAll(mPlaceList);
                    mNewSearch = false;
                }
                List<Place> fetchedPlaces = response.body();
                fetchedAll = fetchedPlaces.size() < pageSize;
                System.out.println("got results: " + fetchedPlaces.size() + "!!");
                for (Place p : fetchedPlaces) {
                    mPlaceList.add(p);
                    fetchMainImage(p);
                }
                Toast.makeText(
                        getContext(),
                        "Found: " + fetchedPlaces.size() + " beautiful places",
                        Toast.LENGTH_LONG
                ).show();

                mPlaceAdapter.notifyDataSetChanged();
                mPlaceAdapter.getDateRange(mButtonFrom.getText().toString(), mButtonTo.getText().toString());
                //close the view
                if (mSearchGroup.getVisibility() == View.VISIBLE) {
                    System.out.println("disappearing view");
                    TransitionManager.beginDelayedTransition(mSearchGroup, new AutoTransition());
                    mSearchGroup.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure on search!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
                mProgressView.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void checkForPreviousSearches(){
        System.out.println("CHECKINGGGGGGGGGG");
        mProgressView.setVisibility(View.VISIBLE);
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Boolean> call = jsonPlaceHolderApi.hasSearched(AppConstants.USER.getId());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                mProgressView.setVisibility(View.INVISIBLE);
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                System.out.println("===>response for previous searches got:: " + response.body());
                if(response.body()){
                    fetchPlaces();
                    mAll = false;
                    System.out.println("===>there are previous searches");
                }
                else{
                    mAll = true;
                    System.out.println("===>There are no previous searches!");
                    fetchPlaces();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "No place fetched!",
                        Toast.LENGTH_SHORT
                ).show();
                System.out.println("Error message:: " + t.getMessage());
                mProgressView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void fetchUserDetails() {
        System.out.println("TOKEN: " + AppConstants.TOKEN);
        String token = AppConstants.TOKEN;
        String username = AppConstants.USERNAME;
        Retrofit retrofit = RestClient.getClient(token);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<User> call = jsonPlaceHolderApi.getUserByUsername(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    mAll = true;
                    fetchPlaces();
                    return;
                }
                System.out.println("GOT USER !!!! Status Code : " + response.code());

                AppConstants.USER = response.body();
                checkForPreviousSearches();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure fetching the user!!",
                        Toast.LENGTH_LONG
                ).show();
                mAll = true;
                fetchPlaces();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
    }
}
