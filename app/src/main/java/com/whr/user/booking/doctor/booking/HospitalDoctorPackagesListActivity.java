package com.whr.user.booking.doctor.booking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.whr.user.R;
import com.whr.user.booking.doctor.booking.adapters.HospitalDoctorAdapter;
import com.whr.user.booking.doctor.booking.adapters.HospitalPackagesAdapter;
import com.whr.user.booking.doctor.booking.adapters.HospitalPackagesHorizontalAdapter;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.booking.doctor.booking.models.HospitalProfile;

import java.util.ArrayList;

public class HospitalDoctorPackagesListActivity extends AppCompatActivity {

    ArrayList<HospitalProfile.DoctorList> objectArrayListDoctors = new ArrayList<>();
    ArrayList<HospitalProfile.HospitalPackages> objectArrayListPackages = new ArrayList<>();
    Context context;
    HospitalModel hospitalModel;
    String TAG = getClass().getSimpleName();
    TextView txtHospitalTitle;
    HospitalProfile.HospitalInfo hospitalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_doctor_list);
        txtHospitalTitle = findViewById(R.id.txtHospitalTitle);
        overridePendingTransitionEnter();
        context = this;
        Log.e(TAG, TAG);
        RecyclerView recyclerViewDoctorPackages = findViewById(R.id.recyclerViewDoctorPackages);
        LinearLayoutManager linearLayoutManagerDoctorPackages = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewDoctorPackages.setLayoutManager(linearLayoutManagerDoctorPackages);
        recyclerViewDoctorPackages.setHasFixedSize(false);

        Intent intent = getIntent();
        if (intent.getExtras().containsKey(DetailHospitalActivity.DOCTOR_LIST_KEY)) {
            hospitalModel = intent.getExtras().getParcelable(DetailHospitalActivity.KEY_HOSPITAL);
            txtHospitalTitle.setText(hospitalModel.getHospitalName());
            objectArrayListDoctors.addAll(intent.getParcelableArrayListExtra(DetailHospitalActivity.DOCTOR_LIST_KEY));
            HospitalDoctorAdapter doctorAdapter = new HospitalDoctorAdapter(this, objectArrayListDoctors);
            recyclerViewDoctorPackages.setAdapter(doctorAdapter);
            doctorAdapter.setOnClickListener(d -> {
                Intent intent1 = new Intent(context, DoctorAppointmentBookingActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                intent1.putExtra(DetailHospitalActivity.KEY_DOCTOR, d);
                intent1.putExtra(DetailHospitalActivity.KEY_HOSPITAL, hospitalModel);
                startActivity(intent1);
            });
        } else if (intent.getExtras().containsKey(DetailHospitalActivity.PACKAGE_LIST_KEY)) {
            hospitalModel = intent.getExtras().getParcelable(DetailHospitalActivity.KEY_HOSPITAL);
            txtHospitalTitle.setText(hospitalModel.getHospitalName());
            objectArrayListPackages.addAll(getIntent().getParcelableArrayListExtra(DetailHospitalActivity.PACKAGE_LIST_KEY));
            hospitalInfo = intent.getParcelableExtra(HospitalPackagesHorizontalAdapter.HOSPITAL_INFO);
            HospitalPackagesAdapter hospitalPackagesAdapter = new HospitalPackagesAdapter(this, objectArrayListPackages, hospitalInfo);
            recyclerViewDoctorPackages.setAdapter(hospitalPackagesAdapter);
        }

        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
    }

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
}