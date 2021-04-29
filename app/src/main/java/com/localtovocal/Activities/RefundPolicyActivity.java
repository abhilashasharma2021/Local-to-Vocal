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
import com.localtovocal.RetrofitModels.RefundPolicyModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RefundPolicyActivity extends AppCompatActivity {
    ProgressBar progress;
    ImageView back;
    TextView text;
    Retrofit retrofit;
    JsonInterface jsonInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_policy);

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
        refund_Policy();

    }

    public void refund_Policy(){
        progress.setVisibility(View.VISIBLE);

        Call<RefundPolicyModel>call=jsonInterface.refundPolicy("terms_and_condition");

        call.enqueue(new Callback<RefundPolicyModel>() {
            @Override
            public void onResponse(Call<RefundPolicyModel> call, Response<RefundPolicyModel> response) {
                if (!response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(RefundPolicyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();


                }

                RefundPolicyModel refundPolicyModel=response.body();
                if (refundPolicyModel!=null){

                    if (refundPolicyModel.getResult()){
                        List<RefundPolicyModel.DataRefund>dataRefundList=refundPolicyModel.getData();
                        Log.e("dksdlks", dataRefundList.toString());

                        for (RefundPolicyModel.DataRefund refund :dataRefundList){


                            String id = refund.getId();
                            String textnew = refund.getText();

                            Log.e("ewtery", textnew);



                            text.setText(Html.fromHtml(textnew, Html.FROM_HTML_MODE_LEGACY));
                            progress.setVisibility(View.GONE);
                        }
                    }

                }

                else {
                    Toast.makeText(RefundPolicyActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RefundPolicyModel> call, Throwable t) {
                Log.e("reyrtut", t.getMessage(), t);
                progress.setVisibility(View.GONE);
            }
        });


    }
}