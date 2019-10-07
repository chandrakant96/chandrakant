package com.whr.user.booking.diagnostics.booking.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.whr.user.activities.ReviewScreen;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.diagnostics.booking.adapter.NewPathologyReviewAdapter;
import com.whr.user.booking.diagnostics.booking.model.PathologyDetailList;
import com.whr.user.booking.diagnostics.booking.model.PathologyListPojo;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewPathalogyDetailsActivity extends AppCompatActivity {

    protected String TAG = getClass().getSimpleName();
    NestedScrollView scrollView;
    private TextView txtPathologyName, txtType, txtworkingFrom, txtAddress, txtLikes, txtDistnce, txtShare;
    private ImageView imagePathology;
    private TextView txtFav;
    private TextView txtReviews;
    private TextView txtReviews1;
    private TextView txtReviews2;
    private TextView txtWorkingHours;
    private MaterialRippleLayout addReview;
    private RecyclerView recyclerViewReview;
    private RequestQueue mQueue;
    private Context context;
    private PreferenceUtils pref;
    Gson gson = new Gson();
    ArrayList<PathologyDetailList.Reviews> reviewsArrayList = new ArrayList<>();
    NewPathologyReviewAdapter newPathologyReviewAdapter;
    LinearLayout lineReview;
    PathologyDetailList.Pathologyprofile pathologyprofile;
    PathologyListPojo pathologyListPojo;
    CoordinatorLayout coordinatorLayout;
    String pathid, pathname, typeid;
    MaterialRippleLayout imgBack;
    ConnectionDector dector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pathalogy_details);


        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        context = this;
        pref = new PreferenceUtils(context);
        dector = new ConnectionDector(context);

        TextView txtReadMoreReview = findViewById(R.id.txtReadMoreReview);
        recyclerViewReview = findViewById(R.id.recyclerViewReview);
        imagePathology = findViewById(R.id.imageDoctor);
        scrollView = findViewById(R.id.scrollView);
        txtFav = findViewById(R.id.txtFav);
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtReviews = findViewById(R.id.txtReviews);
        txtReviews1 = findViewById(R.id.txtReviews1);
        txtReviews2 = findViewById(R.id.txtReviews2);
        txtPathologyName = findViewById(R.id.txtHospitalName);
        txtType = findViewById(R.id.txtType);
        txtworkingFrom = findViewById(R.id.txtworkingFrom);
        txtAddress = findViewById(R.id.txtAddress);
        txtLikes = findViewById(R.id.txtLikes);
        txtDistnce = findViewById(R.id.txtDistance);
        lineReview = findViewById(R.id.lineReview);
        txtShare = findViewById(R.id.txtShare);
        txtWorkingHours = findViewById(R.id.txtWorkingHours);
        TextView txtbookAppoinment = findViewById(R.id.bookAppoentmentbtn);
        addReview = findViewById(R.id.addReview);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerViewReview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewReview.setHasFixedSize(true);
        newPathologyReviewAdapter = new NewPathologyReviewAdapter(reviewsArrayList, context);
        recyclerViewReview.setAdapter(newPathologyReviewAdapter);


        txtReadMoreReview.setOnClickListener(view -> scrollView.requestChildFocus(view, recyclerViewReview));

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReviewScreen.class);
                intent.putExtra("docterIdkey", pathid);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            pathid = bundle.getString("pathid");
            pathname = bundle.getString("pathname");
            typeid = bundle.getString("typeid");
        }
        txtTitle.setText(pathname);

        txtLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLikes();
            }
        });
        txtFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFav();
            }
        });

        txtShare.setOnClickListener(v -> {
            final Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            StringBuilder str = new StringBuilder();
            String link = "http://user.whrhealth.com/Home/GetPathologyAppointment/?speId=" + pathid + "&lat=&log=&reqfrom=1";
            str.append(pathname).append("\nAddress : ").
                    append(txtAddress.getText().toString()).append("\nVisit To : " + link);

            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, str.toString());
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
        });

        txtbookAppoinment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pathid.contains("50017")) {
                    Intent intent = new Intent(context, ThyrocareListActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, PathalogyServicesAndChargesActivity.class);
                    intent.putExtra("pathid", pathid);
                    intent.putExtra("pathname", pathname);
                    intent.putExtra("typeid", typeid);
                    startActivity(intent);
                }
            }
        });


        if (dector.isConnectingToInternet()) {
            getPathologyDetails();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getPathologyDetails();
        }
    }


    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void getPathologyDetails() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("pid", pathid);
            obj.put("userId", pref.getUID());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        GlobalVar.showProgressDialog(this, "", false);
        String url = GlobalVar.ServerAddress + "AndroidNew/PathologyProfile";

        GlobalVar.errorLog(TAG, " url", url);
        GlobalVar.errorLog(TAG, " obj", obj.toString());

        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    try {
                        JSONObject jsonObject = response.getJSONObject("PathologyProfile");
                        PathologyDetailList pathologyDetailList = gson.fromJson(jsonObject.toString(), PathologyDetailList.class);

                        JSONArray jsonArrayReviews = jsonObject.optJSONArray("Reviews");
                        if (jsonArrayReviews != null && jsonArrayReviews.length() > 0) {
                            for (int i = 0; i < jsonArrayReviews.length(); i++) {
                                JSONObject reviewsJSONObject = jsonArrayReviews.getJSONObject(i);
                                PathologyDetailList.Reviews reviews = gson.fromJson(reviewsJSONObject.toString(), PathologyDetailList.Reviews.class);
                                reviewsArrayList.add(reviews);
                                if (i < 4 && !reviews.getUserreview().isEmpty())
                                    txtReviews1.append("- " + reviews.getUserreview() + "\n");
                            }
                            pathologyDetailList.setReviews(reviewsArrayList);
                            newPathologyReviewAdapter.notifyDataSetChanged();
                            txtReviews.setText("Reviews (" + reviewsArrayList.size() + ")");
                            txtReviews2.setText("Reviews (" + reviewsArrayList.size() + ")");
                            lineReview.setVisibility(View.VISIBLE);
                        } else {
                            lineReview.setVisibility(View.GONE);
                        }

                        JSONObject jsonObjectPathologyProfile = jsonObject.optJSONObject("PathologyInfo");
                        if (jsonObjectPathologyProfile != null && jsonObjectPathologyProfile.length() > 0) {
                            pathologyprofile = gson.fromJson(jsonObjectPathologyProfile.toString(), PathologyDetailList.Pathologyprofile.class);
                            pathologyprofile.setDistance(GlobalVar.distance(pathologyprofile.getLatitude(), pathologyprofile.getLongitude()));

                            txtPathologyName.setText(pathologyprofile.getPathologyName());

                            if (typeid.contains("1")) {
                                txtType.setText("Pathology");

                            } else {
                                txtType.setText("Radiology");
                            }

                            if (pathologyprofile.getWorkingfrom()==null){
                                txtworkingFrom.setVisibility(View.GONE);
                                txtWorkingHours.setVisibility(View.GONE);
                            }else{
                                txtworkingFrom.setText(pathologyprofile.getWorkingfrom()+" - "+pathologyprofile.getWorkingto());
                            }
                            txtAddress.setText(pathologyprofile.getAddress());
                            txtDistnce.setText(String.valueOf(pathologyprofile.getDistance()));
                            txtLikes.setText(String.valueOf(pathologyprofile.getLikes()));

                            if (pathologyprofile.getProfile_img() != null && !pathologyprofile.getProfile_img().isEmpty()) {
                                Picasso.with(context).load(pathologyprofile.getProfile_img())
                                        .placeholder(R.drawable.ic_microscope).error(R.drawable.ic_microscope)
                                        .fit().into(imagePathology);
                            } else {
                                imagePathology.setImageResource(R.drawable.ic_microscope);
                            }

                            if (pathologyprofile.isFavoriteStatus()) {
                                txtFav.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_liked_state, 0, 0);
                            } else {
                                txtFav.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_unliked_state, 0, 0);
                            }

                            if (pathologyprofile.isLikeStatus()) {
                                txtLikes.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_liked_state, 0, 0);
                            } else {
                                txtLikes.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_unliked_state, 0, 0);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> GlobalVar.hideProgressDialog());
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    private void getLikes() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("hid", pathid);
            obj.put("status", !pathologyprofile.isLikeStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String url = GlobalVar.ServerAddress + "AndroidNew/PathologyLikes";
        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());
        GlobalVar.showProgressDialog(context, "", false);

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "getLikes", response.toString());
                    try {
                        pathologyprofile.setLikeStatus(response.getBoolean("like"));
                        pathologyprofile.setLikes(String.valueOf(response.getInt("TotalLikeCount")));
                        txtLikes.setText(String.valueOf(pathologyprofile.getLikes()));
                        if (pathologyprofile.isLikeStatus()) {
                            showSnackBarGreen(context.getString(R.string.PathologyLiked));
                            txtLikes.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_liked_state, 0, 0);
                        } else {
                            showSnackBar(context.getString(R.string.PathologyUnliked));
                            txtLikes.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_unliked_state, 0, 0);
                        }
                    } catch (JSONException e) {
                        e.getMessage();
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
        String url = GlobalVar.ServerAddress + "AndroidNew/PathologyFavorite";

        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("hid", pathid);
            obj.put("status", !pathologyprofile.isFavoriteStatus());

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
                        pathologyprofile.setFavoriteStatus(response.getBoolean("favorite"));
                        if (pathologyprofile.isFavoriteStatus()) {
                            showSnackBarGreen(context.getString(R.string.PathologyAddedToFavouriteSuccessfully));
                            txtFav.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_liked_state, 0, 0);
                        } else {
                            showSnackBar("Pathology Successfully Removed From Favourite");
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


    public void startNewActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransitionEnter();
    }
}
