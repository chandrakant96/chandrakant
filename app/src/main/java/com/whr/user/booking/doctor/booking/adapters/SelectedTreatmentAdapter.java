package com.whr.user.booking.doctor.booking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.pojo.DoctorTreatmentPojo;

import java.util.ArrayList;
import java.util.List;

public class SelectedTreatmentAdapter extends RecyclerView.Adapter<SelectedTreatmentAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    private List<DoctorTreatmentPojo> list;
    private List<DoctorTreatmentPojo> mFilterList;
    private List<DoctorTreatmentPojo> totalChargeList;
    private AppCompatActivity activity;
    private AlertDialog removeAlert;
    private boolean isChecked;


    public SelectedTreatmentAdapter(Context context, List<DoctorTreatmentPojo> list, AppCompatActivity activity) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.mFilterList = list;
        this.activity = activity;
        totalChargeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_treatment_selected, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.txtSelectTreatment.setText(list.get(position).getTreatmentname());
        holder.txtSelectTreatment.setOnClickListener(v -> this.onClickListener.onClick(list.get(position)));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtSelectTreatment;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtSelectTreatment = itemView.findViewById(R.id.txtSelectTreatment);
        }
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(DoctorTreatmentPojo d);
    }

    public void getFilterList(String searchKey) {
        mFilterList = new ArrayList<>();
        for (DoctorTreatmentPojo doctorTreatmentPojo : list) {
            if (doctorTreatmentPojo.getTreatmentname().toLowerCase().trim().contains(searchKey)) {
                doctorTreatmentPojo.setCheck(doctorTreatmentPojo.isCheck());
                mFilterList.add(doctorTreatmentPojo);
            }
        }
        notifyDataSetChanged();
    }
}
