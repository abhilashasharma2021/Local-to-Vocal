package com.localtovocal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.localtovocal.Activities.LoginActivity;
import com.localtovocal.Model.SubscribePlansData;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.Paytm.CallPaytmGateway;
import com.localtovocal.R;

import java.util.List;

public class SubscribePlansAdapter extends RecyclerView.Adapter<SubscribePlansAdapter.ViewHolder> {

    List<SubscribePlansData> subscribePlansDataList;
    Context context;

    public SubscribePlansAdapter(List<SubscribePlansData> subscribePlansDataList, Context context) {
        this.subscribePlansDataList = subscribePlansDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public SubscribePlansAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.plans_layout, parent, false);
        return new SubscribePlansAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribePlansAdapter.ViewHolder holder, int position) {

        SubscribePlansData data = subscribePlansDataList.get(position);

        if (data.getMonth().equals("1")) {
            holder.validity.setText(data.getMonth() + " month - ");
            holder.plan.setText(data.getMonth() + " Month Plan");
        } else {
            holder.validity.setText(data.getMonth() + " months - ");
            holder.plan.setText(data.getMonth() + " Months Plan");
        }




        holder.price.setText(data.getPrice() + " Rs");


        holder.rel_plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userID = SharedHelper.getKey(context, AppConstats.USER_ID);

                if (userID.equals("")) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                    Toast.makeText(context, "Please login to proceed", Toast.LENGTH_SHORT).show();
                } else {

                    SharedHelper.putKey(context, AppConstats.SUBSCRIBE_PLAN_ID, data.getPlanID());
                    SharedHelper.putKey(context, AppConstats.SUBSCRIBE_PLAN_AMOUNT, data.getPrice());
                    context.startActivity(new Intent(context, CallPaytmGateway.class));
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return subscribePlansDataList.size();
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


        TextView validity, price,plan;
        RelativeLayout rel_plans;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            price = itemView.findViewById(R.id.price);
            validity = itemView.findViewById(R.id.validity);
            rel_plans = itemView.findViewById(R.id.rel_plans);
            plan = itemView.findViewById(R.id.plan);

        }
    }
}
