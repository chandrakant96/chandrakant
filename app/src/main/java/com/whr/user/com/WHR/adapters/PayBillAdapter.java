package com.whr.user.com.WHR.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.pojo.PayBillPojo;

import java.util.List;

/**
 * Created by lenovo on 6/9/2017.
 */

public class PayBillAdapter extends RecyclerView.Adapter<PayBillAdapter.PaybillViewHolder> {
    private final LayoutInflater inflater;
    private Context context;
    private List<PayBillPojo.TreatmentList> list;

    public PayBillAdapter(Context context, List<PayBillPojo.TreatmentList> rowListItem) {
        this.context = context;
        this.list = rowListItem;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PaybillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_doctor_booking_appoinment, parent, false);
        //View view = inflater.inflate(R.layout.pay_bill_adapter_row, parent, false);

        PaybillViewHolder holder = new PaybillViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PaybillViewHolder holder, int position) {
        holder.treatmentName.setText(list.get(holder.getLayoutPosition()).getTname());
        holder.treatmentName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.treatmentName.setChecked(true);
            }
        });
        holder.txtActualPrice.setText("₹." + list.get(holder.getLayoutPosition()).getDiscount_price());
        holder.txtDescription.setText("" + list.get(holder.getLayoutPosition()).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PaybillViewHolder extends RecyclerView.ViewHolder {
        CheckBox treatmentName;
        TextView txtDescription, txtActualPrice, txtDiscountPrice;
        RelativeLayout relativeItem;

        PaybillViewHolder(View itemView) {
            super(itemView);
            treatmentName = itemView.findViewById(R.id.treatmentName);
            treatmentName.setChecked(true);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtActualPrice = itemView.findViewById(R.id.txtActualPrice);
            txtDiscountPrice = itemView.findViewById(R.id.txtDiscountPrice);
            txtDiscountPrice.setVisibility(View.GONE);
            relativeItem = itemView.findViewById(R.id.relativeItem);
        }
    }
}