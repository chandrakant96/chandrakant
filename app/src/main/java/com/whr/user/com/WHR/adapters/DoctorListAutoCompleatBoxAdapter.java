package com.whr.user.com.WHR.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.pojo.ShowDoctorListFromHospitalActivityPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2/13/2017.
 */

public class DoctorListAutoCompleatBoxAdapter extends ArrayAdapter<ShowDoctorListFromHospitalActivityPojo> {


    Context context;
    int resource;
    public List<ShowDoctorListFromHospitalActivityPojo> mList, filteredList;


    private boolean isString;

    public DoctorListAutoCompleatBoxAdapter(Context context, int resource,
                                            List<ShowDoctorListFromHospitalActivityPojo> mList, boolean isString) {
        super(context, resource);

        this.mList = mList;
        this.filteredList = new ArrayList<>();
        this.context = context;
        this.isString = isString;

    }

    @Override
    public ShowDoctorListFromHospitalActivityPojo getItem(int position) {

        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public void remove(ShowDoctorListFromHospitalActivityPojo object) {
        if (mList != null)
            mList.remove(object);
        else
            filteredList.remove(object);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.medicine_autocompleate_text_row, parent, false);
            TextView medicineName = (TextView) view.findViewById(R.id.medicineName);
            TextView medicineId = (TextView) view.findViewById(R.id.medicineId);
            //   TextView cname = (TextView) view.findViewById(R.id.cname);
            medicineId.setText(mList.get(position).getDid() + "");
            medicineName.setText(mList.get(position).getDname());
            //   cname.setText(mList.get(position). getCname());


        }
        ShowDoctorListFromHospitalActivityPojo model = mList.get(position);
        if (model != null) {
            TextView medicineName = (TextView) view.findViewById(R.id.medicineName);
            if (medicineName != null) {
                medicineName.setText(model.getDname());
            }

            TextView medicineId = (TextView) view.findViewById(R.id.medicineId);
            if (medicineId != null) {
                medicineId.setText(model.getDid() + "");
            }
          /*  TextView cname = (TextView) view.findViewById(R.id.cname);
            if (cname != null) {
                cname.setText(model.getCname()+"");
            }*/

        }
        return view;
    }


    @Override
    public Filter getFilter() {
        return new DoctorListFilterAdapter(this, mList, isString);
    }


}


