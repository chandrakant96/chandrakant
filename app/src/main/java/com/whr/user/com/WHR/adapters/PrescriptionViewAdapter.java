package com.whr.user.com.WHR.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.pojo.PrescriptioPojo;

import java.util.List;

/**
 * Created by lenovo on 6/14/2017.
 */

public class PrescriptionViewAdapter extends RecyclerView.Adapter<PrescriptionViewAdapter.MyViewHolder> {
    private Context context;
    private List<PrescriptioPojo> list;
    private LayoutInflater inflater;

    public PrescriptionViewAdapter(Context context, List<PrescriptioPojo> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.prescription_adapter_layout_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.medicineName.setText(list.get(holder.getLayoutPosition()).getMedicineName());
        holder.moringText.append("\n" + list.get(holder.getLayoutPosition()).getMorniingQty() + "");
        holder.afternoonText.append("\n" + list.get(holder.getLayoutPosition()).getAfternoonQty() + "");
        holder.nightText.append("\n" + list.get(holder.getLayoutPosition()).getNightQty() + "");
        holder.qtytext.append(" " + list.get(holder.getLayoutPosition()).getQuantity() + "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName, moringText, afternoonText, nightText, qtytext;
        ImageView closeButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            closeButton = (ImageView) itemView.findViewById(R.id.closeButton);
            medicineName = (TextView) itemView.findViewById(R.id.medicineBox);
            moringText = (TextView) itemView.findViewById(R.id.Morning);
            afternoonText = (TextView) itemView.findViewById(R.id.Afternoon);
            nightText = (TextView) itemView.findViewById(R.id.Night);
            qtytext = (TextView) itemView.findViewById(R.id.qty);
        }
    }
}