package com.whr.user.com.WHR.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.pojo.ShowDoctorListFromHospitalActivityPojo;

import java.util.List;

/**
 * Created by lenovo on 11/26/2016.
 */

public class ShowDoctorListFromHospitalAdapter extends RecyclerView.Adapter<ShowDoctorListFromHospitalAdapter.MyViewHolder> {
    private List<ShowDoctorListFromHospitalActivityPojo> list;
    private Context context;
    private LayoutInflater inflater;
    private int hospitaId;
    private String hospitalName;

    public ShowDoctorListFromHospitalAdapter(Context context, List<ShowDoctorListFromHospitalActivityPojo> rowListItem, int hospitaId, String hospitalName
    ) {
        this.context = context;
        this.list = rowListItem;
        inflater = LayoutInflater.from(context);
        this.hospitaId = hospitaId;
        this.hospitalName = hospitalName;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.doctor_list_in_hospital_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
//        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        ShowDoctorListFromHospitalActivityPojo pojo = list.get(position);

        if (!list.get(position).getProfile_img().isEmpty()) {
            Picasso.with(context).load(list.get(position).getProfile_img()).placeholder(R.drawable.ctg_doctor)
                    .fit().error(R.drawable.ctg_doctor).into(holder.doctoImage);
        } else {
            Picasso.with(context).load(R.drawable.ctg_doctor).placeholder(R.drawable.ctg_doctor)
                    .fit().error(R.drawable.ctg_doctor).into(holder.doctoImage);
        }

        holder.doctorName.setText(pojo.getDname());
        holder.special.setText(pojo.getSpecial());

        if (pojo.getEducation().isEmpty()) {
            holder.Education.setVisibility(View.GONE);
        } else {
            holder.Education.setVisibility(View.VISIBLE);
            holder.Education.setText(context.getString(R.string.Education) + " : " + pojo.getEducation());
        }

        if (pojo.getExperience().isEmpty()) {
            holder.Experience.setVisibility(View.GONE);
        } else {
            holder.Experience.setVisibility(View.VISIBLE);
            holder.Experience.setText(context.getString(R.string.Experience) + " : " + pojo.getExperience());
        }

        holder.bookAppoentmentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName, doctor_sub_specilization_text;
        ImageView doctoImage;
        TextView bookAppoentmentbtn;
        TextView special, Education, Experience;

        public MyViewHolder(View itemView) {
            super(itemView);
            doctoImage = (ImageView) itemView.findViewById(R.id.doctor_specification_imageView);
            doctorName = (TextView) itemView.findViewById(R.id.doctor_specilization_main_text);
            special = (TextView) itemView.findViewById(R.id.special);
            Education = (TextView) itemView.findViewById(R.id.Education);
            Experience = (TextView) itemView.findViewById(R.id.Experience);
            bookAppoentmentbtn = itemView.findViewById(R.id.bookAppoentmentbtn);
        }
    }
}
