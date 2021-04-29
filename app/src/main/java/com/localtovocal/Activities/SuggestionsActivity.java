package com.localtovocal.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.localtovocal.Others.API;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.SuggestionsData;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SuggestionsActivity extends AppCompatActivity {

    ImageView back;
    EditText editSuggestion;
    Button btnSubmit;
    String strSuggestion = "";

    Retrofit retrofit;
    JsonInterface jsonInterface;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        back = findViewById(R.id.back);
        editSuggestion = findViewById(R.id.editSuggestion);
        btnSubmit = findViewById(R.id.btnSubmit);
        progress = findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);


        back.setOnClickListener(v -> {
            finish();
        });

        btnSubmit.setOnClickListener(view -> {
            strSuggestion = editSuggestion.getText().toString().trim();

            if (strSuggestion.equals("")) {
                Toast.makeText(getApplicationContext(), "Enter your suggestions", Toast.LENGTH_SHORT).show();
            } else {
                sendSuggestions(strSuggestion);
            }
        });


    }


    public void sendSuggestions(String suggestion) {
        progress.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("control", "add_suggestion");
        params.put("suggestion", suggestion);


        Call<SuggestionsData> call = jsonInterface.sendSuggestion(params);

        call.enqueue(new Callback<SuggestionsData>() {
            @Override
            public void onResponse(@NonNull Call<SuggestionsData> call, @NonNull Response<SuggestionsData> response) {

                if (!response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(SuggestionsActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }

                SuggestionsData suggestionsData = response.body();
                if (suggestionsData!=null){
                    String message = suggestionsData.getMessage();

                    if (suggestionsData.getResult()) {

                        progress.setVisibility(View.GONE);
                        successDialog();
                        editSuggestion.setText("");

                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(SuggestionsActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<SuggestionsData> call, @NonNull Throwable t) {
                Log.e("dushduiw", t.getMessage(), t);
                progress.setVisibility(View.GONE);
            }
        });
    }


    public void successDialog() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.request_submitted_layout);


        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}