package com.whr.user.com.WHR.com.WHR.Activities;

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
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.adapters.Suggested_TreatementAdapter;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pojo.Suggested_TreatementPojo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Suggested_TreatementActivity extends AppCompatActivity {


    Suggested_TreatementAdapter adapter;

    RecyclerView recyclerView;
    Context context;

    private LinearLayoutManager lLayout;

    private PreferenceUtils preferenc;
    private ProgressDialog pDialog;
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    private String value = "";
    private String url = "";
    private ConnectionDector connectionDector;
    private Button bookAppoentmentbtn;

    List<Suggested_TreatementPojo> rowListItem;
    private int doctorId;


    private AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested__treatement);
        context = getApplicationContext();
        preferenc = new PreferenceUtils(context);
        connectionDector = new ConnectionDector(context);
        activity = Suggested_TreatementActivity.this;


        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean suggestKey = bundle.containsKey("Suggested_TreatementActivity");
            boolean doctorIdtKey = bundle.containsKey("doctorId");


            if (suggestKey) {
                value = bundle.getString("Suggested_TreatementActivity");
            }
            if (doctorIdtKey) {
                doctorId = bundle.getInt("doctorId");
            }

            try {

                if (value.contains("Test")) {
                    url = GlobalVar.ServerAddress + "user/GetSugTest";
                } else if (value.contains("Treatment")) {
                    url = GlobalVar.ServerAddress + "user/GetSugTreatment";
                }
            } catch (Exception e) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something is wrong please try Again", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }


        rowListItem = new ArrayList<>();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        pDialog = new ProgressDialog(Suggested_TreatementActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();


        bookAppoentmentbtn = (Button) findViewById(R.id.bookAppoentmentbtn);
        recyclerView = (RecyclerView) findViewById(R.id.activitySuggestTreatmentRecycleView);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        bookAppoentmentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   PathalogyBookingCalenderActivity

                String tratmentIdListstr = "";
                List<Suggested_TreatementPojo> stList = ((Suggested_TreatementAdapter) adapter).getTotaTreatmentlList();

                for (int i = 0; i < stList.size(); i++) {
                    Suggested_TreatementPojo singleStudent = stList.get(i);
                    if (singleStudent.isChecked()) {
                        tratmentIdListstr = tratmentIdListstr + singleStudent.getId() + ",";
                    }

                }

                if (tratmentIdListstr.length() > 0) {
                    Log.e("tratmentIdListstr", tratmentIdListstr);

                    Intent i = new Intent(Suggested_TreatementActivity.this, TreatmentTestDoctorPathalogyListActivity.class);
                    i.putExtra("tratmentIdListstr", tratmentIdListstr);
                    i.putExtra("value", value);
                    i.putExtra("doctorId", doctorId);
                    startActivity(i);




                    
                   /* Intent i  = new Intent(Suggested_TreatementActivity.this,TreatmentTestDoctorPathalogyListActivity.class);
                    i.putExtra("tratmentIdListstr",tratmentIdListstr);
                    i.putExtra("value",value);
                    i.putExtra("doctorId",doctorId);
                    startActivity(i);
*/


                } else {
                    if (value.contains("Test")) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Select Test First", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else if (value.contains("Treatment")) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Select Treatment First", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }


                }

            }
        });


        if (connectionDector.isConnectingToInternet()) {
            getAllSuggestTestTreatment();
        } else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Internate Connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }


    }


    private void getAllSuggestTestTreatment() {

        showProgressDialog();

        JSONObject obj = new JSONObject();
        try {
            Log.e("", "getAllSuggestTestTreatment pid :" + preferenc.getUID() + "did :=>" + doctorId);
            obj.put("pid", preferenc.getUID());
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
                        Log.e("Specialization_doct :=>", response.toString());
                        Gson gson = new Gson();
                        JSONArray jsonArray1 = null;
                        try {
                            jsonArray1 = response.getJSONArray("result");
                            if (jsonArray1 != null && jsonArray1.length() > 0) {
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json = jsonArray1.getJSONObject(i);
                                    Suggested_TreatementPojo pojo = gson.fromJson(json.toString(), Suggested_TreatementPojo.class);
                                    rowListItem.add(pojo);
                                }
                            } else {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Data Available", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter = new Suggested_TreatementAdapter(context, rowListItem, activity);
                        recyclerView.setAdapter(adapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();

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
