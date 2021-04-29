package com.localtovocal.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.localtovocal.Activities.DescriptionActivity;
import com.localtovocal.Activities.VideoPlayerActivity;
import com.localtovocal.Fragments.MyProfileFragment;
import com.localtovocal.Model.MyPostsData;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.DeletePost;
import com.localtovocal.RetrofitModels.PostAdvertisement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.ViewHolder> {

    List<MyPostsData> myPostsDataList;
    Context context;


    public MyPostsAdapter(List<MyPostsData> myPostsDataList, Context context) {
        this.myPostsDataList = myPostsDataList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyPostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.myposts_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostsAdapter.ViewHolder holder, int position) {

        MyPostsData data = myPostsDataList.get(position);


        String subscription_status = SharedHelper.getKey(context, AppConstats.USER_SUBSCRIPTION_STATUS);

        Glide.with(context).load(data.getPath() + data.getFilename()).into(holder.image);


        if (data.getType().equals("0")) {

            holder.playBtn.setVisibility(View.GONE);
            holder.image.setOnClickListener(view -> {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.progress_bar);

                ImageView dImage = dialog.findViewById(R.id.dImage);
                ImageView delete = dialog.findViewById(R.id.delete);
                ImageView btn_play = dialog.findViewById(R.id.btn_play);
                ImageView addpost = dialog.findViewById(R.id.addpost);
                ImageView editDesc = dialog.findViewById(R.id.editDesc);
                TextView alreadyShown = dialog.findViewById(R.id.alreadyShown);
                TextView description = dialog.findViewById(R.id.description);

                editDesc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedHelper.putKey(context, AppConstats.MY_POST_ID, data.getId());
                        context.startActivity(new Intent(context, DescriptionActivity.class));
                        dialog.dismiss();
                    }
                });

                if (data.getDescription().equals("")) {
                    description.setText("Description : " + "Not Available");
                } else {
                    description.setText("Description : " + data.getDescription());
                }


                if (data.getAdd_status().equals("true")) {
                    addpost.setVisibility(View.GONE);
                    alreadyShown.setVisibility(View.VISIBLE);
                } else {
                    addpost.setVisibility(View.VISIBLE);
                    alreadyShown.setVisibility(View.GONE);
                    addpost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String userID = SharedHelper.getKey(context, AppConstats.USER_ID);
                            Log.e("auywedws", userID);
                            addAdverstisement(userID, data.getId());
                            dialog.dismiss();

                        }
                    });
                }

                Glide.with(context).load(data.getPath() + data.getFilename()).into(dImage);
                btn_play.setVisibility(View.GONE);
                String deleteHide = SharedHelper.getKey(context, AppConstats.LOCALS_CLICK_FOR_DELETE);

                if (deleteHide.equals("DONT_DELETE")) {
                    delete.setVisibility(View.GONE);
                    addpost.setVisibility(View.GONE);
                    editDesc.setVisibility(View.GONE);
                } else {
                    delete.setVisibility(View.VISIBLE);
                    delete.setOnClickListener(view1 -> {

                        deletePost(data.getId());
                        dialog.dismiss();
                    });
                }

                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            });
        } else {
            holder.playBtn.setVisibility(View.VISIBLE);
            Log.e("rewwerwrwerwerwerwerwe", data.getPath() + data.getFilename());
            try {
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(data.getPath() + data.getFilename(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                holder.image.setImageBitmap(thumbnail);

            } catch (Throwable e) {
                e.printStackTrace();
            }

            holder.image.setOnClickListener(view -> {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.progress_bar);

                ImageView dImage = dialog.findViewById(R.id.dImage);
                ImageView delete = dialog.findViewById(R.id.delete);
                ImageView btn_play = dialog.findViewById(R.id.btn_play);
                ImageView addpost = dialog.findViewById(R.id.addpost);
                ImageView editDesc = dialog.findViewById(R.id.editDesc);
                TextView alreadyShown = dialog.findViewById(R.id.alreadyShown);
                TextView description = dialog.findViewById(R.id.description);


                editDesc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedHelper.putKey(context, AppConstats.MY_POST_ID, data.getId());
                        context.startActivity(new Intent(context, DescriptionActivity.class));
                        dialog.dismiss();
                    }
                });

                if (data.getDescription().equals("")) {
                    description.setText("Description : " + "Not Available");
                } else {
                    description.setText("Description : " + data.getDescription());
                }


                if (data.getAdd_status().equals("true")) {
                    addpost.setVisibility(View.GONE);
                    alreadyShown.setVisibility(View.VISIBLE);
                } else {
                    addpost.setVisibility(View.VISIBLE);
                    alreadyShown.setVisibility(View.GONE);
                    addpost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String userID = SharedHelper.getKey(context, AppConstats.USER_ID);
                            Log.e("auywedws", userID);
                            addAdverstisement(userID, data.getId());
                            dialog.dismiss();


                        }
                    });
                }

                Glide.with(context).load(data.getPath() + data.getFilename()).into(dImage);

                String deleteHide = SharedHelper.getKey(context, AppConstats.LOCALS_CLICK_FOR_DELETE);

                btn_play.setVisibility(View.VISIBLE);

                if (deleteHide.equals("DONT_DELETE")) {
                    delete.setVisibility(View.GONE);
                    addpost.setVisibility(View.GONE);
                    editDesc.setVisibility(View.GONE);
                } else {
                    delete.setVisibility(View.VISIBLE);
                    delete.setOnClickListener(view1 -> {

                        deletePost(data.getId());
                        dialog.dismiss();
                    });
                }

                btn_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedHelper.putKey(context, AppConstats.POSTED_VIDEO_PATH, data.getPath());
                        SharedHelper.putKey(context, AppConstats.POSTED_VIDEO_NAME, data.getFilename());
                        context.startActivity(new Intent(context, VideoPlayerActivity.class));
                    }
                });

                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            });
        }


    }


    @Override
    public int getItemCount() {
        return myPostsDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void deletePost(String id) {
        String userID = SharedHelper.getKey(context, AppConstats.USER_ID);
        Map<String, String> params = new HashMap<>();
        params.put("control", "delete_post");
        params.put("userID", userID);
        params.put("id", id);

        Call<DeletePost> call = MyProfileFragment.jsonInterface.deletePost(params);
        call.enqueue(new Callback<DeletePost>() {
            @Override
            public void onResponse(@NonNull Call<DeletePost> call, @NonNull Response<DeletePost> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                }

                DeletePost deletePost = response.body();

                if (deletePost != null) {
                    if (deletePost.getResult()) {
                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new MyProfileFragment())
                                .commit();
                        notifyDataSetChanged();

                        Toast.makeText(context, deletePost.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, deletePost.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<DeletePost> call, @NonNull Throwable t) {
                Log.e("ksazjdxsjx", t.getMessage(), t);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView playBtn, image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            playBtn = itemView.findViewById(R.id.playBtn);
            image = itemView.findViewById(R.id.image);


        }


    }


    public void addAdverstisement(String userID, String postID) {


        Log.e("sjkjdcs", userID);
        Log.e("sjkjdcs", postID);

        Map<String, String> params = new HashMap<>();
        params.put("control", "change_add");
        params.put("userID", userID);
        params.put("addID", postID);
        Call<PostAdvertisement> call = MyProfileFragment.jsonInterface.postAdv(params);
        call.enqueue(new Callback<PostAdvertisement>() {
            @Override
            public void onResponse(@NonNull Call<PostAdvertisement> call, @NonNull Response<PostAdvertisement> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                }

                PostAdvertisement postAdvertisement = response.body();

                if (postAdvertisement != null) {
                    if (postAdvertisement.getResult()) {
                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new MyProfileFragment())
                                .commit();
                        notifyDataSetChanged();
                        Toast.makeText(context, "Advertisement successfully posted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, postAdvertisement.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<PostAdvertisement> call, @NonNull Throwable t) {
                Log.e("oiewueuwe", t.getMessage(), t);
            }
        });
    }


}
