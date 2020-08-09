package com.example.frontmynbnb.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.MainActivity;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.models.User;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileFragment extends Fragment {

    private ProgressBar mProgressBar;
    private TextView mTextFirstName, mTextLastName, mTextUsername, mTextEmail, mTextPhone,
            mTextAddress, mTextHost;
    private CircleImageView mCircleImage;
    private TableLayout mTableView;

    private User mUser = null;
    private Bitmap mUserImage = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mTextFirstName = (TextView) view.findViewById(R.id.textview_name);
        mTextLastName = (TextView) view.findViewById(R.id.textview_surname);
        mTextUsername = (TextView) view.findViewById(R.id.textview_username);
        mTextEmail = (TextView) view.findViewById(R.id.textview_email);
        mTextPhone = (TextView) view.findViewById(R.id.textview_phone);
        mTextAddress = (TextView) view.findViewById(R.id.textview_address);
        mCircleImage = (CircleImageView) view.findViewById(R.id.imageview_profilepic);
        mTableView = (TableLayout) view.findViewById(R.id.tableview_infotable);
        mTextHost = (TextView) view.findViewById(R.id.textview_host);
        mTextHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mUser.isHost() ){
                    Toast.makeText(
                            getContext(),
                            "You are already a host",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                Retrofit retrofit = RestClient.getClient(MainActivity.getToken());
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<User> call = jsonPlaceHolderApi.switchUserHost(mUser.getId());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if( !response.isSuccessful() ) {
                            Toast.makeText(
                                    getContext(),
                                    "Not successful response " + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }
                        mUser = response.body();
                        assert mUser != null;
                        setUserDetails();
                        Toast.makeText(
                                getContext(),
                                "Woooop! You became a host",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(
                                getContext(),
                                "Failure on switching to host!",
                                Toast.LENGTH_LONG
                        ).show();
                        System.out.println("Error message:: " + t.getMessage());
                    }
                });
            }
        });
        String token = MainActivity.getToken();
        String username = MainActivity.getUsername();
        Retrofit retrofit = RestClient.getClient(token);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<User> call = jsonPlaceHolderApi.getUserByUsername(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if( !response.isSuccessful() ) {
                    Toast.makeText(
                            getContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                System.out.println("Status Code : " + response.code());
                mUser = response.body();
                assert mUser != null;
                setUserDetails();
                fetchUserImage();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Failure!!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
        return view;
    }

    private void setUserDetails(){
        mUser.printDetails();
        mTextFirstName.setText(mUser.getFirstName());
        mTextFirstName.setVisibility(View.VISIBLE);
        mTextLastName.setText(mUser.getLastName());
        mTextLastName.setVisibility(View.VISIBLE);
        mTextUsername.setText(mUser.getUsername());
        mTextUsername.setVisibility(View.VISIBLE);
        mTextEmail.setText(mUser.getEmail());
        mTextPhone.setText(mUser.getPhone());
        mTextAddress.setText(mUser.getAddress());
        mTableView.setVisibility(View.VISIBLE);
        String hostMessage = mUser.isHost() ? "Already a host" : "Click here to become a host";
        mTextHost.setText(hostMessage);
        mTextHost.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void fetchUserImage(){
        String token = MainActivity.getToken();
        Retrofit retrofit = RestClient.getClient(token);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<ResponseBody> call = jsonPlaceHolderApi.getUserImage(mUser.getId());
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
                mUserImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                mCircleImage.setImageBitmap(mUserImage);
                System.out.println("User image set");
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
