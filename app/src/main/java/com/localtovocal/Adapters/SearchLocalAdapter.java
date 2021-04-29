package com.localtovocal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.localtovocal.Activities.LocalDetailsActivity;
import com.localtovocal.Model.LocalData;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class SearchLocalAdapter extends RecyclerView.Adapter<SearchLocalAdapter.ViewHolder> {

    List<LocalData> localDataList;
    Context context;

    OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public SearchLocalAdapter(List<LocalData> localDataList, Context context) {
        this.localDataList = localDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchLocalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.local_layout, parent, false);
        return new SearchLocalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchLocalAdapter.ViewHolder holder, int position) {

        final LocalData data = localDataList.get(position);

        Picasso.get().load(data.getImage()).error(R.drawable.prf_male).into(holder.profile_image);

        if (data.getName().equals("")) {
            holder.name.setText("Name Not available");
        } else {
            holder.name.setText(data.getName());
        }


        if (data.getDescription().equals("")) {

        } else {
            holder.descp.setText(data.getDescription());
        }


        Log.e("ajnskljdklsa", data.getRating()+"sdjs");

        if (data.getStatus().equals("1")) {
            if (!data.getRating().equals("")) {
                double rate = Double.parseDouble(data.getRating());
                holder.rating.setText(new DecimalFormat("##.##").format(rate));

            }else if (data.getRating() != null) {
                double rate = Double.parseDouble(data.getRating());
                holder.rating.setText(new DecimalFormat("##.##").format(rate));
            }
            else {
                holder.rating.setText("0");
            }
        }

        holder.card.setOnClickListener(view -> {

            SharedHelper.putKey(context, AppConstats.LOCALS_USER_ID, data.getId());
            SharedHelper.putKey(context, AppConstats.LOCALS_IMAGE, data.getImage());
            SharedHelper.putKey(context, AppConstats.LOCALS_NUMBER, data.getMobile());
            SharedHelper.putKey(context, AppConstats.LOCALS_USERNAME, data.getName());
            SharedHelper.putKey(context, AppConstats.LOCALS_DISCRETION, data.getDescription());
            SharedHelper.putKey(context, AppConstats.LOCALS_LOCATION, data.getAddress());
            SharedHelper.putKey(context, AppConstats.LOCALS_SHOPNAME, data.getShopName());
            SharedHelper.putKey(context, AppConstats.LOCALS_TAG, data.getTag());
            SharedHelper.putKey(context, AppConstats.LOCALS_LATTITUDE, data.getLattitude());
            SharedHelper.putKey(context, AppConstats.LOCALS_LONGITUDE, data.getLongitude());
            SharedHelper.putKey(context, AppConstats.LOCALS_CLICK_FOR_DELETE, "DONT_DELETE");
            SharedHelper.putKey(context, AppConstats.LOCALS_ALT_MOBILE, data.getAltMobile());

            context.startActivity(new Intent(context, LocalDetailsActivity.class));

        });


    }


    @Override
    public int getItemCount() {
        return localDataList.size();
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


        public ImageView profile_image;
        CardView card;
        TextView name, descp, rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            card = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            descp = itemView.findViewById(R.id.descp);
            rating = itemView.findViewById(R.id.rating);
        }
    }
}

