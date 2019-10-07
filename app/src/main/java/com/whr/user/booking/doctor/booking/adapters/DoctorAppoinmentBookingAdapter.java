package com.whr.user.booking.doctor.booking.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.pojo.DoctorTreatmentPojo;
import com.whr.user.pojo.GlobalVar;

import java.util.ArrayList;
import java.util.List;

public class DoctorAppoinmentBookingAdapter extends RecyclerView.Adapter<DoctorAppoinmentBookingAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    private List<DoctorTreatmentPojo> list;
    private List<DoctorTreatmentPojo> mFilterList;
    private List<DoctorTreatmentPojo> selectedList;
    private List<DoctorTreatmentPojo> totalChargeList;
    private AppCompatActivity activity;
    private AlertDialog removeAlert;
    private boolean isChecked;
    public static double totalval = 0.0f;


    public DoctorAppoinmentBookingAdapter(Context context, List<DoctorTreatmentPojo> list, AppCompatActivity activity) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.mFilterList = list;
        this.selectedList = new ArrayList<>();
        this.activity = activity;
        totalChargeList = new ArrayList<>();
        totalval = 0.0f;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_doctor_booking_appoinment, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.treatmentName.setText(mFilterList.get(holder.getLayoutPosition()).getTreatmentname());
        holder.txtDiscountPrice.setText("Rs. " + GlobalVar.priceWithoutDecimal(mFilterList.get(holder.getLayoutPosition()).getDiscount_price()) + "");
        holder.txtActualPrice.setText("Rs. " + GlobalVar.priceWithoutDecimal(mFilterList.get(holder.getLayoutPosition()).getFee()) + "");
        holder.treatmentName.setChecked(mFilterList.get(holder.getLayoutPosition()).isCheck());
        holder.txtDescription.setText(mFilterList.get(holder.getLayoutPosition()).getDescription());

        if (mFilterList.get(holder.getLayoutPosition()).getWhrdiscount() > 0) {
            holder.txtActualPrice.setPaintFlags(holder.txtActualPrice.getPaintFlags() |
                    Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (mFilterList.get(holder.getLayoutPosition()).getWhrdiscount() == 0) {
            holder.txtActualPrice.setVisibility(View.GONE);
        }


        holder.relativeItem.setOnClickListener(v -> this.onClickListener.onClick(mFilterList.get(holder.getLayoutPosition())));

        holder.treatmentName.setOnClickListener(v -> this.onClickListener.onClick(mFilterList.get(holder.getLayoutPosition())));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getItemCount() {
        return mFilterList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox treatmentName;
        TextView txtDescription, txtActualPrice, txtDiscountPrice;
        RelativeLayout relativeItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            treatmentName = itemView.findViewById(R.id.treatmentName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtActualPrice = itemView.findViewById(R.id.txtActualPrice);
            txtDiscountPrice = itemView.findViewById(R.id.txtDiscountPrice);
            relativeItem = itemView.findViewById(R.id.relativeItem);
        }
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(DoctorTreatmentPojo d);
    }


    public void getFilterList(String searchKey) {
        mFilterList = new ArrayList<>();
        for (DoctorTreatmentPojo doctorTreatmentPojo : list) {
            if (doctorTreatmentPojo.getTreatmentname().toLowerCase().trim().contains(searchKey)) {
                doctorTreatmentPojo.setCheck(doctorTreatmentPojo.isCheck());
                mFilterList.add(doctorTreatmentPojo);
            }
        }
        notifyDataSetChanged();
    }
}
