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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.localtovocal.Others.API;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.ForgotPassModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email;
    String strEmail = "";
    Button btnSubmit;
    ImageView back;
    Retrofit retrofit;
    JsonInterface jsonInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email);
        back = findViewById(R.id.back);
        btnSubmit = findViewById(R.id.btnSubmit);

        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail = email.getText().toString().trim();

                if (strEmail.equals("")) {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    forgotPass(strEmail);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void forgotPass(String email) {

        Map<String, String> params = new HashMap<>();
        params.put("control", "forgot_pass");
        params.put("email", email);
        Call<ForgotPassModel> call = jsonInterface.forgotPassword(params);
        call.enqueue(new Callback<ForgotPassModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgotPassModel> call, @NonNull Response<ForgotPassModel> response) {



                Log.e("cmnkjcn", response.body().toString());

                if (!response.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }

                ForgotPassModel forgotPassData = response.body();

                Log.e("klglf",response.body()+"");

                if (forgotPassData != null) {

                    if (forgotPassData.getResult()) {
                        successDialog();
                        Toast.makeText(ForgotPasswordActivity.this, forgotPassData.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, forgotPassData.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForgotPassModel> call, @NonNull Throwable t) {
                Log.e("sweuiwe", t.getMessage(), t);
            }
        });
    }


    public void successDialog() {

        Dialog dialog = new Dialog(ForgotPasswordActivity.this);
        dialog.setContentView(R.layout.successfull_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


}