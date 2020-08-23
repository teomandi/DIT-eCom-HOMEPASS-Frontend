package com.example.frontmynbnb.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.adapters.AvailabilitiesAdapter;
import com.example.frontmynbnb.adapters.BenefitsAdapter;
import com.example.frontmynbnb.adapters.RulesAdapter;
import com.example.frontmynbnb.models.Availability;
import com.example.frontmynbnb.models.Benefit;
import com.example.frontmynbnb.models.Image;
import com.example.frontmynbnb.models.Place;
import com.example.frontmynbnb.models.Rule;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.frontmynbnb.AppConstants.MAPVIEW_BUNDLE_KEY;


public class PlaceFragment extends MyFragment implements OnMapReadyCallback {
    private LinearLayout noPlaceView, progressBar;
    private ScrollView havingPlaceView;
    private TextView textAddress, textMaxGuest, textMinCost, textCostPerPerson, textBeds, textBaths,
            textBedrooms, textLivingRoom, textArea, textType, textDescription;
    private ListView containerBenefits, containerRules, containerAvailability;
    private Button buttonCreatePlace, buttonDeletePlace, buttonEditPlace;
    private ImageView placeGallery;
    private Place myPlace;

    private BenefitsAdapter mBenAdapter;
    private RulesAdapter mRulAdapter;
    private AvailabilitiesAdapter mAvAdapter;

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LatLng mlatLng;

    private List<Bitmap> mBitmapList;
    private boolean stopGalleryThread = false;

    private void setPlaceOnView(){
        textAddress.setText(myPlace.getAddress());
        textMaxGuest.setText(String.valueOf(myPlace.getMaxGuest()));
        textMinCost.setText(String.valueOf(myPlace.getMinCost()));
        textCostPerPerson.setText(String.valueOf(myPlace.getCostPerPerson()));
        textBeds.setText(String.valueOf(myPlace.getBeds()));
        textBaths.setText(String.valueOf(myPlace.getBaths()));
        textBedrooms.setText(String.valueOf(myPlace.getBedrooms()));
        textLivingRoom.setText(myPlace.isLivingRoom() ? "Yes" : "No");
        textArea.setText(String.valueOf(myPlace.getArea()));
        textType.setText(myPlace.getType());
        textDescription.setText(myPlace.getDescription());
        //set ListViews
        mBenAdapter = new BenefitsAdapter(getActivity(), (ArrayList<Benefit>)myPlace.getBenefits());
        containerBenefits.setAdapter(mBenAdapter);
        mRulAdapter = new RulesAdapter(getActivity(), (ArrayList<Rule>)myPlace.getRules());
        containerRules.setAdapter(mRulAdapter);
        for (Availability av : myPlace.getAvailabilities()){
            av.setFrom(av.getFrom().split("T")[0]);
            av.setTo(av.getTo().split("T")[0]);
        }
        myPlace.printDetails();
        mAvAdapter = new AvailabilitiesAdapter(getActivity(), (ArrayList<Availability>)myPlace.getAvailabilities());
        containerAvailability.setAdapter(mAvAdapter);
        //set GoogleMap
        mlatLng = new LatLng(
                Double.parseDouble(myPlace.getLatitude()),
                Double.parseDouble(myPlace.getLongitude())
        );
        //fetch images
        mBitmapList = new ArrayList<>();
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderApi.class));
        Call<ResponseBody> call = jsonPlaceHolderApi.getPlaceMainImage(myPlace.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + String.valueOf(response.code()),
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
                mBitmapList.add(BitmapFactory.decodeByteArray(mainImageBytes, 0, mainImageBytes.length));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure on main-image call!!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
        for(Image img: myPlace.getImages()) {
            Call<ResponseBody> call2 = jsonPlaceHolderApi.getImageById(img.getId());
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(
                                getContext(),
                                "Not successful response " + String.valueOf(response.code()),
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }
                    byte[] imageBytes = new byte[0];
                    try {
                        imageBytes = response.body().bytes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert imageBytes != null;
                    mBitmapList.add(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(
                            getContext(),
                            "Failure on simple-image call!!",
                            Toast.LENGTH_LONG
                    ).show();
                    System.out.println("Error message:: " + t.getMessage());
                }
            });
        }
        myPlace.printDetails();
        enableGalleryEffect();
        havingPlaceView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_place, container, false);
        //initialize the view
        noPlaceView = (LinearLayout) view.findViewById(R.id.no_having_home_view);
        havingPlaceView = (ScrollView) view.findViewById(R.id.having_home_view);
        progressBar = (LinearLayout) view.findViewById(R.id.progressBarPlace);
        placeGallery = (ImageView) view.findViewById(R.id.imageview_mygallery);
        textAddress = (TextView) view.findViewById(R.id.textview_myaddress);
        mMapView = (MapView) view.findViewById(R.id.mapview2);
        textMaxGuest = (TextView) view.findViewById(R.id.textview_mymaxguest);
        textMinCost = (TextView) view.findViewById(R.id.textview_mymincost);
        textCostPerPerson = (TextView) view.findViewById(R.id.textview_mycostperperson);
        textBeds = (TextView) view.findViewById(R.id.textview_mybeds);
        textBaths = (TextView) view.findViewById(R.id.textview_mybaths);
        textBedrooms = (TextView) view.findViewById(R.id.textview_mybedrooms);
        textLivingRoom = (TextView) view.findViewById(R.id.textview_mylivingroom);
        textArea = (TextView) view.findViewById(R.id.textview_myarea);
        textType = (TextView) view.findViewById(R.id.textview_mytype);
        textDescription = (TextView) view.findViewById(R.id.textview_mydescription);
        containerBenefits = (ListView) view.findViewById(R.id.listview_benefitcontainer2);
        containerRules = (ListView) view.findViewById(R.id.listview_rulescontainer2);
        containerAvailability = (ListView) view.findViewById(R.id.listview_availablecontainer2);
        buttonCreatePlace = (Button) view.findViewById(R.id.button_setplace);
        buttonCreatePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(
                        R.id.fragment_container2,
                        new CreatePlaceFragment()
                ).commit();
            }
        });
        buttonEditPlace = (Button) view.findViewById(R.id.button_edit_myplace);
        buttonEditPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGalleryThread = true;
                getFragmentManager().beginTransaction().replace(
                        R.id.fragment_container2,
                        new CreatePlaceFragment()
                ).commit();
            }
        });
        buttonDeletePlace = (Button) view.findViewById(R.id.button_delete_myplace);
        //fetch the place of the user
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Place> call = jsonPlaceHolderApi.getUsersPlaceByUsername(AppConstants.USERNAME);
        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + String.valueOf(response.code()),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                Toast.makeText(
                        getContext(),
                        "Place fetched with success!",
                        Toast.LENGTH_SHORT
                ).show();
                myPlace = response.body();
                myPlace.printDetails();
                setPlaceOnView();
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "No place fetched!",
                        Toast.LENGTH_SHORT
                ).show();
                System.out.println("Error message:: " + t.getMessage());
                noPlaceView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        initGoogleMap(savedInstanceState);
        return view;
    }

    private void enableGalleryEffect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for (final Bitmap b : mBitmapList) {
                        if(stopGalleryThread)
                            break;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                placeGallery.setImageBitmap(b);
                            }
                        });
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(stopGalleryThread)
                        break;
                }
            }
        }).start();
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        if(mlatLng!=null) {
            mGoogleMap.addMarker(new MarkerOptions().position(mlatLng).title("Your Place"));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mlatLng, 11));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
