package com.whr.user.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.adapter.NewFavouriteAdapter;
import com.whr.user.booking.diagnostics.booking.activities.NewPathalogyDetailsActivity;
import com.whr.user.booking.doctor.booking.DetailDoctorActivity;
import com.whr.user.booking.doctor.booking.DetailHospitalActivity;
import com.whr.user.booking.doctor.booking.models.DoctorModel;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.model.NewFavouritePojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.whr.user.booking.doctor.booking.DoctorHospitalPackagesListActivity.KEY_DOCTOR_MODEL;
import static com.whr.user.booking.doctor.booking.DoctorHospitalPackagesListActivity.KEY_HOSPITAL_MODEL;

public class NewFavouriteActivity extends AppCompatActivity {
    PreferenceUtils pref;
    Context context;
    private RequestQueue mQueue;
    private List<NewFavouritePojo> list;
    private NewFavouriteAdapter adapter;
    String TAG = getClass().getSimpleName();
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayoutManager lLayout;
    private MaterialRippleLayout imgBack;
    private TextView txtTitle;
    private String url = GlobalVar.ServerAddress + "AndroidNew/FavoriteDoctorHospitalPathologyList";


    DoctorModel doctorModel;
    HospitalModel hospitalModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_favourite);

        recyclerView = findViewById(R.id.ffavouritedRecyclyview);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtTitle = findViewById(R.id.txtTitle);
        imgBack = findViewById(R.id.imgBack);

        txtTitle.setText("Favourites");

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        context = NewFavouriteActivity.this;
        pref = new PreferenceUtils(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        list = new ArrayList<>();
        adapter = new NewFavouriteAdapter(context, list);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(adapter);

        getData();

        adapter.setClickLister(favouritePojo -> {
            if (favouritePojo.getType().contains("doctor")) {
                doctorModel = new DoctorModel();
                doctorModel.setId(favouritePojo.getId());
                Intent intent = new Intent(context, DetailDoctorActivity.class);
                intent.putExtra(KEY_DOCTOR_MODEL, doctorModel);
                startActivity(intent);
            } else if (favouritePojo.getType().contains("hospital")) {
                hospitalModel = new HospitalModel();
                hospitalModel.setId(favouritePojo.getId());
                hospitalModel.setSpecialization(favouritePojo.getSpecid());
                Intent intent = new Intent(context, DetailHospitalActivity.class);
                intent.putExtra(KEY_HOSPITAL_MODEL, hospitalModel);
                startActivity(intent);
            } else {
                Intent intent = new Intent(context, NewPathalogyDetailsActivity.class);
                intent.putExtra("pathid", String.valueOf(favouritePojo.getId()));
                intent.putExtra("pathname", favouritePojo.getName());
                if (favouritePojo.getType().contains("Pathology")) {
                    intent.putExtra("typeid", "1");
                } else {
                    intent.putExtra("typeid", "2");
                }
                startActivity(intent);
            }

        });


    }

    private void getData() {
        GlobalVar.showProgressDialog(this, "Loading.....", true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("userId", pref.getUID());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "FavouriteList", response.toString());
                    GlobalVar.hideProgressDialog();
                    Gson gson = new Gson();
                    list.clear();
                    JSONArray jsonArray1 = null;
                    NewFavouritePojo pojo = null;
                    try {
                        jsonArray1 = response.getJSONArray("FavoriteList");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                pojo = gson.fromJson(json.toString(), NewFavouritePojo.class);
                                list.add(pojo);
                            }

                            txtTitle.setText("Favourites(" + list.size() + ")");
                            adapter.notifyDataSetChanged();
                        } else {
                            showSnackBar(getString(R.string.NoDataAvailable));
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
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }
}
