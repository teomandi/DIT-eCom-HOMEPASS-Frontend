package com.example.frontmynbnb;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Retrofit;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontmynbnb.models.Message;
import com.example.frontmynbnb.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText mTextUsername;
    EditText mTextPassword;
    Button mButtonLogin;
    TextView mTextViewRegister;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTextUsername = (EditText) findViewById(R.id.edittext_username);
        mTextPassword = (EditText) findViewById(R.id.edittext_password);
        mButtonLogin = (Button) findViewById(R.id.button_login);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(
                        LoginActivity.this,
                        MainActivity.class
                );
                startActivity(mainIntent);
            }
        });
        mTextViewRegister = (TextView) findViewById(R.id.textview_register);
        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(
                        LoginActivity.this,
                        RegisterActivity.class
                );
                startActivity(registerIntent);
            }
        });

        Button rettest = (Button) findViewById(R.id.retrofit_test);
        rettest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("RETROFIT TEST starts");
                String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZW9tYW5kaTQiLCJleHAiOjE1OTc1OTY5MDV9.MS4OMS2oyb0RSZnGRH2_CSup4ffmN3OO7STQ31FQU2ugOSRGxpWdM8-JS6osxQPsQTxmFuQ8xkBfePHzDNg8hw"
                Retrofit retrofit = RestClient.getClient(token);
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<List<Message>> call = jsonPlaceHolderApi.getAllMessages();
                call.enqueue(new Callback<List<Message>>() {
                    @Override
                    public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                        List<Message> messages = response.body();
                        for (Message msg: messages){
                            System.out.println(msg.getBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Message>> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });

            }
        });




    }
}
