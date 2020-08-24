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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.adapters.AvailabilitiesAdapter;
import com.example.frontmynbnb.adapters.BenefitsAdapter;
import com.example.frontmynbnb.adapters.RulesAdapter;
import com.example.frontmynbnb.misc.Utils;
import com.example.frontmynbnb.models.Availability;
import com.example.frontmynbnb.models.Benefit;
import com.example.frontmynbnb.models.Place;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.frontmynbnb.AppConstants.MAPVIEW_BUNDLE_KEY;

public class CreatePlaceFragment extends MyFragment implements OnMapReadyCallback {

    private static final int SELECT_MAIN_IMAGE = 1;
    private static final int SELECT_MULTIPLE_IMAGES = 2;


    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LatLng latLng;
    private SearchView mSearchAddress;
    private Button mButtonAddAvailability, mButtonMultipleImages, mButtonAddBenefit, mButtonAddRule, mButtonCancel, mButtonPost;
    private ListView availableContainer, benefitContainer, ruleContainer;
    private ImageView mMainImage, mImageMultpiple;
    private TextView mTextMultImages;
    private ScrollView mScrollLayout;
    private EditText mEditBenefit, mEditRule, mEditMaxGeusts, mEditMinCost, mEditCostPerPerson, mEditBeds, mEditBaths, mEditArea, mEditDescription, mEditBedrooms;
    private CheckBox mCheckLivingRoom;
    private RadioGroup mRadioType;

    private ArrayList<Availability> availabilityList;
    private AvailabilitiesAdapter mAvAdapter;
    private ArrayList<Benefit> benefitList;
    private ArrayList<Rule> ruleList;
    private BenefitsAdapter mBenAdapter;
    private RulesAdapter mRulAdapter;
    private LinearLayout mPostingProgressView;

    private Uri mMainImageUri;
    private List<Uri> mImagesUrisList;
    private Bitmap mMainBitmap;
    private List<Bitmap> mImagesBitmapList;

    private String mAvFrom, mAvTo;
    private boolean galleryThread = true;

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

        mScrollLayout = (ScrollView) view.findViewById(R.id.scroll_layout_createplace);
        mPostingProgressView = (LinearLayout) view.findViewById(R.id.layout_postingcomponent);
        mEditMaxGeusts = (EditText) view.findViewById(R.id.edittext_maxguest);
        mEditMinCost = (EditText) view.findViewById(R.id.edittext_mincost);
        mEditCostPerPerson = (EditText) view.findViewById(R.id.edittext_costperperson);
        mEditBeds = (EditText) view.findViewById(R.id.edittext_beds);
        mEditBaths = (EditText) view.findViewById(R.id.edittext_baths);
        mEditBedrooms = (EditText) view.findViewById(R.id.edittext_bedrooms);
        mEditArea = (EditText) view.findViewById(R.id.edittext_area);
        mEditDescription = (EditText) view.findViewById(R.id.edittext_description);
        mCheckLivingRoom = (CheckBox) view.findViewById(R.id.checkbox_livingroom);
        mRadioType = (RadioGroup) view.findViewById(R.id.radio_type);

