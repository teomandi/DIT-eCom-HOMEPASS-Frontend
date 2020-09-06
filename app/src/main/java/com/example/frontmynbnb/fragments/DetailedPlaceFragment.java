package com.example.frontmynbnb.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.adapters.AvailabilitiesAdapter;
import com.example.frontmynbnb.adapters.BenefitsAdapter;
import com.example.frontmynbnb.adapters.RulesAdapter;
import com.example.frontmynbnb.dialogs.RatingDialog;
import com.example.frontmynbnb.models.Availability;
import com.example.frontmynbnb.models.Benefit;
import com.example.frontmynbnb.models.Image;
import com.example.frontmynbnb.models.Place;
import com.example.frontmynbnb.models.Rating;
import com.example.frontmynbnb.models.Rule;
import com.example.frontmynbnb.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.frontmynbnb.AppConstants.MAPVIEW_BUNDLE_KEY;


public class DetailedPlaceFragment extends MyFragment implements OnMapReadyCallback {
    private int placeId;
    private Place targetPlace = null;
    private User mOwner = null;
    private List<Bitmap> galleryBitmapList;

    private LinearLayout mProgressBarView, mRateView;
    private ImageView mGalleryView;
    private CircleImageView mOwnerImage;
    private TextView mTextBeds, mTextBaths, mTextBedrooms, mTextLivingRoom, mTextArea, mTextType,
            mTextDescription, mTextAddress, mTextReservation;
    private ListView mBenefitContainer, mRuleContainer, mAvailabilityContainer;

    private BenefitsAdapter mBenAdapter;
    private RulesAdapter mRulAdapter;
    private AvailabilitiesAdapter mAvAdapter;

    private MapView mMapView;
    private GoogleMap mGoogleMap;

    private Thread galleryThread;
    private String mFrom, mTo;
    private Button mButtonReserve, mButtonMessage, mButtonRate;

    public DetailedPlaceFragment() {
        // Required empty public constructor
    }


