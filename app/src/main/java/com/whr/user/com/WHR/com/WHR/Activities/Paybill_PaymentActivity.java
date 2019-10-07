package com.whr.user.com.WHR.com.WHR.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.activities.PaytmPaymentGatewayActivity;
import com.whr.user.activities.ReceiptActivity;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.com.WHR.adapters.PayBillAdapter;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pojo.PayBillPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Paybill_PaymentActivity extends AppCompatActivity {

    private PayBillAdapter adapter;
    private Context context;
    private LinearLayoutManager lLayout;
    private ProgressDialog pDialog;
    private List<PayBillPojo.TreatmentList> rowListItem;
    private CoordinatorLayout coordinatorLayout;
    // PeopleAdapter peopleAdapteradapter;
    private LinearLayout mainCointenerToreplace;
    private Spinner BrowseCategouries;
    private RequestQueue mQueue;
    private ConnectionDector connectionDector;
    private RecyclerView recyclerView;
    private ImageView doctor_image;
    TextView doctorName, docSpec, doc_address, doc_edu, totalamounttextview, clinicNametxt;
    private double totalAmountTopay = 0;
    private Button submitButton;

    private PreferenceUtils preferenceUtils;
    private int rowid;

    private LinearLayout payBillmainLayout;
    private String resultstr = "";

    private String responceJsonObjectStr = "";
    private String doctorNamestr = "", profile_img = "";

    private int did;
    private String tranid = "";
    private String familyname = "";
    private String familyid = "0";
    DecimalFormat df;
    String TAG = getClass().getSimpleName();
    public static String ONLINE_PAY = "online";
    String hid = "0";
    SimpleDateFormat formatter5 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paybill__payment);
        context = this;
        ///------------New Toolbar Code----------------
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        ///------------New Toolbar code------------------------------

        doctor_image = findViewById(R.id.doctor_image);
        doctorName = findViewById(R.id.doctorName);
        docSpec = findViewById(R.id.doctorSpecilazationtextView);
        doc_edu = findViewById(R.id.educationtextView);
        doc_address = findViewById(R.id.doctorAddress_AddresstextView);
        totalamounttextview = findViewById(R.id.totalamounttextview);
        clinicNametxt = findViewById(R.id.clinicNametxt);
        submitButton = findViewById(R.id.submitButton);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        payBillmainLayout = findViewById(R.id.payBillmainLayout);
        recyclerView = findViewById(R.id.paybill_Recycle_View);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean doctorNamekey = bundle.containsKey("doctorName");
            boolean doctorIdKEY = bundle.containsKey("doctorId");
            boolean profile_imgKEY = bundle.containsKey("profile_img");
            boolean rowidKEY = bundle.containsKey("rowid");
            boolean familyidKEY = bundle.containsKey("familyid");
            boolean familynameKEY = bundle.containsKey("familyname");

            if (familynameKEY) {
                familyname = bundle.getString("familyname");
            }

            if (doctorNamekey) {
                doctorNamestr = bundle.getString("doctorName");
                doctorName.setText(doctorNamestr + "");
            }

            if (doctorIdKEY) {
                did = bundle.getInt("doctorId");
            }

            if (rowidKEY) {
                rowid = bundle.getInt("rowid");
            }
            if (familyidKEY) {
                familyid = bundle.getString("familyid");
            }


            if (profile_imgKEY) {
                profile_img = bundle.getString("profile_img");
                String profile_imgstr = profile_img;
                if (profile_imgstr != null) {
                    if (profile_imgstr.length() > 0) {
                        String str = profile_imgstr.replace("\\", "");
                        if (str != null) {
                            Picasso.with(context).load(str).fit().into(doctor_image);
                        }
                    }
                }
            }

            preferenceUtils = new PreferenceUtils(Paybill_PaymentActivity.this);
            connectionDector = new ConnectionDector(Paybill_PaymentActivity.this);
            rowListItem = new ArrayList<>();
            mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();

            if (connectionDector.isConnectingToInternet()) {
                methodVoid();
            } else {
                startActivityForResult(new Intent(context,NoInternetConnectionActivity.class),GlobalVar.NO_INTERNET_REQUEST_CODE);
                overridePendingTransitionExit();
            }

            submitButton.setOnClickListener(v -> {
                if (totalAmountTopay != 0) {
                    Intent intent = new Intent(context, PaytmPaymentGatewayActivity.class);
                    intent.putExtra(PaytmPaymentGatewayActivity.AMOUNT_KEY, totalAmountTopay);
                    startActivityForResult(intent, 1000);
                }
            });
        }
    }

    public void methodVoid() {
        JSONObject obj = new JSONObject();
        String url = GlobalVar.ServerAddress + "AndroidNew/GetDetailDoctorOrTreatment";
        GlobalVar.showProgressDialog(this, "Loading...", false);
        try {
            obj.put("did", did);
            obj.put("uid", familyid);
            //obj.put("uid", "9545013121");
            GlobalVar.errorLog(TAG, "obj", obj.toString());
            GlobalVar.errorLog(TAG, "url", url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj, response -> {
            GlobalVar.hideProgressDialog();
            Log.e("methodVoid>", response.toString());
            rowListItem.clear();
            Gson gson = new Gson();
            JSONArray jsonArray1 = null;
            try {
                //   jsonArray1 = response.getJSONArray("treatmentList");
                jsonArray1 = response.getJSONArray("treatmentList");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject json = jsonArray1.getJSONObject(i);
                    PayBillPojo.TreatmentList pojo = gson.fromJson(json.toString(), PayBillPojo.TreatmentList.class);
                    rowListItem.add(pojo);
                }

                JSONObject resultObj = response.getJSONObject("result");
                PayBillPojo pojoobj = gson.fromJson(resultObj.toString(), PayBillPojo.class);
                Log.e("paybillPojo", pojoobj.toString());

                doctorName.setText(pojoobj.getDocName());
                docSpec.setText(pojoobj.getDocspec());
                doc_edu.setText(pojoobj.getDoc_education().replace("\n", " "));
                doc_address.setText(pojoobj.getDocAddress());
                did = Integer.parseInt(pojoobj.getDid());
                // String totalAmount = pojoobj.getTotalAmount();
                String cname = pojoobj.getCname();

                if (cname != null) {
                    if (cname.length() > 0) {
                        clinicNametxt.setText(cname);
                    } else {
                        clinicNametxt.setVisibility(View.GONE);
                    }
                } else {
                    clinicNametxt.setVisibility(View.GONE);
                }

                totalAmountTopay = 0;

                for (int k = 0; k < rowListItem.size(); k++) {
                    totalAmountTopay += Double.parseDouble(rowListItem.get(k).getDiscount_price());
                }

                df = (DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH);
                df.applyLocalizedPattern("#.##");
                totalAmountTopay = Double.parseDouble(df.format(totalAmountTopay));
                totalamounttextview.setText(getString(R.string.TotalPrice) + " ₹ " + totalAmountTopay);
                String profile_img = pojoobj.getDocImag();
                if (profile_img != null) {
                    if (profile_img.length() > 0) {
                        String str = profile_img.replace("\\", "");
                        if (str != null) {
                            Picasso.with(context).load(str).fit().into(doctor_image);
                        }
                    }
                }
                if (rowListItem.size() > 0) {
                    submitButton.setVisibility(View.VISIBLE);
                    adapter = new PayBillAdapter(context, rowListItem);
                    recyclerView.setAdapter(adapter);
                } else {
                    submitButton.setVisibility(View.GONE);
                    GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoBillDetailAvailable), context, R.color.red);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            GlobalVar.hideProgressDialog();
            submitButton.setVisibility(View.GONE);
            GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);

        });
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
        String url = GlobalVar.ServerAddress + "AndroidNew/BookAppointment";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("TransactionId", transactionId);
            if (transactionStatus.equalsIgnoreCase("01")) {
                jsonObject.put("TransactionStatus", "1");
            } else {
                jsonObject.put("TransactionStatus", "0");
            }

            jsonObject.put("UserId", preferenceUtils.getUID());

            jsonObject.put("PatientId", familyid);
            jsonObject.put("TransactionForId", did);
            jsonObject.put("HidOfDoctor", hid);
            jsonObject.put("TypeStatus", GlobalVar.PAY_STATUS_DOCTOR);
            jsonObject.put("Trans_String", "");
            // jsonObject.put("AppointmentDate", "");
            jsonObject.put("BookingDate", formatter5.format(new Date()));
            jsonObject.put("BookingFrom", "ANDROID");

            JSONObject jsonObjectPaymentDetails = new JSONObject();
            jsonObjectPaymentDetails.put("TotalFees", totalAmountTopay);

            jsonObjectPaymentDetails.put("PaymentType", ONLINE_PAY);
            jsonObjectPaymentDetails.put("OnlinePaid", totalAmountTopay);
            jsonObjectPaymentDetails.put("WalletDiduction", "0");

            jsonObjectPaymentDetails.put("OfflinePaid", "0");
            jsonObjectPaymentDetails.put("OfferDiduction", "0");

            jsonObject.put("PaymentDetails", jsonObjectPaymentDetails);

            JSONArray jsonArrayTreatment = new JSONArray();
            for (PayBillPojo.TreatmentList d : rowListItem) {
                JSONObject jsonObjectTreatment = new JSONObject();
                jsonObjectTreatment.put("TestOrTreatmentOrPackageId", d.getTid());
                jsonObjectTreatment.put("TestOrTreatmentOrPackageName", d.getTname());
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
