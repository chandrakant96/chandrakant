package com.whr.user.booking.doctor.booking.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.NewDoctorProfile;
import com.whr.user.com.WHR.Utils.CustomSwipViewAdapter;
import com.whr.user.pojo.GlobalVar;

import java.util.ArrayList;
import java.util.List;


public class DoctorHospitalAdapter extends RecyclerView.Adapter<DoctorHospitalAdapter.MyViewHolder> {

    ArrayList<NewDoctorProfile.HospitalProfile> profileArrayList;
    Context context;
    String TAG = getClass().getSimpleName();
    private String clinicArray[];


    public DoctorHospitalAdapter(ArrayList<NewDoctorProfile.HospitalProfile> profileArrayList, Context context) {
        this.profileArrayList = profileArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_doctor_hospital, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        NewDoctorProfile.HospitalProfile doctorProfile = profileArrayList.get(position);
        holder.txtHospitalName.setText(doctorProfile.getHospname());
        holder.txtAddress.setText(doctorProfile.getAddress());
        holder.txtDistance.setText(String.valueOf(doctorProfile.getDistance()) + " KM");

        holder.txtDiscountedFee.setText("  Rs. " + doctorProfile.getWhrdiscountedfee());
        if (doctorProfile.getFees().equals("0")) {
            holder.txtFees.setVisibility(View.GONE);
        } else {
            holder.txtFees.setText(" Rs. " + doctorProfile.getFees());
        }

        if (doctorProfile.isAvailablestatus()) {
            holder.txtTodayAvailable.setText("Available Today");
            holder.txtTodayAvailable.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appointment_available, 0, 0, 0);
            holder.txtTodayTime.setText(doctorProfile.getTodaystime());
        } else {
            holder.txtTodayAvailable.setText("Not Available Today");
            holder.txtTodayAvailable.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appointment_not_available, 0, 0, 0);
            holder.txtTodayTime.setText(doctorProfile.getTodaystime());
        }

        holder.txtNextTimeSlot.setText(doctorProfile.getNextTimeSlots());

        List<NewDoctorProfile.HospitalProfile.Clinicimages> clinicimages = doctorProfile.getClinicimages();
        if (clinicimages != null) {
            if (clinicimages.size() >= 3 && !clinicimages.get(0).getImagespath().isEmpty()
                    && !clinicimages.get(1).getImagespath().isEmpty() && !clinicimages.get(2).getImagespath().isEmpty()) {
                holder.imgFirst.setVisibility(View.VISIBLE);
                holder.imgSecond.setVisibility(View.VISIBLE);
                holder.imgThird.setVisibility(View.VISIBLE);
                Picasso.with(context).load(clinicimages.get(0).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgFirst);
                Picasso.with(context).load(clinicimages.get(1).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgSecond);
                Picasso.with(context).load(clinicimages.get(2).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgThird);

            } else if (clinicimages.size() == 2 && !clinicimages.get(0).getImagespath().isEmpty()
                    && !clinicimages.get(1).getImagespath().isEmpty()) {
                holder.imgFirst.setVisibility(View.VISIBLE);
                holder.imgSecond.setVisibility(View.VISIBLE);
                Picasso.with(context).load(clinicimages.get(0).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgFirst);
                Picasso.with(context).load(clinicimages.get(1).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgSecond);
            } else if (clinicimages.size() == 1 && !clinicimages.get(0).getImagespath().isEmpty()) {
                holder.imgFirst.setVisibility(View.VISIBLE);
                Picasso.with(context).load(clinicimages.get(0).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgFirst);
            }


            holder.lineImage.setOnClickListener(v -> {
                clinicArray = new String[clinicimages.size()];
                int counter = 0;
                for (NewDoctorProfile.HospitalProfile.Clinicimages clinicImage : clinicimages) {
                    clinicArray[counter] = clinicImage.getImagespath();
                    counter++;
                }
                final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.layout_image_slider);
                Window window = dialog.getWindow();
                assert window != null;
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.BOTTOM;
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                window.setAttributes(wlp);
                dialog.show();

                ViewPager viewPager = dialog.findViewById(R.id.pager);
                CustomSwipViewAdapter adapter = new CustomSwipViewAdapter(context,
                        clinicArray);
                viewPager.setAdapter(adapter);
                TextView txtOk = dialog.findViewById(R.id.txtOk);
                ImageView imgClose = dialog.findViewById(R.id.imgClose);

                txtOk.setOnClickListener(v1 -> {
                    dialog.cancel();
                    dialog.dismiss();
                });

                imgClose.setOnClickListener(v1 -> {
                    dialog.cancel();
                    dialog.dismiss();
                });
            });


            if (clinicimages.size() <= 3) {
                holder.txtMore.setVisibility(View.GONE);
            } else {
                holder.txtMore.setVisibility(View.VISIBLE);
            }
        }

        holder.txtDistance.setOnClickListener(view ->

        {
            String strUri = "http://maps.google.com/maps?q=loc:"
                    + profileArrayList.get(holder.getLayoutPosition()).getLatitude() + ","
                    + profileArrayList.get(holder.getLayoutPosition()).getLongitude() +
                    " (" + profileArrayList.get(holder.getLayoutPosition()).getHospname() + ")";
            try {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(strUri));
                intent.setComponent(new ComponentName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity"));
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                try {
                    context.startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.google.android.apps.maps")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(
                            Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                }
                e.printStackTrace();
            }

        });

        holder.txtBook.setOnClickListener(view -> onClickListener.onClickBook(profileArrayList.get(holder.getLayoutPosition())));
        holder.lineDetail.setOnClickListener(view -> onClickListener.onClickHospitalProfile(profileArrayList.get(holder.getLayoutPosition())));


    }

    @Override
    public int getItemCount() {
        return profileArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtHospitalName, txtAddress, txtDistance, txtFees, txtDiscountedFee, txtTodayTime, txtMore, txtBook;
        ImageView imgFirst, imgSecond, imgThird;
        TextView txtTodayAvailable, txtNextTimeSlot;
        LinearLayout lineDetail, lineImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            lineImage = itemView.findViewById(R.id.lineImage);
            lineDetail = itemView.findViewById(R.id.lineDetail);
            txtHospitalName = itemView.findViewById(R.id.txtHospitalName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtFees = itemView.findViewById(R.id.txtFees);
            txtDiscountedFee = itemView.findViewById(R.id.txtDiscountedFee);
            txtTodayTime = itemView.findViewById(R.id.txtTodayTime);
            txtMore = itemView.findViewById(R.id.txtMore);
            txtBook = itemView.findViewById(R.id.txtBook);
            txtTodayTime = itemView.findViewById(R.id.txtTodayTime);
            txtTodayAvailable = itemView.findViewById(R.id.txtTodayAvailable);
            txtNextTimeSlot = itemView.findViewById(R.id.txtNextTimeSlot);

            imgFirst = itemView.findViewById(R.id.imgFirst);
            imgSecond = itemView.findViewById(R.id.imgSecond);
            imgThird = itemView.findViewById(R.id.imgThird);
        }
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClickBook(NewDoctorProfile.HospitalProfile h);

        void onClickHospitalProfile(NewDoctorProfile.HospitalProfile h);
    }
}
