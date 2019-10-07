package com.whr.user.com.WHR.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.pojo.AppoinmentBookingPojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 4/11/2017.
 */

public class AppoinmentBookingAdapter2 extends RecyclerView.Adapter<AppoinmentBookingAdapter2.AppoinmentViewHolder> {
    private Date booking_dateObj;
    public static String selectedBookingTime = "";
    private final RequestQueue mQueue;
    private Context context;
    private LayoutInflater inflater;
    public List<AppoinmentBookingPojo.TimeList> timeList;
    public List<AppoinmentBookingPojo.BookedDates> bookedDatesList;
    public List<Button> buttonList = new ArrayList<>();
    public Button bookedButton;
    public int adapterPosition = 0;
    private String booking_date = "";
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy:MM:dd hh:mm aa");

    public AppoinmentBookingAdapter2(Context context, List<AppoinmentBookingPojo.TimeList> timeList, List<AppoinmentBookingPojo.BookedDates> bookedDatesList
            , String bookingdate
    ) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.timeList = timeList;
        this.bookedDatesList = bookedDatesList;
        this.booking_date = bookingdate;
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();

        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            booking_dateObj = (Date) formatter.parse(booking_date);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public AppoinmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.appoinment_adapter_view_row, parent, false);
        AppoinmentViewHolder holder = new AppoinmentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AppoinmentViewHolder holder, int position) {
        String str = timeList.get(position).getStrtime();
        holder.appinmentBookingBtn.setText(str + "");
        buttonList.add(holder.appinmentBookingBtn);

        String time = str;

        try {
            Calendar calendar = Calendar.getInstance();
            time = calendar.get(Calendar.YEAR) + ":" + (calendar.get(Calendar.MONTH) + 1) + ":" + calendar.get(Calendar.DAY_OF_MONTH) + " " + time.trim(); //2017:7:12 09:00 AM
            Date date = dateFormat2.parse(time);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
            String currentDateandTime = sdf.format(new Date());


            String[] booking_datearr = booking_date.split("/");
            Calendar booking_calendar = Calendar.getInstance();

            //  String book_date= booking_datearr[2]+":"+booking_datearr[1]+":"+booking_datearr[0]+" "+time.trim(); //2017:7:12 09:00 AM


            SimpleDateFormat sdf12 = new SimpleDateFormat("dd/MM/yyyy");
            String currentTime = sdf12.format(new Date());

            Date currentdate = sdf.parse(currentDateandTime);
            Date current_date = sdf12.parse(currentTime);

            calendar = Calendar.getInstance();
            calendar.setTime(currentdate);

            int m = bookedDatesList.size();

            for (int k = 0; k < m; k++) {
                String bookedtime = bookedDatesList.get(k).getBookedDate(); //01:10 AM
                bookedtime = calendar.get(Calendar.YEAR) + ":" + (calendar.get(Calendar.MONTH) + 1) +
                        ":" + calendar.get(Calendar.DAY_OF_MONTH) + " "
                        + bookedtime.trim(); //2017:7:12 09:00 AM
                Date bookeddate = dateFormat2.parse(bookedtime);
                try {
                    Date dclone = new Date(date.getTime());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dclone);
                    cal.add(Calendar.MINUTE, 14);
                    String newTime = dateFormat2.format(cal.getTime());

                    Date d_15 = dateFormat2.parse(newTime);
                    //    if(d_15.before(bookeddate) && date.after(bookeddate) )// todo:booking time comapir  //  && date.before(d_15)


                    if (date.equals(bookeddate)) {
                        holder.appinmentBookingBtn.setBackgroundDrawable(context.getResources().getDrawable(R.color.divider));
                        if (buttonList.indexOf(holder.appinmentBookingBtn) > -1) {
                            holder.appinmentBookingBtn.setOnClickListener(null);
                            buttonList.remove(holder.appinmentBookingBtn);
                        }
                    } else if (d_15.after(bookeddate) && date.before(bookeddate))// todo:booking time comapir  //  && date.before(d_15)
                    {
                        Log.e("isgretter", "time is book desible than Current Time");
                        AppoinmentBookingPojo.BookedDates model = new AppoinmentBookingPojo.BookedDates();
                        model.setBookedDate(str);
                        holder.appinmentBookingBtn.setBackgroundDrawable(context.getResources().getDrawable(R.color.divider));
                        if (buttonList.indexOf(holder.appinmentBookingBtn) > -1) {
                            holder.appinmentBookingBtn.setOnClickListener(null);
                            buttonList.remove(holder.appinmentBookingBtn);
                        }
                        //bookedDatesList.add(model);
                    } else {
                        Log.e("isgretter", " time is less than Current time");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }


            if (current_date.equals(booking_dateObj)) {
                if (currentdate.after(date))  //
                {
                    Log.e("TIMEPASSED", "Time already passed Disable this button");
                    holder.appinmentBookingBtn.setBackgroundDrawable(context.getResources().getDrawable(R.color.divider));
                    if (buttonList.indexOf(holder.appinmentBookingBtn) > -1) {
                        holder.appinmentBookingBtn.setOnClickListener(null);
                        buttonList.remove(holder.appinmentBookingBtn);
                    }

                    AppoinmentBookingPojo.BookedDates model = new AppoinmentBookingPojo.BookedDates();
                    model.setBookedDate(str);
                    //  bookedDatesList.add(model);
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
            e.getMessage();
        }


        holder.appinmentBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = "";
                if (buttonList.contains(holder.appinmentBookingBtn)) {
                    adapterPosition = holder.getLayoutPosition();
                    s = "found";
                    setTimeNameChecker((Button) v);
                } else {
                    s = "Not found";
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    class AppoinmentViewHolder extends RecyclerView.ViewHolder {

        Button appinmentBookingBtn;

        public AppoinmentViewHolder(View itemView) {
            super(itemView);
            appinmentBookingBtn = (Button) itemView.findViewById(R.id.appinmentBookingBtn);
        }

    }

    private void setTimeNameChecker(Button btnTag) {
        for (int i = 0; i < buttonList.size(); i++) {
            if (buttonList.get(i).getText().toString() != btnTag.getText().toString()) {
                buttonList.get(i).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.round_corner_black_border));
            } else {
                selectedBookingTime = buttonList.get(i).getText().toString();
                bookedButton = btnTag;
                buttonList.get(i).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.round_corner_primary_dark_border));
            }

        }

    }

    public String getselectedBookingTime() {
        return selectedBookingTime;
    }


}

