package com.example.frontmynbnb.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.fragments.HomeFragment;
import com.example.frontmynbnb.fragments.HostFragment;
import com.example.frontmynbnb.fragments.MessagesFragment;
import com.example.frontmynbnb.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HostActivity extends AppCompatActivity {

    private static BottomNavigationView bottomNav;
    public static void setBottomNavChecked(int id){
        bottomNav.getMenu().getItem(id).setCheckable(true);
        bottomNav.getMenu().getItem(id).setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        bottomNav = findViewById(R.id.bottom_navigation2);
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
                        Intent hostIntent = new Intent(
                                HostActivity.this,
                                MainActivity.class
                        );
                        AppConstants.MODE = "GUEST";
                        startActivity(hostIntent);
                        return true;
                }
                assert selectedFragment != null;
                fm.beginTransaction().replace(
                        R.id.fragment_container2,
                        selectedFragment
                ).commit();
                return true;
            }
        });
        // put host
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container2,
                new HostFragment()
        ).commit();
        //uncheck them all
        int size = bottomNav.getMenu().size();
        for (int i = 0; i < size; i++) {
            bottomNav.getMenu().getItem(i).setCheckable(false);
        }
    }
}
