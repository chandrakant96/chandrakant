package com.whr.user.com.WHR.adapters;

/**
 * Created by lenovo on 4/12/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.DoctorSpecialitiesModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lenovo on 4/12/2017.
 */

public class SpecializationFilterAdapter extends ArrayAdapter<DoctorSpecialitiesModel> {
    private Context context;
    private int resource;
    public List<DoctorSpecialitiesModel> mList, filteredList;

    public SpecializationFilterAdapter(Context context, int resource, List<DoctorSpecialitiesModel> rowListItem) {

        super(context, resource);
        this.context = context;
        this.mList = rowListItem;

        this.filteredList = new ArrayList<>();

    }

    @Override
    public DoctorSpecialitiesModel getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getPosition(DoctorSpecialitiesModel item) {
        return mList.indexOf(item);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.medicine_autocompleate_text_row, parent, false);
            TextView medicineName = (TextView) view.findViewById(R.id.medicineName);
            TextView medicineId = (TextView) view.findViewById(R.id.medicineId);
            TextView userImageUrl = (TextView) view.findViewById(R.id.medicinPrice);

            medicineName.setText(mList.get(position).getName());
            medicineId.setText(mList.get(position).getId() + "");
            // userImageUrl.setText(mList.get(position).getProfile_img());
        }
        DoctorSpecialitiesModel model = mList.get(position);
        if (model != null) {
            TextView medicineName = (TextView) view.findViewById(R.id.medicineName);
            if (medicineName != null) {
                medicineName.setText(model.getName());
            }


            TextView medicineId = (TextView) view.findViewById(R.id.medicineId);
            if (medicineId != null) {
                medicineId.setText(model.getId() + "");
            }

         /*   TextView userImageUrl = (TextView) view.findViewById(R.id.userImageUrl);
            if (userImageUrl != null) {
                userImageUrl.setText(model.getProfile_img()+"");
            }*/
        }
        return view;
    }

    @Override
    public void remove(DoctorSpecialitiesModel object) {
        if (mList != null)
            mList.remove(object);
        else
            filteredList.remove(object);
    }


    @Override
    public Filter getFilter() {
        return nameFilter;
        // return new AutocomplFilter(this, mList);
    }


    Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                // Your filtering logic goes in here
                for (DoctorSpecialitiesModel userInfo : mList) {


                    if (userInfo.getName().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(userInfo);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {


            mList.clear();
            mList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
    //===
}
