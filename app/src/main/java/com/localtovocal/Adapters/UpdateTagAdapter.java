package com.localtovocal.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.localtovocal.Model.UpdateTagModel;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateTagAdapter extends RecyclerView.Adapter<UpdateTagAdapter.ViewHolder> {

    List<UpdateTagModel> updateTagModelList;
    Context context;
    int count = 0;

    public UpdateTagAdapter(List<UpdateTagModel> updateTagModelList, Context context) {
        this.updateTagModelList = updateTagModelList;
        this.context = context;
    }


    public void setTags(List<UpdateTagModel> updateTagModelList) {
        this.updateTagModelList = new ArrayList<>();
        this.updateTagModelList = updateTagModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UpdateTagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.tags_layout, parent, false);
        return new UpdateTagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateTagAdapter.ViewHolder holder, int position) {

        UpdateTagModel data = updateTagModelList.get(position);

        holder.tagName.setText(data.getTagName());
        holder.bind(data, position);


        Log.e("dksldks", "dskdls" + position);
    }

    @Override
    public int getItemCount() {
        return updateTagModelList.size();
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


        TextView tagName;
        ImageView imgSelect;
        RelativeLayout relative;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSelect = itemView.findViewById(R.id.imgSelect);
            tagName = itemView.findViewById(R.id.tagName);
            relative = itemView.findViewById(R.id.relative);
        }


        public void bind(UpdateTagModel tagsModel, int position) {


            String getTagID = SharedHelper.getKey(context, AppConstats.TAG_ID);

            Log.e("Dldsjks", getTagID);
            Log.e("trtretret", tagsModel.getTagName());

            String[] arr = getTagID.split(",");

            for (int i = 0; i < arr.length; i++) {

                if (arr[i].equals(tagsModel.getTagID())) {

                    Log.e("ndsjdl", tagsModel.getTagName());
                    tagsModel.setSelected(!tagsModel.isSelected());
                    imgSelect.setVisibility(View.VISIBLE);
                }
            }

            itemView.setOnClickListener(view -> {


                tagsModel.setSelected(!tagsModel.isSelected());
                imgSelect.setVisibility(tagsModel.isSelected() ? View.VISIBLE : View.GONE);


            });
        }


    }

    public List<UpdateTagModel> getAll() {
        return updateTagModelList;
    }

    public List<UpdateTagModel> getSelected() {
        ArrayList<UpdateTagModel> selected = new ArrayList<>();
        for (int i = 0; i < updateTagModelList.size(); i++) {
            if (updateTagModelList.get(i).isSelected()) {
                selected.add(updateTagModelList.get(i));
            }
        }
        return selected;
    }
}

