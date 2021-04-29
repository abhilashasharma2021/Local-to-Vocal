package com.localtovocal.Adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.localtovocal.Model.TagsModel;
import com.localtovocal.R;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> implements Filterable {

    List<TagsModel> tagsModelList;
    List<TagsModel> tagsModelListFull;
    Context context;


    public TagsAdapter(List<TagsModel> tagsModelList, Context context) {
        this.tagsModelList = tagsModelList;
        tagsModelListFull = new ArrayList<>(tagsModelList);
        this.context = context;

    }


    public void setTags(List<TagsModel> tagsModelList) {
        this.tagsModelList = new ArrayList<>();
        this.tagsModelList = tagsModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.tags_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsAdapter.ViewHolder holder, int position) {

        TagsModel data = tagsModelList.get(position);
        holder.tagName.setText(data.getTagName());
        holder.bind(tagsModelList.get(position));


    }

    @Override
    public int getItemCount() {
        return tagsModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return tagFilter;
    }

    private final Filter tagFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TagsModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(tagsModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TagsModel item : tagsModelListFull) {
                    if (item.getTagName().toLowerCase().contains(filterPattern)) {

                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tagsModelList.clear();
            tagsModelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView tagName;
        public ImageView imgSelect;
        RelativeLayout relative;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSelect = itemView.findViewById(R.id.imgSelect);
            tagName = itemView.findViewById(R.id.tagName);
            relative = itemView.findViewById(R.id.relative);
        }


        public void bind(TagsModel tagsModel) {

            itemView.setOnClickListener(view -> {

                tagsModel.setSelected(!tagsModel.isSelected());
                imgSelect.setVisibility(tagsModel.isSelected() ? View.VISIBLE : View.GONE);

            });
        }


    }

    public List<TagsModel> getAll() {
        return tagsModelList;
    }

    public List<TagsModel> getSelected() {
        ArrayList<TagsModel> selected = new ArrayList<>();
        for (int i = 0; i < tagsModelList.size(); i++) {
            if (tagsModelList.get(i).isSelected()) {
                selected.add(tagsModelList.get(i));
            }
        }
        return selected;
    }
}
