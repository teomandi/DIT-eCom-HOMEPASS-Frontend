package com.example.frontmynbnb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class RegisterActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 1;

    EditText mTextUsername, mTextPassword, mTextCnfPassword, mTextName, mTextSurname, mTextEmail,
            mTextPhone;
    CheckBox mCheckHost;
    TextView mTextViewLogin;
    ImageView mUploadImage;
    Bitmap mCurrentBitmap;
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
                ),SELECT_IMAGE);
            }
        });
        mButtonRegister = (Button) findViewById(R.id.button_register);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate())
                    return;
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
        if( mTextUsername.getText().length() > 0 &&
                mTextEmail.getText().length() > 0 &&
                mTextPassword.getText().length() > 0 &&
                mTextCnfPassword.getText().length() > 0 &&
                mTextName.getText().length() > 0 &&
                mTextSurname.getText().length() > 0 &&
                mTextPhone.getText().length() > 0 ){
            Toast.makeText(this, "All the fields should be filled.", Toast.LENGTH_LONG).show();
            return false;
        }

        if( mTextCnfPassword.getText() != mTextPassword.getText() ){
            Toast.makeText(this, "Passwords not the same.", Toast.LENGTH_LONG).show();
            return false;
        }

        if( mCurrentBitmap == null){
            Toast.makeText(this, "No upload image found.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}
