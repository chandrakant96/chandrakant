package com.whr.user.com.WHR.adapters;

import android.util.Log;
import android.widget.Filter;

import com.whr.user.pojo.ShowDoctorListFromHospitalActivityPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 3/17/2017.
 */

public class DoctorListFilterAdapter extends Filter {

    DoctorListAutoCompleatBoxAdapter adapter;
    List<ShowDoctorListFromHospitalActivityPojo> originalList;

    List<ShowDoctorListFromHospitalActivityPojo> filteredList;

    private boolean isString;

    public DoctorListFilterAdapter(DoctorListAutoCompleatBoxAdapter doctorListAutoCompleatBoxAdapter,
                                   List<ShowDoctorListFromHospitalActivityPojo> mList, boolean isString) {
        this.adapter = doctorListAutoCompleatBoxAdapter;
        this.originalList = mList;
        this.filteredList = new ArrayList<>();
        this.isString = isString;

    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            String filterPattern = constraint.toString().toLowerCase().trim();

            // Your filtering logic goes in here
            for (ShowDoctorListFromHospitalActivityPojo modelListPojo : originalList) {
               /* if (modelListPojo.getDname().toLowerCase().startsWith(filterPattern)) {
                    filteredList.add(modelListPojo);
                }
                */
                if (isString) {
                    try {
                        //   String strLong = Long.toString(modelListPojo.getDid());
                        String didStr = modelListPojo.getDid() + "";
                        Log.e("NumberFormater", didStr);

                        if (didStr.toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(modelListPojo);
                        }
                    } catch (Exception e) {
                        filteredList.clear();
                        Log.e("NumberFormater", e.getMessage());
                    }
                } else {

                    if (modelListPojo.getDname().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(modelListPojo);
                    }


                }


            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;

    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
    /*    adapter.mList.clear();
        adapter.mList.addAll((List) results.values);
        adapter.notifyDataSetChanged();
*/

        adapter.mList.clear();
        adapter.mList = new ArrayList<ShowDoctorListFromHospitalActivityPojo>();
        if (results.values != null) {
            adapter.mList.addAll((List) results.values);
        } else {
            adapter.mList = new ArrayList<ShowDoctorListFromHospitalActivityPojo>();
        }
        adapter.notifyDataSetChanged();


    }
}