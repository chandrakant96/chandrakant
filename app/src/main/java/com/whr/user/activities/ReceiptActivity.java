package com.whr.user.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.adapter.ReceiptAdapter;

import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.model.PaymentReceiptModel;
import com.whr.user.pojo.GlobalVar;


import org.json.JSONObject;

public class ReceiptActivity extends AppCompatActivity {
    private MaterialRippleLayout materialRippleLayout;
    private TextView title;
    private TextView txtPatientName, txtPatientmobileNumber, txtPatientAge, txtbookingDate, txtBookingId, txtBookedBy;
    private TextView txtDoctorName, txtDoctorSpecilization, txtDoctorAddress, txtDoctorDate, txtDoctortime;
    private TextView txtTotal, txtWalletDiscoutAmount, txtPayAtHospital, txtOnlinePaid;
    private TextView txtTermConditionsOne, txtTermConditionstwo;
    private ImageView btnEmailReceiptEmail, btnSharereceiptWhatsapp;
    private TextView txtTermConditions, txtcancalationPolicy;
    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView scrollView;
    private Context context;
    private String transactionId, type;
    String TAG = getClass().getSimpleName();
    private RequestQueue mQueue;
    private TextView txtTankYou;
    private TextView txtCancelAppoinment, txtResschudle;

    private RecyclerView recycleviewAmountDetail;
    ReceiptAdapter adapter;
    private LinearLayoutManager lLayout;
    public final static String TRANSACTION_ID_KEY = "transactionId";
    public final static String BOOKING_KEY = "booking";
    boolean isBooking = false;
    private String status = "";
    TextView txtPromcode, txtPromcodeDeduction;
    LinearLayout linPromocode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        overridePendingTransitionEnter();

