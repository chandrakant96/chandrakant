package com.whr.user.treatement_suggested.fragments;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.SuggestTestPackages.SuggestTreatmentAdpter;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pay_bill.SuggestDoctorDetailsList;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SuggestTrearmentFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private PreferenceUtils preferenc;
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    private ConnectionDector connectionDector;
    private List<SuggestDoctorDetailsList> rowListItem = new ArrayList<>();
    private SuggestTreatmentAdpter adapter;
    private Context context;
    private List<FamilyMemberListpojo> familyMemberList;
    private String suggestValue;
    private Spinner famillyMemberSpinner;
    private String familyname;
    String TAG = getClass().getSimpleName();
    ProgressBar pb_loading;
    LinearLayout lineNoData;
    TextView txtError;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            suggestValue = getArguments().getString("suggest");
            familyMemberList = bundle.getParcelableArrayList("familyArray");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.suggest_test_treat_tab_recycler_view_xml, container, false);
        init(v);
        return v;
    }

    private void init(View v) {
        rowListItem = new ArrayList<>();
        connectionDector = new ConnectionDector(getActivity());
        preferenc = new PreferenceUtils(getActivity());
        context = getActivity();
        dector = new ConnectionDector(context);
        pb_loading = v.findViewById(R.id.pb_loading);
        pb_loading.incrementProgressBy(10);
        pb_loading.setIndeterminate(true);
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        mQueue = CustomVolleyRequestQueue.getInstance(getActivity()).getRequestQueue();
        recyclerView = v.findViewById(R.id.suggesttesttreatment_recyclerView);
        famillyMemberSpinner = v.findViewById(R.id.famillyMemberSpinner);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        lineNoData = v.findViewById(R.id.lineNoData);
        txtError = v.findViewById(R.id.txtError);
        txtError.setText("No Data Found");
        if (connectionDector.isConnectingToInternet()) {
            familyname = preferenc.getUserName();
            //getDoctorList(Long.parseLong(preferenc.getUID()));
        } else {
            GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
        }

        if (familyMemberList.size() > 0) {
            final ArrayAdapter<FamilyMemberListpojo> familyspinnerArrayAdapter =
                    new ArrayAdapter<FamilyMemberListpojo>(context, R.layout.family_spinner_row, R.id.tvCategory, familyMemberList);
            famillyMemberSpinner.setAdapter(familyspinnerArrayAdapter);

            famillyMemberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    FamilyMemberListpojo selectedItemText = (FamilyMemberListpojo) parent.getItemAtPosition(position);
                    if (connectionDector.isConnectingToInternet()) {
                        getDoctorList(selectedItemText.getFamilyId());
                    } else {
                        GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            getDoctorList(Long.parseLong(preferenc.getUID()));
        }
    }

    Gson gson = new Gson();
    ConnectionDector dector;


    private void getDoctorList(final long familyid) {

        if (dector.isConnectingToInternet()) {


            pb_loading.setVisibility(View.VISIBLE);
            //  http://192.168.1.218/AndroidNew/GetDoctorListOfSuggestTreatment
            String url = GlobalVar.ServerAddress + "AndroidNew/GetDoctorListOfSuggestTreatment";
            JSONObject obj = new JSONObject();
            try {
                obj.put("pid", familyid);
                // obj.put("pid", /*familyid*/"8208441290");
                GlobalVar.errorLog(TAG, "obj", obj.toString());
                GlobalVar.errorLog(TAG, "url", url);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                    url, obj,
                    response -> {
                        GlobalVar.errorLog(TAG, "getDoctorList", response.toString());
                        pb_loading.setVisibility(View.GONE);
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
                                adapter = new SuggestTreatmentAdpter(context, rowListItem, SuggestTrearmentFragment.this, familyid, suggestValue, familyname);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setVisibility(View.VISIBLE);
                                lineNoData.setVisibility(View.GONE);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                lineNoData.setVisibility(View.VISIBLE);
                                //  GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                pb_loading.setVisibility(View.GONE);
                GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);

            });
            jsonRequest.setTag("get");
            mQueue.add(jsonRequest);
        } else {

        }
    }
}