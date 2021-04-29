package com.localtovocal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.EditDescriptionModel;
import com.sdsmdg.tastytoast.ErrorToastView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DescriptionActivity extends AppCompatActivity {


    EditText etDescp;
    String strDesc = "";
    ImageView back;
    JsonInterface jsonInterface;
    Button btnSubmit;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        etDescp = findViewById(R.id.etDescp);
        back = findViewById(R.id.back);
        btnSubmit = findViewById(R.id.btnSubmit);
        progress = findViewById(R.id.progress);
        Sprite sprite = new Circle();
        progress.setIndeterminateDrawable(sprite);


        Retrofit retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strDesc = etDescp.getText().toString().trim();
                if (TextUtils.isEmpty(strDesc)) {
                    Toast.makeText(DescriptionActivity.this, "Enter description", Toast.LENGTH_SHORT).show();
                } else {

                    String userId = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
                    String postID = SharedHelper.getKey(getApplicationContext(), AppConstats.MY_POST_ID);

                    editDescription(userId, postID, strDesc);
                }
            }
        });
    }


    public void editDescription(String userID, String postID, String description) {

        progress.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("control", "update_description");
        params.put("userID", userID);
        params.put("postID", postID);
        params.put("description", description);
        Call<EditDescriptionModel> call = jsonInterface.editDescription(params);

        call.enqueue(new Callback<EditDescriptionModel>() {
            @Override
            public void onResponse(@NonNull Call<EditDescriptionModel> call, @NonNull Response<EditDescriptionModel> response) {
                if (!response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(DescriptionActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }

                EditDescriptionModel descriptionModel = response.body();

                if (descriptionModel != null) {

                    if (descriptionModel.getResult()) {
                        SharedHelper.putKey(getApplicationContext(), AppConstats.RESULT_UPLOAD, "success");
                        startActivity(new Intent(getApplicationContext(), BottomNavigationActivity.class));
                        finishAffinity();
                        progress.setVisibility(View.GONE);
                        Toast.makeText(DescriptionActivity.this, descriptionModel.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DescriptionActivity.this, descriptionModel.getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<EditDescriptionModel> call, @NonNull Throwable t) {
                Log.e("kdsmklds", t.getMessage());
                progress.setVisibility(View.GONE);
            }
        });

    }
}