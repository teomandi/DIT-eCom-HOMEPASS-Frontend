package com.example.frontmynbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.frontmynbnb.fragments.HomeFragment;
import com.example.frontmynbnb.fragments.HostFragment;
import com.example.frontmynbnb.fragments.MessagesFragment;
import com.example.frontmynbnb.fragments.ProfileFragment;
import com.example.frontmynbnb.models.Message;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        final FragmentManager fm = this.getSupportFragmentManager();


        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_messages:
                        selectedFragment = new MessagesFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                    case R.id.nav_switch:
                        selectedFragment = new HostFragment();
                        break;
                }
                assert selectedFragment != null;
                fm.beginTransaction().replace(
                        R.id.fragment_container,
                        selectedFragment
                ).commit();
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container,
                new HomeFragment()
        ).commit();
    }
}
