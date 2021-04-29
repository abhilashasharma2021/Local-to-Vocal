package com.localtovocal.Fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;

import com.localtovocal.Adapters.SubscribePlansAdapter;
import com.localtovocal.Adapters.TextAdapter;
import com.localtovocal.Model.SubscribePlansData;
import com.localtovocal.Model.TextData;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.SubscribePlanModel;
import com.localtovocal.RetrofitModels.TextModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubscribeFragment extends Fragment {

    RecyclerView plansRecycler, textRecycler;

    List<SubscribePlansData> subscribePlansDataList;
    List<TextData> textDataList;
    Retrofit retrofit;
    JsonInterface jsonInterface;
    ProgressBar progress,progress2;
    RelativeLayout rel_plan;
    TextView plan, remainingDay, dateRemain;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);


        textRecycler = view.findViewById(R.id.textRecycler);
        rel_plan = view.findViewById(R.id.rel_plan);
        plansRecycler = view.findViewById(R.id.plansRecycler);
        plan = view.findViewById(R.id.plan);
        dateRemain = view.findViewById(R.id.dateRemain);
        remainingDay = view.findViewById(R.id.remainingDay);
        progress = view.findViewById(R.id.progress);
        progress2 = view.findViewById(R.id.progress2);
        Sprite sprite = new Circle();
        progress.setIndeterminateDrawable(sprite);
        progress2.setIndeterminateDrawable(sprite);

        plansRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        plansRecycler.setItemViewCacheSize(20);

        textRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        textRecycler.setItemViewCacheSize(20);


        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);

        showPlans();
        showText();

        return view;
    }

    public void showPlans() {
        String userID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
        progress.setVisibility(View.VISIBLE);
        progress2.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("control", "show_plan");
        params.put("userID", userID);
        Call<SubscribePlanModel> call = jsonInterface.subscribePlans(params);
        call.enqueue(new Callback<SubscribePlanModel>() {

            @Override
            public void onResponse(@NonNull Call<SubscribePlanModel> call, @NonNull Response<SubscribePlanModel> response) {

                if (!response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    progress2.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), response.code(), Toast.LENGTH_SHORT).show();
                }

                SubscribePlanModel subscribePlanModel = response.body();

                if (subscribePlanModel != null) {

                    if (subscribePlanModel.getResult()) {

                        Log.e("uweuywte", subscribePlanModel.getStatus());
                        if (subscribePlanModel.getStatus().equals("0")) {
                            List<SubscribePlanModel.Datum> datumList = subscribePlanModel.getData();
                            rel_plan.setVisibility(View.GONE);
                            subscribePlansDataList = new ArrayList<>();

                            for (SubscribePlanModel.Datum result : datumList) {
                                SubscribePlansData plansData = new SubscribePlansData();

                                String planID = result.getPlanID();
                                String month = result.getMonth();
                                String price = result.getPrice();
                                plansData.setPlanID(planID);
                                plansData.setMonth(month);
                                plansData.setPrice(price);
                                subscribePlansDataList.add(plansData);
                            }
                            progress2.setVisibility(View.GONE);
                            progress.setVisibility(View.GONE);
                            plansRecycler.setAdapter(new SubscribePlansAdapter(subscribePlansDataList, getActivity()));

                        } else {
                            progress2.setVisibility(View.GONE);
                            rel_plan.setVisibility(View.VISIBLE);
                            List<SubscribePlanModel.Datum> datumList = subscribePlanModel.getData();

                            subscribePlansDataList = new ArrayList<>();

                            for (SubscribePlanModel.Datum result : datumList) {

                                String planID = result.getPlanID();
                                String month = result.getMonth();
                                String price = result.getPrice();
                                String remain_day = result.getRemain_day();

                                plan.setText(price + " Rs");
                                remainingDay.setText("Remaining Days : " + remain_day);


                                int daysRemaining = Integer.parseInt(remain_day);

                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.DATE, daysRemaining);
                                Log.i("MyApp", "10 days ago:" + calendar.getTime().toString());

                                String[] dateTime = calendar.getTime().toString().split(" ");
                                String d1 = dateTime[0];
                                String d2 = dateTime[1];
                                String d3 = dateTime[2];

                                dateRemain.setText("Expire on : " + d1 + "," + d2 + " " + d3);
                            }


                        }


                    }

                }


            }

            @Override
            public void onFailure(Call<SubscribePlanModel> call, Throwable t) {
                progress.setVisibility(View.GONE);
                progress2.setVisibility(View.GONE);
                Log.e("djsdjskds", t.getMessage(), t);

            }
        });
    }


    public void showText() {

        Map<String, String> params = new HashMap<>();
        params.put("control", "show_plan_text");

        Call<TextModel> call = jsonInterface.showText(params);
        call.enqueue(new Callback<TextModel>() {
            @Override
            public void onResponse(@NonNull Call<TextModel> call, @NonNull Response<TextModel> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.code(), Toast.LENGTH_SHORT).show();
                }

                TextModel textModel = response.body();

                assert textModel != null;
                if (textModel.getResult()) {
                    List<TextModel.Datum> datumList = textModel.getData();
                    textDataList = new ArrayList<>();
                    for (TextModel.Datum result : datumList) {

                        TextData textData = new TextData();
                        String txt = result.getText();
                        textData.setText(txt);
                        textDataList.add(textData);
                    }

                    textRecycler.setAdapter(new TextAdapter(textDataList, getActivity()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TextModel> call, Throwable t) {
                Log.e("djsdjskds", t.getMessage(), t);
            }
        });

    }
}