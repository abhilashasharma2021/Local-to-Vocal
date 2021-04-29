package com.localtovocal.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.localtovocal.Fragments.HomeFragment;
import com.localtovocal.Fragments.MyProfileFragment;
import com.localtovocal.Fragments.SettingsFragment;
import com.localtovocal.Fragments.SubscribeFragment;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;

public class BottomNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);


        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        String result = SharedHelper.getKey(getApplicationContext(), AppConstats.RESULT_UPLOAD);

        Log.e("ksdlkjsld", result);

        if (result.equals("success")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyProfileFragment()).commit();
            bottomNav.getMenu().getItem(2).setChecked(true);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_profile:
                String userID = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);

                if (userID.equals("")) {

                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    Animatoo.animateShrink(BottomNavigationActivity.this);

                } else {
                    SharedHelper.putKey(getApplicationContext(), AppConstats.LOCALS_CLICK_FOR_DELETE, "VISIBLE");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyProfileFragment()).commit();

                }
                break;


            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

                break;


            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;


            case R.id.nav_subscription:
                String userIDs = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
                Log.e("ckmcsc", userIDs);
                if (userIDs.equals("")) {
                    Toast.makeText(this, "Please login to proceed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    Animatoo.animateShrink(BottomNavigationActivity.this);
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SubscribeFragment()).commit();

                }


                break;
        }
        return true;
    }
}