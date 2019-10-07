package com.whr.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.whr.user.model.NewAppoinmentHistoryPojo;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AppointmentHistoryAdapter extends RecyclerView.Adapter<AppointmentHistoryAdapter.VideoInfoHolder> {

    Context ctx;
    LayoutInflater inflater;
    List<NewAppoinmentHistoryPojo> list;
    List<NewAppoinmentHistoryPojo> list1;


    public AppointmentHistoryAdapter(Context context, List<NewAppoinmentHistoryPojo> rowListItem) {
        this.ctx = context;
        this.list = rowListItem;
        this.list1 = rowListItem;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.appoinment_history_adapter_row, parent, false);
        VideoInfoHolder holder = new VideoInfoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {

        holder.appointmentDate.setText(list.get(position).getAppointmentDate());
        holder.appointmentTIme.setText(list.get(position).getAppointmentTime());
        holder.appoinmentdoctorLab.setText(list.get(position).getBookedAt());
        holder.resonText.setText(list.get(position).getSpecialization());

        if (list.get(position).getProfile() != null && !(list.get(position).getProfile().isEmpty())) {
            Picasso.with(ctx).load(list.get(position).getProfile())
                    .placeholder(R.drawable.ic_doctor_color).error(R.drawable.ic_doctor_color)
                    .fit().into(holder.image);
        } else
            holder.image.setImageResource(R.drawable.ic_doctor_color);


        holder.mainLayout.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, ReceiptActivity.class);
            intent.putExtra("transactionId", list.get(position).getBookingId());
            intent.putExtra("type", "Pathology");
            intent.putExtra(ReceiptActivity.BOOKING_KEY, "Appointment");
            ctx.startActivity(intent);
        });

        holder.txtCancelAppoinment.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, CancelAppoinmentActivity.class);
            intent.putExtra("bookingid", list.get(position).getBookingId());
            intent.putExtra("did", list.get(position).getId());
            intent.putExtra("hid", list.get(position).getHid());
            intent.putExtra("specilization", list.get(position).getSpecialization());
            ctx.startActivity(intent);
            ((Activity) ctx).finish();

        });

        holder.txtReschedule.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, RescheduleActivity.class);
            intent.putExtra("bookingid", list.get(position).getBookingId());
            intent.putExtra("did", list.get(position).getId());
            intent.putExtra("hid", list.get(position).getHid());
            intent.putExtra("specilization", list.get(position).getSpecialization());
            ctx.startActivity(intent);
            ((Activity) ctx).finish();

        });


        if (list.get(position).getSpecialization().contains("Pathology") && list.get(position).getId().equals("50189")) {
            holder.appointmentTIme.setVisibility(View.VISIBLE);
        } else if (list.get(position).getSpecialization().contains("Radiology")) {
            holder.appointmentTIme.setVisibility(View.GONE);
        } else if (list.get(position).getSpecialization().contains("Multispeciality")) {
            holder.appointmentTIme.setVisibility(View.GONE);
        } else {
            holder.appointmentTIme.setVisibility(View.VISIBLE);
        }


        if (list.get(position).getCancelFlag().contains("1")) {
            holder.txtCancelled.setVisibility(View.VISIBLE);
            holder.layoutSchedule.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);


        } else {
            holder.txtCancelled.setVisibility(View.GONE);
            holder.layoutSchedule.setVisibility(View.VISIBLE);
            if (list.get(position).isIsdatePassed()) {
                holder.layoutSchedule.setVisibility(View.VISIBLE);
                holder.layoutBackground.setBackgroundResource(R.drawable.border_appoinment_history);


            } else {
                holder.layoutSchedule.setVisibility(View.GONE);
                holder.view.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLayout, layoutSchedule;
        ImageView image;
        TextView appointmentDate, appointmentTIme, appoinmentdoctorLab, resonText;
        TextView txtCancelAppoinment, txtReschedule, txtCancelled;
        LinearLayout layoutBackground;
        View view;

        public VideoInfoHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            layoutSchedule = itemView.findViewById(R.id.layoutSchedule);
            appointmentDate = itemView.findViewById(R.id.appointmentDate);
            appointmentTIme = itemView.findViewById(R.id.appointmentTIme);
            appoinmentdoctorLab = itemView.findViewById(R.id.appoinmentdoctorLab);
            resonText = itemView.findViewById(R.id.resonText);
            txtCancelled = itemView.findViewById(R.id.txtCancelled);
            layoutBackground = itemView.findViewById(R.id.layoutBackground);

            txtCancelAppoinment = itemView.findViewById(R.id.txtCancelAppoinment);
            txtReschedule = itemView.findViewById(R.id.txtReschedule);
            view = itemView.findViewById(R.id.view);
        }

    }

    public void getFilterList(String searchKey) {
        list = new ArrayList<>();
        for (NewAppoinmentHistoryPojo NewAppoinmentHistoryPojo : list1) {
            if (NewAppoinmentHistoryPojo.getBookedAt().toLowerCase().trim().contains(searchKey)) {
                list.add(NewAppoinmentHistoryPojo);
            }
        }
        notifyDataSetChanged();
    }


}