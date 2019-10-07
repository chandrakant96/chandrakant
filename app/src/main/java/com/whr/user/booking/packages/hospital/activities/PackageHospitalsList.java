package com.whr.user.booking.packages.hospital.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoDataAvailableActivity;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.doctor.booking.HospitalPackageDetailsActivity;
import com.whr.user.booking.packages.hospital.adapters.PackageDetailAdapter;
import com.whr.user.booking.packages.hospital.model.PackageInfo;
import com.whr.user.booking.packages.hospital.model.SurgeryPackageInHospitals;
import com.whr.user.booking.packages.hospital.model.SurgeryPackagesModel;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.activities.LocationFindActivity;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PackageHospitalsList extends AppCompatActivity {

    String packid = "";
    private TextView title;
    private TextView txtPackageName, txtDiscription, txtHospitalName;
    private RecyclerView recyclerView;

    private RequestQueue mQueue;
    PackageDetailAdapter adapter;
    private LinearLayoutManager lLayout;
    private List<SurgeryPackageInHospitals> list;
    String url = GlobalVar.ServerAddress + "AndroidNew/HospitalPackageDetails";
    private AppCompatActivity activity;
    private CoordinatorLayout coordinatorLayout;
    private String TAG = getClass().getSimpleName();
    private Context context;
    private MaterialRippleLayout imgBack;
    PackageInfo packageInfo;
    public static final String KEY_PACKAGE_INFO = "key_package_info";
    public static final String KEY_HOSPITAL_INFO = "key_hospital_info";
    ConnectionDector dector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);

        context = PackageHospitalsList.this;
        activity = PackageHospitalsList.this;
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        dector = new ConnectionDector(context);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        recyclerView = findViewById(R.id.recycleviewdetail);
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        title = findViewById(R.id.txtTitle);
        txtPackageName = findViewById(R.id.txtPackageName);
        txtDiscription = findViewById(R.id.txtDiscription);
        txtHospitalName = findViewById(R.id.txtHospitalName);
        list = new ArrayList<>();
        adapter = new PackageDetailAdapter(context, list, activity);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (!Objects.requireNonNull(bundle).isEmpty()) {
            boolean isSurgeryPackages = bundle.containsKey(NewSurgeryPackageActivity.KEY_SURGERY_PACKAGE);

            if (isSurgeryPackages) {
                SurgeryPackagesModel surgeryPackagesModel = bundle.getParcelable(NewSurgeryPackageActivity.KEY_SURGERY_PACKAGE);
                packid = String.valueOf(surgeryPackagesModel.getPackid());
            }
        }
        if (dector.isConnectingToInternet()) {
            getData();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }


        adapter.setClickLister(s -> {
            Log.e(TAG, s.toString());
            Log.e(TAG, packageInfo.toString());

            Intent intent1 = new Intent(context, HospitalPackageDetailsActivity.class);
            intent1.putExtra(KEY_HOSPITAL_INFO, s);
            intent1.putExtra(KEY_PACKAGE_INFO, packageInfo);
            startActivity(intent1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getData();
        }
    }

    Gson gson = new Gson();

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void getData() {
        GlobalVar.showProgressDialog(this, "Loading.....", true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("packageid", packid);
            obj.put("lat", LocationFindActivity.lat);
            obj.put("log", LocationFindActivity.lon);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj, response -> {
            GlobalVar.errorLog(TAG, "GetPackageList", response.toString());
            GlobalVar.hideProgressDialog();
            list.clear();
            JSONArray jsonArray1 = null;
            try {
                JSONObject jsonObjectPackInfo = response.getJSONObject("packegeinfo");
                packageInfo = gson.fromJson(jsonObjectPackInfo.toString(), PackageInfo.class);
                GlobalVar.errorLog(TAG, "packageInfo", packageInfo.toString());
                title.setText(packageInfo.getPackegename());
                txtDiscription.setText(packageInfo.getDescription());
                txtPackageName.setText(packageInfo.getPackegename());

                jsonArray1 = response.getJSONArray("Packegedetail");
                if (jsonArray1 != null && jsonArray1.length() > 0) {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json = jsonArray1.getJSONObject(i);
                        SurgeryPackageInHospitals pojo = gson.fromJson(json.toString(), SurgeryPackageInHospitals.class);
                        list.add(pojo);
                    }
                    adapter.notifyDataSetChanged();
                    txtHospitalName.setText("Hospitals" + " (" + list.size() + ")");
                } else {
                    //  showSnackBar(getString(R.string.NoDataAvailable));
                    startActivityForResult(new Intent(context, NoDataAvailableActivity.class), GlobalVar.NO_DATA_AVAILABLE);
                    overridePendingTransitionEnter();
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
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}