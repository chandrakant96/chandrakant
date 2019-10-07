package com.whr.user.com.WHR.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.pojo.DoctorTreatmentPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 9/11/2017.
 */

public class FollowupBookingAdapter extends RecyclerView.Adapter<FollowupBookingAdapter.MyviewHolder> {

    Context context;
    LayoutInflater inflater;
    private List<DoctorTreatmentPojo> list;
    private List<DoctorTreatmentPojo> totalChargeList;
    private AppCompatActivity activity;
    private AlertDialog removeAlert;

    public FollowupBookingAdapter(Context context, List<DoctorTreatmentPojo> list, AppCompatActivity activity) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.activity = activity;
        totalChargeList = new ArrayList<>();
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.treatment_charge_check_box_row, parent, false);
        MyviewHolder holder = new MyviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, int position) {
        holder.Tretment_name.setText(list.get(position).getTreatmentname());
        holder.fee.setText("₹. " + list.get(position).getDiscount_price() + "");
        holder.actual_price.setText("₹. " + list.get(position).getFee() + "");
        holder.check_box.setChecked(true);
        holder.check_box.setEnabled(false);
        if (list.get(position).getWhrdiscount() == 0) {
            holder.discount.setVisibility(View.GONE);
        } else {
            holder.discount.setText("Discount " + list.get(position).getWhrdiscount() + "%");
        }

        if (list.get(position).getWhrdiscount() > 0) {
            holder.actual_price.setPaintFlags(holder.actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (list.get(position).getWhrdiscount() == 0) {
            holder.actual_price.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyviewHolder extends RecyclerView.ViewHolder {
        ImageView Treatment_image;
        TextView Tretment_name, fee, discount_price, discount, actual_price, whrdiscount;
        CheckBox check_box;

        public MyviewHolder(View itemView) {
            super(itemView);
            Tretment_name = (TextView) itemView.findViewById(R.id.treatment_checkboxbookText);
            fee = (TextView) itemView.findViewById(R.id.textView5);
            actual_price = (TextView) itemView.findViewById(R.id.actual_price);
            discount = (TextView) itemView.findViewById(R.id.discount);
            whrdiscount = (TextView) itemView.findViewById(R.id.whrdiscount);
            check_box = (CheckBox) itemView.findViewById(R.id.checkBox4);
        }
    }


    public List<DoctorTreatmentPojo> getTotalList() {
        return list;
    }
}
