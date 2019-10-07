package com.whr.user.booking.packages.hospital.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.booking.packages.hospital.adapters.SurgerryPackageSearchAdapter;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoDataAvailableActivity;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.packages.hospital.adapters.SurgeryPackageAdapter;


import com.whr.user.booking.packages.hospital.model.SurgeryPackagesModel;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.EndlessRecyclerOnScrollListener;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;

import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewSurgeryPackageActivity extends AppCompatActivity {
    private TextView title;
    private MaterialRippleLayout materialRippleLayout;
    private EditText serachview;
    private RecyclerView recyclerView;
    private String TAG = getClass().getSimpleName();
    private PreferenceUtils pref;
    private Context context;
    private RequestQueue mQueue;
    SurgeryPackageAdapter adapter;
    private LinearLayoutManager lLayout;
    private ArrayList<SurgeryPackagesModel> list = new ArrayList<>();
    String url = GlobalVar.ServerAddress + "AndroidNew/HospitalSurgeryPackageListV1";
    private AppCompatActivity activity;
    private CoordinatorLayout coordinatorLayout;
    public static final String KEY_SURGERY_PACKAGE = "key_surgery_package";
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    String status, text;
    ProgressBar pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_surgery_package);
        context = NewSurgeryPackageActivity.this;
        activity = NewSurgeryPackageActivity.this;
        pref = new PreferenceUtils(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        title = findViewById(R.id.txtTitle);
        materialRippleLayout = findViewById(R.id.imgBack);
        pb_loading = findViewById(R.id.pb_loading);
        recyclerView = findViewById(R.id.recycleviewPackage);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        serachview = findViewById(R.id.searchview);
        title.setText("Surgery Packages");
        materialRippleLayout.setOnClickListener(v -> onBackPressed());
        adapter = new SurgeryPackageAdapter(context, list, activity);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(adapter);


        serachview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    list.clear();
                    adapter.notifyDataSetChanged();
                    recyclerView.removeOnScrollListener(endlessRecyclerOnScrollListener);
                    if (jsonRequest != null) {
                        jsonRequest.cancel();
                    }
                    getSeachData(charSequence.toString());

                } else {
                    list.clear();
                    adapter.notifyDataSetChanged();
                    getData(0);

                }

            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Intent intent1 = getIntent();
        Bundle bundle = intent1.getExtras();

        if (bundle != null) {
            status = bundle.getString("status");
            text = bundle.getString("text");
            if (status.equals("central")) {
                serachview.setText(text);
            } else {
                if (new ConnectionDector(context).isConnectingToInternet()) {
                    getData(0);
                } else {
                    startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
                    overridePendingTransitionExit();
                }
            }
        }

        adapter.setClickLister(surgeryPackagePojo -> {
            Log.e(TAG, surgeryPackagePojo.toString());
            Intent intent = new Intent(context, PackageHospitalsList.class);
            intent.putExtra(KEY_SURGERY_PACKAGE, surgeryPackagePojo);
            startNewActivity(intent);
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getData(0);
        }
    }

    Gson gson = new Gson();

    private void getData(int offset) {
        GlobalVar.showProgressDialog(this, "Loading.....", true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("offset", offset);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj, response -> {
            GlobalVar.errorLog(TAG, "getData", response.toString());
            GlobalVar.hideProgressDialog();
            list.clear();
            JSONArray jsonArray1 = null;
            try {
                jsonArray1 = response.getJSONArray("PackagesList");
                if (jsonArray1 != null && jsonArray1.length() > 0) {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json = jsonArray1.getJSONObject(i);
                        SurgeryPackagesModel pojo = gson.fromJson(json.toString(), SurgeryPackagesModel.class);
                        list.add(pojo);
                    }
                    adapter.notifyDataSetChanged();
                    endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(lLayout) {
                        @Override
                        public void onLoadMore(final int current_page) {
                            if (new ConnectionDector(context).isConnectingToInternet()) {
                                recyclerView.smoothScrollToPosition(list.size() - 1);
                                adapter.addLoadingFooter();
                                recyclerView.post(() -> adapter.notifyDataSetChanged());
                                getDataNext(current_page);
                            } else {
                                showSnackBar(getString(R.string.NoInternetConnection));
                            }
                        }
                    };

                    recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
                } else {
                    startActivityForResult(new Intent(context, NoDataAvailableActivity.class), GlobalVar.NO_DATA_AVAILABLE);
                    overridePendingTransitionExit();
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

    public void startNewActivity(Intent intent) {
        startActivity(intent);
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void getDataNext(int offset) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("offset", offset);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj, response -> {
            GlobalVar.errorLog(TAG, "getDataNext", response.toString());
            adapter.removeLoadingFooter();
            JSONArray jsonArray1 = null;
            try {
                jsonArray1 = response.getJSONArray("PackagesList");
                if (jsonArray1 != null && jsonArray1.length() > 0) {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json = jsonArray1.getJSONObject(i);
                        SurgeryPackagesModel pojo = gson.fromJson(json.toString(), SurgeryPackagesModel.class);
                        list.add(pojo);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    startActivityForResult(new Intent(context, NoDataAvailableActivity.class), GlobalVar.NO_DATA_AVAILABLE);
                    overridePendingTransitionExit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            adapter.removeLoadingFooter();
            showSnackBar(VolleyErrorHelper.getMessage(error, context));
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    CustomJSONObjectRequest jsonRequest;

    private void getSeachData(final String query) {
        pb_loading.setVisibility(View.VISIBLE);
        String urlnew = "http://android.whrhealth.com/AndroidNew/GetAllHospitalSearchData";
        JSONObject obj = new JSONObject();
        try {
            obj.put("text", query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.errorLog(TAG, "url", urlnew);
        GlobalVar.errorLog(TAG, "obj", obj.toString());
        jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, urlnew, obj, response -> {
            pb_loading.setVisibility(View.GONE);
            GlobalVar.errorLog(TAG, "getSeachData", response.toString());
            JSONArray jsonArray1 = null;
            try {
                jsonArray1 = response.getJSONArray("PackagesList");
                if (jsonArray1 != null && jsonArray1.length() > 0) {
                    list.clear();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json = jsonArray1.getJSONObject(i);
                        SurgeryPackagesModel pojo = gson.fromJson(json.toString(), SurgeryPackagesModel.class);
                        list.add(pojo);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    startActivityForResult(new Intent(context, NoDataAvailableActivity.class), GlobalVar.NO_DATA_AVAILABLE);
                    overridePendingTransitionExit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            pb_loading.setVisibility(View.GONE);
            showSnackBar(VolleyErrorHelper.getMessage(error, context));
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }
}
