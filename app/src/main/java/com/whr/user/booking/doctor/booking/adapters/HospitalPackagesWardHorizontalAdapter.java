package com.whr.user.booking.doctor.booking.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.HospitalProfile;

import java.util.ArrayList;
import java.util.List;


public class HospitalPackagesWardHorizontalAdapter extends RecyclerView.Adapter<HospitalPackagesWardHorizontalAdapter.MyViewHolder> {

    private Context context;
    public static final String HOSPITAL_PACKAGES_KEY = "HOSPITAL_PACKAGE";
    int selectedPosition;

    List<HospitalProfile.HospitalPackages.Packagesrelation> packagesrelations ;

    public HospitalPackagesWardHorizontalAdapter(Context context,List<HospitalProfile.HospitalPackages.Packagesrelation> packagesrelations, int selectedPosition) {
        this.context = context;
        this.packagesrelations=packagesrelations;
        this.selectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_hospital_ward, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txtWard.setText(packagesrelations.get(position).getWard());
        if (position == selectedPosition) {
            holder.txtWard.setTextColor(context.getResources().getColor(R.color.white));
            holder.txtWard.setBackgroundColor(context.getResources().getColor(R.color.primary));
        } else {
            holder.txtWard.setTextColor(context.getResources().getColor(R.color.grey));
            holder.txtWard.setBackgroundColor(context.getResources().getColor(R.color.greyed_out));
        }
        holder.txtWard.setOnClickListener(view -> {
            selectedPosition = holder.getLayoutPosition();
            this.onClickListener.onClick(selectedPosition);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return packagesrelations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtWard;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtWard = itemView.findViewById(R.id.txtWard);
        }
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int selectionPosition);
    }
}
