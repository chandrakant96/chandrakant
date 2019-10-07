package com.whr.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.DoctorModel;
import com.whr.user.booking.doctor.booking.models.HospitalModel;

import com.whr.user.model.NewFavouritePojo;


import java.util.List;

public class NewFavouriteAdapter extends RecyclerView.Adapter<NewFavouriteAdapter.VideoInfoHolder> {

    Context ctx;
    LayoutInflater inflater;
    List<NewFavouritePojo> list;
    List<NewFavouritePojo> list1;
    DoctorModel doctorModel;
    HospitalModel hospitalModel;
    public NewFavouriteAdapter(Context context, List<NewFavouritePojo> rowListItem) {
        this.ctx = context;

        this.list = rowListItem;
        this.list1 = rowListItem;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_favourite, parent, false);
        VideoInfoHolder holder = new VideoInfoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {

        if (list.get(position).getType().contains("doctor")) {
            holder.txtName.setText(list.get(position).getName());
            holder.txtEducation.setText(list.get(position).getEducation());
            holder.txtSpecilization.setText(list.get(position).getSpecialization());
            holder.txtExperience.setText(list.get(position).getExperience() + " Yrs Experience");

            if (list.get(position).getProfile_img() != null && !(list.get(position).getProfile_img().isEmpty())) {
                Picasso.with(ctx).load(list.get(position).getProfile_img())
                        .placeholder(R.drawable.ic_doctor_color).error(R.drawable.ic_doctor_color)
                        .fit().into(holder.image);
            } else {
                holder.image.setImageResource(R.drawable.ic_doctor_color);
            }

        } else if (list.get(position).getType().contains("hospital")) {
            holder.txtName.setText(list.get(position).getName());
            holder.txtEducation.setVisibility(View.GONE);
            holder.txtSpecilization.setText(list.get(position).getSpecialization());
            holder.txtExperience.setText(list.get(position).getDoctorcount() + " Doctors");

            if (list.get(position).getProfile_img() != null && !(list.get(position).getProfile_img().isEmpty())) {
                Picasso.with(ctx).load(list.get(position).getProfile_img())
                        .placeholder(R.drawable.ic_hospital).error(R.drawable.ic_hospital)
                        .fit().into(holder.image);
            } else {
                holder.image.setImageResource(R.drawable.ic_hospital);
            }

        } else {
            holder.txtName.setText(list.get(position).getName());
            holder.txtEducation.setVisibility(View.GONE);
            holder.txtSpecilization.setText(list.get(position).getSpecialization());
            holder.txtExperience.setVisibility(View.GONE);

            if (list.get(position).getProfile_img() != null && !(list.get(position).getProfile_img().isEmpty())) {
                Picasso.with(ctx).load(list.get(position).getProfile_img())
                        .placeholder(R.drawable.ic_pathology).error(R.drawable.ic_pathology)
                        .fit().into(holder.image);
            } else {
                holder.image.setImageResource(R.drawable.ic_pathology);
            }
        }


        holder.layoutDetail.setOnClickListener(view -> {
            this.clickLister.doctorDetailClick(list.get(position));
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutDetail;
        ImageView image;
        TextView txtName, txtEducation, txtSpecilization, txtExperience;


        public VideoInfoHolder(View itemView) {
            super(itemView);
            layoutDetail = itemView.findViewById(R.id.layoutDetail);
            image = itemView.findViewById(R.id.image);
            txtName = itemView.findViewById(R.id.txtName);
            txtEducation = itemView.findViewById(R.id.txtEducation);
            txtSpecilization = itemView.findViewById(R.id.txtSpecilization);
            txtExperience = itemView.findViewById(R.id.txtExperience);

        }

    }

    private ClickLister clickLister;

    public void setClickLister(ClickLister clickLister) {
        this.clickLister = clickLister;
    }

    public interface ClickLister {

        void doctorDetailClick(NewFavouritePojo favouritePojo);

    }

}
