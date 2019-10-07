package com.whr.user.com.WHR.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.com.WHR.Activities.PrescriptionViewActivity;
import com.whr.user.pojo.DoctorListForPresPojo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lenovo on 7/19/2017.
 */

public class PrescriptionHistoryListAdapter extends RecyclerView.Adapter<PrescriptionHistoryListAdapter.MyView> {

    private final LayoutInflater inflater;
    private Context context;
    private List<DoctorListForPresPojo> rowListItem;
    private int docterId;
    long familyid;

    private PreferenceUtils pref;

    public PrescriptionHistoryListAdapter(Context ctx, List<DoctorListForPresPojo> rowListItem, int docterId, long familyid) {
        this.context = ctx;
        this.rowListItem = rowListItem;
        inflater = LayoutInflater.from(context);
        this.docterId = docterId;
        this.familyid = familyid;
        pref = new PreferenceUtils(context);
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.prescriptionimage_history_row, parent, false);
        MyView holder = new MyView(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyView holder, final int position) {
        DoctorListForPresPojo pojo = rowListItem.get(position);
        holder.presPDate.setText(context.getString(R.string.Date) + " : " + pojo.getPriscriptionDate() + "");
        holder.prespName.setText(pojo.getDoctorName() + "");

        String profile_img = rowListItem.get(holder.getLayoutPosition()).getDoc_profileImage();
        if (profile_img != null) {
            if (profile_img.length() > 0) {
                String str = profile_img.replace("\\", "");
                if (str != null) {
                    Picasso.with(context).load(str).fit().into(holder.doctoImage);
                }
            }
        }

        holder.iteamClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context.getApplicationContext(), PrescriptionViewActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("doctorId", rowListItem.get(position).getDoctorId());
                mBundle.putInt("report_id", rowListItem.get(position).getPriscriptionId());
                mBundle.putString("patientid", String.valueOf(familyid));
                mBundle.putString("uname", pref.getUserName() + "");
                mBundle.putString("date", rowListItem.get(position).getPriscriptionDate());
                mBundle.putBoolean("isPresHistory", true);
                mIntent.putExtras(mBundle);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                context.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rowListItem.size();
    }

    class MyView extends RecyclerView.ViewHolder {
        TextView prespName, presPDate;
        ImageView doctoImage;
        LinearLayout iteamClick;

        public MyView(View itemView) {
            super(itemView);
            doctoImage = (ImageView) itemView.findViewById(R.id.doctor_specification_imageView);
            prespName = (TextView) itemView.findViewById(R.id.doctor_specilization_main_text);
            presPDate = (TextView) itemView.findViewById(R.id.doctor_sub_specilization_text);
            iteamClick = (LinearLayout) itemView.findViewById(R.id.iteamClick);
        }
    }
}