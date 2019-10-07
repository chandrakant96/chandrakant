package com.whr.user.com.WHR.SuggestTestPackages;

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

import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.com.SuggesatNewActivityPackage.SuggestTreatmentListByDoctorActivity;
import com.whr.user.pay_bill.SuggestDoctorDetailsList;
import com.whr.user.treatement_suggested.fragments.SuggestTrearmentFragment;

import java.util.List;

public class SuggestTreatmentAdpter extends RecyclerView.Adapter<SuggestTreatmentAdpter.DoctorListViewHolder> {
    private final LayoutInflater inflater;
    private List<SuggestDoctorDetailsList> rowListItem;
    private Context context;
    private SuggestTrearmentFragment suggestTestFrgment;
    private long familyid;
    private String suggestValue;
    private String familyname;

    public SuggestTreatmentAdpter(Context context, List<SuggestDoctorDetailsList> rowListItem, SuggestTrearmentFragment suggestTestFrgment, long familyid, String suggestValue, String familyname
    ) {
        this.suggestTestFrgment = suggestTestFrgment;
        this.context = context;
        this.rowListItem = rowListItem;
        inflater = LayoutInflater.from(context);
        this.familyid = familyid;
        this.suggestValue = suggestValue;
        this.familyname = familyname;
    }

    @Override
    public DoctorListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.doctor_list_adapter_row, parent, false);
        DoctorListViewHolder holder = new DoctorListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DoctorListViewHolder holder, final int position) {
        holder.suggestedDoctorName.setText(rowListItem.get(position).getDoctorName());
        holder.suggestedDoctorAddress.setText(rowListItem.get(position).getDoctorAddress());
        holder.suggestedDate.setText(rowListItem.get(holder.getLayoutPosition()).getSuggestedDate() + "");

        if (rowListItem.get(position).getProfile_img() != null) {
            if (!rowListItem.get(position).getProfile_img().isEmpty()) {
                Picasso.with(context).load(rowListItem.get(position).getProfile_img()).placeholder(R.drawable.user_blue).error(R.drawable.user_blue).into(holder.imageProfile);
            }
        }

        if (rowListItem.get(position).getCname() != null) {
            if (rowListItem.get(position).getCname().length() > 0) {
                holder.cname.setVisibility(View.VISIBLE);
                holder.cname.setText(rowListItem.get(position).getCname() + "");
            }
        }


        holder.mainLayout.setOnClickListener(v -> {
            Intent mIntent = new Intent(suggestTestFrgment.getActivity(), SuggestTreatmentListByDoctorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("doctorId", String.valueOf(rowListItem.get(position).getDoctorId()));
            bundle.putString("did", String.valueOf(rowListItem.get(position).getDoctorId()));
            bundle.putLong("familyid", familyid);
            bundle.putString("familyname", familyname);
            bundle.putString("suggest", suggestValue);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            context.startActivity(mIntent);


        });
    }

    @Override
    public int getItemCount() {
        return rowListItem.size();
    }

    class DoctorListViewHolder extends RecyclerView.ViewHolder {
        private TextView suggestedDoctorAddress, suggestedDoctorName, suggestedDate, cname;
        private LinearLayout mainLayout;
        ImageView imageProfile;

        public DoctorListViewHolder(View itemView) {
            super(itemView);
            suggestedDoctorName = itemView.findViewById(R.id.suggestedDoctorName);
            suggestedDoctorAddress = itemView.findViewById(R.id.suggestedDoctorAddress);
            cname = itemView.findViewById(R.id.cname);
            suggestedDate = itemView.findViewById(R.id.suggestedDate);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            imageProfile = itemView.findViewById(R.id.imageProfile);
        }
    }
}
