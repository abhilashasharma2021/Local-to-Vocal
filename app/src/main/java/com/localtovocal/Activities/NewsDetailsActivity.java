package com.localtovocal.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;

import java.util.concurrent.TimeUnit;

public class NewsDetailsActivity extends AppCompatActivity {


    ImageView image,back;
    TextView title, description;
    CollapsingToolbarLayout collapsing;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);


        image = findViewById(R.id.image);
        back = findViewById(R.id.back);
        description = findViewById(R.id.description);
        title = findViewById(R.id.title);
        collapsing = findViewById(R.id.collapsing);

        dialog = new ProgressDialog(this);
        dialog.setTitle("New Details");
        dialog.setMessage("please wait...");
        dialog.show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String newTitle = SharedHelper.getKey(getApplicationContext(), AppConstats.NEWS_HEADING);
                String newDescp = SharedHelper.getKey(getApplicationContext(), AppConstats.NEWS_DESCRIPTION);
                String nImage = SharedHelper.getKey(getApplicationContext(), AppConstats.NEWS_IMAGE);

                Glide.with(getApplicationContext()).load(nImage).placeholder(R.drawable.locallogo).into(image);
                if (newTitle.equals("")){
                    title.setText("Not Available");
                }else {
                    title.setText(newTitle);
                }

                if (newDescp.equals("")){
                    description.setText("Not Available");
                }else {
                    description.setText(newDescp);
                }


                collapsing.setTitle(newTitle);

                dialog.dismiss();
            }
        },1500);



    }
}