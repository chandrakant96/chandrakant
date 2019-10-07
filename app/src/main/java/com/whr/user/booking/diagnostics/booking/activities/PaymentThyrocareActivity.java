package com.whr.user.booking.diagnostics.booking.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.whr.user.activities.PaytmPaymentGatewayActivity;
import com.whr.user.activities.ReceiptActivity;
import com.whr.user.booking.diagnostics.booking.model.PathologyPayment;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.booking.diagnostics.booking.model.ThyroCarePojo;
import com.whr.user.model.PromocodeResponce;
import com.whr.user.pojo.GlobalVar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PaymentThyrocareActivity extends AppCompatActivity {
    private Context context;
    TextView txtAll;
    List<ThyroCarePojo> selectedTreatmentList = new ArrayList<>();
    RadioButton radioPayOnline;
    CheckBox chkRedeem;
    TextView txtOnlinePrice;
    double totalPay = 0;
    String TAG = getClass().getSimpleName();
    CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    LinearLayout layoutWalletPoint;
    TextView txtPay;
    PreferenceUtils pref;
    private String pathologyName = "", TransactionForId = "", AppointmentDate = "", patientId = "";
    MaterialRippleLayout materialPayBottom;
    private String patientName = "", gender = "", pincode = "", address = "", email = "";
    ConnectionDector dector;


    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private double amounttoPay;
    String isReports = "N";
    String age = "";
    CheckBox checkBox;

    SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm:ss a");
    String bookedTimeString;

    //promo code
    EditText edPromoCode;
    TextView txtApplyPromo, txtPromoTitle;
    ImageView imgClosePromo;
    String promo_code_Str = "";
    double offerDeduction = 0;

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thyrocare_payment);
        txtAll = findViewById(R.id.txtAll);
        context = this;
        pref = new PreferenceUtils(context);
        layoutWalletPoint = findViewById(R.id.layoutWalletPoint);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        radioPayOnline = findViewById(R.id.radioPayOnline);
        chkRedeem = findViewById(R.id.chkRedeem);
        txtOnlinePrice = findViewById(R.id.txtOnlinePrice);
        txtPay = findViewById(R.id.txtPay);
        overridePendingTransitionEnter();
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        checkBox = findViewById(R.id.chkHardcopy);
        materialPayBottom = findViewById(R.id.materialPayBottom);
        //promo code
        edPromoCode = findViewById(R.id.edPromoCode);
        txtApplyPromo = findViewById(R.id.txtApplyPromo);
        txtPromoTitle = findViewById(R.id.txtPromoTitle);
        txtPromoTitle.setVisibility(View.GONE);
        imgClosePromo = findViewById(R.id.imgClosePromo);
        HideSoft(edPromoCode);

        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        dector = new ConnectionDector(context);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            pathologyName = (String) bundle.get("pathologyName");
            selectedTreatmentList = bundle.getParcelableArrayList("pathologyTreatmentList");
            TransactionForId = bundle.getString("pathid");
            totalPay = ((double) bundle.get("total"));
            AppointmentDate = bundle.getString("strdate");
            patientId = bundle.getString("patientId");
            patientName = bundle.getString("patientName");
            gender = bundle.getString("gender");
            pincode = bundle.getString("pincode");
            address = bundle.getString("address");
            email = bundle.getString("email");
            age = bundle.getString("age");

        }

        if (dector.isConnectingToInternet()) {
            getCalculation("pathology", totalPay);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }


        materialPayBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amounttoPay != 0) {
                    Intent intent = new Intent(context, PaytmPaymentGatewayActivity.class);
                    intent.putExtra(PaytmPaymentGatewayActivity.AMOUNT_KEY, amounttoPay);
                    startActivityForResult(intent, 1000);
                    //generateUniqueKey();
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        mdformat = new SimpleDateFormat("hh:mm:ss a");
        bookedTimeString = mdformat.format(calendar.getTime());
        Log.e("time", bookedTimeString);


        txtApplyPromo.setOnClickListener(v -> {
            getCalculation("pathology", totalPay);
        });


        imgClosePromo.setOnClickListener(v -> {
            edPromoCode.setText("");
            getCalculation("hospital", totalPay);
        });


        edPromoCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String s1 = s.toString();
                if (s1.length() == 0) {
                    txtApplyPromo.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtApplyPromo.setOnClickListener(v -> {
            //apply promo webservices
            promo_code_Str = edPromoCode.getText().toString();
            if (!promo_code_Str.isEmpty()) {
                getPromoCode(promo_code_Str, amounttoPay);
            } else {
                showSnackBar("Please Enter the Promo code.");
            }
        });
    }

    Gson gson = new Gson();

    com.whr.user.booking.diagnostics.booking.model.PathologyPayment PathologyPayment;

    private void getCalculation(String payment, double totalPay) {
        String url = GlobalVar.ServerAddress + "AndroidNew/PaymentCalculations";
        JSONObject obj = new JSONObject();
        try {
            obj.put("totalamount", totalPay);
            obj.put("type", payment);
            obj.put("uid", pref.getUID());
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.showProgressDialog(context, "", true);

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    Log.e(TAG + "getCalculation", response.toString());
                    GlobalVar.hideProgressDialog();
                    try {
                        JSONObject jsonObjectPathologyPayment = response.getJSONObject("PathologyPayment");
                        if (jsonObjectPathologyPayment != null && jsonObjectPathologyPayment.length() > 0) {
                            PathologyPayment = gson.fromJson(jsonObjectPathologyPayment.toString(), PathologyPayment.class);
                            txtOnlinePrice.setText(String.valueOf(PathologyPayment.getOnlinePayment().getTotalAmount()));

                            amounttoPay = PathologyPayment.getOnlinePayment().getTotalAmount();
                            txtPay.setText(String.valueOf(amounttoPay));

                            /*chkRedeem.setOnCheckedChangeListener((buttonView, isChecked) -> {

                                if (radioPayOnline.isChecked()) {
                                    if (isChecked) {
                                        txtOnlinePrice.setText(String.valueOf(PathologyPayment.getOnlinePayment().getWalletTotal()));
                                        if (checkBox.isChecked()) {
                                            double value = PathologyPayment.getOnlinePayment().getWalletTotal();
                                            double value1 = 50;
                                            String payAMount = String.valueOf(value + value1);

                                            amounttoPay = Double.parseDouble(payAMount);
                                            txtPay.setText(String.valueOf(amounttoPay));

                                        } else {
                                            amounttoPay = Double.parseDouble((String.valueOf(PathologyPayment.getOnlinePayment().getWalletTotal())));
                                            txtPay.setText(String.valueOf(amounttoPay));

                                        }

                                    } else {
                                        if (checkBox.isChecked()) {
                                            double value = PathologyPayment.getOnlinePayment().getTotalAmount();
                                            double value1 = 50;
                                            String payAMount = String.valueOf(value + value1);
                                            amounttoPay = Double.parseDouble(payAMount);
                                            txtPay.setText(String.valueOf(amounttoPay));


                                            txtOnlinePrice.setText(String.valueOf(PathologyPayment.getOnlinePayment().getTotalAmount()));
                                        } else {
                                            txtOnlinePrice.setText(String.valueOf(PathologyPayment.getOnlinePayment().getTotalAmount()));
                                            txtPay.setText(String.valueOf(PathologyPayment.getOnlinePayment().getTotalAmount()));
                                            amounttoPay = Double.parseDouble((String.valueOf(PathologyPayment.getOnlinePayment().getTotalAmount())));
                                            txtPay.setText(String.valueOf(amounttoPay));
                                        }
                                    }
                                } else {
                                    chkRedeem.setChecked(false);
                                }

                            });*/

                            /*radioPayOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    radioPayOnline.setChecked(isChecked);
                                    txtPay.setText(String.valueOf(PathologyPayment.getOnlinePayment().getTotalAmount()));
                                    layoutWalletPoint.setVisibility(View.VISIBLE);
                                }
                            });*/


                          /*  checkBox.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (checkBox.isChecked()) {
                                        double value = Double.parseDouble(txtOnlinePrice.getText().toString());
                                        double value1 = 50;
                                        String payAMount = String.valueOf(value + value1);
                                        txtPay.setText(payAMount);
                                    } else {
                                        txtPay.setText(String.valueOf(txtOnlinePrice.getText().toString()));
                                    }
                                }
                            });*/


                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        isReports = "Y";
                                        double value = Double.parseDouble(txtOnlinePrice.getText().toString());
                                        double value1 = 50;
                                        String payAMount = String.valueOf(value + value1);
                                        txtPay.setText(payAMount);
                                    } else {
                                        isReports = "N";
                                        txtPay.setText(String.valueOf(txtOnlinePrice.getText().toString()));
                                    }
                                }
                            });

                        }

                        /*JSONObject jsonObjectPathologyPayment = response.getJSONObject("PathologyPayment");
                        if (jsonObjectPathologyPayment != null && jsonObjectPathologyPayment.length() > 0) {
                            PathologyPayment PathologyPayment = gson.fromJson(jsonObjectPathologyPayment.toString()
                                    , PathologyPayment.class);
                            GlobalVar.errorLog(TAG, "PathologyPayment", PathologyPayment.toString());
                        }*/


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, (VolleyError error) -> {
            GlobalVar.hideProgressDialog();
            showSnackBar(VolleyErrorHelper.getMessage(error.toString(), context));
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);

    }

    private void getPromoCode(String promocode, double totalPay) {

        String url = GlobalVar.ServerAddress + "AndroidNew/ValidatePromoCode";
        JSONObject para = new JSONObject();
        try {
            para.put("promocode", promocode);
            para.put("userid", pref.getUID());
            para.put("total_pay", totalPay);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "para", para.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        GlobalVar.showProgressDialog(context, "Applying Promo Code.", false);
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, para,
                response -> {
                    GlobalVar.errorLog(TAG, "getPromoCode", response.toString());
                    HideSoft(coordinatorLayout);
                    GlobalVar.hideProgressDialog();
                    try {
                        JSONObject jsonObjectPromocodeResponce = response.getJSONObject("PromocodeResponce");
                        PromocodeResponce promocodeResponce = gson.fromJson(jsonObjectPromocodeResponce.toString()
                                , PromocodeResponce.class);
                        txtPromoTitle.setText(promocodeResponce.getMessage());
                        txtPromoTitle.setVisibility(View.VISIBLE);
                        if (promocodeResponce.isStatus()) {
                            txtPromoTitle.setTextColor(getResources().getColor(R.color.primary));
                            promo_code_Str = promocodeResponce.getPromocode();
                            offerDeduction = promocodeResponce.getCalculatedTotal();
                            amounttoPay = promocodeResponce.getCalculatedTotalPay();
                            txtPay.setText(GlobalVar.priceWithoutDecimal(amounttoPay));
                            txtApplyPromo.setVisibility(View.GONE);
                        } else {
                            txtPromoTitle.setTextColor(getResources().getColor(R.color.red));
                            offerDeduction = promocodeResponce.getCalculatedTotal();
                            promo_code_Str = "";
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, (VolleyError error) -> {
            GlobalVar.hideProgressDialog();
            showSnackBar(VolleyErrorHelper.getMessage(error.toString(), context));
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (!bundle.isEmpty()) {
                Bundle paymentBundle = data.getBundleExtra(PaytmPaymentGatewayActivity.PAYMENT_KEY);
                GlobalVar.errorLog(TAG, "paymentBundle", paymentBundle.toString());
                String tranid = "";
                String rspcode = "";
                String TXNAMOUNT = "";
                rspcode = paymentBundle.getString("RESPCODE");
                if (rspcode != null && !rspcode.isEmpty()) {
                    TXNAMOUNT = paymentBundle.getString("TXNAMOUNT");
                    tranid = paymentBundle.getString("TXNID");
                    //booking webservice starts here
                    //bookAppointment(tranid, TXNAMOUNT, rspcode);
                    bookAppoinmentThyrocare(tranid, TXNAMOUNT, rspcode);

                }
            } else {
                GlobalVar.errorLog(TAG, "Payment", "Canceled");
            }

        } else {
            GlobalVar.errorLog(TAG, "Payment", "NO");
        }

    }

    private void generateUniqueKey() {
/*        //-----------
        Random randomGenerator1 = new Random();
        int randomInt1 = randomGenerator1.nextInt(1000000000);
        String test2 = "[{STATUS=TXN_SUCCESS, CHECKSUMHASH=E5WTpaLWAefeG9WqqHuJJxeWgi0nvgTo4TqcAEaHcAzTIIbo0wSEhEb8AsnbBVC2g+lasFfC4FmT2zgavclXHsRZ1oEnnz3oIx06O4VGLk4=, BANKNAME=CANARA, ORDERID=2762, TXNAMOUNT=" + amounttoPay + ", TXNDATE=2018-04-02 16:42:55.0, MID=LivWor74836176531967, TXNID=" + randomInt1 + ", RESPCODE=01, PAYMENTMODE=DC, BANKTXNID=9885543431680920, CURRENCY=INR, GATEWAYNAME=HDFC, RESPMSG=Txn Success}]";
 Log.e(TAG, test2);
        //-----------*/

        GlobalVar.showProgressDialog(this, "Loading....", false);
        String url = "http://android.whrhealth.com/" + "user/GenerateUniquekey";
        //String url = GlobalVar.ServerAddress + "user/GenerateUniquekey";
        JSONObject obj = new JSONObject();
        try {
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1000000000);
            obj.put("orderid", randomInt);
            Log.e(TAG + " url", url);
            Log.e(TAG + " obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.hideProgressDialog();
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(1000000000);
        //bookAppointment(String.valueOf(randomInt), String.valueOf(amounttoPay), "01");
        bookAppoinmentThyrocare(String.valueOf(randomInt), String.valueOf(amounttoPay), "01");

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "generateUniqueKey", response.toString());
                    GlobalVar.hideProgressDialog();
                    try {
                        JSONObject jobj = response.getJSONObject("uniquekey");
                        String key = jobj.getString("uniquekey");
                        if (key != null && !key.isEmpty()) {
                            if (new ConnectionDector(context).isConnectingToInternet()) {
                                //booking webervice
                                // bookAppointment(key, String.valueOf(amounttoPay), "01");
                                bookAppoinmentThyrocare(key, String.valueOf(amounttoPay), "01");
                            } else {
                                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {

            GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        });
        jsonRequest.setTag("get");
        // mQueue.add(jsonRequest);
    }


    private void bookAppointment(String transactionId, String amountPaid, String transactionStatus) {
        GlobalVar.showProgressDialog(context, "", false);
        String url = GlobalVar.ServerAddress + "AndroidNew/BookAppointmentV1";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("TransactionId", transactionId);
            if (transactionStatus.equalsIgnoreCase("01")) {
                jsonObject.put("TransactionStatus", "1");
            } else {
                jsonObject.put("TransactionStatus", "0");
            }
            jsonObject.put("UserId", pref.getUID());

            jsonObject.put("PatientId", patientId);
            jsonObject.put("TransactionForId", TransactionForId);
            jsonObject.put("HidOfDoctor", "0");
            jsonObject.put("TypeStatus", GlobalVar.PAY_STATUS_PATHOLOGY);
            jsonObject.put("Trans_String", "");
            //jsonObject.put("AppointmentDate", AppointmentDate);
            jsonObject.put("BookingDate", AppointmentDate + " " + bookedTimeString);
            jsonObject.put("BookingFrom", "ANDROID");

            JSONObject jsonObjectPaymentDetails = new JSONObject();
            jsonObjectPaymentDetails.put("TotalFees", totalPay);

            jsonObjectPaymentDetails.put("PaymentType", "Online");
            if (chkRedeem.isChecked()) {
                double wallet_deduction_value = PathologyPayment.getOnlinePayment().getTotalAmount() - PathologyPayment.getOnlinePayment().getWalletTotal();
                jsonObjectPaymentDetails.put("OnlinePaid", PathologyPayment.getOnlinePayment().getWalletTotal());
                jsonObjectPaymentDetails.put("WalletDiduction", wallet_deduction_value);
            } else {
                jsonObjectPaymentDetails.put("OnlinePaid", PathologyPayment.getOnlinePayment().getTotalAmount());
                jsonObjectPaymentDetails.put("WalletDiduction", "0");
            }

            jsonObjectPaymentDetails.put("PromoCode", promo_code_Str);
            jsonObjectPaymentDetails.put("OfferDiduction", offerDeduction);
            jsonObjectPaymentDetails.put("OfflinePaid", "0");
            jsonObject.put("PaymentDetails", jsonObjectPaymentDetails);

            JSONArray jsonArrayTreatment = new JSONArray();
            for (ThyroCarePojo d : selectedTreatmentList) {
                JSONObject jsonObjectTreatment = new JSONObject();
                jsonObjectTreatment.put("TestOrTreatmentOrPackageId", d.getName());
                jsonObjectTreatment.put("TestOrTreatmentOrPackageName", d.getName());
                jsonObjectTreatment.put("DicountPrice", d.getPay_amt());
                jsonObjectTreatment.put("OriginalPrice", d.getB2c());
                jsonArrayTreatment.put(jsonObjectTreatment);
            }

            jsonObject.put("TestOrTreatmentDetails", jsonArrayTreatment);

            GlobalVar.errorLog(TAG, "jsonObject", jsonObject.toString());
            GlobalVar.errorLog(TAG, "url", url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "bookAppointment", response.toString());
                    try {
                        String result = response.getString("result");
                        if (!result.equalsIgnoreCase("0")) {
                            Intent i = new Intent(context, ReceiptActivity.class);
                            i.putExtra(ReceiptActivity.TRANSACTION_ID_KEY, result);
                            i.putExtra(ReceiptActivity.BOOKING_KEY, ReceiptActivity.BOOKING_KEY);
                            startActivity(i);
                        } else {
                            GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.EnableToBookAppointmentPleaseryAgain), context, R.color.green);
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

    private void bookAppoinmentThyrocare(String transactionId, String amountPaid, String transactionStatus) {

        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject obj = new JSONObject();
        //String url = "https://www.thyrocare.com/API_BETA/ORDER.svc/Postorderdata";
        String url = "https://www.thyrocare.com/APIS/ORDER.svc/Postorderdata";
        try {

            String tid = "";
            for (int i = 0; i < selectedTreatmentList.size(); i++) {

                GlobalVar.errorLog(TAG, "doctorTreatmentList", selectedTreatmentList.get(i).toString());

                if (tid.isEmpty()) {
                    tid = String.valueOf(selectedTreatmentList.get(i).getTestName());
                } else {
                    tid = tid + "," + String.valueOf(selectedTreatmentList.get(i).getTestName());
                }

            }

            String bendataxml = "<NewDataSet><Ben_details><Name>" + patientName + "</Name>" + "<Age>" + age + "</Age>" + "<Gender>" + gender + "</Gender>" + "</Ben_details></NewDataSet>";

            try {
                obj.put("api_key", "2Xcj7vKYZqD1FUOlOaNlogerOvCjCtfundHTSG4LUDeUGZeZv7spTUDAQP@Qipxn");
                obj.put("orderid", transactionId);
                obj.put("address", address);
                obj.put("pincode", pincode);
                obj.put("product", tid);
                obj.put("mobile", patientId);
                obj.put("email", email);
                obj.put("service_type", "H");
                obj.put("order_by", patientName);
                obj.put("rate", amounttoPay);
                obj.put("hc", 100);
                Date initDate = new SimpleDateFormat("dd-MM-yyyy").parse(AppointmentDate);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String parsedDate = formatter.format(initDate) + " " + bookedTimeString;
                obj.put("appt_date", parsedDate);
                obj.put("reports", isReports);
                obj.put("ref_code", "9607960010");
                obj.put("pay_type", "POSTPAID");
                obj.put("bencount", "1");
                obj.put("bendataxml", bendataxml);
                Log.e(TAG + " url ", url);
                Log.e(TAG + " obj ", obj.toString());


            } catch (Exception ex) {
                GlobalVar.hideProgressDialog();
                ex.printStackTrace();
            }
        } catch (Exception e) {
            GlobalVar.hideProgressDialog();
            e.printStackTrace();
        }
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG + " : bookingResponce:=> ", response.toString());
                        GlobalVar.hideProgressDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            boolean result;
                            if (!jsonObject.getString("STATUS").equalsIgnoreCase("null")) {
                                result = true;
                            } else {
                                result = false;
                            }

                            if (result) {
                                bookAppointment(transactionId, amountPaid, transactionStatus);


                                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.AppointmentSuccessfullyBooked), context, R.color.green);

                            } else {
                                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.EnableToBookAppointmentPleaseryAgain), context, R.color.red);

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

}