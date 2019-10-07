package com.whr.user.booking.doctor.booking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.DoctorSpecialitiesModel;

import java.util.List;

public class DoctorSpecialitiesRecyclerViewAdapter extends RecyclerView.Adapter<DoctorSpecialitiesRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<DoctorSpecialitiesModel> list;
    private List<DoctorSpecialitiesModel> mFilterList;

    public DoctorSpecialitiesRecyclerViewAdapter(Context context, List<DoctorSpecialitiesModel> rowListItem) {
        this.context = context;
        this.list = rowListItem;
        this.mFilterList = rowListItem;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_doctor_specialities, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.txtSpecialization.setText(mFilterList.get(position).getName());
        if (!mFilterList.get(position).getWatermark().isEmpty())
            holder.txtIllness.setText("(" + mFilterList.get(position).getWatermark() + ")");

        holder.relativeItem.setOnClickListener(v -> clickLister.click(mFilterList.get(position)));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtSpecialization;
        TextView txtIllness;
        RelativeLayout relativeItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtSpecialization = itemView.findViewById(R.id.txtSpecialization);
            txtIllness = itemView.findViewById(R.id.txtIllness);
            relativeItem = itemView.findViewById(R.id.relativeItem);
        }
    }

    private ClickLister clickLister;

    public void setClickLister(ClickLister clickLister) {
        this.clickLister = clickLister;
    }

    public interface ClickLister {
        void click(DoctorSpecialitiesModel doctorSpecialitiesModel);
    }
}
