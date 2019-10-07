package com.whr.user.booking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.booking.adapters.AlternativeTreatmentAdapter;
import com.whr.user.booking.models.AlternativeTreatment;
import com.whr.user.booking.doctor.booking.models.DoctorSpecialitiesModel;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.whr.user.booking.doctor.booking.DoctorSpecialtiesActivity.KEY_SPECIALIZATION;

public class FilterActivity extends AppCompatActivity {


    LinearLayout lineDoctorFilter;

    TextView txtApplyFilter;
    String TAG = getClass().getSimpleName();
    private String specializationId = "";
    DoctorSpecialitiesModel doctorSpecialitiesModel;

    RecyclerView recyclerViewPathy;
    ArrayList<AlternativeTreatment> alternativeTreatments = new ArrayList<>();
    Context context;
    AlternativeTreatmentAdapter adapter;
    RequestQueue mQueue;
    ImageView imageClose;
    ProgressBar progressBarTreatment;
    RadioButton radioDoctor, radioHospital, radioFee300, radioFee500, radioAbove500;

    RadioButton radioHigh, radioLow, radioNearBy;

    public static String CONSULTATION_FEE_300 = "0-300";
    public static String CONSULTATION_FEE_500 = "300-500";
    public static String CONSULTATION_FEE_ABOVE_500 = "Above 500";
    public static String USER_LIKE_HIGH_LOW = "High To Low";
    public static String USER_LIKE_LOW_HIGH = "Low To High";
    public static String NEAR_BY = "Near By";
    public static String DOCTOR = "Doctor";
    public static String HOSPITAL = "Hospital";

    public static String strFilterType = "";
    public static String strFilterPathyName = "";
    public static String strFilterConsultation = "";
    public static String strFilterLike = "";
    public static String strFilterNearBy = "";
    public static String strPathyId = "";

    public static ArrayList<String> arrayList = new ArrayList<>();
    TextView txtResetFilter;
    public static String FILTER_APPLY = "filter_apply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        context = this;
        txtResetFilter = findViewById(R.id.txtResetFilter);
        radioNearBy = findViewById(R.id.radioNearBy);
        radioLow = findViewById(R.id.radioLow);
        radioHigh = findViewById(R.id.radioHigh);
        radioAbove500 = findViewById(R.id.radioAbove500);
        radioFee500 = findViewById(R.id.radioFee500);
        radioFee300 = findViewById(R.id.radioFee300);
        radioHospital = findViewById(R.id.radioHospital);
        radioDoctor = findViewById(R.id.radioDoctor);
        imageClose = findViewById(R.id.imageClose);
        txtApplyFilter = findViewById(R.id.txtApplyFilter);
        recyclerViewPathy = findViewById(R.id.recyclerViewPathy);
        lineDoctorFilter = findViewById(R.id.lineDoctorFilter);
        progressBarTreatment = findViewById(R.id.progressBarTreatment);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        alternativeTreatments.clear();
        recyclerViewPathy.setLayoutManager(new GridLayoutManager(context, 2));
        adapter = new AlternativeTreatmentAdapter(context, alternativeTreatments);
        recyclerViewPathy.setAdapter(adapter);
        adapter.setOnClickListener(treatment -> {
            txtApplyFilter.setVisibility(View.VISIBLE);
            strFilterPathyName = treatment.getType();
            if (!treatment.isCheck()) {
                treatment.setCheck(true);
                strPathyId = treatment.getId();
            }
            adapter.notifyDataSetChanged();
            printFilterData();
        });

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        assert bundle != null;
        if (!bundle.isEmpty()) {
            boolean isSpecializationKey = bundle.containsKey(KEY_SPECIALIZATION);
            if (isSpecializationKey) {
                doctorSpecialitiesModel = bundle.getParcelable(KEY_SPECIALIZATION);
                assert doctorSpecialitiesModel != null;
                specializationId = String.valueOf(doctorSpecialitiesModel.getId());
            }
        }


