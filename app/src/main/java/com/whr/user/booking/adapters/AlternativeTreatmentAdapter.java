package com.whr.user.booking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.whr.user.R;
import com.whr.user.booking.models.AlternativeTreatment;

import java.util.ArrayList;


public class AlternativeTreatmentAdapter extends RecyclerView.Adapter<AlternativeTreatmentAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    private ArrayList<AlternativeTreatment> list;


    public AlternativeTreatmentAdapter(Context context, ArrayList<AlternativeTreatment> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public AlternativeTreatmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_filter_alternative_treatment, parent, false);
        return new AlternativeTreatmentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlternativeTreatmentAdapter.MyViewHolder holder, final int position) {
        holder.txtAlternativeTreatment.setText(list.get(position).getType());
        if (list.get(position).isCheck()) {
            holder.txtAlternativeTreatment.setChecked(true);
        } else {
            holder.txtAlternativeTreatment.setChecked(false);
        }
        holder.txtAlternativeTreatment.setOnClickListener(view -> {

            for (AlternativeTreatment treatment : list) {
                treatment.setCheck(false);
            }
            onClickListener.onClick(list.get(holder.getLayoutPosition()));
        });

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

        RadioButton txtAlternativeTreatment;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtAlternativeTreatment = itemView.findViewById(R.id.txtAlternativeTreatment);
        }
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(AlternativeTreatment treatment);
    }
}

