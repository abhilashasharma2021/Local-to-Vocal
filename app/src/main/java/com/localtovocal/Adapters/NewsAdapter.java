package com.localtovocal.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.localtovocal.Activities.LocalDetailsActivity;
import com.localtovocal.Activities.NewsDetailsActivity;
import com.localtovocal.Activities.VideoPlayerActivity;
import com.localtovocal.Model.NewsData;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    List<NewsData> newsDataList;
    Context context;

    public NewsAdapter(List<NewsData> newsDataList, Context context) {
        this.newsDataList = newsDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.news_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {

        NewsData data = newsDataList.get(position);

        if (data.getShowType().equals("1")) {

            if (data.getType() != null) {

                if (data.getType().equals("0")) {  /*for images*/

                    holder.card2.setVisibility(View.VISIBLE);
                    holder.card.setVisibility(View.GONE);
                    Log.e("uiewe", data.getPath() + data.getName());
                    Picasso.get().load(data.getPath() + data.getName()).placeholder(R.drawable.l).into(holder.s_image);


                    holder.info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedHelper.putKey(context, AppConstats.LOCALS_USER_ID, data.getUserID());
                            SharedHelper.putKey(context, AppConstats.LOCALS_IMAGE, data.getImage());
                            SharedHelper.putKey(context, AppConstats.LOCALS_NUMBER, data.getMobileNumber());
                            SharedHelper.putKey(context, AppConstats.LOCALS_USERNAME, data.getUserName());
                            SharedHelper.putKey(context, AppConstats.LOCALS_DISCRETION, data.getDescription());
                            SharedHelper.putKey(context, AppConstats.LOCALS_LOCATION, data.getAddress());
                            SharedHelper.putKey(context, AppConstats.LOCALS_SHOPNAME, data.getShopName());
                            SharedHelper.putKey(context, AppConstats.LOCALS_LATTITUDE, data.getLatitude());
                            SharedHelper.putKey(context, AppConstats.LOCALS_LONGITUDE, data.getLongitude());
                            SharedHelper.putKey(context, AppConstats.LOCALS_CLICK_FOR_DELETE, "DONT_DELETE");
                            SharedHelper.putKey(context, AppConstats.LOCALS_ALT_MOBILE, data.getAlternateMobileNumber());
                            context.startActivity(new Intent(context, LocalDetailsActivity.class));
                        }
                    });

                    holder.card2.setOnClickListener(view -> {
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.zoom_img);

                        ImageView dImage = dialog.findViewById(R.id.dImage);
                        ImageView btn_play = dialog.findViewById(R.id.btn_play);
                        btn_play.setVisibility(View.GONE);

                        Picasso.get().load(data.getPath() + data.getName()).placeholder(R.drawable.l).into(dImage);
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    });


                } else {/*for Videos*/
                    holder.card2.setVisibility(View.VISIBLE);
                    holder.card.setVisibility(View.GONE);
                    holder.play.setVisibility(View.VISIBLE);


                    holder.info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedHelper.putKey(context, AppConstats.LOCALS_USER_ID, data.getUserID());
                            SharedHelper.putKey(context, AppConstats.LOCALS_IMAGE, data.getImage());
                            SharedHelper.putKey(context, AppConstats.LOCALS_NUMBER, data.getMobileNumber());
                            SharedHelper.putKey(context, AppConstats.LOCALS_USERNAME, data.getUserName());
                            SharedHelper.putKey(context, AppConstats.LOCALS_DISCRETION, data.getDescription());
                            SharedHelper.putKey(context, AppConstats.LOCALS_LOCATION, data.getAddress());
                            SharedHelper.putKey(context, AppConstats.LOCALS_SHOPNAME, data.getShopName());
                            SharedHelper.putKey(context, AppConstats.LOCALS_LATTITUDE, data.getLatitude());
                            SharedHelper.putKey(context, AppConstats.LOCALS_LONGITUDE, data.getLongitude());
                            SharedHelper.putKey(context, AppConstats.LOCALS_CLICK_FOR_DELETE, "DONT_DELETE");
                            SharedHelper.putKey(context, AppConstats.LOCALS_ALT_MOBILE, data.getAlternateMobileNumber());
                            context.startActivity(new Intent(context, LocalDetailsActivity.class));
                        }
                    });

                    try {

                        Glide.with(context).load(data.getPath() + data.getName()).into(holder.s_image);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("sndkjs", e.getMessage(), e);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    holder.card2.setOnClickListener(view -> {
                        SharedHelper.putKey(context, AppConstats.POSTED_VIDEO_PATH, data.getPath());
                        SharedHelper.putKey(context, AppConstats.POSTED_VIDEO_NAME, data.getName());
                        context.startActivity(new Intent(context, VideoPlayerActivity.class));
                    });


                }
            }


        } else {

            /*for news*/
            holder.card2.setVisibility(View.GONE);
            holder.card.setVisibility(View.VISIBLE);
            Picasso.get().load(data.getImage()).placeholder(R.drawable.l).into(holder.image);

            if (data.getDescription().length() > 150) {

                holder.description.setText(data.getDescription().substring(0, 150) + "....");

            } else {
                holder.description.setText(data.getDescription());
            }

            holder.date.setText(data.getDate());
            holder.title.setText(data.getNewstitle());

            holder.card.setOnClickListener(view -> {
                SharedHelper.putKey(context, AppConstats.NEWS_HEADING, data.getNewstitle());
                SharedHelper.putKey(context, AppConstats.NEWS_IMAGE, data.getImage());
                SharedHelper.putKey(context, AppConstats.NEWS_DESCRIPTION, data.getDescription());
                context.startActivity(new Intent(context, NewsDetailsActivity.class));
            });
        }




    }


    @Override
    public int getItemCount() {
        return newsDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView description, date, title;
        ImageView image, s_image, play,info;
        CardView card, card2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);
            s_image = itemView.findViewById(R.id.s_image);
            card = itemView.findViewById(R.id.card);
            card2 = itemView.findViewById(R.id.card2);
            play = itemView.findViewById(R.id.play);
            title = itemView.findViewById(R.id.title);
            info = itemView.findViewById(R.id.info);

        }
    }
}