        imageClose.setOnClickListener(view -> {
            alternativeTreatments.clear();
            adapter.notifyDataSetChanged();
            Intent intent = getIntent();
            intent.putExtra(FILTER_APPLY, false);
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransitionExit();
        });

        txtApplyFilter.setOnClickListener(view -> {
            alternativeTreatments.clear();
            adapter.notifyDataSetChanged();
            Intent intent = getIntent();
            arrayList.clear();

            if (radioNearBy.isChecked()) {
                strFilterNearBy = NEAR_BY;
            } else {
                strFilterNearBy = "";
            }

            if (strFilterType.equalsIgnoreCase(DOCTOR)) {
                if (!strFilterType.isEmpty())
                    arrayList.add(strFilterType);

                if (!strFilterPathyName.isEmpty())
                    arrayList.add(strFilterPathyName);

                if (!strFilterConsultation.isEmpty())
                    arrayList.add(strFilterConsultation);

            } else if (strFilterType.equalsIgnoreCase(HOSPITAL)) {
                strFilterConsultation = "";
                strFilterPathyName = "";
                if (!strFilterType.isEmpty())
                    arrayList.add(strFilterType);
            }

            if (!strFilterLike.isEmpty())
                arrayList.add(strFilterLike);

            if (!strFilterNearBy.isEmpty())
                arrayList.add(strFilterNearBy);

            intent.putExtra("FILTER", arrayList);
            intent.putExtra(FILTER_APPLY, true);
            printFilterData();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransitionExit();
        });

        txtResetFilter.setOnClickListener(view -> resetFilterValue());
        setCheckedListener();
        checkPreviousValue();

        radioDoctor.setOnClickListener(view -> {
            radioDoctor.setChecked(true);
            radioHospital.setChecked(false);
            strFilterType = DOCTOR;
            lineDoctorFilter.setVisibility(View.VISIBLE);
            getAlternativeTreatment();
            printFilterData();
        });


        radioHospital.setOnClickListener(view -> {
            radioHospital.setChecked(true);
            radioDoctor.setChecked(false);
            lineDoctorFilter.setVisibility(View.GONE);
            strFilterType = HOSPITAL;
            printFilterData();
        });


        radioHigh.setOnClickListener(view -> {
            radioHigh.setChecked(true);
            radioLow.setChecked(false);
            strFilterLike = USER_LIKE_HIGH_LOW;
            printFilterData();
        });


        radioLow.setOnClickListener(view -> {
            radioLow.setChecked(true);
            radioHigh.setChecked(false);
            strFilterLike = USER_LIKE_LOW_HIGH;
            printFilterData();
        });


        radioFee300.setOnClickListener(view -> {
            radioFee300.setChecked(true);
            radioFee500.setChecked(false);
            radioAbove500.setChecked(false);
            strFilterConsultation = CONSULTATION_FEE_300;
            printFilterData();
        });


        radioFee500.setOnClickListener(view -> {
            radioFee300.setChecked(false);
            radioAbove500.setChecked(false);
            radioFee500.setChecked(true);
            strFilterConsultation = CONSULTATION_FEE_500;
            printFilterData();
        });


        radioAbove500.setOnClickListener(view -> {
            radioFee300.setChecked(false);
            radioFee500.setChecked(false);
            radioAbove500.setChecked(true);
            strFilterConsultation = CONSULTATION_FEE_ABOVE_500;
            printFilterData();
        });


