package com.whr.user.com.WHR.SuggestTestPackages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.pojo.Suggested_TreatementPojo;

import java.util.List;

/**
 * Created by lenovo on 2/16/2017.
 */


public class SuggestTreatmentListByDoctorFragmentAdapter extends RecyclerView.Adapter<SuggestTreatmentListByDoctorFragmentAdapter.Suggested_TreatementAdapterViewHolder> {

    Context context;
    List<Suggested_TreatementPojo> list;
    LayoutInflater inflater;

    public SuggestTreatmentListByDoctorFragmentAdapter(Context context, List<Suggested_TreatementPojo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Suggested_TreatementAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      //  View view1 = inflater.inflate(R.layout.suggested_treatement_adapter_row, parent, false);
        View view = inflater.inflate(R.layout.row_doctor_booking_appoinment, parent, false);
        Suggested_TreatementAdapterViewHolder holder = new Suggested_TreatementAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Suggested_TreatementAdapterViewHolder holder, final int position) {
        holder.txtDescription.setText("(" + list.get(position).getDate() + ")");
        holder.treatmentName.setText(list.get(position).getTname());
        holder.treatmentName.setChecked(list.get(position).isChecked());
        holder.treatmentName.setTag(list.get(position));
        holder.treatmentName.setChecked(list.get(holder.getLayoutPosition()).isCheck());
        holder.relativeItem.setOnClickListener(v -> {
            this.onClickListener.onClick(list.get(holder.getLayoutPosition()));
        });


        holder.treatmentName.setOnClickListener(v -> {
            this.onClickListener.onClick(list.get(holder.getLayoutPosition()));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Suggested_TreatementAdapterViewHolder extends RecyclerView.ViewHolder {
        CheckBox treatmentName;
        TextView txtDescription;
        LinearLayout layout2;
        RelativeLayout relativeItem;

        public Suggested_TreatementAdapterViewHolder(View itemView) {
            super(itemView);
            layout2 = itemView.findViewById(R.id.layout2);
            layout2.setVisibility(View.GONE);
            treatmentName = itemView.findViewById(R.id.treatmentName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            relativeItem = itemView.findViewById(R.id.relativeItem);
        }
    }

    public List<Suggested_TreatementPojo> getTotaTreatmentlList() {
        return list;
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(Suggested_TreatementPojo d);
    }
}