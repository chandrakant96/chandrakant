package com.whr.user.com.WHR.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.pojo.ReffredDoctorPojo;

import java.util.ArrayList;
import java.util.List;

public class ReffredDoctorListAdapter extends RecyclerView.Adapter<ReffredDoctorListAdapter.ReffredDoctorViewHJolder> {
    private Context context;
    private List<ReffredDoctorPojo> list;
    private List<ReffredDoctorPojo> filterList;


    private LayoutInflater inflater;

    public ReffredDoctorListAdapter(Context context, List<ReffredDoctorPojo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.filterList = this.list;
    }

    @Override
    public ReffredDoctorViewHJolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.new_reffred_doctor_adapter_row, parent, false);
        return new ReffredDoctorViewHJolder(view);
    }

    @Override
    public void onBindViewHolder(ReffredDoctorViewHJolder holder, int position) {
        ReffredDoctorPojo pojo = filterList.get(position);
        holder.txtFromDoctor.setText(pojo.getFromDoctorName() + "\n" + pojo.getFromDoctorDegreeSpecfication());
        holder.txtToDoctor.setText(pojo.getToDoctorName() + "\n" + pojo.getToDoctorDegreeSpecfication());
        holder.txtdate.setText(pojo.getReferOn());
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    class ReffredDoctorViewHJolder extends RecyclerView.ViewHolder {

        TextView txtFromDoctor, txtToDoctor, txtdate;

        public ReffredDoctorViewHJolder(View itemView) {
            super(itemView);
            txtFromDoctor = itemView.findViewById(R.id.txtFromDoctor);
            txtToDoctor = itemView.findViewById(R.id.txtToDoctor);
            txtdate = itemView.findViewById(R.id.txtdate);
        }
    }

    public void getFilter(String query) {
        filterList = new ArrayList<ReffredDoctorPojo>();
        for (int i = 0; i < list.size(); i++) {
            String text = list.get(i).getFromDoctorName().toLowerCase();
            String text2 = list.get(i).getToDoctorName().toLowerCase();
            if (text.contains(query) || text2.contains(query)) {
                filterList.add(list.get(i));
            }
        }
        notifyDataSetChanged();
    }


    public void getSelectedList(String query) {
        filterList = new ArrayList<ReffredDoctorPojo>();
        for (int i = 0; i < list.size(); i++) {
            String text = list.get(i).getFromDoctorName().toLowerCase();
            String text2 = list.get(i).getToDoctorName().toLowerCase();
            if (text.contains(query) || text2.contains(query)) {
                filterList.add(list.get(i));
            }
        }
        notifyDataSetChanged();
    }
}
