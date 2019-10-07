
package com.whr.user.pay_bill.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pay_bill.SuggestDoctorDetailsList;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 6/9/2017.
 */

public class PayBillFragment extends Fragment {

    private PayBillDoctorListFragemntAdapter adapter;
    private Context context;
    private LinearLayoutManager lLayout;
    private List<SuggestDoctorDetailsList> rowListItem;
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    private ConnectionDector connectionDector;
    private RecyclerView recyclerView;
    private PreferenceUtils preferenceUtils;
    private List<FamilyMemberListpojo> familyMemberList = new ArrayList<>();
    private Spinner famillyMemberSpinner;
    String TAG = getClass().getSimpleName();
    LinearLayout lineNoData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            familyMemberList = bundle.getParcelableArrayList("familyArray");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.suggest_test_treat_tab_recycler_view_xml, null, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        intit(v);
        return v;
    }

    private void intit(View v) {
        context = getActivity();
        preferenceUtils = new PreferenceUtils(getActivity());
        connectionDector = new ConnectionDector(getActivity());
        rowListItem = new ArrayList<>();
        lineNoData = v.findViewById(R.id.lineNoData);
        //   coordinatorLayout  = (CoordinatorLayout) v.findViewById(R.id.order_medicine_fragment_layoutcontainer);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        famillyMemberSpinner = v.findViewById(R.id.famillyMemberSpinner);
        recyclerView = v.findViewById(R.id.suggesttesttreatment_recyclerView);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);

        if (familyMemberList.size() > 0) {
            final ArrayAdapter<FamilyMemberListpojo> familyspinnerArrayAdapter =
                    new ArrayAdapter<>(context, R.layout.family_spinner_row, R.id.tvCategory, familyMemberList);
            famillyMemberSpinner.setAdapter(familyspinnerArrayAdapter);

            famillyMemberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    FamilyMemberListpojo selectedItemText = (FamilyMemberListpojo) parent.getItemAtPosition(position);
                    if (connectionDector.isConnectingToInternet()) {
                        methodVoid(String.valueOf(selectedItemText.getFamilyId()), selectedItemText.getFamilyName());
                    } else {
                        GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            methodVoid(preferenceUtils.getUID(), preferenceUtils.getUserName());
        }

    }

    Gson gson = new Gson();

    public void methodVoid(final String uid, final String name) {
        JSONObject obj = new JSONObject();
        String url = GlobalVar.ServerAddress + "AndroidNew/GetDoctorListOfPayBill";
        try {
            //  obj.put("uid", uid);
            obj.put("uid", uid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        GlobalVar.errorLog(TAG, "obj", obj.toString());
        GlobalVar.errorLog(TAG, "url", url);
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj, response -> {
            rowListItem.clear();
            GlobalVar.errorLog(TAG, "response", response.toString());
            JSONArray jsonArray1 = null;
            rowListItem.clear();
            try {
                jsonArray1 = response.getJSONArray("result");
                if (jsonArray1 != null && jsonArray1.length() > 0) {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json = jsonArray1.getJSONObject(i);
                        SuggestDoctorDetailsList pojo = gson.fromJson(json.toString(), SuggestDoctorDetailsList.class);
                        rowListItem.add(pojo);
                    }

                    adapter = new PayBillDoctorListFragemntAdapter(context, rowListItem, PayBillFragment.this, uid, name);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    lineNoData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    lineNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    //  GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red));
        mQueue.add(jsonRequest);
    }
}