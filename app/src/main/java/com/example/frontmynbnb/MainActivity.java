package com.example.frontmynbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.frontmynbnb.fragments.HomeFragment;
import com.example.frontmynbnb.fragments.HostFragment;
import com.example.frontmynbnb.fragments.MessagesFragment;
import com.example.frontmynbnb.fragments.ProfileFragment;
import com.example.frontmynbnb.fragments.MyFragment;
import com.example.frontmynbnb.models.Login;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static String mToken, mUsername;
    public static String getToken() {
        return mToken;
    }
    public static String getUsername() {
        return mUsername;
    }
    public static void setUsername(String username) {
        mUsername = username;
    }

    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUsername = extras.getString("username");
            mToken = extras.getString("token");
            System.out.println("Username and token collected");
        }

        bottomNav = findViewById(R.id.bottom_navigation);
        final FragmentManager fm = this.getSupportFragmentManager();
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
//                    case R.id.nav_home:
//                        selectedFragment = new HomeFragment();
//                        break;
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
}
