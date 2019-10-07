package com.whr.user.com.SuggesatNewActivityPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.whr.user.com.WHR.SuggestTestPackages.SuggestTreatmentListByDoctorFragmentAdapter;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pojo.Suggested_TreatementPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SuggestTreatmentListByDoctorActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private Context context;

    private LinearLayoutManager lLayout;
    private PreferenceUtils preferenc;
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    private ConnectionDector connectionDector;
    private TextView bookAppoentmentbtn;
    private List<Suggested_TreatementPojo> rowListItem;
    private List<Suggested_TreatementPojo> stList = new ArrayList<>();

    private String doctorId = "";
    private String did = "";
    private SuggestTreatmentListByDoctorFragmentAdapter adapter;
    private long familyid;
    private String suggestValue;
    private String familyname;

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_treatment_list_by_doctor);
        context = SuggestTreatmentListByDoctorActivity.this;
        ///------------New Toolbar Code----------------
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        ///------------New Toolbar code------------------------------
        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (bundle != null) {
            boolean suggestValuekey = bundle.containsKey("suggest");
            boolean familyidkey = bundle.containsKey("familyid");
            boolean familynamekey = bundle.containsKey("familyname");
            boolean docterIdkey = bundle.containsKey("doctorId");
            boolean isDid = bundle.containsKey("did");

            // Suggest_Test  Suggest_Treatment
            if (suggestValuekey) {
                suggestValue = bundle.getString("suggest");
            }
            if (familyidkey) {
                familyid = bundle.getLong("familyid");
            }
            if (familynamekey) {
                familyname = bundle.getString("familyname");
            }
            if (docterIdkey) {
                doctorId = bundle.getString("doctorId");
            }

            if (isDid) {
                did = bundle.getString("did");
            }
        }
        init();
    }

    private void init() {
        connectionDector = new ConnectionDector(context);
        preferenc = new PreferenceUtils(context);
        rowListItem = new ArrayList<>();
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        bookAppoentmentbtn = findViewById(R.id.bookbtn);
        recyclerView = findViewById(R.id.suggestTreatmentRecyceler);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);

        bookAppoentmentbtn.setOnClickListener(v -> {
            String tratmentIdListstr = "";
            for (Suggested_TreatementPojo s : rowListItem) {
                if (s.isCheck()) {
                    if (tratmentIdListstr.isEmpty()) {
                        tratmentIdListstr = String.valueOf(s.getId());
                    } else {
                        tratmentIdListstr = tratmentIdListstr + "," + s.getId();
                    }
                }
            }
            if (tratmentIdListstr.length() > 0) {
                Intent mIntent = new Intent(SuggestTreatmentListByDoctorActivity.this, TreatmentTestDoctorListByTreatmentActitivty.class);
                Bundle bundle = new Bundle();
                bundle.putString("tratmentIdListstr", tratmentIdListstr);
                bundle.putParcelableArrayList("stList", (ArrayList<? extends Parcelable>) stList);
                bundle.putString("doctorId", did);
                bundle.putLong("familyid", familyid);
                bundle.putString("familyname", familyname);
                mIntent.putExtras(bundle);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                context.startActivity(mIntent);
            } else {
                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.Pleaseselectatleastonetreatment), context, R.color.red);
            }
        });


        if (connectionDector.isConnectingToInternet()) {
            getAllSuggestTestTreatment();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
    }


    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    String TAG = getClass().getSimpleName();

    Gson gson = new Gson();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
            getAllSuggestTestTreatment();
        }
    }


    private void getAllSuggestTestTreatment() {
        GlobalVar.showProgressDialog(this, "Loading....", false);
        //http://192.168.1.218/
        //String url ="http://192.168.1.218/" + "AndroidNew/GetSugTreatment";
        String url = GlobalVar.ServerAddress + "AndroidNew/GetSugTreatment";
        JSONObject obj = new JSONObject();
        try {
            obj.put("pid", familyid);
            obj.put("did", did);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
            GlobalVar.errorLog(TAG, "url", url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "getAllSuggestTestTreatment ", response.toString());
                    rowListItem.clear();
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("result");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                Suggested_TreatementPojo pojo = gson.fromJson(json.toString(), Suggested_TreatementPojo.class);
                                rowListItem.add(pojo);
                            }
                            adapter = new SuggestTreatmentListByDoctorFragmentAdapter(context, rowListItem);
                            recyclerView.setAdapter(adapter);

                            adapter.setOnClickListener(d -> {
                                if (!d.isCheck()) {
                                    d.setCheck(true);
                                } else {
                                    d.setCheck(false);
                                }
                                adapter.notifyDataSetChanged();
                            });
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
    protected void onRestart() {
        super.onRestart();
    }
}
