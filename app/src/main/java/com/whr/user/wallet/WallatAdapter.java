package com.whr.user.wallet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;

import java.util.List;



public class WallatAdapter extends RecyclerView.Adapter<WallatAdapter.WallatAdapterHolder> {
    private List<WallatPojo> list;
    private LayoutInflater inflater;
    private Context context;
    int type = 0;

    public WallatAdapter(Context context, List<WallatPojo> rowListItem, int type) {
        this.context = context;
        this.type = type;
        this.list = rowListItem;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public WallatAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wallat_adapter_row, parent, false);
        WallatAdapterHolder holder = new WallatAdapterHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WallatAdapterHolder holder, int position) {

        try {
            holder.txtPoints.setText(list.get(holder.getLayoutPosition()).getCrediteddebitedPoints());
            holder.txtServiceName.setText(list.get(holder.getLayoutPosition()).getTesttreatmentpackagename()+" By "+list.get(holder.getLayoutPosition()).getDochospathname());
            holder.txtBookingId.setText(list.get(holder.getLayoutPosition()).getBookingId());
            holder.txtTransactionDate.setText(list.get(holder.getLayoutPosition()).getTransactionDate());
            if (list.get(holder.getLayoutPosition()).getType().equals("C")) {
                holder.txtcrediteddebitedpoints.setText("Credited Points : ");
            } else if (list.get(holder.getLayoutPosition()).getType().equals("D")) {
                holder.txtcrediteddebitedpoints.setText("Debited Points : ");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class WallatAdapterHolder extends RecyclerView.ViewHolder {

        TextView txtcrediteddebitedpoints, txtPoints,txtServiceName,txtBookingId,txtTransactionDate;

        public WallatAdapterHolder(View itemView) {
            super(itemView);
            txtcrediteddebitedpoints=itemView.findViewById(R.id.txtcrediteddebitedpoints);
            txtPoints = itemView.findViewById(R.id.txtPoints);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            txtBookingId = itemView.findViewById(R.id.txtBookingId);
            txtTransactionDate = itemView.findViewById(R.id.txtTransactionDate);
        }
    }
}