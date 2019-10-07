package com.whr.user.first_aid.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.first_aid.adapters.FirstAidAdapter;
import com.whr.user.first_aid.model.FirstAidPojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstAidActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirstAidAdapter adapter;
    String TAG = getClass().getSimpleName();
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    private PreferenceUtils pref;
    private LinearLayoutManager lLayout;
    private TextView title;
    private EditText etxSearch;

    private Context context;
    private List<FirstAidPojo> list;
    String url=GlobalVar.ServerAddress +"AndroidNew/GetFirstAidVideo";
    private AppCompatActivity activity;
    private ImageView imgBack;
    ConnectionDector dector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);

        context=FirstAidActivity.this;
        activity = FirstAidActivity.this;
        pref = new PreferenceUtils(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        dector=new ConnectionDector(context);

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.firstAidRecyclyview);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);
        title=findViewById(R.id.title);
        title.setText("First Aid Tips");

        imgBack=findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        adapter = new FirstAidAdapter(context, list, activity);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(adapter);

        etxSearch=findViewById(R.id.edSearch);

        etxSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilterList(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (dector.isConnectingToInternet()) {
            getData();
        } else {
            startActivityForResult(new Intent(context,NoInternetConnectionActivity.class),GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getData();
        }
    }



    private void getData() {
        GlobalVar.showProgressDialog(this, "Loading.....", true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "GetVideoList", response.toString());
                    GlobalVar.hideProgressDialog();
                    Gson gson = new Gson();
                    list.clear();
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("FirstAid");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                FirstAidPojo pojo = gson.fromJson(json.toString(), FirstAidPojo.class);
                                list.add(pojo);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            showSnackBar(getString(R.string.NoDataAvailable));
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

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
