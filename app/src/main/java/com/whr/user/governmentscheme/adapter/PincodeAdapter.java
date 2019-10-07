package com.whr.user.governmentscheme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.booking.packages.hospital.model.SurgeryPackagesModel;
import com.whr.user.governmentscheme.models.PincodePojoAddress;
import com.whr.user.pojo.PincodePojo;

import java.util.ArrayList;
import java.util.List;

public class PincodeAdapter extends RecyclerView.Adapter<PincodeAdapter.SchemeInfoHolder> {

    Context ctx;
    LayoutInflater inflater;
    List<PincodePojoAddress> list;
    List<PincodePojoAddress> list1;

    public PincodeAdapter(Context context, List<PincodePojoAddress> list) {
        this.ctx = context;
        this.list = list;
        this.list1 = list;
        inflater = LayoutInflater.from(context);
    }

   
    @Override
    public PincodeAdapter.SchemeInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_pincode, parent, false);
        PincodeAdapter.SchemeInfoHolder holder = new PincodeAdapter.SchemeInfoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PincodeAdapter.SchemeInfoHolder holder, final int position) {
        holder.txtAddress.setText(list.get(position).getCityOrVillage()+", "+list.get(position).getTehsil()+", "+list.get(position).getDistrict()+", "+list.get(position).getState()+"-"+list.get(position).getPincode());

        holder.layoutCLick.setOnClickListener(view -> this.clickLister.PincodeClick(list.get(position)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SchemeInfoHolder extends RecyclerView.ViewHolder {
        TextView txtAddress;
        RelativeLayout layoutCLick;

        public SchemeInfoHolder(View itemView) {
            super(itemView);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            layoutCLick = itemView.findViewById(R.id.layoutCLick);

        }
    }

    private PincodeAdapter.ClickLister clickLister;

    public void setClickLister(PincodeAdapter.ClickLister clickLister) {
        this.clickLister = clickLister;
    }

    private boolean isLoadingAdded = false;

    public interface ClickLister {
        void PincodeClick(PincodePojoAddress pincodePojo);

    }

    public void getFilterList(String searchKey) {
        list = new ArrayList<>();
        for (PincodePojoAddress pincodePojo : list1) {
            if (pincodePojo.getCityOrVillage().toLowerCase().trim().contains(searchKey)) {
                list.add(pincodePojo);
            }
        }
        notifyDataSetChanged();
    }
}
