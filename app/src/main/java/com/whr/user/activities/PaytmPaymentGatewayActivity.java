package com.whr.user.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomStringRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaytmPaymentGatewayActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    Context context;
    private RequestQueue mQueue;
    CoordinatorLayout coordinatorLayout;
    PreferenceUtils pref;
    ProgressBar progressBar;
    private double amounttoPay = 0;
    public static final String PAYMENT_KEY = "payment_key";
    public static final String AMOUNT_KEY = "amount_key";
    //public static final String PAYMENT_KEY = "payment_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_payment_gateway);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        context = this;
        pref = new PreferenceUtils(context);
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean isAmount = bundle.containsKey(AMOUNT_KEY);
            if (isAmount) {
                amounttoPay = intentExtra.getDoubleExtra(AMOUNT_KEY, 0);
            }
        }

        callPaymentWebservices();

        coordinatorLayout.setOnClickListener(v -> callPaymentWebservices());
    }

    //call webservices

    public void callPaymentWebservices() {
        if (new ConnectionDector(context).isConnectingToInternet()) {
            generateUniqueKey();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void generateUniqueKey() {
        GlobalVar.showProgressDialog(this, "Loading....", false);
        String url = "http://android.whrhealth.com/" + "user/GenerateUniquekey";
        //   String url = GlobalVar.ServerAddress + "user/GenerateUniquekey";
        JSONObject obj = new JSONObject();
        try {
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1000000000);
            obj.put("orderid", randomInt);
            //  Log.e(TAG + " url", url);
            //    Log.e(TAG + " obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "generateUniqueKey", response.toString());
                    GlobalVar.hideProgressDialog();
                    try {
                        JSONObject jobj = response.getJSONObject("uniquekey");
                        String key = jobj.getString("uniquekey");
                        if (key != null && !key.isEmpty()) {
                            paytmCode(key);
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

    private void paytmCode(String key) {
        progressBar.setVisibility(View.VISIBLE);
        final int orderId = Integer.parseInt(String.valueOf(key));
        String PAYTM_URL = "http://android.whrhealth.com/Checksum/GenerateChecksum.aspx";
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, PAYTM_URL,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    GlobalVar.errorLog(TAG, "paytmCode", response);
                    GlobalVar.errorLog(TAG, "paytmCode", String.valueOf(orderId));

                    //-----------------Paytm Parameters-------------------------------
                    PaytmPGService paytmPGService = PaytmPGService.getProductionService();
                    Map<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put("MID", GlobalVar.MID);
                    paramMap.put("ORDER_ID", String.valueOf(orderId));
                    paramMap.put("CUST_ID", String.valueOf(orderId));
                    paramMap.put("INDUSTRY_TYPE_ID", GlobalVar.Industry_type_ID);
                    paramMap.put("CHANNEL_ID", GlobalVar.Channel_ID);
                    String str = String.valueOf(amounttoPay);
                    paramMap.put("TXN_AMOUNT", str);
                    paramMap.put("WEBSITE", GlobalVar.Website);
                    paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
                    paramMap.put("EMAIL", pref.getEmail());
                    paramMap.put("MOBILE_NO", pref.getUID());
                    paramMap.put("CHECKSUMHASH", response);
                    GlobalVar.errorLog(TAG, "paytmCode paramMap", paramMap.toString());

                    //-----------------Paytm-------------------------------
                    PaytmOrder paytmOrder = new PaytmOrder(paramMap);
                    paytmPGService.initialize(paytmOrder, null);
                    paytmPGService.startPaymentTransaction(context, true,
                            true,
                            new PaytmPaymentTransactionCallback() {
                                @Override
                                public void someUIErrorOccurred(String inErrorMessage) {
                                    GlobalVar.errorLog(TAG, "paytmCode", response);
                                    GlobalVar.errorLog(TAG, "someUIErrorOccurred", inErrorMessage);
                                    finish();
                                }

                                @Override
                                public void onTransactionResponse(Bundle inResponse) {
                                    GlobalVar.errorLog(TAG, "paytmCode", "inResponse");
                                    GlobalVar.errorLog(TAG, "onTransactionResponse inResponse", inResponse.toString());
                                    String tranid = "";
                                    String rspcode = "";
                                    String TXNAMOUNT = "";

                                    rspcode = inResponse.getString("RESPCODE");
                                    if (rspcode != null && !rspcode.isEmpty()) {
                                        TXNAMOUNT = inResponse.getString("TXNAMOUNT");
                                        tranid = inResponse.getString("TXNID");

                                        switch (rspcode) {
                                            case "01":
                                                if (new ConnectionDector(context).isConnectingToInternet()) {
                                                    GlobalVar.showSnackBar(coordinatorLayout,
                                                            "Payment Successful Of ₹ " + TXNAMOUNT,
                                                            context, R.color.green);
                                                    Intent intent = getIntent();
                                                    intent.putExtra(PAYMENT_KEY, inResponse);
                                                    setResult(Activity.RESULT_OK, intent);
                                                    finish();
                                                    //bookAppoinment(adapter.bookedButton, Double.parseDouble(TXNAMOUNT), bundlevalue, tranid);
                                                } else {
                                                    GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
                                                    return;
                                                }
                                                break;
                                            case "141":
                                                GlobalVar.showSnackBar(coordinatorLayout,
                                                        "Transaction cancelled by customer after landing on Payment Gateway Page",
                                                        context, R.color.red);
                                                finishActivity();
                                                break;
                                            case "":
                                                GlobalVar.showSnackBar(coordinatorLayout,
                                                        "Payment Failed due to a Bank Failure. Please try after some time.", context, R.color.red);
                                                finishActivity();
                                                break;
                                            case "810":
                                                GlobalVar.showSnackBar(coordinatorLayout,
                                                        "Page closed by customer after landing on Payment Gateway Page.", context, R.color.red);
                                                finishActivity();
                                                break;
                                            case "8102":
                                                GlobalVar.showSnackBar(coordinatorLayout,
                                                        "Customer had sufficient Wallet balance for completing transaction", context, R.color.red);
                                                finishActivity();
                                                break;
                                            case "8103":
                                                GlobalVar.showSnackBar(coordinatorLayout,
                                                        "Customer had in-sufficient Wallet balance for completing transaction.",
                                                        context, R.color.red);
                                                finishActivity();
                                                break;

                                            default:
                                               /* GlobalVar.showSnackBar(coordinatorLayout,
                                                    "Customer had in-sufficient Wallet balance for completing transaction.",
                                                    context, R.color.red)*/
                                                ;
                                                finishActivity();
                                                break;
                                        }
                                    }
                                }

                                @Override
                                public void networkNotAvailable() {
                                    GlobalVar.errorLog(TAG, "paytmCode", "networkNotAvailable");
                                    finishActivity();
                                }

                                @Override
                                public void clientAuthenticationFailed(String inErrorMessage) {
                                    GlobalVar.errorLog(TAG, "paytmCode", "clientAuthenticationFailed");
                                    GlobalVar.errorLog(TAG, "paytmCode", "inErrorMessage");
                                    finishActivity();
                                }

                                @Override
                                public void onErrorLoadingWebPage(int iniErrorCode,
                                                                  String inErrorMessage, String inFailingUrl) {
                                    GlobalVar.errorLog(TAG, "paytmCode", "onErrorLoadingWebPage");
                                    GlobalVar.errorLog(TAG, "paytmCode", inErrorMessage);
                                    finishActivity();
                                }

                                @Override
                                public void onBackPressedCancelTransaction() {
                                    GlobalVar.errorLog(TAG, "paytmCode", "onBackPressedCancelTransaction");
                                    finishActivity();
                                }

                                @Override
                                public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                    GlobalVar.errorLog(TAG, "paytmCode", "onTransactionCancel");
                                    GlobalVar.errorLog(TAG, "paytmCode", inErrorMessage);
                                    finishActivity();
                                }
                            });
                    //-------------------------Paytm--------------------------
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> obj = new HashMap<String, String>();
                obj.put("MID", GlobalVar.MID);
                obj.put("ORDER_ID", String.valueOf(orderId));
                obj.put("CUST_ID", String.valueOf(orderId));
                obj.put("CHANNEL_ID", GlobalVar.Channel_ID);
                obj.put("INDUSTRY_TYPE_ID", GlobalVar.Industry_type_ID);
                obj.put("WEBSITE", GlobalVar.Website);
                String str = String.valueOf(amounttoPay);
                obj.put("TXN_AMOUNT", str);
                obj.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
                obj.put("EMAIL", pref.getEmail());
                obj.put("MOBILE_NO", pref.getUID());
                GlobalVar.errorLog("orderid@chechsum=>", "Order_Id", String.valueOf(orderId));
                GlobalVar.errorLog(TAG, "getParams", obj.toString());
                return obj;
            }
        };
        mQueue.add(stringRequest);
    }

    public void finishActivity() {
        Intent intent = getIntent();
        setResult(1000, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
            callPaymentWebservices();
        }
    }



}
