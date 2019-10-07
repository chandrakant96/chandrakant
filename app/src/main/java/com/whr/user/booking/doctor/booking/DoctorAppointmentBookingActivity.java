package com.whr.user.booking.doctor.booking;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoDataAvailableActivity;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.doctor.booking.adapters.DoctorAppoinmentBookingAdapter;
import com.whr.user.booking.doctor.booking.adapters.SelectedTreatmentAdapter;
import com.whr.user.booking.doctor.booking.adapters.TimeSlotAdapter;
import com.whr.user.booking.doctor.booking.models.Clinicname;
import com.whr.user.booking.doctor.booking.models.DoctorModel;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.booking.doctor.booking.models.HospitalProfile;
import com.whr.user.booking.doctor.booking.models.NewDoctorProfile;
import com.whr.user.booking.models.TimeSlot;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.DoctorTreatmentPojo;
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


public class DoctorAppointmentBookingActivity extends AppCompatActivity {

    private String hid;
    private CoordinatorLayout coordinatorLayout;
    private ConnectionDector connectionDector;
    private DoctorAppoinmentBookingAdapter adaptertratment;
    private SelectedTreatmentAdapter selectedTreatmentAdapter;
    private Context context;
    private LinearLayoutManager lLayout, lLayout2;
    private List<DoctorTreatmentPojo> rowListItem = new ArrayList<>();
    private List<DoctorTreatmentPojo> selectedTreatmentList = new ArrayList<>();

    private String hospitalNameArray[];
    private RequestQueue mQueue;
    LinearLayout layoutCalender;
    private PreferenceUtils pref;
    private long familyid = 0;
    private AppCompatActivity activity;
    private Spinner spinnerHospital;

