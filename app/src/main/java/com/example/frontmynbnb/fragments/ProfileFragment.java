package com.example.frontmynbnb.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.activities.HostActivity;
import com.example.frontmynbnb.activities.MainActivity;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.misc.Utils;
import com.example.frontmynbnb.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileFragment extends MyFragment {

    private static final int SELECT_IMAGE = 1;

    private ProgressBar mProgressBar;
    private TextView mTextFirstName, mTextLastName, mTextUsername, mTextEmail, mTextPhone,
            mTextAddress, mTextHost;
    private CircleImageView mCircleImage, mCircleImageEdit;
    private TableLayout mTableView;
    private Button mButtonEdit, mButtonSave, mButtonCancel;
    private LinearLayout infoLinearLayout;
    private ScrollView scrollView2;
    private EditText mEditUsername, mEditFirstName, mEditLastName, mEditEmail, mEditPhone, mEditAddress;

    private User mUser = null;
    private Bitmap mUserImage = null, mNewUserImage;
    private Uri mBitmapUri = null;

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
        mCircleImageEdit = (CircleImageView) view.findViewById(R.id.imageview_editprofilepic);
        scrollView2 = (ScrollView) view.findViewById(R.id.scrollView2);
        mCircleImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(
                        intent,
                        "Select Picture"
                ), SELECT_IMAGE);
            }
        });
        mTableView = (TableLayout) view.findViewById(R.id.tableview_infotable);
        infoLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout1);
        mEditUsername = (EditText) view.findViewById(R.id.editview_username2);
        mEditFirstName = (EditText) view.findViewById(R.id.editview_firstname2);
        mEditLastName = (EditText) view.findViewById(R.id.editview_lastname2);
        mEditEmail = (EditText) view.findViewById(R.id.editview_email2);
        mEditPhone = (EditText) view.findViewById(R.id.editview_phone2);
        mEditAddress = (EditText) view.findViewById(R.id.editview_address2);
        mButtonSave = (Button) view.findViewById(R.id.button_save);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                if( !validate() )
                    return;
                RequestBody usernamePart = RequestBody.create(
                        MultipartBody.FORM,
                        mEditUsername.getText().toString()
                );
                RequestBody firstNamePart = RequestBody.create(
                        MultipartBody.FORM,
                        mEditFirstName.getText().toString()
                );
                RequestBody lastNamePart = RequestBody.create(
                        MultipartBody.FORM,
                        mEditLastName.getText().toString()
                );
                RequestBody emailPart = RequestBody.create(
                        MultipartBody.FORM,
                        mEditEmail.getText().toString()
                );
                RequestBody phonePart = RequestBody.create(
                        MultipartBody.FORM,
                        mEditPhone.getText().toString()
                );
                RequestBody addressPart = RequestBody.create(
                        MultipartBody.FORM,
                        mEditAddress.getText().toString()
                );
                // password and host parts will be ignored in the backend
                RequestBody passwordPart = RequestBody.create(
                        MultipartBody.FORM,
                        ""
                );
                RequestBody hostPart = RequestBody.create(
                        MultipartBody.FORM,
                        "false"
                );
                //image part
                MultipartBody.Part imageFilePart = null;
                if (mBitmapUri != null) {
                    byte[] img = null;
                    try {
                        InputStream iStream = getContext().getContentResolver().openInputStream(mBitmapUri);
                        img = Utils.getBytes(iStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("uri ~~> " + mBitmapUri + "   " + img.length);
                    RequestBody imageFile = RequestBody.create(MediaType.parse("image/jpeg"), img);
                    imageFilePart = MultipartBody.Part.createFormData(
                            "picture",
                            mBitmapUri.getLastPathSegment(),
                            imageFile
                    );
                    System.out.println("filepart initialized");
                }
                else {
                    RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
                    imageFilePart = MultipartBody.Part.createFormData("picture", "", attachmentEmpty);
                }
                Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<User> call = jsonPlaceHolderApi.editUser(
                        mUser.getId(), usernamePart, emailPart, passwordPart, firstNamePart,
                        lastNamePart, phonePart, hostPart, addressPart, imageFilePart);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if( !response.isSuccessful() ) {
                            Toast.makeText(
                                    getContext(),
                                    "Not successful response " + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }
                        System.out.println("Status Code : " + response.code());
                        scrollView2.setVisibility(View.INVISIBLE);
                        mButtonEdit.setVisibility(View.VISIBLE);
                        infoLinearLayout.setVisibility(View.VISIBLE);
                        mUser = response.body();
                        AppConstants.USER = mUser;
                        setUserDetails();
                        fetchUserImage();
                        Toast.makeText(
                                getContext(),
                                "Info edited with success " + response.code(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(
                                getContext(),
                                "Failure on putting user!",
                                Toast.LENGTH_LONG
                        ).show();
                        System.out.println("Error message:: " + t.getMessage());
                    }
                });
                //update the username
                AppConstants.USERNAME = mUser.getUsername();

            }
        });
        mButtonCancel = (Button) view.findViewById(R.id.button_cancel);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        getContext(),
                        "Edit Canceled",
                        Toast.LENGTH_SHORT
                ).show();
                scrollView2.setVisibility(View.INVISIBLE);
                mButtonEdit.setVisibility(View.VISIBLE);
                infoLinearLayout.setVisibility(View.VISIBLE);
            }
        });
        mButtonEdit = (Button) view.findViewById(R.id.button_edit);
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        getContext(),
                        "Edit mode activated",
                        Toast.LENGTH_SHORT
                ).show();
                infoLinearLayout.setVisibility(View.INVISIBLE);
                mButtonEdit.setVisibility(View.INVISIBLE);
                scrollView2.setVisibility(View.VISIBLE);
                mCircleImageEdit.setImageBitmap(mUserImage);
                // set the values
                mEditUsername.setText(mTextUsername.getText());
                mEditFirstName.setText(mTextFirstName.getText());
                mEditLastName.setText(mTextLastName.getText());
                mEditEmail.setText(mTextEmail.getText());
                mEditPhone.setText(mTextPhone.getText());
                mEditAddress.setText(mTextAddress.getText());
            }
        });
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
                mProgressBar.setVisibility(View.VISIBLE);
                Retrofit retrofit = RestClient.getClient(AppConstants.TOKEN);
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<User> call = jsonPlaceHolderApi.switchUserHost(mUser.getId());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if( !response.isSuccessful() ) {
                            Toast.makeText(
                                    getContext(),
                                    "Not successful response " + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }
                        mUser = response.body();
                        AppConstants.USER = mUser;
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
                        mProgressBar.setVisibility(View.INVISIBLE);
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
        String token = AppConstants.TOKEN;
        String username = AppConstants.USERNAME;
        Retrofit retrofit = RestClient.getClient(token);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<User> call = jsonPlaceHolderApi.getUserByUsername(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
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
                AppConstants.USER = mUser;
                assert mUser != null;
                setUserDetails();
                fetchUserImage();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(
                        getContext(),
                        "Failure!!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
            }
        });
        if(AppConstants.MODE.equals("GUEST"))
            MainActivity.setBottomNavChecked(1);
        else
            HostActivity.setBottomNavChecked(1);
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
    }

    private void fetchUserImage(){
        String token = AppConstants.TOKEN;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        mBitmapUri = data.getData();
                        mNewUserImage = MediaStore.Images.Media.getBitmap(
                                getContext().getContentResolver(),
                                data.getData()
                        );
                        mCircleImageEdit.setImageBitmap((mNewUserImage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validate() {
        if (mEditUsername.getText().length() == 0 ||
                mEditEmail.getText().length() == 0 ||
                mEditFirstName.getText().length() == 0 ||
                mEditLastName.getText().length() == 0 ||
                mEditPhone.getText().length() == 0 ||
                mEditAddress.getText().length() == 0) {
            Toast.makeText(getContext(), "All the fields should be filled.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("PROFILE ~~" + AppConstants.MODE);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                AppConstants.MODE.equals("GUEST") ? R.id.fragment_container : R.id.fragment_container2,
                AppConstants.MODE.equals("GUEST") ? new HomeFragment() : new PlaceFragment()
        ).addToBackStack(null).commit();
        return true;
    }
}
