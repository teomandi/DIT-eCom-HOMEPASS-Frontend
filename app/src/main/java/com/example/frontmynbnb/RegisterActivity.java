package com.example.frontmynbnb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontmynbnb.misc.Utils;
import com.example.frontmynbnb.models.User;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegisterActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 1;

    EditText mTextUsername, mTextPassword, mTextCnfPassword, mTextName, mTextSurname, mTextEmail,
            mTextPhone, mTextAddress;
    CheckBox mCheckHost;
    TextView mTextViewLogin;
    ImageView mUploadImage;
    Bitmap mCurrentBitmap;
    Uri mBitmapUri = null;

    Button mButtonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mTextUsername = (EditText) findViewById(R.id.edittext_username);
        mTextEmail = (EditText) findViewById(R.id.edittext_email);
        mTextName = (EditText) findViewById(R.id.edittext_firstname);
        mTextSurname = (EditText) findViewById(R.id.edittext_lastname);
        mTextPhone = (EditText) findViewById(R.id.edittext_phone);
        mTextPassword = (EditText) findViewById(R.id.edittext_password);
        mTextCnfPassword = (EditText) findViewById(R.id.edittext_cnf_password);
        mTextAddress = (EditText) findViewById(R.id.edittext_address);
        mCheckHost = (CheckBox) findViewById(R.id.checkbox_behost);
        mUploadImage = (ImageView) findViewById(R.id.imageview_uploadpic);
        mUploadImage.setOnClickListener(new View.OnClickListener() {
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
        mButtonRegister = (Button) findViewById(R.id.button_register);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate())
                    return;
                RequestBody usernamePart = RequestBody.create(
                        MultipartBody.FORM,
                        mTextUsername.getText().toString()
                );
                RequestBody emailPart = RequestBody.create(
                        MultipartBody.FORM,
                        mTextEmail.getText().toString()
                );
                RequestBody passwordPart = RequestBody.create(
                        MultipartBody.FORM,
                        mTextPassword.getText().toString()
                );
                RequestBody firstNamePart = RequestBody.create(
                        MultipartBody.FORM,
                        mTextName.getText().toString()
                );
                RequestBody lastNamePart = RequestBody.create(
                        MultipartBody.FORM,
                        mTextSurname.getText().toString()
                );
                RequestBody phonePart = RequestBody.create(
                        MultipartBody.FORM,
                        mTextPhone.getText().toString()
                );
                RequestBody addressPart = RequestBody.create(
                        MultipartBody.FORM,
                        mTextAddress.getText().toString()
                );
                RequestBody hostPart = RequestBody.create(
                        MultipartBody.FORM,
                        String.valueOf(mCheckHost.isChecked())
                );
                //image part
                MultipartBody.Part imageFilePart = null;
                if (mBitmapUri != null) {
                    byte[] img = null;
                    try {
                        InputStream iStream = getContentResolver().openInputStream(mBitmapUri);
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
                Retrofit retrofit = RestClient.getClient(null);
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<User> call = jsonPlaceHolderApi.register(
                        usernamePart, emailPart, passwordPart, firstNamePart, lastNamePart,
                        phonePart, hostPart, addressPart, imageFilePart
                );
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        System.out.println("Register user status ~~> " + response.code());
                        if( response.code() != 200 ){
                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Creating user failed",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }
                        User retUser = response.body();
                        Toast.makeText(
                                RegisterActivity.this,
                                "User " + retUser.getUsername() + " successfully registered",
                                Toast.LENGTH_SHORT
                        ).show();
                        Intent loginIntent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class
                        );
                        loginIntent.putExtra("username", retUser.getUsername());
                        startActivity(loginIntent);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println("Error message:: " + t.getMessage());
                        Toast.makeText(
                                RegisterActivity.this,
                                "Failure!!",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });



            }
        });
        mTextViewLogin = (TextView) findViewById(R.id.textview_login);
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(
                        RegisterActivity.this,
                        LoginActivity.class
                );
                startActivity(registerIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("on-resume!!!");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        mBitmapUri = data.getData();
                        mCurrentBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        mUploadImage.setImageBitmap((mCurrentBitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean validate(){
        if( mTextUsername.getText().length() == 0 ||
                mTextEmail.getText().length() == 0 ||
                mTextPassword.getText().length() == 0 ||
                mTextCnfPassword.getText().length() == 0 ||
                mTextName.getText().length() == 0 ||
                mTextSurname.getText().length() == 0 ||
                mTextPhone.getText().length() == 0 ){
            Toast.makeText(this, "All the fields should be filled.", Toast.LENGTH_LONG).show();
            return false;
        }

        System.out.println(mTextCnfPassword.getText().toString() + "=?=" + mTextPassword.getText().toString());
        if( !mTextCnfPassword.getText().toString().equals(mTextPassword.getText().toString()) ){
            Toast.makeText(this, "Passwords not the same.", Toast.LENGTH_LONG).show();
            return false;
        }

//        if( mCurrentBitmap == null){
//            Toast.makeText(this, "Please select a photo to upload.", Toast.LENGTH_LONG).show();
//            return false;
//        }

        return true;
    }

}
