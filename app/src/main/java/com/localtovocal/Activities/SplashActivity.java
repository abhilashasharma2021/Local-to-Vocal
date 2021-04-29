package com.localtovocal.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class SplashActivity extends AppCompatActivity {


    String userID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        userID = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);

        printHashKey(this);


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {


            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        SharedHelper.putKey(getApplicationContext(),AppConstats.RESULT_UPLOAD,"");
                        startActivity(new Intent(SplashActivity.this, BottomNavigationActivity.class));
                        Animatoo.animateShrink(SplashActivity.this);
                        finish();


                    }
                }, 1000);


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();


    }


    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("eruytur", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (Exception e) {
            Log.e("eruytur", "printHashKey()", e);
        }
    }


}