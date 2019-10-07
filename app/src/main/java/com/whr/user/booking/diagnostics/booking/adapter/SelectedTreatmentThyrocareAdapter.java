package com.whr.user.booking.diagnostics.booking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.booking.diagnostics.booking.model.ThyroCarePojo;

import java.util.ArrayList;
import java.util.List;

public class SelectedTreatmentThyrocareAdapter extends RecyclerView.Adapter<SelectedTreatmentThyrocareAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    private List<ThyroCarePojo> list;
    private List<ThyroCarePojo> mFilterList;
    private List<ThyroCarePojo> totalChargeList;
    private AppCompatActivity activity;
    private AlertDialog removeAlert;
    private boolean isChecked;


    public SelectedTreatmentThyrocareAdapter(Context context, List<ThyroCarePojo> list, AppCompatActivity activity) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.mFilterList = list;
        this.activity = activity;
        totalChargeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_treatment_selected, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.txtSelectTreatment.setText(list.get(position).getName());
        holder.txtSelectTreatment.setOnClickListener(v -> this.onClickListener.onClick(list.get(position)));

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
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtSelectTreatment;


        public MyViewHolder(View itemView) {
            super(itemView);
            txtSelectTreatment = itemView.findViewById(R.id.txtSelectTreatment);
        }
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(ThyroCarePojo d);
    }

    public void getFilterList(String searchKey) {
        mFilterList = new ArrayList<>();
        for (ThyroCarePojo ThyroCarePojo : list) {
            if (ThyroCarePojo.getName().toLowerCase().trim().contains(searchKey)) {
                ThyroCarePojo.setCheck(ThyroCarePojo.isCheck());
                mFilterList.add(ThyroCarePojo);
            }
        }
        notifyDataSetChanged();
    }
}
