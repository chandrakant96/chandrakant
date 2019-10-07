package com.whr.user.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.adapter.AppointmentHistoryAdapter;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.model.NewAppoinmentHistoryPojo;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentHistoryActivity extends AppCompatActivity {
    private PreferenceUtils pref;
    private RecyclerView recyclerView;
    private Context context;
    String TAG = getClass().getSimpleName();

    String url = GlobalVar.ServerAddress + "AndroidNew/AllAppointments";
    private AppointmentHistoryAdapter adapter;
    private List<NewAppoinmentHistoryPojo> list;
    private List<FamilyMemberListpojo> familyMemberList;
    private LinearLayoutManager lLayout;
    private Spinner famillyMemberSpinner;

    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    double familyid;
    Gson gson = new Gson();
    FamilyMemberListpojo familyMemberListpojoUpdate = new FamilyMemberListpojo();
    String familyId;
    String type = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment_history);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        context = AppointmentHistoryActivity.this;
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        pref = new PreferenceUtils(context);

        ImageView txtLocation = findViewById(R.id.txtLocation);
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        dector = new ConnectionDector(context);

        txtLocation.setOnClickListener(v -> getDialogFilter());

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        recyclerView = findViewById(R.id.AppoinmentHistoryDoctorRecycleView);
        famillyMemberSpinner = findViewById(R.id.famillyMemberSpinner);

        list = new ArrayList<>();
        familyMemberList = new ArrayList<>();
        adapter = new AppointmentHistoryAdapter(context, list);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(adapter);
        familyId = pref.getUID();
        getFamilyMemberList();
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfdefault = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();
    ConnectionDector dector;


    private void getData(String uid, String type) {
        if (dector.isConnectingToInternet()) {
            GlobalVar.showProgressDialog(this, "Loading.....", true);
            JSONObject obj = new JSONObject();
            try {
                obj.put("userid", uid);
                obj.put("type", type);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());

            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                    url, obj,
                    response -> {
                        GlobalVar.errorLog(TAG, "GetAppoinmentList", response.toString());
                        GlobalVar.hideProgressDialog();
                        Gson gson = new Gson();
                        list.clear();
                        JSONArray jsonArray1 = null;
                        try {
                            jsonArray1 = response.getJSONArray("Appointments");
                            if (jsonArray1 != null && jsonArray1.length() > 0) {
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json = jsonArray1.getJSONObject(i);
                                    NewAppoinmentHistoryPojo pojo = gson.fromJson(json.toString(), NewAppoinmentHistoryPojo.class);
                                    pojo.setIsdatePassed(checkTimeHasPass(pojo.getAppointmentDate() + " " + pojo.getAppointmentTime()));
                                    list.add(pojo);

                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                startActivityForResult(new Intent(context, NoDataAvailableActivity.class), GlobalVar.NO_DATA_AVAILABLE);
                                overridePendingTransitionEnter();
                                // showSnackBar(getString(R.string.NoDataAvailable));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                GlobalVar.hideProgressDialog();
                showSnackBar(VolleyErrorHelper.getMessage(error, context));
            });
            jsonRequest.setTag("get");
            mQueue.add(jsonRequest);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void getFamilyMemberList() {
        String url = GlobalVar.ServerAddress + "AndroidNew/MyFamilyNew";

        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    Log.e("famillyMemberList", response.toString());

                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("result");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            familyMemberList.clear();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                FamilyMemberListpojo model = gson.fromJson(json.toString(), FamilyMemberListpojo.class);
                                if (i == 0) {
                                    model.setFamilyName(model.getFamilyName() + " (MySelf)");
                                }
                                familyMemberList.add(model);
                            }

                            final ArrayAdapter<FamilyMemberListpojo> familyMemberAdapter =
                                    new ArrayAdapter<>(context, R.layout.family_spinner_row,
                                            R.id.tvCategory, familyMemberList);
                            famillyMemberSpinner.setAdapter(familyMemberAdapter);

                            famillyMemberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                    familyMemberListpojoUpdate = (FamilyMemberListpojo) adapterView.getItemAtPosition(position);
                                    familyid = familyMemberListpojoUpdate.getFamilyId();
//                                    getData(familyId);
                                    getData(familyId, type);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        } else {
                            getData(familyId, type);
                            GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoFamilyMemberAvailable), context, R.color.red);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red));
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    private void getDialogFilter() {
        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.layout_dialog_appointment_filter);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        RadioButton radioUpcoming = dialog.findViewById(R.id.radioUpcoming);
        RadioButton radioFollowUp = dialog.findViewById(R.id.radioFollowUp);
        RadioButton radioHistory = dialog.findViewById(R.id.radioHistory);
        RadioButton radioPathology = dialog.findViewById(R.id.radioPathology);

        radioUpcoming.setOnClickListener(v -> {
            dialog.cancel();
            type = "1";
            getData(familyId, type);

        });

        radioFollowUp.setOnClickListener(v -> {
            dialog.cancel();
            type = "2";
            getData(familyId, type);

        });
        radioHistory.setOnClickListener(v -> {
            dialog.cancel();
            type = "3";
            getData(familyId, type);


        });
        radioPathology.setOnClickListener(v -> {
            dialog.cancel();
            type = "4";
            getData(familyId, type);

        });


        dialog.show();
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
    SimpleDateFormat formatter4 = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());

    private boolean checkTimeHasPass(String t) {

        String strCurrentDate = formatter4.format(new Date());
        try {
            Date currentDate = formatter4.parse(strCurrentDate);
            Date adapterDate = formatter4.parse(t);
            if (currentDate.after(adapterDate)) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getData(familyId, type);
        }
    }
}