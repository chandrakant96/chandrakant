package com.whr.user.com.SuggesatNewActivityPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.booking.diagnostics.booking.activities.PathologyCalenderActivity;
import com.whr.user.booking.diagnostics.booking.adapter.TestTreatFeeAdapter;
import com.whr.user.booking.diagnostics.booking.model.Suggested_TreatementPojo;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PathalogyListFragmentActivity extends AppCompatActivity {

    private TestTreatFeeAdapter adapter;
    private RecyclerView recyclerView;
    private Context context;
    private LinearLayoutManager lLayout;

    private TextView txtTotal;
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    private String value = "";
    private String url = GlobalVar.ServerAddress + "AndroidNew/getPathologytestDetailsId";
    private ConnectionDector connectionDector;
    private MaterialRippleLayout bookTest;

    private List<Suggested_TreatementPojo> rowListItem;
    private int doctorId, selected_doctorId = 0;
    private String tratmentIdListstr;
    private String doctorName = "", profileImage = "";
    private String suggestValue;
    private long familyid;
    private AppCompatActivity activity;
    MaterialRippleLayout imgBack;
    ArrayList<FamilyMemberListpojo> familyMemberList;
    private List<Suggested_TreatementPojo> selectedTreatmentList = new ArrayList<>();


    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathalogy_list_fragment);

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (bundle != null) {
            doctorId = bundle.getInt("doctorId");
            selected_doctorId = bundle.getInt("selected_doctorId", 0);
            tratmentIdListstr = bundle.getString("tratmentIdListstr");
            suggestValue = bundle.getString("suggest");
            doctorName = bundle.getString("doctorName");
            profileImage = bundle.getString("profileImage");
            familyid = bundle.getLong("familyid");

            boolean isFamilyMemberList = bundle.containsKey("familyMemberList");

            if (isFamilyMemberList) {
                familyMemberList = bundle.getParcelableArrayList("familyMemberList");
                GlobalVar.errorLog(TAG, "familyList", familyMemberList.size() + "");
            }
        }
        init();
    }

    private void init() {
        context = PathalogyListFragmentActivity.this;
        activity = PathalogyListFragmentActivity.this;
        connectionDector = new ConnectionDector(context);
        rowListItem = new ArrayList<>();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        imgBack = findViewById(R.id.imgBack);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        txtTotal = findViewById(R.id.txtTotal);
        bookTest = findViewById(R.id.bookTest);
        TextView titalName = (TextView) findViewById(R.id.titalName);

        if (suggestValue.equals("Suggest_Test")) {
            titalName.setText("Test Name");
        }
        ;

        recyclerView = (RecyclerView) findViewById(R.id.activitySuggestTreatmentRecycleView);
        adapter = new TestTreatFeeAdapter(context, rowListItem, activity);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(adapter);

        imgBack.setOnClickListener(v -> onBackPressed());

        adapter.setOnClickListener(d -> {
            if (!d.isCheck()) {
                d.setCheck(true);
                TestTreatFeeAdapter.totalval += Double.parseDouble(d.getWhrdiscounted_price() + " ");
                txtTotal.setText("₹. " + TestTreatFeeAdapter.totalval);
                selectedTreatmentList.add(d);
            } else {
                d.setCheck(false);
                TestTreatFeeAdapter.totalval -= Double.parseDouble(d.getWhrdiscounted_price() + " ");
                txtTotal.setText("₹. " + TestTreatFeeAdapter.totalval);
                selectedTreatmentList.remove(d);
            }

            adapter.notifyDataSetChanged();
        });


        bookTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTreatmentList.isEmpty()) {
                    showSnackBar("Select Altleast One Test/Treatment");
                } else {
                    Intent intent = new Intent(context, PathologyCalenderActivity.class);
                    intent.putParcelableArrayListExtra("pathologyTreatmentList", (ArrayList<? extends Parcelable>) selectedTreatmentList);
                    intent.putExtra("pathologyName", doctorName);
                    intent.putExtra("pathid",String.valueOf(doctorId));
                    intent.putExtra("total", TestTreatFeeAdapter.totalval);
                    startActivity(intent);
                }

            }
        });


        if (connectionDector.isConnectingToInternet()) {
            getTestTretFee();
        } else {
            GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
        }
    }

    String TAG = getClass().getSimpleName();

    private void getTestTretFee() {
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject obj = new JSONObject();
        try {
            obj.put("testid", tratmentIdListstr);
            obj.put("did", doctorId); // did is pathid doctorId is pathid
            GlobalVar.errorLog(TAG, "obj", obj.toString());
            GlobalVar.errorLog(TAG, "url", url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GlobalVar.hideProgressDialog();
                        Log.e("getTestTretFee", response.toString());
                        Gson gson = new Gson();
                        JSONArray jsonArray1 = null;
                        rowListItem.clear();
                        try {
                            jsonArray1 = response.getJSONArray("result");
                            if (jsonArray1 != null && jsonArray1.length() > 0) {
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json = jsonArray1.getJSONObject(i);
                                    Suggested_TreatementPojo pojo = gson.fromJson(json.toString(), Suggested_TreatementPojo.class);
                                    rowListItem.add(pojo);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                adapter.notifyDataSetChanged();
                                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalVar.hideProgressDialog();
                if (error != null) {
                    GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
                }
            }
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG + " AppoinmenmtRes", "Appoinment_Restart");
    }
}
