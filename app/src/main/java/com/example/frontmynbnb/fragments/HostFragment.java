package com.example.frontmynbnb.fragments;

import android.app.DatePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontmynbnb.R;
import com.example.frontmynbnb.adapters.AvailabilitiesAdapter;
import com.example.frontmynbnb.models.Availability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.frontmynbnb.AppConstants.MAPVIEW_BUNDLE_KEY;

public class HostFragment extends Fragment implements OnMapReadyCallback {

    public static final int REQUEST_CODE = 11; // Used to identify the result

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private SearchView mSearchAddress;
    private Button mButtonAddAvailability;
    private ListView availableContainer;

    private ArrayList<Availability> availabilityList;
    private AvailabilitiesAdapter mAvAdapter;

    private String mAvFrom, mAvTo;


    public HostFragment() {
        // Required empty public constructor
    }

    public static HostFragment newInstance() {
        HostFragment fragment = new HostFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host, container, false);

        availabilityList = new ArrayList<Availability>();
        mButtonAddAvailability = view.findViewById(R.id.add_availability);
        mButtonAddAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Adding availability");
                mAvFrom = "";
                mAvTo = "";


                DatePickerFragment dpf2 = new DatePickerFragment().newInstance();
                dpf2.setCallBack(onDateTo);
                dpf2.show(getFragmentManager().beginTransaction(), "DatePickerFragment");
                DatePickerFragment dpf = new DatePickerFragment().newInstance();
                dpf.setCallBack(onDateFrom);
                dpf.show(getFragmentManager().beginTransaction(), "DatePickerFragment");

            }
        });
        availableContainer = (ListView) view.findViewById(R.id.listview_availablecontainer);
        availableContainer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                availabilityList.remove(position);
                mAvAdapter.notifyDataSetChanged();
                return false;
            }


        });
        mAvAdapter = new AvailabilitiesAdapter(getActivity(), availabilityList);
        availableContainer.setAdapter(mAvAdapter);

        mMapView = (MapView) view.findViewById(R.id.mapview);
        mSearchAddress = (SearchView) view.findViewById(R.id.searchview_address);
        mSearchAddress.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mSearchAddress.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
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
                        return false;
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mGoogleMap.clear();
                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        initGoogleMap(savedInstanceState);
        return view;
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

    DatePickerDialog.OnDateSetListener onDateFrom = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mAvFrom =  String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1)
                    + "/" + String.valueOf(dayOfMonth);
        }
    };
    DatePickerDialog.OnDateSetListener onDateTo = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mAvTo = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1)
                    + "-" + String.valueOf(dayOfMonth);

            Availability availability = new Availability(mAvFrom, mAvTo);
            availabilityList.add(availability);
            mAvAdapter.notifyDataSetChanged();
        }
    };



}


