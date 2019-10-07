package com.whr.user.booking.diagnostics.booking.adapter;

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
import com.whr.user.booking.diagnostics.booking.model.Suggested_TreatementPojo;

import java.util.ArrayList;
import java.util.List;


public class TestTreatFeeAdapter extends RecyclerView.Adapter<TestTreatFeeAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    private List<Suggested_TreatementPojo> list;
    private List<Suggested_TreatementPojo> mFilterList;
    private List<Suggested_TreatementPojo> selectedList;
    private List<Suggested_TreatementPojo> totalChargeList;
    private AppCompatActivity activity;
    private AlertDialog removeAlert;
    private boolean isChecked;
    public static double totalval = 0.0f;


    public TestTreatFeeAdapter(Context context, List<Suggested_TreatementPojo> list, AppCompatActivity activity) {
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
        View view = inflater.inflate(R.layout.treatment_charge_check_box_row, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.treatmentName.setText(mFilterList.get(holder.getLayoutPosition()).getTname());
        holder.txtDiscountPrice.setText("₹. " + mFilterList.get(holder.getLayoutPosition()).getWhrdiscounted_price() + "");
        holder.txtActualPrice.setText("₹. " + mFilterList.get(holder.getLayoutPosition()).getFee() + "");
        holder.treatmentName.setChecked(mFilterList.get(holder.getLayoutPosition()).isCheck());

        if (Integer.parseInt(mFilterList.get(holder.getLayoutPosition()).getWhr_discount()) > 0) {
            holder.txtActualPrice.setPaintFlags(holder.txtActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (Integer.parseInt(mFilterList.get(holder.getLayoutPosition()).getWhr_discount()) == 0) {
            holder.txtActualPrice.setVisibility(View.GONE);
        }


        holder.relativeItem.setOnClickListener(v -> {
            this.onClickListener.onClick(mFilterList.get(holder.getLayoutPosition()));
        });

        holder.treatmentName.setOnClickListener(v -> {
            this.onClickListener.onClick(mFilterList.get(holder.getLayoutPosition()));
        });
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
        TextView  txtActualPrice, txtDiscountPrice;
        RelativeLayout relativeItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            treatmentName = itemView.findViewById(R.id.treatmentName);
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
        void onClick(Suggested_TreatementPojo d);
    }

    public void getFilterList(String searchKey) {
        mFilterList = new ArrayList<>();
        for (Suggested_TreatementPojo suggested_treatementPojo : list) {
            if (suggested_treatementPojo.getTname().toLowerCase().trim().contains(searchKey)) {
                suggested_treatementPojo.setCheck(suggested_treatementPojo.isCheck());
                mFilterList.add(suggested_treatementPojo);
            }
        }
        notifyDataSetChanged();
    }

    public List<Suggested_TreatementPojo> getTotaTreatmentlList() {
        return mFilterList;

    }
}
