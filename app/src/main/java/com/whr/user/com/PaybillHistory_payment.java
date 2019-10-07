package com.whr.user.com;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.pojo.PayBillPojo;

import java.util.List;

/**
 * Created by lenovo on 7/28/2017.
 */

public class PaybillHistory_payment extends RecyclerView.Adapter<PaybillHistory_payment.PaybillViewHolder> {

    private final LayoutInflater inflater;
    private Context context;
    private List<PayBillPojo.TreatmentList> list;


    public PaybillHistory_payment(Context context, List<PayBillPojo.TreatmentList> rowListItem) {
        this.context = context;
        this.list = rowListItem;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PaybillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pay_bill_adapter_row, parent, false);
        PaybillViewHolder holder = new PaybillViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(PaybillViewHolder holder, int position) {

        holder.tname.setText(list.get(holder.getLayoutPosition()).getTname());
        holder.fee.setText("₹." + list.get(holder.getLayoutPosition()).getDiscount_price());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class PaybillViewHolder extends RecyclerView.ViewHolder {

        TextView tname, fee;

        public PaybillViewHolder(View itemView) {
            super(itemView);
            tname = (TextView) itemView.findViewById(R.id.treatmentNameTrxt);
            fee = (TextView) itemView.findViewById(R.id.adapttreatmentprice);
        }


    }

}
