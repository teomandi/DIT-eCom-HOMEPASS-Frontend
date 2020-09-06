package com.example.frontmynbnb.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.activities.MainActivity;
import com.example.frontmynbnb.adapters.RatingAdapter;
import com.example.frontmynbnb.models.Place;
import com.example.frontmynbnb.models.Rating;
import com.example.frontmynbnb.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailedOwnerFragment extends MyFragment {

    private ProgressBar mProgressBar;
    private CircleImageView mOwnerImageView;
    private TextView mTextName, mTextSurname, mTextUsername, mTextEmail, mTextPhone, mTextAddress;
    private ListView mRatingContainer;
    private List<Rating> mRatingList;
    private String mTo, mFrom;

    public DetailedOwnerFragment() {
        // Required empty public constructor
    }

    public static DetailedOwnerFragment newInstance(String param1, String param2) {
        DetailedOwnerFragment fragment = new DetailedOwnerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private int placeId, userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_owner, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarOwner);
        mOwnerImageView = (CircleImageView) view.findViewById(R.id.imageview_owner_pic);
        mTextName = (TextView) view.findViewById(R.id.textview_owner_name);
        mTextSurname = (TextView) view.findViewById(R.id.textview_owner_surname);
        mTextUsername = (TextView) view.findViewById(R.id.textview_owner_username);
        mTextEmail = (TextView) view.findViewById(R.id.textview_owner_email);
        mTextPhone = (TextView) view.findViewById(R.id.textview_owner_phone);
        mTextAddress = (TextView) view.findViewById(R.id.textview_owner_address);
        mRatingContainer = (ListView) view.findViewById(R.id.container_owner_ratings);

        if (getArguments() != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            placeId = getArguments().getInt("place_id");
            userId = getArguments().getInt("user_id");
            mFrom = getArguments().getString("from");
            mTo= getArguments().getString("to");
            System.out.println("Place: " + placeId + " user: " + userId);
            fetchOwner();
            fetchOwnerImage();
            fetchPlaceRatings();
        }
        return view;
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("detailed owner fragment");

        Bundle bundle = new Bundle();
        bundle.putString("from", mFrom);
        bundle.putString("to", mTo);
        bundle.putInt("place_id", placeId);
        DetailedPlaceFragment fragment = new DetailedPlaceFragment();
        fragment.setArguments(bundle);

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container,
                fragment
        ).addToBackStack(null).commit();
        return true;
    }

    private void fetchPlaceRatings() {
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
                mRatingList = new ArrayList<>();
                mRatingList.addAll(response.body().getRatings());
                for (Rating r: mRatingList)
                    System.out.println(r.getComment());
                RatingAdapter ratingAdapter = new RatingAdapter(getActivity(), mRatingList);
                mRatingContainer.setAdapter(ratingAdapter);
                System.out.println("ratings set " + mRatingList.size());

            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "No ratings fetched!",
                        Toast.LENGTH_SHORT
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
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
                User owner = response.body();
                mTextName.setText(owner.getFirstName());
                mTextSurname.setText(owner.getLastName());
                mTextUsername.setText(owner.getUsername());
                mTextEmail.setText(owner.getEmail());
                mTextPhone.setText(owner.getPhone());
                mTextAddress.setText(owner.getAddress());
                System.out.println("user info set");

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
        Call<ResponseBody> call = jsonPlaceHolderApi.getUserImage(userId);
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
                byte[] imageBytes = new byte[0];
                try {
                    imageBytes = response.body().bytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert imageBytes != null;
                Bitmap ownerBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                mOwnerImageView.setImageBitmap(ownerBitmap);
                System.out.println("Owner image set");
                mProgressBar.setVisibility(View.INVISIBLE);
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
}
