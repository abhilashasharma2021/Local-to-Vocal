package com.localtovocal.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.localtovocal.Adapters.UpdateTagAdapter;
import com.localtovocal.Model.UpdateTagModel;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.ShowTags;
import com.localtovocal.RetrofitModels.UpdateTagData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TagsFragment extends Fragment {

    RecyclerView tagsRecycler;
    JsonInterface jsonInterface;
    Retrofit retrofit;
    List<UpdateTagModel> updateTagModelList;
    ImageView back;
    UpdateTagAdapter tagsAdapter;
    Button btnSelect;

    String updateTagID = "", updateTagNames = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tags, container, false);
        tagsRecycler = view.findViewById(R.id.tagsRecycler);
        back = view.findViewById(R.id.back);
        btnSelect = view.findViewById(R.id.btnSelect);


        tagsRecycler.setHasFixedSize(true);
        tagsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);

        back.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditProfileFragment()).commit();
        });

        showTags();


        btnSelect.setOnClickListener(vi -> {

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

                EditProfileFragment.MY_TAGS = newString;
                SharedHelper.putKey(getActivity(), AppConstats.SELECTED_TAGS_ID, newTAG_ID);


                updateTags(newTAG_ID);


                Log.e("xajzlxkjalkxj", newString);
                Log.e("xajzlxkjalkxj", newTAG_ID);
            } else {
                Toast.makeText(getActivity(), "Tags not selected yet", Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }


    public void showTags() {


        Map<String, String> params = new HashMap<>();
        params.put("control", "show_tag");
        Call<ShowTags> call = jsonInterface.showUserTags(params);
        call.enqueue(new Callback<ShowTags>() {
            @Override
            public void onResponse(Call<ShowTags> call, Response<ShowTags> response) {

                if (!response.isSuccessful()) {

                    Toast.makeText(getActivity(), response.code(), Toast.LENGTH_SHORT).show();
                }


                ShowTags showTags = response.body();
                if (showTags.getResult()) {


                    updateTagModelList = new ArrayList<>();

                    List<ShowTags.Datum> showTagsList = showTags.getData();

                    for (ShowTags.Datum d : showTagsList) {

                        UpdateTagModel tagsModel = new UpdateTagModel();
                        String tagID = d.getTagID();
                        String name = d.getName();

                        tagsModel.setTagID(tagID);
                        tagsModel.setTagName(name);

                        updateTagModelList.add(tagsModel);

                    }

                    tagsAdapter = new UpdateTagAdapter(updateTagModelList, getActivity());
                    tagsRecycler.setAdapter(tagsAdapter);
                    tagsAdapter.setTags(updateTagModelList);


                }

            }

            @Override
            public void onFailure(Call<ShowTags> call, Throwable t) {

                Log.e("dwuydgs", t.getMessage(), t);

            }
        });


    }


    public void updateTags(String tagID) {

        String userID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
        Log.e("dskjlkfcjdlk", userID);
        Log.e("dskjlkfcjdlk", tagID);

        Map<String, String> param = new HashMap<>();
        param.put("control", "add_tag");
        param.put("userID", userID);
        param.put("tagID", tagID);

        Call<UpdateTagData> call = jsonInterface.updateTagsData(param);

        call.enqueue(new Callback<UpdateTagData>() {
            @Override
            public void onResponse(Call<UpdateTagData> call, Response<UpdateTagData> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.code(), Toast.LENGTH_SHORT).show();
                }

                UpdateTagData updateTagData = response.body();

                if (updateTagData.getResult()) {

                    List<UpdateTagData.Datum> upData = updateTagData.getData();

                    if (upData.size() > 0) {

                        StringBuilder stringBuilder = new StringBuilder();
                        StringBuilder stringBuilder2 = new StringBuilder();

                        for (UpdateTagData.Datum d : upData) {

                            String tagID = d.getTagID();
                            String tagName = d.getName();

                            stringBuilder.append(tagID);
                            stringBuilder.append(",");

                            stringBuilder2.append(tagName);
                            stringBuilder2.append(",");
                        }

                        updateTagID = stringBuilder.toString();
                        updateTagID = updateTagID.substring(0, updateTagID.length() - 1);

                        updateTagNames = stringBuilder2.toString();
                        updateTagNames = updateTagNames.substring(0, updateTagNames.length() - 1);

                        Log.e("kjshdjs", updateTagID);
                        Log.e("kjshdjs", updateTagNames);

                        SharedHelper.putKey(getActivity(), AppConstats.TAG, updateTagNames);
                        SharedHelper.putKey(getActivity(), AppConstats.TAG_ID, updateTagID);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditProfileFragment()).commit();

                    }


                } else {
                    Log.e("dskdsknd", updateTagData.getMessage());
                    Toast.makeText(getActivity(), updateTagData.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UpdateTagData> call, Throwable t) {
                Log.e("dskdsknd", t.getMessage());

            }
        });
    }

}