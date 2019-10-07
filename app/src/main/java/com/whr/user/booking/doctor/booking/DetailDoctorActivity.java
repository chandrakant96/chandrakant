package com.whr.user.booking.doctor.booking;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.model.CentralSearchModel;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.CentralSearchActivity;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.activities.ReviewScreen;
import com.whr.user.booking.doctor.booking.adapters.DoctorHospitalAdapter;
import com.whr.user.booking.doctor.booking.adapters.DoctorReviewAdapter;
import com.whr.user.booking.doctor.booking.models.DoctorModel;
import com.whr.user.booking.doctor.booking.models.NewDoctorProfile;
import com.whr.user.com.WHR.Utils.CustomSwipViewAdapter;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.whr.user.booking.doctor.booking.DoctorHospitalPackagesListActivity.KEY_DOCTOR_MODEL;

public class DetailDoctorActivity extends AppCompatActivity {

    public static final String TAG = DetailDoctorActivity.class.getSimpleName();
    TextView txtDoctorName, txtEducation, txtSpecialization, txtExperience,
            txtLikes, txtTitle, txtReadMoreReview, txtReviews, txtReviews2, txtReviews3;
    NestedScrollView scrollView;
    ImageView imageDoctor;
    RecyclerView recyclerViewHospital, recyclerViewReview;
    RequestQueue mQueue;
    Context context;
    Gson gson = new Gson();
    DoctorModel doctorModel;
    private PreferenceUtils pref;
    ArrayList<NewDoctorProfile.HospitalProfile> doctorProfiles = new ArrayList<>();
    ArrayList<NewDoctorProfile.Reviews> reviewsArrayList = new ArrayList<>();
    NewDoctorProfile.DoctorInfo doctorInfo;
    DoctorHospitalAdapter doctorHospitalAdapter;
    DoctorReviewAdapter doctorReviewAdapter;
    LinearLayout lineReview;
    TextView txtFav, txtShare;
    CoordinatorLayout coordinatorLayout;
    public static String HOSPITAL_PROFILE = "hospital_profile";
    TextView txtAddReview;
    CentralSearchModel c;
    String shareData = "";
    LinearLayout lineImage;
    ImageView imgFirst, imgSecond, imgThird;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        imgFirst = findViewById(R.id.imgFirst);
        imgSecond = findViewById(R.id.imgSecond);
        imgThird = findViewById(R.id.imgThird);
        lineImage = findViewById(R.id.lineImage);
        txtMore = findViewById(R.id.txtMore);
        context = this;
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        pref = new PreferenceUtils(context);
        txtAddReview = findViewById(R.id.txtAddReview);
        txtDoctorName = findViewById(R.id.txtDoctorName);
        txtEducation = findViewById(R.id.txtEducation);
        txtSpecialization = findViewById(R.id.txtSpecialization);
        txtExperience = findViewById(R.id.txtExperience);
        txtLikes = findViewById(R.id.txtLikes);
        txtTitle = findViewById(R.id.txtTitle);
        txtReadMoreReview = findViewById(R.id.txtReadMoreReview);
        imageDoctor = findViewById(R.id.imageDoctor);
        recyclerViewHospital = findViewById(R.id.recyclerViewHospital);
        recyclerViewReview = findViewById(R.id.recyclerViewReview);
        scrollView = findViewById(R.id.scrollView);
        txtReviews = findViewById(R.id.txtReviews);
        txtReviews2 = findViewById(R.id.txtReviews2);
        txtReviews3 = findViewById(R.id.txtReviews3);
        lineReview = findViewById(R.id.lineReview);
        txtFav = findViewById(R.id.txtFav);
        txtShare = findViewById(R.id.txtShare);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        dector = new ConnectionDector(context);

        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        recyclerViewHospital.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewHospital.setHasFixedSize(true);
        doctorHospitalAdapter = new DoctorHospitalAdapter(doctorProfiles, context);
        recyclerViewHospital.setAdapter(doctorHospitalAdapter);

