package com.example.frontmynbnb.fragments;

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

import de.hdodenhof.circleimageview.CircleImageView;
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
        mTextHost = (TextView) view.findViewById(R.id.textview_host);
        mCircleImage = (CircleImageView) view.findViewById(R.id.imageview_profilepic);
        mTableView = (TableLayout) view.findViewById(R.id.tableview_infotable);

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
                            "Not successful response " + String.valueOf(response.code()),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                System.out.println("Status Code : " + response.code());
                mUser = response.body();
                assert mUser != null;
                mUser.printDetails();
                // set the view
                mProgressBar.setVisibility(View.INVISIBLE);

                mTextFirstName.setText(mUser.getFirstName());
                mTextFirstName.setVisibility(View.VISIBLE);
                mTextLastName.setText(mUser.getLastName());
                mTextLastName.setVisibility(View.VISIBLE);
                mTextUsername.setText(mUser.getUsername());
                mTextUsername.setVisibility(View.VISIBLE);
                //the table
                mTextEmail.setText(mUser.getEmail());
                mTextPhone.setText(mUser.getPhone());
                mTextAddress.setText(mUser.getAddress());
                mTableView.setVisibility(View.VISIBLE);

                String hostMessage = mUser.isHost() ? "Already a host" : "Click here to become a host";
                mTextHost.setText(hostMessage);
                mTextHost.setVisibility(View.VISIBLE);

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
}
