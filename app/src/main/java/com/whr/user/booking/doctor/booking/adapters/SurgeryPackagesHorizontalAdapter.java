package com.whr.user.booking.doctor.booking.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.SurgeryPackages;

import java.util.ArrayList;


public class SurgeryPackagesHorizontalAdapter extends RecyclerView.Adapter<SurgeryPackagesHorizontalAdapter.MyViewHolder> {

    private ArrayList<SurgeryPackages> data;
    private Context context;

    SurgeryPackagesHorizontalAdapter(Context context, ArrayList<SurgeryPackages> data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ahorizontal_single_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.title.setText(data.get(position).getImgDescription());
        holder.title.setOnClickListener(view -> Toast.makeText(view.getContext(), "" + data.get(position).toString(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
