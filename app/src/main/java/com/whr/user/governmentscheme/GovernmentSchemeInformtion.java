package com.whr.user.governmentscheme;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.governmentscheme.adapter.GovermentSchemeAdapter;
import com.whr.user.governmentscheme.adapter.GovernmentSchemeInformationAdapter;
import com.whr.user.governmentscheme.models.GovernmentSchemeInformationPojo;
import com.whr.user.governmentscheme.models.GovernmentSchemePojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GovernmentSchemeInformtion extends AppCompatActivity {
    private MaterialRippleLayout imgBack;
    private TextView title;
    private Context context;
    String TAG = getClass().getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    private ConnectionDector dector;
    String id, name;

    private RequestQueue mQueue;
    private List<GovernmentSchemeInformationPojo> list;
    private GovernmentSchemeInformationAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;

    private RelativeLayout layoutInformation;
    private TextView txtInforation, txtApply, txtSchemeTitle;
    String ShemeInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_scheme_informtion);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            id = bundle.getString("id");
            name = bundle.getString("name");
        }

        context = GovernmentSchemeInformtion.this;
        dector = new ConnectionDector(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewSchemeInformation);

        adapter = new GovernmentSchemeInformationAdapter(context, list);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(adapter);

        init();

        if (id.equals("3")) {
            layoutInformation.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            layoutInformation.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void init() {
        imgBack = findViewById(R.id.imgBack);
        title = findViewById(R.id.title);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        layoutInformation = findViewById(R.id.layoutInformation);
        txtInforation = findViewById(R.id.txtInforation);
        txtApply = findViewById(R.id.txtApply);
        txtSchemeTitle = findViewById(R.id.txtSchemeTitle);

        title.setText("Government Scheme Information");
        txtSchemeTitle.setText(name);
        txtInforation.setMovementMethod(new ScrollingMovementMethod());

        txtApply.setOnClickListener(v -> {
            Intent intent = new Intent(context, SchemeFormOne.class);
            intent.putExtra("id", id);
            intent.putExtra("name", name);
            startActivity(intent);

        });


        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });


        if (dector.isConnectingToInternet()) {
            if (id.equals("3")) {
                getEngioList();
            } else {
                getSchemeInfo();
            }
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
        }


    }

    private void getEngioList() {
        String url = GlobalVar.ServerAddress + "user/GetEngioList";
        GlobalVar.showProgressDialog(this, "Loading.....", true);
        JSONObject obj = new JSONObject();

        GlobalVar.errorLog(TAG, "obj", obj.toString());
        GlobalVar.errorLog(TAG, "url", url);

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "GetEngioList", response.toString());
                    GlobalVar.hideProgressDialog();
                    Gson gson = new Gson();
                    list.clear();
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("Engio");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                GovernmentSchemeInformationPojo pojo = gson.fromJson(json.toString(), GovernmentSchemeInformationPojo.class);
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

    private void getSchemeInfo() {
        JSONObject obj = new JSONObject();
        String url = GlobalVar.ServerAddress + "user/GetSchemeInformation";
        //GlobalVar.showProgressDialog(context, "Loading......", false);
        try {
            obj.put("id", id);
            GlobalVar.errorLog(TAG, "url", url);
            Log.e("obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, response -> {
            parseUserResponse(response);
        }, error -> {
            //GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        });

        mQueue.add(customJSONObjectRequest);
    }


    private void parseUserResponse(JSONObject jsonRootobj) {
        try {
            JSONObject jsonObject = jsonRootobj.getJSONObject("profile");

            ShemeInformation = jsonObject.getString("ShemeInformation");


            txtInforation.setText(ShemeInformation);

        } catch (Exception e) {
            GlobalVar.hideProgressDialog();
            e.printStackTrace();
        }
    }
}
