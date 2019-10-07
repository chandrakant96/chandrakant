package com.whr.user.booking.doctor.booking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.whr.user.booking.PaymentPackageActivity;
import com.whr.user.booking.doctor.booking.models.PackagesDetails;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingDetailsPackageActivity extends AppCompatActivity {
    private Spinner spinnerFamilyMember;
    private LinearLayout layoutDateOfBirth, layoytPatient;
    private EditText etxYourName, etxYourMobileNumber, etxpatientName, etxpatientMobileNumber;
    private TextView txtDone, etxPatientDateOfBirth, etxYourDateOfBirth;
    private View viewFirst, viewSecond;
    Context context;
    private PreferenceUtils pref;
    String TAG = getClass().getSimpleName();
    private List<FamilyMemberListpojo> familyMemberList;
    int totalPay = 0;

    private CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    String strAppointmentDate = "";
    String strFamilyType = "";
    boolean isShowProgressDialog = true;
    PackagesDetails packagesDetails;
    FamilyMemberListpojo familyMemberListpojoUpdate = new FamilyMemberListpojo();
    public static final String APPOINTMENT_FAMILY_KEY = "appointment_for";

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
        dialog.setContentView(R.layout.booking_confirmation_activity);
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

        txtBookingDate.setText(dateFormat.format(new Date()));
        txtPatientName.setText(familyMemberListpojoUpdate.getFamilyName());
        txtPatientMobileNumber.setText(familyMemberListpojoUpdate.getCno());
        txtPatientDob.setText(familyMemberListpojoUpdate.getDob());
        txtAppointmentDate.setText(strAppointmentDate);
        txtSelectedTests.setText(packagesDetails.getPackageName());
        txtBookedBy.setText(pref.getUserName());


        txtConfirm.setOnClickListener((View v1) -> {
            Intent intent = new Intent(context, PaymentPackageActivity.class);
            intent.putExtra(DoctorAppointmentBookingActivity.APPOINTMENT_DATE_KEY, strAppointmentDate);
            intent.putExtra(APPOINTMENT_FAMILY_KEY, familyMemberListpojoUpdate);
            intent.putExtra(HospitalPackageDetailsActivity.HOSPITAL_PACKAGE_DETAILS_KEY, packagesDetails);
            startActivity(intent);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        overridePendingTransitionEnter();
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        context = BookingDetailsPackageActivity.this;
        pref = new PreferenceUtils(context);
        familyMemberList = new ArrayList<>();
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        coordinatorLayout = findViewById(R.id.layoutbookingDetails);
        spinnerFamilyMember = findViewById(R.id.spinnerFamilyMember);
        layoutDateOfBirth = findViewById(R.id.layoytDateOfBirth);

        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Booking Details");
        TextView txtBookingFor = findViewById(R.id.txtBookingFor);
        txtBookingFor.setText("Booking For");

        etxYourName = findViewById(R.id.etxYourName);
        etxYourMobileNumber = findViewById(R.id.etxYourMobileNumber);
        etxYourDateOfBirth = findViewById(R.id.etxYourDateOfBirth);
        etxpatientName = findViewById(R.id.etxpatientName);
        etxpatientMobileNumber = findViewById(R.id.etxpatientMobileNumber);
        etxPatientDateOfBirth = findViewById(R.id.etxPatientDateOfBirth);
        viewFirst = findViewById(R.id.firstView);
        viewSecond = findViewById(R.id.secondView);
        layoytPatient = findViewById(R.id.layoutPatient);
        txtDone = findViewById(R.id.txtDone);
        etxYourName.setText(pref.getUserName());
        etxYourMobileNumber.setText(pref.getCno());

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean isAppointmentDate = bundle.containsKey(HospitalPackagesCalenderActivity.APPOINTMENT_DATE_KEY);
            boolean isTotalPay = bundle.containsKey(DoctorAppointmentBookingActivity.TOTAL_PAY_KEY);

            if (isTotalPay) {
                totalPay = bundle.getInt(DoctorAppointmentBookingActivity.TOTAL_PAY_KEY, 0);
                Log.e(TAG, "total :" + totalPay);
            }

            if (isAppointmentDate) {
                strAppointmentDate = bundle.getString(HospitalPackagesCalenderActivity.APPOINTMENT_DATE_KEY);
                GlobalVar.errorLog(TAG, "strAppointmentDate", strAppointmentDate);
            }

            if (intentExtra.getExtras().containsKey(HospitalPackageDetailsActivity.HOSPITAL_PACKAGE_DETAILS_KEY)) {
                packagesDetails = intentExtra.getParcelableExtra(HospitalPackageDetailsActivity.HOSPITAL_PACKAGE_DETAILS_KEY);
                GlobalVar.errorLog(TAG, "packageDetails", packagesDetails.toString());
            }
        }

        txtDone.setOnClickListener(v -> {

            if (strFamilyType.equalsIgnoreCase("myself")) {
                String name = etxYourName.getText().toString().replace("MySelf", "")
                        .replace("(", "").replace(")", "");
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

            if (familyMemberListpojoUpdate.getFamilyName().isEmpty()) {
                showSnackBar("patient name empty");
            } else if (familyMemberListpojoUpdate.getDob().isEmpty()) {
                showSnackBar("patient dob empty");
            } else if (familyMemberListpojoUpdate.getCno().isEmpty()) {
                showSnackBar("patient mobile number empty");
            } else {
                if (new ConnectionDector(context).isConnectingToInternet()) {
                    updateFamilyMember();
                } else {
                    startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
                    overridePendingTransitionExit();
                }
            }
        });

        Calendar today = Calendar.getInstance();

        etxYourDateOfBirth.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            final DatePickerDialog dp = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    DecimalFormat formatter = new DecimalFormat("00");
                    today.set(year, monthOfYear, dayOfMonth);
                    long selectedTimeInMillis = today.getTimeInMillis();
                    long systemTimeInMillis = Calendar.getInstance().getTimeInMillis();

                    if (selectedTimeInMillis <= systemTimeInMillis) {
                        etxYourDateOfBirth.setText(formatter.format(dayOfMonth) + "/" + formatter.format(monthOfYear + 1) + "/" + year);
                    } else {
                        Toast.makeText(context, getString(R.string.Cannotselectfuturedate), Toast.LENGTH_SHORT).show();
                    }
                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            //    today.add(Calendar.YEAR, 5);
            //dp.getDatePicker().setCalendarViewShown(false);
            dp.getDatePicker().setMaxDate(today.getTimeInMillis());
            dp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dp.show();

        });

        etxPatientDateOfBirth.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            final DatePickerDialog dp = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    DecimalFormat formatter = new DecimalFormat("00");
                    today.set(year, monthOfYear, dayOfMonth);
                    long selectedTimeInMillis = today.getTimeInMillis();
                    long systemTimeInMillis = Calendar.getInstance().getTimeInMillis();

                    if (selectedTimeInMillis <= systemTimeInMillis) {
                        etxPatientDateOfBirth.setText(formatter.format(dayOfMonth) + "/" + formatter.format(monthOfYear + 1) + "/" + year);
                    } else {
                        Toast.makeText(context, getString(R.string.Cannotselectfuturedate), Toast.LENGTH_SHORT).show();
                    }
                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            //    today.add(Calendar.YEAR, 5);
            //dp.getDatePicker().setCalendarViewShown(false);
            dp.getDatePicker().setMaxDate(today.getTimeInMillis());
            dp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dp.show();

        });

        if (new ConnectionDector(context).isConnectingToInternet()) {
            getFamilyMemberList();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }

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
            obj.put("uid", familyMemberListpojoUpdate.getFamilyId());
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

                            familyMemberAdapter =
                                    new ArrayAdapter<>(context, R.layout.family_spinner_row,
                                            R.id.tvCategory, familyMemberList);
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

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    private void showSnackBarGreen(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.green);
    }
}