package com.example.frontmynbnb.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.frontmynbnb.AppConstants;
import com.example.frontmynbnb.R;
import com.example.frontmynbnb.fragments.CreateHostFragment;
import com.example.frontmynbnb.fragments.MessagesFragment;
import com.example.frontmynbnb.fragments.MyFragment;
import com.example.frontmynbnb.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

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
                new CreateHostFragment()
        ).commit();
        //uncheck them all
        int size = bottomNav.getMenu().size();
        for (int i = 0; i < size; i++) {
            bottomNav.getMenu().getItem(i).setCheckable(false);
        }
        AppConstants.MODE = "HOST";
    }

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
                    HostActivity.this,
                    LoginActivity.class
            );
            startActivity(loginIntent);
        }
    }
}
