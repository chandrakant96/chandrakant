package com.whr.user.booking.doctor.booking;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.whr.user.activities.CentralSearchActivity;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.activities.ReviewScreen;
import com.whr.user.booking.doctor.booking.adapters.HospitalDoctorAdapter;
import com.whr.user.booking.doctor.booking.adapters.HospitalPackagesAdapter;
import com.whr.user.booking.doctor.booking.adapters.HospitalPackagesHorizontalAdapter;
import com.whr.user.booking.doctor.booking.adapters.HospitalReviewAdapter;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.booking.doctor.booking.models.HospitalProfile;
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

public class DetailHospitalActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    ImageView imageHospital;
    TextView txtHospitalName, txtEstd, txtDoctorCount, txtLikes,
            txtAddress, txtFav, txtDistance, txtReviewsTitle, txtReviews, txtReviews2, txtMoreReviews,
            txtAdmitNow, txtPackages, txtDoctor, txtHospitalTitle, txtMore;

    LinearLayout lineReview;

    RecyclerView recyclerViewDoctorPackages, recyclerViewReview;
    RequestQueue mQueue;
    Context context;
    NestedScrollView nestedScrollView;

    HospitalModel hospitalModel;
    CoordinatorLayout coordinatorLayout;
    HospitalDoctorAdapter doctorAdapter;
    PreferenceUtils pref;

    HospitalReviewAdapter hospitalReviewAdapter;
    ArrayList<HospitalProfile.Reviews> reviewsArrayList = new ArrayList<>();
    ImageView imgFirst, imgSecond, imgThird;

    TextView txtLoadMore;
    int doctorListSize = 0;
    ArrayList<HospitalProfile.DoctorList> doctorLists = new ArrayList<>();
    ArrayList<HospitalProfile.DoctorList> doctorListSub = new ArrayList<>();
    ArrayList<HospitalProfile.HospitalPackages> hospitalPackages = new ArrayList<>();
    ArrayList<HospitalProfile.HospitalPackages> hospitalPackagesSub = new ArrayList<>();

    HospitalPackagesAdapter packagesAdapter;
    public static String DOCTOR_LIST_KEY = "Doctor_List";
    public static String PACKAGE_LIST_KEY = "Package_List";
    LinearLayout lineDoctorPackages;
    boolean isScrollToPackageList = false;

    public static String KEY_HOSPITAL = "key_hospital";
    public static String KEY_DOCTOR = "key_doctor";
    HospitalProfile.HospitalInfo hospitalInfo;
    Gson gson = new Gson();
    String specialization = "0";
    TextView txtAddReview, txtShare;
    LinearLayout lineImage;
    //LinearLayout relativeScrollDoctorPackage;
    LinearLayout lineScroll;
    String central = "";
    String clicked = "doctors";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_hospital_detail2);
        lineScroll = findViewById(R.id.lineScroll);
        //relativeScrollDoctorPackage = findViewById(R.id.relativeScrollDoctorPackage);
        lineImage = findViewById(R.id.lineImage);
        txtAddReview = findViewById(R.id.txtAddReview);
        txtShare = findViewById(R.id.txtShare);
        lineDoctorPackages = findViewById(R.id.lineDoctorPackages);
        txtLoadMore = findViewById(R.id.txtLoadMore);
        txtMore = findViewById(R.id.txtMore);
        imgFirst = findViewById(R.id.imgFirst);
        imgSecond = findViewById(R.id.imgSecond);
        imgThird = findViewById(R.id.imgThird);
        imageHospital = findViewById(R.id.imageHospital);
        txtHospitalName = findViewById(R.id.txtHospitalName);
        txtEstd = findViewById(R.id.txtEstd);
        txtDoctorCount = findViewById(R.id.txtDoctorCount);
        txtLikes = findViewById(R.id.txtLikes);
        txtAddress = findViewById(R.id.txtAddress);
        txtFav = findViewById(R.id.txtFav);
        txtDistance = findViewById(R.id.txtDistance);
        txtReviewsTitle = findViewById(R.id.txtReviewsTitle);
        txtReviews = findViewById(R.id.txtReviews);
        txtMoreReviews = findViewById(R.id.txtMoreReviews);
        txtAdmitNow = findViewById(R.id.txtAdmitNow);
        txtPackages = findViewById(R.id.txtPackages);
        txtDoctor = findViewById(R.id.txtDoctor);
        txtReviews2 = findViewById(R.id.txtReviews2);
        lineReview = findViewById(R.id.lineReview);
        lineReview.setVisibility(View.GONE);
        recyclerViewDoctorPackages = findViewById(R.id.recyclerViewDoctorPackages);
        recyclerViewReview = findViewById(R.id.recyclerViewReview);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        txtHospitalTitle = findViewById(R.id.txtHospitalTitle);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        context = this;
        pref = new PreferenceUtils(context);
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        LinearLayoutManager linearLayoutManagerHospitalReview = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewReview.setLayoutManager(linearLayoutManagerHospitalReview);
        recyclerViewReview.setHasFixedSize(true);
        hospitalReviewAdapter = new HospitalReviewAdapter(reviewsArrayList, context);
        recyclerViewReview.setAdapter(hospitalReviewAdapter);

        LinearLayoutManager linearLayoutManagerDoctorPackages = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewDoctorPackages.setLayoutManager(linearLayoutManagerDoctorPackages);
        recyclerViewDoctorPackages.setHasFixedSize(false);

        txtMoreReviews.setOnClickListener(view -> nestedScrollView.requestChildFocus(view, recyclerViewReview));
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (!Objects.requireNonNull(bundle).isEmpty()) {
            boolean isDoctorModel = bundle.containsKey(DoctorHospitalPackagesListActivity.KEY_HOSPITAL_MODEL);
            if (isDoctorModel) {
                central = bundle.getString(CentralSearchActivity.CENTRAL_SEARCH_KEY);
                hospitalModel = bundle.getParcelable(DoctorHospitalPackagesListActivity.KEY_HOSPITAL_MODEL);
                assert hospitalModel != null;
                GlobalVar.errorLog(TAG, DoctorHospitalPackagesListActivity.KEY_HOSPITAL_MODEL,
                        hospitalModel.toString());
            }

            isScrollToPackageList = bundle.containsKey(DoctorHospitalPackagesListActivity.KEY_SCROLL_HOSPITAL);

          /*  if (isScrollToPackageList) {
                String keyScroll = bundle.getString(DoctorHospitalPackagesListActivity.KEY_SCROLL_HOSPITAL);
                Log.e(TAG, keyScroll);

                if (keyScroll.equalsIgnoreCase("packages")) {
                    changeBgColor(txtPackages, txtDoctor);
                    //------packages list------------
                    setRecyclerViewPackageList();
                } else {
                    changeBgColor(txtDoctor, txtPackages);
                    //-------Doctor List-------s
                    setRecyclerViewDoctorList();
                }

                nestedScrollView.requestChildFocus(lineScroll, recyclerViewDoctorPackages);
                //nestedScrollView.scroll
            }*/

            txtAdmitNow.setOnClickListener(v -> {
                Intent intent1 = new Intent(context, BookingDetailsAdmitNowActivity.class);
                intent1.putExtra(DoctorHospitalPackagesListActivity.KEY_HOSPITAL_MODEL, hospitalModel);
                startNewActivity(intent1);
            });
        }


        txtDoctor.setOnClickListener(view -> {
            clicked = "doctors";
            changeBgColor(txtDoctor, txtPackages);
            doctorListSub.clear();
            if (doctorLists.size() > 5) {
                doctorListSub.addAll(doctorLists.subList(0, 5));
                txtLoadMore.setVisibility(View.VISIBLE);
            } else {
                doctorListSub.addAll(doctorLists);
                txtLoadMore.setVisibility(View.GONE);
            }
            setRecyclerViewDoctorList();
            recyclerViewDoctorPackages.setAdapter(doctorAdapter);
            doctorAdapter.notifyDataSetChanged();
        });


        txtPackages.setOnClickListener(view -> {
            clicked = "packages";
            hospitalPackagesSub.clear();
            changeBgColor(txtPackages, txtDoctor);
            if (hospitalPackages.size() > 5) {
                hospitalPackagesSub.addAll(hospitalPackages.subList(0, 5));
                txtLoadMore.setVisibility(View.VISIBLE);
            } else {
                hospitalPackagesSub.addAll(hospitalPackages);
                txtLoadMore.setVisibility(View.GONE);
            }
            setRecyclerViewPackageList();
            recyclerViewDoctorPackages.setAdapter(packagesAdapter);
            packagesAdapter.notifyDataSetChanged();
        });


        txtLoadMore.setOnClickListener(view -> {
            Intent intent1 = new Intent(context, HospitalDoctorPackagesListActivity.class);
            if (clicked.equalsIgnoreCase("doctors")) {
                intent1.putParcelableArrayListExtra(DOCTOR_LIST_KEY, doctorLists);
                intent1.putExtra(KEY_HOSPITAL, hospitalModel);
                intent1.putExtra(HospitalPackagesHorizontalAdapter.HOSPITAL_INFO, hospitalInfo);
            }
            if (clicked.equalsIgnoreCase("packages")) {
                intent1.putParcelableArrayListExtra(PACKAGE_LIST_KEY, hospitalPackages);
                intent1.putExtra(KEY_HOSPITAL, hospitalModel);
                intent1.putExtra(HospitalPackagesHorizontalAdapter.HOSPITAL_INFO, hospitalInfo);
            }

            startNewActivity(intent1);
        });

        if (new ConnectionDector(context).isConnectingToInternet()) {
            getHospitalDetails();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }

        txtLikes.setOnClickListener(v -> {
            getLikes();
        });

        txtFav.setOnClickListener(v -> {
            getFav();
        });


        txtAddReview.setOnClickListener(v -> {
            Intent i = new Intent(context, ReviewScreen.class);
            i.putExtra("docterIdkey", String.valueOf(hospitalModel.getId()));
            startActivity(i);
        });


        txtShare.setOnClickListener(v -> {
            final Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            StringBuilder str = new StringBuilder();
            String link = "http://user.whrhealth.com/Home/DoctorForHosp/?hid=" + hospitalModel.getId();

            str.append(hospitalModel.getHospitalName()).append("\n Address : ").append(hospitalInfo.getAddress()).append("\nVisit To Hospital Profile : " + link);

            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, str.toString());
            startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
            getHospitalDetails();
        }
    }

    private void setRecyclerViewPackageList() {
        packagesAdapter = new HospitalPackagesAdapter(context, hospitalPackagesSub, hospitalInfo);
        recyclerViewDoctorPackages.setAdapter(packagesAdapter);
        packagesAdapter.notifyDataSetChanged();
    }

    private String clinicArray[];

    private void getHospitalDetails() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("hid", hospitalModel.getId());
            obj.put("userId", pref.getUID());
            if (central.equals("true")) {

                obj.put("specialization", "0");
            } else {
                obj.put("specialization", hospitalModel.getSpecialization());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.showProgressDialog(this, "", false);
        String url = GlobalVar.ServerAddress + "AndroidNew/HospitalProfile";
        GlobalVar.errorLog(TAG, " url", url);
        GlobalVar.errorLog(TAG, " obj", obj.toString());

        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    JSONObject jsonObjectHospitalProfile = null;
                    try {
                        jsonObjectHospitalProfile = response.getJSONObject("HospitalProfile");
                        HospitalProfile hospitalProfile = gson.fromJson(jsonObjectHospitalProfile.toString(), HospitalProfile.class);

                        JSONObject jsonObjectHospitalInfo = jsonObjectHospitalProfile.getJSONObject("HospitalInfo");
                        if (jsonObjectHospitalInfo != null && jsonObjectHospitalInfo.length() > 0) {
                            hospitalInfo = gson.fromJson(jsonObjectHospitalInfo.toString(), HospitalProfile.HospitalInfo.class);
                            hospitalInfo.setDistance(GlobalVar.distance(hospitalInfo.getLatitude(), hospitalInfo.getLongitude()));
                            hospitalProfile.setHospitalInfo(hospitalInfo);
                            if (hospitalInfo.getProfileimage() != null && !hospitalInfo.getProfileimage().isEmpty()) {
                                Picasso.with(context).load(hospitalInfo.getProfileimage())
                                        .placeholder(R.drawable.ic_hospital).error(R.drawable.ic_hospital)
                                        .fit().into(imageHospital);
                            } else
                                imageHospital.setImageResource(R.drawable.ic_hospital);

                            txtHospitalName.setText(hospitalInfo.getHospitalname());
                            hospitalModel.setHospitalName(hospitalInfo.getHospitalname());
                            txtHospitalTitle.setText(hospitalInfo.getHospitalname());
                            txtAddress.setText(hospitalInfo.getAddress());
                            txtDistance.setText(String.valueOf(hospitalInfo.getDistance()) + " KM");

                            txtDistance.setOnClickListener(v -> {
                                String strUri = "http://maps.google.com/maps?q=loc:"
                                        + hospitalInfo.getLatitude() + ","
                                        + hospitalInfo.getLongitude() +
                                        " (" + hospitalInfo.getHospitalname() + ")";
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
                            });

                            txtLikes.setText(String.valueOf(hospitalInfo.getLikes()));
                            txtDoctorCount.setText(String.valueOf(hospitalInfo.getDoctorcount()));

                            if (hospitalInfo.isLikeStatus()) {
                                txtLikes.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_liked_state, 0, 0);
                            } else {
                                txtLikes.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_unliked_state, 0, 0);
                            }

                            if (hospitalInfo.isFavoriteStatus()) {
                                txtFav.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_liked_state, 0, 0);
                            } else {
                                txtFav.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_unliked_state, 0, 0);
                            }

                            List<HospitalProfile.HospitalInfo.Clinicimages> clinicimages = hospitalInfo.getClinicimages();
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
                                if (imagePath2 != null && !imagePath2.isEmpty()) {
                                    Picasso.with(context).load(imagePath3)
                                            .placeholder(R.drawable.ic_hospital).error(R.drawable.ic_hospital)
                                            .fit().into(imgThird);
                                    imgThird.setVisibility(View.VISIBLE);
                                }
                            }

                            lineImage.setOnClickListener(v -> {
                                clinicArray = new String[clinicimages.size()];
                                int counter = 0;
                                for (HospitalProfile.HospitalInfo.Clinicimages clinicImage : clinicimages) {
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

                        //--------doctor list
                        JSONArray jsonArrayDoctorList = jsonObjectHospitalProfile.getJSONArray("DoctorList");
                        if (jsonArrayDoctorList != null && jsonArrayDoctorList.length() > 0) {
                            doctorListSize = jsonArrayDoctorList.length();
                            for (int i = 0; i < doctorListSize; i++) {
                                JSONObject jsonObjectDoctorList = jsonArrayDoctorList.getJSONObject(i);
                                HospitalProfile.DoctorList doctorList = gson.fromJson(jsonObjectDoctorList.toString()
                                        , HospitalProfile.DoctorList.class);
                                doctorLists.add(doctorList);
                            }
                            hospitalProfile.setDoctorList(doctorLists);
                            GlobalVar.errorLog(TAG, "doctorLists", String.valueOf(doctorLists.size()));
                        } else {
                            txtDoctor.setVisibility(View.GONE);
                        }
                        //-------packages list
                        JSONArray jsonArrayHospitalPackages = jsonObjectHospitalProfile.getJSONArray("HospitalPackages");
                        if (jsonArrayHospitalPackages != null && jsonArrayHospitalPackages.length() > 0) {
                            for (int i = 0; i < jsonArrayHospitalPackages.length(); i++) {
                                JSONObject jsonObjectHospitalPackages = jsonArrayHospitalPackages.getJSONObject(i);
                                HospitalProfile.HospitalPackages hospitalPackagesModel = gson.fromJson(jsonObjectHospitalPackages.toString()
                                        , HospitalProfile.HospitalPackages.class);
                                hospitalPackages.add(hospitalPackagesModel);
                            }
                            hospitalProfile.setHospitalPackages(hospitalPackages);
                            GlobalVar.errorLog(TAG, "hospitalPackages", String.valueOf(hospitalPackages.size()));
                        } else {
                            txtPackages.setVisibility(View.GONE);
                        }
                        txtLoadMore.setVisibility(View.GONE);
                        if (isScrollToPackageList) {
                            String keyScroll = getIntent().getExtras().getString(DoctorHospitalPackagesListActivity.KEY_SCROLL_HOSPITAL);
                            Log.e(TAG, keyScroll);
                            if (keyScroll.equalsIgnoreCase("packages")) {
                                changeBgColor(txtPackages, txtDoctor);
                                //------Packages list------------
                                setRecyclerViewPackageList();
                                if (hospitalPackages.size() > 5) {
                                    hospitalPackagesSub.addAll(hospitalPackages.subList(0, 5));
                                    txtLoadMore.setVisibility(View.VISIBLE);
                                } else {
                                    hospitalPackagesSub.addAll(hospitalPackages);
                                    txtLoadMore.setVisibility(View.GONE);
                                }
                                nestedScrollView.requestChildFocus(lineScroll, recyclerViewDoctorPackages);
                            } else if (keyScroll.equalsIgnoreCase("doctors")) {
                                //-------Doctor List-------
                                changeBgColor(txtDoctor, txtPackages);
                                setRecyclerViewDoctorList();
                                if (doctorLists.size() > 5) {
                                    doctorListSub.addAll(doctorLists.subList(0, 5));
                                    txtLoadMore.setVisibility(View.VISIBLE);
                                } else {
                                    doctorListSub.addAll(doctorLists);
                                    txtLoadMore.setVisibility(View.GONE);
                                }
                                nestedScrollView.requestChildFocus(lineScroll, recyclerViewDoctorPackages);
                            } else {
                                //-------Doctor List Default-------
                                changeBgColor(txtDoctor, txtPackages);
                                setRecyclerViewDoctorList();
                                if (doctorLists.size() > 5) {
                                    doctorListSub.addAll(doctorLists.subList(0, 5));
                                    txtLoadMore.setVisibility(View.VISIBLE);
                                } else {
                                    doctorListSub.addAll(doctorLists);
                                    txtLoadMore.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            //-------Doctor List Default-------
                            changeBgColor(txtDoctor, txtPackages);
                            setRecyclerViewDoctorList();
                            if (doctorLists.size() > 5) {
                                doctorListSub.addAll(doctorLists.subList(0, 5));
                                txtLoadMore.setVisibility(View.VISIBLE);
                            } else {
                                doctorListSub.addAll(doctorLists);
                                txtLoadMore.setVisibility(View.GONE);
                            }
                        }

                        //-----------Reviews---------------

                        JSONArray jsonArrayReviews = jsonObjectHospitalProfile.getJSONArray("Reviews");
                        if (jsonArrayReviews != null && jsonArrayReviews.length() > 0) {
                            List<HospitalProfile.Reviews> reviewsArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArrayReviews.length(); i++) {
                                JSONObject jsonObjectHospitalReview = jsonArrayReviews.getJSONObject(i);
                                HospitalProfile.Reviews reviews = gson.fromJson(jsonObjectHospitalReview.toString()
                                        , HospitalProfile.Reviews.class);
                                //  GlobalVar.errorLog(TAG, "Reviews", reviews.toString());
                                if (i < 4 && !reviews.getUserreview().isEmpty())
                                    txtReviews.append("- " + reviews.getUserreview() + "\n");
                                reviewsArrayList.add(reviews);
                            }
                            hospitalProfile.setReviews(reviewsArrayList);
                            // txtReviews.setText("Reviews (" + reviewsArrayList.size() + ")");
                            txtReviews2.setText("Reviews (" + reviewsArrayList.size() + ")");
                            lineReview.setVisibility(View.VISIBLE);
                            DetailHospitalActivity.this.reviewsArrayList.clear();
                            DetailHospitalActivity.this.reviewsArrayList.addAll(reviewsArrayList);
                            hospitalReviewAdapter.notifyDataSetChanged();
                        } else {
                            lineReview.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        GlobalVar.hideProgressDialog();
                        e.printStackTrace();
                    }
                }, error -> {
            GlobalVar.hideProgressDialog();
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    private void changeBgColor(TextView t1, TextView t2) {
        t1.setTextColor(getResources().getColor(R.color.white));
        t1.setBackgroundResource(R.color.primary);
        t1.setEnabled(false);
        t1.setClickable(false);

        t2.setTextColor(getResources().getColor(R.color.primary));
        t2.setBackgroundResource(R.drawable.border);

        t2.setEnabled(true);
        t2.setClickable(true);
    }


    public void setRecyclerViewDoctorList() {
        doctorAdapter = new HospitalDoctorAdapter(context, doctorListSub);
        recyclerViewDoctorPackages.setAdapter(doctorAdapter);
        doctorAdapter.setOnClickListener(d -> {
            Intent intent1 = new Intent(context, DoctorAppointmentBookingActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            intent1.putExtra(KEY_DOCTOR, d);
            intent1.putExtra(KEY_HOSPITAL, hospitalModel);
            GlobalVar.errorLog(TAG, "KEY_DOCTOR", d.toString());
            startNewActivity(intent1);
        });

    }

    void startNewActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void getLikes() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("hid", hospitalModel.getId());
            obj.put("status", !hospitalInfo.isLikeStatus());
            obj.put("specializationId", hospitalModel.getSpecialization());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String url = GlobalVar.ServerAddress + "AndroidNew/CounthospLikes";
        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());
        GlobalVar.showProgressDialog(context, "", false);

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "getLikes", response.toString());
                    GlobalVar.hideProgressDialog();
                    try {
                        hospitalInfo.setLikeStatus(response.getBoolean("like"));
                        hospitalInfo.setLikes(response.getInt("TotalLikeCount"));
                        txtLikes.setText(String.valueOf(hospitalInfo.getLikes()));

                        if (hospitalInfo.isLikeStatus()) {
                            showSnackBarGreen("Hospital Liked");
                            txtLikes.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_liked_state, 0, 0);
                        } else {
                            showSnackBar("Hospital UnLiked");
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

    private void getFav() {
        PreferenceUtils pref = new PreferenceUtils(context);
        String url = GlobalVar.ServerAddress + "AndroidNew/CounthospFavorite";

        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("status", !hospitalInfo.isFavoriteStatus());
            obj.put("hid", hospitalModel.getId());
            obj.put("specializationId", hospitalModel.getSpecialization());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.showProgressDialog(context, "", false);
        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "getFav", response.toString());
                    try {
                        hospitalInfo.setFavoriteStatus(response.getBoolean("favorite"));
                        if (hospitalInfo.isFavoriteStatus()) {
                            showSnackBarGreen("Hospital Successfully Added To Favourite");
                            txtFav.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_liked_state, 0, 0);
                        } else {
                            showSnackBar("Hospital Successfully Removed From Favourite");
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

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    private void showSnackBarGreen(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.green);
    }

}