        doctorHospitalAdapter.setOnClickListener(new DoctorHospitalAdapter.OnClickListener() {
            @Override
            public void onClickBook(NewDoctorProfile.HospitalProfile h) {
                Intent intent = new Intent(context, DoctorAppointmentBookingActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                intent.putExtra(KEY_DOCTOR_MODEL, doctorModel);
                intent.putExtra(HOSPITAL_PROFILE, h);
                startActivity(intent);
            }

            @Override
            public void onClickHospitalProfile(NewDoctorProfile.HospitalProfile h) {
                Intent intent = new Intent(context, DetailHospitalActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                //  intent.putExtra(KEY_DOCTOR_MODEL, doctorModel);
                HospitalModel hospitalModel = new HospitalModel();
                hospitalModel.setId(Integer.parseInt(h.getHid()));
                intent.putExtra(DoctorHospitalPackagesListActivity.KEY_HOSPITAL_MODEL, hospitalModel);
                startActivity(intent);
            }
        });

        recyclerViewReview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewReview.setHasFixedSize(true);
        doctorReviewAdapter = new DoctorReviewAdapter(reviewsArrayList, context);
        recyclerViewReview.setAdapter(doctorReviewAdapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (!Objects.requireNonNull(bundle).isEmpty()) {
            boolean isDoctorModel = bundle.containsKey(KEY_DOCTOR_MODEL);
            boolean isCentralSearch = bundle.containsKey(CentralSearchActivity.CENTRAL_SEARCH_KEY);
            if (isDoctorModel) {
                doctorModel = bundle.getParcelable(KEY_DOCTOR_MODEL);
                assert doctorModel != null;
                GlobalVar.errorLog(TAG, KEY_DOCTOR_MODEL, doctorModel.toString());
                getDoctorDetails();
            } else if (isCentralSearch) {
                c = bundle.getParcelable(CentralSearchActivity.CENTRAL_SEARCH_KEY);
                doctorModel = new DoctorModel();
                GlobalVar.errorLog(TAG, CentralSearchActivity.CENTRAL_SEARCH_KEY, c.toString());
                doctorModel.setId(Integer.parseInt(c.getId()));
                getDoctorDetails();
            }
        }

        txtReadMoreReview.setOnClickListener(view -> scrollView.requestChildFocus(view, recyclerViewReview));
        txtLikes.setOnClickListener(view -> getLikes());
        txtFav.setOnClickListener(view -> getFav());
        txtAddReview.setOnClickListener(view -> {
            Intent i = new Intent(context, ReviewScreen.class);
            i.putExtra("docterIdkey", String.valueOf(doctorModel.getId()));
            startActivity(i);
        });

        txtShare.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareData);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share"));
        });

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

    String nextTime = "";
    private String clinicArray[];
    TextView txtMore;
    ConnectionDector dector;

