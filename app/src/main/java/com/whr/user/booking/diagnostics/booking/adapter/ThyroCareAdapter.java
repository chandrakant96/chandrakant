package com.whr.user.booking.diagnostics.booking.adapter;


import android.content.Context;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.booking.diagnostics.booking.model.ThyroCarePojo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.whr.user.booking.diagnostics.booking.activities.ThyrocareListActivity.txtTotal;


public class ThyroCareAdapter extends RecyclerView.Adapter<ThyroCareAdapter.MyViewHolder> {
    private List<ThyroCarePojo> rowListItem;
    private Context context;
    private String value;
    private List<ThyroCarePojo> filteredList;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    

    public static double totalVal = 0.0f;
    DecimalFormat df = new DecimalFormat("#.##");

    public ThyroCareAdapter(Context context, List<ThyroCarePojo> rowListItem, String value, AppCompatActivity activity) {
        this.rowListItem = rowListItem;
        this.value = value;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.filteredList = rowListItem;
        this.activity = activity;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.treatment_charge_check_box_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtDiscountPrice.setText("₹. " + filteredList.get(position).getPay_amt());
        holder.treatmentName.setText(filteredList.get(position).getName());
        holder.treatmentName.setChecked(filteredList.get(position).isChecked());
        if (filteredList.get(position).getType().equals("OFFER")) {
            holder.txtActualPrice.setPaintFlags(holder.txtActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtActualPrice.setText("₹. " + filteredList.get(position).getB2c() + "");
        } else {
            holder.txtActualPrice.setVisibility(View.GONE);
        }


        if (filteredList.get(position).isChecked()) {
            holder.treatmentName.setChecked(true);
        } else {
            holder.treatmentName.setChecked(false);
        }

        holder.treatmentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filteredList.get(position).getType().equals("OFFER")) {
                    if (!filteredList.get(position).isChecked()) {
                        setAllToUncheck();
                        filteredList.get(position).setChecked(true);
                        totalVal += Double.parseDouble(filteredList.get(position).getPay_amt() + " ");
                    } else {
                        totalVal -= Double.parseDouble(filteredList.get(position).getPay_amt() + " ");
                        filteredList.get(position).setChecked(false);
                    }
                } else {
                    if (!filteredList.get(position).isChecked()) {
                        filteredList.get(position).setChecked(true);
                        totalVal += Double.parseDouble(filteredList.get(position).getPay_amt() + " ");
                    } else {
                        totalVal -= Double.parseDouble(filteredList.get(position).getPay_amt() + " ");
                        filteredList.get(position).setChecked(false);
                    }
                }
                txtTotal.setText(String.valueOf(totalVal));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemCount() {
        return filteredList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox treatmentName;
        TextView txtActualPrice, txtDiscountPrice, fastingType;

        public MyViewHolder(View view) {
            super(view);
            treatmentName = itemView.findViewById(R.id.treatmentName);
            txtActualPrice = itemView.findViewById(R.id.txtActualPrice);
            txtDiscountPrice = itemView.findViewById(R.id.txtDiscountPrice);
            fastingType = itemView.findViewById(R.id.fastingType);
      
        

        }
    }

    public List<ThyroCarePojo> getTotaTreatmentlList() {
        return filteredList;
    }

    private int getSelectedListSize() {
        int count = 0;
        for (ThyroCarePojo pojo : filteredList) {
            if (pojo.isChecked()) {
                count++;
            }
        }
        return count;
    }


    private void setAllToUncheck() {
        for (ThyroCarePojo pojo : filteredList) {
            if (pojo.isChecked()) {
                totalVal = totalVal - Double.parseDouble(pojo.getPay_amt());
            }
            pojo.setChecked(false);
        }
    }


    public void getFilterList(String searchkey) {
        filteredList = new ArrayList<>();
        for (ThyroCarePojo pojo : rowListItem) {
            if (pojo.getName().trim().toLowerCase().contains(searchkey)) {
                filteredList.add(pojo);
            }
        }
        notifyDataSetChanged();
    }


}