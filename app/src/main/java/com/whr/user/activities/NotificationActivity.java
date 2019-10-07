package com.whr.user.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.adapter.NotificationActivityAdapter;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.model.NotificationActivityPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {


    private NotificationActivityAdapter adapter;
    private RecyclerView recyclerView;
    private Context context;
    private LinearLayoutManager lLayout;
    private CoordinatorLayout coordinatorLayout;

    private List<NotificationActivityPojo> rowListItem;
    private PreferenceUtils pref;
    private RequestQueue mQueue;
    private AppCompatActivity activity;
    private ConnectionDector connectionDector;
    String TAG = getClass().getSimpleName();
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        overridePendingTransitionEnter();
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        context = NotificationActivity.this;
        activity = NotificationActivity.this;
        rowListItem = new ArrayList<>();
        ///------------New Toolbar Code----------------

        textView = findViewById(R.id.txtTitle);
        textView.setText(getString(R.string.notification));

        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        pref = new PreferenceUtils(NotificationActivity.this);
        connectionDector = new ConnectionDector(context);
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        recyclerView = findViewById(R.id.NotificationRecycleView);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);

        if (connectionDector.isConnectingToInternet()) {
            getNotificationData();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
            getNotificationData();
        }
    }

    private void getNotificationData() {
        String url = GlobalVar.ServerAddress + "doctor/GetUserNotification";
        GlobalVar.showProgressDialog(this, "Loading...", false);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    Log.e("getNotificationData", response.toString());
                    GlobalVar.hideProgressDialog();
                    Gson gson = new Gson();
                    JSONArray jsonArray1 = null;
                    JSONArray jsonArray2 = null;
                    try {
                        jsonArray1 = response.getJSONArray("result");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                NotificationActivityPojo pojo = gson.fromJson(json.toString(), NotificationActivityPojo.class);
                                rowListItem.add(pojo);
                            }

                            jsonArray2 = response.getJSONArray("offernotification");
                            for (int i = 0; i < jsonArray2.length(); i++) {
                                JSONObject json = jsonArray2.getJSONObject(i);
                                NotificationActivityPojo pojo = gson.fromJson(json.toString(), NotificationActivityPojo.class);
                                rowListItem.add(pojo);
                            }
                            adapter = new NotificationActivityAdapter(context, rowListItem, activity);
                            recyclerView.setAdapter(adapter);

                            textView.append(" (" + rowListItem.size() + ")");

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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
