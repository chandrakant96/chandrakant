package com.whr.user.booking.doctor.booking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.whr.user.booking.doctor.booking.models.HospitalProfile;

import java.util.List;

public class HospitalDoctorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = HospitalDoctorAdapter.class.getSimpleName();
    private Context context;
    private List<HospitalProfile.DoctorList> items;

    private RecyclerView.RecycledViewPool viewPool;

    public HospitalDoctorAdapter(Context context, List<HospitalProfile.DoctorList> items) {
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
        View view = inflater.inflate(R.layout.recycler_item_hospital_doctor, parent, false);
        return new HospitalDoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HospitalDoctorViewHolder doctorViewHolder = (HospitalDoctorViewHolder) holder;
        HospitalProfile.DoctorList o = items.get(position);
        doctorViewHolder.txtDoctorName.setText(o.getName());
        if (o.getProfileimage() != null &&
                !o.getProfileimage().isEmpty()) {
            Picasso.with(context).load(o.getProfileimage())
                    .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                    .fit().into(doctorViewHolder.imageDoctor);
        } else
            doctorViewHolder.imageDoctor.setImageResource(R.drawable.user_blue);

        doctorViewHolder.txtDoctorName.setText(o.getName());
        if (!o.getEducation().isEmpty()){
            doctorViewHolder.txtEducation.setText(o.getEducation());
        }else {
            doctorViewHolder.txtEducation.setVisibility(View.GONE);
        }

        doctorViewHolder.txtSpecialization.setText(o.getSpecial());
        if (o.getExperience().isEmpty()) {
            doctorViewHolder.txtExperience.setVisibility(View.GONE);
            doctorViewHolder.txtExpTitle.setVisibility(View.GONE);
        } else {
            doctorViewHolder.txtExperience.setText(o.getExperience());
        }
        doctorViewHolder.txtLikes.setText(String.valueOf(o.getLikes()) + "\n Likes");
        //  doctorViewHolder.txtReviews.setText(String.valueOf(((HospitalProfile.DoctorList) o).getReviews()));
        doctorViewHolder.txtReviews.setText(String.valueOf((0)) + "\nReviews");
        doctorViewHolder.txtDistance.setVisibility(View.GONE);

        if (o.isAvailablestatus()) {
            doctorViewHolder.txtAvailableToday.setText("Slots available for Today");
            doctorViewHolder.txtAvailableToday.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appointment_available, 0, 0, 0);
        } else {
            doctorViewHolder.txtAvailableToday.setText("Slots Not available for Today");
            doctorViewHolder.txtAvailableToday.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appointment_not_available, 0, 0, 0);
        }

        doctorViewHolder.txtDiscountedFee.setText("  Rs. " + o.getWhrdiscountedfee());
        if (o.getFee().equals("0")) {
            doctorViewHolder.txtFees.setVisibility(View.GONE);
        } else {
            doctorViewHolder.txtFees.setText(" Rs. " + o.getFee());
        }

        doctorViewHolder.txtBook.setOnClickListener(view -> onClickListener.onClick(items.get(doctorViewHolder.getLayoutPosition())));
    }

    public class HospitalDoctorViewHolder extends RecyclerView.ViewHolder {
        CardView card_view;
        ImageView imageDoctor;
        TextView txtDoctorName, txtEducation, txtSpecialization, txtExperience, txtBook,
                txtPersonalHospitalName, txtFees, txtDiscountedFee,
                txtLikes, txtReviews, txtDistance, txtAvailableToday, txtCall, txtExpTitle;
        LinearLayout relativeDetails;

        HospitalDoctorViewHolder(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            imageDoctor = itemView.findViewById(R.id.imageDoctor);
            txtBook = itemView.findViewById(R.id.txtBook);
            txtDoctorName = itemView.findViewById(R.id.txtDoctorName);
            txtEducation = itemView.findViewById(R.id.txtEducation);
            txtSpecialization = itemView.findViewById(R.id.txtSpecialization);
            txtExperience = itemView.findViewById(R.id.txtExperience);
            txtPersonalHospitalName = itemView.findViewById(R.id.txtPersonalHospitalName);
            txtFees = itemView.findViewById(R.id.txtFees);
            txtDiscountedFee = itemView.findViewById(R.id.txtDiscountedFee);
            txtLikes = itemView.findViewById(R.id.txtLikes);
            txtReviews = itemView.findViewById(R.id.txtReviews);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtAvailableToday = itemView.findViewById(R.id.txtAvailableToday);
            txtAvailableToday = itemView.findViewById(R.id.txtAvailableToday);
            txtExpTitle = itemView.findViewById(R.id.txtExpTitle);
            txtCall = itemView.findViewById(R.id.txtCall);
            txtCall.setVisibility(View.INVISIBLE);
            txtPersonalHospitalName.setVisibility(View.GONE);
        }
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(HospitalProfile.DoctorList d);
    }
}