package com.whr.user.booking.packages.hospital.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.whr.user.R;
import com.whr.user.booking.packages.hospital.model.SurgeryPackagesModel;
import com.whr.user.first_aid.activities.FirstDetailActivity;
import com.whr.user.pojo.GlobalVar;

import java.util.ArrayList;
import java.util.List;

public class SurgerryPackageSearchAdapter extends RecyclerView.Adapter<SurgerryPackageSearchAdapter.PackageHolder> {

    Context ctx;
    LayoutInflater inflater;
    ArrayList<SurgeryPackagesModel> list;


    public SurgerryPackageSearchAdapter(Context context, ArrayList<SurgeryPackagesModel> rowListItem) {
        this.ctx = context;
        this.list = rowListItem;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PackageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_surgery_package_search, parent, false);
        PackageHolder holder = new PackageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PackageHolder holder, final int position) {

        holder.txtPackageName.setText(list.get(position).getPckname());
        holder.txtMinPrice.setText("Rs." + "\t" + GlobalVar.priceWithoutDecimal(Double.parseDouble(list.get(position).getMinfee())));
        holder.txtHospital.setText(Integer.toString(list.get(position).getHospitals()));
        holder.txtDoctors.setText(Integer.toString(list.get(position).getDoctors()));
        holder.mainLayout.setOnClickListener(view -> this.clickLister1.SurgeryPackageClick(list.get(position)));
        
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

    private SurgeryPackageAdapter.ClickLister clickLister1;

    public void setClickLister1(SurgeryPackageAdapter.ClickLister clickLister1) {
        this.clickLister1 = clickLister1;
    }

    public interface ClickLister {
        void SurgeryPackageClick1(SurgeryPackagesModel surgeryPackagePojo);
    }
}