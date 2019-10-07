package com.whr.user.com.WHR.com.WHR.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.com.WHR.adapters.PrescriptionViewAdapter;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pojo.PrescriptioPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionViewActivity extends AppCompatActivity {

    private ImageView headerimg, footerimg;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private Context context;
    private List<PrescriptioPojo> list = new ArrayList<>();

    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    private String date, uname, docName;
    private int doctorId, report_id;
    private String patientid;
    private TextView ageEdittext, bllodPluseText, weightText, rrText, bslRbsText, patientName, presDate, patienIdTxt;
    private FloatingActionButton discriptionButton;
    private PrescriptionViewAdapter adapter;
    private AlertDialog discriptionAlert;
    private String discriptionString = "";
    private boolean isPresHistory = false;
    private String url = "";
    TextView title;

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_view);
        context = PrescriptionViewActivity.this;
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        title = findViewById(R.id.title);
        ///------------New Toolbar Code----------------

        title.setText(getString(R.string.PrescriptionView));
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        ///------------New Toolbar code------------------------------

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        headerimg = findViewById(R.id.headerimg);
        footerimg = findViewById(R.id.footerimg);
        recyclerView = findViewById(R.id.prescriptionrecyclerview);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);

        discriptionButton = findViewById(R.id.discriptionButton);
        ageEdittext = findViewById(R.id.ageEdittext);
        bllodPluseText = findViewById(R.id.bllodPluseText);
        weightText = findViewById(R.id.weightText);
        rrText = findViewById(R.id.rrText);
        bslRbsText = findViewById(R.id.bslRbsText);

        patientName = findViewById(R.id.patientName);
        presDate = findViewById(R.id.presDate);
        patienIdTxt = findViewById(R.id.patienIdTxt);

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean report_idkey = bundle.containsKey("report_id");
            boolean patientidkey = bundle.containsKey("patientid");
            boolean doctorIdkey = bundle.containsKey("doctorId");
            boolean docNameIdkey = bundle.containsKey("docName");
            boolean unameIdkey = bundle.containsKey("uname");
            boolean datedkey = bundle.containsKey("date");
            boolean isPresHistorykey = bundle.containsKey("isPresHistory");

            if (isPresHistorykey) {
                isPresHistory = bundle.getBoolean("isPresHistory");

                if (isPresHistory) {
                    url = GlobalVar.ServerAddress + "User/ViewPriscription";
                } else {
                    url = GlobalVar.ServerAddress + "User/GetPriscriptionwithheaderfooter";
                }
            }

            if (report_idkey) {
                report_id = bundle.getInt("report_id");
            }

            if (patientidkey) {
                patientid = bundle.getString("patientid");
                patienIdTxt.setText("" + patientid);
            }
            if (doctorIdkey) {
                doctorId = bundle.getInt("doctorId");
            }
            if (docNameIdkey) {
                docName = bundle.getString("docName");
            }
            if (unameIdkey) {
                uname = bundle.getString("uname");
                patientName.setText(uname + "");
            }
            if (datedkey) {
                date = bundle.getString("date");
                presDate.setText(date + "");
            }
        }


        discriptionButton.setOnClickListener(v -> {
            if (discriptionAlert != null && !discriptionAlert.isShowing()) {
                discriptionAlert.show();
            }
        });


        ConnectionDector connectionDector = new ConnectionDector(PrescriptionViewActivity.this);
        if (connectionDector.isConnectingToInternet()) {
            getPrescriptionFromServer();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
    }


    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }


    String TAG = getClass().getSimpleName();

    private void getPrescriptionFromServer() {

        GlobalVar.showProgressDialog(this, "Loading...", false);
        JSONObject obj = new JSONObject();
        try {
            obj.put("priscriptionId", report_id);
            obj.put("uid", patientid);
            obj.put("did", doctorId);
            GlobalVar.errorLog(TAG, "obj=>", obj.toString());
            GlobalVar.errorLog(TAG, "url=>", url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GlobalVar.hideProgressDialog();
                GlobalVar.errorLog(TAG, "response=>", response.toString());
                try {
                    list.clear();
                    Gson gson = new Gson();
                    JSONObject jsonRootObject = new JSONObject(response.toString());
                    JSONArray jsonArray1 = jsonRootObject.optJSONArray("medicine");
                    jsonArray1 = response.getJSONArray("medicine");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json = jsonArray1.getJSONObject(i);
                        PrescriptioPojo pojo = gson.fromJson(json.toString(), PrescriptioPojo.class);
                        list.add(pojo);
                    }
                    if (list.size() > 0) {
                        adapter = new PrescriptionViewAdapter(context, list);
                        recyclerView.setAdapter(adapter);
                    } else {
                        list = new ArrayList<>();
                        adapter = new PrescriptionViewAdapter(context, list);
                        recyclerView.setAdapter(adapter);
                        GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                    }

                    JSONObject resultjsonObject = response.getJSONObject("result");
                    ageEdittext.append("\n" + resultjsonObject.getString("age"));
                    bllodPluseText.append("\n" + resultjsonObject.getString("blod_pluse"));
                    weightText.append("\n" + resultjsonObject.getString("weight"));
                    rrText.append("\n" + resultjsonObject.getString("rr"));
                    bslRbsText.append("\n" + resultjsonObject.getString("bsl_rsl"));
                    patienIdTxt.setText(resultjsonObject.getString("uname"));

                    discriptionString = resultjsonObject.getString("description");

                    AlertDialog.Builder discriptionbuilder = new AlertDialog.Builder(PrescriptionViewActivity.this);
                    LayoutInflater discriptioninflater = getLayoutInflater();
                    View discriptionindoorview = discriptioninflater.inflate(R.layout.treatment_description_popup, null);
                    discriptionbuilder.setView(discriptionindoorview);
                    TextView expandableTextView;
                    expandableTextView = (TextView) discriptionindoorview.findViewById(R.id.expandable_text);

                    final ImageView discriptioncloseButton = (ImageView) discriptionindoorview.findViewById(R.id.description_close);

                    expandableTextView.setText(discriptionString);
                    discriptioncloseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (discriptionAlert != null && discriptionAlert.isShowing()) {
                                discriptionAlert.cancel();
                            }
                        }
                    });


                    discriptionAlert = discriptionbuilder.create();

                    String headerImagStr = resultjsonObject.getString("headerImg");
                    String footerImagStr = resultjsonObject.getString("footerImg");
                    String str = headerImagStr.replace("\\", "");
                    if (str != null) {
                        if (str.length() > 0) {
                            String dpStr = str.replaceAll(" ", "%20");
                            if (dpStr != null) {
                                Picasso.with(PrescriptionViewActivity.this).load(dpStr).fit().into(headerimg);
                            }
                        } else {
                        }
                    } else {
                    }
                    String footerIstr = footerImagStr.replace("\\", "");
                    if (footerIstr != null) {
                        if (footerIstr.length() > 0) {
                            String dpStr = footerIstr.replaceAll(" ", "%20");
                            if (dpStr != null) {
                                Picasso.with(PrescriptionViewActivity.this).load(dpStr).fit().into(footerimg);
                            }
                        } else {
                        }
                    } else {
                    }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here

            getPrescriptionFromServer();
        }
    }

}
