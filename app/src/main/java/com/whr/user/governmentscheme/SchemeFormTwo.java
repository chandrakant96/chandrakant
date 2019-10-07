package com.whr.user.governmentscheme;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.governmentscheme.adapter.PincodeAdapter;
import com.whr.user.governmentscheme.models.PincodePojoAddress;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SchemeFormTwo extends AppCompatActivity {
    private MaterialRippleLayout imgBack;
    private TextView title, txtNext;
    private Context context;
    CoordinatorLayout coordinatorLayout;
    String uid;
    String status;

    private RequestQueue mQueue;
    String TAG = getClass().getSimpleName();
    private EditText f2etxName, f2etxHospitalName, f2etxPincode, f2etxTaluka, f2etxDist, f2etxMobile, f2etxDoctorName, f2etxDoctorEducation, f2etxWard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_form_two);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            uid = bundle.getString("uid");
            Log.e("uid",uid);
        }

        context = SchemeFormTwo.this;
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        imgBack = findViewById(R.id.imgBack);
        title = findViewById(R.id.title);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtNext = findViewById(R.id.txtNext);

        title.setText("आजाराबाबत तपशील");

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        f2etxName = findViewById(R.id.f2etxName);
        f2etxHospitalName = findViewById(R.id.f2etxHospitalName);
        f2etxPincode = findViewById(R.id.f2etxPincode);
        f2etxTaluka = findViewById(R.id.f2etxTaluka);
        f2etxDist = findViewById(R.id.f2etxDist);
        f2etxMobile = findViewById(R.id.f2etxMobile);
        f2etxDoctorName = findViewById(R.id.f2etxDoctorName);
        f2etxDoctorEducation = findViewById(R.id.f2etxDoctorEducation);
        f2etxWard = findViewById(R.id.f2etxWard);


        f2etxPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6)
                    dialogPincode(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtNext.setOnClickListener(v -> {
//            Intent intent1 = new Intent(context, SchemeFormThree.class);
//            intent1.putExtra("uid", "1");
//            startActivity(intent1);
            if (f2etxName.getText().toString().isEmpty()) {
                GlobalVar.showMessage("रुग्णाला झालेल्या आजाराचे नाव टाका", context);
            } else if (f2etxHospitalName.getText().toString().isEmpty()) {
                GlobalVar.showMessage("उपचार चालू असलेल्या रुग्णालयाचे  नाव व पत्ता टाका", context);
            } else if (f2etxPincode.getText().toString().isEmpty()) {
                GlobalVar.showMessage("पिनकोड टाका", context);
            } else if (f2etxTaluka.getText().toString().isEmpty()) {
                GlobalVar.showMessage("तालुका टाका", context);
            } else if (f2etxDist.getText().toString().isEmpty()) {
                GlobalVar.showMessage("जिल्हा टाका", context);
            } else if (f2etxMobile.getText().toString().isEmpty()) {
                GlobalVar.showMessage("उपचार चालू असलेल्या रूग्णालयाचा संपर्क क्रमांक टाका", context);
            } else {
                apply();
            }

        });
    }

    private void dialogPincode(String pincode) {
        final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_pincode);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        EditText serachview = dialog.findViewById(R.id.auto_complete_textview);

        List<PincodePojoAddress> list;
        list = new ArrayList<>();
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PincodeAdapter adapter = new PincodeAdapter(context, list);
        recyclerView.setAdapter(adapter);

        adapter.setClickLister(pojo -> {
            PincodePojoAddress pinCodeSearchModel = pojo;
            f2etxTaluka.setText(pinCodeSearchModel.getTehsil().toUpperCase());
            f2etxDist.setText(pinCodeSearchModel.getDistrict().toUpperCase());

            dialog.dismiss();
        });

        serachview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    adapter.getFilterList(charSequence.toString());

                } else {

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        imgClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        String url = "http://android.whrhealth.com/Healthcard/GetPincodeWiseStateAndCity";
        GlobalVar.showProgressDialog(this, "Loading.....", true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("pincode", pincode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "Citydata", response.toString());
                    GlobalVar.hideProgressDialog();
                    Gson gson = new Gson();
                    list.clear();
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("Citydata");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                PincodePojoAddress pojo = gson.fromJson(json.toString(), PincodePojoAddress.class);
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

        dialog.show();

    }

    private void apply() {
        GlobalVar.showProgressDialog(context, "", false);
        String url = GlobalVar.ServerAddress + "user/PatientDieseaseDetail";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", uid);
            jsonObject.put("DieseaseName", f2etxName.getText().toString());
            jsonObject.put("HospitalNameAndAddress", f2etxHospitalName.getText().toString());
            jsonObject.put("Pincode", f2etxPincode.getText().toString());
            jsonObject.put("Tehsil", f2etxTaluka.getText().toString());
            jsonObject.put("District", f2etxDist.getText().toString());
            jsonObject.put("HospitalContactNo", f2etxMobile.getText().toString());
            jsonObject.put("DoctorName", f2etxDoctorName.getText().toString());
            jsonObject.put("DoctorEducation", f2etxDoctorEducation.getText().toString());
            jsonObject.put("HospitalWardAndBedNo", f2etxWard.getText().toString());

            GlobalVar.errorLog(TAG, "jsonObject", jsonObject.toString());
            GlobalVar.errorLog(TAG, "url", url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    GlobalVar.hideProgressDialog();
                    try {
                        status = response.getString("result");
                        if (status.equals("false")) {
                            showSnackBar("Unable to register");
                        } else {
                            Intent intent1 = new Intent(context, SchemeFormThree.class);
                            intent1.putExtra("uid", uid);
                            startActivity(intent1);
                            finish();
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

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

}
