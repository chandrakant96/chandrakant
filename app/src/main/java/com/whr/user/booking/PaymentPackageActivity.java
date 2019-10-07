package com.whr.user.booking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.activities.PaytmPaymentGatewayActivity;
import com.whr.user.activities.ReceiptActivity;
import com.whr.user.booking.doctor.booking.BookingDetailsPackageActivity;
import com.whr.user.booking.doctor.booking.DoctorAppointmentBookingActivity;
import com.whr.user.booking.doctor.booking.HospitalPackageDetailsActivity;
import com.whr.user.booking.doctor.booking.models.PackagesDetails;
import com.whr.user.booking.models.HospitalPayment;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.model.PromocodeResponce;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PaymentPackageActivity extends AppCompatActivity {
    private Context context;
    TextView txtAll;
    String strAppointmentDate = "";
    PackagesDetails packagesDetails;
    FamilyMemberListpojo familyMember;
    RadioButton radioPayOnline;
    TextView txtHospital, txtHospitalPrice;
    double totalPay = 0;
    String TAG = getClass().getSimpleName();
    CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    LinearLayout layoutWalletPoint;
    TextView txtPay;
    PreferenceUtils pref;
    private double amounttoPay = 0;
    ConnectionDector dector;
    MaterialRippleLayout materialPayBottom;


    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    String PaymentType = "offline";
    String hid = "";
    String did = "";
    TextView txtContent;
    String bookingDate = "";

    //promo code
    EditText edPromoCode;
    TextView txtApplyPromo, txtPromoTitle;

    ImageView imgClosePromo;

    String promo_code_Str = "";

    double offerDeduction = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment_package);
        txtAll = findViewById(R.id.txtAll);
        context = this;
        pref = new PreferenceUtils(context);
        layoutWalletPoint = findViewById(R.id.layoutWalletPoint);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        radioPayOnline = findViewById(R.id.radioPayOnline);
        txtHospital = findViewById(R.id.txtHospital);
        txtHospitalPrice = findViewById(R.id.txtHospitalPrice);
        txtPay = findViewById(R.id.txtPay);
        txtContent = findViewById(R.id.txtContent);
        materialPayBottom = findViewById(R.id.materialPayBottom);
        txtContent.setText("WHR would be taking atleast 20% of \n the surgery package amount online right now \n for confirming the appointment");
        overridePendingTransitionEnter();
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        //promo code
        edPromoCode = findViewById(R.id.edPromoCode);
        txtApplyPromo = findViewById(R.id.txtApplyPromo);
        txtPromoTitle = findViewById(R.id.txtPromoTitle);
        imgClosePromo = findViewById(R.id.imgClosePromo);

        HideSoft(edPromoCode);

        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        dector = new ConnectionDector(context);
        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean isAppointmentDate = bundle.containsKey(DoctorAppointmentBookingActivity.APPOINTMENT_DATE_KEY);
            boolean isFamily = bundle.containsKey(BookingDetailsPackageActivity.APPOINTMENT_FAMILY_KEY);

            if (isFamily) {
                familyMember = bundle.getParcelable(BookingDetailsPackageActivity.APPOINTMENT_FAMILY_KEY);
                GlobalVar.errorLog(TAG, "familyMember", familyMember.toString());
            }

            if (isAppointmentDate) {
                strAppointmentDate = bundle.getString(DoctorAppointmentBookingActivity.APPOINTMENT_DATE_KEY);

                SimpleDateFormat formatter4 = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formatter5 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                bookingDate = strAppointmentDate;
                try {
                    Date datTime = formatter4.parse(bookingDate);
                    bookingDate = formatter5.format(datTime);
                    GlobalVar.errorLog(TAG, "datTime", bookingDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                GlobalVar.errorLog(TAG, "strAppointmentDate", strAppointmentDate);
            }


            if (intentExtra.getExtras().containsKey(HospitalPackageDetailsActivity.HOSPITAL_PACKAGE_DETAILS_KEY)) {
                packagesDetails = intentExtra.getParcelableExtra(HospitalPackageDetailsActivity.HOSPITAL_PACKAGE_DETAILS_KEY);
                did = packagesDetails.getHid();
                totalPay = Double.parseDouble(packagesDetails.getDiscountprice());

                if (dector.isConnectingToInternet()) {
                    getCalculation("hospital", totalPay);
                } else {
                    startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
                    overridePendingTransitionExit();
                }

            }
        }

        materialPayBottom.setOnClickListener(v -> {
            if (amounttoPay != 0) {
                Intent intent = new Intent(context, PaytmPaymentGatewayActivity.class);
                intent.putExtra(PaytmPaymentGatewayActivity.AMOUNT_KEY, amounttoPay);
                startActivityForResult(intent, 1000);
                //book appointment webservices
                // generateUniqueKey();
            }
        });

        txtApplyPromo.setOnClickListener(v -> {
            getCalculation("hospital", totalPay);
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

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    Gson gson = new Gson();

    HospitalPayment hospitalPayment;

    private void getCalculation(String payment, double totalPay) {
        HideSoft(edPromoCode);
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

                    GlobalVar.errorLog(TAG, "getCalculation", response.toString());
                    GlobalVar.hideProgressDialog();
                    try {
                        JSONObject jsonObjectHospitalPayment = response.getJSONObject("HospitalPayment");
                        JSONObject jsonObjectAdvancePayment = jsonObjectHospitalPayment.getJSONObject("AdvanceAmt");
                        if (jsonObjectAdvancePayment != null && jsonObjectAdvancePayment.length() > 0) {
                            hospitalPayment = gson.fromJson(jsonObjectAdvancePayment.toString()
                                    , HospitalPayment.class);
                            GlobalVar.errorLog(TAG, "PackagePayment", hospitalPayment.toString());
                            edPromoCode.setText("");
                            txtPromoTitle.setText("");
                            txtPromoTitle.setVisibility(View.GONE);
                            offerDeduction = 0;
                            txtApplyPromo.setVisibility(View.VISIBLE);
                            amounttoPay = hospitalPayment.getPayOnlineAmt();
                            txtHospital.setText("Rs. " + GlobalVar.priceWithoutDecimal(Double.parseDouble(String.valueOf(hospitalPayment.getPayOnlineAmt()))));
                            txtHospitalPrice.setText("20% of " + GlobalVar.priceWithoutDecimal(Double.parseDouble(String.valueOf(totalPay))));
                            txtContent.setText(hospitalPayment.getContent());
                            txtPay.setText(GlobalVar.priceWithoutDecimal(Double.parseDouble(String.valueOf(amounttoPay))));
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

    private void showSnackBarGreen(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.green);
    }

    private void generateUniqueKey() {
        GlobalVar.showProgressDialog(this, "Loading....", false);
        String url = GlobalVar.ServerAddress + "user/GenerateUniquekey";
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

            jsonObject.put("PatientId", familyMember.getFamilyId());
            jsonObject.put("TransactionForId", did);
            jsonObject.put("HidOfDoctor", hid);
            jsonObject.put("TypeStatus", GlobalVar.PAY_STATUS_HOSPITAL_PACKAGES);
            jsonObject.put("Trans_String", "");
            jsonObject.put("BookingDate", bookingDate);
            jsonObject.put("BookingFrom", "ANDROID");

            JSONObject jsonObjectPaymentDetails = new JSONObject();
            jsonObjectPaymentDetails.put("TotalFees", totalPay);
            jsonObjectPaymentDetails.put("PaymentType", PaymentType);
            jsonObjectPaymentDetails.put("OnlinePaid", hospitalPayment.getPayOnlineAmt());
            jsonObjectPaymentDetails.put("OfflinePaid", hospitalPayment.getPayAtHospitalAmt());
            jsonObjectPaymentDetails.put("WalletDiduction", "0");
            jsonObjectPaymentDetails.put("OfferDiduction", "0");
            jsonObjectPaymentDetails.put("PromoCode", promo_code_Str);
            jsonObjectPaymentDetails.put("OfferDiduction", offerDeduction);

            jsonObject.put("PaymentDetails", jsonObjectPaymentDetails);

            JSONArray jsonArrayTreatment = new JSONArray();
            JSONObject jsonObjectTreatment = new JSONObject();
            jsonObjectTreatment.put("TestOrTreatmentOrPackageId", packagesDetails.getTid());
            jsonObjectTreatment.put("TestOrTreatmentOrPackageName", packagesDetails.getPackageName());
            jsonObjectTreatment.put("DicountPrice", packagesDetails.getDiscountprice());
            jsonObjectTreatment.put("OriginalPrice", packagesDetails.getTotaltreatmentfee());
            jsonArrayTreatment.put(jsonObjectTreatment);
            jsonObject.put("TestOrTreatmentDetails", jsonArrayTreatment);

            GlobalVar.errorLog(TAG, "jsonObject", jsonObject.toString());
            GlobalVar.errorLog(TAG, "url", url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, jsonObject, response -> {
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
}