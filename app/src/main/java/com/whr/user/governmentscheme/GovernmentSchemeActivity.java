package com.whr.user.governmentscheme;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.R;
import com.whr.user.activities.LocationFindActivity;
import com.whr.user.activities.NevigationDrawerDashBordActivity;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomStringRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.first_aid.activities.FirstAidActivity;
import com.whr.user.first_aid.adapters.FirstAidAdapter;
import com.whr.user.first_aid.model.FirstAidPojo;
import com.whr.user.governmentscheme.adapter.GovermentSchemeAdapter;
import com.whr.user.governmentscheme.models.GovernmentSchemePojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GovernmentSchemeActivity extends AppCompatActivity {
    private MaterialRippleLayout imgBack;
    private TextView title;
    private Context context;

    String TAG = getClass().getSimpleName();
    private List<GovernmentSchemePojo> list;
    private ConnectionDector dector;
    private GovermentSchemeAdapter adapter;
    private RequestQueue mQueue;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_scheme);

        context = GovernmentSchemeActivity.this;
        list = new ArrayList<>();
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        dector = new ConnectionDector(context);
        recyclerView = findViewById(R.id.recyclerviewScheme);

        adapter = new GovermentSchemeAdapter(context, list);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(adapter);


        init();
    }

    public void init() {
        imgBack = findViewById(R.id.imgBack);
        title = findViewById(R.id.title);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        title.setText("Government Scheme");

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });


        if (dector.isConnectingToInternet()) {
            getData();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
        }


    }

    private void getData() {
        String url = GlobalVar.ServerAddress + "user/GetGovernmentScheme";
        GlobalVar.showProgressDialog(this, "Loading.....", true);
        JSONObject obj = new JSONObject();

        GlobalVar.errorLog(TAG, "obj", obj.toString());
        GlobalVar.errorLog(TAG, "url", url);

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "GetList", response.toString());
                    GlobalVar.hideProgressDialog();
                    Gson gson = new Gson();
                    list.clear();
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("GovernmentScheme");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                GovernmentSchemePojo pojo = gson.fromJson(json.toString(), GovernmentSchemePojo.class);
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

}