        radioNearBy.setOnClickListener(view -> {
            radioNearBy.setChecked(true);
            strFilterNearBy = NEAR_BY;
            printFilterData();
        });

    }

    public void setCheckedListener() {
        radioDoctor.setChecked(false);
        radioHospital.setChecked(false);
        radioHigh.setChecked(false);
        radioLow.setChecked(false);
        radioFee300.setChecked(false);
        radioFee500.setChecked(false);
        radioNearBy.setChecked(false);
        radioAbove500.setChecked(false);
    }


    Gson gson = new Gson();

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void getAlternativeTreatment() {
        String url = GlobalVar.ServerAddress + "AndroidNew/GetPathy";
        Log.e(TAG, url);
        alternativeTreatments.clear();
        adapter.notifyDataSetChanged();
        progressBarTreatment.setVisibility(View.VISIBLE);
        CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest(Request.Method.GET, url, null, response -> {
            progressBarTreatment.setVisibility(View.GONE);
            try {
                JSONArray jsonArray = response.getJSONArray("pathy");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    AlternativeTreatment treatment = gson.fromJson(jsonObject.toString()
                            , AlternativeTreatment.class);
                    if (treatment.getType().equalsIgnoreCase(strFilterPathyName)) {
                        treatment.setCheck(true);
                    }
                    alternativeTreatments.add(treatment);
                }

                Log.e(TAG, String.valueOf(alternativeTreatments.size()));
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressBarTreatment.setVisibility(View.GONE);
        });
        mQueue.add(customJSONObjectRequest);
    }

    public void checkPreviousValue() {

        Log.e(TAG, "Check Previous Value");
        if (strFilterType.isEmpty()) {
            resetFilterValue();
        } else if (strFilterType.equals(DOCTOR)) {
            radioDoctor.setChecked(true);
            strFilterType = DOCTOR;
            lineDoctorFilter.setVisibility(View.VISIBLE);

            //pathy
            getAlternativeTreatment();

            //consultation
            if (strFilterConsultation.isEmpty()) {

            } else if (strFilterConsultation.equals(CONSULTATION_FEE_300)) {
                radioFee300.setChecked(true);
            } else if (strFilterConsultation.equals(CONSULTATION_FEE_500)) {
                radioFee500.setChecked(true);
            } else if (strFilterConsultation.contains(CONSULTATION_FEE_ABOVE_500)) {
                radioAbove500.setChecked(true);
            }
        } else if (strFilterType.equals(HOSPITAL)) {
            radioHospital.setChecked(true);
        }


        if (strFilterNearBy.isEmpty()) {
            radioNearBy.setChecked(false);
        } else if (strFilterNearBy.equals(NEAR_BY)) {
            radioNearBy.setChecked(true);
        }

        if (strFilterLike.equals(USER_LIKE_HIGH_LOW)) {
            radioHigh.setChecked(true);
        } else if (strFilterLike.equals(USER_LIKE_LOW_HIGH)) {
            radioLow.setChecked(true);
        }

        printFilterData();
    }

    public void printFilterData() {
        GlobalVar.errorLog(TAG, "strFilterType", strFilterType);
        GlobalVar.errorLog(TAG, "strFilterPathyName", strFilterPathyName);
        GlobalVar.errorLog(TAG, "strFilterConsultation", strFilterConsultation);
        GlobalVar.errorLog(TAG, "strFilterLike", strFilterLike);
        GlobalVar.errorLog(TAG, "strFilterNearBy", strFilterNearBy);
        GlobalVar.errorLog(TAG, "arrayList", arrayList.toString());
    }

    public void resetFilterValue() {
        alternativeTreatments.clear();
        adapter.notifyDataSetChanged();
        lineDoctorFilter.setVisibility(View.GONE);
        arrayList.clear();
        //type 
        strFilterType = "";

        //doctor
        strFilterPathyName = "";

        //------Consultation -----
        strFilterConsultation = "";

        //-------near by-----
        strFilterNearBy = "";

        //-----likes---
        strFilterLike = "";

        Log.e(TAG, "Reset Value After");
        printFilterData();
        setCheckedListener();
        lineDoctorFilter.setVisibility(View.GONE);

    }

    public static void resetFilter() {

        arrayList.clear();
        //type
        strFilterType = "";

        //doctor
        strFilterPathyName = "";

        //------Consultation -----
        strFilterConsultation = "";

        //-------near by-----
        strFilterNearBy = "";

        //-----likes---
        strFilterLike = "";
    }
}
