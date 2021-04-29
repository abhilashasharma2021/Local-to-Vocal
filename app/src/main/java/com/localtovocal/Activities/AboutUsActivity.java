package com.localtovocal.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.localtovocal.Others.API;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.AboutUsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AboutUsActivity extends AppCompatActivity {
    ProgressBar progress;
    ImageView back;
    TextView text;
    Retrofit retrofit;
    JsonInterface jsonInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        text = findViewById(R.id.text);
        back = findViewById(R.id.back);
        progress = findViewById(R.id.progress);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        retrofit=new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface=retrofit.create(JsonInterface.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        about_us();
    }

    public void about_us(){

        progress.setVisibility(View.VISIBLE);
        Call<AboutUsModel>call=jsonInterface.aboutus("terms_and_condition");
        call.enqueue(new Callback<AboutUsModel>() {
            @Override
            public void onResponse(Call<AboutUsModel> call, Response<AboutUsModel> response) {

                if (!response.isSuccessful()){

                    progress.setVisibility(View.GONE);
                    Toast.makeText(AboutUsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

                AboutUsModel aboutUsModel=response.body();
                if (aboutUsModel !=null){
                    if (aboutUsModel.getResult()){

                        List<AboutUsModel.DataAbout>dataAbouts=aboutUsModel.getData();
                        Log.e("dksdlks", dataAbouts.toString());

                        for (AboutUsModel.DataAbout dataAbout :dataAbouts){


                            String id = dataAbout.getId();
                            String textnew = dataAbout.getText();

                            Log.e("fdsfsd", textnew);



                            text.setText(Html.fromHtml(textnew, Html.FROM_HTML_MODE_LEGACY));
                            progress.setVisibility(View.GONE);

                        }
                    }


                    }
                else {
                    Toast.makeText(AboutUsActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<AboutUsModel> call, Throwable t) {
                Log.e("sgdfh", t.getMessage(), t);
                progress.setVisibility(View.GONE);
            }
        });


    }
}