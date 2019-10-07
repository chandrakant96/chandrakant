package com.whr.user.com.WHR.com.WHR.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.squareup.picasso.Picasso;
import com.whr.user.activities.LocationFindActivity;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomStringRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NevigationDrawerDashBordActivity;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.com.WHR.adapters.FollowupBookingAdapter;
import com.whr.user.pojo.DoctorTreatmentPojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static com.whr.user.com.WHR.adapters.TreatmentChargeCheckBookAdapter.totalval;

public class BookingFromFollowupActivity extends AppCompatActivity {


    TextView usernameText, hospitalName, doctorName, booking_date, followuptimetxt;
    private ImageView doctor_image;
    private int hospitalId = 0;
    private String feerowid = "";

    private String doctotrNamestr = "";
    private Context context;
    private int suggest_doctorId = 0;
    private boolean isSuggestedTreatment = false;
    private long familyid = 0;
    private String followupdate_date = "";
    private int docterId = 0, rowid = 0;
    private String hospitalname = "";
    private String followuptime = "";
    private String uname = "";
    private AppCompatActivity activity;
    private PreferenceUtils pref;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private RequestQueue mQueue;
    private ConnectionDector connectionDector;

    private CoordinatorLayout coordinatorLayout;
    private ProgressDialog pd, pdialog, pDialog;
    private FollowupBookingAdapter adapter;
    private List<DoctorTreatmentPojo> rowListItem = new ArrayList<>();
    private Button submitButton;
    private String docImagStr = "";
    private String tranid = "";
    private AlertDialog hospitalLocationConfornDialog;
    private boolean isCashPayment = false;

    String TAG = getClass().getSimpleName();

