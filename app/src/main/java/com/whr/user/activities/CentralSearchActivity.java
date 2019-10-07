package com.whr.user.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.model.CentralSearchModel;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomStringRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.booking.FilterActivity;
import com.whr.user.booking.adapters.RecyclerAdapterCentralSearch;
import com.whr.user.booking.diagnostics.booking.activities.NewPathalogyActivity;
import com.whr.user.booking.diagnostics.booking.activities.NewPathalogyDetailsActivity;
import com.whr.user.booking.doctor.booking.BookingDetailsAdmitNowActivity;
import com.whr.user.booking.doctor.booking.DetailDoctorActivity;
import com.whr.user.booking.doctor.booking.DoctorHospitalPackagesListActivity;
import com.whr.user.booking.doctor.booking.DetailHospitalActivity;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.booking.packages.hospital.activities.NewSurgeryPackageActivity;
import com.whr.user.booking.packages.hospital.activities.PackageHospitalsList;
import com.whr.user.booking.packages.hospital.model.SurgeryPackagesModel;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CentralSearchActivity extends AppCompatActivity {


    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    Context context;
    RecyclerView recyclerViewCentralSearch;
    String TAG = getClass().getSimpleName();
    RequestQueue mQueue;
    EditText searchBox;
    RecyclerAdapterCentralSearch recyclerAdapterCentralSearch;
    CoordinatorLayout coordinatorLayout;
    ArrayList<CentralSearchModel> arrayListTestTreatment = new ArrayList<>();
    Gson gson = new Gson();
    public final static String CENTRAL_SEARCH_KEY = "central_search_key";

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HideSoft(coordinatorLayout);
        finish();
        overridePendingTransitionExit();
    }

    PreferenceUtils pref;
    String url = GlobalVar.ServerAddress + "AndroidNew/GetAllSearchData";
    public static final String KEY_CENTRAL_SEARCH_DOCTOR = "central_search_doctor";
    ProgressBar pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_search);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        context = this;
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        searchBox = findViewById(R.id.auto_complete_textview);
        searchBox.setHint(getString(R.string.searchby));
        pref = new PreferenceUtils(context);
        GlobalVar.errorLog("ACTIVITY", TAG, "yes");
        pb_loading = findViewById(R.id.pb_loading);
        pb_loading.incrementProgressBy(10);
        pb_loading.setIndeterminate(true);

        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        TextView txtLocation = findViewById(R.id.txtLocation);

        txtLocation.setOnClickListener(v -> {
            Intent i = new Intent(context, LocationFindActivity.class);
            startActivityForResult(i, 1);
            overridePendingTransitionEnter();
        });

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        assert bundle != null;
        if (!bundle.isEmpty()) {
            boolean isCentralSearch = bundle.containsKey(KEY_CENTRAL_SEARCH_DOCTOR);
            boolean isCentralSearchPathology = bundle.containsKey(NewPathalogyActivity.KEY_CENTRAL_SEARCH_PATHOLOGY);

            if (isCentralSearch) {
                url = GlobalVar.ServerAddress + "AndroidNew/GetAllSearchDataForDoctor";
            }

            if (isCentralSearchPathology) {
                url = GlobalVar.ServerAddress + "AndroidNew/GetAllPathologySearchData";
            }


        }
        recyclerViewCentralSearch = findViewById(R.id.recyclerViewCentralSearch);
        LinearLayoutManager lLayout = new LinearLayoutManager(context);
        recyclerViewCentralSearch.setLayoutManager(lLayout);
        recyclerViewCentralSearch.setHasFixedSize(true);
        recyclerAdapterCentralSearch = new RecyclerAdapterCentralSearch(context, arrayListTestTreatment, RecyclerAdapterCentralSearch.TOP_SEARCH);
        recyclerViewCentralSearch.setAdapter(recyclerAdapterCentralSearch);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                String query1 = query.toString();
                if (query.length() > 1) {
                    getCentralData(query1);
                } else {
                    arrayListTestTreatment.clear();
                    recyclerAdapterCentralSearch.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recyclerAdapterCentralSearch.setOnClickListener(this::addSearchResult);
    }

    private void getNextActivity(CentralSearchModel c) {
        ///--------------------
        GlobalVar.hideProgressDialog();
        switch (c.getType()) {
            case "tbl_doctor": {
                Intent i = new Intent(context, DetailDoctorActivity.class);
                i.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, c);
                startNewActivity(i);
                break;
            }
            case "tbl_doctorsSpeciality": {
                Intent i = new Intent(context, DoctorHospitalPackagesListActivity.class);
                i.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, c);
                startNewActivity(i);
                break;
            }

            case "tbl_hospital":
                if (c.getName().contains("Multispeciality")) {
                    HospitalModel hospitalModel = new HospitalModel();
                    hospitalModel.setId(Integer.parseInt(c.getId()));
                    getDialogBookingBottom(hospitalModel);
                }
                break;

            case "tbl_hospitalsSpeciality":
                Intent i = new Intent(context, DoctorHospitalPackagesListActivity.class);
                i.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, c);
                FilterActivity.strFilterType = FilterActivity.HOSPITAL;
                FilterActivity.arrayList.add(FilterActivity.strFilterType);
                startNewActivity(i);
                break;

            ///central Search done up to here

            case "tbl_hospitalpackeges": {
                Intent i2 = new Intent(context, PackageHospitalsList.class);
                SurgeryPackagesModel surgeryPackagePojo = new SurgeryPackagesModel();
                surgeryPackagePojo.setPackid(Integer.parseInt(c.getId()));
                i2.putExtra(NewSurgeryPackageActivity.KEY_SURGERY_PACKAGE, surgeryPackagePojo);
                startNewActivity(i2);
                break;
            }

            case "tbl_pathologylabTest": {
                Bundle mBundle = new Bundle();
                Intent i2 = new Intent(context, NewPathalogyActivity.class);
                mBundle.putString("testid", c.getId());
                i2.putExtras(mBundle);
                i2.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                startNewActivity(i2);
                break;
            }
            case "tbl_pathologylab": {
                Intent intent = new Intent(context, NewPathalogyDetailsActivity.class);
                intent.putExtra("pathid", c.getId());
                intent.putExtra("pathname", c.getName());
                intent.putExtra("typeid", c.getTypeId());
                startActivity(intent);
                break;
            }
        }

        ///--------------------
    }

    public void addSearchResult(CentralSearchModel c) {
        String url = GlobalVar.ServerAddress + "AndroidNew/InsertSearch";
        GlobalVar.showProgressDialog(context, "", true);

        JSONObject obj = new JSONObject();
        //int id, long userId, string name, string type, string typeid
        try {
            obj.put("userId", pref.getUID());
            obj.put("id", c.getId());
            obj.put("name", c.getName());
            obj.put("type", c.getType());
            obj.put("typeid", c.getTypeId());
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "CentralSearchModel", c.toString());
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, response -> getNextActivity(c), error -> getNextActivity(c));

        mQueue.add(jsonObjectRequest);
    }

    private void getCentralData(final String query) {
        pb_loading.setVisibility(View.VISIBLE);

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, response -> {
            pb_loading.setVisibility(View.GONE);
            arrayListTestTreatment.clear();
            recyclerAdapterCentralSearch.notifyDataSetChanged();

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonData = jsonObject.getJSONArray("data");
                if (jsonData.length() > 0) {
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject json = jsonData.getJSONObject(i);
                        CentralSearchModel pojo = gson.fromJson(json.toString(),
                                CentralSearchModel.class);
                        arrayListTestTreatment.add(pojo);
                    }
                    recyclerAdapterCentralSearch.notifyDataSetChanged();
                } else {
                    // GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }, error -> {
            pb_loading.setVisibility(View.GONE);
            GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("text", query);
                map.put("city", "");
                Log.e("url", url);
                Log.e("url", map.toString());
                return map;

            }
        };
        mQueue.add(stringRequest);
    }

    void startNewActivity(Intent intent) {
        HideSoft(coordinatorLayout);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getDialogBookingBottom(HospitalModel hospitalModel) {

        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.layout_dialog_hosptial_book);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        RadioButton txtAdmitHospital = dialog.findViewById(R.id.txtAdmitHospital);
        RadioButton txtBookDoctor = dialog.findViewById(R.id.txtBookDoctor);
        RadioButton txtSurgery = dialog.findViewById(R.id.txtSurgery);
        TextView txtPayAdmit = dialog.findViewById(R.id.txtPayAdmit);
        txtPayAdmit.setVisibility(View.GONE);

        txtPayAdmit.setOnClickListener(v -> {
            dialog.cancel();
            dialog.dismiss();
            Intent intent = new Intent(context, BookingDetailsAdmitNowActivity.class);
            intent.putExtra(DoctorHospitalPackagesListActivity.KEY_HOSPITAL_MODEL, hospitalModel);
            startNewActivity(intent);
            //Booking Activity

        });

        txtAdmitHospital.setOnClickListener(view -> {
            txtBookDoctor.setChecked(false);
            txtSurgery.setChecked(false);
            txtPayAdmit.setVisibility(View.VISIBLE);
        });

        txtBookDoctor.setOnClickListener(v -> {
            dialog.cancel();
            dialog.dismiss();
            Intent intent = new Intent(context, DetailHospitalActivity.class);
            intent.putExtra(DoctorHospitalPackagesListActivity.KEY_HOSPITAL_MODEL, hospitalModel);
            intent.putExtra(DoctorHospitalPackagesListActivity.KEY_SCROLL_HOSPITAL, "doctors");
            startNewActivity(intent);
        });

        txtSurgery.setOnClickListener(v -> {
            dialog.cancel();
            dialog.dismiss();
            Intent intent = new Intent(context, DetailHospitalActivity.class);
            intent.putExtra(DoctorHospitalPackagesListActivity.KEY_HOSPITAL_MODEL, hospitalModel);
            intent.putExtra(DoctorHospitalPackagesListActivity.KEY_SCROLL_HOSPITAL, "packages");
            startNewActivity(intent);
        });

        dialog.show();
    }
}