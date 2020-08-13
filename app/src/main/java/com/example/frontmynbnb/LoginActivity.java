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

import com.example.frontmynbnb.models.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                String username = mTextUsername.getText().toString();
                String password = mTextPassword.getText().toString();
                if ( username.length() == 0 || password.length() == 0 ) {
                    Toast.makeText(
                            LoginActivity.this,
                            "Please, fulfill both of the fields.",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                Login login = new Login(username, password);
                Retrofit retrofit = RestClient.getStringClient();
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<String> call = jsonPlaceHolderApi.login(login);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        System.out.println("We have a response!" + response.code());
                        if(response.code() != 200){
                            Toast.makeText(
                                    LoginActivity.this,
                                    "Authorization failed",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }
                        Toast.makeText(
                                LoginActivity.this,
                                "Authorization succeeded",
                                Toast.LENGTH_SHORT
                        ).show();

                        String userToken = response.headers().get("Authorization");
                        Intent mainIntent = new Intent(
                                LoginActivity.this,
                                MainActivity.class
                        );
                        mainIntent.putExtra("username", mTextUsername.getText().toString());
                        mainIntent.putExtra("token", userToken);
                        System.out.println("~~~~> " + userToken);
                        startActivity(mainIntent);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(
                                LoginActivity.this,
                                "Failure!!",
                                Toast.LENGTH_LONG
                        ).show();
                        System.out.println("Error message:: " + t.getMessage());
                    }
                });


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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("username");
            mTextUsername.setText(value);
        }

    }
}
