package com.example.frontmynbnb.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.adapters.AvailabilitiesAdapter;
import com.example.frontmynbnb.adapters.BenefitsAdapter;
import com.example.frontmynbnb.adapters.RulesAdapter;
import com.example.frontmynbnb.models.Availability;
import com.example.frontmynbnb.models.Benefit;
import com.example.frontmynbnb.models.Rule;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.frontmynbnb.AppConstants.MAPVIEW_BUNDLE_KEY;

public class CreatePlaceFragment extends MyFragment implements OnMapReadyCallback {

    private static final int SELECT_MAIN_IMAGE = 1;
    private static final int SELECT_MULTIPLE_IMAGES = 2;


    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private SearchView mSearchAddress;
    private Button mButtonAddAvailability, mButtonMultipleImages, mButtonAddBenefit, mButtonAddRule
            , mButtonCancel, mButtonPost;
    private ListView availableContainer, benefitContainer, ruleContainer;
    private ImageView mMainImage, mImageMultpiple;
    private TextView mTextMultImages;
    private ScrollView mScrollLayout;
    private EditText mEditBenefit, mEditRule;

    private ArrayList<Availability> availabilityList;
    private AvailabilitiesAdapter mAvAdapter;
    private ArrayList<Benefit> benefitList;
    private ArrayList<Rule> ruleList;
    private BenefitsAdapter mBenAdapter;
    private RulesAdapter mRulAdapter;

    private Uri mMainImageUri;
    private List<Uri> mImagesUrisList;
    private Bitmap mMainBitmap;
    private List<Bitmap> mImagesBitmapList;

    private String mAvFrom, mAvTo;


    public CreatePlaceFragment() {
        // Required empty public constructor
    }

    public static CreatePlaceFragment newInstance() {
        CreatePlaceFragment fragment = new CreatePlaceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createplace, container, false);

        availabilityList = new ArrayList<Availability>();
        benefitList = new ArrayList<Benefit>();
        ruleList = new ArrayList<Rule>();

        mScrollLayout = (ScrollView) view.findViewById(R.id.scroll_layout);
        // benefits
        mButtonPost = (Button) view.findViewById(R.id.button_postplace);
        mButtonCancel = (Button) view.findViewById(R.id.button_cancelcreate);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(
                        R.id.fragment_container2,
                        new PlaceFragment()
                ).commit();
            }
        });
        mEditBenefit = (EditText) view.findViewById(R.id.edittext_newbenefit);
        mButtonAddBenefit = (Button)  view.findViewById(R.id.button_addbenefit);
        mButtonAddBenefit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEditBenefit.getText().toString();
                if( content.isEmpty() ) {
                    Toast.makeText(
                            getContext(),
                            "Benefit is empty!",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                benefitList.add(new Benefit(content));
                mBenAdapter.notifyDataSetChanged();
                mEditBenefit.setText("");
                Toast.makeText(
                        getContext(),
                        "Benefit added",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        benefitContainer = (ListView) view.findViewById(R.id.listview_benefitcontainer);
        benefitContainer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                benefitList.remove(position);
                mBenAdapter.notifyDataSetChanged();
                return false;
            }
        });
        mBenAdapter = new BenefitsAdapter(getActivity(), benefitList);
        benefitContainer.setAdapter(mBenAdapter);
        // rules
        mEditRule = (EditText) view.findViewById(R.id.edittext_newrule);
        mButtonAddRule = (Button)  view.findViewById(R.id.button_addrule);
        mButtonAddRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEditRule.getText().toString();
                if( content.isEmpty() ) {
                    Toast.makeText(
                            getContext(),
                            "Rule is empty!",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                ruleList.add(new Rule(content));
                mRulAdapter.notifyDataSetChanged();
                mEditRule.setText("");
                Toast.makeText(
                        getContext(),
                        "Rule added!",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        ruleContainer = (ListView) view.findViewById(R.id.listview_rulecontainer);
        ruleContainer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ruleList.remove(position);
                mRulAdapter.notifyDataSetChanged();
                return false;
            }
        });
        mRulAdapter = new RulesAdapter(getActivity(), ruleList);
        ruleContainer.setAdapter(mRulAdapter);


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
        mImageMultpiple = (ImageView) view.findViewById(R.id.imageview_multiple);
        mMainImage = (ImageView) view.findViewById(R.id.imageview_main);
        mMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(
                        intent,
                        "Select Picture"
                ), SELECT_MAIN_IMAGE);
            }
        });
        mTextMultImages = (TextView) view.findViewById(R.id.textview_multimages);
        mButtonMultipleImages = (Button) view.findViewById(R.id.button_multimages);
        mButtonMultipleImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setAction();
                startActivityForResult(Intent.createChooser(
                        intent,
                        "Select Picture"
                        ), SELECT_MULTIPLE_IMAGES);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_MAIN_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        mMainImageUri = data.getData();
                        mMainBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                        mMainImage.setImageBitmap(mMainBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
            //scroll there
            mScrollLayout.post(new Runnable() {
                @Override
                public void run() {
                    mScrollLayout.smoothScrollTo(0, mMainImage.getTop() - 30);
                }
            });
        } else if (requestCode == SELECT_MULTIPLE_IMAGES) {
            if (resultCode == Activity.RESULT_OK) {
                mImagesBitmapList = new ArrayList<>();
                mImagesUrisList = new ArrayList<>();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for ( int i=0; i<clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        mImagesUrisList.add(imageUri);
                        try {
                            InputStream is = getContext().getContentResolver().openInputStream(imageUri);
                            Bitmap bmp = BitmapFactory.decodeStream(is);
                            mImagesBitmapList.add(bmp);
                            mImagesUrisList.add(imageUri);
                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                } else {
                    Uri imageUri = data.getData();
                    try {
                        InputStream is = getContext().getContentResolver().openInputStream(imageUri);
                        Bitmap bmp = BitmapFactory.decodeStream(is);
                        mImagesBitmapList.add(bmp);
                        mImagesUrisList.add(imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                //scroll there
                mScrollLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollLayout.smoothScrollTo(0, mImageMultpiple.getBottom());
                    }
                });
                setMultipleImagesEffect();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void setMultipleImagesEffect(){
        String textImgCounter = mImagesBitmapList.size() + "images have been added";
        mTextMultImages.setText(textImgCounter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (final Bitmap b : mImagesBitmapList){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mImageMultpiple.setImageBitmap(b);
                        }
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("Create ~~" + AppConstants.MODE);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container2,
                new PlaceFragment()
        ).addToBackStack(null).commit();
        return true;
    }


}


