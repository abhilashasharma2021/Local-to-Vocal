package com.localtovocal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.localtovocal.Model.SubscribePlansData;
import com.localtovocal.Model.TextData;
import com.localtovocal.R;

import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {

    List<TextData> textDataList;
    Context context;

    public TextAdapter(List<TextData> textDataList, Context context) {
        this.textDataList = textDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public TextAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.text_layout, parent, false);
        return new TextAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextAdapter.ViewHolder holder, int position) {

        TextData data = textDataList.get(position);
        holder.txt.setText(data.getText());




    }

    @Override
    public int getItemCount() {
        return textDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt = itemView.findViewById(R.id.txt);

        }
    }
}
