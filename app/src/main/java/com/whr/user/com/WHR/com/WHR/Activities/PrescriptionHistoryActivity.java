package com.whr.user.com.WHR.com.WHR.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.whr.user.activities.LocationFindActivity;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NevigationDrawerDashBordActivity;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.com.WHR.adapters.PrescriptionHistoryListAdapter;
import com.whr.user.pojo.DoctorListForPresPojo;
import com.whr.user.pojo.GlobalVar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionHistoryActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    private PreferenceUtils pref;
    private List<DoctorListForPresPojo> rowListItem;
    private Context context;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private PrescriptionHistoryListAdapter adapter;
    private int docterId;
    private ConnectionDector connectionDector;
    private long familyid;
    String TAG = getClass().getSimpleName();

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_history);
        context = PrescriptionHistoryActivity.this;
        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean hospitalKey = bundle.containsKey("doctorId");
            boolean familyidKey = bundle.containsKey("patientid");

            if (familyidKey) {
                familyid = bundle.getLong("patientid");
                Log.e("familyid", " " + familyid);
            }
            if (hospitalKey) {
                docterId = bundle.getInt("doctorId");
            }
        }

        ///------------New Toolbar Code----------------
        RelativeLayout toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.title);
        TextView txtLocation = toolbar.findViewById(R.id.txtLocation);
        txtLocation.setVisibility(View.GONE);
        textView.setText(R.string.PrescriptionList);
        MaterialRippleLayout imgBack = toolbar.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        MaterialRippleLayout imgHome = toolbar.findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, NevigationDrawerDashBordActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                overridePendingTransitionExit();
            }
        });

        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LocationFindActivity.class);
                startActivityForResult(i, 1);
            }
        });
        ///------------New Toolbar code------------------------------

        pref = new PreferenceUtils(context);
        rowListItem = new ArrayList<>();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        recyclerView = (RecyclerView) findViewById(R.id.audioVideolist1);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        prescriptionImageList();
    }


    private void prescriptionImageList() {
        String url = GlobalVar.ServerAddress + "User/GetbyprescriptionHistory";
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject obj = new JSONObject();
        try {
            obj.put("userId", familyid);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GlobalVar.hideProgressDialog();
                        Log.e("prescriptionImageList>", response.toString());
                        Gson gson = new Gson();
                        JSONArray jsonArray1 = null;
                        try {
                            jsonArray1 = response.getJSONArray("DoctorList");
                            if (jsonArray1 != null && jsonArray1.length() > 0) {
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json = jsonArray1.getJSONObject(i);
                                    DoctorListForPresPojo model = gson.fromJson(json.toString(), DoctorListForPresPojo.class);
                                    rowListItem.add(model);
                                }
                            } else {
                                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                            }
                            adapter = new PrescriptionHistoryListAdapter(context, rowListItem, docterId, familyid);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                    GlobalVar.hideProgressDialog();
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}