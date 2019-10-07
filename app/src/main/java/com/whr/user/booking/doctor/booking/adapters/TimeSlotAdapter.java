package com.whr.user.booking.doctor.booking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.booking.models.TimeSlot;
import com.whr.user.pojo.AppoinmentBookingPojo;

import java.util.Date;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeViewHolder> {
    private Date booking_dateObj;
    public static String selectedBookingTime = "";
    private final RequestQueue mQueue;
    private Context context;
    private LayoutInflater inflater;
    public List<TimeSlot> timeList;
    public List<AppoinmentBookingPojo.BookedDates> bookedDatesList;
    public Button bookedButton;
    public int adapterPosition = 0;
    private String booking_date = "";

    public TimeSlotAdapter(Context context, List<TimeSlot> timeList
            , String bookingdate
    ) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.timeList = timeList;
        this.bookedDatesList = bookedDatesList;
        this.booking_date = bookingdate;
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_item_time_slot, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimeViewHolder holder, int position) {
        String str = timeList.get(position).getStrtime();
        holder.txtTime.setText(str);

       /* if (timeList.get(holder.getAdapterPosition()).isChecked()) {
            holder.txtTime.setTextColor(context.getResources().getColor(R.color.white));
            holder.txtTime.setBackgroundColor(context.getResources().getColor(R.color.primary));
        } else {
            holder.txtTime.setTextColor(context.getResources().getColor(R.color.black));
            holder.txtTime.setBackground(context.getResources().getDrawable(R.drawable.round_corner_black_border));
        }*/

        /*holder.txtTime.setTextColor(context.getResources().getColor(R.color.black));
        holder.txtTime.setBackground(context.getResources().getDrawable(R.drawable.round_corner_black_border));*/

        if (timeList.get(holder.getAdapterPosition()).isPassed() || timeList.get(holder.getAdapterPosition()).isBooked()) {
            holder.txtTime.setTextColor(context.getResources().getColor(R.color.black));
            holder.txtTime.setBackgroundColor(context.getResources().getColor(R.color.divider2));
        } else if (timeList.get(holder.getAdapterPosition()).isChecked()) {
            holder.txtTime.setTextColor(context.getResources().getColor(R.color.white));
            holder.txtTime.setBackgroundColor(context.getResources().getColor(R.color.primary));
        } else if (!timeList.get(holder.getAdapterPosition()).isChecked()) {
            holder.txtTime.setTextColor(context.getResources().getColor(R.color.black));
            holder.txtTime.setBackground(context.getResources().getDrawable(R.drawable.round_corner_black_border));
        }
        holder.txtTime.setOnClickListener(view -> {
            for (TimeSlot slot : timeList) slot.setChecked(false);
            onClickListener.onClick(timeList.get(holder.getLayoutPosition()));
        });
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView txtTime;

        TimeViewHolder(View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(TimeSlot timeSlot);
    }
}

