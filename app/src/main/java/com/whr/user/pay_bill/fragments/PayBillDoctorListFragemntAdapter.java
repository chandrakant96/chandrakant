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
import com.whr.user.com.WHR.com.WHR.Activities.Paybill_PaymentActivity;
import com.whr.user.pay_bill.SuggestDoctorDetailsList;
import com.whr.user.pojo.GlobalVar;

import java.util.List;

public class PayBillDoctorListFragemntAdapter extends RecyclerView.Adapter<PayBillDoctorListFragemntAdapter.DoctorListViewHolder> {
    private LayoutInflater inflater;
    private List<SuggestDoctorDetailsList> rowListItem;
    private Context context;
    private PayBillFragment paybillfrag;
    private String familyid;
    private String suggestValue;
    private String familyname;
    String TAG = getClass().getSimpleName();


    public PayBillDoctorListFragemntAdapter(Context context, List<SuggestDoctorDetailsList> rowListItem,
                                            PayBillFragment payBillFragment, String familyid, String familyname) {
        this.paybillfrag = payBillFragment;
        this.context = context;
        this.rowListItem = rowListItem;
        this.familyid = familyid;
        this.familyname = familyname;
        inflater = LayoutInflater.from(context);
        GlobalVar.errorLog(TAG, "adapter", TAG);
    }


    @Override
    public DoctorListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.doctor_list_adapter_row, parent, false);
        DoctorListViewHolder holder = new DoctorListViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final DoctorListViewHolder holder, final int position) {
        if (rowListItem.get(position).getProfile_img().length() > 0) {
            String emgimageView1profile_img = rowListItem.get(position).getProfile_img();
            String emgimageView1profstr = emgimageView1profile_img.replace("\\", "");

            if (emgimageView1profstr.length() > 0) {
                String pStr = emgimageView1profstr.replaceAll(" ", "%20");
                Picasso.with(context).load(pStr).fit().error(R.drawable.user_blue).placeholder(R.drawable.user_blue).into(holder.imageView);
            }
        }

        holder.suggestedDoctorName.setText(rowListItem.get(position).getDoctorName());
        holder.suggestedDoctorAddress.setText(rowListItem.get(position).getDoctorAddress());
        holder.suggestedDate.setText(rowListItem.get(holder.getLayoutPosition()).getSuggestedDate() + "");

        if (rowListItem.get(holder.getLayoutPosition()).getCname() != null) {
            if (rowListItem.get(holder.getLayoutPosition()).getCname().length() > 0) {
                holder.cname.setText(rowListItem.get(holder.getLayoutPosition()).getCname() + "");
            } else {
                holder.cname.setVisibility(View.GONE);
            }
        } else {
            holder.cname.setVisibility(View.GONE);
        }


        holder.mainLayout.setOnClickListener(v -> {
            Intent i = new Intent(context, Paybill_PaymentActivity.class);
            i.putExtra("doctorName", rowListItem.get(holder.getLayoutPosition()).getDoctorName());
            i.putExtra("doctorId", rowListItem.get(holder.getLayoutPosition()).getDoctorId());
            i.putExtra("profile_img", rowListItem.get(holder.getLayoutPosition()).getProfile_img());
            i.putExtra("rowid", rowListItem.get(holder.getLayoutPosition()).getRowid());
            i.putExtra("familyid", familyid);
            i.putExtra("familyname", familyname);
            i.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            context.startActivity(i);


        });
    }

    @Override
    public int getItemCount() {
        return rowListItem.size();
    }


    class DoctorListViewHolder extends RecyclerView.ViewHolder {
        private TextView suggestedDoctorAddress, suggestedDoctorName, suggestedDate, cname;
        private LinearLayout mainLayout;
        private ImageView imageView;

        public DoctorListViewHolder(View itemView) {
            super(itemView);
            suggestedDoctorName = itemView.findViewById(R.id.suggestedDoctorName);
            suggestedDoctorAddress = itemView.findViewById(R.id.suggestedDoctorAddress);
            cname = itemView.findViewById(R.id.cname);
            suggestedDate = itemView.findViewById(R.id.suggestedDate);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            imageView = itemView.findViewById(R.id.imageProfile);
        }
    }
}
