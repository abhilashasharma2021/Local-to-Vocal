package com.localtovocal.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.localtovocal.Activities.UploadFileActivity;
import com.localtovocal.Adapters.MyPostsAdapter;
import com.localtovocal.BuildConfig;
import com.localtovocal.Model.MyPostsData;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.GPSUtils;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.DeletePost;
import com.localtovocal.RetrofitModels.ShowUserPosts;
import com.sdsmdg.tastytoast.TastyToast;
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

import static com.facebook.FacebookSdk.getApplicationContext;


public class MyProfileFragment extends Fragment {

    List<MyPostsData> myPostsDataList;
    RecyclerView myPostRecycler;
    MyPostsAdapter adapter;
    CardView cardLocation, cardDis;
    LinearLayout linearShop;

    TextView mNumber, mEmail, mShopName, mUsername, mDiscretion, mlocationtxt;

    ImageView prf, nopost_Img;

    FloatingActionButton btnFab;

    boolean isPermissionAccepted;

    public static final int REQUEST_CODE = 1001;

    ImageView share, editPrf;

    public static Retrofit retrofit;
    public static JsonInterface jsonInterface;
    RelativeLayout noConnection;
    private boolean isGPS = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);


        myPostRecycler = view.findViewById(R.id.myPostRecycler);
        mNumber = view.findViewById(R.id.number);
        mEmail = view.findViewById(R.id.email);
        mShopName = view.findViewById(R.id.shopName);
        mUsername = view.findViewById(R.id.username);
        mlocationtxt = view.findViewById(R.id.locationtxt);
        mDiscretion = view.findViewById(R.id.discretion);

        cardLocation = view.findViewById(R.id.cardLocation);
        cardDis = view.findViewById(R.id.cardDis);
        linearShop = view.findViewById(R.id.linearShop);

        prf = view.findViewById(R.id.prf);
        share = view.findViewById(R.id.share);
        editPrf = view.findViewById(R.id.editPrf);
        btnFab = view.findViewById(R.id.btnFab);
        nopost_Img = view.findViewById(R.id.nopost_Img);
        noConnection = view.findViewById(R.id.noConnection);

        editPrf.setOnClickListener(view12 -> {
            EditProfileFragment.MY_TAGS = "";
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditProfileFragment()).commit();
        });

        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UploadFileActivity.class));
                getActivity().finish();
            }
        });

        share.setOnClickListener(view1 -> {

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        myPostRecycler.setLayoutManager(staggeredGridLayoutManager);
        myPostRecycler.setItemViewCacheSize(20);
        myPostRecycler.setHasFixedSize(true);

        getPermissions();
        permission();


        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);


        return view;
    }


    public void permission() {
        if (isPermissionAccepted) {

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

        }

    }


    public void getPermissions() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            isPermissionAccepted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if ((requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            isPermissionAccepted = true;
        }

    }


    @Override
    public void onResume() {
        super.onResume();


        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new GPSUtils(getActivity()).turnGPSOn(isGPSEnable -> {
                // turn on GPS
                isGPS = isGPSEnable;
            });

            if (isGPS) {

                showPosts();
            }
            noConnection.setVisibility(View.GONE);
        } else {
            noConnection.setVisibility(View.VISIBLE);

        }

        getSetData();

    }

    public void getSetData() {

        String name = SharedHelper.getKey(getActivity(), AppConstats.USER_NAME);
        String number = SharedHelper.getKey(getActivity(), AppConstats.USER_MOBILE_NUMBER);
        String email = SharedHelper.getKey(getActivity(), AppConstats.USER_EMAIL);
        String shopName = SharedHelper.getKey(getActivity(), AppConstats.USER_SHOPNAME);
        String type = SharedHelper.getKey(getActivity(), AppConstats.USER_TYPE);
        String image = SharedHelper.getKey(getActivity(), AppConstats.USER_IMAGE);
        String path = SharedHelper.getKey(getActivity(), AppConstats.USER_PATH);
        String desc = SharedHelper.getKey(getActivity(), AppConstats.DISCRETION);
        String address = SharedHelper.getKey(getActivity(), AppConstats.ADDRESS);
        String userID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
        String city = SharedHelper.getKey(getActivity(), AppConstats.USER_CITY);
        String state = SharedHelper.getKey(getActivity(), AppConstats.USER_STATE);


        mUsername.setText(name);

        if (desc.equals("")) {
            mDiscretion.setText("Not Available");
        } else {
            mDiscretion.setText(desc);
        }


        if (number.equals("")) {
            mNumber.setText("Not Available");
        } else {
            mNumber.setText(number);
        }

        if (email.equals("")) {
            mEmail.setText("Not Available");
        } else {
            mEmail.setText(email);
        }


        if (shopName.equals("")) {
            mShopName.setText("Not Available");
        } else {
            mShopName.setText(shopName);
        }

        if (address.equals("")) {
            mlocationtxt.setText("Not Available");
        } else {

            mlocationtxt.setText(address);
        }


        if (path.equals("")) {
            prf.setImageResource(R.drawable.prf_male);
        } else {
            Picasso.get().load(path + image).error(R.drawable.prf_male).into(prf);

        }


    }


    public void showPosts() {

        String userID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
        Log.e("disuydiw", userID);
        Map<String, String> params = new HashMap<>();
        params.put("control", "show_images");
        params.put("userID", userID);

        Call<ShowUserPosts> call = jsonInterface.showUserPosts(params);

        call.enqueue(new Callback<ShowUserPosts>() {
            @Override
            public void onResponse(@NonNull Call<ShowUserPosts> call, @NonNull Response<ShowUserPosts> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.code(), Toast.LENGTH_SHORT).show();
                }

                myPostsDataList = new ArrayList<>();

                ShowUserPosts showUserPosts = response.body();

                if (showUserPosts != null) {
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

                        if (myPostsDataList.size() == 0) {
                            nopost_Img.setVisibility(View.VISIBLE);
                        } else {
                            adapter = new MyPostsAdapter(myPostsDataList, getActivity());
                            myPostRecycler.setAdapter(adapter);
                        }


                    } else {
                        nopost_Img.setVisibility(View.VISIBLE);
                        TastyToast.makeText(getApplicationContext(), "No Posted Data yet!!!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                }


            }

            @Override
            public void onFailure(Call<ShowUserPosts> call, Throwable t) {

                Log.e("dsaduis", t.getMessage(), t);
            }
        });

    }


}