    private void getDoctorDetails() {
        if (dector.isConnectingToInternet()) {


            JSONObject obj = new JSONObject();
            try {
                obj.put("did", doctorModel.getId());
                obj.put("userId", pref.getUID());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            GlobalVar.showProgressDialog(this, "", false);

            String url = GlobalVar.ServerAddress + "AndroidNew/DoctorProfile";

            GlobalVar.errorLog(TAG, " url", url);
            GlobalVar.errorLog(TAG, " obj", obj.toString());

            CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                    response -> {
                        GlobalVar.hideProgressDialog();
                        //GlobalVar.errorLog(TAG, "GetDoctorList response", response.toString());
                        try {
                            JSONObject jsonObject = response.getJSONObject("DoctorProfile");
                            NewDoctorProfile newDoctorProfile = gson.fromJson(jsonObject.toString(), NewDoctorProfile.class);

                            JSONArray jsonArrayHospitalProfile = jsonObject.optJSONArray("HospitalProfile");
                            if (jsonArrayHospitalProfile != null && jsonArrayHospitalProfile.length() > 0) {
                                for (int i = 0; i < jsonArrayHospitalProfile.length(); i++) {
                                    JSONObject jsonObjectHospital = jsonArrayHospitalProfile.getJSONObject(i);
                                    NewDoctorProfile.HospitalProfile hospitalProfile = gson.fromJson(jsonObjectHospital.toString(), NewDoctorProfile.HospitalProfile.class);
                                    hospitalProfile.setDistance(GlobalVar.distance(hospitalProfile.getLatitude(), hospitalProfile.getLongitude()));
                                    nextTime = "";
                                    for (NewDoctorProfile.HospitalProfile.TimeSlots slots : hospitalProfile.getTimeSlots()) {

                                        if (nextTime.isEmpty()) {
                                            nextTime = slots.getSchedulestring();
                                        } else {
                                            nextTime = nextTime + "\n" + slots.getSchedulestring();
                                        }

                                    }
                                    hospitalProfile.setNextTimeSlots(nextTime);
                                    doctorProfiles.add(hospitalProfile);
                                }
                                newDoctorProfile.setHospitalProfile(doctorProfiles);
                                doctorHospitalAdapter.notifyDataSetChanged();
                            }

                            JSONArray jsonArrayReviews = jsonObject.optJSONArray("Reviews");
                            if (jsonArrayReviews != null && jsonArrayReviews.length() > 0) {
                                for (int i = 0; i < jsonArrayReviews.length(); i++) {
                                    JSONObject reviewsJSONObject = jsonArrayReviews.getJSONObject(i);
                                    NewDoctorProfile.Reviews reviews = gson.fromJson(reviewsJSONObject.toString(), NewDoctorProfile.Reviews.class);
                                    reviewsArrayList.add(reviews);
                                    if (i < 4 && !reviews.getUserreview().isEmpty())
                                        txtReviews3.append("- " + reviews.getUserreview() + "\n");
                                }
                                newDoctorProfile.setReviews(reviewsArrayList);
                                doctorReviewAdapter.notifyDataSetChanged();
                                txtReviews.setText("Reviews (" + reviewsArrayList.size() + ")");
                                txtReviews2.setText("Reviews (" + reviewsArrayList.size() + ")");
                                lineReview.setVisibility(View.VISIBLE);
                            } else {
                                lineReview.setVisibility(View.GONE);
                            }

                            JSONObject jsonObjectDoctorProfile = jsonObject.optJSONObject("DoctorInfo");
                            if (jsonObjectDoctorProfile != null && jsonObjectDoctorProfile.length() > 0) {
                                doctorInfo = gson.fromJson(jsonObjectDoctorProfile.toString(), NewDoctorProfile.DoctorInfo.class);
                                newDoctorProfile.setDoctorInfo(doctorInfo);
                                txtTitle.setText(doctorInfo.getName());
                                txtDoctorName.setText(doctorInfo.getName());
                                txtEducation.setText(doctorInfo.getEducation());
                                txtSpecialization.setText(doctorInfo.getSpecial());
                                txtExperience.setText(doctorInfo.getExperience());
                                txtLikes.setText(String.valueOf(doctorInfo.getLikes()));
                                shareData = doctorInfo.getName() + "\n" + doctorInfo.getSpecial() + "\n" + "http://user.whrhealth.com/Home/DoctorDetails/?id=" + doctorModel.getId() + "&sid=" + doctorInfo.getSid();
                                doctorModel.setName(doctorInfo.getName());

                                if (doctorInfo.getProfileimage() != null && !doctorInfo.getProfileimage().isEmpty()) {
                                    Picasso.with(context).load(doctorInfo.getProfileimage())
                                            .placeholder(R.drawable.ic_doctor_color).error(R.drawable.ic_doctor_color)
                                            .fit().into(imageDoctor);
                                } else
                                    imageDoctor.setImageResource(R.drawable.ic_doctor_color);

                                if (doctorInfo.isFavoriteStatus()) {
                                    txtFav.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_liked_state, 0, 0);
                                } else {
                                    txtFav.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_unliked_state, 0, 0);
                                }

                                if (doctorInfo.isLikeStatus()) {
                                    txtLikes.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_liked_state, 0, 0);
                                } else {
                                    txtLikes.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_unliked_state, 0, 0);
                                }


                                List<NewDoctorProfile.DoctorInfo.Clinicimages> clinicimages = doctorInfo.getClinicimages();
                                if (clinicimages != null) {
                                    int clinicSize = clinicimages.size();

                                    if (clinicSize > 3) {
                                        txtMore.setVisibility(View.VISIBLE);
                                    } else {
                                        txtMore.setVisibility(View.GONE);
                                    }
                                    if (clinicSize == 1) {
                                        String imagePath = clinicimages.get(0).getImagespath();
                                        if (imagePath != null && !imagePath.isEmpty()) {
                                            Picasso.with(context).load(imagePath)
                                                    .placeholder(R.drawable.ic_hospital).error(R.drawable.ic_hospital)
                                                    .fit().into(imgFirst);
                                            imgFirst.setVisibility(View.VISIBLE);
                                        }
                                    } else if (clinicSize == 2) {
                                        String imagePath = clinicimages.get(0).getImagespath();
                                        String imagePath2 = clinicimages.get(1).getImagespath();
                                        if (imagePath != null && !imagePath.isEmpty()) {
                                            Picasso.with(context).load(imagePath)
                                                    .placeholder(R.drawable.ic_hospital).error(R.drawable.ic_hospital)
                                                    .fit().into(imgFirst);
                                            imgFirst.setVisibility(View.VISIBLE);
                                        }
                                        if (imagePath2 != null && !imagePath2.isEmpty()) {
                                            Picasso.with(context).load(imagePath2)
                                                    .placeholder(R.drawable.ic_hospital).error(R.drawable.ic_hospital)
                                                    .fit().into(imgSecond);
                                            imgSecond.setVisibility(View.VISIBLE);
                                        }

                                    } else if (clinicSize >= 3) {
                                        String imagePath = clinicimages.get(0).getImagespath();
                                        String imagePath2 = clinicimages.get(1).getImagespath();
                                        String imagePath3 = clinicimages.get(2).getImagespath();
                                        if (imagePath != null && !imagePath.isEmpty()) {
                                            Picasso.with(context).load(imagePath)
                                                    .placeholder(R.drawable.ic_hospital).error(R.drawable.ic_hospital)
                                                    .fit().into(imgFirst);
                                            imgFirst.setVisibility(View.VISIBLE);
                                        }
                                        if (imagePath2 != null && !imagePath2.isEmpty()) {
                                            Picasso.with(context).load(imagePath2)
                                                    .placeholder(R.drawable.ic_hospital).error(R.drawable.ic_hospital)
                                                    .fit().into(imgSecond);
                                            imgSecond.setVisibility(View.VISIBLE);
                                        }
                                        if (imagePath3 != null && !imagePath3.isEmpty()) {
                                            Picasso.with(context).load(imagePath3)
                                                    .placeholder(R.drawable.ic_hospital).error(R.drawable.ic_hospital)
                                                    .fit().into(imgThird);
                                            imgThird.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    lineImage.setOnClickListener(v -> {
                                        clinicArray = new String[clinicimages.size()];
                                        int counter = 0;
                                        for (NewDoctorProfile.DoctorInfo.Clinicimages clinicImage : clinicimages) {
                                            clinicArray[counter] = clinicImage.getImagespath();
                                            counter++;
                                        }
                                        final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setCancelable(true);
                                        dialog.setCanceledOnTouchOutside(true);
                                        dialog.setContentView(R.layout.layout_image_slider);
                                        Window window = dialog.getWindow();
                                        assert window != null;
                                        WindowManager.LayoutParams wlp = window.getAttributes();
                                        wlp.gravity = Gravity.BOTTOM;
                                        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        window.setAttributes(wlp);
                                        dialog.show();

                                        ViewPager viewPager = dialog.findViewById(R.id.pager);
                                        CustomSwipViewAdapter adapter = new CustomSwipViewAdapter(context,
                                                clinicArray);
                                        viewPager.setAdapter(adapter);
                                        TextView txtOk = dialog.findViewById(R.id.txtOk);
                                        ImageView imgClose = dialog.findViewById(R.id.imgClose);

                                        txtOk.setOnClickListener(v1 -> {
                                            dialog.cancel();
                                            dialog.dismiss();
                                        });

                                        imgClose.setOnClickListener(v1 -> {
                                            dialog.cancel();
                                            dialog.dismiss();
                                        });
                                    });
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> GlobalVar.hideProgressDialog());
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
            getDoctorDetails();
        }
    }

    private void getLikes() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("did", doctorModel.getId());
            obj.put("status", !doctorInfo.isLikeStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String url = GlobalVar.ServerAddress + "AndroidNew/CountLikes";
        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());
        GlobalVar.showProgressDialog(context, "", false);

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "getLikes", response.toString());
                    GlobalVar.hideProgressDialog();
                    try {
                        doctorInfo.setLikeStatus(response.getBoolean("like"));
                        doctorInfo.setLikes(response.getInt("TotalLikeCount"));
                        txtLikes.setText(String.valueOf(doctorInfo.getLikes()));
                        if (doctorInfo.isLikeStatus()) {
                            showSnackBarGreen(context.getString(R.string.DoctorLiked));
                            txtLikes.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_liked_state, 0, 0);
                        } else {
                            showSnackBar(context.getString(R.string.DoctorUnliked));
                            txtLikes.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_unliked_state, 0, 0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, (VolleyError error) -> {
            showSnackBar(VolleyErrorHelper.getMessage(error, context));
            GlobalVar.hideProgressDialog();
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

    private void getFav() {
        PreferenceUtils pref = new PreferenceUtils(context);
        String url = GlobalVar.ServerAddress + "AndroidNew/CountFavorite";
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("did", doctorModel.getId());
            obj.put("status", !doctorInfo.isFavoriteStatus());
            obj.put("specializationId", doctorModel.getSpecializationId());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.showProgressDialog(context, "", false);
        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "getFav", response.toString());
                    GlobalVar.hideProgressDialog();
                    try {
                        doctorInfo.setFavoriteStatus(response.getBoolean("favorite"));
                        if (doctorInfo.isFavoriteStatus()) {
                            showSnackBarGreen(context.getString(R.string.DoctorSuccessfullyAddedToFavourite));
                            txtFav.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_liked_state, 0, 0);
                        } else {
                            showSnackBar(context.getString(R.string.DoctorSuccessfullyRemovedfromFavourite));
                            txtFav.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_unliked_state, 0, 0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, (VolleyError error) -> {
            GlobalVar.hideProgressDialog();
            showSnackBar(VolleyErrorHelper.getMessage(error, context));
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }


}