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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.localtovocal.Adapters.TagsAdapter;
import com.localtovocal.Model.TagsModel;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.ShowTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TagsActivity extends AppCompatActivity {

    RecyclerView tagsRecycler;
    JsonInterface jsonInterface;
    Retrofit retrofit;
    List<TagsModel> tagsModelList;
    ImageView back, suggestion;
    TagsAdapter tagsAdapter;
    Button btnSelect;
    EditText etSearch;


    String username = "", email = "", password = "", number = "", shopName = "", image = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);


        tagsRecycler = findViewById(R.id.tagsRecycler);
        etSearch = findViewById(R.id.etSearch);
        back = findViewById(R.id.back);
        btnSelect = findViewById(R.id.btnSelect);
        suggestion = findViewById(R.id.suggestion);
        tagsRecycler.setHasFixedSize(true);
        tagsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);

        back.setOnClickListener(view -> {
            finish();
        });

        username = SharedHelper.getKey(getApplicationContext(), AppConstats.S_USERNAME);
        email = SharedHelper.getKey(getApplicationContext(), AppConstats.S_EMAIL);
        password = SharedHelper.getKey(getApplicationContext(), AppConstats.S_PASSWORD);
        number = SharedHelper.getKey(getApplicationContext(), AppConstats.S_MOBILE);
        shopName = SharedHelper.getKey(getApplicationContext(), AppConstats.S_SHOPNAME);

        suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), SuggestionsActivity.class));
            }
        });


        showTags();

        btnSelect.setOnClickListener(view -> {

            if (tagsAdapter.getSelected().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder stringBuilder2 = new StringBuilder();
                for (int i = 0; i < tagsAdapter.getSelected().size(); i++) {
                    stringBuilder.append(tagsAdapter.getSelected().get(i).getTagName());
                    stringBuilder2.append(tagsAdapter.getSelected().get(i).getTagID());
                    stringBuilder.append(",");
                    stringBuilder2.append(",");
                }


                String newString = stringBuilder.toString();
                newString = newString.substring(0, newString.length() - 1);

                String newTAG_ID = stringBuilder2.toString();
                newTAG_ID = newTAG_ID.substring(0, newTAG_ID.length() - 1);

                SharedHelper.putKey(getApplicationContext(), AppConstats.SELECTED_TAGS, newString);
                SharedHelper.putKey(getApplicationContext(), AppConstats.SELECTED_TAGS_ID, newTAG_ID);

                SharedHelper.putKey(getApplicationContext(), AppConstats.S_USERNAME, username);
                SharedHelper.putKey(getApplicationContext(), AppConstats.S_EMAIL, email);
                SharedHelper.putKey(getApplicationContext(), AppConstats.S_PASSWORD, password);
                SharedHelper.putKey(getApplicationContext(), AppConstats.S_MOBILE, number);
                SharedHelper.putKey(getApplicationContext(), AppConstats.S_SHOPNAME, shopName);
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finishAffinity();

            } else {
                Toast.makeText(this, "Tags not selected yet", Toast.LENGTH_SHORT).show();
            }

        });


    }


    public void showTags() {


        Map<String, String> params = new HashMap<>();
        params.put("control", "show_tag");
        Call<ShowTags> call = jsonInterface.showUserTags(params);
        call.enqueue(new Callback<ShowTags>() {
            @Override
            public void onResponse(@NonNull Call<ShowTags> call, @NonNull Response<ShowTags> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(TagsActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }


                ShowTags showTags = response.body();
                if (showTags.getResult()) {


                    tagsModelList = new ArrayList<>();

                    List<ShowTags.Datum> showTagsList = showTags.getData();

                    for (ShowTags.Datum d : showTagsList) {

                        TagsModel tagsModel = new TagsModel();
                        String tagID = d.getTagID();
                        String name = d.getName();

                        tagsModel.setTagID(tagID);
                        tagsModel.setTagName(name);

                        tagsModelList.add(tagsModel);

                    }

                    tagsAdapter = new TagsAdapter(tagsModelList, TagsActivity.this);
                    tagsRecycler.setAdapter(tagsAdapter);
                    tagsAdapter.setTags(tagsModelList);

                    etSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            tagsAdapter.getFilter().filter(charSequence);

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                           /* if (editable.length() == 0) {
                                showTags();
                            }*/

                        }
                    });


                }

            }

            @Override
            public void onFailure(@NonNull Call<ShowTags> call, @NonNull Throwable t) {

                Log.e("dwuydgs", t.getMessage(), t);

            }
        });


    }

}