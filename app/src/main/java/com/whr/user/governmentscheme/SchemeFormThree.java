package com.whr.user.governmentscheme;

import android.app.DatePickerDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SchemeFormThree extends AppCompatActivity {
    private MaterialRippleLayout imgBack;
    private TextView title, txtNext;
    private Context context;
    CoordinatorLayout coordinatorLayout;

    private EditText f3TotalIncome, f3etxIncomeCertificateNumber, f3etxReshanCardNumber, f3etxTotalRupye, f3etxPatrakNumber, f3TotalTime;
    private EditText f3ShifrasName, f3ShifarasPad, f3ShifrasNumber, f3Date, f3Address, f3pincode, f3Taluka, f3Dist;

    private RequestQueue mQueue;
    String TAG = getClass().getSimpleName();
    String uid;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_form_three);

        context = SchemeFormThree.this;
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            uid = bundle.getString("uid");
            Log.e("uid",uid);
        }

        imgBack = findViewById(R.id.imgBack);
        title = findViewById(R.id.title);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtNext = findViewById(R.id.txtNext);

        f3TotalIncome = findViewById(R.id.f3TotalIncome);
        f3etxIncomeCertificateNumber = findViewById(R.id.f3etxIncomeCertificateNumber);
        f3etxReshanCardNumber = findViewById(R.id.f3etxReshanCardNumber);
        f3etxTotalRupye = findViewById(R.id.f3etxTotalRupye);
        f3etxPatrakNumber = findViewById(R.id.f3etxPatrakNumber);
        f3TotalTime = findViewById(R.id.f3TotalTime);

        f3ShifrasName = findViewById(R.id.f3ShifrasName);
        f3ShifarasPad = findViewById(R.id.f3ShifarasPad);
        f3ShifrasNumber = findViewById(R.id.f3ShifrasNumber);
        f3Date = findViewById(R.id.f3Date);
        f3Address = findViewById(R.id.f3Address);
        f3pincode = findViewById(R.id.f3pincode);
        f3Taluka = findViewById(R.id.f3Taluka);
        f3Dist = findViewById(R.id.f3Dist);

        title.setText("आवश्यक कागदपत्रांचा तपशील");

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        f3pincode.addTextChangedListener(new TextWatcher() {
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

        Calendar today = Calendar.getInstance();
        f3Date.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            final DatePickerDialog dp = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog, (view1, year, monthOfYear, dayOfMonth) -> {
                DecimalFormat formatter = new DecimalFormat("00");
                today.set(year, monthOfYear, dayOfMonth);
                long selectedTimeInMillis = today.getTimeInMillis();
                long systemTimeInMillis = Calendar.getInstance().getTimeInMillis();

                if (selectedTimeInMillis <= systemTimeInMillis) {
                    f3Date.setText(formatter.format(dayOfMonth) + "/" + formatter.format(monthOfYear + 1) + "/" + year);
                } else {
                    Toast.makeText(context, getString(R.string.Cannotselectfuturedate), Toast.LENGTH_SHORT).show();
                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            dp.getDatePicker().setMaxDate(today.getTimeInMillis());
            dp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dp.show();

        });

        txtNext.setOnClickListener(v -> {
//            Intent intent1 = new Intent(context, SchemeFormFour.class);
//            intent1.putExtra("uid", "1");
//            startActivity(intent1);

            if (f3TotalIncome.getText().toString().isEmpty()) {
                GlobalVar.showMessage("मागील वर्षाचे कुटुंबाचे एकत्रित उत्पन्न रुपये टाका", context);
            } else if (f3etxIncomeCertificateNumber.getText().toString().isEmpty()) {
                GlobalVar.showMessage("उत्पन्नाचे प्रमाणपत्र क्रमांकटा का", context);
            } else if (f3etxReshanCardNumber.getText().toString().isEmpty()) {
                GlobalVar.showMessage("राशन कार्ड क्रमांकटा का", context);
            } else if (f3etxTotalRupye.getText().toString().isEmpty()) {
                GlobalVar.showMessage("उपचारासाठी रुग्णालयाचा खर्च टाका", context);
            } else if (f3ShifrasName.getText().toString().isEmpty()) {
                GlobalVar.showMessage("शिफारस करणाऱ्यांचे संपूर्ण नाव टाका", context);
            } else if (f3ShifarasPad.getText().toString().isEmpty()) {
                GlobalVar.showMessage("शिफारस करणाऱ्यांचे पद टाका", context);
            } else if (f3ShifrasNumber.getText().toString().isEmpty()) {
                GlobalVar.showMessage("शिफारस पत्र क्रमांक टाका", context);
            } else if (f3Date.getText().toString().isEmpty()) {
                GlobalVar.showMessage("दिनांक टाका", context);
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
            f3Taluka.setText(pinCodeSearchModel.getTehsil().toUpperCase());
            f3Dist.setText(pinCodeSearchModel.getDistrict().toUpperCase());

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
        String url = GlobalVar.ServerAddress + "user/ReferalInformation";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", uid);
            jsonObject.put("backyearincome", f3TotalIncome.getText().toString().trim());
            jsonObject.put("incomecertificateno", f3etxIncomeCertificateNumber.getText().toString().trim());
            jsonObject.put("rationcardno", f3etxReshanCardNumber.getText().toString().trim());
            jsonObject.put("TotalCostOfHospital", f3etxTotalRupye.getText().toString().trim());
            jsonObject.put("CostCertificateNo", f3etxPatrakNumber.getText().toString().trim());
            jsonObject.put("TimeOfTreatment", f3TotalTime.getText().toString().trim());

            jsonObject.put("ApplicantFullName", f3ShifrasName.getText().toString().trim());
            jsonObject.put("ApplicantDegree", f3ShifarasPad.getText().toString().trim());
            jsonObject.put("ApplicantNo", f3ShifrasNumber.getText().toString().trim());
            jsonObject.put("Date", f3Date.getText().toString().trim());
            jsonObject.put("AddressOfApplicant", f3Address.getText().toString().trim());
            jsonObject.put("PincodeOfApplicant", f3pincode.getText().toString().trim());
            jsonObject.put("Tehsil", f3Taluka.getText().toString().trim());
            jsonObject.put("Dist", f3Dist.getText().toString().trim());

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
                            Intent intent1 = new Intent(context, SchemeFormFour.class);
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
