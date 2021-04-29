package com.localtovocal.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.localtovocal.Adapters.GPSTracker;
import com.localtovocal.Adapters.LocalAdapter;
import com.localtovocal.Model.LocalData;
import com.localtovocal.Others.API;
import com.localtovocal.Others.GPSUtils;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.ShowLocals;

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

public class LocalFragment extends Fragment {

    List<LocalData> localDataList;
    LocalAdapter adapter;
    RecyclerView localRecycler;

    Retrofit retrofit;
    JsonInterface jsonInterface;
    ProgressBar progress;

    GPSTracker gpsTracker;
    double latitude;
    double longitude;

    RelativeLayout rel, noConnection;
    String finalTag = "";

    private boolean isGPS = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_local, container, false);

        localRecycler = view.findViewById(R.id.localRecycler);
        rel = view.findViewById(R.id.rel);
        noConnection = view.findViewById(R.id.noConnection);
        progress = view.findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        PullRefreshLayout layout = view.findViewById(R.id.swipeRefreshLayout);

        layout.setOnRefreshListener(() -> layout.postDelayed(() -> {
            layout.setRefreshing(false);
            showLocals();
        }, 2000));

        retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        jsonInterface = retrofit.create(JsonInterface.class);

        localRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new GPSUtils(getActivity()).turnGPSOn(isGPSEnable -> isGPS = isGPSEnable);

            if (isGPS) {
                showLocals();
            }

            Log.e("cmskdxckms", isGPS + "");

            noConnection.setVisibility(View.GONE);
        } else {
            noConnection.setVisibility(View.VISIBLE);

        }

/*
  List<ListClass.MyData> myDataList = new ArrayList<>();


        for (int i = 0; i < 1; i++) {
            myDataList.add(new ListClass.MyData("1", "Raj", "Android"));
            myDataList.add(new ListClass.MyData("1", "Arpan", "Android"));
        }


        for (int i = 0; i <myDataList.size() ; i++) {
            String c = myDataList.get(i).getName();
            Toast.makeText(getActivity(), c, Toast.LENGTH_SHORT).show();
        }
*/


        return view;
    }


    public void showLocals() {
        progress.setVisibility(View.VISIBLE);
        gpsTracker = new GPSTracker(getActivity());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        Map<String, String> param = new HashMap<>();
        param.put("control", "show_shop");
        param.put("latitude", String.valueOf(latitude));
        param.put("longitude", String.valueOf(longitude));

        Call<ShowLocals> call = jsonInterface.showLocals(param);

        call.enqueue(new Callback<ShowLocals>() {
            @Override
            public void onResponse(@NonNull Call<ShowLocals> call, @NonNull Response<ShowLocals> response) {

                if (!response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), response.code() + "Something went wrong", Toast.LENGTH_SHORT).show();
                }


                localDataList = new ArrayList<>();


                ShowLocals showLocals = response.body();

                if (showLocals != null) {
                    String msg = showLocals.getMessage();

                    if (showLocals.getResult()) {

                        List<ShowLocals.Datum> list = showLocals.getData();

                        if (list.size() == 0) {
                            rel.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                        } else {
                            for (ShowLocals.Datum data : list) {

                                LocalData localData = new LocalData();


                                String userID = data.getUserID();
                                String type = data.getType();
                                String description = data.getDescription();
                                String image = data.getImage();
                                String path = data.getPath();
                                String name = data.getName();
                                String mobile = data.getMobile();
                                String address = data.getAddress();
                                String shopName = data.getShopName();
                                String lattitude = data.getLatitude();
                                String longitude = data.getLongitude();
                                String mobile2 = data.getMobile2();
                                String rating = data.getRate();


                                Log.e("euryuigf", rating);

                                List<ShowLocals.Datum.Tag> tagList = data.getTag();

                                if (tagList.size() > 0) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (ShowLocals.Datum.Tag myTags : tagList) {
                                        stringBuilder.append(myTags.getTagName());
                                        stringBuilder.append(",");
                                        Log.e("dklsjdlks", myTags.getTagID() + "," + myTags.getTagName());
                                    }

                                    finalTag = stringBuilder.toString();
                                    finalTag = finalTag.substring(0, finalTag.length() - 1);

                                    Log.e("smkashnajk", finalTag);
                                } else {
                                    finalTag = "";
                                }


                                Log.e("ncscncs", userID);


                                localData.setId(userID);
                                localData.setImage(path + image);
                                localData.setDescription(description);
                                localData.setName(name);
                                localData.setMobile(mobile);
                                localData.setAddress(address);
                                localData.setShopName(shopName);
                                localData.setTag(finalTag);
                                localData.setLattitude(lattitude);
                                localData.setLongitude(longitude);
                                localData.setAltMobile(mobile2);
                                localData.setRating(rating);
                                localData.setStatus("0");


                                localDataList .add(localData);
                                progress.setVisibility(View.GONE);
                            }

                            adapter = new LocalAdapter(localDataList, getActivity());
                            localRecycler.setAdapter(adapter);
                            rel.setVisibility(View.GONE);
                        }


                    } else {

                        progress.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<ShowLocals> call, Throwable t) {
                Log.e("ryueir", t.getMessage(), t);
                progress.setVisibility(View.GONE);
            }
        });


    }


}