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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.activities.LocationFindActivity;
import com.whr.user.booking.diagnostics.booking.model.PathologyListPojo;
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
import com.whr.user.model.PromocodeResponce;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.booking.diagnostics.booking.model.Suggested_TreatementPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PaymentPathologyActivity extends AppCompatActivity {
    private Context context;
    TextView txtAll;
    List<Suggested_TreatementPojo> selectedTreatmentList = new ArrayList<>();

    FamilyMemberListpojo familyMember;
    RadioButton radioPayOnline, radioButtonPayAtHospital;
    CheckBox chkRedeem;
    TextView txtOnlinePrice, txtHospital, txtHospitalPrice;
    TextView txtPayAtOnlinetwentyPercent, txtPayAtPathology;

    String TAG = getClass().getSimpleName();
    CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    LinearLayout layoutWalletPoint;
    TextView txtPay;
    PreferenceUtils pref;
    MaterialRippleLayout materialPayBottom;
    private double amounttoPay;
    ConnectionDector dector;
    LinearLayout layoutPayAtDiagnostics;

    private String pathologyName = "", TransactionId = " ", TransactionStatus = "", patientId = "", TransactionForId = "", HidOfDoctor = "",
            Trans_String = "", AppointmentDate = "",AppoinmentTme="", BookingFrom = "ANDROID";
    double totalPay = 0;
    private String WalletDiduction = "", OfferDiduction = "", OnlinePaid = "", OfflinePaid = "", PaymentType = "";
    private String TestOrTreatmentOrPackageId = "", TestOrTreatmentOrPackageName = "", DicountPrice = "", OriginalPrice = "";
    public static String ONLINE_PAY = "online";
    public static String PAY_AT_HOSPITAL = "offline";

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    //promo code
    EditText edPromoCode;
    TextView txtApplyPromo, txtPromoTitle;
    ImageView imgClosePromo;
    String promo_code_Str = "";
    double offerDeduction = 0;
    String pathAddressId = "";
    String collectionType = "";
    String age = "";
    String gender = "";
    String collectionAddress = "";


    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pathology_payment);
        txtAll = findViewById(R.id.txtAll);
        context = this;
        pref = new PreferenceUtils(context);
        dector = new ConnectionDector(context);
        layoutWalletPoint = findViewById(R.id.layoutWalletPoint);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        radioPayOnline = findViewById(R.id.radioPayOnline);
        radioButtonPayAtHospital = findViewById(R.id.radioButtonPayAtHospital);

        chkRedeem = findViewById(R.id.chkRedeem);
        txtOnlinePrice = findViewById(R.id.txtOnlinePrice);
        txtHospital = findViewById(R.id.txtHospital);
        txtHospitalPrice = findViewById(R.id.txtHospitalPrice);
        txtPayAtOnlinetwentyPercent = findViewById(R.id.txtPayAtOnlinetwentyPercent);
        txtPayAtPathology = findViewById(R.id.txtPayAtPathology);
        txtPay = findViewById(R.id.txtPay);
        materialPayBottom = findViewById(R.id.materialPayBottom);
        layoutPayAtDiagnostics = findViewById(R.id.layoutPayAtDiagnostics);


        overridePendingTransitionEnter();
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        //promo code
        edPromoCode = findViewById(R.id.edPromoCode);
        txtApplyPromo = findViewById(R.id.txtApplyPromo);
        txtPromoTitle = findViewById(R.id.txtPromoTitle);
        txtPromoTitle.setVisibility(View.GONE);
        imgClosePromo = findViewById(R.id.imgClosePromo);
        HideSoft(edPromoCode);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            pathologyName = (String) bundle.get("pathologyName");
            selectedTreatmentList = bundle.getParcelableArrayList("pathologyTreatmentList");
            TransactionForId = bundle.getString("pathid");
            totalPay = ((double) bundle.get("total"));
            AppointmentDate = bundle.getString("strdate");
            AppoinmentTme = bundle.getString("strTime");
            patientId = bundle.getString("patientId");
            pathAddressId = (String) bundle.get("pathAddressId");
            collectionType = (String) bundle.get("collectionType");
            age =  bundle.getString("age");
            gender =  bundle.getString("gender");
            collectionAddress =  bundle.getString("collectionAddress");
            HidOfDoctor = pathAddressId;
            Log.e("pathAddressId", pathAddressId);
        }

        if (dector.isConnectingToInternet()) {
            getCalculation("pathology", totalPay);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }


        materialPayBottom.setOnClickListener(v -> {
            if (amounttoPay != 0) {
                if (radioButtonPayAtHospital.isChecked() && radioButtonPayAtHospital.getText().equals("Pay At Cash")) {
                    Intent intent1 = new Intent(context, PaytmPaymentGatewayActivity.class);
                    intent1.putExtra(PaytmPaymentGatewayActivity.AMOUNT_KEY, amounttoPay);
                    //startActivityForResult(intent1, 1000);
                    generateUniqueKey();
                } else {
                    Intent intent1 = new Intent(context, PaytmPaymentGatewayActivity.class);
                    intent1.putExtra(PaytmPaymentGatewayActivity.AMOUNT_KEY, amounttoPay);
                    startActivityForResult(intent1, 1000);
                    //generateUniqueKey();
                }

            }
        });

        txtApplyPromo.setOnClickListener(v -> {
            getCalculation("pathology", totalPay);
        });


        imgClosePromo.setOnClickListener(v -> {
            edPromoCode.setText("");
            getCalculation("pathology", totalPay);
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
                    HideSoft(coordinatorLayout);
                    Log.e(TAG + "getCalculation", response.toString());
                    GlobalVar.hideProgressDialog();
                    try {
                        JSONObject jsonObjectPathologyPayment = response.getJSONObject("PathologyPayment");
                        if (jsonObjectPathologyPayment != null && jsonObjectPathologyPayment.length() > 0) {
                            PathologyPayment = gson.fromJson(jsonObjectPathologyPayment.toString(), PathologyPayment.class);
                            txtOnlinePrice.setText("Rs. " + GlobalVar.priceWithoutDecimal(PathologyPayment.getOnlinePayment().getTotalAmount()));
                            if (TransactionForId.equals("50189")) {
                                txtHospital.setText("Rs. " + GlobalVar.priceWithoutDecimal(totalPay));
                                txtHospitalPrice.setText("");

                            } else {
                                txtHospital.setText("Rs. " + GlobalVar.priceWithoutDecimal(PathologyPayment.getPayAtPathology().getPayOnlineAmt()));
                                txtHospitalPrice.setText("20% of Rs. " + GlobalVar.priceWithoutDecimal(PathologyPayment.getOnlinePayment().getTotalAmount()));
                            }

                            amounttoPay = PathologyPayment.getOnlinePayment().getTotalAmount();
                            txtPay.setText(GlobalVar.priceWithoutDecimal(amounttoPay));

                            txtPayAtOnlinetwentyPercent.setText("Rs. " + GlobalVar.priceWithoutDecimal(PathologyPayment.getPayAtPathology().getPayOnlineAmt()));
                            txtPayAtPathology.setText("Rs. " + GlobalVar.priceWithoutDecimal(PathologyPayment.getPayAtPathology().getPayAtPathlogyAmt()));
                            PaymentType = ONLINE_PAY;

                            edPromoCode.setText("");
                            txtPromoTitle.setText("");
                            txtPromoTitle.setVisibility(View.GONE);
                            offerDeduction = 0;
                            txtApplyPromo.setVisibility(View.VISIBLE);

                            chkRedeem.setOnCheckedChangeListener((buttonView, isChecked) -> {

                                if (radioPayOnline.isChecked()) {
                                    if (isChecked) {
                                        txtOnlinePrice.setText("Rs. " + GlobalVar.priceWithoutDecimal(PathologyPayment.getOnlinePayment().getWalletTotal()));
                                        amounttoPay = PathologyPayment.getOnlinePayment().getWalletTotal();
                                        WalletDiduction = String.valueOf(PathologyPayment.getOnlinePayment().getTotalAmount() - PathologyPayment.getOnlinePayment().getWalletTotal());
                                        txtPay.setText(GlobalVar.priceWithoutDecimal(amounttoPay));
                                    } else {
                                        txtOnlinePrice.setText("Rs. " + GlobalVar.priceWithoutDecimal(PathologyPayment.getOnlinePayment().getTotalAmount()));
                                        amounttoPay = PathologyPayment.getOnlinePayment().getTotalAmount();
                                        WalletDiduction = "";
                                        txtPay.setText(GlobalVar.priceWithoutDecimal(amounttoPay));
                                    }
                                }

                            });

                            radioPayOnline.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                radioPayOnline.setChecked(isChecked);
                                radioButtonPayAtHospital.setChecked(!isChecked);
                                amounttoPay = PathologyPayment.getOnlinePayment().getTotalAmount();
                                txtPay.setText(GlobalVar.priceWithoutDecimal(amounttoPay));
                                layoutWalletPoint.setVisibility(View.VISIBLE);
                                PaymentType = ONLINE_PAY;
                                edPromoCode.setText("");
                                txtPromoTitle.setText("");
                                txtPromoTitle.setVisibility(View.GONE);
                                offerDeduction = 0;
                                txtApplyPromo.setVisibility(View.VISIBLE);

                            });

                            radioButtonPayAtHospital.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                chkRedeem.setChecked(false);
                                radioButtonPayAtHospital.setChecked(isChecked);
                                radioPayOnline.setChecked(!isChecked);
                                amounttoPay = PathologyPayment.getPayAtPathology().getPayOnlineAmt();
                                if (TransactionForId.equals("50189")) {
                                    txtPay.setText("At CAsh");
                                } else {
                                    txtPay.setText(GlobalVar.priceWithoutDecimal(amounttoPay));
                                }
                                layoutWalletPoint.setVisibility(View.GONE);
                                PaymentType = PAY_AT_HOSPITAL;
                                edPromoCode.setText("");
                                txtPromoTitle.setText("");
                                txtPromoTitle.setVisibility(View.GONE);
                                offerDeduction = 0;
                                txtApplyPromo.setVisibility(View.VISIBLE);

                            });

                            if (TransactionForId.equals("50189")) {
                                radioButtonPayAtHospital.setText("Pay At Cash");
                                layoutPayAtDiagnostics.setVisibility(View.GONE);
                            }


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
                    bookAppointment(tranid, TXNAMOUNT, rspcode);
                }
            } else {
                GlobalVar.errorLog(TAG, "Payment", "Canceled");
            }

        } else {
            GlobalVar.errorLog(TAG, "Payment", "NO");
        }

    }

    private void generateUniqueKey() {
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
        bookAppointment(String.valueOf(randomInt), String.valueOf(amounttoPay), "01");

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
                                bookAppointment(key, String.valueOf(amounttoPay), "01");
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
            jsonObject.put("HidOfDoctor", HidOfDoctor);
            jsonObject.put("TypeStatus", GlobalVar.PAY_STATUS_PATHOLOGY);
            jsonObject.put("Trans_String", Trans_String);
            if (TransactionForId.equals("50189")) {
                jsonObject.put("BookingDate", AppointmentDate+" "+AppoinmentTme);
            } else {
                jsonObject.put("BookingDate", AppointmentDate + " 12:00:00 PM");
            }
            jsonObject.put("BookingFrom", BookingFrom);

            jsonObject.put("HomeCollectionflag", collectionType);
            jsonObject.put("Address", LocationFindActivity.fullAddress);
            jsonObject.put("Pincode", LocationFindActivity.postalCode);
            jsonObject.put("latitude", LocationFindActivity.lat);
            jsonObject.put("longitude", LocationFindActivity.lon);
            jsonObject.put("pathologytype", PathalogyServicesAndChargesActivity.typeid);

            JSONObject jsonObjectPaymentDetails = new JSONObject();
            jsonObjectPaymentDetails.put("TotalFees", totalPay);

            jsonObjectPaymentDetails.put("PaymentType", PaymentType);
            if (PaymentType.equals(ONLINE_PAY)) {
                if (chkRedeem.isChecked()) {
                    double wallet_deduction_value = PathologyPayment.getOnlinePayment().getTotalAmount() - PathologyPayment.getOnlinePayment().getWalletTotal();
                    jsonObjectPaymentDetails.put("OnlinePaid", PathologyPayment.getOnlinePayment().getWalletTotal());
                    jsonObjectPaymentDetails.put("WalletDiduction", wallet_deduction_value);
                } else {
                    jsonObjectPaymentDetails.put("OnlinePaid", PathologyPayment.getOnlinePayment().getTotalAmount());
                    jsonObjectPaymentDetails.put("WalletDiduction", "0");
                }

                jsonObjectPaymentDetails.put("OfflinePaid", "0");
            } else if (PaymentType.equals(PAY_AT_HOSPITAL)) {
                jsonObjectPaymentDetails.put("WalletDiduction", "0");
                jsonObjectPaymentDetails.put("OnlinePaid", PathologyPayment.getPayAtPathology().getPayOnlineAmt());
                jsonObjectPaymentDetails.put("OfflinePaid", PathologyPayment.getPayAtPathology().getPayAtPathlogyAmt());
            }

            jsonObjectPaymentDetails.put("PromoCode", promo_code_Str);
            jsonObjectPaymentDetails.put("OfferDiduction", offerDeduction);

            jsonObject.put("PaymentDetails", jsonObjectPaymentDetails);

            JSONArray jsonArrayTreatment = new JSONArray();
            for (Suggested_TreatementPojo d : selectedTreatmentList) {
                JSONObject jsonObjectTreatment = new JSONObject();
                jsonObjectTreatment.put("TestOrTreatmentOrPackageId", d.getRowid());
                jsonObjectTreatment.put("TestOrTreatmentOrPackageName", d.getTname());
                jsonObjectTreatment.put("DicountPrice", d.getWhrdiscounted_price());
                jsonObjectTreatment.put("OriginalPrice", d.getFee());
                jsonArrayTreatment.put(jsonObjectTreatment);
            }

            jsonObject.put("TestOrTreatmentDetails", jsonArrayTreatment);
            jsonObject.put("gender", gender);
            jsonObject.put("age", age);
            jsonObject.put("CollectionAddress", collectionAddress);

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

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    private void showSnackBarGreen(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.green);
    }


}