    Animation animationTimeSlotShow;
    private RecyclerView recycleTimeSlot, recyclerViewTreatment, recyclerViewSelectedTreatment;
    private CalendarView calendarView1;
    Calendar calendar;
    String strApiDate;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat formatter4 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    private TextView txtBookAppointment;
    DoctorModel doctorModel;
    LinearLayout lineTreatment;
    TextView txtDropDownTreatment, txtDropDownCalender;
    boolean isTreatExpand = false;
    boolean isCalenderExpand = false;
    EditText edSearch;
    String TAG = getClass().getSimpleName();
    TextView txtDoctorName, txtDistance, txtTitle;
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;
    public static final String APPOINTMENT_DATE_KEY = "booking_date";
    public static final String APPOINTMENT_TIME_KEY = "booking_time";
    public static final String TOTAL_PAY_KEY = "total_pay";
    public static final String HID_KEY = "hid";
    public static final String DID_KEY = "did";
    public static final String SELECTED_TREATMENT_LIST_KEY = "selected_treatment_list";
    public static final String MORNING_KEY = "Morning";
    public static final String AFTERNOON_KEY = "Afternoon";
    public static final String EVENING_KEY = "Evening";
    TextView txtMorning, txtAfternoon, txtEvening;
    String strTimeApi = "";
    NewDoctorProfile.HospitalProfile hospitalProfile;
    HospitalProfile.DoctorList doctorList;
    HospitalModel hospitalModel;
    String did = "";
    double totalPay = 0;
    CardView cardTreatment, cardDate;
    LinearLayout hideLayout, txtTitlesHide;
    String hospitalName = "";
    boolean isKeyHospital;
    boolean isKeyDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appoinment_bookin);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        overridePendingTransitionEnter();
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        context = DoctorAppointmentBookingActivity.this;
        progressBar = findViewById(R.id.progressBar);
        cardDate = findViewById(R.id.cardDate);

        txtTitlesHide = findViewById(R.id.txtTitlesHide);
        hideLayout = findViewById(R.id.hideLayout);
        lineTypeTime = findViewById(R.id.lineTypeTime);
        txtMorning = findViewById(R.id.txtMorning);
        txtAfternoon = findViewById(R.id.txtAfternoon);
        txtEvening = findViewById(R.id.txtEvening);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        cardTreatment = findViewById(R.id.cardTreatment);
        txtDropDownCalender = findViewById(R.id.txtDropDownCalender);
        txtDropDownTreatment = findViewById(R.id.txtDropDownTreatment);

        txtDoctorName = findViewById(R.id.txtDoctorName);
        txtDistance = findViewById(R.id.txtdistance);
        txtTitle = findViewById(R.id.txtTitle);
        lineTreatment = findViewById(R.id.lineTreatment);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        edSearch = findViewById(R.id.edSearch);
        layoutCalender = findViewById(R.id.layoutCalender);
        txtBookAppointment = findViewById(R.id.txtBookAppointment);
        // txtBookAppointment.setVisibility(View.GONE);
        animationTimeSlotShow = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        calendarView1 = findViewById(R.id.calendarView);
        spinnerHospital = findViewById(R.id.spinnerHospital);
        recyclerViewSelectedTreatment = findViewById(R.id.recyclerViewSelectedTreatment);
        recyclerViewTreatment = findViewById(R.id.hospitalRecycleView);
        connectionDector = new ConnectionDector(context);
        pref = new PreferenceUtils(context);
        activity = DoctorAppointmentBookingActivity.this;
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        hideKeyboard(edSearch);

        strApiDate = formatter.format(Calendar.getInstance().getTime());
        recycleTimeSlot = findViewById(R.id.recyclerViewTimeSlot);
        GridLayoutManager lLayoutTimeSlot = new GridLayoutManager(this, 3);
        recycleTimeSlot.setLayoutManager(lLayoutTimeSlot);
        adapterTimeSlot = new TimeSlotAdapter(this, timeList, strApiDate);
        recycleTimeSlot.setAdapter(adapterTimeSlot);

        adapterTimeSlot.setOnClickListener(timeSlot -> {
            if (!checkTimeHasPass(timeSlot)) {
                showSnackBar("Time has passed please select another time");
            } else {
                if (timeSlot.isBooked()) {
                    showSnackBar("Time Already Booked");
                } else {

                    strTimeApi = timeSlot.getStrtime();
                    timeSlot.setChecked(true);
                    adapterTimeSlot.notifyDataSetChanged();
                    txtBookAppointment.setVisibility(View.VISIBLE);
                    txtDropDownCalender.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_verified), null);
                }
            }
        });

        lLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSelectedTreatment.setLayoutManager(lLayout);
        selectedTreatmentAdapter = new SelectedTreatmentAdapter(context,
                selectedTreatmentList, activity);
        recyclerViewSelectedTreatment.setHasFixedSize(true);
        recyclerViewSelectedTreatment.setAdapter(selectedTreatmentAdapter);

        selectedTreatmentAdapter.setOnClickListener(d -> {
            if (!d.isCheck()) {
                d.setCheck(true);
                totalPay += Double.parseDouble(d.getDiscount_price() + " ");
                txtBookAppointment.setText("Pay Rs. " + GlobalVar.priceWithoutDecimal(totalPay));
            } else {
                d.setCheck(false);
                totalPay -= Double.parseDouble(d.getDiscount_price() + " ");
                txtBookAppointment.setText("Pay Rs. " + GlobalVar.priceWithoutDecimal(totalPay));
            }
            selectedTreatmentList.remove(d);
            selectedTreatmentAdapter.notifyDataSetChanged();

            for (DoctorTreatmentPojo doctorTreatmentPojo : rowListItem) {
                if (doctorTreatmentPojo.getTid() == d.getTid()) {
                    doctorTreatmentPojo.setCheck(d.isCheck());
                }
            }
            adaptertratment.notifyDataSetChanged();
        });

        lLayout2 = new LinearLayoutManager(context);
        recyclerViewTreatment.setLayoutManager(lLayout2);
        adaptertratment = new DoctorAppoinmentBookingAdapter(context,
                rowListItem, activity);
        recyclerViewTreatment.setHasFixedSize(true);
        recyclerViewTreatment.setAdapter(adaptertratment);

        adaptertratment.setOnClickListener(d -> {
            if (!d.isCheck()) {
                d.setCheck(true);
                totalPay += Double.parseDouble(d.getDiscount_price() + " ");
                txtBookAppointment.setText("Pay Rs. " + GlobalVar.priceWithoutDecimal(totalPay));
                selectedTreatmentList.add(d);
            } else {
                d.setCheck(false);
                totalPay -= Double.parseDouble(d.getDiscount_price() + " ");
                txtBookAppointment.setText("Pay Rs. " + GlobalVar.priceWithoutDecimal(totalPay));
                selectedTreatmentList.remove(d);
            }

            txtBookAppointment.setVisibility(View.VISIBLE);

            if (totalPay == 0) {
                txtDropDownTreatment.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else {
                txtDropDownTreatment.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this,
                        R.drawable.ic_verified), null);
            }


            adaptertratment.notifyDataSetChanged();
            selectedTreatmentAdapter.notifyDataSetChanged();
        });


        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchKey = charSequence.toString().toLowerCase();
                adaptertratment.getFilterList(searchKey);
                if (searchKey.length() > 0) {
                    hideLayout.setVisibility(View.GONE);
                    txtTitlesHide.setVisibility(View.GONE);
                } else {
                    hideLayout.setVisibility(View.VISIBLE);
                    txtTitlesHide.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edSearch.setOnClickListener(v -> {
            hideLayout.setVisibility(View.GONE);
            txtTitlesHide.setVisibility(View.GONE);
        });


        changeSelection(cardTreatment, txtDropDownTreatment, cardDate, txtDropDownCalender, lineTreatment, layoutCalender);

        cardTreatment.setOnClickListener(v -> {
            txtBookAppointment.setVisibility(View.GONE);
            changeSelection(cardTreatment, txtDropDownTreatment, cardDate, txtDropDownCalender, lineTreatment, layoutCalender);
        });

        cardDate.setOnClickListener(v -> {
            txtBookAppointment.setVisibility(View.GONE);
            changeSelection(cardDate, txtDropDownCalender, cardTreatment, txtDropDownTreatment, layoutCalender, lineTreatment);
            strApiDate = formatter.format(Calendar.getInstance().getTime());
            GetTimeSlot(strApiDate, MORNING_KEY);
        });

      /*  txtBookAppointment.setOnClickListener(v -> {
            if (selectedTreatmentList.size() != 0) {
                changeSelection(cardDate, txtDropDownCalender, cardTreatment, txtDropDownTreatment, layoutCalender, lineTreatment);
                txtBookAppointment.setVisibility(View.GONE);
                // txtBookAppointment.setVisibility(View.VISIBLE);
                strApiDate = formatter.format(Calendar.getInstance().getTime());
               // GetTimeSlot(strApiDate, MORNING_KEY);
            } else {
                showSnackBar("Please Select Treatments");
            }
        });*/

        calendar = Calendar.getInstance();
        calendarView1.setDate(calendar.getTimeInMillis());
        calendarView1.setMinDate(calendar.getTimeInMillis());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, 2);
        calendarView1.setMinDate(calendar.getTimeInMillis());
        calendarView1.setMaxDate(calendar1.getTimeInMillis());
        calendarView1.setDate(calendar.getTimeInMillis());

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        assert bundle != null;
        if (!bundle.isEmpty()) {
            boolean isDoctorModel = bundle.containsKey(DoctorHospitalPackagesListActivity.KEY_DOCTOR_MODEL);
            boolean isHospitalProfile = bundle.containsKey(DetailDoctorActivity.HOSPITAL_PROFILE);
            isKeyDoctor = bundle.containsKey(DetailHospitalActivity.KEY_DOCTOR);
            isKeyHospital = bundle.containsKey(DetailHospitalActivity.KEY_HOSPITAL);

            //-------Hospital Profile Doctors List ---------
            if (isKeyDoctor) {
                doctorList = bundle.getParcelable(DetailHospitalActivity.KEY_DOCTOR);
                txtDoctorName.setText("Choose a Hospital to available services from " + doctorList.getName());
                txtTitle.setText("Book Appointment for " + doctorList.getName());
                did = doctorList.getDid();
                if (isKeyHospital) {
                    hospitalModel = bundle.getParcelable(DetailHospitalActivity.KEY_HOSPITAL);
                    hospitalNameArray = new String[]{hospitalModel.getHospitalName()};
                    ArrayAdapter<String> hospitalSpinnerAdapter = new ArrayAdapter<>(context,
                            R.layout.family_spinner_row, R.id.tvCategory, hospitalNameArray);
                    spinnerHospital.setAdapter(hospitalSpinnerAdapter);
                    hid = String.valueOf(hospitalModel.getId());
                    GetTreatmentList();
                }
            }
            //-------Doctor Profile Hospital List ---------
            else if (isHospitalProfile) {
                //hid remaining
                hospitalProfile = bundle.getParcelable(DetailDoctorActivity.HOSPITAL_PROFILE);
                hospitalNameArray = new String[]{hospitalProfile.getHospname()};
                ArrayAdapter<String> hospitalSpinnerAdapter = new ArrayAdapter<>(context,
                        R.layout.family_spinner_row, R.id.tvCategory, hospitalNameArray);
                spinnerHospital.setAdapter(hospitalSpinnerAdapter);
                if (isDoctorModel) {
                    doctorModel = bundle.getParcelable(DoctorHospitalPackagesListActivity.KEY_DOCTOR_MODEL);
                    Log.e("aaaaaaaaaa", String.valueOf(doctorModel));
                    did = String.valueOf(doctorModel.getId());
                    txtDoctorName.setText("Choose a Hospital to available services from " + doctorModel.getName());
                    txtTitle.setText("Book Appointment for " + doctorModel.getName());
                    //   txtDistance.setText(String.valueOf(doctorModel.getDistance()) + " km");
                    hid = hospitalProfile.getHid();
                    GetTreatmentList();
                }
            }
            //-------Doctor Book Appointment-------
            else if (isDoctorModel) {
                doctorModel = bundle.getParcelable(DoctorHospitalPackagesListActivity.KEY_DOCTOR_MODEL);
                did = String.valueOf(doctorModel.getId());
                assert doctorModel != null;
                GlobalVar.errorLog(TAG, DoctorHospitalPackagesListActivity.KEY_DOCTOR_MODEL, doctorModel.toString());
                txtDoctorName.setText("Choose a Hospital to available services from " + doctorModel.getName());
                txtTitle.setText("Book Appointment for " + doctorModel.getName());
                //   txtDistance.setText(String.valueOf(doctorModel.getDistance()) + " km");
                int counter = 0;
                ArrayList<Clinicname> clinicnames = doctorModel.getClinicname();
                hospitalNameArray = new String[clinicnames.size()];
                for (Clinicname clinicname : clinicnames) {
                    hospitalNameArray[counter] = clinicname.getClinicname();
                    counter++;
                }
                ArrayAdapter<String> hospitalSpinnerAdapter = new ArrayAdapter<>(context,
                        R.layout.family_spinner_row, R.id.tvCategory, hospitalNameArray);
                spinnerHospital.setAdapter(hospitalSpinnerAdapter);
                spinnerHospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        hid = clinicnames.get(i).getHid();
                        hospitalName = clinicnames.get(i).getClinicname();
                        GetTreatmentList();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }

        calendarView1.setOnDateChangeListener((CalendarView calendarView, int i, int i1, int i2) -> {
            txtBookAppointment.setVisibility(View.GONE);
            i1 = i1 + 1;
            strApiDate = (i2 < 10 ? ("0" + i2) : (i2)) + "-" + (i1 < 10 ? ("0" + i1) : (i1)) + "-" + i;
            progressBar.setVisibility(View.VISIBLE);
            scrollToView(calendarView1, progressBar);
            GetTimeSlot(strApiDate, MORNING_KEY);
        });

        txtAfternoon.setOnClickListener(view -> changeColorTime(txtAfternoon, txtMorning, txtEvening,
                AFTERNOON_KEY));
        txtMorning.setOnClickListener(view -> changeColorTime(txtMorning, txtAfternoon, txtEvening,
                MORNING_KEY));
        txtEvening.setOnClickListener(view -> changeColorTime(txtEvening, txtMorning, txtAfternoon,
                EVENING_KEY));

        txtBookAppointment.setOnClickListener(v -> {
            String strBookingDate = strApiDate;
            String strBookingTime = strTimeApi;

            if (selectedTreatmentList.size() == 0) {
                DoctorAppointmentBookingActivity.this.showSnackBar("Please Select Treatments");
            } else if (strBookingDate.isEmpty()) {
                DoctorAppointmentBookingActivity.this.showSnackBar("Please Select Date");
            } else if (strBookingTime.isEmpty()) {
                DoctorAppointmentBookingActivity.this.showSnackBar("Please Select Time");
            } else {
                Intent mIntent = new Intent(context, BookingDetailsActivity.class);
                mIntent.putExtra(APPOINTMENT_DATE_KEY, strBookingDate);
                mIntent.putExtra(APPOINTMENT_TIME_KEY, strBookingTime);
                mIntent.putExtra(TOTAL_PAY_KEY, totalPay);
                mIntent.putExtra(HID_KEY, hid);
                mIntent.putExtra(DID_KEY, did);
                mIntent.putParcelableArrayListExtra(SELECTED_TREATMENT_LIST_KEY, (ArrayList<? extends Parcelable>)
                        selectedTreatmentList);
                startActivity(mIntent);
            }
        });
    }

    private List<TimeSlot> timeList = new ArrayList<>();
    private List<TimeSlot> timeListMorning = new ArrayList<>();
    private List<TimeSlot> timeListBooked = new ArrayList<>();
    private List<TimeSlot> timeListEvening = new ArrayList<>();
    private List<TimeSlot> timeListAfternoon = new ArrayList<>();
    private TimeSlotAdapter adapterTimeSlot;
    Gson gson = new Gson();
    LinearLayout lineTypeTime;

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
        if (connectionDector.isConnectingToInternet()) {
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

                            //BookedDates

                            JSONArray jsonArrayBooked = response.getJSONArray("BookedDates");

                            if (jsonArrayBooked != null && jsonArrayBooked.length() > 0) {
                                for (int i = 0; i < jsonArrayBooked.length(); i++) {
                                    JSONObject json = jsonArrayBooked.getJSONObject(i);
                                    TimeSlot pojoBook = gson.fromJson(json.toString(), TimeSlot.class);
                                    GlobalVar.errorLog(TAG, "booked", pojoBook.toString());
                                    timeListBooked.add(pojoBook);
                                }
                            }

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

                            txtDropDownCalender.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

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
                                showSnackBar("Time slot not available for " + strApiDate + ". Please choose another date ");
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

    private void GetTreatmentList() {

        if (connectionDector.isConnectingToInternet()) {
            totalPay = 0;
            txtBookAppointment.setText("Pay Rs. " + GlobalVar.priceWithoutDecimal(totalPay));
            selectedTreatmentList.clear();
            selectedTreatmentAdapter.notifyDataSetChanged();

            String url = GlobalVar.ServerAddress + "AndroidNew/GetHospChargesListNew";
            GlobalVar.showProgressDialog(this, "Loading....", false);
            JSONObject obj = new JSONObject();
            try {
                obj.put("hid", hid);
                obj.put("did", did);
                obj.put("tid", 0);
                obj.put("uid", pref.getUID());
                GlobalVar.errorLog(TAG, "url", url);
                GlobalVar.errorLog(TAG, "obj", obj.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                    url, obj,
                    response -> {
                        GlobalVar.errorLog(TAG, "GetTreatmentList", response.toString());
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
                                adaptertratment.notifyDataSetChanged();

                                JSONObject jsonObjectlatlong = response.getJSONObject("latlong");
                                if (jsonObjectlatlong != null) {
//                                    doctorModel.setDistance(GlobalVar.distance(jsonObjectlatlong.getDouble("latitude"), jsonObjectlatlong.getDouble("longitude")));
//                                    txtDistance.setText(String.valueOf(doctorModel.getDistance()) + " km");

                                }

                                txtDistance.setOnClickListener(v -> {

                                    String strUri = null;
                                    try {
                                        strUri = "http://maps.google.com/maps?q=loc:"
                                                + jsonObjectlatlong.getDouble("latitude") + ","
                                                + jsonObjectlatlong.getDouble("longitude") +
                                                " (" + hospitalName + ")";

                                        try {
                                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                    Uri.parse(strUri));
                                            intent.setComponent(new ComponentName("com.google.android.apps.maps",
                                                    "com.google.android.maps.MapsActivity"));
                                            context.startActivity(intent);
                                        } catch (ActivityNotFoundException e) {
                                            try {
                                                context.startActivity(new Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse("market://details?id=com.google.android.apps.maps")));
                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                context.startActivity(new Intent(
                                                        Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                                            }
                                            e.printStackTrace();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });
                            } else {
                                startActivityForResult(new Intent(context, NoDataAvailableActivity.class), GlobalVar.NO_DATA_AVAILABLE);
                                overridePendingTransitionEnter();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                GlobalVar.hideProgressDialog();
                showSnackBar(VolleyErrorHelper.getMessage(error, context));
            });
            jsonRequest.setTag("get");
            mQueue.add(jsonRequest);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
           /* Intent intent = getIntent();
            startActivity(intent);
            finish();*/
            GetTreatmentList();
        } else if (requestCode == GlobalVar.NO_DATA_AVAILABLE) {
            //  GetTreatmentList();
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        overridePendingTransitionExit();
        super.onBackPressed();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    private void showSnackBarGreen(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.green);
    }


    public void changeSelection(CardView selectedCard, TextView selectedTxt, CardView
            unSelectedCard, TextView unSelectedTxt, LinearLayout selectedLine, LinearLayout unSelectedLine) {
        selectedCard.setCardBackgroundColor(context.getResources().getColor(R.color.accent));
        selectedLine.setVisibility(View.VISIBLE);

        unSelectedCard.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        unSelectedLine.setVisibility(View.GONE);

        //ic_verified
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
            showSnackBar(strTimeApiType + " Time slot not available");
        }
        scrollToView(calendarView1, recycleTimeSlot);
        adapterTimeSlot.notifyDataSetChanged();
    }
}