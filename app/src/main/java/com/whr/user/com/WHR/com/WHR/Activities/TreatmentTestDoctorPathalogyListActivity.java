package com.whr.user.com.WHR.com.WHR.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.com.WHR.adapters.DoctorPathalogyListFromTidAdapter;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pojo.DoctorPathalogyListPojo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TreatmentTestDoctorPathalogyListActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private ProgressDialog pDialog;
    private List<DoctorPathalogyListPojo> rowListItem;
    private Context context;
    private PreferenceUtils pref;
    ConnectionDector connectionDector;
    private RequestQueue mQueue;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private String tratmentIdListstr, value;
    private String url = "";
    private DoctorPathalogyListFromTidAdapter adapter;
    private Activity activity;
    private int doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathalogy_list_from_treatment_id);

     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) toolbar.findViewById(R.id.toolbarTilte);




        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
*/
        context = TreatmentTestDoctorPathalogyListActivity.this;
        pref = new PreferenceUtils(context);
        connectionDector = new ConnectionDector(context);
        activity = this;


        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean treatmentListKey = bundle.containsKey("tratmentIdListstr");
            boolean valueKey = bundle.containsKey("value");
            boolean doctorIdKey = bundle.containsKey("doctorId");


            try {
                if (doctorIdKey) {
                    doctorId = bundle.getInt("doctorId");
                }

                if (treatmentListKey) {
                    tratmentIdListstr = bundle.getString("tratmentIdListstr");
                }
                if (valueKey) {
                    value = bundle.getString("value");
                }

              /*  if (value.contains("Test")) {
                    title.setText("Pathalogy List");
                } else if (value.contains("Treatment")) {
                    title.setText("Doctor List");
                }
*/

                if (value.contains("Test")) {
                    url = GlobalVar.ServerAddress + "Android/getPathologyListBytestId";
                } else if (value.contains("Treatment")) {
                    url = GlobalVar.ServerAddress + "Android/getdoctorListBytestId";
                }
            } catch (Exception e) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something is wrong please try Again", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }


        Log.e("testid", tratmentIdListstr);
        Log.e("did", String.valueOf(doctorId));


        rowListItem = new ArrayList<>();

        //   rowListItem = getList();


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        pDialog = new ProgressDialog(TreatmentTestDoctorPathalogyListActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        recyclerView = (RecyclerView) findViewById(R.id.activitySuggestTreatmentRecycleView);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);

      /*  adapter = new DoctorPathalogyListFromTidAdapter(context,rowListItem ,activity);
        recyclerView.setAdapter(adapter);
*/


        if (connectionDector.isConnectingToInternet()) {
            getPAthDocList();
        } else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Internate Connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }


    private void getPAthDocList() {

        showProgressDialog();
        JSONObject obj = new JSONObject();
        try {


            obj.put("testid", tratmentIdListstr);
            obj.put("did", doctorId);


        } catch (Exception ex) {

        }


        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .POST,
                url,
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        Log.e("docPAthListResp :=>", response.toString());
                        Gson gson = new Gson();
                        JSONArray jsonArray1 = null;
                        try {
                            jsonArray1 = response.getJSONArray("teatTreatResult");
                            if (jsonArray1 != null && jsonArray1.length() > 0) {
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json = jsonArray1.getJSONObject(i);
                                    DoctorPathalogyListPojo pojo = gson.fromJson(json.toString(), DoctorPathalogyListPojo.class);
                                    rowListItem.add(pojo);
                                }
                            } else {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Data Available", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }/*
                        adapter = new DoctorPathalogyListFromTidAdapter(context,rowListItem ,tratmentIdListstr);
                        recyclerView.setAdapter(adapter);
*/


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
                }
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }


    @Override
    protected void onDestroy() {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


}
