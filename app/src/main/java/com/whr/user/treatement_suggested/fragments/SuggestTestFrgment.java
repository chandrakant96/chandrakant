package com.whr.user.treatement_suggested.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.SuggestTestPackages.SuggestTestAdapterDoctorList;
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

public class SuggestTestFrgment extends Fragment {
    private String url = GlobalVar.ServerAddress + "user/GetDoctorListOfSuggestTest";
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private PreferenceUtils preferenc;
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    private ConnectionDector connectionDector;
    private List<SuggestDoctorDetailsList> rowListItem;
    private SuggestTestAdapterDoctorList adapter;
    private Context context;
    private String suggestValue;
    private ArrayList<FamilyMemberListpojo> familyMemberList = new ArrayList<>();
    private Spinner famillyMemberSpinner;
    ConnectionDector dector;
    LinearLayout lineNoData;
    TextView txtError;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            // Suggest_Test  Suggest_Treatment
            suggestValue = getArguments().getString("suggest");
            familyMemberList = bundle.getParcelableArrayList("familyArray");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.suggest_test_treat_tab_recycler_view_xml, container, false);
        // suggest
        init(v);
        return v;
    }

    private void init(View v) {
        lineNoData = v.findViewById(R.id.lineNoData);
        rowListItem = new ArrayList<>();
        connectionDector = new ConnectionDector(getActivity());
        preferenc = new PreferenceUtils(getActivity());
        context = getActivity();
        dector = new ConnectionDector(context);
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        mQueue = CustomVolleyRequestQueue.getInstance(getActivity()).getRequestQueue();
        recyclerView = v.findViewById(R.id.suggesttesttreatment_recyclerView);
        famillyMemberSpinner = v.findViewById(R.id.famillyMemberSpinner);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        txtError = v.findViewById(R.id.txtError);
        txtError.setText("No Data Found");

        if (familyMemberList.size() > 0) {
            final ArrayAdapter<FamilyMemberListpojo> familyspinnerArrayAdapter =
                    new ArrayAdapter<>(context, R.layout.family_spinner_row, R.id.tvCategory, familyMemberList);
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

    String TAG = getClass().getSimpleName();

    private void getDoctorList(final long familyid) {
        if (dector.isConnectingToInternet()) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("pid", familyid);
                GlobalVar.errorLog(TAG, "obj", obj.toString());
                GlobalVar.errorLog(TAG, "url", url);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                    response -> {
                        rowListItem.clear();
                        Log.e("getDoctorList", response.toString());
                        Gson gson = new Gson();
                        JSONArray jsonArray1 = null;
                        try {
                            jsonArray1 = response.getJSONArray("result");
                            if (jsonArray1 != null && jsonArray1.length() > 0) {
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json = jsonArray1.getJSONObject(i);
                                    SuggestDoctorDetailsList pojo = gson.fromJson(json.toString(), SuggestDoctorDetailsList.class);
                                    rowListItem.add(pojo);
                                }
                                adapter = new SuggestTestAdapterDoctorList(context, rowListItem, SuggestTestFrgment.this, suggestValue, familyid, familyMemberList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
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
                    }, error -> GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red));
            jsonRequest.setTag("get");
            mQueue.add(jsonRequest);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
        }
    }
}