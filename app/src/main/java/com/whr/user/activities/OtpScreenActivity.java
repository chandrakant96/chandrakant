package com.whr.user.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.goodiebag.pinview.Pinview;

import com.google.firebase.iid.FirebaseInstanceId;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;


import org.json.JSONException;
import org.json.JSONObject;

public class OtpScreenActivity extends AppCompatActivity {
    private TextView txtMobileNumber, txtResendCode, txtChangeNumber;
    private String mobNumber, randomPIN, status;
    private Pinview pinview;
    private int otpKey = 0;
    Context context;
    String TAG = getClass().getSimpleName();
    private RequestQueue mQueue;
    private PreferenceUtils preference;
    String token;
    private static final int READ_SMS_PERMISSION_CODE = 101;
    Context mContext;
    ConnectionDector dector;

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);
        mContext = OtpScreenActivity.this;
        context = OtpScreenActivity.this;
        dector = new ConnectionDector(context);
        token = FirebaseInstanceId.getInstance().getToken();
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        preference = new PreferenceUtils(OtpScreenActivity.this);

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        mobNumber = bundle.getString("mobNumber");
        randomPIN = bundle.getString("key");
        status = bundle.getString("status");

        txtChangeNumber = findViewById(R.id.txtChangeNumber);
        txtResendCode = findViewById(R.id.txtResendCode);
        txtMobileNumber = findViewById(R.id.txtMobileNumber);
        pinview = findViewById(R.id.pinview);

        txtMobileNumber.setText(mobNumber);
        txtChangeNumber.setOnClickListener(v -> finish());
S
        txtResendCode.setOnClickListener(v -> {
            otpKey = (int) (Math.random() * 9000) + 1000;
            Log.e("Random Otp", Integer.toString(otpKey));
            if (dector.isConnectingToInternet()) {
                resendOtp(otpKey);
            } else {
                startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
                overridePendingTransitionEnter();
            }

        });




        pinview.setPinViewEventListener((pinview, fromUser) -> {
            if (pinview.getValue().equals(randomPIN)) {
                if (status.equals("mobile")) {
                    if (dector.isConnectingToInternet()) {
                        doSocialLogin();
                    } else {
                        startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
                        overridePendingTransitionEnter();
                    }
                } else {
                    if (dector.isConnectingToInternet()) {
                        updateMobileNumber();
                    } else {
                        startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
                        overridePendingTransitionEnter();
                    }
                }
            } else {
                GlobalVar.showMessage("Wrong OTP !!! Enter Correct OTP",context);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    //-----------webservice call-----------

    private void resendOtp(final int otpCode) {
        GlobalVar.showProgressDialog(this, "Loading....", false);
        String url = GlobalVar.ServerAddress + "AndroidNew/SendOtpToAllNumber";
        JSONObject params = new JSONObject();
        try {
            params.put("cno", mobNumber);
            params.put("key", otpCode);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "params", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, params,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "SendOtpToUser", response.toString());
                    try {
                        boolean result = response.getBoolean("result");
                        randomPIN = String.valueOf(otpCode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showMessage(VolleyErrorHelper.getMessage(error, context),context);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    public void updateMobileNumber() {
        String url = GlobalVar.ServerAddress + "/AndroidNew/UpdateUserCotactNumber";
        JSONObject params = new JSONObject();
        try {
            params.put("uid", preference.getUID());
            params.put("cno", mobNumber);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "params", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, params,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "updateMobileNumber", response.toString());
                    finish();
                }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showMessage(VolleyErrorHelper.getMessage(error, context),context);

        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);

    }

    private void doSocialLogin() {
        GlobalVar.showProgressDialog(context, "Please Wait.....", false);
        JSONObject obj = new JSONObject();
        try {
            obj.put("logintype", "ANDROID");
            obj.put("uid", mobNumber);
            obj.put("name", "");
            obj.put("email", "");
            obj.put("deviceid", token);
            obj.put("cno", mobNumber);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String url = GlobalVar.ServerAddress + "AndroidNew/CheckUserExistUsingContactnoV1";
        Log.e("url", url);
        Log.e("obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    Log.e("doSocialLogin:", response.toString());
                    try {
                        boolean isExist = response.getString("result").equals("true");
                        preference.setUID(mobNumber);
                        preference.setLogin(true);
                        preference.setKeepMeLoin(true);
                        Intent i = new Intent(context, NevigationDrawerDashBordActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransitionEnter();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showMessage(VolleyErrorHelper.getMessage(error, context),context);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }
}



