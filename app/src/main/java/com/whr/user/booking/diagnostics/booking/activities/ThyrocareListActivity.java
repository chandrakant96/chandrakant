package com.whr.user.booking.diagnostics.booking.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.diagnostics.booking.adapter.ThyroCareAdapter;
import com.whr.user.booking.diagnostics.booking.model.ThyroCarePojo;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ThyrocareListActivity extends AppCompatActivity {

    private ThyroCareAdapter adapter;
    private RecyclerView recyclerView;
    private Context context;
    private LinearLayoutManager lLayout;
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    private String value = "";
    private String url = "";
    private View viewOne, viewTwo;
    private ConnectionDector connectionDector;
    private TextView txtTitle;
    public static TextView txtTotal;
    private List<ThyroCarePojo> selectedTreatmentList = new ArrayList<>();
    private MaterialRippleLayout bookTest;

    private int pathlogyId;
    private String PathName = "";
    private PreferenceUtils pref;
    private AppCompatActivity activity;
    private String profileImagestr = "";


    public static EditText searchBox;
    private List<ThyroCarePojo> rowListItem;
    private List<ThyroCarePojo> rowListItem1;


    private String urlJsonArry = "https://www.thyrocare.com/APIS/master.svc/2Xcj7vKYZqD1FUOlOaNlogerOvCjCtfundHTSG4LUDeUGZeZv7spTUDAQP@Qipxn/ALL/products";
    String TAG = getClass().getSimpleName();
    TextView txtTest, txtOffers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thyrocare_list);
        overridePendingTransitionEnter();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = ThyrocareListActivity.this;
        pref = new PreferenceUtils(context);
        connectionDector = new ConnectionDector(context);
        activity = ThyrocareListActivity.this;
        txtTest = findViewById(R.id.txtTest);
        txtOffers = findViewById(R.id.txtOffers);
        viewOne = findViewById(R.id.viewOne);
        viewTwo = findViewById(R.id.viewTwo);
        txtTitle = findViewById(R.id.txtTitle);
        txtTotal = findViewById(R.id.txtTotal);
        bookTest = findViewById(R.id.bookTest);

        txtTitle.setText("Thyrocare");


        txtOffers.setOnClickListener(view -> {
            txtOffers.setTextColor(getResources().getColor(R.color.primary));
            txtOffers.setBackgroundColor(getResources().getColor(R.color.white));
            txtTest.setTextColor(getResources().getColor(R.color.divider));
            txtTest.setBackgroundColor(getResources().getColor(R.color.white));
            viewOne.setVisibility(View.GONE);
            viewTwo.setVisibility(View.VISIBLE);
            adapter = new ThyroCareAdapter(context, rowListItem1, value, activity);
            recyclerView.setAdapter(adapter);

        });

        txtTest.setOnClickListener(view -> {
            txtTest.setTextColor(getResources().getColor(R.color.primary));
            txtTest.setBackgroundColor(getResources().getColor(R.color.white));
            txtOffers.setTextColor(getResources().getColor(R.color.divider));
            txtOffers.setBackgroundColor(getResources().getColor(R.color.white));
            viewOne.setVisibility(View.VISIBLE);
            viewTwo.setVisibility(View.GONE);
            adapter = new ThyroCareAdapter(context, rowListItem, value, activity);
            recyclerView.setAdapter(adapter);
        });

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean pathlogyIdKey = bundle.containsKey("pathlogyId");
            boolean PathNameKey = bundle.containsKey("PathName");
            boolean profileKey = bundle.containsKey("profileImage");

            if (profileKey) {
                profileImagestr = bundle.getString("profileImage");
            }

            if (PathNameKey) {
                PathName = bundle.getString("PathName");
            }
            if (pathlogyIdKey) {
                pathlogyId = bundle.getInt("pathlogyId");
            }
        }

        rowListItem = new ArrayList<>();
        rowListItem1 = new ArrayList<>();
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);

        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        recyclerView = (RecyclerView) findViewById(R.id.pathologyRecycleListRecycleView);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);

        connectionDector = new ConnectionDector(context);
        rowListItem = new ArrayList<>();

        searchBox = (EditText) findViewById(R.id.auto_complete_textview);
        searchBox.setHint(getString(R.string.SearchByTest));
        Typeface myFont = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        searchBox.setTypeface(myFont);


        if (connectionDector.isConnectingToInternet()) {
            methodVoid();

        } else {
            startActivityForResult(new Intent(context,NoInternetConnectionActivity.class),GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilterList(query.toString().trim().toLowerCase());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        bookTest.setOnClickListener(v -> {
            for (int k = 0; k < rowListItem.size(); k++) {
                if (rowListItem.get(k).isChecked()) {
                    selectedTreatmentList.add(rowListItem.get(k));
                }
            }

            for (int k = 0; k < rowListItem1.size(); k++) {
                if (rowListItem1.get(k).isChecked()) {
                    selectedTreatmentList.add(rowListItem1.get(k));
                }
            }

            if (selectedTreatmentList.isEmpty()) {
                showSnackBar("Select Altleast One Test/Treatment");
            } else {
                Intent intent = new Intent(context, PathologyCalenderActivity.class);
                intent.putParcelableArrayListExtra("pathologyTreatmentList", (ArrayList<? extends Parcelable>) selectedTreatmentList);
                intent.putExtra("pathologyName", "Thyrocare");
                intent.putExtra("pathid", "50017");
                intent.putExtra("total", Double.parseDouble(txtTotal.getText().toString()));
                startActivity(intent);
            }
        });

        ThyroCareAdapter.totalVal = 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
           methodVoid();
        }
    }

    public void methodVoid() {
        GlobalVar.showProgressDialog(this, "Loading.....", false);
        GlobalVar.errorLog(TAG, "url", urlJsonArry);
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, urlJsonArry, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GlobalVar.hideProgressDialog();
                parsingLogic(response.toString());
            }
        }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        });
        mQueue.add(jsonRequest);
    }


    Gson gson = new Gson();

    public void parsingLogic(String response1) {
        try {

            JSONArray jsonArray1 = null;
            JSONArray jsonArray2 = null;
            JSONArray jsonArray3 = null;
            JSONArray jsonArray4 = null;
            JSONObject jsonObject;

            JSONArray jsonArray5 = null;
            try {
                JSONObject response = new JSONObject(response1);
                jsonObject = response.getJSONObject("MASTERS");
                jsonArray1 = jsonObject.getJSONArray("OFFER");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject json = jsonArray1.getJSONObject(i);
                    JSONObject json2 = json.getJSONObject("rate");
                    ThyroCarePojo pojo = gson.fromJson(json.toString(), ThyroCarePojo.class);
                    pojo.setTestName(json.getString("testnames"));
                    pojo.setName(json.getString("testnames"));
                    pojo.setB2c(json2.getString("b2c"));
                    pojo.setPay_amt(json2.getString("offer_rate"));
                    pojo.setType("OFFER");

                    if (pojo.getFasting().equalsIgnoreCase("CF")) {
                        pojo.setFasting("Fasting");

                    } else if (pojo.getFasting().equalsIgnoreCase("NF")) {
                        pojo.setFasting("Non-Fasting");
                    }

                    JSONArray jsonArray = json.getJSONArray("childs");
                    ArrayList<ThyroCarePojo.Child> childArrayList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject json1 = jsonArray.getJSONObject(j);
                        ThyroCarePojo.Child pojo1 = gson.fromJson(json1.toString(), ThyroCarePojo.Child.class);
                        childArrayList.add(pojo1);

                    }

                    pojo.setChild(childArrayList);
                    GlobalVar.errorLog(TAG, "child", String.valueOf(pojo.getChild().size()));
                    rowListItem1.add(pojo);


                }
//                jsonArray2 = jsonObject.getJSONArray("POP");
//                for (int i = 0; i < jsonArray2.length(); i++) {
//                    JSONObject json = jsonArray2.getJSONObject(i);
//                    JSONObject json2 = json.getJSONObject("rate");
//                    ThyroCarePojo pojo = gson.fromJson(json.toString(), ThyroCarePojo.class);
//                    pojo.setTestName(json.getString("name"));
//                    pojo.setB2c(json2.getString("b2c"));
//                    pojo.setPay_amt(json2.getString("pay_amt"));
//                    pojo.setType("POP");
//                    if (pojo.getFasting().equalsIgnoreCase("CF")) {
//                        pojo.setFasting("Fasting");
//
//                    } else if (pojo.getFasting().equalsIgnoreCase("NF")) {
//                        pojo.setFasting("Non-Fasting");
//                    }
//
//                    JSONArray jsonArray = json.getJSONArray("childs");
//                    ArrayList<ThyroCarePojo.Child> childArrayList = new ArrayList<>();
//                    for (int j = 0; j < jsonArray.length(); j++) {
//                        JSONObject json1 = jsonArray.getJSONObject(j);
//                        ThyroCarePojo.Child pojo1 = gson.fromJson(json1.toString(), ThyroCarePojo.Child.class);
//                        childArrayList.add(pojo1);
//
//                    }
//                    pojo.setChild(childArrayList);
//                    GlobalVar.errorLog(TAG, "child", String.valueOf(pojo.getChild().size()));
//                    rowListItem.add(pojo);
//                }
                jsonArray3 = jsonObject.getJSONArray("PROFILE");
                for (int i = 0; i < jsonArray3.length(); i++) {
                    JSONObject json = jsonArray3.getJSONObject(i);
                    JSONObject json2 = json.getJSONObject("rate");
                    ThyroCarePojo pojo = gson.fromJson(json.toString(), ThyroCarePojo.class);
                    pojo.setTestName(json.getString("name"));
                    pojo.setB2c(json2.getString("b2c"));
                    pojo.setPay_amt(json2.getString("pay_amt"));
                    pojo.setType("PROFILE");
                    if (pojo.getFasting().equalsIgnoreCase("CF")) {
                        pojo.setFasting("Fasting");

                    } else if (pojo.getFasting().equalsIgnoreCase("NF")) {
                        pojo.setFasting("Non-Fasting");
                    }

                    JSONArray jsonArray = json.getJSONArray("childs");
                    ArrayList<ThyroCarePojo.Child> childArrayList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject json1 = jsonArray.getJSONObject(j);
                        ThyroCarePojo.Child pojo1 = gson.fromJson(json1.toString(), ThyroCarePojo.Child.class);
                        childArrayList.add(pojo1);

                    }
                    pojo.setChild(childArrayList);
                    GlobalVar.errorLog(TAG, "child", String.valueOf(pojo.getChild().size()));
                    rowListItem.add(pojo);
                }

                jsonArray4 = jsonObject.getJSONArray("TESTS");
                for (int i = 0; i < jsonArray4.length(); i++) {
                    JSONObject json = jsonArray4.getJSONObject(i);
                    JSONObject json2 = json.getJSONObject("rate");
                    ThyroCarePojo pojo = gson.fromJson(json.toString(), ThyroCarePojo.class);
                    pojo.setTestName(json.getString("code"));
                    pojo.setB2c(json2.getString("b2c"));
                    pojo.setPay_amt(json2.getString("pay_amt"));
                    pojo.setType("TESTS");
                    if (pojo.getFasting().equalsIgnoreCase("CF")) {
                        pojo.setFasting("Fasting");

                    } else if (pojo.getFasting().equalsIgnoreCase("NF")) {
                        pojo.setFasting("Non-Fasting");
                    }

                    JSONArray jsonArray = json.getJSONArray("childs");
                    ArrayList<ThyroCarePojo.Child> childArrayList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject json1 = jsonArray.getJSONObject(j);
                        ThyroCarePojo.Child pojo1 = gson.fromJson(json1.toString(), ThyroCarePojo.Child.class);
                        childArrayList.add(pojo1);

                    }
                    pojo.setChild(childArrayList);
                    GlobalVar.errorLog(TAG, "child", String.valueOf(pojo.getChild().size()));
                    rowListItem.add(pojo);
                }

                adapter = new ThyroCareAdapter(context, rowListItem, value, activity);
                recyclerView.setAdapter(adapter);

            } catch (JSONException j) {
                j.printStackTrace();
            }
        } finally {
            GlobalVar.errorLog(TAG, "finally", "executed");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransitionExit();
        return true;
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