    public static DetailedPlaceFragment newInstance(String param1, String param2) {
        DetailedPlaceFragment fragment = new DetailedPlaceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_place, container, false);
        mProgressBarView = (LinearLayout) view.findViewById(R.id.progressBarPlace2);
        mRateView = (LinearLayout) view.findViewById(R.id.linearLayout_rateview);
        mGalleryView = (ImageView) view.findViewById(R.id.imageview_place_gallery);
        mTextBeds = (TextView) view.findViewById(R.id.textview_place_beds);
        mTextBaths = (TextView) view.findViewById(R.id.textview_place_baths);
        mTextBedrooms = (TextView) view.findViewById(R.id.textview_place_bedrooms);
        mTextLivingRoom = (TextView) view.findViewById(R.id.textview_place_livingroom);
        mTextArea = (TextView) view.findViewById(R.id.textview_place_area);
        mTextType = (TextView) view.findViewById(R.id.textview_place_type);
        mTextDescription = (TextView) view.findViewById(R.id.textview_place_description);
        mTextAddress = (TextView) view.findViewById(R.id.textview_place_address);
        mMapView = (MapView) view.findViewById(R.id.mapview3);
        mBenefitContainer = (ListView) view.findViewById(R.id.listview_benefitcontainer3);
        mRuleContainer = (ListView) view.findViewById(R.id.listview_rulescontainer3);
        mAvailabilityContainer = (ListView) view.findViewById(R.id.listview_availablecontainer3);
        mTextReservation = (TextView) view.findViewById(R.id.text_place_makereservation);
        mButtonReserve = (Button) view.findViewById(R.id.button_place_makereservation);
        mButtonReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReservation();
            }
        });
        mButtonRate = (Button) view.findViewById(R.id.button_rating_rate);
        mButtonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingDialog ratingDialog = RatingDialog.newInstance(
                        placeId,
                        targetPlace.getAddress()
                );
                ratingDialog.show(getFragmentManager(), "rate dialog");
            }
        });
        mButtonMessage = (Button) view.findViewById(R.id.button_place_sendmessage);
        mButtonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatFragment chatFragment = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("other_user_id", mOwner.getId());
                chatFragment.setArguments(bundle);
                galleryThread.interrupt();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                            R.id.fragment_container,
                            chatFragment
                    ).addToBackStack(null).commit();
            }
        });

        mOwnerImage = (CircleImageView) view.findViewById(R.id.imageview_place_ownerpic);
        mOwnerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("owner image clickeddd");
                Bundle bundle = new Bundle();
                bundle.putInt("place_id", targetPlace.getId());
                bundle.putInt("user_id", mOwner.getId());
                bundle.putString("from", mFrom);
                bundle.putString("to", mTo);
                DetailedOwnerFragment fragment = new DetailedOwnerFragment();
                fragment.setArguments(bundle);
                galleryThread.interrupt();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragment_container,
                        fragment
                ).commit();
            }
        });
        if (getArguments() != null) {
            placeId = getArguments().getInt("place_id");
            mFrom = getArguments().getString("from");
            mTo= getArguments().getString("to");
            String emptyDate = getResources().getString(R.string.empty_date);
            if(!mFrom.equals(emptyDate)  && !mTo.equals(emptyDate)){
                mTextReservation.setText("Reserve the place from: " + mFrom + " to " + mTo);
                mButtonReserve.setEnabled(true);
            } else {
                mTextReservation.setText("No dates have been provided");
                mTextReservation.setTextColor(getResources().getColor(R.color.colorRed));
            }
            fetchPlace();
            fetchOwner();
            checkIfCanRate();
        }

        initGoogleMap(savedInstanceState);
        return view;
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("detailed place fragment");
        if(galleryThread!=null)
            galleryThread.interrupt();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container,
                new HomeFragment()
        ).addToBackStack(null).commit();
        return true;
    }

    private void fetchPlace() {
        mProgressBarView.setVisibility(View.VISIBLE);
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Place> call = jsonPlaceHolderApi.getPlaceById(placeId);
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
                targetPlace = response.body();
                fetchImages();
                fetchOwner();
                setPlaceOnView();
                mProgressBarView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "No place fetched!",
                        Toast.LENGTH_SHORT
                ).show();
                mProgressBarView.setVisibility(View.INVISIBLE);
                System.out.println("Error message:: " + t.getMessage());
            }
        });
    }

    private void setPlaceOnView(){
        mTextBeds.setText(String.valueOf(targetPlace.getBeds()));
        mTextBaths.setText(String.valueOf(targetPlace.getBaths()));
        mTextBedrooms.setText(String.valueOf(targetPlace.getBedrooms()));
        mTextLivingRoom.setText(targetPlace.isLivingRoom() ? "Yes" : "No");
        mTextArea.setText(String.valueOf(targetPlace.getArea()));
        mTextType.setText(targetPlace.getType());
        mTextDescription.setText(targetPlace.getDescription());
        mTextAddress.setText(targetPlace.getAddress());

        //set ListViews
        mBenAdapter = new BenefitsAdapter(getActivity(), (ArrayList<Benefit>)targetPlace.getBenefits());
        mBenefitContainer.setAdapter(mBenAdapter);
        mRulAdapter = new RulesAdapter(getActivity(), (ArrayList<Rule>)targetPlace.getRules());
        mRuleContainer.setAdapter(mRulAdapter);
        for (Availability av : targetPlace.getAvailabilities()){
            av.setFrom(av.getFrom().split("T")[0]);
            av.setTo(av.getTo().split("T")[0]);
        }
        mAvAdapter = new AvailabilitiesAdapter(getActivity(), (ArrayList<Availability>)targetPlace.getAvailabilities());
        mAvailabilityContainer.setAdapter(mAvAdapter);
    }

    private void fetchImages(){
        //fetch images
        galleryBitmapList = new ArrayList<>();
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create((JsonPlaceHolderApi.class));
        Call<ResponseBody> call = jsonPlaceHolderApi.getPlaceMainImage(targetPlace.getId());
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
                galleryBitmapList.add(BitmapFactory.decodeByteArray(mainImageBytes, 0, mainImageBytes.length));
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
        for(Image img: targetPlace.getImages()) {
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
                    galleryBitmapList.add(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
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
        enableGalleryEffect();
    }

    private void enableGalleryEffect(){
        galleryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(true) {
                    for (final Bitmap b : galleryBitmapList) {
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mGalleryView.setImageBitmap(b);
                                }
                            });
                        }catch (NullPointerException e){
                            break;
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        galleryThread.start();
    }


    private void fetchOwner(){
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<User> call = jsonPlaceHolderApi.getPlaceOwner(placeId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + String.valueOf(response.code()),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                mOwner = response.body();
                fetchOwnerImage();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure on fetching owner",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
    }

    private void fetchOwnerImage(){
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<ResponseBody> call = jsonPlaceHolderApi.getUserImage(mOwner.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
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
                Bitmap ownerBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                mOwnerImage.setImageBitmap(ownerBitmap);
                System.out.println("Owner image set");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure on image call!!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
    }

    private void checkIfCanRate(){
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Boolean> call = jsonPlaceHolderApi.checkIfUserCanRate(placeId, AppConstants.USER.getId());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                if(response.body()){
                    mRateView.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "You can rate the Place", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure on image call!!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });

    }

    private void makeReservation(){
        Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Boolean> call = jsonPlaceHolderApi.createReservation(
                placeId, AppConstants.USER.getId(), mFrom, mTo);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                if(response.body()){
                    Toast.makeText(
                            getContext(),
                            "Reservation done with success",
                            Toast.LENGTH_LONG
                    ).show();
                    galleryThread.interrupt();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                            R.id.fragment_container,
                            new HomeFragment()
                    ).addToBackStack(null).commit();
                }
                else{
                    Toast.makeText(
                            getContext(),
                            "Reservation rejected!",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure on image call!!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });

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
        if(galleryThread!=null)
            galleryThread.interrupt();
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        if(targetPlace!=null) {
            LatLng latLng = new LatLng(
                    targetPlace.getLatitude(),
                    targetPlace.getLongitude()
            );
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Your Place"));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        }
    }

    @Override
    public void onPause() {
        if(galleryThread!=null)
            galleryThread.interrupt();
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
