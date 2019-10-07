package com.whr.user.booking.diagnostics.booking.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.whr.user.booking.diagnostics.booking.model.ThyroCarePojo;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.booking.diagnostics.booking.adapter.SelectedTreatmentPathologyAdapter;
import com.whr.user.booking.diagnostics.booking.model.PathologyListPojo;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.booking.diagnostics.booking.adapter.TestTreatFeeAdapter;

import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.booking.diagnostics.booking.model.Suggested_TreatementPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PathalogyServicesAndChargesActivity extends AppCompatActivity {

    private TestTreatFeeAdapter adapter;
    private RecyclerView recyclerView, recyclerViewSelectedTreatment;
    private Context context;
    private LinearLayoutManager lLayout;
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    private String url = "";
    private ConnectionDector connectionDector;
    private PathologyListPojo pathologyListPojo;
    private PreferenceUtils pref;
    private AppCompatActivity activity;
    EditText searchBox;
    private List<Suggested_TreatementPojo> rowListItem;
    String TAG = getClass().getSimpleName();
    TextView txtTitle, txtTotal;
    private List<Suggested_TreatementPojo> selectedTreatmentList = new ArrayList<>();
    private SelectedTreatmentPathologyAdapter selectedTreatmentAdapter;
    private MaterialRippleLayout bookTest;
    private MaterialRippleLayout imgBack;
    private String pathid = "", pathname = "";
    public static String typeid="";
    String pathAddressId = "";
    private TextView txtChooseTestTreatment, txtChooseTest;
    private Spinner spinnerAddressPathology;
    TextView txtdistance;

    ImageView imgProfile, imgClose;
    LinearLayout layoutPrescription;
    RelativeLayout layoutAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pathalogy_services_and_charges_activity_xml);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = PathalogyServicesAndChargesActivity.this;
        pref = new PreferenceUtils(context);
        connectionDector = new ConnectionDector(context);
        activity = PathalogyServicesAndChargesActivity.this;
        rowListItem = new ArrayList<>();
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        rowListItem = new ArrayList<>();

        txtdistance = findViewById(R.id.txtdistance);
        spinnerAddressPathology = findViewById(R.id.spinnerAddressPathology);
        recyclerView = findViewById(R.id.pathologyRecycleListRecycleView);
        recyclerViewSelectedTreatment = findViewById(R.id.recyclerViewSelectedTreatment);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imgBack = findViewById(R.id.imgBack);
        searchBox = findViewById(R.id.auto_complete_textview);
        txtTitle = findViewById(R.id.txtTitle);
        txtTotal = findViewById(R.id.txtTotal);
        bookTest = findViewById(R.id.bookTest);
        txtChooseTestTreatment = findViewById(R.id.txtChooseTestTreatment);
        txtChooseTest = findViewById(R.id.txtChooseTest);
        layoutPrescription = findViewById(R.id.layoutPrescription);
        layoutAddress = findViewById(R.id.layoutAddress);

        adapter = new TestTreatFeeAdapter(context, rowListItem, activity);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(adapter);

        imgBack.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            typeid = bundle.getString("typeid");
            pathid = bundle.getString("pathid");
            pathname = bundle.getString("pathname");

        }
        txtTitle.setText(pathname);

        searchBox.setHint("Search for a treatment package");
        Typeface myFont = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        searchBox.setTypeface(myFont);

        if (connectionDector.isConnectingToInternet()) {
            getTestTretFee();
            getPathologyAddress();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    txtChooseTest.setVisibility(View.VISIBLE);
                    txtChooseTestTreatment.setVisibility(View.VISIBLE);
                } else {
                    txtChooseTest.setVisibility(View.GONE);
                    txtChooseTestTreatment.setVisibility(View.GONE);
                }

                adapter.getFilterList(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSelectedTreatment.setLayoutManager(lLayout);
        selectedTreatmentAdapter = new SelectedTreatmentPathologyAdapter(context, selectedTreatmentList, activity);
        recyclerViewSelectedTreatment.setHasFixedSize(true);
        recyclerViewSelectedTreatment.setAdapter(selectedTreatmentAdapter);

        selectedTreatmentAdapter.setOnClickListener(d -> {
            if (!d.isCheck()) {
                d.setCheck(true);
                TestTreatFeeAdapter.totalval += Double.parseDouble(d.getWhrdiscounted_price() + " ");
                txtTotal.setText(GlobalVar.priceWithoutDecimal(TestTreatFeeAdapter.totalval));
            } else {
                d.setCheck(false);
                TestTreatFeeAdapter.totalval -= Double.parseDouble(d.getWhrdiscounted_price() + " ");
                txtTotal.setText(GlobalVar.priceWithoutDecimal(TestTreatFeeAdapter.totalval));
            }
            selectedTreatmentList.remove(d);
            selectedTreatmentAdapter.notifyDataSetChanged();

            for (Suggested_TreatementPojo suggested_treatementPojo : rowListItem) {
                if (suggested_treatementPojo.getTid().equals(d.getTid())) {
                    suggested_treatementPojo.setCheck(d.isCheck());
                }
            }
            adapter.notifyDataSetChanged();
        });

        adapter.setOnClickListener(d -> {
            if (!d.isCheck()) {
                d.setCheck(true);
                TestTreatFeeAdapter.totalval += Double.parseDouble(d.getWhrdiscounted_price() + " ");
                txtTotal.setText(GlobalVar.priceWithoutDecimal(TestTreatFeeAdapter.totalval));
                selectedTreatmentList.add(d);
            } else {
                d.setCheck(false);
                TestTreatFeeAdapter.totalval -= Double.parseDouble(d.getWhrdiscounted_price() + " ");
                txtTotal.setText(GlobalVar.priceWithoutDecimal(TestTreatFeeAdapter.totalval));
                selectedTreatmentList.remove(d);
            }

            adapter.notifyDataSetChanged();
            selectedTreatmentAdapter.notifyDataSetChanged();
        });

        bookTest.setOnClickListener(v -> {
            if (selectedTreatmentList.isEmpty()) {
                showSnackBar("Select Altleast One Test/Treatment");
            } else {
                Intent intent1 = new Intent(context, PathologyCalenderActivity.class);
                intent1.putParcelableArrayListExtra("pathologyTreatmentList", (ArrayList<? extends Parcelable>) selectedTreatmentList);
                intent1.putExtra("pathologyName", pathname);
                intent1.putExtra("pathid", pathid);
                intent1.putExtra("pathAddressId", pathAddressId);
                intent1.putExtra("total", TestTreatFeeAdapter.totalval);
                startActivity(intent1);
            }
        });


        if (pathid.contains("50189")) {
            layoutPrescription.setVisibility(View.VISIBLE);
            layoutAddress.setVisibility(View.GONE);
        } else {
            layoutPrescription.setVisibility(View.GONE);
            layoutAddress.setVisibility(View.VISIBLE);
        }

        layoutPrescription.setOnClickListener(v -> {
            dialogUploadImage();
        });

    }

    private void getTestTretFee() {
        GlobalVar.showProgressDialog(this, "Loading.....", true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("pathId", pathid);
            obj.put("typeid", typeid);
            obj.put("uid", pref.getUID());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        url = GlobalVar.ServerAddress + "AndroidNew/GetTestListByPathId";

        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "GetTreatmentList", response.toString());
                    GlobalVar.hideProgressDialog();
                    rowListItem.clear();
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("result");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                Suggested_TreatementPojo pojo = gson.fromJson(json.toString(), Suggested_TreatementPojo.class);
                                rowListItem.add(pojo);
                            }
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


    Gson gson = new Gson();
    ArrayAdapter<PathAddress> pathAddressAdapter;
    double lat = 0.0, lng = 0.0;

    private void getPathologyAddress() {
        url = GlobalVar.ServerAddress + "AndroidNew/Pathologyaddress";


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

                            pathAddressAdapter =
                                    new ArrayAdapter<>(context, R.layout.family_spinner_row,
                                            R.id.tvCategory, pathAddresses);
                            spinnerAddressPathology.setAdapter(pathAddressAdapter);

                            spinnerAddressPathology.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    PathAddress pathAddress = ((PathAddress)
                                            parent.getItemAtPosition(position));
                                    pathAddressId = pathAddress.getAddressId();
                                    lat = pathAddress.getLattitude();
                                    lng = pathAddress.getLongitude();
                                    Log.e("pathAddressId ", position + " " + pathAddressId);
                                    txtdistance.setText(GlobalVar.distance(lat, lng) + " km");
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                            txtdistance.setOnClickListener(v -> {
                                String strUri = null;
                                strUri = "http://maps.google.com/maps?q=loc:"
                                        + lat + ","
                                        + lng +
                                        " (" + pathname + ")";

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

    private void dialogUploadImage() {
        final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_prescription_image_upload);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        imgProfile = dialog.findViewById(R.id.imgProfile);
        TextView txtConfirm = dialog.findViewById(R.id.txtConfirm);
        imgClose = dialog.findViewById(R.id.imgClose);


        imgProfile.setOnClickListener(v -> {

            onSelectImageClick();

        });

        imgClose.setOnClickListener(v -> {
            dialog.dismiss();
        });


        dialog.show();
    }

    public void onSelectImageClick() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("My Crop")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Done")
                .setRequestedSize(400, 400)
                .setCropMenuCropButtonIcon(R.drawable.ic_done_white)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getTestTretFee();
        }

        // handle result of CropImageActivity

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Picasso.with(context).load(result.getUri()).fit().into(imgProfile, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                    }
                });
                toast(" # " + result.getSampleSize());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                toast("Failed: " + result.getError());
            }
        } else {

        }
    }

    @Override
    public void onBackPressed() {
        overridePendingTransitionExit();
        super.onBackPressed();
    }

    public void toast(String msg) {
        GlobalVar.toast(context, msg);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }



}
