package com.localtovocal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.ReviewModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button btnSubmit;
    String strRating = "";
    EditText etReview, etEmail;
    String strReview = "", strEmail = "";
    ImageView back;
    JsonInterface jsonInterface;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ratingBar = findViewById(R.id.ratingBar);
        btnSubmit = findViewById(R.id.btnSubmit);
        etReview = findViewById(R.id.etReview);
        back = findViewById(R.id.back);
        etEmail = findViewById(R.id.etEmail);


        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);


        back.setOnClickListener(view -> finish());

        strRating = "0";

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                double numStars = ratingBar.getRating();
                String fRate = String.valueOf(numStars);
                Log.e("klasa", fRate);
                strRating = fRate;
            }
        });
        String userID = SharedHelper.getKey(ReviewActivity.this, AppConstats.LOCALS_USER_ID);

        Log.e("uewiwewr", userID);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strReview = etReview.getText().toString().trim();
                strEmail = etEmail.getText().toString().trim();


                if (strReview.equals("")) {
                    Toast.makeText(ReviewActivity.this, "Please enter your review", Toast.LENGTH_SHORT).show();
                } else if (strEmail.equals("")) {
                    Toast.makeText(ReviewActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();

                } else {
                    String userID = SharedHelper.getKey(ReviewActivity.this, AppConstats.LOCALS_USER_ID);
                    String uID = SharedHelper.getKey(ReviewActivity.this, AppConstats.USER_ID);

                    if (uID.equals(userID)) {
                        Toast.makeText(ReviewActivity.this, "You can't review yourself", Toast.LENGTH_SHORT).show();

                    } else {
                        review(strRating, strReview, userID, strEmail);
                        etReview.setText("");
                    }


                }


            }
        });


    }


    public void review(String rating, String feedback, String localuserID, String email) {

        Map<String, String> param = new HashMap<>();
        param.put("control", "add_review");
        param.put("rate", rating);
        param.put("feedback", feedback);
        param.put("shopID", localuserID);
        param.put("email", email);
        Call<ReviewModel> call = jsonInterface.giveReview(param);
        call.enqueue(new Callback<ReviewModel>() {
            @Override
            public void onResponse(@NonNull Call<ReviewModel> call, @NonNull Response<ReviewModel> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(ReviewActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }

                ReviewModel reviewModel = response.body();
                if (reviewModel != null) {

                    if (reviewModel.getResult()) {
                        startActivity(new Intent(ReviewActivity.this, LocalDetailsActivity.class));
                        finish();
                        Toast.makeText(ReviewActivity.this, "Sent Successfully", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(ReviewActivity.this, reviewModel.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewModel> call, @NonNull Throwable t) {

            }
        });

    }
}