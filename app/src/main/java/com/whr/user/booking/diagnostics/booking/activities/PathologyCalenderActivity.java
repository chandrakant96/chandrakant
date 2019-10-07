package com.whr.user.booking.diagnostics.booking.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.diagnostics.booking.model.Suggested_TreatementPojo;
import com.whr.user.booking.doctor.booking.adapters.TimeSlotAdapter;
import com.whr.user.booking.models.TimeSlot;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PathologyCalenderActivity extends AppCompatActivity {
    private TextView txtConfirm;
    private Context context;
    private TextView txtTitle;
    String TAG = getClass().getSimpleName();
    private CalendarView calendarView1;
    private String strdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    Calendar calendar;
    ArrayList<Suggested_TreatementPojo> pathologyTreatmentList;
    private String pathologyName = "", pathid = "";
    double total;
    String pathAddressId = "";
    TextView textWithTIme, text;
    LinearLayout layoutTime;


    private RequestQueue mQueue;
    private ConnectionDector connectionDector;
    Animation animationTimeSlotShow;
    private RecyclerView recycleTimeSlot;
    String strApiDate;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat formatter4 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;
    public static final String MORNING_KEY = "Morning";
    public static final String AFTERNOON_KEY = "Afternoon";
    public static final String EVENING_KEY = "Evening";
    TextView txtMorning, txtAfternoon, txtEvening;
    String strTimeApi = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathology_calender);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        overridePendingTransitionEnter();
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        context = PathologyCalenderActivity.this;
        progressBar = findViewById(R.id.progressBar);
        connectionDector = new ConnectionDector(context);
        txtMorning = findViewById(R.id.txtMorning);
        txtAfternoon = findViewById(R.id.txtAfternoon);
        txtEvening = findViewById(R.id.txtEvening);
        nestedScrollView = findViewById(R.id.nestedScrollView);

        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        animationTimeSlotShow = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);

        strApiDate = formatter.format(Calendar.getInstance().getTime());
        recycleTimeSlot = findViewById(R.id.recyclerViewTimeSlot);
        GridLayoutManager lLayoutTimeSlot = new GridLayoutManager(this, 3);
        recycleTimeSlot.setLayoutManager(lLayoutTimeSlot);
        adapterTimeSlot = new TimeSlotAdapter(this, timeList, strApiDate);
        recycleTimeSlot.setAdapter(adapterTimeSlot);

        adapterTimeSlot.setOnClickListener(timeSlot -> {
            if (!checkTimeHasPass(timeSlot)) {
                GlobalVar.showMessage("Time has passed please select another time", context);
            } else {
                if (timeSlot.isBooked()) {
                    GlobalVar.showMessage("Time Already Booked", context);
                } else {
                    strTimeApi = timeSlot.getStrtime();
                    timeSlot.setChecked(true);
                    adapterTimeSlot.notifyDataSetChanged();
                }
            }
        });


        context = PathologyCalenderActivity.this;
        pathologyTreatmentList = new ArrayList<>();


        txtConfirm = findViewById(R.id.txtconfirmDate);
        txtTitle = findViewById(R.id.txtTitle);
        calendarView1 = findViewById(R.id.calendarView);
        imgBack = findViewById(R.id.imgBack);

        text = findViewById(R.id.text);
        textWithTIme = findViewById(R.id.textWithTIme);
        layoutTime = findViewById(R.id.layoutTime);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            pathologyName = (String) bundle.get("pathologyName");
            pathologyTreatmentList = bundle.getParcelableArrayList("pathologyTreatmentList");
            pathid = (String) bundle.get("pathid");
            total = ((double) bundle.get("total"));
        }

        txtTitle.setText(pathologyName);

        txtConfirm.setOnClickListener(v -> {
            if (pathid.equals("50189")) {
                if (strTimeApi.isEmpty()) {
                    GlobalVar.showMessage("Please Select Time", context);
                } else {
                    Intent intent1 = new Intent(context, NewPathologyBookingDetailsActivity.class);
                    intent1.putParcelableArrayListExtra("pathologyTreatmentList", pathologyTreatmentList);
                    intent1.putExtra("pathologyName", pathologyName);
                    intent1.putExtra("pathAddressId", pathAddressId);
                    intent1.putExtra("pathid", pathid);
                    intent1.putExtra("total", total);
                    intent1.putExtra("strdate", strdate );
                    intent1.putExtra("strTime",  strTimeApi);

                    startActivity(intent1);
                }
            }else{
                Intent intent1 = new Intent(context, NewPathologyBookingDetailsActivity.class);
                intent1.putParcelableArrayListExtra("pathologyTreatmentList", pathologyTreatmentList);
                intent1.putExtra("pathologyName", pathologyName);
                intent1.putExtra("pathAddressId", pathAddressId);
                intent1.putExtra("pathid", pathid);
                intent1.putExtra("total", total);
                intent1.putExtra("strdate", strdate);
                intent1.putExtra("strTime",  "");

                startActivity(intent1);

            }

        });

        imgBack.setOnClickListener(v -> onBackPressed());

        calendar = Calendar.getInstance();
        calendarView1.setDate(calendar.getTimeInMillis());
        calendarView1.setMinDate(calendar.getTimeInMillis());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, 2);
        calendarView1.setMinDate(calendar.getTimeInMillis());
        calendarView1.setMaxDate(calendar1.getTimeInMillis());
        calendarView1.setDate(calendar.getTimeInMillis());

        calendarView1.setOnDateChangeListener((CalendarView calendarView, int i, int i1, int i2) -> {

            i1 = i1 + 1;
            strdate = (i2 < 10 ? ("0" + i2) : (i2)) + "-" + (i1 < 10 ? ("0" + i1) : (i1)) + "-" + i;

        });


        if (pathid.contains("50189")) {
            text.setVisibility(View.GONE);
            layoutTime.setVisibility(View.VISIBLE);
            recycleTimeSlot.setVisibility(View.VISIBLE);
            GetTimeSlot(MORNING_KEY);
        } else {
            textWithTIme.setVisibility(View.GONE);
            layoutTime.setVisibility(View.GONE);
            recycleTimeSlot.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

        }

        txtAfternoon.setOnClickListener(view -> changeColorTime(txtAfternoon, txtMorning, txtEvening, AFTERNOON_KEY));
        txtMorning.setOnClickListener(view -> changeColorTime(txtMorning, txtAfternoon, txtEvening, MORNING_KEY));
        txtEvening.setOnClickListener(view -> changeColorTime(txtEvening, txtMorning, txtAfternoon, EVENING_KEY));
    }

    private List<TimeSlot> timeList = new ArrayList<>();
    private List<TimeSlot> timeListMorning = new ArrayList<>();
    private List<TimeSlot> timeListBooked = new ArrayList<>();
    private List<TimeSlot> timeListEvening = new ArrayList<>();
    private List<TimeSlot> timeListAfternoon = new ArrayList<>();
    private TimeSlotAdapter adapterTimeSlot;
    Gson gson = new Gson();

    private boolean checkTimeHasPass(TimeSlot t) {
        Calendar calendar1 = Calendar.getInstance();
        String strCurrentDate = formatter4.format(calendar1.getTime());
        String strAdapterDate = strApiDate + " " + t.getStrtime();
        try {
            Date currentDate = formatter4.parse(strCurrentDate);
            Date adapterDate = formatter4.parse(strAdapterDate);
            if (currentDate.after(adapterDate)) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void GetTimeSlot(String strTimeApiType) {
        if (connectionDector.isConnectingToInternet()) {
            String url = GlobalVar.ServerAddress + "Krasna/GetAvailableTimeSlot1";
            final JSONObject obj = new JSONObject();
            try {
                GlobalVar.errorLog(TAG, "url", url);
                GlobalVar.errorLog(TAG, "obj", obj.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            timeList.clear();
            timeListEvening.clear();
            timeListAfternoon.clear();
            timeListMorning.clear();
            adapterTimeSlot.notifyDataSetChanged();
            progressBar.setVisibility(View.VISIBLE);

            CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                    url, obj,
                    response -> {
                        progressBar.setVisibility(View.GONE);
                        JSONArray jsonArray1 = null;
                        JSONArray jsonArray2 = null;
                        JSONArray jsonArray3 = null;

                        try {

                            //BookedDates

//                            JSONArray jsonArrayBooked = response.getJSONArray("BookedDates");
//
//                            if (jsonArrayBooked != null && jsonArrayBooked.length() > 0) {
//                                for (int i = 0; i < jsonArrayBooked.length(); i++) {
//                                    JSONObject json = jsonArrayBooked.getJSONObject(i);
//                                    TimeSlot pojoBook = gson.fromJson(json.toString(), TimeSlot.class);
//                                    GlobalVar.errorLog(TAG, "booked", pojoBook.toString());
//                                    timeListBooked.add(pojoBook);
//                                }
//                            }

                            JSONObject object = response.getJSONObject("TimeList");

                            GlobalVar.errorLog(TAG, "MORNINGS", "MORNINGS");

                            jsonArray1 = object.getJSONArray("Mornings");
                            if (jsonArray1 != null && jsonArray1.length() > 0) {
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json = jsonArray1.getJSONObject(i);
                                    TimeSlot pojoM = gson.fromJson(json.toString(), TimeSlot.class);
                                    for (TimeSlot timeSlot : timeListBooked)
                                        if (timeSlot.getStrtime().equals(pojoM.getStrtime())) {
                                            pojoM.setBooked(true);
                                        }
                                    pojoM.setPassed(!checkTimeHasPass(pojoM));
                                    GlobalVar.errorLog(TAG, "pojoM", pojoM.toString());
                                    timeListMorning.add(pojoM);
                                }
                            }

                            GlobalVar.errorLog(TAG, "AFTERNOON", "AFTERNOON");

                            jsonArray2 = object.getJSONArray("Afternoons");
                            if (jsonArray2 != null && jsonArray2.length() > 0) {
                                for (int i = 0; i < jsonArray2.length(); i++) {
                                    JSONObject json = jsonArray2.getJSONObject(i);
                                    TimeSlot pojoA = gson.fromJson(json.toString(), TimeSlot.class);
                                    for (TimeSlot timeSlot : timeListBooked)
                                        if (timeSlot.getStrtime().equals(pojoA.getStrtime())) {
                                            pojoA.setBooked(true);
                                        }
                                    pojoA.setPassed(!checkTimeHasPass(pojoA));
                                    GlobalVar.errorLog(TAG, "pojoA", pojoA.toString());
                                    timeListAfternoon.add(pojoA);
                                }
                            }

                            GlobalVar.errorLog(TAG, "EVENING", "EVENING");
                            jsonArray3 = object.getJSONArray("Evenings");
                            if (jsonArray3 != null && jsonArray3.length() > 0) {
                                for (int i = 0; i < jsonArray3.length(); i++) {
                                    JSONObject json = jsonArray3.getJSONObject(i);
                                    TimeSlot pojoE = gson.fromJson(json.toString(), TimeSlot.class);
                                    for (TimeSlot timeSlot : timeListBooked)
                                        if (timeSlot.getStrtime().equals(pojoE.getStrtime())) {
                                            pojoE.setBooked(true);
                                        }
                                    pojoE.setPassed(!checkTimeHasPass(pojoE));
                                    GlobalVar.errorLog(TAG, "pojoE", pojoE.toString());
                                    timeListEvening.add(pojoE);
                                }
                            }

                            txtMorning.setVisibility(View.VISIBLE);
                            txtAfternoon.setVisibility(View.VISIBLE);
                            txtEvening.setVisibility(View.VISIBLE);

                            if (timeListMorning.size() == 0) {
                                txtMorning.setVisibility(View.GONE);
                            }
                            if (timeListAfternoon.size() == 0) {
                                txtAfternoon.setVisibility(View.GONE);
                            }
                            if (timeListEvening.size() == 0) {
                                txtEvening.setVisibility(View.GONE);
                            }

                            if (timeListMorning.size() != 0) {
                                changeColorTime(txtMorning, txtAfternoon, txtEvening, MORNING_KEY);
                            } else if (timeListAfternoon.size() != 0) {
                                changeColorTime(txtAfternoon, txtMorning, txtEvening, AFTERNOON_KEY);
                            } else if (timeListEvening.size() != 0) {
                                changeColorTime(txtEvening, txtMorning, txtAfternoon, EVENING_KEY);
                            } else {
                                GlobalVar.showMessage("Time slot not available for " + strApiDate + ". Please choose another date ", context);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> progressBar.setVisibility(View.GONE));
            jsonRequest.setTag("get");
            mQueue.add(jsonRequest);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void scrollToView(View view1, View view2) {
        nestedScrollView.requestChildFocus(view1, view2);
    }

    public void changeColorTime(TextView t1, TextView t2, TextView t3, String strTimeApiType) {
        t1.setTextColor(getResources().getColor(R.color.primary));
        t2.setTextColor(getResources().getColor(R.color.black));
        t3.setTextColor(getResources().getColor(R.color.black));
        timeList.clear();
        if (strTimeApiType.equalsIgnoreCase(MORNING_KEY)) {
            timeList.addAll(timeListMorning);
        } else if (strTimeApiType.equalsIgnoreCase(AFTERNOON_KEY)) {
            timeList.addAll(timeListAfternoon);
        } else if (strTimeApiType.equalsIgnoreCase(EVENING_KEY)) {
            timeList.addAll(timeListEvening);
        }

        if (timeList.size() != 0) {
            recycleTimeSlot.startAnimation(animationTimeSlotShow);
            recycleTimeSlot.setVisibility(View.VISIBLE);
        } else {
            GlobalVar.showMessage(strTimeApiType + " Time slot not available", context);
        }
        scrollToView(calendarView1, recycleTimeSlot);
        adapterTimeSlot.notifyDataSetChanged();
    }
}