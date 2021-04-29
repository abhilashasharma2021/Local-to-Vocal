package com.localtovocal.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.localtovocal.Adapters.ReviewAdapter;
import com.localtovocal.Model.ReviewData;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.ShowReviewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowReviewsActivity extends AppCompatActivity {


    List<ReviewData> reviewDataList;
    RecyclerView localReviews;
    JsonInterface jsonInterface;
    Retrofit retrofit;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reviews);

        localReviews = findViewById(R.id.localReviews);
        back = findViewById(R.id.back);

        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);

        localReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        localReviews.setItemViewCacheSize(20);
        localReviews.setHasFixedSize(true);

        back.setOnClickListener(view -> finish());

        showReviews();
    }

    public void showReviews() {
        String userID = SharedHelper.getKey(getApplicationContext(), AppConstats.LOCALS_USER_ID);
        Log.e("dskjd", userID);
        Map<String, String> param = new HashMap<>();
        param.put("control", "show_review");
        param.put("userID", userID);
        Call<ShowReviewModel> call = jsonInterface.showReviews(param);
        call.enqueue(new Callback<ShowReviewModel>() {
            @Override
            public void onResponse(@NonNull Call<ShowReviewModel> call, @NonNull Response<ShowReviewModel> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(ShowReviewsActivity.this, response.code() + " Failed", Toast.LENGTH_SHORT).show();
                }

                ShowReviewModel showReviewModel = response.body();

                if (showReviewModel != null) {
                    if (showReviewModel.getResult()) {

                        reviewDataList = new ArrayList<>();
                        List<ShowReviewModel.Datum> datumList = showReviewModel.getData();

                        Log.e("dksdlks", datumList.toString());

                        for (ShowReviewModel.Datum datum : datumList) {

                            ReviewData data = new ReviewData();

                            String id = datum.getId();
                            String rate = datum.getRate();
                            String feedback = datum.getFeedback();

                            Log.e("fdsfsd", feedback);

                            data.setDescription(feedback);
                            data.setId(id);
                            data.setRate(rate);

                            reviewDataList.add(data);


                        }

                        localReviews.setAdapter(new ReviewAdapter(reviewDataList, ShowReviewsActivity.this));

                    } else {
                        Toast.makeText(ShowReviewsActivity.this, "No Reviews for this user", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<ShowReviewModel> call, Throwable t) {
                Log.e("diksdksd", t.getMessage(), t);
            }
        });
    }
}