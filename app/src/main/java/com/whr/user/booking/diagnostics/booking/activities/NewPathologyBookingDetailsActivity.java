package com.whr.user.booking.diagnostics.booking.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.booking.diagnostics.booking.model.ThyroCarePojo;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.booking.diagnostics.booking.model.Suggested_TreatementPojo;
import com.whr.user.pojo.FamilyMemberListpojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class NewPathologyBookingDetailsActivity extends AppCompatActivity {
    private Spinner spinnerFamilyMember;
    private LinearLayout layoutDateOfBirth, layoytPatient, layoutPatientOne;
    private EditText etxYourName, etxYourMobileNumber, etxYourDateOfBirth, etxpatientName, etxpatientMobileNumber, etxPatientDateOfBirth;
    private TextView txtDone;
    private View viewFirst, viewSecond;
    Context context;
    private PreferenceUtils pref;
    String TAG = getClass().getSimpleName();
    private List<FamilyMemberListpojo> familyMemberList;

    ArrayList<Suggested_TreatementPojo> pathologyTreatmentList;
    ArrayList<ThyroCarePojo> thyrocareTreatmentList;
    private String pathologyName = "", pathid = "";
    double total;
    RadioGroup radioGroup;
    private RadioButton radioMale, radioFemale;
    private EditText etxpatientAge, etxpatientPincode, etxPatientAddress, etxEmailId;
    private String gender = "M";
    private int date, month, year;


    private CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    String strAppointmentDate = "", strAppointmentTime = "";
    String strFamilyType = "";
    boolean isShowProgressDialog = true;

    FamilyMemberListpojo familyMemberListpojoUpdate = new FamilyMemberListpojo();
    public static final String APPOINTMENT_FAMILY_KEY = "appointment_for";
    ConnectionDector dector;
    String pathAddressId = "",pathAddresss="";
    LinearLayout layoutForKrsnna;
    Spinner spinnerCollction,spinnerAddressPathology,spinnerGender;
    String collectionType="0",genderType="",age="";

    @Override
    public void onBackPressed() {
        overridePendingTransitionExit();
        super.onBackPressed();
    }

    private void dialogGetDetails() {
        final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.pathology_booking_confirmation_dialog);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        TextView txtConfirm = dialog.findViewById(R.id.txtConfirm);
        TextView txtPatientName = dialog.findViewById(R.id.txtPatientName);
        TextView txtPatientMobileNumber = dialog.findViewById(R.id.txtPatientMobileNumber);
        TextView txtPatientDob = dialog.findViewById(R.id.txtPatientDob);
        TextView txtSelectedTests = dialog.findViewById(R.id.txtSelectedTests);
        TextView txtAppointmentDate = dialog.findViewById(R.id.txtAppointmentDate);
        TextView txtBookingDate = dialog.findViewById(R.id.txtBookingDate);
        TextView txtBookedBy = dialog.findViewById(R.id.txtBookedBy);
        TextView txtAppointmentTIme = dialog.findViewById(R.id.txtAppointmentTIme);
        TextView txtGender = dialog.findViewById(R.id.txtGender);
        TextView txtAge = dialog.findViewById(R.id.txtAge);
        TextView txAmount = dialog.findViewById(R.id.txAmount);

        LinearLayout layoutGender=dialog.findViewById(R.id.layoutGender);
        LinearLayout layoutAge=dialog.findViewById(R.id.layoutAge);
        LinearLayout layoutTIme=dialog.findViewById(R.id.layoutTIme);

        txtBookingDate.setText(dateFormat.format(new Date()));
        txtPatientName.setText(familyMemberListpojoUpdate.getFamilyName());
        txtPatientMobileNumber.setText(familyMemberListpojoUpdate.getCno());
        txtPatientDob.setText(familyMemberListpojoUpdate.getDob());
        txtAppointmentDate.setText(strAppointmentDate);
        txtAppointmentTIme.setText(strAppointmentTime);
        txtGender.setText(genderType);
        txAmount.setText(String.valueOf(total));
        txtAge.setText(age);

        if (pathid.equals("50189")){
            layoutGender.setVisibility(View.VISIBLE);
            layoutAge.setVisibility(View.VISIBLE);
            layoutTIme.setVisibility(View.VISIBLE);
        }else {
            layoutGender.setVisibility(View.GONE);
            layoutAge.setVisibility(View.GONE);
            layoutTIme.setVisibility(View.GONE);
        }

        String strSelectedTreatmentList = "";
        if (pathid.contains("50017")) {
            for (ThyroCarePojo d : thyrocareTreatmentList) {
                if (thyrocareTreatmentList.isEmpty()) {
                    strSelectedTreatmentList = d.getName();
                } else {
                    strSelectedTreatmentList += ", " + d.getName();
                }
            }

        } else {
            for (Suggested_TreatementPojo d : pathologyTreatmentList) {
                if (pathologyTreatmentList.isEmpty()) {
                    strSelectedTreatmentList = d.getTname();
                } else {
                    strSelectedTreatmentList += ", " + d.getTname();
                }
            }

        }

        txtSelectedTests.setText(strSelectedTreatmentList);

        //txtBookedBy.setText(pref.getUserName());
        txtBookedBy.setText(etxYourName.getText().toString());


        txtConfirm.setOnClickListener((View v1) -> {
            if (pathid.contains("50017")) {
                if (etxpatientAge.getText().toString().isEmpty()) {
                    showSnackBar("Enter Your Age");
                } else if (etxpatientPincode.getText().toString().isEmpty()) {
                    showSnackBar("Enter Your Pincode");
                } else if (etxPatientAddress.getText().toString().isEmpty()) {
                    showSnackBar("Enter Your Address");
                } else if (etxEmailId.getText().toString().isEmpty()) {
                    showSnackBar("Enter Your Email Id");
                } else {

                    Intent intent = new Intent(context, PaymentThyrocareActivity.class);
                    intent.putParcelableArrayListExtra("pathologyTreatmentList", (ArrayList<? extends Parcelable>) thyrocareTreatmentList);
                    intent.putExtra("pathologyName", pathologyName);
                    intent.putExtra("pathid", pathid);
                    intent.putExtra("total", total);
                    intent.putExtra("strdate", strAppointmentTime);
                    intent.putExtra("patientId", txtPatientMobileNumber.getText().toString());
                    intent.putExtra("patientName", txtPatientName.getText().toString());
                    intent.putExtra("gender", gender);
                    intent.putExtra("pincode", etxpatientPincode.getText().toString());
                    intent.putExtra("address", etxPatientAddress.getText().toString());
                    intent.putExtra("email", etxEmailId.getText().toString());
                    intent.putExtra("age", etxpatientAge.getText().toString());
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(context, PaymentPathologyActivity.class);
                intent.putParcelableArrayListExtra("pathologyTreatmentList", pathologyTreatmentList);
                intent.putExtra("pathologyName", pathologyName);
                intent.putExtra("pathid", pathid);
                intent.putExtra("total", total);
                intent.putExtra("strdate", strAppointmentDate);
                intent.putExtra("strTime", strAppointmentTime);
                intent.putExtra("patientId", txtPatientMobileNumber.getText().toString());
                intent.putExtra("pathAddressId", pathAddressId);
                intent.putExtra("age", age);
                intent.putExtra("gender", genderType);
                intent.putExtra("collectionAddress", pathAddresss);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    Gson gson = new Gson();

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    int totalPay = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pathology_booking_details);
        overridePendingTransitionEnter();
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        context = NewPathologyBookingDetailsActivity.this;
        pref = new PreferenceUtils(context);
        dector = new ConnectionDector(context);

        familyMemberList = new ArrayList<>();
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        coordinatorLayout = findViewById(R.id.layoutbookingDetails);
        spinnerFamilyMember = findViewById(R.id.spinnerFamilyMember);
        layoutDateOfBirth = findViewById(R.id.layoytDateOfBirth);

        /*TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Booking Details");
        TextView txtBookingFor = findViewById(R.id.txtBookingFor);
        txtBookingFor.setText("Booking For");*/

        etxYourName = findViewById(R.id.etxYourName);
        etxYourMobileNumber = findViewById(R.id.etxYourMobileNumber);
        etxYourDateOfBirth = findViewById(R.id.etxYourDateOfBirth);
        etxpatientName = findViewById(R.id.etxpatientName);
        etxpatientMobileNumber = findViewById(R.id.etxpatientMobileNumber);
        etxPatientDateOfBirth = findViewById(R.id.etxPatientDateOfBirth);
        viewFirst = findViewById(R.id.firstView);
        viewSecond = findViewById(R.id.secondView);
        layoytPatient = findViewById(R.id.layoutPatient);
        layoutPatientOne = findViewById(R.id.layoutPatientOne);
        txtDone = findViewById(R.id.txtDone);
        etxYourName.setText(pref.getUserName());
        etxYourMobileNumber.setText(pref.getCno());

        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        radioGroup = findViewById(R.id.radioGroup);
        etxpatientAge = findViewById(R.id.etxpatientAge);
        etxpatientPincode = findViewById(R.id.etxpatientPincode);
        etxPatientAddress = findViewById(R.id.etxPatientAddress);
        etxEmailId = findViewById(R.id.etxEmailId);

        layoutForKrsnna = findViewById(R.id.layoutForKrsnna);
        spinnerCollction = findViewById(R.id.spinnerCollction);
        spinnerAddressPathology = findViewById(R.id.spinnerAddressPathology);
        spinnerGender = findViewById(R.id.spinnerGender);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            pathologyName = (String) bundle.get("pathologyName");
            pathologyTreatmentList = bundle.getParcelableArrayList("pathologyTreatmentList");
            thyrocareTreatmentList = bundle.getParcelableArrayList("pathologyTreatmentList");
            pathid = (String) bundle.get("pathid");
            total = ((double) bundle.get("total"));
            strAppointmentDate = bundle.getString("strdate");
            strAppointmentTime = bundle.getString("strTime");

        }
        if (pathid.contains("50017")) {
            layoutPatientOne.setVisibility(View.VISIBLE);
        } else {
            layoutPatientOne.setVisibility(View.GONE);
        }
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (radioMale.isChecked()) {
                gender = "M";
            } else {
                gender = "F";
            }
        });

        txtDone.setOnClickListener(v -> {
            if (strFamilyType.equalsIgnoreCase("myself")) {
                String name = etxYourName.getText().toString().replace("MySelf", "").replace("(", "").replace(")", "");
                familyMemberListpojoUpdate.setFamilyName(name.trim());
                familyMemberListpojoUpdate.setCno(etxYourMobileNumber.getText().toString());
                familyMemberListpojoUpdate.setDob(etxYourDateOfBirth.getText().toString());
            } else if (strFamilyType.equalsIgnoreCase("family")) {
                String name = etxpatientName.getText().toString();
                familyMemberListpojoUpdate.setFamilyName(name);
                familyMemberListpojoUpdate.setCno(etxpatientMobileNumber.getText().toString());
                familyMemberListpojoUpdate.setDob(etxPatientDateOfBirth.getText().toString());
            } else {//other
                familyMemberListpojoUpdate.setFamilyName(etxpatientName.getText().toString());
                familyMemberListpojoUpdate.setCno(etxpatientMobileNumber.getText().toString());
                familyMemberListpojoUpdate.setFamilyId(Long.parseLong(etxpatientMobileNumber.getText().toString()));
                familyMemberListpojoUpdate.setDob(etxPatientDateOfBirth.getText().toString());
            }

            if (pathid.contains("50017")) {
                if (etxpatientAge.getText().toString().isEmpty()) {
                    showSnackBar("Enter Your Age");
                } else if (etxpatientPincode.getText().toString().isEmpty()) {
                    showSnackBar("Enter Your pincode");
                } else if (etxPatientAddress.getText().toString().isEmpty()) {
                    showSnackBar("Enter your Address");
                } else if (etxEmailId.getText().toString().isEmpty()) {
                    showSnackBar("Enter your Email");
                } else {
                    updateFamilyMember();
                }
            } else if (familyMemberListpojoUpdate.getFamilyName().isEmpty()) {
                showSnackBar("patient name empty");
            } else if (familyMemberListpojoUpdate.getDob().isEmpty()) {
                showSnackBar("patient dob empty");
            } else if (familyMemberListpojoUpdate.getCno().isEmpty()) {
                showSnackBar("patient mobile number empty");
            } else {
                updateFamilyMember();
            }

        });

        Calendar today = Calendar.getInstance();
        etxYourDateOfBirth.setOnClickListener(view -> {
            StringBuilder strBuild = new StringBuilder();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            final DatePickerDialog dp = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog, (view1, year, monthOfYear, dayOfMonth) -> {
                DecimalFormat formatter = new DecimalFormat("00");
                today.set(year, monthOfYear, dayOfMonth);
                long selectedTimeInMillis = today.getTimeInMillis();
                long systemTimeInMillis = Calendar.getInstance().getTimeInMillis();

                if (selectedTimeInMillis <= systemTimeInMillis) {
                    etxYourDateOfBirth.setText(formatter.format(dayOfMonth) + "/" + formatter.format(monthOfYear + 1) + "/" + year);

                    int ag = Calendar.getInstance().get(Calendar.YEAR)- year;
                    age=String.valueOf(ag);
                } else {
                    Toast.makeText(context, getString(R.string.Cannotselectfuturedate), Toast.LENGTH_SHORT).show();
                }

            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            dp.getDatePicker().setMaxDate(today.getTimeInMillis());
            dp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            dp.show();

        });

        etxPatientDateOfBirth.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            final DatePickerDialog dp = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog, (view1, year, monthOfYear, dayOfMonth) -> {
                DecimalFormat formatter = new DecimalFormat("00");
                today.set(year, monthOfYear, dayOfMonth);
                long selectedTimeInMillis = today.getTimeInMillis();
                long systemTimeInMillis = Calendar.getInstance().getTimeInMillis();

                if (selectedTimeInMillis <= systemTimeInMillis) {
                    etxPatientDateOfBirth.setText(formatter.format(dayOfMonth) + "/" + formatter.format(monthOfYear + 1) + "/" + year);

                    int ag = Calendar.getInstance().get(Calendar.YEAR)- year;
                    age=String.valueOf(ag);
                } else {
                    Toast.makeText(context, getString(R.string.Cannotselectfuturedate), Toast.LENGTH_SHORT).show();
                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            dp.getDatePicker().setMaxDate(today.getTimeInMillis());
            dp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dp.show();

        });


        if (dector.isConnectingToInternet()) {
            getFamilyMemberList();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }


        if (pathid.contains("50189")) {
            layoutForKrsnna.setVisibility(View.VISIBLE);
        } else {
            layoutForKrsnna.setVisibility(View.GONE);
        }

        String[] addresses = {"Free Home Collection","Diagnostic Center"};
        ArrayAdapter aa = new ArrayAdapter<>(this, R.layout.family_spinner_row, R.id.tvCategory, addresses);
        spinnerCollction.setAdapter(aa);


        spinnerCollction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    collectionType="1";
                }else {
                    collectionType="0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] gender = {"Male","Female","Other"};
        ArrayAdapter bb = new ArrayAdapter<>(this, R.layout.family_spinner_row, R.id.tvCategory, gender);
        spinnerGender.setAdapter(bb);


        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    genderType="Male";
                }else if (position==1){
                    genderType="Female";
                }else{
                    genderType="Other";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getPathologyAddress();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getFamilyMemberList();
        }
    }

    ArrayAdapter<FamilyMemberListpojo> familyMemberAdapter;

    private void updateFamilyMember() {
        String url = GlobalVar.ServerAddress + "AndroidNew/UpdateUserNewChangeV1";
        //String url = GlobalVar.ServerAddress + "AndroidNew/UpdateUserNewChange";
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("cno", familyMemberListpojoUpdate.getCno());
            obj.put("dob", familyMemberListpojoUpdate.getDob());
            obj.put("name", familyMemberListpojoUpdate.getFamilyName().trim());
            obj.put("type", strFamilyType);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.showProgressDialog(context, "", true);

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    Log.e(TAG + "updateFamilyMember", response.toString());
                    GlobalVar.hideProgressDialog();
                    try {
                        String result = response.getString("result").trim().toLowerCase();
                        if (result.equalsIgnoreCase("true".trim().toLowerCase())) {
                            //pref.setUserName(familyMemberListpojoUpdate.getFamilyName());
                            getFamilyMemberList2();
                            dialogGetDetails();
                        } else if (result.equalsIgnoreCase("User Allready Exist.".trim().toLowerCase())) {
                            showSnackBar("User Already Exist.");
                        } else {
                            showSnackBar("Please Try Again");
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

    private void getFamilyMemberList() {
        String url = GlobalVar.ServerAddress + "AndroidNew/MyFamilyNew";
        JSONObject obj = new JSONObject();
        GlobalVar.showProgressDialog(context, "", true);
        try {
            obj.put("uid", pref.getUID());
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    Log.e("getFamilyMemberList", response.toString());

                    GlobalVar.hideProgressDialog();
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("result");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            familyMemberList.clear();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                FamilyMemberListpojo model = gson.fromJson(json.toString(), FamilyMemberListpojo.class);
                                if (i == 0) {
                                    model.setFamilyName(model.getFamilyName() + " (MySelf)");
                                }
                                familyMemberList.add(model);
                            }

                            //EXTRA OBJECT FOR SELECT MEMBER
                            FamilyMemberListpojo familyMemberListpojoSelect = new FamilyMemberListpojo();
                            familyMemberListpojoSelect.setCno("0000000000");
                            familyMemberListpojoSelect.setFamilyId(-1);
                            familyMemberListpojoSelect.setFamilyName("other");
                            familyMemberList.add(familyMemberListpojoSelect);

                            familyMemberAdapter = new ArrayAdapter<>(context, R.layout.family_spinner_row, R.id.tvCategory, familyMemberList);
                            spinnerFamilyMember.setAdapter(familyMemberAdapter);

                            spinnerFamilyMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                                    familyMemberListpojoUpdate = (FamilyMemberListpojo)
                                            adapterView.getItemAtPosition(position);

                                    Log.e(TAG, familyMemberListpojoUpdate.toString());

                                    if (familyMemberListpojoUpdate.getFamilyName().contains("MySelf")) {
                                        layoutDateOfBirth.setVisibility(View.VISIBLE);
                                        layoytPatient.setVisibility(View.GONE);
                                        txtDone.setVisibility(View.VISIBLE);

                                        etxYourName.setText(familyMemberListpojoUpdate.getFamilyName());
                                        etxYourMobileNumber.setText(familyMemberListpojoUpdate.getCno());
                                        etxYourDateOfBirth.setText(familyMemberListpojoUpdate.getDob());

                                        strFamilyType = "MySelf";
                                    } else if (familyMemberListpojoUpdate.getFamilyName().equalsIgnoreCase("other")) {
                                        layoutDateOfBirth.setVisibility(View.GONE);
                                        viewSecond.setVisibility(View.VISIBLE);
                                        layoytPatient.setVisibility(View.VISIBLE);
                                        txtDone.setVisibility(View.VISIBLE);

                                        etxYourName.setText(pref.getUserName());
                                        etxYourMobileNumber.setText(pref.getCno());
                                        etxYourDateOfBirth.setText("");

                                        etxpatientName.setText("");
                                        etxpatientMobileNumber.setText("");
                                        etxPatientDateOfBirth.setText("");

                                        strFamilyType = "other";

                                    } else {
                                        layoutDateOfBirth.setVisibility(View.GONE);
                                        viewFirst.setVisibility(View.VISIBLE);
                                        viewSecond.setVisibility(View.GONE);
                                        layoytPatient.setVisibility(View.VISIBLE);
                                        txtDone.setVisibility(View.VISIBLE);

                                        etxYourName.setText(pref.getUserName());
                                        etxYourMobileNumber.setText(pref.getCno());
                                        etxYourDateOfBirth.setText("");

                                        etxpatientName.setText(familyMemberListpojoUpdate.getFamilyName());
                                        etxPatientDateOfBirth.setText(familyMemberListpojoUpdate.getDob());
                                        etxpatientMobileNumber.setText(familyMemberListpojoUpdate.getCno());

                                        strFamilyType = "Family";
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
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

    private void getFamilyMemberList2() {
        String url = GlobalVar.ServerAddress + "AndroidNew/MyFamilyNew";

        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    Log.e("getFamilyMemberList", response.toString());

                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("result");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            familyMemberList.clear();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                FamilyMemberListpojo model = gson.fromJson(json.toString(), FamilyMemberListpojo.class);
                                if (i == 0) {
                                    model.setFamilyName(model.getFamilyName() + " (MySelf)");
                                }
                                familyMemberList.add(model);
                            }

                            //EXTRA OBJECT FOR SELECT MEMBER
                            FamilyMemberListpojo familyMemberListpojoSelect = new FamilyMemberListpojo();
                            familyMemberListpojoSelect.setCno("0000000000");
                            familyMemberListpojoSelect.setFamilyId(-1);
                            familyMemberListpojoSelect.setFamilyName("other");
                            familyMemberList.add(familyMemberListpojoSelect);
                            familyMemberAdapter.notifyDataSetChanged();

                            GlobalVar.errorLog(TAG, "getFamilyMemberList2_familyMemberListpojoUpdate", familyMemberListpojoUpdate.toString1());


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }


    ArrayAdapter<PathAddress> pathAddressAdapter;
    double lat = 0.0, lng = 0.0;

    private void getPathologyAddress() {
        String url = GlobalVar.ServerAddress + "AndroidNew/Pathologyaddress";


        JSONObject obj = new JSONObject();
        try {
            obj.put("pathId", pathid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "getPathologyAddress", response.toString());
                    pathAddresses.clear();
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("Address");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                              PathAddress pojo = gson.fromJson(json.toString(), PathAddress.class);
                                pathAddresses.add(pojo);
                            }

                            pathAddressAdapter = new ArrayAdapter<>(context, R.layout.family_spinner_row, R.id.tvCategory, pathAddresses);
                            spinnerAddressPathology.setAdapter(pathAddressAdapter);

                            spinnerAddressPathology.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    PathAddress pathAddress = ((PathAddress) parent.getItemAtPosition(position));
                                    pathAddressId = pathAddress.getAddressId();
                                    pathAddresss = pathAddress.getAddress();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            showSnackBar(VolleyErrorHelper.getMessage(error, context));
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    ArrayList<PathAddress> pathAddresses = new ArrayList<>();

    class PathAddress {
        @Override
        public String toString() {
            return Address;
        }

        String pathid = "";
        String AddressId = "";
        String Address = "";
        double lattitude = 0.0;
        double longitude = 0.0;

        public double getLattitude() {
            return lattitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getPathid() {
            return pathid;
        }

        public void setPathid(String pathid) {
            this.pathid = pathid;
        }

        public String getAddressId() {
            return AddressId;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }
    }



    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    private void showSnackBarGreen(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.green);
    }

}