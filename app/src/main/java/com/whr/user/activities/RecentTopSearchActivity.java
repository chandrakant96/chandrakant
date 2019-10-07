package com.whr.user.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecentTopSearchActivity extends AppCompatActivity {
    TextView edSearch;
    ImageView imgBack;
    Context context;
    RecyclerView recyclerViewRecentSearch, recyclerViewTopSearch, recyclerViewCentralSearch;
    ArrayList<CentralSearchModel> listTop = new ArrayList<>();
    ArrayList<CentralSearchModel> listRecent = new ArrayList<>();
    ArrayList<CentralSearchModel> listSearch = new ArrayList<>();
    RecyclerAdapterCentralSearch adapterRecent, adapterTop, adapterSearch;
    RequestQueue mQueue;
    LinearLayout linearLayout;
    String TAG = getClass().getSimpleName();
    LinearLayout lineSearch;
    LinearLayout layoutCentralSearch;
    ProgressBar pb_loading;
    Gson gson = new Gson();
    PreferenceUtils pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_top_search_activity);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        pref = new PreferenceUtils(context);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        edSearch = findViewById(R.id.edSearch);
        edSearch.setFocusable(true);
        imgBack = findViewById(R.id.imgBack);
        linearLayout = findViewById(R.id.linearLayout);
        lineSearch = findViewById(R.id.lineSearch);
        layoutCentralSearch = findViewById(R.id.layoutCentralSearch);
        recyclerViewCentralSearch = findViewById(R.id.recyclerViewCentralSearch);
        pb_loading = findViewById(R.id.pb_loading);
        pb_loading.incrementProgressBy(10);
        pb_loading.setIndeterminate(true);
        imgBack.setOnClickListener(view -> onBackPressed());


        recyclerViewRecentSearch = findViewById(R.id.recyclerViewRecentSearch);
        LinearLayoutManager lLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecentSearch.setLayoutManager(lLayout);
        recyclerViewRecentSearch.setHasFixedSize(true);
        adapterRecent = new RecyclerAdapterCentralSearch(context, listRecent,
                RecyclerAdapterCentralSearch.RECENT_SEARCH);
        recyclerViewRecentSearch.setAdapter(adapterRecent);
        adapterRecent.setOnClickListener(this::getNextActivity);

        recyclerViewTopSearch = findViewById(R.id.recyclerView);
        LinearLayoutManager lLayout2 = new LinearLayoutManager(context);
        recyclerViewTopSearch.setLayoutManager(lLayout2);
        recyclerViewTopSearch.setHasFixedSize(true);
        adapterTop = new RecyclerAdapterCentralSearch(context, listTop, RecyclerAdapterCentralSearch.TOP_SEARCH);
        recyclerViewTopSearch.setAdapter(adapterTop);
        adapterTop.setOnClickListener(this::getNextActivity);

        // HideSoft(edSearch);

        recyclerViewCentralSearch = findViewById(R.id.recyclerViewCentralSearch);
        LinearLayoutManager lLayout3 = new LinearLayoutManager(context);
        recyclerViewCentralSearch.setLayoutManager(lLayout3);
        recyclerViewCentralSearch.setHasFixedSize(true);
        adapterSearch = new RecyclerAdapterCentralSearch(context, listSearch, RecyclerAdapterCentralSearch.CENTRAL_SEARCH);
        recyclerViewCentralSearch.setAdapter(adapterSearch);
        adapterSearch.setOnClickListener(this::addSearchResult);

        if (new ConnectionDector(context).isConnectingToInternet()) {
            getCentralTop();
            getCentralRecent();
        } else {
            showSnackBar(getString(R.string.NoInternetConnection));
        }

        layoutCentralSearch.setVisibility(View.GONE);
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
        GlobalVar.showSnackBarLinearLayout(linearLayout, message, context, R.color.red);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransitionExit();
    }

    private void getCentralTop() {
        GlobalVar.showProgressDialog(context, "", false);
        String url = GlobalVar.ServerAddress + "AndroidNew/GetTopsearches";
        GlobalVar.errorLog(TAG, "getCentralTop", url);
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.GET, url, response -> {
            GlobalVar.hideProgressDialog();
            try {
                Log.e(TAG, response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonData = jsonObject.getJSONArray("data");
                if (jsonData.length() > 0) {
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject json = jsonData.getJSONObject(i);
                        CentralSearchModel pojo = gson.fromJson(json.toString(), CentralSearchModel.class);
                        listTop.add(pojo);
                    }
                    adapterTop.notifyDataSetChanged();
                } else {
                    // GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            GlobalVar.hideProgressDialog();
        });
        mQueue.add(stringRequest);
    }

    private void getCentralRecent() {
        String url = GlobalVar.ServerAddress + "AndroidNew/GetRecentsearches";
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.GET,
                url, response -> {
            try {
                Log.e(TAG, response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonData = jsonObject.getJSONArray("data");
                if (jsonData.length() > 0) {
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject json = jsonData.getJSONObject(i);
                        CentralSearchModel pojo = gson.fromJson(json.toString(),
                                CentralSearchModel.class);
                        listRecent.add(pojo);
                    }
                    adapterRecent.notifyDataSetChanged();
                } else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error ->
                showSnackBar(VolleyErrorHelper.getMessage(error, context)));
        mQueue.add(stringRequest);
    }

    private void getCentralData(final String query) {
        pb_loading.setVisibility(View.VISIBLE);
        String url = GlobalVar.ServerAddress + "AndroidNew/GetAllSearchData";
        Log.e("url", url);

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
                Intent i2 = new Intent(context, NewSurgeryPackageActivity.class);
                i2.putExtra("text", c.getName());
                i2.putExtra("status", "central");
                i2.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
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


            case "tbl_pathologylabTest": {
                Intent i2 = new Intent(context, NewPathalogyActivity.class);
                i2.putExtra("text", c.getName());
                i2.putExtra("type", "Test");
                i2.putExtra("status", "central");
                i2.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                startNewActivity(i2);
                break;
            }
            case "tbl_pathologylab": {
                Intent intent = new Intent(context, NewPathalogyActivity.class);
                intent.putExtra("text", c.getName());
                intent.putExtra("type", "Pathology");
                intent.putExtra("status", "central");
                startActivity(intent);
                finish();
                break;
            }
        }

    }

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
