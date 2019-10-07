package com.whr.user.com.SuggesatNewActivityPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.diagnostics.booking.adapter.PathologyListAdapter;
import com.whr.user.booking.diagnostics.booking.model.PathologyListPojo;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TreatmentTestPathalogyListActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    ArrayList<Object> rowListItem = new ArrayList<>();
    private Context context;
    private PreferenceUtils pref;
    private ConnectionDector connectionDector;
    private RequestQueue mQueue;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private String tratmentIdListstr, value;
    private String url = GlobalVar.ServerAddress + "AndroidNew/getPathologyListBytestId";
    private PathologyListAdapter adapter;
    protected String TAG = getClass().getSimpleName();

    private int doctorId;
    private AppCompatActivity fragment;
    private String suggestValue;
    private long familyid;
    ArrayList<FamilyMemberListpojo> familyMemberList;
    MaterialRippleLayout imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_test_pathalogy_list);

        context = TreatmentTestPathalogyListActivity.this;
        connectionDector = new ConnectionDector(context);
        pref = new PreferenceUtils(context);
        rowListItem = new ArrayList<>();
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imgBack = findViewById(R.id.imgBack);

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            doctorId = bundle.getInt("doctorId", 0);
            tratmentIdListstr = bundle.getString("tratmentIdListstr");
            suggestValue = bundle.getString("suggest");
            familyid = bundle.getLong("familyid");
            boolean isFamilyMemberList = bundle.containsKey("familyMemberList");
            if (isFamilyMemberList) {
                familyMemberList = bundle.getParcelableArrayList("familyMemberList");
                GlobalVar.errorLog(TAG, "familyList", familyMemberList.size() + "");
            }
        }

        recyclerView = findViewById(R.id.activitySuggestTreatmentRecycleView);
        lLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        imgBack.setOnClickListener(v -> onBackPressed());
        adapter = new PathologyListAdapter(context, rowListItem);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        adapter.setClickLister(new PathologyListAdapter.ClickLister() {
            @Override
            public void PathologyBookClick(PathologyListPojo pathologyListPojo) {
                Intent mIntent = new Intent(context, PathalogyListFragmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tratmentIdListstr", tratmentIdListstr);
                bundle.putString("suggest", suggestValue);
                bundle.putString("doctorName", pathologyListPojo.getPathologyName());
                bundle.putInt("doctorId", Integer.parseInt(pathologyListPojo.getId()));
                bundle.putLong("familyid", familyid);
                bundle.putString("profileImage", pathologyListPojo.getProfile_img());
                bundle.putInt("selected_doctorId", doctorId);
                mIntent.putParcelableArrayListExtra("familyMemberList", familyMemberList);
                mIntent.putExtras(bundle);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                context.startActivity(mIntent);
            }

            /*@Override
            public void PathologyrDetailClick(PathologyListPojo pathologyListPojo) {


            }*/

            @Override
            public void PathologyrReviewClick(PathologyListPojo pathologyListPojo) {

            }

            @Override
            public void PathologyLikeClick(PathologyListPojo pathologyListPojo) {

            }

            @Override
            public void PathologyFaveClick(PathologyListPojo pathologyListPojo) {

            }

            @Override
            public void PathologyShareClick(PathologyListPojo pathologyListPojo) {

            }
        });
        if (connectionDector.isConnectingToInternet()) {
            GetPathologyList();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE || requestCode == GlobalVar.NO_DATA_AVAILABLE) {
            //webservice call again here
            GetPathologyList();
        }
    }

    Gson gson = new Gson();

    private void GetPathologyList() {
        rowListItem.clear();
        JSONObject obj = new JSONObject();
        try {
            obj.put("did", "-1");
            obj.put("testid", tratmentIdListstr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.showProgressDialog(this, "", false);
        GlobalVar.errorLog(TAG, " url", url);
        GlobalVar.errorLog(TAG, " obj", obj.toString());

        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    JSONArray jsonArray1 = null;
                    GlobalVar.errorLog(TAG, "GetPathologyList response", response.toString());

                    try {
                        jsonArray1 = response.optJSONArray("PathologyList");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            rowListItem.clear();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                PathologyListPojo pojo = gson.fromJson(json.toString(), PathologyListPojo.class);
                                pojo.setDistance(GlobalVar.distance(pojo.getLatitude(), pojo.getLongitude()));
                                rowListItem.add(pojo);
                            }

                        } else {
                            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_DATA_AVAILABLE);
                            overridePendingTransitionEnter();
                        }


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


}