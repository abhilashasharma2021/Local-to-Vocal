package com.localtovocal.Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.localtovocal.Adapters.GPSTracker;
import com.localtovocal.Adapters.NewsAdapter;
import com.localtovocal.Model.NewsData;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.NewsDetailData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewsFragment extends Fragment {

    List<NewsData> newsDataList;

    NewsAdapter adapter;
    RecyclerView newRecycler;
    Retrofit retrofit;
    JsonInterface jsonInterface;
    ProgressBar progress;
    NewsData newsData;
    GPSTracker gpsTracker;
    double latitude;
    double longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        progress = view.findViewById(R.id.progress);

        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);
        PullRefreshLayout layout = view.findViewById(R.id.swipeRefreshLayout);

        layout.setOnRefreshListener(() -> layout.postDelayed(() -> {
            layout.setRefreshing(false);
            showNews();
        }, 2000));


        retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);

        newRecycler = view.findViewById(R.id.newRecycler);
        newRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        String userID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);


        showNews();


        return view;
    }


    public void showNews() {
        progress.setVisibility(View.VISIBLE);

        gpsTracker = new GPSTracker(getActivity());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        String pincode = "";

        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

            if (addressList != null) {
                pincode = addressList.get(0).getPostalCode();
                Log.e("ewrerewr", pincode);
            }

        } catch (Exception e) {
            Log.e("djksads", "sdnjksd" + e.getMessage());
        }

        Map<String, String> param = new HashMap<>();
        param.put("control", "show_news");
        param.put("pin_code", pincode);

        Call<NewsDetailData> call = jsonInterface.showNewsDetails(param);

        call.enqueue(new Callback<NewsDetailData>() {
            @Override
            public void onResponse(@NonNull Call<NewsDetailData> call, @NonNull Response<NewsDetailData> response) {

                if (!response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), response.code() + "Something went wrong", Toast.LENGTH_SHORT).show();
                }


                newsDataList = new ArrayList<>();

                NewsDetailData newsDetailData = response.body();

                if (newsDetailData != null) {
                    String msg = newsDetailData.getMessage();

                    if (newsDetailData.getResult()) {

                        List<NewsDetailData.Datum> list = newsDetailData.getData();

                        for (NewsDetailData.Datum data : list) {


                            String description = data.getDescription();
                            String image = data.getImage();
                            String path = data.getPath();
                            String show_type = data.getShowType();
                            String date_time = data.getDateTime();
                            String title = data.getTitle();

                            newsData = new NewsData();

                            if (show_type.equals("1")) {

                                NewsDetailData.Datum.AddvertiseInfo addvertiseInfo = data.getAddvertiseInfo();

                                if (addvertiseInfo != null) {

                                    String advID = addvertiseInfo.getId();
                                    String advType = addvertiseInfo.getType();
                                    String advImage = addvertiseInfo.getName();
                                    String advpath = addvertiseInfo.getPath();

                                    newsData.setAdID(advID);
                                    newsData.setType(advType);
                                    newsData.setName(advImage);
                                    newsData.setPath(advpath);

                                }


                            }

                            newsData.setImage(path + image);
                            newsData.setDescription(description);
                            newsData.setShowType(show_type);
                            newsData.setDate(date_time);
                            newsData.setNewstitle(title);

                            newsData.setShopName(data.getShopName());
                            newsData.setMobileNumber(data.getMobile());
                            newsData.setAlternateMobileNumber(data.getMobile2());
                            newsData.setAddress(data.getAddress());
                            newsData.setLatitude(data.getLatitude());
                            newsData.setLongitude(data.getLongitude());
                            newsData.setUserName(data.getName());
                            newsData.setUserID(data.getUserID());


                            newsDataList.add(newsData);
                            progress.setVisibility(View.GONE);
                        }

                        adapter = new NewsAdapter(newsDataList, getActivity());
                        newRecycler.setAdapter(adapter);


                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<NewsDetailData> call, @NonNull Throwable t) {
                Log.e("ryueir", t.getMessage(), t);
                progress.setVisibility(View.GONE);
            }
        });


    }


}