        linPromocode = findViewById(R.id.linPromocode);
        txtPromcode = findViewById(R.id.txtPromcode);
        txtPromcodeDeduction = findViewById(R.id.txtPromcodeDeduction);
        title = findViewById(R.id.title);
        txtTankYou = findViewById(R.id.txtTankYou);
        materialRippleLayout = findViewById(R.id.materialLayout);
        materialRippleLayout.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = (String) bundle.get("type");
            status = (String) bundle.get("status");
            transactionId = (String) bundle.get(TRANSACTION_ID_KEY);
            isBooking = bundle.containsKey(BOOKING_KEY);
            status = intent.getStringExtra(BOOKING_KEY);
            if (status.contains("Appointment")) {
                txtTankYou.setVisibility(View.GONE);

            } else {
                txtTankYou.setVisibility(View.VISIBLE);
            }
        }


        title.setText("Payment Receipt");
        context = ReceiptActivity.this;
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();

        txtPatientName = findViewById(R.id.txtPatientName);
        txtPatientmobileNumber = findViewById(R.id.txtPatientmobileNumber);
        txtPatientAge = findViewById(R.id.txtPatientAge);
        txtbookingDate = findViewById(R.id.txtbookingDate);
        txtBookingId = findViewById(R.id.txtBookingId);
        txtBookedBy = findViewById(R.id.txtBookedBy);

        txtDoctorName = findViewById(R.id.txtDoctorName);
        txtDoctorSpecilization = findViewById(R.id.txtDoctorSpecilization);
        txtDoctorAddress = findViewById(R.id.txtDoctorAddress);
        txtDoctorDate = findViewById(R.id.txtDoctorDate);
        txtDoctortime = findViewById(R.id.txtDoctortime);

        txtTotal = findViewById(R.id.txtTotal);
        txtWalletDiscoutAmount = findViewById(R.id.txtWalletDiscoutAmount);
        txtPayAtHospital = findViewById(R.id.txtPayAtHospital);
        txtOnlinePaid = findViewById(R.id.txtOnlinePaid);

        btnEmailReceiptEmail = findViewById(R.id.btnEmailReceiptEmail);
        btnSharereceiptWhatsapp = findViewById(R.id.btnSharereceiptWhatsapp);
        txtTermConditionsOne = findViewById(R.id.txtTermConditionsOne);
        txtTermConditionstwo = findViewById(R.id.txtTermConditionstwo);
        txtCancelAppoinment = findViewById(R.id.txtCancelAppoinment);
        txtResschudle = findViewById(R.id.txtResschudle);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        scrollView = findViewById(R.id.scrollview);
        recycleviewAmountDetail = findViewById(R.id.recycleviewAmountDetail);
        lLayout = new LinearLayoutManager(context);
        recycleviewAmountDetail.setLayoutManager(lLayout);

        String receiptLink = GlobalVar.ServerAddress + "AndroidNew/TransactionReceiptForEmail?transactionid=" + transactionId;
        btnSharereceiptWhatsapp.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.setType("text/plain");
            intent1.setPackage("com.whatsapp");
            if (intent1 != null) {
                intent1.putExtra(Intent.EXTRA_TEXT, receiptLink);
                startActivity(Intent.createChooser(intent1, "Send"));
            } else {
                Toast.makeText(this, "App not found", Toast.LENGTH_SHORT).show();
            }
        });


        btnEmailReceiptEmail.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.setType("text/plain");
            intent1.setPackage("com.google.android.gm");
            intent1.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
            intent1.putExtra(Intent.EXTRA_SUBJECT, "ReceiptActivity");
            intent1.putExtra(Intent.EXTRA_TEXT, receiptLink);
            startActivity(Intent.createChooser(intent1, "Send Email"));
        });


        if (new ConnectionDector(context).isConnectingToInternet()) {
            getPaymentReceipt();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }

        txtCancelAppoinment.setOnClickListener(v -> {
            startActivity(new Intent(context, CancelAppoinmentActivity.class));
            finish();
        });
        txtResschudle.setOnClickListener(v -> {
            startActivity(new Intent(context, RescheduleActivity.class));
            finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
            getPaymentReceipt();
        }
    }

    private void getPaymentReceipt() {
        JSONObject obj = new JSONObject();
        String url = GlobalVar.ServerAddress + "AndroidNew/TransactionReceipt";
        GlobalVar.showProgressDialog(context, "", false);

        try {
            obj.put("transactionid", transactionId);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, this::parseUserResponse, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context),
                    context, R.color.red
            );
        });
        customJSONObjectRequest.setTag("get");
        mQueue.add(customJSONObjectRequest);
    }

    Gson gson = new Gson();

    private void parseUserResponse(JSONObject response) {
        GlobalVar.hideProgressDialog();
        try {

            PaymentReceiptModel pojo = gson.fromJson(response.toString(), PaymentReceiptModel.class);
            txtPatientName.setText(pojo.getPatientName());
            txtPatientmobileNumber.setText(pojo.getPatientMobileNo());
            txtPatientAge.setText(pojo.getPatientAge());
            txtbookingDate.setText(pojo.getDateOfBooking());
            txtBookingId.setText(pojo.getBookingId());
            txtBookedBy.setText(pojo.getBookedBy());

            txtDoctorName.setText(pojo.getBookedAt());
            txtDoctorSpecilization.setText("(" + pojo.getSpecialization() + ")");
            txtDoctorAddress.setText(pojo.getAddress());
            txtDoctorDate.setText(pojo.getAppointmentDate());
            txtDoctortime.setText(pojo.getAppointmentTime());

            txtTotal.setText(pojo.getTotal());
            txtWalletDiscoutAmount.setText(pojo.getWalletDiscount());
            txtPayAtHospital.setText(pojo.getPayAtHospital());
            txtOnlinePaid.setText(pojo.getOnlinePaid());

            txtTermConditionsOne.setText(pojo.getCancelationpolicy());
            txtTermConditionstwo.setText(pojo.getBookingpolicy());
            adapter = new ReceiptAdapter(context, pojo.getTestOrTreatmentDetails());
            recycleviewAmountDetail.setAdapter(adapter);

            if (pojo.getPromocode() != null && !pojo.getPromocode().isEmpty() &&
                    pojo.getOfferDiscount() != null && !pojo.getOfferDiscount().isEmpty()
                    ) {
                linPromocode.setVisibility(View.VISIBLE);
                txtPromcode.setText(pojo.getPromocode());
                txtPromcodeDeduction.setText(pojo.getOfferDiscount());
            } else {
                linPromocode.setVisibility(View.GONE);
            }

            adapter.notifyDataSetChanged();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (isBooking) {
            Intent i = new Intent(context, NevigationDrawerDashBordActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else {
            finish();
        }
        overridePendingTransitionExit();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}