package com.localtovocal.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.localtovocal.Adapters.GPSTracker;
import com.localtovocal.Adapters.LocalAdapter;
import com.localtovocal.Adapters.SearchLocalAdapter;
import com.localtovocal.Model.LocalData;
import com.localtovocal.Others.API;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.SearchShopData;
import com.localtovocal.RetrofitModels.ShowLocals;
import com.localtovocal.RetrofitModels.ShowTags;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchUsersActivity extends AppCompatActivity {


    List<LocalData> localDataList;
    LocalAdapter adapter;
    SearchLocalAdapter searchLocalAdapter;
    RecyclerView searchRecycler, localRecycler;

    Retrofit retrofit;
    JsonInterface jsonInterface;
    ProgressBar progress;

    String finalTag = "";
    String strSearch = "", strTagID = "";
    EditText etSearch;
    ImageView imgsearch, imgFilter;
    RelativeLayout rel, rel_notfound;
    Spinner spinnerTagSearch;
    ArrayList<String> tagsList, tagIDsList;

    GPSTracker gpsTracker;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        searchRecycler = findViewById(R.id.searchRecycler);
        localRecycler = findViewById(R.id.localRecycler);
        rel_notfound = findViewById(R.id.rel_notfound);
        spinnerTagSearch = findViewById(R.id.spinnerTagSearch);
        rel = findViewById(R.id.rel);

        etSearch = findViewById(R.id.etSearch);
        imgsearch = findViewById(R.id.imgsearch);
        imgFilter = findViewById(R.id.imgFilter);
        progress = findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();


        jsonInterface = retrofit.create(JsonInterface.class);

        searchRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.anim_recycler);
        searchRecycler.setLayoutAnimation(controller);
        searchRecycler.scheduleLayoutAnimation();

        localRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        localRecycler.setLayoutAnimation(controller);
        localRecycler.scheduleLayoutAnimation();


        spinnerTagSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String tagName = tagsList.get(i);
                String tagID = tagIDsList.get(i);

                Log.e("djskdjs", tagName);
                Log.e("djskdjs", tagID);

                strTagID = tagID;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        imgsearch.setOnClickListener(view -> {
            strSearch = etSearch.getText().toString().trim();
            showLocals(strSearch, strTagID);
            localRecycler.setVisibility(View.GONE);
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                strSearch = etSearch.getText().toString().trim();

                showLocals(strSearch, strTagID);
                localRecycler.setVisibility(View.GONE);

                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    showLocals(strSearch, strTagID);
                    localRecycler.setVisibility(View.GONE);
                }
                return false;
            }
        });

        showTags();
        showLocalsTwo();


    }


    public void showLocals(String strSearch, String tagID) {
        progress.setVisibility(View.VISIBLE);
        localRecycler.setVisibility(View.GONE);
        Log.e("djskldj", strSearch);
        Log.e("djskldj", tagID);

        Map<String, String> param = new HashMap<>();
        param.put("control", "search_shop");
        param.put("word", strSearch);
        param.put("tagID", tagID);

        Call<SearchShopData> call = jsonInterface.searchShops(param);
        call.enqueue(new Callback<SearchShopData>() {
            @Override
            public void onResponse(@NonNull Call<SearchShopData> call, @NonNull Response<SearchShopData> response) {

                if (!response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    localRecycler.setVisibility(View.GONE);
                    TastyToast.makeText(getApplicationContext(), response.code() + "", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }

                SearchShopData shopData = response.body();
                if (shopData != null) {
                    if (shopData.getResult()) {
                        searchRecycler.setVisibility(View.VISIBLE);
                        localDataList = new ArrayList<>();
                        progress.setVisibility(View.GONE);

                        List<SearchShopData.Datum> list = shopData.getData();

                        if (list.size() == 0) {

                        } else {
                            for (SearchShopData.Datum data : list) {

                                LocalData localData = new LocalData();

                                String userID = data.getUserID();
                                String type = data.getType();
                                String description = data.getDescription();
                                String image = data.getImage();
                                String name = data.getName();
                                String mobile = data.getMobile();
                                String address = data.getAddress();
                                String shopName = data.getShopName();
                                String path = data.getPath();
                                String latitude = data.getLatitude();
                                String longitude = data.getLongitude();
                                String altMobile = data.getMobile2();
                                String rating = data.getRate();



                                Log.e("jsakdjlkas","msg : "+ rating);

                                List<SearchShopData.Datum.Tag> tagList = data.getTag();

                                if (tagList.size() > 0) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (SearchShopData.Datum.Tag myTags : tagList) {
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


                                localData.setId(userID);
                                localData.setImage(path + image);
                                localData.setDescription(description);
                                localData.setName(name);
                                localData.setMobile(mobile);
                                localData.setAddress(address);
                                localData.setShopName(shopName);
                                localData.setTag(finalTag);
                                localData.setLattitude(latitude);
                                localData.setLongitude(longitude);
                                localData.setAltMobile(altMobile);
                                localData.setRating(rating);
                                localData.setStatus("1");

                                localDataList.add(localData);


                            }
                            rel_notfound.setVisibility(View.GONE);
                            localRecycler.setVisibility(View.GONE);
                            searchLocalAdapter = new SearchLocalAdapter(localDataList, SearchUsersActivity.this);
                            searchRecycler.setAdapter(searchLocalAdapter);

                        }


                    } else {
                        progress.setVisibility(View.GONE);
                        rel_notfound.setVisibility(View.VISIBLE);
                        searchRecycler.setVisibility(View.GONE);
                        localRecycler.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<SearchShopData> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
                rel_notfound.setVisibility(View.GONE);
                localRecycler.setVisibility(View.GONE);
                Log.e("sjmsaiduw", t.getMessage(), t);
            }
        });


    }


    public void showTags() {


        Map<String, String> params = new HashMap<>();
        params.put("control", "show_tag");
        Call<ShowTags> call = jsonInterface.showUserTags(params);
        call.enqueue(new Callback<ShowTags>() {
            @Override
            public void onResponse(@NonNull Call<ShowTags> call, @NonNull Response<ShowTags> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(SearchUsersActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }


                ShowTags showTags = response.body();
                assert showTags != null;
                if (showTags.getResult()) {

                    tagsList = new ArrayList<>();
                    tagIDsList = new ArrayList<>();

                    List<ShowTags.Datum> showTagsList = showTags.getData();

                    tagIDsList.add("0");
                    tagsList.add("Select Tag");

                    for (ShowTags.Datum d : showTagsList) {

                        Log.e("dskjdks", d.getName());
                        Log.e("dskjdks", d.getTagID());
                        tagsList.add(d.getName());
                        tagIDsList.add(d.getTagID());

                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, tagsList);
                    spinnerTagSearch.setAdapter(arrayAdapter);

                }

            }

            @Override
            public void onFailure(@NonNull Call<ShowTags> call, @NonNull Throwable t) {

                Log.e("dwuydgs", t.getMessage(), t);

            }
        });


    }


    public void showLocalsTwo() {

        progress.setVisibility(View.VISIBLE);
        gpsTracker = new GPSTracker(this);
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
                    Toast.makeText(SearchUsersActivity.this, response.code() + "Something went wrong", Toast.LENGTH_SHORT).show();
                }


                localDataList = new ArrayList<>();


                ShowLocals showLocals = response.body();

                if (showLocals != null) {
                    String msg = showLocals.getMessage();

                    if (showLocals.getResult()) {

                        List<ShowLocals.Datum> list = showLocals.getData();

                        if (list.size() == 0) {
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

                                Log.e("skdkls",data.getRate());


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


                                } else {
                                    finalTag = "";
                                }

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

                                localDataList.add(localData);
                                progress.setVisibility(View.GONE);
                            }

                            adapter = new LocalAdapter(localDataList, SearchUsersActivity.this);
                            localRecycler.setAdapter(adapter);
                        }


                    } else {

                        progress.setVisibility(View.GONE);
                        Toast.makeText(SearchUsersActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<ShowLocals> call, @NonNull Throwable t) {
                Log.e("ryueir", t.getMessage(), t);
                progress.setVisibility(View.GONE);
            }
        });


    }


}