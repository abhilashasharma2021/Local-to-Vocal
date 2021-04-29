package com.localtovocal.Activities;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.Wave;
import com.localtovocal.Model.ReviewData;
import com.localtovocal.Others.API;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.ShowReviewModel;
import com.localtovocal.RetrofitModels.TermsAndConditionsData;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TermAndConditionActivity extends AppCompatActivity {


    TextView text;
    Retrofit retrofit;
    JsonInterface jsonInterface;
    ProgressBar progress;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);

        text = findViewById(R.id.text);
        back = findViewById(R.id.back);
        progress = findViewById(R.id.progress);


        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        termsAndCond();

    }


    public void termsAndCond() {
        progress.setVisibility(View.VISIBLE);
        Call<TermsAndConditionsData> call = jsonInterface.termsAnConditions("terms_and_condition");

        call.enqueue(new Callback<TermsAndConditionsData>() {
            @Override
            public void onResponse(Call<TermsAndConditionsData> call, Response<TermsAndConditionsData> response) {
              Log.e("gfdghf",response.toString());
                if (!response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(TermAndConditionActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


                TermsAndConditionsData termsAndConditionsData = response.body();

                if (termsAndConditionsData != null) {
                    if (termsAndConditionsData.getResult()) {


                        List<TermsAndConditionsData.Dataterm> datumList = termsAndConditionsData.getData();

                        Log.e("dksdlks", datumList.toString());

                        for (TermsAndConditionsData.Dataterm datum : datumList) {


                            String id = datum.getId();
                            String textnew = datum.getText();

                            Log.e("fdsfsd", textnew);



                            text.setText(Html.fromHtml(textnew, Html.FROM_HTML_MODE_LEGACY));
                            progress.setVisibility(View.GONE);

                        }
                    }



                        }
                else {
                    Toast.makeText(TermAndConditionActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                    }
                    }

               /* String msg = termsAndConditionsData.getMessage();

                if (termsAndConditionsData.getResult()) {

                    TermsAndConditionsData.Data data = termsAndConditionsData.getData();

                    JSONArray jsonArray=new JSONArray();
                    for (int i=0;i<jsonArray.length();i++){

                        String myText = data.getText();

                        text.setText(Html.fromHtml(myText,Html.FROM_HTML_MODE_LEGACY));

                        //text.setText(myText);
                        progress.setVisibility(View.GONE);

                    }





                }*/

            @Override
            public void onFailure(Call<TermsAndConditionsData> call, Throwable t) {
                Log.e("fhduie", t.getMessage(), t);
                progress.setVisibility(View.GONE);
            }
        });
    }
}