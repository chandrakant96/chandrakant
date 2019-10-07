package com.whr.user.booking.diagnostics.booking.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;

import com.whr.user.activities.CentralSearchActivity;
import com.whr.user.booking.adapters.RecyclerAdapterCentralSearch;
import com.whr.user.booking.diagnostics.booking.adapter.NewPathologyReviewAdapter;
import com.whr.user.booking.diagnostics.booking.model.PathologyDetailList;

import com.whr.user.booking.doctor.booking.BookingDetailsAdmitNowActivity;
import com.whr.user.booking.doctor.booking.DetailHospitalActivity;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomStringRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.diagnostics.booking.adapter.FilterRecyclerViewAdapter;
import com.whr.user.booking.diagnostics.booking.adapter.PathologyListAdapter;
import com.whr.user.booking.diagnostics.booking.model.PathologyListPojo;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.activities.LocationFindActivity;
import com.whr.user.model.CentralSearchModel;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewPathalogyActivity extends AppCompatActivity {
    private RecyclerView pathologyRecyclerView, recyclerViewFilter1;
    private LinearLayoutManager lLayout, lLayout2;
    private Context context;
    private PathologyListAdapter adapter;
    private ConnectionDector connectionDector;
    private PreferenceUtils pref;
    protected String TAG = getClass().getSimpleName();
    ArrayList<Object> rowListItem = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    public static final String KEY_PATHOLOGY_MODEL = "PATHOLOGY_MODEL";
    private EditText searchBox;
    private TextView txtLocation, txtFilter;
    private MaterialRippleLayout imgBack;
    private String testid = "";

    private String type = " ", nearby = " ", minfee = " ", maxfee = " ";
    private String filter = "";
    ArrayList<String> list = new ArrayList<String>();
    FilterRecyclerViewAdapter filterRecyclerViewAdapter;
    public static final String KEY_CENTRAL_SEARCH_PATHOLOGY = "central_pathology";
    Gson gson = new Gson();

    LinearLayout layoutCentralSearch;
    ProgressBar pb_loading;
    RecyclerAdapterCentralSearch adapterSearch;
    RecyclerView recyclerViewCentralSearch;
    String searchType = "", searchText = "";
    String availableTestName = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pathalogy);

        context = NewPathalogyActivity.this;
        connectionDector = new ConnectionDector(context);
        pref = new PreferenceUtils(context);
        rowListItem = new ArrayList<>();
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imgBack = findViewById(R.id.imgBack);
        searchBox = findViewById(R.id.edSearch);
        txtLocation = findViewById(R.id.txtLocation);
        txtFilter = findViewById(R.id.txtFilter);

        layoutCentralSearch = findViewById(R.id.layoutCentralSearch);
        pb_loading = findViewById(R.id.pb_loading);

        searchBox.setHint("Pathology Lab");
        searchBox.setClickable(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            testid = bundle.getString("testid");
            type = bundle.getString("type");
            nearby = bundle.getString("nearby");
            minfee = bundle.getString("minfee");
            maxfee = bundle.getString("maxfee");
            maxfee = bundle.getString("maxfee");
            filter = bundle.getString("status");
            list = bundle.getStringArrayList("list");
            searchText = bundle.getString("text");
            searchType = bundle.getString("type");
        }

        pathologyRecyclerView = findViewById(R.id.pathologyListRecyclerView);
        lLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        pathologyRecyclerView.setLayoutManager(lLayout);
        pathologyRecyclerView.setHasFixedSize(true);


        recyclerViewFilter1 = findViewById(R.id.recyclerViewFilter);
        lLayout2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFilter1.setLayoutManager(lLayout2);
        recyclerViewFilter1.setHasFixedSize(true);
        filterRecyclerViewAdapter = new FilterRecyclerViewAdapter(context, list);
        recyclerViewFilter1.setAdapter(filterRecyclerViewAdapter);
        filterRecyclerViewAdapter.notifyDataSetChanged();

        imgBack.setOnClickListener(v -> onBackPressed());


        if (LocationFindActivity.myLocation != null) {
            if (LocationFindActivity.myLocation.length() > 0) {
                txtLocation.setText(LocationFindActivity.myLocation);
            } else {
                txtLocation.setText(getString(R.string.location));
            }
        } else {
            txtLocation.setText(getString(R.string.location));
        }
        adapter = new PathologyListAdapter(context, rowListItem);
        pathologyRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        if (connectionDector.isConnectingToInternet()) {
            if (filter.equals("filter")) {
                GetPathologyListWithFilter();

            } else if (filter.equals("central")) {
                if (searchType.equals("Test")) {
                    availableTestName = searchText;
                    searchBox.setHint(searchText);
                } else {
                    searchBox.setHint(searchText);
                    availableTestName = "NOTTEST";
                }
                GetPathologyListAfterSearch(searchText, LocationFindActivity.lat, LocationFindActivity.lon, searchType);
            } else {
                GetPathologyList();
            }
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }


        adapter.setClickLister(new PathologyListAdapter.ClickLister() {
            @Override
            public void PathologyBookClick(PathologyListPojo pathologyListPojo) {
                if (pathologyListPojo.getAvailablepackege().equals("0")) {
                    if (pathologyListPojo.getId().equals("50017")) {
                        Intent intent = new Intent(context, ThyrocareListActivity.class);
                        startNewActivity(intent);
                    } else {
                        Intent intent = new Intent(context, PathalogyServicesAndChargesActivity.class);
                        intent.putExtra("pathid", pathologyListPojo.getId());
                        intent.putExtra("pathname", pathologyListPojo.getPathologyName());
                        if (pathologyListPojo.getType().contains("Pathology")) {
                            intent.putExtra("typeid", "1");
                            Log.e("typeid", pathologyListPojo.getType());
                        } else {
                            intent.putExtra("typeid", "2");
                        }
                        startActivity(intent);
                    }
                } else {
                    getDialogBookingPathalogyPackage(pathologyListPojo);
                }


            }

          /*  @Override
            public void PathologyrDetailClick(PathologyListPojo pathologyListPojo) {
                Intent intent = new Intent(context, NewPathalogyDetailsActivity.class);
                intent.putExtra("pathid", pathologyListPojo.getId());
                intent.putExtra("pathname", pathologyListPojo.getPathologyName());
                if (pathologyListPojo.getType().contains("Pathology")) {
                    intent.putExtra("typeid", "1");
                    Log.e("typeid", pathologyListPojo.getType());
                } else {
                    intent.putExtra("typeid", "2");
                }
                startActivity(intent);

            }*/

            @Override
            public void PathologyrReviewClick(PathologyListPojo pathologyListPojo) {
                getDialogBookingReview(pathologyListPojo);
            }

            @Override
            public void PathologyLikeClick(PathologyListPojo pathologyListPojo) {
                getLikes(pathologyListPojo);
            }

            @Override


            public void PathologyFaveClick(PathologyListPojo pathologyListPojo) {
                getFav(pathologyListPojo);
            }

            @Override
            public void PathologyShareClick(PathologyListPojo pathologyListPojo) {
                final Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                StringBuilder str = new StringBuilder();
                String link = "http://user.whrhealth.com/Home/GetPathologyAppointment/?speId=" + pathologyListPojo.getId()
                        + "&lat=&log=&reqfrom=1";
                str.append(pathologyListPojo.getPathologyName()).append("\nAddress : ").
                        append(pathologyListPojo.getAddress()).append("\nVisit To : " + link);

                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, str.toString());
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
            }
        });


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.length() > 0) {
                    layoutCentralSearch.setVisibility(View.VISIBLE);
                    //lineSearch.setBackgroundColor(context.getResources().getColor(R.color.white));
                    txtFilter.setVisibility(View.GONE);
                    getCentralData(query.toString());
                } else {
                    layoutCentralSearch.setVisibility(View.GONE);
                    //lineSearch.setBackgroundColor(context.getResources().getColor(R.color.primary));
                    txtFilter.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recyclerViewCentralSearch = findViewById(R.id.recyclerViewCentralSearch);
        LinearLayoutManager lLayout3 = new LinearLayoutManager(context);
        recyclerViewCentralSearch.setLayoutManager(lLayout3);
        recyclerViewCentralSearch.setHasFixedSize(true);
        adapterSearch = new RecyclerAdapterCentralSearch(context, listSearch, RecyclerAdapterCentralSearch.CENTRAL_SEARCH);
        recyclerViewCentralSearch.setAdapter(adapterSearch);
        adapterSearch.setOnClickListener(this::getNextActivity);


        txtLocation.setOnClickListener(v -> {
            Intent i = new Intent(context, LocationFindActivity.class);
            startActivityForResult(i, 1);
        });

        txtFilter.setOnClickListener(v -> {
            startNewActivity(new Intent(context, NewPathalogyFilterList.class));
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent adata) {
        super.onActivityResult(requestCode, resultCode, adata);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            if (filter == null) {
                GetPathologyList();
            } else {
                GetPathologyListWithFilter();
            }
        } else {
            txtLocation.setText(LocationFindActivity.myLocation);
            finish();
            startActivity(getIntent());
        }
    }

    private void GetPathologyList() {
        String url = GlobalVar.ServerAddress + "AndroidNew/NewPathologyList";
        rowListItem.clear();
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("testid", testid);
            obj.put("lat", LocationFindActivity.lat);
            obj.put("log", LocationFindActivity.lon);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.showProgressDialog(this, "", false);
        GlobalVar.errorLog(TAG, " obj", obj.toString());

        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    JSONArray jsonArray1 = null;
                    GlobalVar.errorLog(TAG, "url", url);
                    GlobalVar.errorLog(TAG, "GetPathologyList response", response.toString());

                    try {
                        jsonArray1 = response.optJSONArray("PathologyList");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            rowListItem.clear();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                PathologyListPojo pojo = gson.fromJson(json.toString(), PathologyListPojo.class);
                                pojo.setDistance(GlobalVar.distance(pojo.getLatitude(), pojo.getLongitude()));
                                rowListItem.add(pojo);

                            }

                        }

                        adapter.notifyDataSetChanged();
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

    private void GetPathologyListWithFilter() {
        recyclerViewFilter1.setVisibility(View.VISIBLE);
        String url = GlobalVar.ServerAddress + "AndroidNew/GetPathologyDetails";
        rowListItem.clear();
        JSONObject obj = new JSONObject();
        try {
            obj.put("userId", pref.getUID());
            obj.put("lat", String.valueOf(LocationFindActivity.lat));
            obj.put("log", String.valueOf(LocationFindActivity.lon));
            obj.put("Type", type);
            obj.put("nearby", nearby);
            obj.put("minfee", minfee);
            obj.put("maxfee", maxfee);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.showProgressDialog(this, "", false);
        GlobalVar.errorLog(TAG, "orl", url);
        GlobalVar.errorLog(TAG, " obj", obj.toString());

        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    JSONArray jsonArray1 = null;
                    GlobalVar.errorLog(TAG, "url", url);
                    GlobalVar.errorLog(TAG, "GetPathologyList filter", response.toString());

                    try {
                        jsonArray1 = response.optJSONArray("category");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            rowListItem.clear();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                PathologyListPojo pojo = gson.fromJson(json.toString(), PathologyListPojo.class);
                                rowListItem.add(pojo);
                            }

                        }

                        adapter.notifyDataSetChanged();
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

    ArrayList<CentralSearchModel> listSearch = new ArrayList<>();

    private void getCentralData(final String query) {
        pb_loading.setVisibility(View.VISIBLE);
        String url = GlobalVar.ServerAddress + "AndroidNew/GetAllPathologySearchData";

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, response -> {
            pb_loading.setVisibility(View.GONE);
            listSearch.clear();
            //   recyclerAdapterCentralSearch.notifyDataSetChanged();

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonData = jsonObject.getJSONArray("data");
                if (jsonData.length() > 0) {
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject json = jsonData.getJSONObject(i);
                        CentralSearchModel pojo = gson.fromJson(json.toString(), CentralSearchModel.class);
                        listSearch.add(pojo);
                    }
                    adapterSearch.notifyDataSetChanged();
                } else {
                    // GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }, error -> {
            pb_loading.setVisibility(View.GONE);
            showSnackBar(VolleyErrorHelper.getMessage(error, context));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("text", query);
                map.put("city", "");
                Log.e("url", url);
                Log.e("response", map.toString());
                return map;

            }
        };
        mQueue.add(stringRequest);
    }

    private void getNextActivity(CentralSearchModel c) {

        GlobalVar.hideProgressDialog();
        switch (c.getType()) {
            case "tbl_pathologylabTest": {
                availableTestName = c.getName();
                Log.e("availableTestName", availableTestName);
                GetPathologyListAfterSearch(searchBox.getText().toString().trim(), LocationFindActivity.lat, LocationFindActivity.lon, "Test");
                searchBox.setText("" + c.getName());
                layoutCentralSearch.setVisibility(View.GONE);
                break;
            }
            case "tbl_pathologylab": {
                availableTestName = "NOTTEST";
                Log.e("availableTestName", availableTestName);
                GetPathologyListAfterSearch(searchBox.getText().toString().trim(), LocationFindActivity.lat, LocationFindActivity.lon, "Pathology");
                searchBox.setText("" + c.getName());
                layoutCentralSearch.setVisibility(View.GONE);
                break;
            }
        }
    }

    private void GetPathologyListAfterSearch(String text, double lat, double log, String type) {
        String url = GlobalVar.ServerAddress + "AndroidNew/NewCentralSearch";
        rowListItem.clear();
        JSONObject obj = new JSONObject();
        try {
            obj.put("searchtext", text);
            obj.put("lat", lat);
            obj.put("log", log);
            obj.put("texttype", type);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.showProgressDialog(this, "", false);
        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, " obj", obj.toString());

        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();

                    JSONArray jsonArray1 = null;
                    GlobalVar.errorLog(TAG, "url", url);
                    GlobalVar.errorLog(TAG, "GetPathologyListAfterSearch", response.toString());

                    try {
                        jsonArray1 = response.optJSONArray("ObjectList");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            rowListItem.clear();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                PathologyListPojo pojo = gson.fromJson(json.toString(), PathologyListPojo.class);
                                pojo.setDistance(GlobalVar.distance(pojo.getLatitude(), pojo.getLongitude()));
                                pojo.setAvailabletestName(availableTestName);
                                rowListItem.add(pojo);
                            }

                        }

                        adapter.notifyDataSetChanged();
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

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void startNewActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransitionEnter();
    }

    ArrayList<PathologyDetailList.Reviews> reviewsArrayList = new ArrayList<>();

    private void getDialogBookingReview(PathologyListPojo pathologyListPojo) {
        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.layout_dialog_pathology_review);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        TextView txtReviews = dialog.findViewById(R.id.txtReviews);
        TextView txtAddReview = dialog.findViewById(R.id.txtAddReview);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        RecyclerView recyclerViewReview = dialog.findViewById(R.id.recyclerViewReview);

        NewPathologyReviewAdapter newPathologyReviewAdapter;
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewReview.setHasFixedSize(true);
        newPathologyReviewAdapter = new NewPathologyReviewAdapter(reviewsArrayList, context);
        recyclerViewReview.setAdapter(newPathologyReviewAdapter);


        txtReviews.setOnClickListener(view -> {

        });

        txtAddReview.setOnClickListener(v -> {
            dialog.dismiss();
            getDialogBookingAddReview(pathologyListPojo);
        });

        dialog.show();

        String url = GlobalVar.ServerAddress + "AndroidNew/PathologyReview";
        JSONObject obj = new JSONObject();
        try {
            obj.put("pid", pathologyListPojo.getId());
            obj.put("userId", pref.getUID());
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, response -> {
            progressBar.setVisibility(View.GONE);

            try {

                JSONArray jsonArrayReviews = response.optJSONArray("Reviews");
                if (jsonArrayReviews != null && jsonArrayReviews.length() > 0) {
                    reviewsArrayList.clear();
                    for (int i = 0; i < jsonArrayReviews.length(); i++) {
                        JSONObject reviewsJSONObject = jsonArrayReviews.getJSONObject(i);
                        PathologyDetailList.Reviews reviews = gson.fromJson(reviewsJSONObject.toString(), PathologyDetailList.Reviews.class);
                        reviewsArrayList.add(reviews);
                    }
                    newPathologyReviewAdapter.notifyDataSetChanged();
                    txtReviews.setText("Reviews (" + reviewsArrayList.size() + ")");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressBar.setVisibility(View.GONE);
        });
        mQueue.add(jsonObjectRequest);
    }

    private void getDialogBookingAddReview(PathologyListPojo pathologyListPojo) {
        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.layout_dialog_pathology_add_review);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);
        EditText edUserName = dialog.findViewById(R.id.edUserName);
        EditText edReview = dialog.findViewById(R.id.edReview);
        TextView txtPostReviews = dialog.findViewById(R.id.txtPostReviews);
        dialog.show();


        txtPostReviews.setOnClickListener(v -> {
            if (edUserName.getText().toString().isEmpty()) {
                GlobalVar.showSnackBar(coordinatorLayout, "Please Enter Your Name", context, R.color.red);
            } else if (edReview.getText().toString().isEmpty()) {
                GlobalVar.showSnackBar(coordinatorLayout, "Please Enter Review", context, R.color.red);
            } else {

                GlobalVar.showProgressDialog(context, "Posting Review..", false);
                String url = GlobalVar.ServerAddress + "AndroidNew/AddReview";
                JSONObject params = new JSONObject();
                try {
                    params.put("pid", pathologyListPojo.getId());
                    params.put("userid", pref.getUID());
                    params.put("name", edUserName.getText().toString());
                    params.put("doctorId", pathologyListPojo.getId());
                    params.put("recommend", "");
                    params.put("valueformoney", "");
                    params.put("thingsimproved", "");
                    params.put("userexperience", edReview.getText().toString());
                    GlobalVar.errorLog(TAG, "url", url);
                    GlobalVar.errorLog(TAG, "obj", params.toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                        url, params, response -> {
                    GlobalVar.hideProgressDialog();
                    dialog.dismiss();
                    GlobalVar.showSnackBar(coordinatorLayout, "Review Posted", context, R.color.green);
                }, error -> {
                    GlobalVar.hideProgressDialog();
                });
                mQueue.add(jsonObjectRequest);
            }
        });
    }

    private void getLikes(PathologyListPojo pathologyListPojo) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("hid", pathologyListPojo.getId());
            obj.put("status", !pathologyListPojo.isLikeStatus());
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
                        pathologyListPojo.setLikeStatus(response.getBoolean("like"));
                        pathologyListPojo.setLikes(String.valueOf(response.getInt("TotalLikeCount")));
                        if (pathologyListPojo.isLikeStatus()) {
                            showSnackBar(context.getString(R.string.PathologyLiked));
                        } else {
                            showSnackBar(context.getString(R.string.PathologyUnliked));
                        }
                        adapter.notifyDataSetChanged();
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

    private void getFav(PathologyListPojo pathologyListPojo) {
        PreferenceUtils pref = new PreferenceUtils(context);
        String url = GlobalVar.ServerAddress + "AndroidNew/PathologyFavorite";

        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("hid", pathologyListPojo.getId());
            obj.put("status", !pathologyListPojo.isFavoriteStatus());

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
                        pathologyListPojo.setFavoriteStatus(response.getBoolean("favorite"));
                        if (pathologyListPojo.isFavoriteStatus()) {
                            showSnackBar(context.getString(R.string.PathologyAddedToFavouriteSuccessfully));
                        } else {
                            showSnackBar("Pathology Successfully Removed From Favourite");
                        }
                        adapter.notifyDataSetChanged();
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

    private void getDialogBookingPathalogyPackage(PathologyListPojo pathologyListPojo) {
        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.layout_dialog_pathalogy_book);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        RadioButton txtBookTest = dialog.findViewById(R.id.txtBookTest);
        RadioButton txtPackage = dialog.findViewById(R.id.txtPackage);

        txtPackage.setChecked(false);
        txtBookTest.setChecked(false);

        txtBookTest.setOnClickListener(v -> {
            txtPackage.setChecked(false);
            if (pathologyListPojo.getId().equals("50017")) {
                Intent intent = new Intent(context, ThyrocareListActivity.class);
                startNewActivity(intent);
                dialog.dismiss();
            } else {
                Intent intent = new Intent(context, PathalogyServicesAndChargesActivity.class);
                intent.putExtra("pathid", pathologyListPojo.getId());
                intent.putExtra("pathname", pathologyListPojo.getPathologyName());
                if (pathologyListPojo.getType().contains("Pathology")) {
                    intent.putExtra("typeid", "1");
                    Log.e("typeid", pathologyListPojo.getType());
                } else {
                    intent.putExtra("typeid", "2");
                }
                startActivity(intent);
                dialog.dismiss();

            }

        });

        txtPackage.setOnClickListener(v -> {
            txtBookTest.setChecked(false);
            Intent intent = new Intent(context, PathologyPackageActivity.class);
            intent.putExtra("pathid", pathologyListPojo.getId());
            intent.putExtra("pathname", pathologyListPojo.getPathologyName());
            if (pathologyListPojo.getType().contains("Pathology")) {
                intent.putExtra("typeid", "1");
            } else {
                intent.putExtra("typeid", "2");
            }
            startActivity(intent);
            dialog.dismiss();
        });
        dialog.show();
    }
}