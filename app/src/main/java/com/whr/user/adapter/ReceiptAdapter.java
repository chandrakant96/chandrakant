package com.whr.user.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.model.PaymentReceiptModel;

import java.util.ArrayList;
import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.VideoInfoHolder> {

    Context ctx;
    LayoutInflater inflater;
    List<PaymentReceiptModel.TestOrTreatmentDetails> list;

    public ReceiptAdapter(Context context, List<PaymentReceiptModel.TestOrTreatmentDetails> list) {
        this.ctx = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_receipt_row, parent, false);
        VideoInfoHolder holder = new VideoInfoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {
        holder.txtTestName.setText(list.get(position).getTestOrTreatmentOrPackageName());
        holder.txtTestPrice.setText(String.valueOf(list.get(position).getDicountPrice()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder {
        TextView txtTestName, txtTestPrice;

        public VideoInfoHolder(View itemView) {
            super(itemView);
            txtTestName = itemView.findViewById(R.id.txtTestName);
            txtTestPrice = itemView.findViewById(R.id.txtTestPrice);
        }
    }
}