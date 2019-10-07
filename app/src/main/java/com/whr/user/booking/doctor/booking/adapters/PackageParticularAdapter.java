package com.whr.user.booking.doctor.booking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.PackagesDetails;
import com.whr.user.pojo.GlobalVar;

import java.util.List;

public class PackageParticularAdapter extends RecyclerView.Adapter<PackageParticularAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    private List<PackagesDetails.PerticularDetails> list;


    public PackageParticularAdapter(Context context, List<PackagesDetails.PerticularDetails> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_packages_particular, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.txtAmount.setText(String.valueOf("Rs " + GlobalVar.priceWithoutDecimal(Double.valueOf(list.get(position).getFee()))));
        holder.txtPackageItem.setText(list.get(position).getPerticular());
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

        TextView txtPackageItem, txtAmount;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtPackageItem = itemView.findViewById(R.id.txtPackageItem);
            txtAmount = itemView.findViewById(R.id.txtAmount);
        }
    }
}
