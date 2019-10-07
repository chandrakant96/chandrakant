package com.whr.user.booking.packages.hospital.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.booking.packages.hospital.model.SurgeryPackagesModel;
import com.whr.user.pojo.GlobalVar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SurgeryPackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context ctx;
    LayoutInflater inflater;
    ArrayList<SurgeryPackagesModel> list;
    ArrayList<SurgeryPackagesModel> list1;
    private FragmentActivity activity;
    private final int LOADING = 1;
    private final int DOCTOR = 2;

    public SurgeryPackageAdapter(Context context, ArrayList<SurgeryPackagesModel> rowListItem, FragmentActivity activity) {
        this.ctx = context;
        this.activity = activity;
        this.list = rowListItem;
        this.list1 = rowListItem;
        inflater = LayoutInflater.from(context);
    }


    DecimalFormat decimalFormat = new DecimalFormat("#");

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case DOCTOR:
                view = inflater.inflate(R.layout.row_surgery_package, parent, false);
                holder = new PackageHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.dialog_loading, parent, false);
                holder = new LoadingVH(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, final int position) {
        switch (holder1.getItemViewType()) {
            case DOCTOR: {
                PackageHolder holder = (PackageHolder) holder1;
                holder.txtPackageName.setText(list.get(position).getPckname());
                //              holder.txtMinPrice.setText("Rs." + "\t" + String.valueOf(decimalFormat.format(Double.parseDouble(list.get(position).getMinfee()))));/*+ " " +
//                        "Rs" + "\t" + String.valueOf(decimalFormat.format(Double.parseDouble(list.get(position).getMaxfee()))));*/

                holder.txtMinPrice.setText("Rs." + "\t" + GlobalVar.priceWithoutDecimal(Double.parseDouble(list.get(position).getMinfee())));
               // holder.txtMinPrice.setText("Rs." + "\t" + GlobalVar.priceWithoutDecimal(Double.parseDouble(list.get(position).getMinfee())));
                holder.txtHospital.setText(Integer.toString(list.get(position).getHospitals()));
                holder.txtDoctors.setText(Integer.toString(list.get(position).getDoctors()));
                holder.mainLayout.setOnClickListener(view -> this.clickLister.SurgeryPackageClick(list.get(position)));
            }
            case LOADING:
                //  LoadingVH loadingVH = (LoadingVH) holder;
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ((list.size() == 0 || position == list.size() - 1) && isLoadingAdded) {
            return LOADING;
        } else if (list.get(position) instanceof SurgeryPackagesModel)
            return DOCTOR;
        return -1;
    }

    private boolean isLoadingAdded = false;

    public void addLoadingFooter() {
        isLoadingAdded = true;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PackageHolder extends RecyclerView.ViewHolder {
        TextView txtPackageName, txtMinPrice, txtHospital, txtDoctors;
        LinearLayout mainLayout;

        public PackageHolder(View itemView) {
            super(itemView);
            txtPackageName = itemView.findViewById(R.id.txtPackageName);
            txtMinPrice = itemView.findViewById(R.id.txtMinPrice);
            txtHospital = itemView.findViewById(R.id.txtHospital);
            txtDoctors = itemView.findViewById(R.id.txtDoctors);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

    public class LoadingVH extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingVH(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.pb_loading);
            progressBar.incrementProgressBy(10);
            progressBar.setIndeterminate(true);
        }
    }

    public void getFilterList(String searchKey) {
        list = new ArrayList<>();
        for (SurgeryPackagesModel SurgeryPackagePojo : list1) {
            if (SurgeryPackagePojo.getPckname().toLowerCase().trim().contains(searchKey)) {
                list.add(SurgeryPackagePojo);
            }
        }
        notifyDataSetChanged();
    }

    private ClickLister clickLister;

    public void setClickLister(ClickLister clickLister) {
        this.clickLister = clickLister;
    }

    public interface ClickLister {
        void SurgeryPackageClick(SurgeryPackagesModel surgeryPackagePojo);
    }
}