        // benefits
        mButtonPost = (Button) view.findViewById(R.id.button_postplace);
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPlace();
            }
        });
        mButtonCancel = (Button) view.findViewById(R.id.button_cancelcreate);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryThread = false;
                getFragmentManager().beginTransaction().replace(
                        R.id.fragment_container2,
                        new PlaceFragment()
                ).commit();
            }
        });
        mEditBenefit = (EditText) view.findViewById(R.id.edittext_newbenefit);
        mButtonAddBenefit = (Button) view.findViewById(R.id.button_addbenefit);
        mButtonAddBenefit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEditBenefit.getText().toString();
                if (content.isEmpty()) {
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
        mButtonAddRule = (Button) view.findViewById(R.id.button_addrule);
        mButtonAddRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEditRule.getText().toString();
                if (content.isEmpty()) {
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
        mImageMultpiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click the button above!", Toast.LENGTH_SHORT).show();
            }
        });
        mMainImage = (ImageView) view.findViewById(R.id.imageview_main);
        mMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryThread = false;
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
                galleryThread = false;
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
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    System.out.println("lat: " + latLng.latitude);
                    System.out.println("long: " + latLng.longitude);
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
            mAvFrom = String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1)
                    + "/" + String.valueOf(dayOfMonth);
        }
    };
    DatePickerDialog.OnDateSetListener onDateTo = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mAvTo = String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1)
                    + "/" + String.valueOf(dayOfMonth);

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
            } else if (resultCode == Activity.RESULT_CANCELED) {
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
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        try {
                            InputStream is = getContext().getContentResolver().openInputStream(imageUri);
                            Bitmap bmp = BitmapFactory.decodeStream(is);
                            mImagesBitmapList.add(bmp);
                            mImagesUrisList.add(imageUri);
                        } catch (FileNotFoundException e) {
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
                        mScrollLayout.smoothScrollTo(0, mImageMultpiple.getTop() - 30);
                    }
                });
                setMultipleImagesEffect();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void setMultipleImagesEffect() {
        String textImgCounter = mImagesBitmapList.size() + "images have been added";
        mTextMultImages.setText(textImgCounter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (final Bitmap b : mImagesBitmapList) {
                    if( galleryThread ) {
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
            }
        }).start();
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("Create ~~" + AppConstants.MODE);
        galleryThread = false;
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container2,
                new PlaceFragment()
        ).addToBackStack(null).commit();
        return true;
    }

    private boolean validate() {
        return (latLng != null &&
                !mEditMaxGeusts.getText().toString().equals("") &&
                !mEditMinCost.getText().toString().equals("") &&
                !mEditCostPerPerson.getText().toString().equals("") &&
                !mEditBeds.getText().toString().equals("") &&
                !mEditBaths.getText().toString().equals("") &&
                !mEditArea.getText().toString().equals("") &&
                !mEditDescription.getText().toString().equals("") &&
                mMainImageUri != null);
    }

    private void postPlace() {
        mPostingProgressView.setVisibility(View.VISIBLE);
        if (!validate()) {
            Toast.makeText(
                    getActivity(),
                    "Please fulfill all the fields",
                    Toast.LENGTH_SHORT
            ).show();
            mPostingProgressView.setVisibility(View.INVISIBLE);
            return;
        }
        RequestBody addressPart = RequestBody.create(
                MultipartBody.FORM,
                mSearchAddress.getQuery().toString()
        );
        RequestBody latPart = RequestBody.create(
                MultipartBody.FORM,
                String.valueOf(latLng.latitude)
        );
        RequestBody longPart = RequestBody.create(
                MultipartBody.FORM,
                String.valueOf(latLng.longitude)
        );
        RequestBody maxGuestPart = RequestBody.create(
                MultipartBody.FORM,
                mEditMaxGeusts.getText().toString()
        );
        RequestBody minCostPart = RequestBody.create(
                MultipartBody.FORM,
                mEditMinCost.getText().toString()
        );
        RequestBody costPerPersonPart = RequestBody.create(
                MultipartBody.FORM,
                mEditCostPerPerson.getText().toString()
        );
        RequestBody typePart = RequestBody.create(
                MultipartBody.FORM,
                mRadioType.getCheckedRadioButtonId() == R.id.radio_house ? "House" : "Room"
        );
        RequestBody descriptionPart = RequestBody.create(
                MultipartBody.FORM,
                mEditDescription.getText().toString()
        );
        RequestBody bedsPart = RequestBody.create(
                MultipartBody.FORM,
                mEditBeds.getText().toString()
        );
        RequestBody bathsPart = RequestBody.create(
                MultipartBody.FORM,
                mEditBaths.getText().toString()
        );
        RequestBody bedroomsPart = RequestBody.create(
                MultipartBody.FORM,
                mEditBedrooms.getText().toString()
        );
        RequestBody livingRoomPart = RequestBody.create(
                MultipartBody.FORM,
                String.valueOf(mCheckLivingRoom.isChecked())
        );
        RequestBody areaPart = RequestBody.create(
                MultipartBody.FORM,
                mEditArea.getText().toString()
        );
        byte[] img = null;
        try {
            InputStream iStream = getActivity().getContentResolver().openInputStream(mMainImageUri);
            img = Utils.getBytes(iStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("uri ~~> " + mMainImageUri + "   " + img.length);
        RequestBody imageFile = RequestBody.create(MediaType.parse("image/jpeg"), img);
        MultipartBody.Part mainImageFilePart = MultipartBody.Part.createFormData(
                "image",
                mMainImageUri.getLastPathSegment(),
                imageFile
        );
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Place> call = jsonPlaceHolderApi.postUsersPlace(
                AppConstants.USER.getId(),
                addressPart, latPart, longPart, maxGuestPart, minCostPart,
                costPerPersonPart, typePart, descriptionPart, bedsPart, bathsPart, bedroomsPart,
                livingRoomPart, areaPart, mainImageFilePart
        );
        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                Place p = response.body();
                postBenefits(p.getId());
                postRules(p.getId());
                postAvailabilities(p.getId());
                postImages(p.getId());
                mPostingProgressView.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                mPostingProgressView.setVisibility(View.INVISIBLE);
                Toast.makeText(
                        getContext(),
                        "Failure on putting place!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
    }

    private void postBenefits(int pid) {
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        for (Benefit b : benefitList) {
            System.out.println("sending b:" + b.getContent());

            Call<Benefit> call = jsonPlaceHolderApi.postPlaceBenefit(pid, b.getContent());
            call.enqueue(new Callback<Benefit>() {
                @Override
                public void onResponse(Call<Benefit> call, Response<Benefit> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(
                                getContext(),
                                "Not successful response " + response.code(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }
                    System.out.println("Benefit sent!");
                }

                @Override
                public void onFailure(Call<Benefit> call, Throwable t) {
                    Toast.makeText(
                            getContext(),
                            "Failure on putting benefit!",
                            Toast.LENGTH_LONG
                    ).show();
                    System.out.println("Error message:: " + t.getMessage());
                }
            });

        }
    }

    private void postRules(int pid) {
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        for (Rule r : ruleList) {
            Call<Rule> call = jsonPlaceHolderApi.postPlaceRule(pid, r.getContent());
            call.enqueue(new Callback<Rule>() {
                @Override
                public void onResponse(Call<Rule> call, Response<Rule> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(
                                getContext(),
                                "Not successful response " + response.code(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }
                    System.out.println("Rule sent!");
                }

                @Override
                public void onFailure(Call<Rule> call, Throwable t) {
                    Toast.makeText(
                            getContext(),
                            "Failure on putting Rule!",
                            Toast.LENGTH_LONG
                    ).show();
                    System.out.println("Error message:: " + t.getMessage());
                }
            });

        }
    }

    private void postAvailabilities(int pid) {
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        for (Availability av : availabilityList) {
            av.print();
            Call<Availability> call = jsonPlaceHolderApi.postPlaceAvailability(
                    pid, av.getFrom(), av.getTo());
            call.enqueue(new Callback<Availability>() {
                @Override
                public void onResponse(Call<Availability> call, Response<Availability> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(
                                getContext(),
                                "Not successful response " + response.code(),
                                Toast.LENGTH_LONG
                        ).show();
                        return;
                    }
                    System.out.println("Availability sent!");
                }

                @Override
                public void onFailure(Call<Availability> call, Throwable t) {
                    Toast.makeText(
                            getContext(),
                            "Failure on putting availability!",
                            Toast.LENGTH_LONG
                    ).show();
                    System.out.println("Error message:: " + t.getMessage());
                }
            });
        }
    }

    private void postImages(int pid) {
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        List<MultipartBody.Part> imagesPart = new ArrayList<>();
        System.out.println("selected images are: " + mImagesUrisList.size());
        for (Uri imgUri : mImagesUrisList) {
            byte[] img = null;
            try {
                InputStream iStream = getActivity().getContentResolver().openInputStream(imgUri);
                img = Utils.getBytes(iStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("uri ~~> " + imgUri + "   " + img.length);
            RequestBody imageFile = RequestBody.create(MediaType.parse("image/jpeg"), img);
            MultipartBody.Part imageFilePart = MultipartBody.Part.createFormData(
                    "images",
                    imgUri.getLastPathSegment(),
                    imageFile
            );
            imagesPart.add(imageFilePart);
        }
        System.out.println("Will send: " + + imagesPart.size());
        Call<Void> call = jsonPlaceHolderApi.postPlaceImage(pid, imagesPart);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                System.out.println("Images sent!");
                Toast.makeText(
                        getContext(),
                        "Your place was set!",
                        Toast.LENGTH_SHORT
                ).show();
                galleryThread = false;
                getFragmentManager().beginTransaction().replace(
                        R.id.fragment_container2,
                        new PlaceFragment()
                ).commit();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure on putting image!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
    }


}


