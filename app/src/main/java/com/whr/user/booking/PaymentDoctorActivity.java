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
import android.view.WindowManager;
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
import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.activities.PaytmPaymentGatewayActivity;
import com.whr.user.activities.ReceiptActivity;
import com.whr.user.booking.doctor.booking.BookingDetailsActivity;
import com.whr.user.booking.doctor.booking.DoctorAppointmentBookingActivity;
import com.whr.user.booking.doctor.booking.HospitalPackageDetailsActivity;
import com.whr.user.booking.doctor.booking.adapters.HospitalPackagesHorizontalAdapter;
import com.whr.user.booking.doctor.booking.models.HospitalProfile;
import com.whr.user.booking.doctor.booking.models.PackagesDetails;
import com.whr.user.booking.models.DoctorPayment;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.model.PromocodeResponce;
import com.whr.user.pojo.DoctorTreatmentPojo;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PaymentDoctorActivity extends AppCompatActivity {
    private Context context;
    TextView txtAll;
    String strAppointmentDate = "", strAppointmentTime = "";
    List<DoctorTreatmentPojo> selectedTreatmentList = new ArrayList<>();
    HospitalProfile.HospitalPackages hospitalPackages;
    PackagesDetails packagesDetails;
    FamilyMemberListpojo familyMember;
    RadioButton radioPayOnline, radioButtonPayAtHospital;
    CheckBox chkRedeem;
    TextView txtOnlinePrice, txtHospital, txtHospitalPrice;
    double totalPay = 0;
    String TAG = getClass().getSimpleName();
    CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    LinearLayout layoutWalletPoint;
    TextView txtPay;
    PreferenceUtils pref;
    private double amounttoPay = 0;

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    String PaymentType = "";
    public static String ONLINE_PAY = "online";
    public static String PAY_AT_HOSPITAL = "offline";
    //public static String PAY_AT_HOSPITAL = "pay_at_hospital";
    String hid = "";
    String did = "";

    SimpleDateFormat formatter4 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    SimpleDateFormat formatter5 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

    //promo code
    EditText edPromoCode;
    TextView txtApplyPromo, txtPromoTitle;
    ImageView imgClosePromo;
    String promo_code_Str = "";
    double offerDeduction = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        txtAll = findViewById(R.id.txtAll);
        context = this;
        pref = new PreferenceUtils(context);
        layoutWalletPoint = findViewById(R.id.layoutWalletPoint);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        radioPayOnline = findViewById(R.id.radioPayOnline);
        radioButtonPayAtHospital = findViewById(R.id.radioButtonPayAtHospital);
        chkRedeem = findViewById(R.id.chkRedeem);
        txtOnlinePrice = findViewById(R.id.txtOnlinePrice);
        txtHospital = findViewById(R.id.txtHospital);
        txtHospitalPrice = findViewById(R.id.txtHospitalPrice);
        txtPay = findViewById(R.id.txtPay);
        overridePendingTransitionEnter();
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        dector = new ConnectionDector(context);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        //promo code
        edPromoCode = findViewById(R.id.edPromoCode);
        txtApplyPromo = findViewById(R.id.txtApplyPromo);
        txtPromoTitle = findViewById(R.id.txtPromoTitle);
        txtPromoTitle.setVisibility(View.GONE);
        imgClosePromo = findViewById(R.id.imgClosePromo);
        HideSoft(edPromoCode);

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean isAppointmentDate = bundle.containsKey(DoctorAppointmentBookingActivity.APPOINTMENT_DATE_KEY);
            boolean isAppointmentTime = bundle.containsKey(DoctorAppointmentBookingActivity.APPOINTMENT_TIME_KEY);
            boolean isTreatmentList = bundle.containsKey(DoctorAppointmentBookingActivity.SELECTED_TREATMENT_LIST_KEY);
            boolean isFamily = bundle.containsKey(BookingDetailsActivity.APPOINTMENT_FAMILY_KEY);
            boolean isTotalPay = bundle.containsKey(DoctorAppointmentBookingActivity.TOTAL_PAY_KEY);
            boolean isHid = bundle.containsKey(DoctorAppointmentBookingActivity.HID_KEY);
            boolean isDid = bundle.containsKey(DoctorAppointmentBookingActivity.DID_KEY);
            if (isTotalPay) {
                totalPay = bundle.getDouble(DoctorAppointmentBookingActivity.TOTAL_PAY_KEY, 0);
                Log.e(TAG, "total :" + totalPay);
            }

            if (isFamily) {
                familyMember = bundle.getParcelable(BookingDetailsActivity.APPOINTMENT_FAMILY_KEY);
                GlobalVar.errorLog(TAG, "familyMemberListpojoUpdate", familyMember.toString1());
            }

            if (isAppointmentDate) {
                strAppointmentDate = bundle.getString(DoctorAppointmentBookingActivity.APPOINTMENT_DATE_KEY);
            }

            if (isAppointmentTime) {
                strAppointmentTime = bundle.getString(DoctorAppointmentBookingActivity.APPOINTMENT_TIME_KEY);
                bookingDate = strAppointmentDate + " " + strAppointmentTime;
                try {
                    Date datTime = formatter4.parse(bookingDate);
                    bookingDate = formatter5.format(datTime);
                    GlobalVar.errorLog(TAG, "datTime", bookingDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (isTreatmentList) {
                selectedTreatmentList.addAll(bundle.getParcelableArrayList(DoctorAppointmentBookingActivity.SELECTED_TREATMENT_LIST_KEY));
            }

            if (isHid) {
                hid = bundle.getString(DoctorAppointmentBookingActivity.HID_KEY);
            }


            if (isDid) {
                did = bundle.getString(DoctorAppointmentBookingActivity.DID_KEY);
            }

            getCalculation("doctor", totalPay);

            //-------- Surgery Packages --------

            if (intentExtra.getExtras().containsKey(HospitalPackagesHorizontalAdapter.HOSPITAL_PACKAGES_KEY)) {
                hospitalPackages = intentExtra.getParcelableExtra(HospitalPackagesHorizontalAdapter.HOSPITAL_PACKAGES_KEY);
            }

            if (intentExtra.getExtras().containsKey(HospitalPackageDetailsActivity.HOSPITAL_PACKAGE_DETAILS_KEY)) {
                packagesDetails = intentExtra.getParcelableExtra(HospitalPackageDetailsActivity.HOSPITAL_PACKAGE_DETAILS_KEY);
            }
        }

        txtPay.setOnClickListener(v -> {
            if (amounttoPay != 0) {
                Intent intent = new Intent(context, PaytmPaymentGatewayActivity.class);
                intent.putExtra(PaytmPaymentGatewayActivity.AMOUNT_KEY, amounttoPay);
               startActivityForResult(intent, 1000);
                //overridePendingTransitionEnter();
                //book appointment webservices
              //  generateUniqueKey();
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


        imgClosePromo.setOnClickListener(v -> {
            edPromoCode.setText("");
            getCalculation("doctor", totalPay);
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
                            txtPay.setText("Pay Rs. " + GlobalVar.priceWithoutDecimal(amounttoPay));
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

    private void getCalculation(String payment, double totalPay) {

        if (dector.isConnectingToInternet()) {
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
                            JSONObject jsonObjectDoctorPayment = response.getJSONObject("DoctorPayment");
                            if (jsonObjectDoctorPayment != null && jsonObjectDoctorPayment.length() > 0) {
                                doctorPayment = gson.fromJson(jsonObjectDoctorPayment.toString()
                                        , DoctorPayment.class);
                                GlobalVar.errorLog(TAG, "DoctorPayment", doctorPayment.toString());
                                txtOnlinePrice.setText("Rs. " + GlobalVar.priceWithoutDecimal(doctorPayment.getOnlinePayment().getTotalAmount()));
                                txtHospital.setText("Rs. " + GlobalVar.priceWithoutDecimal(doctorPayment.getPayAtHospital().getPayOnlineAmt()));
                                txtHospitalPrice.setText("20% of " + "Rs. " + GlobalVar.priceWithoutDecimal(doctorPayment.getOnlinePayment().getTotalAmount()));
                                amounttoPay = doctorPayment.getOnlinePayment().getTotalAmount();
                                txtPay.setText("Pay Rs. " + GlobalVar.priceWithoutDecimal(amounttoPay));
                                PaymentType = ONLINE_PAY;
                                chkRedeem.setChecked(false);
                                edPromoCode.setText("");
                                txtPromoTitle.setText("");
                                txtPromoTitle.setVisibility(View.GONE);
                                offerDeduction = 0;
                                txtApplyPromo.setVisibility(View.VISIBLE);

                                radioPayOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        radioPayOnline.setChecked(isChecked);
                                        radioButtonPayAtHospital.setChecked(!isChecked);
                                        amounttoPay = doctorPayment.getOnlinePayment().getTotalAmount();

                                        edPromoCode.setText("");
                                        txtPromoTitle.setText("");
                                        txtPromoTitle.setVisibility(View.GONE);
                                        offerDeduction = 0;
                                        txtApplyPromo.setVisibility(View.VISIBLE);

                                        txtPay.setText("Pay Rs. " + GlobalVar.priceWithoutDecimal(amounttoPay));
                                        layoutWalletPoint.setVisibility(View.VISIBLE);
                                        PaymentType = ONLINE_PAY;
                                    }
                                });

                                chkRedeem.setOnCheckedChangeListener((buttonView, isChecked) -> {

                                    edPromoCode.setText("");
                                    txtPromoTitle.setText("");
                                    txtPromoTitle.setVisibility(View.GONE);
                                    offerDeduction = 0;
                                    txtApplyPromo.setVisibility(View.VISIBLE);

                                    if (radioPayOnline.isChecked()) {
                                        if (isChecked) {
                                            amounttoPay = doctorPayment.getOnlinePayment().getWalletTotal();
                                            txtOnlinePrice.setText("Rs. " + GlobalVar.priceWithoutDecimal(amounttoPay));
                                            txtPay.setText("Pay Rs. " + GlobalVar.priceWithoutDecimal(amounttoPay));
                                        } else {
                                            amounttoPay = doctorPayment.getOnlinePayment().getTotalAmount();
                                            txtOnlinePrice.setText("Rs. " + GlobalVar.priceWithoutDecimal(amounttoPay));
                                            txtPay.setText("Pay Rs. " + GlobalVar.priceWithoutDecimal(amounttoPay));
                                        }
                                    }
                                });

                                radioButtonPayAtHospital.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                        edPromoCode.setText("");
                                        txtPromoTitle.setText("");
                                        txtPromoTitle.setVisibility(View.GONE);
                                        offerDeduction = 0;
                                        txtApplyPromo.setVisibility(View.VISIBLE);

                                        chkRedeem.setChecked(false);
                                        radioButtonPayAtHospital.setChecked(isChecked);
                                        radioPayOnline.setChecked(!isChecked);
                                        amounttoPay = doctorPayment.getPayAtHospital().getPayOnlineAmt();
                                        txtPay.setText("Pay Rs. " + GlobalVar.priceWithoutDecimal(amounttoPay));
                                        layoutWalletPoint.setVisibility(View.GONE);
                                        PaymentType = PAY_AT_HOSPITAL;
                                    }
                                });

                            }
                            HideSoft(coordinatorLayout);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, (VolleyError error) -> {
                GlobalVar.hideProgressDialog();
                showSnackBar(VolleyErrorHelper.getMessage(error.toString(), context));
            });
            jsonRequest.setTag("get");
            mQueue.add(jsonRequest);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getCalculation("doctor", totalPay);
        } else if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
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

    Gson gson = new Gson();

    DoctorPayment doctorPayment;

    ConnectionDector dector;

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

    DecimalFormat precision = new DecimalFormat("0.00");
    String bookingDate = "";

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
            jsonObject.put("TypeStatus", GlobalVar.PAY_STATUS_DOCTOR);
            jsonObject.put("Trans_String", "");
            // jsonObject.put("AppointmentDate", "");
            jsonObject.put("BookingDate", bookingDate);
            jsonObject.put("BookingFrom", "ANDROID");

            JSONObject jsonObjectPaymentDetails = new JSONObject();
            jsonObjectPaymentDetails.put("TotalFees", totalPay);

            jsonObjectPaymentDetails.put("PaymentType", PaymentType);
            if (PaymentType.equals(ONLINE_PAY)) {
                if (chkRedeem.isChecked()) {
                    jsonObjectPaymentDetails.put("OnlinePaid", amounttoPay);
                    //     jsonObjectPaymentDetails.put("OnlinePaid", doctorPayment.getOnlinePayment().getWalletTotal());
                    jsonObjectPaymentDetails.put("WalletDiduction", doctorPayment.getOnlinePayment().getWallet_amount());
                } else {
                    jsonObjectPaymentDetails.put("OnlinePaid", amounttoPay);
                    //   jsonObjectPaymentDetails.put("OnlinePaid", doctorPayment.getOnlinePayment().getTotalAmount());
                    jsonObjectPaymentDetails.put("WalletDiduction", "0");
                }

                jsonObjectPaymentDetails.put("OfflinePaid", "0");
            } else if (PaymentType.equals(PAY_AT_HOSPITAL)) {
                jsonObjectPaymentDetails.put("WalletDiduction", "0");
                jsonObjectPaymentDetails.put("OnlinePaid", amounttoPay);
                //   jsonObjectPaymentDetails.put("OnlinePaid", doctorPayment.getPayAtHospital().getPayOnlineAmt());
                jsonObjectPaymentDetails.put("OfflinePaid", doctorPayment.getPayAtHospital().getPayAtHospitalAmt());
            }

            jsonObjectPaymentDetails.put("PromoCode", promo_code_Str);
            jsonObjectPaymentDetails.put("OfferDiduction", offerDeduction);

            //PromoCode

            jsonObject.put("PaymentDetails", jsonObjectPaymentDetails);

            JSONArray jsonArrayTreatment = new JSONArray();
            for (DoctorTreatmentPojo d : selectedTreatmentList) {
                JSONObject jsonObjectTreatment = new JSONObject();
                jsonObjectTreatment.put("TestOrTreatmentOrPackageId", d.getRowid());
                jsonObjectTreatment.put("TestOrTreatmentOrPackageName", d.getTreatmentname());
                jsonObjectTreatment.put("DicountPrice", d.getDiscount_price());
                jsonObjectTreatment.put("OriginalPrice", d.getFee());
                jsonArrayTreatment.put(jsonObjectTreatment);
            }

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