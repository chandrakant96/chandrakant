package com.whr.user.booking.doctor.booking;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.whr.user.booking.diagnostics.booking.activities.NewPathalogyActivity;
import com.whr.user.booking.diagnostics.booking.activities.NewPathalogyDetailsActivity;
import com.whr.user.booking.packages.hospital.activities.NewSurgeryPackageActivity;
import com.whr.user.booking.packages.hospital.activities.PackageHospitalsList;
import com.whr.user.booking.packages.hospital.model.SurgeryPackagesModel;
import com.whr.user.model.CentralSearchModel;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomStringRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.CentralSearchActivity;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.FilterActivity;
import com.whr.user.booking.adapters.RecyclerAdapterCentralSearch;
import com.whr.user.booking.doctor.booking.adapters.DoctorSpecialitiesRecyclerViewAdapter;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.booking.doctor.booking.models.DoctorSpecialitiesModel;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorSpecialtiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DoctorSpecialitiesRecyclerViewAdapter adapter;
    private Context context;
    private LinearLayoutManager lLayout;
    private List<DoctorSpecialitiesModel> rowListItem = new ArrayList<>();
    RequestQueue mQueue;
    private ConnectionDector connectionDector;
    CoordinatorLayout coordinatorLayout;
    String TAG = getClass().getSimpleName();
    private String url = GlobalVar.ServerAddress + "AndroidNew/GetSpecializationNew";
    Gson gson = new Gson();
    PreferenceUtils pref;
    EditText edSearch;
    ImageView imgBack;
    public static String KEY_SPECIALIZATION = "specialization";
    RecyclerView recyclerViewRecentSearch;
    ArrayList<CentralSearchModel> arrayListTestTreatment = new ArrayList<>();
    ArrayList<CentralSearchModel> arrayListTestTreatment2 = new ArrayList<>();
    RecyclerAdapterCentralSearch recentAdapter, recyclerAdapterCentralSearch;
    ProgressBar pb_loading;
    RecyclerView recyclerViewCentralSearch;
    LinearLayout layoutCentralSearch;
    LinearLayout lineSearch;
    TextView txtRecentSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_spcialites);
        txtRecentSearch = findViewById(R.id.txtRecentSearch);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        edSearch = findViewById(R.id.edSearch);
        imgBack = findViewById(R.id.imgBack);
        edSearch.setClickable(true);
        imgBack.setOnClickListener(view -> onBackPressed());
        // edSearch.setHint(getString(R.string.searchby));
        context = this;
        pref = new PreferenceUtils(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();

        lLayout = new LinearLayoutManager(context);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        adapter = new DoctorSpecialitiesRecyclerViewAdapter(context, rowListItem);
        recyclerView.setAdapter(adapter);

        HideSoft(coordinatorLayout);

        pb_loading = findViewById(R.id.pb_loading);
        pb_loading.incrementProgressBy(10);
        pb_loading.setIndeterminate(true);

        connectionDector = new ConnectionDector(context);

        adapter.setClickLister(doctorSpecificationModel -> {
            GlobalVar.errorLog(TAG, " ", doctorSpecificationModel.toString());
            Intent i = new Intent(context, DoctorHospitalPackagesListActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putParcelable(KEY_SPECIALIZATION, doctorSpecificationModel);
            i.putExtras(bundle1);
            startNewActivity(i);
        });


        recyclerViewRecentSearch = findViewById(R.id.recyclerViewRecentSearch);
        LinearLayoutManager lLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecentSearch.setLayoutManager(lLayout);
        recyclerViewRecentSearch.setHasFixedSize(true);
        recentAdapter = new RecyclerAdapterCentralSearch(context, arrayListTestTreatment, RecyclerAdapterCentralSearch.RECENT_SEARCH);
        recyclerViewRecentSearch.setAdapter(recentAdapter);
        recentAdapter.setOnClickListener(this::getNextActivity);

        recyclerViewCentralSearch = findViewById(R.id.recyclerViewCentralSearch);
        LinearLayoutManager lLayout3 = new LinearLayoutManager(context);
        recyclerViewCentralSearch.setLayoutManager(lLayout3);
        recyclerViewCentralSearch.setHasFixedSize(true);
        recyclerAdapterCentralSearch = new RecyclerAdapterCentralSearch(context, arrayListTestTreatment2, RecyclerAdapterCentralSearch.CENTRAL_SEARCH);
        recyclerViewCentralSearch.setAdapter(recyclerAdapterCentralSearch);
        recyclerAdapterCentralSearch.setOnClickListener(this::addSearchResult);

        layoutCentralSearch = findViewById(R.id.layoutCentralSearch);
        layoutCentralSearch.setVisibility(View.GONE);
        lineSearch = findViewById(R.id.lineSearch);
        lineSearch.setBackgroundColor(context.getResources().getColor(R.color.primary));

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.length() > 0) {
                    layoutCentralSearch.setVisibility(View.VISIBLE);
                    lineSearch.setBackgroundColor(context.getResources().getColor(R.color.white));
                    getCentralData(query.toString());
                } else {
                    layoutCentralSearch.setVisibility(View.GONE);
                    lineSearch.setBackgroundColor(context.getResources().getColor(R.color.primary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edSearch.setOnClickListener(v -> {
            layoutCentralSearch.setVisibility(View.VISIBLE);
            lineSearch.setBackgroundColor(context.getResources().getColor(R.color.white));
        });
        webserviceCall();
    }

    public void webserviceCall() {
        if (connectionDector.isConnectingToInternet()) {
            getCentralRecent();
            getDoctorSpecialization();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
    }

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    public void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransitionExit();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
            webserviceCall();
        }
    }

    //--------webservices
    public void getDoctorSpecialization() {
        url = url + "?uid=" + pref.getUID();
        GlobalVar.errorLog(TAG, " url", url);
        GlobalVar.showProgressDialog(this, "Loading....", false);
        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET,
                url, null, response -> {
            GlobalVar.hideProgressDialog();
            try {
                JSONArray jsonArray1 = response.getJSONArray("special");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject json = jsonArray1.getJSONObject(i);
                    DoctorSpecialitiesModel pojo = gson.fromJson(json.toString(), DoctorSpecialitiesModel.class);
                    rowListItem.add(pojo);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            GlobalVar.hideProgressDialog();
            showSnackBar(VolleyErrorHelper.getMessage(error, context));
        });
        mQueue.add(jsonRequest);
    }

    public void addSearchResult(CentralSearchModel c) {
        edSearch.setText("");
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

    private void getCentralRecent() {
        String url = GlobalVar.ServerAddress + "AndroidNew/GetRecentsearchesforspeificuser";

        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            // obj.put("type", "tbl_doctor");
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        CustomJSONObjectRequest stringRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj, response -> {
            try {
                JSONArray jsonData = response.getJSONArray("data");
                if (jsonData.length() > 0) {
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject json = jsonData.getJSONObject(i);
                        CentralSearchModel pojo = gson.fromJson(json.toString(),
                                CentralSearchModel.class);
                        arrayListTestTreatment.add(pojo);
                    }
                    recentAdapter.notifyDataSetChanged();
                } else {
                    txtRecentSearch.setVisibility(View.GONE);
                    // GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error ->
                GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context),
                        context, R.color.red));
        mQueue.add(stringRequest);
    }

    private void getCentralData(final String query) {
        pb_loading.setVisibility(View.VISIBLE);
        String url = GlobalVar.ServerAddress + "AndroidNew/GetAllSearchDataForDoctor";

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, response -> {
            pb_loading.setVisibility(View.GONE);
            arrayListTestTreatment2.clear();
            recyclerAdapterCentralSearch.notifyDataSetChanged();

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
                        arrayListTestTreatment2.add(pojo);
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
}
