package com.whr.user.pay_bill.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.activities.ReceiptActivity;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pay_bill.PayBillHistoryPojo;

import java.util.List;

public class PayBillHistoryAdapter extends RecyclerView.Adapter<PayBillHistoryAdapter.PayHistoryHolder> {

    private List<PayBillHistoryPojo> list;
    private Context context;
    private LayoutInflater inflater;
    private long familyid;
    private String familyname = "";
    String TAG = getClass().getSimpleName();

    public PayBillHistoryAdapter(Context context, List<PayBillHistoryPojo> list, long familyid, String familyname) {
        this.list = list;
        this.familyid = familyid;
        this.familyname = familyname;
        this.context = context;
        inflater = LayoutInflater.from(context);
        GlobalVar.errorLog(TAG, "adapter", TAG);
    }

    @Override
    public PayHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pay_bill_hisstory_row, parent, false);
        PayHistoryHolder holder = new PayHistoryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PayHistoryHolder holder, final int position) {

        holder.name_textView.setText(list.get(holder.getLayoutPosition()).getName() + "");
        holder.addresstextView.setText(list.get(holder.getLayoutPosition()).getAddress());
        holder.typeTextview.setText(list.get(holder.getLayoutPosition()).getType() + "");

        if (list.get(holder.getLayoutPosition()).getAppointmentdate() != null) {
            if (list.get(holder.getLayoutPosition()).getAppointmentdate().length() > 0) {
                if (list.get(holder.getLayoutPosition()).getStatus() == "3") {
                    holder.appoinmentdattxt.setVisibility(View.GONE);
                } else {
                    holder.appoinmentdattxt.setText(context.getString(R.string.Booking) + " "
                            + context.getString(R.string.Date) + " : " +
                            GlobalVar.changeFont(context, list.get(holder.getLayoutPosition()).getAppointmentdate()
                            ));
                }
            } else {
                holder.appoinmentdattxt.setVisibility(View.GONE);
            }
        } else {
            holder.appoinmentdattxt.setVisibility(View.GONE);
        }


        if (list.get(holder.getLayoutPosition()).getTransactiondate() != null) {
            if (list.get(holder.getLayoutPosition()).getTransactiondate().length() > 0) {

                holder.transactiondatetextView.setText(context.getString(R.string.Transaction) + " "
                        + context.getString(R.string.Date) + " : " +
                        GlobalVar.changeFont(context,
                                list.get(holder.getLayoutPosition()).getTransactiondate()
                        ));
            } else {
                holder.transactiondatetextView.setVisibility(View.GONE);
            }
        } else {
            holder.transactiondatetextView.setVisibility(View.GONE);
        }

        if (list.get(holder.getLayoutPosition()).getImage() != null) {
            if (list.get(holder.getLayoutPosition()).getImage().length() > 0) {
                String profile_img = list.get(holder.getLayoutPosition()).getImage();
                Picasso.with(context).load(profile_img).fit().placeholder(R.drawable.user_blue).
                        error(R.drawable.user_blue).fit().into(holder.type_image);
            } else {
                holder.type_image.setImageResource(R.drawable.user_blue);
            }
        } else {
            holder.type_image.setImageResource(R.drawable.user_blue);
        }

        if (list.get(holder.getLayoutPosition()).getCname() != null) {
            if (list.get(holder.getLayoutPosition()).getCname().length() > 0) {
                holder.cnametxt.setText(list.get(holder.getLayoutPosition()).getCname() + "");
            } else {
                holder.cnametxt.setVisibility(View.GONE);
            }
        } else {
            holder.cnametxt.setVisibility(View.GONE);
        }


        holder.mainLayout.setOnClickListener(v -> {
            Intent i = new Intent(context, ReceiptActivity.class);
            i.putExtra(ReceiptActivity.TRANSACTION_ID_KEY, list.get(holder.getLayoutPosition()).getTransactionid());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PayHistoryHolder extends RecyclerView.ViewHolder {

        ImageView type_image;
        TextView name_textView, addresstextView, typeTextview, transactiondatetextView, cnametxt, appoinmentdattxt;
        LinearLayout mainLayout;

        public PayHistoryHolder(View itemView) {
            super(itemView);
            type_image = itemView.findViewById(R.id.type_image);
            name_textView = itemView.findViewById(R.id.name_textView);
            addresstextView = itemView.findViewById(R.id.doctorAddresstextView);
            typeTextview = itemView.findViewById(R.id.typeTextview);
            cnametxt = itemView.findViewById(R.id.cnametxt);
            transactiondatetextView = itemView.findViewById(R.id.transactiondatetextView);
            appoinmentdattxt = itemView.findViewById(R.id.appoinmentdattxt);
            mainLayout = itemView.findViewById(R.id.category_DetailsSecction);
        }
    }

}
