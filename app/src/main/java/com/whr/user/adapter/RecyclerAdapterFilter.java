package com.whr.user.adapter;

//

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.whr.user.R;
import com.whr.user.pojo.DoctorTreatmentPojo;

import java.util.ArrayList;

public class RecyclerAdapterFilter extends RecyclerView.Adapter<RecyclerAdapterFilter.MyviewHolder> {

    Context context;
    LayoutInflater inflater;
    private ArrayList<DoctorTreatmentPojo> list;
    private ArrayList<DoctorTreatmentPojo> mFilterList;

    public RecyclerAdapterFilter(Context context, ArrayList<DoctorTreatmentPojo> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        mFilterList = list;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_filter, parent, false);
        MyviewHolder holder = new MyviewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {
        holder.checkBoxTreatment.setText(mFilterList.get(position).getTreatmentname());

        if (mFilterList.get(position).isCheck()) {
            holder.checkBoxTreatment.setChecked(true);
        } else {
            holder.checkBoxTreatment.setChecked(false);
        }

        holder.checkBoxTreatment.setOnClickListener(v -> {
            onCheckedItemListener.setCheckedItem(mFilterList.get(position));
            notifyDataSetChanged();
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


    class MyviewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxTreatment;

        public MyviewHolder(View itemView) {
            super(itemView);
            checkBoxTreatment = (CheckBox) itemView.findViewById(R.id.checkBoxTreatment);
        }
    }

    public ArrayList<DoctorTreatmentPojo> getCheckedList() {
        ArrayList<DoctorTreatmentPojo> arrayListSelected = new ArrayList<>();
        for (DoctorTreatmentPojo doctorTreatmentPojo : list) {
            if (doctorTreatmentPojo.isCheck())
                arrayListSelected.add(doctorTreatmentPojo);
        }
        return arrayListSelected;
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


    OnCheckedItemListener onCheckedItemListener;

    public void setOnCheckedItemListener(OnCheckedItemListener onCheckedItemListener) {
        this.onCheckedItemListener = onCheckedItemListener;
    }

    public interface OnCheckedItemListener {
        void setCheckedItem(DoctorTreatmentPojo d);
    }
}