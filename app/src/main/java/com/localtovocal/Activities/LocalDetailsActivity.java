package com.localtovocal.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.login.Login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.localtovocal.Adapters.MyPostsAdapter;
import com.localtovocal.Model.MyPostsData;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.ShowUserPosts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocalDetailsActivity extends AppCompatActivity {

    List<MyPostsData> myPostsDataList;

    RecyclerView myPostRecycler;
    MyPostsAdapter adapter;
    ImageView back, Limage, nopost_Img;
    TextView tShopName, tName, tDiscretion, tLocation, tNumber, altNumber;

    private Animator currentAnimator;
    int shortAnimationDuration;
    Button btncall,btnWhatsApp;


    Retrofit retrofit;
    JsonInterface jsonInterface;
    ImageView ic_map;
    TextView addReview;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_details);

        myPostRecycler = findViewById(R.id.myPostRecycler);
        fab = findViewById(R.id.fab);


        back = findViewById(R.id.back);
        Limage = findViewById(R.id.Limage);
        btncall = findViewById(R.id.btncall);
        ic_map = findViewById(R.id.ic_map);

        tShopName = findViewById(R.id.tShopName);
        tName = findViewById(R.id.tName);
        addReview = findViewById(R.id.addReview);
        altNumber = findViewById(R.id.altNumber);

        tDiscretion = findViewById(R.id.tDiscretion);
        tLocation = findViewById(R.id.tLocation);
        tNumber = findViewById(R.id.tNumber);
        nopost_Img = findViewById(R.id.nopost_Img);
        btnWhatsApp = findViewById(R.id.btnWhatsApp);

        getData();


        back.setOnClickListener(view -> {
            finish();
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        myPostRecycler.setLayoutManager(staggeredGridLayoutManager);
        myPostRecycler.setItemViewCacheSize(20);
        myPostRecycler.setHasFixedSize(true);


        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);

        ic_map.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MapActivity.class)));

        fab.setOnClickListener(view -> {
            String userID = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
            if (userID.equals("")) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            } else {

                startActivity(new Intent(getApplicationContext(), ShowReviewsActivity.class));
            }

        });

        addReview.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ReviewActivity.class)));
        showPosts();


        btnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openWhatsApp();
                } catch (Exception e) {

                }


            }
        });


    }


    private void openWhatsApp() {
        String altMobile = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_NUMBER);
        Log.e("jhjhkj",altMobile);


        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {




            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("91"+altMobile) + "@s.whatsapp.net");//phone number without "+" prefix
            startActivity(sendIntent);


        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    public void getData() {

        final String userID = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        String image = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_IMAGE);
        String name = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_USERNAME);
        final String number = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_NUMBER);
        String Disretion = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_DISCRETION);
        String address = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_LOCATION);
        String shopN = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_SHOPNAME);
        String tag = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_TAG);
        String altMobile = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_ALT_MOBILE);

        tName.setText(name);

        if (number.equals("")) {
            tNumber.setText("Not Available");
        } else {
            tNumber.setText(number);
        }


        if (address.equals("")) {
            tLocation.setText("Not Available");
        } else {
            tLocation.setText(address);
        }

        if (altMobile.equals("")) {
            altNumber.setText("");
        } else {
            altNumber.setText(altMobile);
        }


        if (Disretion.equals("")) {
            tDiscretion.setText("Not Available");
        } else {
            tDiscretion.setText(Disretion);
        }

        tShopName.setText(shopN.toUpperCase());

        btncall.setOnClickListener(view -> {

            if (userID.equals("")) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }


        });

        Picasso.get().load(image).error(R.drawable.picture_na).into(Limage);

        Limage.setOnClickListener(view -> zoomImageFromThumb(Limage));
    }


    private void zoomImageFromThumb(final View thumbView) {

        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        String image = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_IMAGE);

        final ImageView expandedImageView = findViewById(
                R.id.expanded_image);
        // expandedImageView.setImageResource(R.drawable.share);

        Picasso.get().load(image).error(R.drawable.picture_na).into(expandedImageView);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();


        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;

        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {

            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }


        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);


        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);


        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;


        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }


                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }

    public void showPosts() {

        String lUserID = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_USER_ID);

        Log.e("sajdlk", lUserID);

        Map<String, String> params = new HashMap<>();
        params.put("control", "show_images");
        params.put("userID", lUserID);

        Call<ShowUserPosts> call = jsonInterface.showUserPosts(params);

        call.enqueue(new Callback<ShowUserPosts>() {
            @Override
            public void onResponse(@NonNull Call<ShowUserPosts> call, @NonNull Response<ShowUserPosts> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(LocalDetailsActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }

                myPostsDataList = new ArrayList<>();

                ShowUserPosts showUserPosts = response.body();

                if (showUserPosts.getResult()) {

                    List<ShowUserPosts.Datum> dataList = showUserPosts.getData();


                    for (ShowUserPosts.Datum d : dataList) {

                        MyPostsData myPostsData = new MyPostsData();

                        String id = d.getId();
                        String type = d.getType();
                        String name = d.getName();
                        String path = d.getPath();
                        String add_status = d.getAdd_status();
                        String description = d.getDescription();

                        myPostsData.setId(id);
                        myPostsData.setType(type);
                        myPostsData.setFilename(name);
                        myPostsData.setPath(path);
                        myPostsData.setAdd_status(add_status);
                        myPostsData.setDescription(description);

                        myPostsDataList.add(myPostsData);


                        Log.e("xsjhxkjs", type + "," + name + "," + path);
                    }

                    adapter = new MyPostsAdapter(myPostsDataList, LocalDetailsActivity.this);
                    myPostRecycler.setAdapter(adapter);
                } else {
                    nopost_Img.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShowUserPosts> call, @NonNull Throwable t) {
                Log.e("xsjhxkjs", t.getMessage(), t);
            }
        });

    }

}