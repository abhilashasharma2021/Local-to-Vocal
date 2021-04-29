package com.localtovocal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.localtovocal.Image.Constants;
import com.localtovocal.Image.Payload;
import com.localtovocal.Image.Utils;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.UserSignUp;
import com.localtovocal.RetrofitModels.VerifyOtpModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtpVerifyActivity extends AppCompatActivity {
    ProgressBar progress;
    ImageView back;
    Retrofit retrofit;
    Button btnSend;
    JsonInterface jsonInterface;
    EditText editOne, editTwo, editThree, editFour;
    String strs = "", strsp = "", strspl = "", strsplite = "", strcombine, strMobile = "";
    String strName="",strPassword="",strEmail="",strShopName="",strImage="",strDescretion="",strCity="",strPinCode="",strState="",strAddress="",strLatitude="",strLongitude="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify_layout);
         strName = SharedHelper.getKey(getApplicationContext(), AppConstats.S_USERNAME);
         strEmail = SharedHelper.getKey(getApplicationContext(), AppConstats.S_EMAIL);
         strPassword = SharedHelper.getKey(getApplicationContext(), AppConstats.S_PASSWORD);
         strMobile = SharedHelper.getKey(getApplicationContext(), AppConstats.S_MOBILE);
        strShopName = SharedHelper.getKey(getApplicationContext(), AppConstats.S_SHOPNAME);
        strDescretion = SharedHelper.getKey(getApplicationContext(), AppConstats.S_Discreption);
        strCity = SharedHelper.getKey(getApplicationContext(), AppConstats.S_City);
        strPinCode = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_PINCODE);
        strState = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_STATE);
        strAddress = SharedHelper.getKey(getApplicationContext(), AppConstats.S_Address);
        strLatitude = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_LATITUDE);
        strLongitude = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_LONGITUDE);
        strImage = SharedHelper.getKey(getApplicationContext(), AppConstats.S_MOBILE);
        editOne = findViewById(R.id.editOne);
        editTwo = findViewById(R.id.editTwo);
        editThree = findViewById(R.id.editThree);
        editFour = findViewById(R.id.editFour);
        btnSend = findViewById(R.id.btnSend);
        progress = findViewById(R.id.progress);


        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);


        editOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                editTwo.requestFocus();
            }
        });


        editTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                editThree.requestFocus();

            }
        });

        editThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                editFour.requestFocus();

            }
        });

        editFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                strs = editOne.getText().toString().trim();
                Log.e("fgfgfg", strs);
                strsp = editTwo.getText().toString().trim();
                Log.e("fgfgfg", strsp);
                strspl = editThree.getText().toString().trim();
                Log.e("fgfgfg", strspl);
                strsplite = editFour.getText().toString().trim();
                Log.e("fgfgfg", strsplite);
                strcombine = strs + strsp + strspl + strsplite;
                Log.e("dkjfdfdljd", strcombine);

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_otp();
            }
        });
    }


    public void verify_otp(){


        progress.setVisibility(View.VISIBLE);

        Map<String , String>map=new HashMap<>();
        map.put("control","verify_otp");
        map.put("mobile",strMobile);
        map.put("otp",strcombine);

        Call<VerifyOtpModel>call=jsonInterface.verifyOtp(map);
        call.enqueue(new Callback<VerifyOtpModel>() {
            @Override
            public void onResponse(Call<VerifyOtpModel> call, Response<VerifyOtpModel> response) {
               Log.e("jfksdjk",response.toString());

               if (!response.isSuccessful()){
                   Toast.makeText(OtpVerifyActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                   progress.setVisibility(View.GONE);
               }

               VerifyOtpModel verifyOtpModel=response.body();
               if (verifyOtpModel!=null){

                   if (verifyOtpModel.getResult()){

                      Toast.makeText(OtpVerifyActivity.this, verifyOtpModel.getMessage(), Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(OtpVerifyActivity.this, BottomNavigationActivity.class));

                    //   signUp();
                       progress.setVisibility(View.GONE);
                   }else{

                       Toast.makeText(OtpVerifyActivity.this, verifyOtpModel.getMessage(), Toast.LENGTH_SHORT).show();
                       progress.setVisibility(View.GONE);
                   }

               }
            }

            @Override
            public void onFailure(Call<VerifyOtpModel> call, Throwable t) {
                Log.e("rt6uhyjh", t.getMessage());
                progress.setVisibility(View.GONE);
            }
        });



    }

    public void signUp(){

        String getTagsID = SharedHelper.getKey(getApplicationContext(), AppConstats.SELECTED_TAGS_ID);
        Log.e("dskdsklsdlkzns", getTagsID);
        progress.setVisibility(View.VISIBLE);
        ArrayList<Payload> payLoads = new ArrayList<>();
        payLoads.add(new Payload("control", "signup", Constants.MEDIA_TEXT));
        payLoads.add(new Payload("email", strEmail, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("password", strPassword, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("mobile", strMobile, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("name", strName, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("type", "1", Constants.MEDIA_TEXT));
        payLoads.add(new Payload("image", strImage, Constants.MEDIA_IMAGE));
        payLoads.add(new Payload("tag", getTagsID, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("shop_name", strShopName, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("description", strDescretion, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("address", strAddress, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("latitude", strLatitude, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("longitude", strLongitude, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("city", strCity, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("state", strState, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("pinCODE", strPinCode, Constants.MEDIA_TEXT));


        Call<UserSignUp> call = jsonInterface.newUser(Utils.generateMultiPartBody(payLoads));

        call.enqueue(new Callback<UserSignUp>() {
            @Override
            public void onResponse(@NonNull Call<UserSignUp> call, @NonNull Response<UserSignUp> response) {


                if (!response.isSuccessful()) {
                    Toast.makeText(OtpVerifyActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }

                UserSignUp userSignUp = response.body();

                if (userSignUp != null) {

                    UserSignUp.Data data = userSignUp.getData();

                    if (userSignUp.getResult()) {

                        String id = data.getUserID();
                        String name = data.getName();
                        String image = data.getImage();
                        String path = data.getPath();
                        String shopName = data.getShopName();
                        String tag = data.getTag();
                        String dis = data.getDescription();
                        String address = data.getAddress();
                        String mobile = data.getMobile();
                        String lat = data.getLatitude();
                        String lon = data.getLongitude();

                        Log.e("xjhxdjs", id);
                        Log.e("xjhxdjs", name);
                        Log.e("xjhxdjs", image);
                        Log.e("xjhxdjs", path);
                        Log.e("xjhxdjs", mobile);

                        Log.e("xjhxdjs", shopName);
                        Log.e("xjhxdjs", tag);
                        Log.e("xjhxdjs", dis);
                        Log.e("xjhxdjs", address);

                        Log.e("xjhxdjs", lat);
                        Log.e("xjhxdjs", lon);

                        progress.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();

                    } else {

                        Toast.makeText(OtpVerifyActivity.this, userSignUp.getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<UserSignUp> call, @NonNull Throwable t) {

                Log.e("csjnjc", t.getMessage(), t);
            }
        });

    }


}
