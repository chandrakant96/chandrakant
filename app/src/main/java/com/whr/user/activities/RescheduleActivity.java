package com.whr.user.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.booking.doctor.booking.adapters.TimeSlotAdapter;
import com.whr.user.booking.models.TimeSlot;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
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

import static com.whr.user.booking.doctor.booking.DoctorAppointmentBookingActivity.AFTERNOON_KEY;
import static com.whr.user.booking.doctor.booking.DoctorAppointmentBookingActivity.EVENING_KEY;
import static com.whr.user.booking.doctor.booking.DoctorAppointmentBookingActivity.MORNING_KEY;

public class RescheduleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView imgBack;
    private TextView title, txtMorning, txtAfternoon, txtEvening, txtConfirmReschedule;
    private CalendarView calendarView1;
    private Spinner famillyMemberSpinner;
    private RecyclerView recycleTimeSlot;
    String TAG = getClass().getSimpleName();
    ProgressBar progressBar;
    private RequestQueue mQueue;
    Context context;
    Calendar calendar;
    String strApiDate = "";
    String strTimeApi = "";
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat formatter4 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    Animation animationTimeSlotShow;
    NestedScrollView nestedScrollView;
    CoordinatorLayout coordinatorLayout;
    PreferenceUtils pref;
    LinearLayout lineTypeTime;
    String did, hid, specilization, bookingid;
    String item = "";
    LinearLayout layoutMessage;
    EditText etxMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule);

        context = RescheduleActivity.this;
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        pref = new PreferenceUtils(context);


        imgBack = findViewById(R.id.imgBack);
        title = findViewById(R.id.title);
        progressBar = findViewById(R.id.progressBar);
        calendarView1 = findViewById(R.id.calendarView);
        animationTimeSlotShow = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtMorning = findViewById(R.id.txtMorning);
        txtAfternoon = findViewById(R.id.txtAfternoon);
        txtEvening = findViewById(R.id.txtEvening);
        lineTypeTime = findViewById(R.id.lineTypeTime);
        famillyMemberSpinner = findViewById(R.id.famillyMemberSpinner);
        layoutMessage = findViewById(R.id.layoutMessage);
        etxMessage = findViewById(R.id.etxMessage);

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        title.setText("Reschedule Appointment");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            bookingid = (String) bundle.get("bookingid");
            did = (String) bundle.get("did");
            hid = (String) bundle.get("hid");
            specilization = (String) bundle.get("specilization");

        }

        txtConfirmReschedule = findViewById(R.id.txtConfirmReschedule);

        txtConfirmReschedule.setOnClickListener(v -> {
            if (item.contains("Other")) {
                if (etxMessage.getText().toString().isEmpty()) {
                    showSnackBar("Enter Reason for cancellation*");
                } else if (strTimeApi.isEmpty()) {
                    showSnackBar("Select Time");
                } else {
                    item = etxMessage.getText().toString();
                    rescheduleAppoinment();
                }
            } else {
                if (strTimeApi.isEmpty()) {
                    showSnackBar("Select Time");
                } else {
                    rescheduleAppoinment();
                }
            }


        });

        txtAfternoon.setOnClickListener(view -> changeColorTime(txtAfternoon, txtMorning, txtEvening,
                AFTERNOON_KEY));
        txtMorning.setOnClickListener(view -> changeColorTime(txtMorning, txtAfternoon, txtEvening,
                MORNING_KEY));
        txtEvening.setOnClickListener(view -> changeColorTime(txtEvening, txtMorning, txtAfternoon,
                EVENING_KEY));


        calendar = Calendar.getInstance();
        //calendar.add(Calendar.DATE, 1);
        calendarView1.setDate(calendar.getTimeInMillis());
        calendarView1.setMinDate(calendar.getTimeInMillis());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, 2);
        calendarView1.setMinDate(calendar.getTimeInMillis());
        calendarView1.setMaxDate(calendar1.getTimeInMillis());
        calendarView1.setDate(calendar.getTimeInMillis());

        strApiDate = formatter.format(Calendar.getInstance().getTime());
        recycleTimeSlot = findViewById(R.id.recyclerViewTimeSlot);
        GridLayoutManager lLayoutTimeSlot = new GridLayoutManager(this, 4);
        recycleTimeSlot.setLayoutManager(lLayoutTimeSlot);
        adapterTimeSlot = new TimeSlotAdapter(this, timeList, strApiDate);
        recycleTimeSlot.setAdapter(adapterTimeSlot);

        if (did.equals("50189")) {
            GetTimeSlotKrsnaa(MORNING_KEY);
        } else {
            GetTimeSlot(strApiDate, MORNING_KEY);
        }

        calendarView1.setOnDateChangeListener((CalendarView calendarView, int i, int i1, int i2) -> {
            i1 = i1 + 1;
            strApiDate = (i2 < 10 ? ("0" + i2) : (i2)) + "-" + (i1 < 10 ? ("0" + i1) : (i1)) + "-" + i;
            progressBar.setVisibility(View.VISIBLE);
            scrollToView(calendarView1, progressBar);
            if (did.equals("50189")) {
                GetTimeSlotKrsnaa(MORNING_KEY);
            } else {
                GetTimeSlot(strApiDate, MORNING_KEY);
            }
        });
        adapterTimeSlot.setOnClickListener(timeSlot -> {
            if (did.equals("50189")) {
                strTimeApi = timeSlot.getStrtime();
                timeSlot.setChecked(true);
                adapterTimeSlot.notifyDataSetChanged();
            } else {
                if (!checkTimeHasPass(timeSlot)) {
                    showSnackBar("Time has passed please select another time");
                } else {
                    strTimeApi = timeSlot.getStrtime();
                    timeSlot.setChecked(true);
                    adapterTimeSlot.notifyDataSetChanged();
                }
            }


        });

        if (did.equals("50189")) {
            recycleTimeSlot.setVisibility(View.VISIBLE);
        } else {

            if (specilization.contains("Pathology")) {
                strTimeApi = "12:00 PM";
                recycleTimeSlot.setVisibility(View.GONE);
                lineTypeTime.setVisibility(View.GONE);

            }
            if (specilization.contains("Radiology")) {
                strTimeApi = "12:00 PM";
                recycleTimeSlot.setVisibility(View.GONE);
                lineTypeTime.setVisibility(View.GONE);
            } else if (specilization.contains("Multispeciality")) {
                strTimeApi = "12:00 PM";
                recycleTimeSlot.setVisibility(View.GONE);
                lineTypeTime.setVisibility(View.GONE);
            } else {
                recycleTimeSlot.setVisibility(View.VISIBLE);
            }
        }

        famillyMemberSpinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Not available at that time");
        categories.add("Found another Doctor/pathology lab");
        categories.add("Other");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        famillyMemberSpinner.setAdapter(dataAdapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();

        if (item.contains("Other")) {
            layoutMessage.setVisibility(View.VISIBLE);
        } else {
            layoutMessage.setVisibility(View.GONE);
        }


    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    private void dialogReschedule() {
        final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_reschedule);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);
        LinearLayout linearLayout = dialog.findViewById(R.id.layoutCancel);
        TextView txtDateTime = dialog.findViewById(R.id.txtDateTime);

        if (specilization.contains("Pathology")) {
            txtDateTime.setText(strApiDate);

        } else if (specilization.contains("Radiology")) {
            txtDateTime.setText(strApiDate);
        } else if (specilization.contains("Multispeciality")) {
            txtDateTime.setText(strApiDate);
        } else {
            txtDateTime.setText(strApiDate + "," + strTimeApi);
            ;
        }

        linearLayout.setOnClickListener(v -> startActivity(new Intent(context, AppointmentHistoryActivity.class)));

        dialog.show();
    }

    private List<TimeSlot> timeList = new ArrayList<>();
    private List<TimeSlot> timeListMorning = new ArrayList<>();
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

    private void GetTimeSlot(String strDateResp, String strTimeApiType) {
        String url = GlobalVar.ServerAddress + "AndroidNew/GetAvailableTimeSlot1";
        final JSONObject obj = new JSONObject();
        try {
            obj.put("did", did);
            obj.put("hid", hid);
            obj.put("uid", pref.getUID());
            obj.put("appointmentdate", strDateResp);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            GlobalVar.hideProgressDialog();
        }

        timeList.clear();
        timeListEvening.clear();
        timeListAfternoon.clear();
        timeListMorning.clear();
        adapterTimeSlot.notifyDataSetChanged();
        lineTypeTime.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    lineTypeTime.setVisibility(View.VISIBLE);
                    JSONArray jsonArray1 = null;
                    JSONArray jsonArray2 = null;
                    JSONArray jsonArray3 = null;

                    try {
                        JSONObject object = response.getJSONObject("TimeList");
                        jsonArray1 = object.getJSONArray("Mornings");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                TimeSlot pojo = gson.fromJson(json.toString(), TimeSlot.class);
                                if (checkTimeHasPass(pojo))
                                    timeListMorning.add(pojo);
                            }
                        }

                        jsonArray2 = object.getJSONArray("Afternoons");
                        if (jsonArray2 != null && jsonArray2.length() > 0) {
                            for (int i = 0; i < jsonArray2.length(); i++) {
                                JSONObject json = jsonArray2.getJSONObject(i);
                                TimeSlot pojo2 = gson.fromJson(json.toString(), TimeSlot.class);
                                if (checkTimeHasPass(pojo2))
                                    timeListAfternoon.add(pojo2);
                            }
                        }

                        jsonArray3 = object.getJSONArray("Evenings");
                        if (jsonArray3 != null && jsonArray3.length() > 0) {
                            for (int i = 0; i < jsonArray3.length(); i++) {
                                JSONObject json = jsonArray3.getJSONObject(i);
                                TimeSlot pojo3 = gson.fromJson(json.toString(), TimeSlot.class);
                                if (checkTimeHasPass(pojo3))
                                    timeListEvening.add(pojo3);
                            }
                        }

                        txtMorning.setVisibility(View.VISIBLE);
                        txtAfternoon.setVisibility(View.VISIBLE);
                        txtEvening.setVisibility(View.VISIBLE);

                        if (timeListMorning.size() == 0) {
                            txtMorning.setVisibility(View.GONE);
                        } else if (timeListAfternoon.size() == 0) {
                            txtAfternoon.setVisibility(View.GONE);
                        } else if (timeListEvening.size() == 0) {
                            txtEvening.setVisibility(View.GONE);
                        }


                        if (timeListMorning.size() != 0) {
                            changeColorTime(txtMorning, txtAfternoon, txtEvening, MORNING_KEY);
                        } else if (timeListAfternoon.size() != 0) {
                            changeColorTime(txtAfternoon, txtMorning, txtEvening, AFTERNOON_KEY);
                        } else if (timeListEvening.size() != 0) {
                            changeColorTime(txtEvening, txtMorning, txtAfternoon, EVENING_KEY);
                        } else {
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> progressBar.setVisibility(View.GONE));
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }


    private void GetTimeSlotKrsnaa(String strTimeApiType) {

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


                        JSONObject object = response.getJSONObject("TimeList");

                        GlobalVar.errorLog(TAG, "MORNINGS", "MORNINGS");

                        jsonArray1 = object.getJSONArray("Mornings");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                TimeSlot pojoM = gson.fromJson(json.toString(), TimeSlot.class);
                                //pojoM.setPassed(!checkTimeHasPass(pojoM));
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
                                /// pojoA.setPassed(!checkTimeHasPass(pojoA));
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
                                //pojoE.setPassed(!checkTimeHasPass(pojoE));
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
            showSnackBar(strTimeApiType + " Time slot not available");
        }
        scrollToView(calendarView1, recycleTimeSlot);
        adapterTimeSlot.notifyDataSetChanged();
    }

    private void scrollToView(View view1, View view2) {
        nestedScrollView.requestChildFocus(view1, view2);
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }


    private void rescheduleAppoinment() {
        String url = GlobalVar.ServerAddress + "AndroidNew/ResheduleAppointmet";
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject params = new JSONObject();
        try {
            params.put("bookingid", bookingid);
            params.put("Appointmentdate", strApiDate + " " + strTimeApi);
            params.put("reson", item);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "params", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, params,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "Canccelation :=>", response.toString());
                    try {
                        JSONObject jsonRootObject = new JSONObject(response.toString());
                        if (jsonRootObject.length() > 0) {
                            boolean result = jsonRootObject.getBoolean("result");
                            Log.e("User_Reg_result", Boolean.toString(result));
                            if (result) {
                                dialogReschedule();
                                finish();

                            } else if (response.toString().contains("Duplicate")) {
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
}
