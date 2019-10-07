package com.whr.user.booking.doctor.booking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.whr.user.R;
import com.whr.user.booking.doctor.booking.adapters.HospitalPackagesHorizontalAdapter;
import com.whr.user.booking.doctor.booking.models.HospitalProfile;
import com.whr.user.booking.doctor.booking.models.PackagesDetails;
import com.whr.user.booking.packages.hospital.activities.PackageHospitalsList;
import com.whr.user.booking.packages.hospital.model.SurgeryPackageInHospitals;
import com.whr.user.pojo.GlobalVar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HospitalPackagesCalenderActivity extends AppCompatActivity {

    PackagesDetails packagesDetails;
    String TAG = getClass().getSimpleName();

    CalendarView calendarView1;
    String strApiDate = "";
    TextView txtConfirm, txtPackageName, txtPackageTitle;
    Context context;
    public static final String APPOINTMENT_DATE_KEY = "appointment_date_key";
    HospitalProfile.HospitalInfo hospitalInfo;
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransitionExit();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_packages_calender);
        context = this;
        txtPackageTitle = findViewById(R.id.txtPackageTitle);
        txtPackageName = findViewById(R.id.txtPackageName);
        txtConfirm = findViewById(R.id.txtConfirm);
        calendarView1 = findViewById(R.id.calendarView);
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        Intent intent = getIntent();

        if (intent.getExtras().containsKey(HospitalPackageDetailsActivity.HOSPITAL_PACKAGE_DETAILS_KEY)) {
            if (intent.getExtras().containsKey(HospitalPackageDetailsActivity.HOSPITAL_PACKAGE_DETAILS_KEY)) {
                packagesDetails = intent.getParcelableExtra(HospitalPackageDetailsActivity.HOSPITAL_PACKAGE_DETAILS_KEY);
                txtPackageName.setText("Choose Date for " + packagesDetails.getPackageName() + " appointment in " + packagesDetails.getHospname());
                txtPackageTitle.setText(packagesDetails.getPackageName());
                GlobalVar.errorLog(TAG, "packagesDetails", packagesDetails.toString());
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendarView1.setDate(calendar.getTimeInMillis());
        calendarView1.setMinDate(calendar.getTimeInMillis());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, 2);
        calendarView1.setMinDate(calendar.getTimeInMillis());
        calendarView1.setMaxDate(calendar1.getTimeInMillis());
        calendarView1.setDate(calendar.getTimeInMillis());
        calendarView1.setSelected(true);

        /*Calendar calendar2 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_MONTH, 2);
        calendarView1.setDate(calendar2.getTimeInMillis());
        calendarView1.setSelected(true);*/

        calendarView1.setOnDateChangeListener((CalendarView calendarView, int i, int i1, int i2) -> {
            i1 = i1 + 1;
            strApiDate = (i2 < 10 ? ("0" + i2) : (i2)) + "-" + (i1 < 10 ? ("0" + i1) : (i1)) + "-" + i;
        });
        strApiDate = dateFormat.format(calendarView1.getDate());
        GlobalVar.errorLog(TAG, "strdate", strApiDate);

        txtConfirm.setOnClickListener(view -> {
            Intent intent1 = new Intent(context, BookingDetailsPackageActivity.class);
            intent1.putExtra(HospitalPackageDetailsActivity.HOSPITAL_PACKAGE_DETAILS_KEY, packagesDetails);
            intent1.putExtra(APPOINTMENT_DATE_KEY, strApiDate);
            startActivity(intent1);
        });
    }
}