package com.whr.user.booking.doctor.booking.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.HospitalProfile;
import com.whr.user.booking.doctor.booking.models.NewDoctorProfile;

import java.util.ArrayList;
import java.util.List;


public class HospitalReviewAdapter extends RecyclerView.Adapter<HospitalReviewAdapter.MyViewHolder> {

    List<HospitalProfile.Reviews> reviewsArrayList;
    Context context;

    public HospitalReviewAdapter(List<HospitalProfile.Reviews> reviewsArrayList, Context context) {
        this.reviewsArrayList = reviewsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_user_review, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        HospitalProfile.Reviews reviews = reviewsArrayList.get(position);
        holder.txtUserName.setText(reviews.getUsername());
        holder.txtReviews.setText(reviews.getUserreview());
    }

    @Override
    public int getItemCount() {
        return reviewsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtUserName, txtReviews;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtReviews = itemView.findViewById(R.id.txtReviews);
        }
    }
}
