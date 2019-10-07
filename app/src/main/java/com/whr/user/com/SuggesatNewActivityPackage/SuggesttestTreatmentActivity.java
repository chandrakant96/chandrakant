package com.whr.user.com.SuggesatNewActivityPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.com.WHR.adapters.Suggested_TreatementAdapter;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pojo.Suggested_TreatementPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SuggesttestTreatmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private String url = GlobalVar.ServerAddress + "user/GetSugTest";
    private LinearLayoutManager lLayout;
    private PreferenceUtils preferenc;
    //private ProgressDialog pDialog;
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    private ConnectionDector connectionDector;
    private Button bookAppoentmentbtn;
    private List<Suggested_TreatementPojo> rowListItem = new ArrayList<>();
    private int doctorId;
    private Suggested_TreatementAdapter adapter;
    private String suggestValue;
    private long familyid;
    private AppCompatActivity activity;
    ArrayList<FamilyMemberListpojo> familyMemberList;

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggesttest_treatment);

        ///------------New Toolbar Code----------------
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        activity = SuggesttestTreatmentActivity.this;
        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            suggestValue = bundle.getString("suggest");
            familyid = bundle.getLong("familyid");
            doctorId = bundle.getInt("doctorId");
            boolean isFamilyMemberList = bundle.containsKey("familyMemberList");

            if (isFamilyMemberList) {
                familyMemberList = bundle.getParcelableArrayList("familyMemberList");
                GlobalVar.errorLog(TAG, "familyList", familyMemberList.size() + "");
            }
        }
        init();
    }


    private void init() {
        context = SuggesttestTreatmentActivity.this;
        connectionDector = new ConnectionDector(context);
        preferenc = new PreferenceUtils(context);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        TextView titalName = (TextView) findViewById(R.id.titalName);

        if (suggestValue.equals("Suggest_Test")) {
            titalName.setText("Test Name");
        }

        mQueue = CustomVolleyRequestQueue.getInstance(SuggesttestTreatmentActivity.this).getRequestQueue();

        bookAppoentmentbtn = findViewById(R.id.bookAppoentmentbtn);
        recyclerView = findViewById(R.id.activitySuggestTreatmentRecycleView);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        adapter = new Suggested_TreatementAdapter(context, rowListItem, activity);
        recyclerView.setAdapter(adapter);

        bookAppoentmentbtn.setOnClickListener(v -> {

            String tratmentIdListstr = "";
            List<Suggested_TreatementPojo> stList = ((Suggested_TreatementAdapter) adapter).getTotaTreatmentlList();

            for (int i = 0; i < stList.size(); i++) {
                Suggested_TreatementPojo treatmentSelectedList = stList.get(i);
                if (treatmentSelectedList.isChecked()) {
                    tratmentIdListstr = tratmentIdListstr + treatmentSelectedList.getId() + ",";
                }
            }

            if (tratmentIdListstr.length() > 0) {
                Intent mIntent = new Intent(SuggesttestTreatmentActivity.this, TreatmentTestPathalogyListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tratmentIdListstr", tratmentIdListstr);
                bundle.putString("suggest", suggestValue);
                bundle.putInt("doctorId", doctorId);
                bundle.putLong("familyid", familyid);
                mIntent.putParcelableArrayListExtra("familyMemberList", familyMemberList);
                mIntent.putExtras(bundle);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                context.startActivity(mIntent);
            } else {
                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.PleaseSelectTest), context, R.color.red);
            }
        });


        if (connectionDector.isConnectingToInternet()) {
            getAllSuggestTestTreatment();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
        }
    }

    String TAG = getClass().getSimpleName();


    private void getAllSuggestTestTreatment() {
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject obj = new JSONObject();
        try {
            obj.put("pid", familyid);
            obj.put("did", doctorId);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
            GlobalVar.errorLog(TAG, "url", url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    Log.e("getAllTestTreatmentResp", response.toString());
                    Gson gson = new Gson();
                    JSONArray jsonArray1 = null;
                    try {
                        rowListItem.clear();
                        jsonArray1 = response.getJSONArray("result");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                Suggested_TreatementPojo pojo = gson.fromJson(json.toString(), Suggested_TreatementPojo.class);
                                rowListItem.add(pojo);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);

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
        Log.e("AppoinmenmtRes", "Appoinment_Restart");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getAllSuggestTestTreatment();
        }
    }
}