package com.whr.user.booking.doctor.booking.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;

import java.util.ArrayList;
import java.util.List;

public class HospitalRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    public List<HospitalModel> list;
    private List<HospitalModel> mFilterList;
    private PreferenceUtils pref;
    private String clinicArray[];
    private int doctorSpecId = 0;
    private String value = "";
    private CoordinatorLayout coordinatorLayout;
    private boolean isalternative = false;
    private int alternativeTratmentId = 0;

    public String id;
    private AlertDialog paycallAlert;
    private ConnectionDector connectionDector;
    public String phno = "";
    private String notid;
    private String url;
    Handler handler;
    TextView fee;
    String TAG = getClass().getSimpleName();
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private RequestQueue mQueue;

    public HospitalRecyclerViewAdapter(Context context, List<HospitalModel> rowListItem, Activity activity,
                                       boolean favStatuseFromurl, int doctorSpecId, String value
            , double latitude, double longitude,
                                       CoordinatorLayout coordinatorLayout, boolean isalternative, int tid) {

        this.context = context;
        this.list = rowListItem;
        this.mFilterList = rowListItem;
        inflater = LayoutInflater.from(context);
        this.coordinatorLayout = coordinatorLayout;
        this.value = value;
        this.doctorSpecId = doctorSpecId;
        connectionDector = new ConnectionDector(this.context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        this.pref = new PreferenceUtils(context);
        this.isalternative = isalternative;
        this.alternativeTratmentId = tid;
    }

    public HospitalRecyclerViewAdapter(Context context, List<HospitalModel> rowListItem) {
        this.context = context;
        this.list = rowListItem;
        this.mFilterList = rowListItem;
        inflater = LayoutInflater.from(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, @SuppressLint("RecyclerView") final int position) {
        MyViewHolder holder = (MyViewHolder) holder1;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        viewHolder = getViewHolder(parent, inflater);
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.recycler_item_hospital, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    public void getFilter(String query) {
        mFilterList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String text = String.valueOf(list.get(i).getId()).toLowerCase();
            String text2 = list.get(i).getHospitalName().toLowerCase();
            String text3 = list.get(i).getHospitalName().toLowerCase();
            if (text.contains(query) || text2.contains(query) || text3.contains(query)) {
                mFilterList.add(list.get(i));
            }
        }
        notifyDataSetChanged();
    }

    ///----------------Webservices ----------------------

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void removeAt(int position) {
        mFilterList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mFilterList.size());
    }
}