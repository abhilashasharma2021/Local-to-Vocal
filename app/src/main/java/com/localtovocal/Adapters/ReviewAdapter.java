package com.localtovocal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.localtovocal.Model.ReviewData;
import com.localtovocal.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    List<ReviewData> reviewDataList;
    Context context;


    public ReviewAdapter(List<ReviewData> reviewDataList, Context context) {
        this.reviewDataList = reviewDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.locals_reviews_layout, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {

        final ReviewData data = reviewDataList.get(position);

        holder.description.setText(data.getDescription());
        String rate = data.getRate();
        holder.rating.setRating(Float.parseFloat(rate));

    }


    @Override
    public int getItemCount() {
        return reviewDataList.size();
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

        ImageView image;
        CardView card;
        TextView name, description;
        RatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            card = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            rating = itemView.findViewById(R.id.rating);

        }
    }
}