    private void GetChargeList() {
        GlobalVar.showProgressDialog(context, "Loading....", false);
        String url = GlobalVar.ServerAddress + "User/GetAppointmentTreatmentList";
        JSONObject obj = new JSONObject();
        try {
            obj.put("rowid", rowid);
            obj.put("hid", hospitalId);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GlobalVar.errorLog(TAG, "response :=>", response.toString());
                        GlobalVar.hideProgressDialog();
                        Gson gson = new Gson();
                        rowListItem.clear();
                        JSONArray jsonArray1 = null;
                        try {
                            jsonArray1 = response.getJSONArray("result");
                            if (jsonArray1 != null && jsonArray1.length() > 0) {
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json = jsonArray1.getJSONObject(i);
                                    DoctorTreatmentPojo pojo = gson.fromJson(json.toString(), DoctorTreatmentPojo.class);
                                    rowListItem.add(pojo);
                                }
                                adapter = new FollowupBookingAdapter(context, rowListItem, activity);
                                recyclerView.setAdapter(adapter);
                            } else {
                                rowListItem = new ArrayList<>();
                                adapter = new FollowupBookingAdapter(context, rowListItem, activity);
                                recyclerView.setAdapter(adapter);
                                GlobalVar.showSnackBar(coordinatorLayout, "No Data Available", context, R.color.red);
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

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void configViews() {
        context = BookingFromFollowupActivity.this;
        pref = new PreferenceUtils(context);
        activity = BookingFromFollowupActivity.this;
        connectionDector = new ConnectionDector(context);
        usernameText = (TextView) findViewById(R.id.usernameText);
        hospitalName = (TextView) findViewById(R.id.hospitalName);
        doctorName = (TextView) findViewById(R.id.doctorName);
        booking_date = (TextView) findViewById(R.id.booking_date);
        doctor_image = (ImageView) findViewById(R.id.doctor_image);
        followuptimetxt = (TextView) findViewById(R.id.followuptimetxt);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        submitButton = (Button) findViewById(R.id.submitButton);
        recyclerView = (RecyclerView) findViewById(R.id.hospitalListRecycleView);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        ///------------New Toolbar Code----------------
        RelativeLayout toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.title);
        textView.setText(getString(R.string.Treatment));
        TextView txtLocation = toolbar.findViewById(R.id.txtLocation);
        txtLocation.setVisibility(View.GONE);
        MaterialRippleLayout imgBack = toolbar.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        MaterialRippleLayout imgHome = toolbar.findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, NevigationDrawerDashBordActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                overridePendingTransitionExit();
            }
        });

        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LocationFindActivity.class);
                startActivityForResult(i, 1);
            }
        });
        ///------------New Toolbar code------------------------------

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {

            boolean bookingDateBool = bundle.containsKey("booking_date");
            boolean hospitalNameStrKey = bundle.containsKey("hospitalName");
            boolean docterIdkey = bundle.containsKey("docterIdkey");
            boolean rowidkey = bundle.containsKey("rowid");
            boolean docterNamekey = bundle.containsKey("doctotrName");
            boolean hospitalIdboolkey = bundle.containsKey("hospitalId");
            boolean feerowidboolkey = bundle.containsKey("feerowid");
            boolean unamekey = bundle.containsKey("uname");
            boolean doctorProfileImagekey = bundle.containsKey("doctorProfileImage");
            boolean familyidkey = bundle.containsKey("familyid");
            boolean isSuggestedTreatmentkey = bundle.containsKey("isSuggestedTreatment");
            boolean suggest_doctorIdkey = bundle.containsKey("suggest_doctorId");
            boolean appTimekey = bundle.containsKey("appTime");

            if (unamekey) {
                uname = bundle.getString("uname");
                usernameText.setText(uname);
            }

            if (isSuggestedTreatmentkey) {
                isSuggestedTreatment = bundle.getBoolean("isSuggestedTreatment");
            }

            if (suggest_doctorIdkey) {
                suggest_doctorId = bundle.getInt("suggest_doctorId");
            }

            if (familyidkey) {
                familyid = bundle.getLong("familyid");
            }
            if (hospitalIdboolkey) {
                hospitalId = Integer.parseInt(bundle.getString("hospitalId"));
            }

            if (feerowidboolkey) {
                feerowid = bundle.getString("feerowid");
            }
            if (appTimekey) {
                followuptime = bundle.getString("appTime");
                followuptimetxt.setText(followuptime + "");
            }
            if (docterNamekey) {
                doctotrNamestr = bundle.getString("doctotrName");
                doctorName.setText(doctotrNamestr + "");
            }

            if (doctorProfileImagekey) {
                docImagStr = bundle.getString("doctorProfileImage");

                if (docImagStr != null) {
                    if (docImagStr.length() > 0) {
                        String profile_img = docImagStr;
                        String str = profile_img.replace("\\", "");
                        if (profile_img.length() > 0) {
                            String pStr = str.replaceAll(" ", "%20");
                            if (pStr != null) {
                                Picasso.with(context).load(pStr).fit().into(doctor_image);
                            }
                        }
                    }
                }
            }
            if (rowidkey) {
                rowid = Integer.parseInt(bundle.getString("rowid"));
            }

            if (docterIdkey) {
                docterId = Integer.parseInt(bundle.getString("docterIdkey"));
            }

            if (bookingDateBool) {
                followupdate_date = bundle.getString("booking_date");
                booking_date.setText(getString(R.string.Date) + " : " + followupdate_date + "");
            }

            if (hospitalNameStrKey) {
                hospitalname = bundle.getString("hospitalName");
                Log.e("hospitalname", hospitalname);
                hospitalName.setText(hospitalname + "");
            }
        }

        if (connectionDector.isConnectingToInternet()) {
            if (hospitalId > 0) {
                GetChargeList();
            }
        } else {
            GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_from_followup);
        configViews();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    if (rowListItem.size() > 0 && rowListItem != null) {
                        getPaymentDialog();
                    } else {
                        GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.Pleaseselectatleastonetreatment), context, R.color.red);
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //---- payment code ----
    double tempsum = 0;
    double total = 0;
    double prize = 0;
    public static double sum = 0;
    private double orignalamountShowtoUser = 0;
    private double total_waltpoint = 0;
    private DecimalFormat df;
    private double savedAmount = 0;
    private double amounttoPay = 0;

    private void getPaymentDialog() {
        //----------------  payment dialog -----------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(BookingFromFollowupActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.show_payment_detail_layout, null);
        builder.setView(view);
        builder.setCancelable(false);
        Button yesBtn = (Button) view.findViewById(R.id.yesBtn);
        final RadioButton cashdelivery = (RadioButton) view.findViewById(R.id.cashdeliverycheck);
        final RadioButton onlinechck = (RadioButton) view.findViewById(R.id.onlinechck);
        RadioGroup rediaGroupButton = (RadioGroup) view.findViewById(R.id.rediaGroupButton);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        onlinechck.setTypeface(myFont);
        cashdelivery.setTypeface(myFont);
        ImageView closeBtn = (ImageView) view.findViewById(R.id.closeBtn);
        Button cashBenifitbtn = (Button) view.findViewById(R.id.cashBenifitbtn);
        Button onlinebenifitBtn = (Button) view.findViewById(R.id.onlinebenifitBtn);
        TextView benoifittext = (TextView) view.findViewById(R.id.benoifittext);

        String bookedTimeStr = "";

        if (followuptime != null && followuptime.length() > 0) {
            if (adapter != null) {
                bookedTimeStr = followuptime;
                Toast.makeText(context, "Book time" + bookedTimeStr, Toast.LENGTH_SHORT).show();
            } else {
                GlobalVar.showSnackBar(coordinatorLayout, "Please Select Appointment Time", context, R.color.red);
                return;
            }


            //--------Calculation is here -------------------
            Log.e("------- Calculation", "Starts -------");

            totalval = 0;

            for (int i = 0; i < rowListItem.size(); i++) {
                totalval = totalval + rowListItem.get(i).getDiscount_price();
                tempsum = totalval;
            }

            Log.e(" doctrTreatmentList", rowListItem.toString());
            Log.e(" tempsum", String.valueOf(tempsum));
            Log.e(" totalval", String.valueOf(totalval));

            Log.e("Amount_to_pay tempsum", tempsum + "");
            sum = tempsum;
            orignalamountShowtoUser = tempsum;
            Log.e("doctorTreatmentList", sum + "");

            double total = tempsum * 20 / 100;
            double prize = tempsum - total;
            orignalamountShowtoUser = tempsum;
            Log.e("doctorTreatmentList", sum + "");
            Log.e("total", total + "");
            Log.e("prize", prize + "");

            double s;
            double waletdiscounte = 0, waletdiscountedprice = 0;

            if (pref.getwallet_point() != null) {
                if (pref.getwallet_point().length() > 0) {
                    total_waltpoint = Double.parseDouble(pref.getwallet_point());
                }
            }

            Log.e("total_waltpoint", total_waltpoint + "");

            if (pref.getwallet_discount() != null) {
                if (pref.getwallet_discount().length() > 0) {
                    waletdiscounte = Double.parseDouble(pref.getwallet_discount());
                }
            }

            Log.e("waletdiscounte", waletdiscounte + "");
            df = new DecimalFormat("0.##");
            s = 100 - waletdiscounte;
            waletdiscountedprice = (s * sum) / 100;
            savedAmount = sum - waletdiscountedprice;
            // totalamounttxt.setText(sum + "₹");


            if (total_waltpoint == 0) {
                //     waletdiscountedprice= (s*sum)/100;
                //     amounttoPay= waletdiscountedprice;
                amounttoPay = sum;
                // totalamounttxt.setText(df.format(amounttoPay) + "₹");
                //  discountLayout.setVisibility(View.GONE);
                // savelayout.setVisibility(View.GONE);

            } else {
                if (total_waltpoint <= savedAmount) {
                    double temp = savedAmount - total_waltpoint;
                    amounttoPay = waletdiscountedprice + temp;
                    total_waltpoint = total_waltpoint - temp;
                    savedAmount = savedAmount - temp;
                    //  savemoneyamount.setText(df.format(savedAmount) + "₹");
                    Log.e("amounttoPay", amounttoPay + "");
                    // waletdiscountedpricetxt.setText(df.format(amounttoPay) + "₹");
                } else if (total_waltpoint >= savedAmount) {
                    amounttoPay = waletdiscountedprice;
                    total_waltpoint = total_waltpoint - savedAmount;
                    //savemoneyamount.setText(df.format(savedAmount) + "₹");
                    Log.e("total_waltpoint", total_waltpoint + "");
                    //waletdiscountedpricetxt.setText(df.format(amounttoPay) + "₹");
                }
            }

            Log.e("amounttoPay", amounttoPay + "");
            Log.e("total_waltpoint", total_waltpoint + "");

            Log.e("-------Calculation", "Ends -------");

            //------------- Calculation ends here -------------------


            df = (DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH);
            df.applyLocalizedPattern("#.##");
            amounttoPay = Double.valueOf(df.format(amounttoPay));


            cashdelivery.setText(getString(R.string.Cash) + " " + "₹ " + orignalamountShowtoUser);
            onlinechck.setText(getString(R.string.Online) + " " + "₹ " + amounttoPay);
            benoifittext.setPaintFlags(benoifittext.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            benoifittext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rowid = "";
                    for (int i = 0; i < rowListItem.size(); i++) {
                        rowid += rowListItem.get(i).getRowid() + ",";
                    }
                /*    Intent i = new Intent(context, ShowBenifiltActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("type", "online");
                    mBundle.putString("rowid", rowid);
                    mBundle.putInt("callType", 1);
                    i.putExtras(mBundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                    context.startActivity(i);*/

                }
            });

            cashBenifitbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rowid = "";
                    for (int i = 0; i < rowListItem.size(); i++) {
                        rowid += rowListItem.get(i).getRowid() + ",";
                    }
                 /*   Intent i = new Intent(context, ShowBenifiltActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("type", "cash");
                    mBundle.putString("rowid", rowid);
                    mBundle.putInt("callType", 2);
                    i.putExtras(mBundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                    context.startActivity(i);*/
                }
            });


            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isch = false;
                    if (cashdelivery.isChecked()) {
                        isch = true;
                        isCashPayment = true;
                    } else if (onlinechck.isChecked()) {
                        isch = true;
                        isCashPayment = false;
                    }
                    if (cashdelivery.isChecked()) {
                        if (hospitalLocationConfornDialog != null && hospitalLocationConfornDialog.isShowing()) {
                            hospitalLocationConfornDialog.dismiss();
                        }
                        payAtHospital();

                    } else if (onlinechck.isChecked()) {
                        if (connectionDector.isConnectingToInternet()) {
                            if (hospitalLocationConfornDialog != null && hospitalLocationConfornDialog.isShowing()) {
                                hospitalLocationConfornDialog.dismiss();
                            }
                            /*Random randomGenerator = new Random();
                            int randomInt = randomGenerator.nextInt(1000000000);
                            String test2 = "[{STATUS=TXN_SUCCESS, CHECKSUMHASH=E5WTpaLWAefeG9WqqHuJJxeWgi0nvgTo4TqcAEaHcAzTIIbo0wSEhEb8AsnbBVC2g+lasFfC4FmT2zgavclXHsRZ1oEnnz3oIx06O4VGLk4=, BANKNAME=CANARA, ORDERID=2762, TXNAMOUNT=" + amounttoPay + ", TXNDATE=2018-04-02 16:42:55.0, MID=LivWor74836176531967, TXNID=" + randomInt + ", RESPCODE=01, PAYMENTMODE=DC, BANKTXNID=9885543431680920, CURRENCY=INR, GATEWAYNAME=HDFC, RESPMSG=Txn Success}]";
                            bookAppoinment(adapter.bookedButton, amounttoPay, test2, String.valueOf(randomInt));*/
                            generateUniqueKey();


                        } else {
                            GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
                            return;
                        }
                    } else {
                        GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.PleaseSelectPaymentType), context, R.color.red);
                        return;
                    }
                }
            });


            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hospitalLocationConfornDialog != null && hospitalLocationConfornDialog.isShowing()) {
                        hospitalLocationConfornDialog.dismiss();
                    }
                }
            });

            hospitalLocationConfornDialog = builder.create();
            hospitalLocationConfornDialog.show();
        } else {
            GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.PleaseSelectAppointmentTime), context, R.color.red);
            return;
        }

    }

    public void payAtHospital() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BookingFromFollowupActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.pay_at_hospital, null);
        builder.setView(view);
        builder.setCancelable(false);

        Button yesBtn = (Button) view.findViewById(R.id.yesBtn);
        ImageView closeBtn = (ImageView) view.findViewById(R.id.closeBtn);

        total = tempsum * 20 / 100;
        prize = tempsum - total;

        TextView textView = (TextView) view.findViewById(R.id.avdanceAmt);
        textView.setText("₹ : " + (int) total);

        TextView textView1 = (TextView) view.findViewById(R.id.remainingAmt);
        textView1.setText("₹ : " + (int) prize);


        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //      String test2 = "[{STATUS=TXN_SUCCESS, CHECKSUMHASH=E5WTpaLWAefeG9WqqHuJJxeWgi0nvgTo4TqcAEaHcAzTIIbo0wSEhEb8AsnbBVC2g+lasFfC4FmT2zgavclXHsRZ1oEnnz3oIx06O4VGLk4=, BANKNAME=CANARA, ORDERID=2762, TXNAMOUNT=" + total + ", TXNDATE=2018-04-02 16:42:55.0, MID=LivWor74836176531967, TXNID=" + randomInt + ", RESPCODE=01, PAYMENTMODE=DC, BANKTXNID=9885543431680920, CURRENCY=INR, GATEWAYNAME=HDFC, RESPMSG=Txn Success}]";
                //  GetAppointmetforCashPayment(adapter.bookedButton, total, test2, String.valueOf(randomInt));
                generateUniqueKeyCash();
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hospitalLocationConfornDialog != null && hospitalLocationConfornDialog.isShowing()) {
                    hospitalLocationConfornDialog.dismiss();
                }
            }
        });

        hospitalLocationConfornDialog = builder.create();
        hospitalLocationConfornDialog.show();

    }


    private void bookAppoinment(final double amountTopay, String paytmresponsevalue, final String txn_id) {
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject obj = new JSONObject();
        try {
            String bookingTime = followuptime;
            String tid = "";
            String price = "";
            String disp = "";
            String originalfee = "";
            for (int i = 0; i < rowListItem.size(); i++) {
                tid += rowListItem.get(i).getRowid() + ",";
                price += rowListItem.get(i).getDiscount_price() + ",";
                disp += rowListItem.get(i).getDescription() + "#whr#";
                originalfee += rowListItem.get(i).getFee() + ",";
            }

            boolean isFollowup = true;

            String strDateResp = booking_date.getText().toString().replace("/", "-");
            //  obj.put("appointmentdate",booking_date );

            obj.put("appointmentdate", strDateResp);
            obj.put("cid", familyid);
            obj.put("did", docterId);
            obj.put("hid", hospitalId);
            obj.put("bookingTime", bookingTime);
            obj.put("loginid", pref.getUID());
            obj.put("amountPayedByUser", amountTopay);
            obj.put("paytmresponsevalue", paytmresponsevalue);
            obj.put("fee", price);
            obj.put("originalfee", originalfee);
            obj.put("description", disp);
            obj.put("bookingid", txn_id);
            obj.put("suggestedby", suggest_doctorId);
            obj.put("tid", tid);
            obj.put("isFollowup", isFollowup);
            obj.put("issuggested", isSuggestedTreatment);

            Log.e("url", GlobalVar.ServerAddress + "User/GetAppointmet");
            Log.e("obj", obj.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }


//================================================start ============================================

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                GlobalVar.ServerAddress + "User/GetAppointmet", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG + "Sandip :=>", response.toString());
                        GlobalVar.hideProgressDialog();
                        try {

                            String result = response.getString("result");
                            if (result.equalsIgnoreCase("true")) {
                                GlobalVar.showSnackBar(coordinatorLayout, "Appointment Successfully Booked", context, R.color.green);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {


                                    }
                                }, 3000);
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
                GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.green);
            }
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);


    }

    private void GetAppointmetforCashPayment(double paytmresponsevalue, final String amountTopay, String bookingId) {
        GlobalVar.showProgressDialog(this, ":Loading....", false);
        JSONObject obj = new JSONObject();
        try {
            String bookingTime = followuptime;
            String tid = "";
            String price = "";
            String disp = "";
            String originalfee = "";

            for (int i = 0; i < rowListItem.size(); i++) {
                tid += rowListItem.get(i).getRowid() + ",";
                price += rowListItem.get(i).getDiscount_price() + ",";
                disp += rowListItem.get(i).getDescription() + "#whr#";
                originalfee += rowListItem.get(i).getFee() + ",";
            }

            boolean isFollowup = true;
            //==============================================================
            String strDateResp = booking_date.getText().toString().replace("/", "-");
            obj.put("appointmentdate", strDateResp);
            obj.put("cid", familyid);
            obj.put("did", docterId);
            obj.put("hid", hospitalId);

            obj.put("bookingTime", bookingTime);
            obj.put("loginid", pref.getUID());
            obj.put("amountPayedByUser", tempsum);

            obj.put("fee", price);
            obj.put("originalfee", originalfee);
            obj.put("bookingid", bookingId);
            obj.put("description", disp);

            obj.put("paytmresponsevalue", amountTopay);
            obj.put("onlinepaid", String.valueOf(total));
            obj.put("inhospitalpaid", String.valueOf(prize));

            obj.put("suggestedby", suggest_doctorId);
            obj.put("tid", tid);
            obj.put("isFollowup", isFollowup);
            obj.put("issuggested", isSuggestedTreatment);


        } catch (Exception ex) {
            ex.printStackTrace();
            GlobalVar.hideProgressDialog();
        }

        String url = GlobalVar.ServerAddress + "User/GetAppointmetforCashPayment";
        Log.e(TAG + " url", url);
        Log.e(TAG + " obj", obj.toString());


        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .POST, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG + "bookingAppoint :=>", response.toString());
                        GlobalVar.hideProgressDialog();
                        try {
                            String result = response.getString("result");
                            final String bookingid = response.getString("bookingid");

                            if (result.equalsIgnoreCase("true")) {
                                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.AppointmentSuccessfullyBooked), context, R.color.green);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                }, 3000);
                            } else {
                                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.EnableToBookAppointmentPleaseryAgain), context, R.color.green);
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

    private void generateUniqueKey() {
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject obj = new JSONObject();
        try {

            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1000000000);
            final int orderid = randomInt;
            obj.put("orderid", orderid);
            Log.e(TAG + " url", GlobalVar.ServerAddress + "user/GenerateUniquekey");
            Log.e(TAG + " obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, GlobalVar.ServerAddress + "user/GenerateUniquekey", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG + " Sandip response :=>", response.toString());
                        GlobalVar.hideProgressDialog();
                        try {

                            JSONObject jobj = response.getJSONObject("uniquekey");
                            String key = jobj.getString("uniquekey");
                            Log.e(TAG + " Sandip uniquekey", key);

                            if (key != null) {
                                if (key.length() > 0) {
                                    if (connectionDector.isConnectingToInternet()) {
                                        paytmCode(key);

                                    } else {
                                        GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
                                        return;
                                    }
                                }
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

    private void generateUniqueKeyCash() {
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject obj = new JSONObject();
        try {
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1000000000);
            final int orderid = randomInt;
            obj.put("orderid", orderid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .POST, GlobalVar.ServerAddress + "user/GenerateUniquekey", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("bookingAppoint :=>", response.toString());
                        GlobalVar.hideProgressDialog();
                        try {

                            JSONObject jobj = response.getJSONObject("uniquekey");
                            String key = jobj.getString("uniquekey");
                            Log.e("generated_key", key);

                            if (key != null) {
                                if (key.length() > 0) {
                                    if (connectionDector.isConnectingToInternet()) {
                                        paytmCodeCash(key);
                                    } else {
                                        GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
                                        return;
                                    }
                                }
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

    private void paytmCode(String key) {

        final int orderId = Integer.parseInt(String.valueOf(key));
        String PAYTM_URL = "http://android.whrhealth.com/Checksum/GenerateChecksum.aspx";
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, PAYTM_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

                        Log.e("paytm_result", response);
                        Log.e("OrderIdInLog", String.valueOf(orderId));

                        PaytmPGService Service = PaytmPGService.getProductionService();
                        Map<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", GlobalVar.MID);
                        paramMap.put("ORDER_ID", String.valueOf(orderId));
                        paramMap.put("CUST_ID", String.valueOf(orderId));
                        paramMap.put("INDUSTRY_TYPE_ID", GlobalVar.Industry_type_ID);
                        paramMap.put("CHANNEL_ID", GlobalVar.Channel_ID);
                        String str = String.valueOf(amounttoPay);
                        Log.e("totalAmountTopay", str);
                        paramMap.put("TXN_AMOUNT", str);
                        paramMap.put("WEBSITE", GlobalVar.Website);
                        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
                        paramMap.put("EMAIL", pref.getEmail());
                        paramMap.put("MOBILE_NO", pref.getUID());
                        paramMap.put("CHECKSUMHASH", response);


                        //-----------------Paytm-------------------------------

                        PaytmOrder Order = new PaytmOrder(paramMap);
                        // PaytmMerchant paytmMerchant = new PaytmMerchant(response,"android.whrhealth.com/Checksum/transactionpaytm.aspx");
                        Service.initialize(Order, null);
                        Service.startPaymentTransaction(BookingFromFollowupActivity.this, true, true,
                                new PaytmPaymentTransactionCallback() {
                                    @Override
                                    public void someUIErrorOccurred(String inErrorMessage) {
                                        String resultstr = inErrorMessage;
                                        Log.e("Sandip resultstr", resultstr);
                                    }

                                    @Override
                                    public void onTransactionResponse(Bundle inResponse) {
                                        Log.d("LOG", "Payment Transaction : " + inResponse);
                                        String resultstr = inResponse.toString();
                                        Log.e("c", resultstr);

                                        String resp = resultstr.replace("Bundle", "");
                                        String[] data = resp.split(", ");
                                        String checksumstr = data[1];
                                        Log.e("bunddleValue_CHECK", checksumstr + "");

                                        String TXNID = data[7];
                                        String TXN_ID[] = TXNID.split("=");

                                        String tranid = "";
                                        String rspcode = "";
                                        String TXNAMOUNT = "";

                                        for (String str : data) {
                                            if (str.contains("TXNID")) {
                                                tranid = "";
                                                String[] TXNIDdata = resp.split("TXNID=");
                                                String[] TXNIDdata1 = TXNIDdata[1].split(", ");
                                                tranid = TXNIDdata1[0];
                                            } else if (str.contains("RESPCODE")) {
                                                rspcode = "";
                                                String[] TXNIDdata = resp.split("RESPCODE=");
                                                String[] TXNIDdata1 = TXNIDdata[1].split(", ");
                                                rspcode = TXNIDdata1[0];
                                            } else if (str.contains("TXNAMOUNT")) {
                                                // TXNAMOUNT=2.00,
                                                TXNAMOUNT = "";
                                                String[] TXNIDdata = resp.split("TXNAMOUNT=");
                                                String[] TXNIDdata1 = TXNIDdata[1].split(", ");
                                                TXNAMOUNT = TXNIDdata1[0];
                                            }
                                        }

                                        if (rspcode != null) {
                                            if (rspcode.length() > 0) {
                                                try {
                                                    if (Integer.parseInt(rspcode) == 01) {
                                                        if (rspcode != null) {
                                                            if (rspcode.length() > 0) {
                                                                String bundlevalue = resultstr.replace("Bundle", "");
                                                                if (connectionDector.isConnectingToInternet()) {
                                                                    GlobalVar.showSnackBar(coordinatorLayout,
                                                                            "Payment Successful Of ₹ " + TXNAMOUNT, context, R.color.green);
                                                                    bookAppoinment(Double.parseDouble(TXNAMOUNT), bundlevalue, tranid);
                                                                } else {
                                                                    GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
                                                                    return;
                                                                }
                                                            } else {
                                                                GlobalVar.showSnackBar(coordinatorLayout, "Payment Failed", context, R.color.green);
                                                            }
                                                        }
                                                    } else if (Integer.parseInt(rspcode) == 141) {
                                                        GlobalVar.showSnackBar(coordinatorLayout,
                                                                "Transaction cancelled by customer after landing on Payment Gateway Page",
                                                                context, R.color.red);
                                                    } else if (Integer.parseInt(rspcode) == 227) {
                                                        GlobalVar.showSnackBar(coordinatorLayout,
                                                                "Payment Failed due to a Bank Failure. Please try after some time.", context, R.color.red);
                                                    } else if (Integer.parseInt(rspcode) == 810) {
                                                        GlobalVar.showSnackBar(coordinatorLayout,
                                                                "Page closed by customer after landing on Payment Gateway Page.", context, R.color.red);
                                                    } else if (Integer.parseInt(rspcode) == 8102) {
                                                        GlobalVar.showSnackBar(coordinatorLayout,
                                                                "Customer had sufficient Wallet balance for completing transaction", context, R.color.red);
                                                    } else if (Integer.parseInt(rspcode) == 8103) {
                                                        GlobalVar.showSnackBar(coordinatorLayout,
                                                                "Customer had in-sufficient Wallet balance for completing transaction.",
                                                                context, R.color.red);
                                                    }
                                                } catch (Exception e) {
                                                    e.getMessage();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void networkNotAvailable() {
                                    }

                                    @Override
                                    public void clientAuthenticationFailed(String inErrorMessage) {
                                        String resultstr = inErrorMessage;
                                        Log.e("resultstr", resultstr);
                                    }

                                    @Override
                                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                                      String inErrorMessage, String inFailingUrl) {
                                        String resultstr = inErrorMessage;
                                        Log.e("resultstr", resultstr);
                                    }

                                    @Override
                                    public void onBackPressedCancelTransaction() {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                                        String resultstr = inErrorMessage;
                                        Log.e("resultstr", resultstr);
                                        Toast.makeText(context, "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                                    }
                                });


                        //-------------------------Paytm--------------------------


                        // end of function

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
                    }
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
                Log.e("totalAmountTopay", str);
                obj.put("TXN_AMOUNT", str);
                obj.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
                obj.put("EMAIL", pref.getEmail());
                obj.put("MOBILE_NO", pref.getUID());
                Log.e("orderid@chechsum=>", "Order_Id" + orderId);
                Log.e("order_inStringId=>", "Order_Id" + orderId);
                Log.e("order_inStringId=>", "obj" + obj.toString());
                return obj;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void paytmCodeCash(String key) {
        final int orderId = Integer.parseInt(String.valueOf(key));
        GlobalVar.showProgressDialog(this, "Loading....", false);

        String PAYTM_URL = "http://android.whrhealth.com/Checksum/GenerateChecksum.aspx";
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, PAYTM_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        GlobalVar.hideProgressDialog();
                        Log.e("paytm_result", response);
                        Log.e("OrderIdInLog", String.valueOf(orderId));

                        PaytmPGService Service = PaytmPGService.getProductionService();
                        Map<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", GlobalVar.MID);
                        paramMap.put("ORDER_ID", String.valueOf(orderId));
                        paramMap.put("CUST_ID", String.valueOf(orderId));
                        paramMap.put("INDUSTRY_TYPE_ID", GlobalVar.Industry_type_ID);
                        paramMap.put("CHANNEL_ID", GlobalVar.Channel_ID);
                        String str = String.valueOf(total);
                        Log.e("totalAmountTopay", str);
                        paramMap.put("TXN_AMOUNT", str);
                        paramMap.put("WEBSITE", GlobalVar.Website);
                        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
                        paramMap.put("EMAIL", pref.getEmail());
                        paramMap.put("MOBILE_NO", pref.getUID());
                        paramMap.put("CHECKSUMHASH", response);

                        PaytmOrder Order = new PaytmOrder(paramMap);
                        // PaytmMerchant paytmMerchant = new PaytmMerchant(response,"android.whrhealth.com/Checksum/transactionpaytm.aspx");
                        Service.initialize(Order, null);
                        Service.startPaymentTransaction(BookingFromFollowupActivity.this, true, true,
                                new PaytmPaymentTransactionCallback() {
                                    @Override
                                    public void someUIErrorOccurred(String inErrorMessage) {
                                        String resultstr = inErrorMessage;
                                        Log.e("resultstr", resultstr);
                                    }

                                    @Override
                                    public void onTransactionResponse(Bundle inResponse) {
                                        Log.d("LOG", "Payment Transaction : " + inResponse);
                                        String resultstr = inResponse.toString();
                                        Log.e("inResponse_resultstr", resultstr);


                                        String resp = resultstr.replace("Bundle", "");
                                        String[] data = resp.split(", ");
                                        String checksumstr = data[1];
                                        Log.e("bunddleValue_CHECK", checksumstr + "");

                                        String TXNID = data[7];
                                        String TXN_ID[] = TXNID.split("=");


                                        String tranid = "";
                                        String rspcode = "";
                                        String TXNAMOUNT = "";

                                        for (String str : data) {
                                            if (str.contains("TXNID")) {
                                                tranid = "";
                                                String[] TXNIDdata = resp.split("TXNID=");
                                                String[] TXNIDdata1 = TXNIDdata[1].split(", ");
                                                tranid = TXNIDdata1[0];
                                            } else if (str.contains("RESPCODE")) {
                                                rspcode = "";
                                                String[] TXNIDdata = resp.split("RESPCODE=");
                                                String[] TXNIDdata1 = TXNIDdata[1].split(", ");
                                                rspcode = TXNIDdata1[0];
                                            } else if (str.contains("TXNAMOUNT")) {

                                                // TXNAMOUNT=2.00,
                                                TXNAMOUNT = "";
                                                String[] TXNIDdata = resp.split("TXNAMOUNT=");
                                                String[] TXNIDdata1 = TXNIDdata[1].split(", ");
                                                TXNAMOUNT = TXNIDdata1[0];
                                            }


                                        }

                                        if (rspcode != null) {
                                            if (rspcode.length() > 0) {
                                                try {
                                                    if (Integer.parseInt(rspcode) == 01) {
                                                        if (rspcode != null) {

                                                            if (rspcode.length() > 0) {


                                                                String bundlevalue = resultstr.replace("Bundle", "");

                                                                if (connectionDector.isConnectingToInternet()) {
                                                                    GlobalVar.showSnackBar(coordinatorLayout, "Payment Successful Of ₹ " + TXNAMOUNT, context, R.color.green);
                                                                    // GetAppointmetforCashPayment(adapter.bookedButton, total);
                                                                    GetAppointmetforCashPayment(Double.parseDouble(TXNAMOUNT), bundlevalue, tranid);
                                                                } else {
                                                                    GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.green);
                                                                    return;
                                                                }
                                                            } else {
                                                                GlobalVar.showSnackBar(coordinatorLayout, "Payment Failed", context, R.color.green);
                                                            }

                                                        }
                                                    } else if (Integer.parseInt(rspcode) == 141) {
                                                        GlobalVar.showSnackBar(coordinatorLayout, "Transaction cancelled by customer after landing on Payment Gateway Page", context, R.color.red);
                                                    } else if (Integer.parseInt(rspcode) == 227) {
                                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Payment Failed due to a Bank Failure. Please try after some time.", Snackbar.LENGTH_LONG);
                                                        View snackBarView = snackbar.getView();
                                                        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                                                        snackbar.show();
                                                    } else if (Integer.parseInt(rspcode) == 810) {
                                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Page closed by customer after landing on Payment Gateway Page.", Snackbar.LENGTH_LONG);
                                                        View snackBarView = snackbar.getView();
                                                        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                                                        snackbar.show();
                                                    } else if (Integer.parseInt(rspcode) == 8102) {
                                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Customer had sufficient Wallet balance for completing transaction", Snackbar.LENGTH_LONG);
                                                        View snackBarView = snackbar.getView();
                                                        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                                                        snackbar.show();
                                                    } else if (Integer.parseInt(rspcode) == 8103) {
                                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Customer had in-sufficient Wallet balance for completing transaction.", Snackbar.LENGTH_LONG);
                                                        View snackBarView = snackbar.getView();
                                                        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                                                        snackbar.show();
                                                    }
                                                } catch (Exception e) {
                                                    e.getMessage();
                                                }
                                            }


                                        }

                                    }

                                    @Override
                                    public void networkNotAvailable() {
                                    }

                                    @Override
                                    public void clientAuthenticationFailed(String inErrorMessage) {
                                        String resultstr = inErrorMessage;
                                        Log.e("resultstr", resultstr);
                                    }

                                    @Override
                                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                                      String inErrorMessage, String inFailingUrl) {
                                        String resultstr = inErrorMessage;
                                        Log.e("resultstr", resultstr);
                                    }

                                    @Override
                                    public void onBackPressedCancelTransaction() {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                                        String resultstr = inErrorMessage;
                                        Log.e("resultstr", resultstr);
                                        Toast.makeText(context, "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                                    }

                                });


                        // end of function

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        GlobalVar.hideProgressDialog();
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                        snackbar.show();
                    }
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
                String str = String.valueOf(total);
                Log.e("totalAmountTopay", str);
                obj.put("TXN_AMOUNT", str);
                obj.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
                obj.put("EMAIL", pref.getEmail());
                obj.put("MOBILE_NO", pref.getUID());
                Log.e("orderid@chechsum=>", "Order_Id" + orderId);


                Log.e("order_inStringId=>", "Order_Id" + orderId);
                return obj;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }


    //previous code for pay at hospital

    private void GetAppointmetforCashPayment1(final double amountTopay) {

        JSONObject obj = new JSONObject();
        try {

            String tid = "";
            String price = "";
            String disp = "";
            String originalfee = "";
            for (int i = 0; i < rowListItem.size(); i++) {
                tid += rowListItem.get(i).getTid() + ",";
                price += rowListItem.get(i).getDiscount_price() + ",";
                disp += rowListItem.get(i).getDescription() + "#whr#";
                originalfee += rowListItem.get(i).getFee() + ",";
            }


            boolean isFollowup = false;

            String strDateResp = followupdate_date.replace("/", "-");
            Log.e("cid: ", pref.getUID() + " did: " + docterId + " appointmentdate: " + strDateResp + " hid: " + hospitalId + " tid: " + tid + " bookingTime: " + " bookingType " + " dispcription " + disp + " suggest_doctorId " + suggest_doctorId + " amountTopay " + amountTopay + " originalfee: " + originalfee);
            Log.e("amountToPaid", amountTopay + "");

            //  obj.put("appointmentdate",booking_date );
            obj.put("appointmentdate", strDateResp);
            obj.put("cid", familyid);
            obj.put("did", docterId);
            obj.put("hid", hospitalId);
            obj.put("bookingTime", followuptime);
            obj.put("loginid", pref.getUID());
            obj.put("amountPayedByUser", amountTopay);

            obj.put("fee", price);
            obj.put("originalfee", originalfee);
            obj.put("bookingid", "");
            obj.put("description", disp);
            obj.put("suggestedby", 0);
            obj.put("tid", tid);
            obj.put("isFollowup", true);
            obj.put("issuggested", false);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


//================================================start ============================================

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                GlobalVar.ServerAddress + "User/GetAppointmetforCashPayment",
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("bookingAppoint :=>", response.toString());
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        try {

                            boolean result = response.getBoolean("result");
                            final String bookingid = response.getString("bookingid");

                            if (result) {
                                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.AppointmentSuccessfullyBooked), context, R.color.green);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {



                                    }
                                }, 3000);
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
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
            }
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
        pDialog = ProgressDialog.show(BookingFromFollowupActivity.this, "Loading", "Please wait", true, false);
    }
}
