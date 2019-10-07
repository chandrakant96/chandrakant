package com.whr.user.booking.doctor.booking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.doctor.booking.adapters.HospitalPackagesHorizontalAdapter;
import com.whr.user.booking.doctor.booking.adapters.HospitalPackagesWardHorizontalAdapter;
import com.whr.user.booking.doctor.booking.adapters.PackageParticularAdapter;
import com.whr.user.booking.doctor.booking.models.HospitalProfile;
import com.whr.user.booking.doctor.booking.models.PackagesDetails;
import com.whr.user.booking.packages.hospital.activities.PackageHospitalsList;
import com.whr.user.booking.packages.hospital.model.PackageInfo;
import com.whr.user.booking.packages.hospital.model.SurgeryPackageInHospitals;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HospitalPackageDetailsActivity extends AppCompatActivity {

    TextView txtPackageName, txtTotalAmount, txtDiscountAmount, txtIncluded, txtExcluded, txtTerms, txtTotalStay;
    RecyclerView recyclerViewWard, recyclerViewParticular;
    Context context;
    int selectedPosition = 0;
    String TAG = getClass().getSimpleName();
    RequestQueue mQueue;
    HospitalProfile.HospitalPackages hospitalPackages;
    List<HospitalProfile.HospitalPackages.Packagesrelation> packagesrelations = new ArrayList<>();
    List<PackagesDetails.PerticularDetails> perticularDetails = new ArrayList<>();
    PackageParticularAdapter packageParticularAdapter;
    TextView txtBook, txtPackageTitle,txtHospitalName,packageDescription;
    PackagesDetails packagesDetails;
    public static final String HOSPITAL_PACKAGE_DETAILS_KEY = "HOSPITAL_PACKAGE_DETAILS";
    HospitalPackagesWardHorizontalAdapter adapter;
    PackageInfo packageInfo;
    SurgeryPackageInHospitals inHospitals;
    HospitalProfile.HospitalInfo hospitalInfo;
    ConnectionDector dector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_package_details);
        context = this;
        dector = new ConnectionDector(context);
        txtPackageTitle = findViewById(R.id.txtPackageTitle);
        txtHospitalName = findViewById(R.id.txtHospitalName);
        packageDescription = findViewById(R.id.packageDescription);
        txtBook = findViewById(R.id.txtBook);
        txtPackageName = findViewById(R.id.txtPackageName);
        txtTotalStay = findViewById(R.id.txtTotalStay);
        recyclerViewParticular = findViewById(R.id.recyclerViewParticular);
        recyclerViewParticular.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewParticular.setHasFixedSize(true);
        recyclerViewWard = findViewById(R.id.recyclerViewWard);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewWard.setLayoutManager(linearLayoutManager);
        recyclerViewWard.setHasFixedSize(true);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtDiscountAmount = findViewById(R.id.txtDiscountAmount);
        txtIncluded = findViewById(R.id.txtIncluded);
        txtExcluded = findViewById(R.id.txtExcluded);
        txtTerms = findViewById(R.id.txtTerms);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        packageParticularAdapter = new PackageParticularAdapter(context, perticularDetails);
        recyclerViewParticular.setAdapter(packageParticularAdapter);
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();

        if (intent.getExtras().containsKey(HospitalPackagesHorizontalAdapter.HOSPITAL_PACKAGES_KEY)) {
            hospitalPackages = intent.getParcelableExtra(HospitalPackagesHorizontalAdapter.HOSPITAL_PACKAGES_KEY);
            hospitalInfo = intent.getParcelableExtra(HospitalPackagesHorizontalAdapter.HOSPITAL_INFO);
            GlobalVar.errorLog(TAG, "hospitalInfo", hospitalInfo.toString());
            if (intent.getExtras().containsKey(HospitalPackagesHorizontalAdapter.HOSPITAL_PACKAGES_SELECTED_KEY)) {
                selectedPosition = intent.getIntExtra(HospitalPackagesHorizontalAdapter.HOSPITAL_PACKAGES_SELECTED_KEY,
                        0);
            }
            txtPackageName.setText(hospitalPackages.getPackagename());
            txtHospitalName.setText(hospitalInfo.getHospitalname());
            txtPackageTitle.setText(hospitalPackages.getPackagename());
            packageDescription.setText(hospitalPackages.getDescription());
            packagesrelations = hospitalPackages.getPackagesrelation();
            adapter = new HospitalPackagesWardHorizontalAdapter(context, packagesrelations, selectedPosition);
            recyclerViewWard.setAdapter(adapter);
            getHospitalDetails(selectedPosition);
            adapter.setOnClickListener(selectionPosition -> getHospitalDetails(selectionPosition));
            txtBook.setOnClickListener(view -> {
                Intent intent1 = new Intent(context, HospitalPackagesCalenderActivity.class);
                intent1.putExtra(HOSPITAL_PACKAGE_DETAILS_KEY, packagesDetails);
                startActivity(intent1);
            });
        } else if (intent.getExtras().containsKey(PackageHospitalsList.KEY_PACKAGE_INFO)) {
            packageInfo = intent.getParcelableExtra(PackageHospitalsList.KEY_PACKAGE_INFO);
            inHospitals = intent.getParcelableExtra(PackageHospitalsList.KEY_HOSPITAL_INFO);
            Log.e(TAG, packageInfo.toString());
            Log.e(TAG, inHospitals.toString());
            txtPackageName.setText(packageInfo.getPackegename());
            txtPackageTitle.setText(packageInfo.getPackegename());
            txtHospitalName.setText(inHospitals.getHospitalname());
            packageDescription.setVisibility(View.GONE);
            packagesrelations = packageInfo.getPackagesrelation();
            adapter = new HospitalPackagesWardHorizontalAdapter(context,
                    packagesrelations, selectedPosition);
            recyclerViewWard.setAdapter(adapter);
            adapter.setOnClickListener(selectionPosition -> getHospitalDetails(selectionPosition));
            txtBook.setOnClickListener(view -> {
                Intent intent1 = new Intent(context, HospitalPackagesCalenderActivity.class);
                intent1.putExtra(HOSPITAL_PACKAGE_DETAILS_KEY, packagesDetails);
                startActivity(intent1);
            });

            if (dector.isConnectingToInternet()) {
                getHospitalDetails(selectedPosition);
            } else {
                startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
                overridePendingTransitionExit();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getHospitalDetails(selectedPosition);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransitionExit();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    Gson gson = new Gson();

    private void getHospitalDetails(int selectedPosition) {

        JSONObject obj = new JSONObject();
        try {
            obj.put("packagesubid", packagesrelations.get(selectedPosition).getHospitalPackagetableSecondId());
            obj.put("ward", packagesrelations.get(selectedPosition).getWard());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.showProgressDialog(this, "", false);
        String url = GlobalVar.ServerAddress + "AndroidNew/GetHospitalPackageDetailss";
        GlobalVar.errorLog(TAG, " url", url);
        GlobalVar.errorLog(TAG, " obj", obj.toString());

        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    JSONObject jsonObject = null;
                    //Log.e(TAG, response.toString());
                    GlobalVar.hideProgressDialog();
                    perticularDetails.clear();

                    try {
                        jsonObject = response.getJSONObject("result");
                        packagesDetails = gson.fromJson(jsonObject.toString()
                                , PackagesDetails.class);
                        txtTotalStay.setText(packagesDetails.getStay());
                        txtDiscountAmount.setText("Rs. " + GlobalVar.priceWithoutDecimal(Double.valueOf(packagesDetails.getDiscountprice())));
                        txtTotalAmount.setText("Rs. " + GlobalVar.priceWithoutDecimal(Double.valueOf(packagesDetails.getTotaltreatmentfee())));
                        txtExcluded.setText(packagesDetails.getExcluded());
                        txtIncluded.setText(packagesDetails.getIncluded());
                        txtTerms.setText(packagesDetails.getTerms());
                        perticularDetails.addAll(packagesDetails.getPerticularDetails());
                        packageParticularAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> GlobalVar.hideProgressDialog());
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }
}
