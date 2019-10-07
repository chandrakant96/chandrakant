package com.whr.user.booking.doctor.booking.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.booking.doctor.booking.HospitalPackageDetailsActivity;
import com.whr.user.booking.doctor.booking.models.HospitalProfile;
import com.whr.user.pojo.GlobalVar;

import java.util.List;


public class HospitalPackagesHorizontalAdapter extends RecyclerView.Adapter<HospitalPackagesHorizontalAdapter.MyViewHolder> {

    private List<HospitalProfile.HospitalPackages.Packagesrelation> dataPackagesrelation;
    private Context context;
    HospitalProfile.HospitalPackages data;
    public static final String HOSPITAL_PACKAGES_KEY = "HOSPITAL_PACKAGE";
    public static final String HOSPITAL_PACKAGES_SELECTED_KEY = "position";
    public static final String HOSPITAL_INFO = "hospital_info";
    HospitalProfile.HospitalInfo hospitalInfo;

    HospitalPackagesHorizontalAdapter(Context context, HospitalProfile.HospitalPackages data,HospitalProfile.HospitalInfo hospitalInfo) {
        this.data = data;
        this.context = context;
        this.dataPackagesrelation = data.getPackagesrelation();
        this.hospitalInfo = hospitalInfo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_hospital_package_relation, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txtSubPackageName.setText(dataPackagesrelation.get(position).getWard());
        holder.txtDiscountPrice.setText("Rs. " + GlobalVar.priceWithoutDecimal(Double.valueOf(dataPackagesrelation.get(position).getDiscount_price())));
        holder.txtActualPrice.setText("Rs. " + GlobalVar.priceWithoutDecimal(Double.valueOf(dataPackagesrelation.get(position).getFee())));
        holder.linePackage.setOnClickListener(view -> {
            Intent intent = new Intent(context, HospitalPackageDetailsActivity.class);
            intent.putExtra(HOSPITAL_PACKAGES_KEY, data);
            intent.putExtra(HOSPITAL_PACKAGES_SELECTED_KEY, holder.getLayoutPosition());
            intent.putExtra(HOSPITAL_INFO, hospitalInfo);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataPackagesrelation.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubPackageName, txtDiscountPrice, txtActualPrice;
        LinearLayout linePackage;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtSubPackageName = itemView.findViewById(R.id.txtSubPackageName);
            txtDiscountPrice = itemView.findViewById(R.id.txtDiscountPrice);
            txtActualPrice = itemView.findViewById(R.id.txtActualPrice);
            linePackage = itemView.findViewById(R.id.linePackage);
        }
    }
}
