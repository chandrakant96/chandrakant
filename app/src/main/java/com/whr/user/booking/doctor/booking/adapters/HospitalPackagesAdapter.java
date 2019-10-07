package com.whr.user.booking.doctor.booking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.DoctorModel;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.booking.doctor.booking.models.HospitalProfile;

import java.util.ArrayList;

public class HospitalPackagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = HospitalPackagesAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<HospitalProfile.HospitalPackages> items;
    private RecyclerView.RecycledViewPool viewPool;
    HospitalProfile.HospitalInfo hospitalInfo;

    public HospitalPackagesAdapter(Context context, ArrayList<HospitalProfile.HospitalPackages> items, HospitalProfile.HospitalInfo hospitalInfo) {
        this.context = context;
        this.items = items;
        this.hospitalInfo = hospitalInfo;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    public HospitalPackagesAdapter(Context context, ArrayList<HospitalProfile.HospitalPackages> items) {
        this.context = context;
        this.items = items;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = null;
        View view = inflater.inflate(R.layout.recycler_item_hospital_package, parent, false);
        holder = new HospitalPackagesViewHolder(view);
        ((HospitalPackagesViewHolder) holder).inner_recyclerView.setRecycledViewPool(viewPool);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HospitalPackagesViewHolder hospitalPackagesViewHolder = (HospitalPackagesViewHolder) holder;
        hospitalPackagesViewHolder.txtPackageName.setText(items.get(position).getPackagename());
        HospitalPackagesHorizontalAdapter adapter = new HospitalPackagesHorizontalAdapter(context,
                items.get(position), hospitalInfo);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.setInitialPrefetchItemCount(items.get(position).getPackagesrelation().size());
        hospitalPackagesViewHolder.inner_recyclerView.setLayoutManager(linearLayoutManager);
        hospitalPackagesViewHolder.inner_recyclerView.setHasFixedSize(true);
        hospitalPackagesViewHolder.inner_recyclerView.setAdapter(adapter);
    }


    private ClickLister clickLister;

    public void setClickLister(ClickLister clickLister) {
        this.clickLister = clickLister;
    }

    public interface ClickLister {
        void doctorBookClick(DoctorModel doctorModel);

        void doctorDetailClick(DoctorModel doctorModel);

        void hospitalBookClick(HospitalModel hospitalModel);

        void hospitalDetailClick(HospitalModel hospitalModel);
    }

    public class HospitalPackagesViewHolder extends RecyclerView.ViewHolder {
        RecyclerView inner_recyclerView;
        TextView txtPackageName;

        HospitalPackagesViewHolder(View itemView) {
            super(itemView);
            inner_recyclerView = itemView.findViewById(R.id.inner_recyclerView);
            txtPackageName = itemView.findViewById(R.id.txtPackageName);
        }
    }
}