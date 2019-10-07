package com.whr.user.com.SuggesatNewActivityPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoDataAvailableActivity;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.doctor.booking.DetailDoctorActivity;
import com.whr.user.booking.doctor.booking.DoctorAppointmentBookingActivity;
import com.whr.user.booking.doctor.booking.DoctorHospitalPackagesListActivity;
import com.whr.user.booking.doctor.booking.adapters.DoctorHospitalPackagesFirstAidAdapter;
import com.whr.user.booking.doctor.booking.models.DoctorModel;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pojo.Suggested_TreatementPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TreatmentTestDoctorListByTreatmentActitivty extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<Object> rowListItem = new ArrayList<>();
    private Context context;
    private PreferenceUtils pref;
    private ConnectionDector connectionDector;
    private RequestQueue mQueue;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private String tratmentIdListstr, value;
    private DoctorHospitalPackagesFirstAidAdapter adapter;
    private String doctorId = "";
    String did = "";
    private long familyid;
    private String familyname;
    private List<Suggested_TreatementPojo> stList = new ArrayList<>();
    private AppCompatActivity activity;

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_test_doctor_list_by_treatment_actitivty);

        ///------------New Toolbar Code----------------
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        ///------------New Toolbar code------------------------------
        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean doctorTretkey = bundle.containsKey("doctorTreatmentList");
            boolean bookingDateBool = bundle.containsKey("booking_date");
            stList = bundle.getParcelableArrayList("stList");
            doctorId = bundle.getString("doctorId");
            tratmentIdListstr = bundle.getString("tratmentIdListstr");
            familyid = bundle.getLong("familyid");
            familyname = bundle.getString("familyname");
            did = bundle.getString("did");
        }
        init();
    }


    private void init() {
        context = TreatmentTestDoctorListByTreatmentActitivty.this;
        activity = TreatmentTestDoctorListByTreatmentActitivty.this;
        pref = new PreferenceUtils(context);
        connectionDector = new ConnectionDector(context);
        rowListItem = new ArrayList<>();
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        recyclerView = findViewById(R.id.activitySuggestTreatmentRecycleView);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        if (connectionDector.isConnectingToInternet()) {
            getPAthDocList();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
    }

    String TAG = getClass().getSimpleName();

    private void getPAthDocList() {
        GlobalVar.showProgressDialog(this, "Loading....", false);
        String url = GlobalVar.ServerAddress + "AndroidNew/getdoctorListByTreatmentId";
        JSONObject obj = new JSONObject();
        try {
            //  obj.put("testid", 2);
            obj.put("testid", tratmentIdListstr);
           // obj.put("did", did);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
            GlobalVar.errorLog(TAG, "url", url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    Log.e("getPAthDocList", response.toString());
                    Gson gson = new Gson();
                    JSONArray jsonArray1 = null;
                    rowListItem.clear();
                    try {
                        jsonArray1 = response.optJSONArray("DoctorList");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                DoctorModel pojo = gson.fromJson(json.toString(), DoctorModel.class);
                                pojo.setDistance(GlobalVar.distance(pojo.getLatitude(), pojo.getLongitude()));
                                rowListItem.add(pojo);
                            }

                            adapter = new DoctorHospitalPackagesFirstAidAdapter(context, rowListItem);
                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            adapter.setClickLister(new DoctorHospitalPackagesFirstAidAdapter.ClickLister() {
                                @Override
                                public void doctorBookClick(DoctorModel doctorModel) {
                                    GlobalVar.errorLog(TAG, DoctorHospitalPackagesListActivity.KEY_DOCTOR_MODEL, doctorModel.toString());
                                    Intent intent = new Intent(context, DoctorAppointmentBookingActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                                    intent.putExtra(DoctorHospitalPackagesListActivity.KEY_DOCTOR_MODEL, doctorModel);
                                    startNewActivity(intent);
                                }

                                @Override
                                public void doctorDetailClick(DoctorModel doctorModel) {
                                    GlobalVar.errorLog(TAG, DoctorHospitalPackagesListActivity.KEY_DOCTOR_MODEL, doctorModel.toString());
                                    Intent intent = new Intent(context, DetailDoctorActivity.class);
                                    intent.putExtra(DoctorHospitalPackagesListActivity.KEY_DOCTOR_MODEL, doctorModel);
                                    startNewActivity(intent);
                                }

                                @Override
                                public void hospitalBookClick(HospitalModel hospitalModel) {


                                }

                                @Override
                                public void hospitalDetailClick(HospitalModel hospitalModel) {

                                }

                                @Override
                                public void hospitalSurgeryClick(HospitalModel hospitalModel) {

                                }
                            });

                        } else {
                            startActivityForResult(new Intent(context,NoDataAvailableActivity.class),GlobalVar.NO_DATA_AVAILABLE);
                            overridePendingTransitionEnter();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    public void startNewActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransitionEnter();
    }


    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
            getPAthDocList();
        }
    }
}
