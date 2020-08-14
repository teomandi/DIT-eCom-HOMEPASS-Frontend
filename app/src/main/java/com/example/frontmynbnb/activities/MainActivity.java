package com.example.frontmynbnb.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.JsonPlaceHolderApi;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.RestClient;
import com.example.frontmynbnb.fragments.HomeFragment;
import com.example.frontmynbnb.fragments.MessagesFragment;
import com.example.frontmynbnb.fragments.ProfileFragment;
import com.example.frontmynbnb.fragments.MyFragment;
import com.example.frontmynbnb.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private static BottomNavigationView bottomNav;
    private boolean fetched = false;

    public static void setBottomNavChecked(int id){
        bottomNav.getMenu().getItem(id).setCheckable(true);
        bottomNav.getMenu().getItem(id).setChecked(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottom_navigation);
        final FragmentManager fm = this.getSupportFragmentManager();
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_messages:
                        selectedFragment = new MessagesFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                    case R.id.nav_switch:
                        if ( !fetched ) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Don't rush!",
                                    Toast.LENGTH_SHORT
                            ).show();
                            fetchUserDetails();
                            return true;
                        }
                        if ( !AppConstants.USER.isHost() ){
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Sorry, you haven't activate the host role!",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return true;
                        }
                        //check if it can change
                        Intent hostIntent = new Intent(
                                MainActivity.this,
                                HostActivity.class
                        );
                        AppConstants.MODE = "HOST";
                        startActivity(hostIntent);
                        return true;
                }
                assert selectedFragment != null;
                fm.beginTransaction().replace(
                        R.id.fragment_container,
                        selectedFragment
                ).commit();



                return true;
            }
        });
        AppConstants.MODE = "GUEST";
        System.out.println("uname1: ~~~~~~~~~~~~~~~~~~~~~~> " + AppConstants.USERNAME);
        // put home
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container,
                new HomeFragment()
        ).commit();
        //uncheck them all
        int size = bottomNav.getMenu().size();
        for (int i = 0; i < size; i++) {
            bottomNav.getMenu().getItem(i).setCheckable(false);
        }
        //fetch user
        fetchUserDetails();
    }


    // handle back
    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = this.getSupportFragmentManager().getFragments();
        boolean handled = false;
        for(Fragment f : fragmentList) {
            if(f instanceof MyFragment) {
                handled = ((MyFragment)f).onBackPressed();
                int size = bottomNav.getMenu().size();
                for (int i = 0; i < size; i++) {
                    bottomNav.getMenu().getItem(i).setCheckable(false);
                }
                if(handled) {
                    break;
                }
            }
        }
        if(!handled) {
            Intent loginIntent = new Intent(
                    MainActivity.this,
                    LoginActivity.class
            );
            startActivity(loginIntent);
        }
    }

    private void fetchUserDetails() {
        String token = AppConstants.TOKEN;
        String username = AppConstants.USERNAME;
        Retrofit retrofit = RestClient.getClient(token);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<User> call = jsonPlaceHolderApi.getUserByUsername(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Not successful response " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                System.out.println("Status Code : " + response.code());
                AppConstants.USER = response.body();
                fetched = true;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(
                        getApplicationContext(),
                        "Failure fetching the user!!",
                        Toast.LENGTH_LONG
                ).show();
                System.out.println("Error message:: " + t.getMessage());
                fetched = false;
            }
        });
    }
}
