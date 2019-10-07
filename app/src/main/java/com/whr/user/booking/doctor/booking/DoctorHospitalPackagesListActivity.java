package com.whr.user.booking.doctor.booking;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.activities.NoDataAvailableActivity;
import com.whr.user.model.CentralSearchModel;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomStringRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.CentralSearchActivity;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.FilterActivity;
import com.whr.user.booking.adapters.RecyclerAdapterCentralSearch;
import com.whr.user.booking.diagnostics.booking.activities.NewPathalogyActivity;
import com.whr.user.booking.diagnostics.booking.activities.NewPathalogyDetailsActivity;
import com.whr.user.booking.doctor.booking.adapters.DoctorHospitalPackagesFirstAidAdapter;
import com.whr.user.booking.doctor.booking.adapters.FilterRecyclerViewAdapter;
import com.whr.user.booking.doctor.booking.models.DoctorModel;
import com.whr.user.booking.doctor.booking.models.DoctorSpecialitiesModel;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.booking.packages.hospital.activities.NewSurgeryPackageActivity;
import com.whr.user.booking.packages.hospital.activities.PackageHospitalsList;
import com.whr.user.booking.packages.hospital.model.SurgeryPackagesModel;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.EndlessRecyclerOnScrollListener;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.activities.LocationFindActivity;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.whr.user.booking.doctor.booking.DoctorSpecialtiesActivity.KEY_SPECIALIZATION;

public class DoctorHospitalPackagesListActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout, lLayout2;
    private DoctorHospitalPackagesFirstAidAdapter adapter;
    private String specializationId = "1";
    DoctorSpecialitiesModel doctorSpecialitiesModel;
    private RequestQueue mQueue;
    private Activity activity;
    ArrayList<Object> rowListItem = new ArrayList<>();
    private EditText edSearch;
    private CoordinatorLayout coordinatorLayout;
    private String url = "";
    private PreferenceUtils pref;
    private ConnectionDector connectionDector;
    protected String TAG = getClass().getSimpleName();
    RecyclerView recyclerViewCentralSearch;

    TextView txtLocation;
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    TextView txtFilter;

    public static final String KEY_DOCTOR_MODEL = "DOCTOR_MODEL";
    public static final String KEY_HOSPITAL_MODEL = "HOSPITAL_MODEL";
    public static final String KEY_SCROLL_HOSPITAL = "SCROLL_HOSPITAL_PACKAGES";


    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    RecyclerView recyclerViewFilter1;
    FilterRecyclerViewAdapter filterRecyclerViewAdapter;
    CentralSearchModel c;
    LinearLayout layoutCentralSearch;
    RelativeLayout lineSearch;
    ProgressBar pb_loading;
    RecyclerAdapterCentralSearch adapterSearch;
    boolean isCentralSearch = false;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.doctor_specification_main_onclick_iteam);
        activity = this;
        context = DoctorHospitalPackagesListActivity.this;
        overridePendingTransitionEnter();
        pref = new PreferenceUtils(context);
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        layoutCentralSearch = findViewById(R.id.layoutCentralSearch);
        lineSearch = findViewById(R.id.lineSearch);
        pb_loading = findViewById(R.id.pb_loading);
        txtLocation = findViewById(R.id.txtLocation);

        txtLocation.setOnClickListener(v -> {
            Intent i = new Intent(context, LocationFindActivity.class);
            startActivityForResult(i, 1);
            overridePendingTransitionEnter();
        });

        connectionDector = new ConnectionDector(context);
        rowListItem = new ArrayList<>();
        edSearch = findViewById(R.id.edSearch);

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        assert bundle != null;
        if (!bundle.isEmpty()) {
            Log.e(TAG, "BUNDLE " + bundle.toString());
            boolean isSpecializationKey = bundle.containsKey(KEY_SPECIALIZATION);
            isCentralSearch = bundle.containsKey(CentralSearchActivity.CENTRAL_SEARCH_KEY);
            if (isSpecializationKey) {
                doctorSpecialitiesModel = bundle.getParcelable(KEY_SPECIALIZATION);
                specializationId = String.valueOf(doctorSpecialitiesModel.getId());
                edSearch.setHint("" + doctorSpecialitiesModel.getName());
            } else if (isCentralSearch) {
                c = bundle.getParcelable(CentralSearchActivity.CENTRAL_SEARCH_KEY);
                GlobalVar.errorLog(TAG, CentralSearchActivity.CENTRAL_SEARCH_KEY, c.toString());
                edSearch.setHint("" + c.getName());
            }
        }

        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtFilter = findViewById(R.id.txtFilter);

        recyclerView = findViewById(R.id.doctor_specificationOnclickIeamcleview);
        lLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);

        recyclerViewFilter1 = findViewById(R.id.recyclerViewFilter);
        lLayout2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFilter1.setLayoutManager(lLayout2);
        recyclerViewFilter1.setHasFixedSize(true);
        filterRecyclerViewAdapter = new FilterRecyclerViewAdapter(context, FilterActivity.arrayList);
        recyclerViewFilter1.setAdapter(filterRecyclerViewAdapter);

        adapter = new DoctorHospitalPackagesFirstAidAdapter(context, rowListItem);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        filterRecyclerViewAdapter.setOnClickListener(s -> {
            if (s.equalsIgnoreCase(FilterActivity.DOCTOR)) {

                if (FilterActivity.arrayList.contains(FilterActivity.strFilterConsultation)) {
                    FilterActivity.arrayList.remove(FilterActivity.strFilterConsultation);
                    FilterActivity.strFilterConsultation = "";
                }

                if (FilterActivity.arrayList.contains(FilterActivity.strFilterPathyName)) {
                    FilterActivity.arrayList.remove(FilterActivity.strFilterPathyName);
                    FilterActivity.strFilterPathyName = "";
                    FilterActivity.strPathyId = "";
                }

                FilterActivity.strFilterType = "";

            } else if (s.equalsIgnoreCase(FilterActivity.HOSPITAL)) {
                if (FilterActivity.arrayList.contains(FilterActivity.strFilterConsultation)) {
                    FilterActivity.arrayList.remove(FilterActivity.strFilterConsultation);
                    FilterActivity.strFilterConsultation = "";
                }

                if (FilterActivity.arrayList.contains(FilterActivity.strFilterPathyName)) {
                    FilterActivity.arrayList.remove(FilterActivity.strFilterPathyName);
                    FilterActivity.strFilterPathyName = "";
                    FilterActivity.strPathyId = "";
                }

                FilterActivity.strFilterType = "";

            } else if (s.equalsIgnoreCase(FilterActivity.strFilterPathyName)) {
                FilterActivity.strFilterPathyName = "";
            } else if (s.equalsIgnoreCase(FilterActivity.strFilterConsultation)) {
                FilterActivity.strFilterConsultation = "";
            } else if (s.equalsIgnoreCase(FilterActivity.strFilterLike)) {
                FilterActivity.strFilterLike = "";
            } else if (s.equalsIgnoreCase(FilterActivity.strFilterNearBy)) {
                FilterActivity.strFilterNearBy = "";
            }

            FilterActivity.arrayList.remove(s);
            filterRecyclerViewAdapter.notifyDataSetChanged();
            if (connectionDector.isConnectingToInternet()) {
                GetDoctorList(0);
            } else {
                startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
                overridePendingTransitionEnter();
            }
        });

        if (LocationFindActivity.myLocation != null) {
            if (LocationFindActivity.myLocation.length() > 0) {
                txtLocation.setText(LocationFindActivity.myLocation);
            } else {
                txtLocation.setText(getString(R.string.location));
            }
        } else {
            txtLocation.setText(getString(R.string.location));
        }


        if (connectionDector.isConnectingToInternet()) {
            GetDoctorList(0);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }


        adapter.setClickLister(new DoctorHospitalPackagesFirstAidAdapter.ClickLister() {
            @Override
            public void doctorBookClick(DoctorModel doctorModel) {
                GlobalVar.errorLog(TAG, KEY_DOCTOR_MODEL, doctorModel.toString());
                Intent intent = new Intent(context, DoctorAppointmentBookingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                intent.putExtra(KEY_DOCTOR_MODEL, doctorModel);
                startNewActivity(intent);
            }

            @Override
            public void doctorDetailClick(DoctorModel doctorModel) {
                GlobalVar.errorLog(TAG, KEY_DOCTOR_MODEL, doctorModel.toString());
                Intent intent = new Intent(context, DetailDoctorActivity.class);
                intent.putExtra(KEY_DOCTOR_MODEL, doctorModel);
                startNewActivity(intent);
            }

            @Override
            public void hospitalBookClick(HospitalModel hospitalModel) {
                GlobalVar.errorLog(TAG, KEY_HOSPITAL_MODEL, hospitalModel.toString());
                getDialogBookingBottom(hospitalModel);
            }

            @Override
            public void hospitalDetailClick(HospitalModel hospitalModel) {
                GlobalVar.errorLog(TAG, KEY_HOSPITAL_MODEL, hospitalModel.toString());
                Intent intent = new Intent(context, DetailHospitalActivity.class);
                intent.putExtra(KEY_HOSPITAL_MODEL, hospitalModel);
                intent.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, String.valueOf(isCentralSearch));
                intent.putExtra(KEY_SCROLL_HOSPITAL, "no");
                Log.e("hospitalDetailClick", String.valueOf(isCentralSearch));
                startNewActivity(intent);
            }

            @Override
            public void hospitalSurgeryClick(HospitalModel hospitalModel) {
                Intent intent = new Intent(context, DetailHospitalActivity.class);
                intent.putExtra(KEY_HOSPITAL_MODEL, hospitalModel);
                intent.putExtra(KEY_SCROLL_HOSPITAL, "packages");
                intent.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, String.valueOf(isCentralSearch));
                Log.e("hospitalSurgeryClick", String.valueOf(isCentralSearch));
                startNewActivity(intent);
            }
        });

        // recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
        txtFilter.setOnClickListener(view -> {
            Intent intent = new Intent(context, FilterActivity.class);
            intent.putExtra(KEY_SPECIALIZATION, doctorSpecialitiesModel);
            startActivityForResult(intent, 333);
        });

        layoutCentralSearch.setVisibility(View.GONE);
        lineSearch.setBackgroundColor(context.getResources().getColor(R.color.primary));
        txtFilter.setVisibility(View.VISIBLE);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.length() > 0) {
                    layoutCentralSearch.setVisibility(View.VISIBLE);
                    lineSearch.setBackgroundColor(context.getResources().getColor(R.color.white));
                    txtFilter.setVisibility(View.GONE);
                    getCentralData(query.toString());
                } else {
                    layoutCentralSearch.setVisibility(View.GONE);
                    lineSearch.setBackgroundColor(context.getResources().getColor(R.color.primary));
                    txtFilter.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edSearch.setOnClickListener(v -> {
            layoutCentralSearch.setVisibility(View.VISIBLE);
            lineSearch.setBackgroundColor(context.getResources().getColor(R.color.white));
            txtFilter.setVisibility(View.GONE);
        });
        recyclerViewCentralSearch = findViewById(R.id.recyclerViewCentralSearch);
        LinearLayoutManager lLayout3 = new LinearLayoutManager(context);
        recyclerViewCentralSearch.setLayoutManager(lLayout3);
        recyclerViewCentralSearch.setHasFixedSize(true);
        adapterSearch = new RecyclerAdapterCentralSearch(context, listSearch, RecyclerAdapterCentralSearch.CENTRAL_SEARCH);
        recyclerViewCentralSearch.setAdapter(adapterSearch);
        adapterSearch.setOnClickListener(this::addSearchResult);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransitionExit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
            GetDoctorList(0);
        } else if (requestCode == 333 && resultCode == Activity.RESULT_OK) {

            if (data.getBooleanExtra(FilterActivity.FILTER_APPLY, false)) {
                filterRecyclerViewAdapter.notifyDataSetChanged();
                //filter apply
                Log.e(TAG, "filter apply");
                GetDoctorList(0);
            } else {
                //do nothing
                //filter not apply
                Log.e(TAG, "filter not apply");
            }
        } else if (requestCode == GlobalVar.NO_DATA_AVAILABLE) {

        } else {
            txtLocation.setText(LocationFindActivity.myLocation);
            GetDoctorList(0);
        }
    }

    Gson gson = new Gson();

    private void GetDoctorList(final int distance_counter) {
        rowListItem.clear();
        JSONObject obj = new JSONObject();
        if (FilterActivity.arrayList.size() == 0) {
            if (isCentralSearch) {
                url = GlobalVar.ServerAddress + "AndroidNew/NewCentralSearch";
                try {
                    obj.put("searchtext", c.getName());
                    obj.put("texttype", c.getType());
                    obj.put("log", LocationFindActivity.lon);
                    obj.put("lat", LocationFindActivity.lat);
                    //obj.put("offset", 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                url = GlobalVar.ServerAddress + "AndroidNew/NewCentralizeSearchIOS";
                try {
                    obj.put("specializationId", specializationId);
                    obj.put("userId", pref.getUID());
                    obj.put("log", LocationFindActivity.lon);
                    obj.put("lat", LocationFindActivity.lat);
                    obj.put("Totaldistance", 0);
                    obj.put("offset", 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            url = GlobalVar.ServerAddress + "AndroidNew/NewFilter";
            try {
                obj.put("specializationId", specializationId);
                obj.put("userId", pref.getUID());
                obj.put("log", LocationFindActivity.lon);
                obj.put("lat", LocationFindActivity.lat);
                obj.put("Totaldistance", 0);

                //---------TYPE DOCTOR HOSPITAL---------
                if (FilterActivity.strFilterType.equalsIgnoreCase(FilterActivity.DOCTOR)) {
                    obj.put("type", "doctor");
                } else if (FilterActivity.strFilterType.equalsIgnoreCase(FilterActivity.HOSPITAL)) {
                    obj.put("type", "hospital");
                } else {
                    obj.put("type", "");
                }

                //---------PATHY_ID----------
                if (FilterActivity.strFilterType.equalsIgnoreCase(FilterActivity.DOCTOR)) {
                    if (!FilterActivity.strFilterPathyName.isEmpty() && !FilterActivity.strPathyId.isEmpty()) {
                        obj.put("pathy", FilterActivity.strPathyId);
                    } else {
                        obj.put("pathy", "");
                    }
                } else {
                    obj.put("pathy", "");
                }

                //-----------Consultation Fee--------------
                if (FilterActivity.strFilterConsultation.equalsIgnoreCase(FilterActivity.CONSULTATION_FEE_300)) {
                    obj.put("min", "0");
                    obj.put("max", "300");
                } else if (FilterActivity.strFilterConsultation.equalsIgnoreCase(FilterActivity.CONSULTATION_FEE_500)) {
                    obj.put("min", "300");
                    obj.put("max", "500");
                } else if (FilterActivity.strFilterConsultation.equalsIgnoreCase(FilterActivity.CONSULTATION_FEE_ABOVE_500)) {
                    obj.put("min", "500");
                    obj.put("max", "50000");
                } else {
                    obj.put("min", "");
                    obj.put("max", "");
                }

                //-----------Near By--------------
                if (FilterActivity.strFilterNearBy.equalsIgnoreCase(FilterActivity.NEAR_BY)) {
                    obj.put("nearby", "ASC");
                } else {
                    obj.put("nearby", "");
                }

                //-----------Like--------------
                if (FilterActivity.strFilterLike.equalsIgnoreCase(FilterActivity.USER_LIKE_HIGH_LOW)) {
                    obj.put("likes", "ASC");
                } else if (FilterActivity.strFilterLike.equalsIgnoreCase(FilterActivity.USER_LIKE_LOW_HIGH)) {
                    obj.put("likes", "DESC");
                } else {
                    obj.put("likes", "");
                }

                obj.put("availablestatus", "");
                obj.put("packageid", "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        GlobalVar.showProgressDialog(this, "", false);


        GlobalVar.errorLog(TAG, " url", url);
        GlobalVar.errorLog(TAG, " obj", obj.toString());
        // GlobalVar.errorLog(TAG, " offset", String.valueOf(distance_counter));

        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();

                    JSONArray jsonArray1 = null;
                    JSONArray jsonArray2 = null;
                    JSONArray jsonArray3 = null;
                    JSONArray jsonArray4 = null;
                    GlobalVar.errorLog(TAG, "GetDoctorList response", response.toString());
                    try {
                        if (isCentralSearch) {
                            if (c.getType().contains("Doctor") || c.getType().contains("Speciality") ||
                                    c.getType().contains("Treatment")) {
                                jsonArray1 = response.optJSONArray("ObjectList");
                            } else if (c.getType().contains("Surgery Package") || c.getType().contains("Hospital")) {
                                jsonArray2 = response.optJSONArray("ObjectList");
                            }
                        } else {
                            jsonArray1 = response.optJSONArray("DoctorList");
                            jsonArray2 = response.optJSONArray("HospitalList");
                        }

                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                DoctorModel pojo = gson.fromJson(json.toString(), DoctorModel.class);
                                pojo.setDistance(GlobalVar.distance(pojo.getLatitude(), pojo.getLongitude()));
                                pojo.setSpecializationId(specializationId);
                                if (pojo.getFee() != null &&
                                        !pojo.getFee().equals("0") &&
                                        !pojo.getFee().equals("NA") &&
                                        !pojo.getFee().isEmpty()) {
                                    pojo.setFee(GlobalVar.priceWithoutDecimal(Double.valueOf(pojo.getFee())));
                                } else {
                                    pojo.setFee("0");
                                }
                                rowListItem.add(pojo);
                            }
                        }

                        if (jsonArray2 != null && jsonArray2.length() > 0) {
                            for (int i = 0; i < jsonArray2.length(); i++) {
                                JSONObject json = jsonArray2.getJSONObject(i);
                                HospitalModel pojo = gson.fromJson(json.toString(), HospitalModel.class);
                                pojo.setDistance(GlobalVar.distance(pojo.getLatitude(), pojo.getLongitude()));
                                pojo.setDistance(GlobalVar.distance(pojo.getLatitude(), pojo.getLongitude()));
                                pojo.setSpecialization(specializationId);
                                rowListItem.add(pojo);
                            }
                        }

                        if ((jsonArray1 == null || jsonArray1.length() == 0) && (jsonArray2 == null || jsonArray2.length() == 0)) {
                            startActivityForResult(new Intent(context, NoDataAvailableActivity.class), GlobalVar.NO_DATA_AVAILABLE);
                            overridePendingTransitionEnter();
                        }
/*
                        jsonArray3 = response.optJSONArray("SurgeryPackages");

                        ArrayList<SurgeryPackages> surgeryPackages = new ArrayList<>();
                        if (jsonArray3 != null && jsonArray3.length() > 0) {
                            for (int i = 0; i < jsonArray3.length(); i++) {
                                JSONObject json = jsonArray3.getJSONObject(i);
                                SurgeryPackages pojo = gson.fromJson(json.toString(), SurgeryPackages.class);
                                surgeryPackages.add(pojo);
                            }
                            SurgeryPackages surgeryPackages1 = new SurgeryPackages();
                            surgeryPackages1.setSurgeryPackages(surgeryPackages);
                            rowListItem.add(surgeryPackages1);
                        }*/

   /*                     jsonArray4 = response.optJSONArray("FirstAid");

                        if (jsonArray4 != null && jsonArray4.length() > 0) {
                            for (int i = 0; i < jsonArray4.length(); i++) {
                                JSONObject json = jsonArray4.getJSONObject(i);
                                FirstAid pojo = gson.fromJson(json.toString(), FirstAid.class);
                                rowListItem.add(pojo);
                            }
                        }*/

                        adapter.notifyDataSetChanged();
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

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void startNewActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransitionEnter();
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

        if (hospitalModel.getAvailablepackege() == 0) {
            txtSurgery.setVisibility(View.GONE);
        }

        txtPayAdmit.setOnClickListener(v -> {
            dialog.cancel();
            dialog.dismiss();
            Intent intent = new Intent(context, BookingDetailsAdmitNowActivity.class);
            intent.putExtra(KEY_HOSPITAL_MODEL, hospitalModel);
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
            intent.putExtra(KEY_HOSPITAL_MODEL, hospitalModel);
            intent.putExtra(KEY_SCROLL_HOSPITAL, "doctors");
            intent.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, String.valueOf(isCentralSearch));
            Log.e("aaaaaaaaaa", String.valueOf(isCentralSearch));
            startNewActivity(intent);
        });

        txtSurgery.setOnClickListener(v -> {
            dialog.cancel();
            dialog.dismiss();
            Intent intent = new Intent(context, DetailHospitalActivity.class);
            intent.putExtra(KEY_HOSPITAL_MODEL, hospitalModel);
            intent.putExtra(KEY_SCROLL_HOSPITAL, "packages");
            intent.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, String.valueOf(isCentralSearch));
            Log.e("aaaaaaaaaa", String.valueOf(isCentralSearch));
            startNewActivity(intent);
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FilterActivity.resetFilter();
    }

    ArrayList<CentralSearchModel> listSearch = new ArrayList<>();

    private void getCentralData(final String query) {
        pb_loading.setVisibility(View.VISIBLE);
        String url = GlobalVar.ServerAddress + "AndroidNew/GetAllSearchDataForDoctor";

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, response -> {
            pb_loading.setVisibility(View.GONE);
            listSearch.clear();
            adapterSearch.notifyDataSetChanged();

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonData = jsonObject.getJSONArray("data");
                if (jsonData.length() > 0) {
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject json = jsonData.getJSONObject(i);
                        CentralSearchModel pojo = gson.fromJson(json.toString(),
                                CentralSearchModel.class);
                        String name = pojo.getName();
                        name = name.replace("[", "#");
                        name = name.replace("]", "#");
                        if (name.contains("#")) {
                            pojo.setName(name.split("#")[0]);
                        } else {
                            pojo.setName(name);
                        }
                        listSearch.add(pojo);
                    }
                    adapterSearch.notifyDataSetChanged();
                } else {
                    // GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }, error -> {
            pb_loading.setVisibility(View.GONE);
            showSnackBar(VolleyErrorHelper.getMessage(error, context));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("text", query);
                map.put("city", "");
                Log.e("url", map.toString());
                return map;

            }
        };
        mQueue.add(stringRequest);
    }

    private void getNextActivity(CentralSearchModel c) {
        ///--------------------
        GlobalVar.hideProgressDialog();
        switch (c.getType()) {
            case "tbl_doctor": {
                Intent i = new Intent(context, DoctorHospitalPackagesListActivity.class);
                c.setType("Doctor");
                i.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, c);
                startNewActivity(i);
                break;
            }
            case "tbl_doctorsSpeciality": {
                Intent i = new Intent(context, DoctorHospitalPackagesListActivity.class);
                i.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, c);
                c.setType("Speciality");
                startNewActivity(i);
                break;
            }

            case "tbl_hospital":
                Intent iHospital = new Intent(context, DoctorHospitalPackagesListActivity.class);
                iHospital.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, c);
                c.setType("Hospital");
                startNewActivity(iHospital);
                break;

            case "tbl_hospitalpackeges": {
                Intent i2 = new Intent(context, DoctorHospitalPackagesListActivity.class);
                i2.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, c);
                c.setType("Surgery Packages");
                startNewActivity(i2);
                break;
            }

            case "tbl_treatment": {
                Intent i2 = new Intent(context, DoctorHospitalPackagesListActivity.class);
                i2.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, c);
                c.setType("Treatment");
                startNewActivity(i2);
                break;
            }

            ///--------- done up to here ---------
            case "tbl_hospitalsSpeciality":
                Intent i = new Intent(context, DoctorHospitalPackagesListActivity.class);
                i.putExtra(CentralSearchActivity.CENTRAL_SEARCH_KEY, c);
                FilterActivity.strFilterType = FilterActivity.HOSPITAL;
                FilterActivity.arrayList.add(FilterActivity.strFilterType);
                startNewActivity(i);
                break;

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
        listSearch.clear();
        adapterSearch.notifyDataSetChanged();
        edSearch.setText("");
        layoutCentralSearch.setVisibility(View.GONE);
        lineSearch.setBackgroundColor(context.getResources().getColor(R.color.primary));
        String url = GlobalVar.ServerAddress + "AndroidNew/InsertSearch";
        GlobalVar.showProgressDialog(context, "", true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("userId", pref.getUID());
            obj.put("id", c.getId());
            obj.put("name", c.getName());
            obj.put("type", c.getType());
            obj.put("typeid", c.getTypeId());
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "addSearchResult", c.toString());
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, response -> getNextActivity(c), error -> getNextActivity(c));

        mQueue.add(jsonObjectRequest);
    }
}