package com.whr.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.activities.CancelAppoinmentActivity;
import com.whr.user.activities.ReceiptActivity;
import com.whr.user.activities.RescheduleActivity;
import com.whr.user.booking.diagnostics.booking.model.PathologyListPojo;
import com.whr.user.pojo.NewReportPojo;

import java.util.ArrayList;
import java.util.List;

public class NewReportAdapter extends RecyclerView.Adapter<NewReportAdapter.VideoInfoHolder> {

    Context ctx;
    LayoutInflater inflater;
    List<NewReportPojo> list;
    List<NewReportPojo> list1;


    public NewReportAdapter(Context context, List<NewReportPojo> rowListItem) {
        this.ctx = context;
        this.list = rowListItem;
        this.list1 = rowListItem;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public NewReportAdapter.VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.design_new_report, parent, false);
        NewReportAdapter.VideoInfoHolder holder = new NewReportAdapter.VideoInfoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReportAdapter.VideoInfoHolder holder, final int position) {

        holder.txtPathDocName.setText(list.get(position).getPname());
        holder.txtName.setText(list.get(position).getUsername());
        holder.txtMobile.setText(list.get(position).getMobileno());

        holder.txtView.setOnClickListener(view -> this.clickLister.viewClick(list.get(position)));

        holder.txtDownload.setOnClickListener(view -> this.clickLister.downloadClick(list.get(position)));

        holder.relativeDetails.setOnClickListener(view -> this.clickLister.layoutClick(list.get(position)));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder {
           TextView txtPathDocName,txtName,txtMobile,txtDownload,txtView;
           CardView relativeDetails;

        public VideoInfoHolder(View itemView) {
            super(itemView);

            relativeDetails=itemView.findViewById(R.id.relativeDetails);
            txtPathDocName=itemView.findViewById(R.id.txtPathDocName);
            txtName=itemView.findViewById(R.id.txtName);
            txtMobile=itemView.findViewById(R.id.txtMobile);
            txtDownload=itemView.findViewById(R.id.txtDownload);
            txtView=itemView.findViewById(R.id.txtView);
        }

    }

    public void getFilterList(String searchKey) {
        list = new ArrayList<>();
        for (NewReportPojo NewReportPojo : list1) {
            if (NewReportPojo.getPname().toLowerCase().trim().contains(searchKey)) {
                list.add(NewReportPojo);
            }
        }
        notifyDataSetChanged();
    }

    private NewReportAdapter.ClickLister clickLister;

    public void setClickLister(NewReportAdapter.ClickLister clickLister) {
        this.clickLister = clickLister;
    }

    private boolean isLoadingAdded = false;

    public interface ClickLister {
        void viewClick(NewReportPojo NewReportPojo);

        void downloadClick(NewReportPojo NewReportPojo);

        void layoutClick(NewReportPojo NewReportPojo);

       
    }

}
