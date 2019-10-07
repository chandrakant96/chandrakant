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
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pay_bill.PayBillHistoryPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryFragment extends Fragment {
    private PayBillHistoryAdapter adapter;
    private Context context;
    private LinearLayoutManager lLayout;
    private List<PayBillHistoryPojo> rowListItem = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    private ConnectionDector connectionDector;
    private RecyclerView recyclerView;
    private int did;
    private PreferenceUtils preferenceUtils;
    private Spinner famillyMemberSpinner;
    private long familyid;
    private List<FamilyMemberListpojo> familyMemberList = new ArrayList<>();
    private String familyName = "";
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
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        recyclerView = v.findViewById(R.id.suggesttesttreatment_recyclerView);
        familyid = Long.parseLong(preferenceUtils.getUID());
        familyName = preferenceUtils.getUserName();
        famillyMemberSpinner = v.findViewById(R.id.famillyMemberSpinner);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        lineNoData = v.findViewById(R.id.lineNoData);

        if (familyMemberList.size() > 0) {
            final ArrayAdapter<FamilyMemberListpojo> familyspinnerArrayAdapter =
                    new ArrayAdapter<>(context, R.layout.family_spinner_row, R.id.tvCategory, familyMemberList);
            famillyMemberSpinner.setAdapter(familyspinnerArrayAdapter);

            famillyMemberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    FamilyMemberListpojo selectedItemText = (FamilyMemberListpojo) parent.getItemAtPosition(position);
                    if (connectionDector.isConnectingToInternet()) {
                        methodVoid(selectedItemText.getFamilyId());
                    } else {
                        GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            methodVoid(Long.parseLong(preferenceUtils.getUID()));
        }
    }

    private void methodVoid(long uid) {
        String url = GlobalVar.ServerAddress + "AndroidNew/GetHistoryforPayment";
        JSONObject obj = new JSONObject();
        try {
            obj.put("userid", uid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, response -> {
            rowListItem.clear();
            GlobalVar.errorLog(TAG, "response", response.toString());
            Gson gson = new Gson();
            JSONArray jsonArray1 = null;
            try {
                jsonArray1 = response.getJSONArray("History");
                if (jsonArray1.length() > 0) {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json = jsonArray1.getJSONObject(i);
                        PayBillHistoryPojo pojo = gson.fromJson(json.toString(), PayBillHistoryPojo.class);
                        rowListItem.add(pojo);
                    }
                    adapter = new PayBillHistoryAdapter(context, rowListItem, familyid, familyName);
                    recyclerView.setAdapter(adapter);
                    lineNoData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    lineNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red));
        mQueue.add(jsonRequest);
    }
}
