package com.whr.user.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CommanUtils;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.com.WHR.adapters.DoctorListAutoCompleatBoxAdapter;
import com.whr.user.com.WHR.adapters.ReffredDoctorListAdapter;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pojo.ReffredDoctorPojo;
import com.whr.user.pojo.ShowDoctorListFromHospitalActivityPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReferDoctorList extends AppCompatActivity {
    String uid = "";
    AlertDialog dialog;
    Calendar today;

    private RecyclerView recyclerView;

    private TextView fromDate, toDate;

    private LinearLayout addFamily, doctorNameEdittextDialog;
    // AddFamilyAdapter adapter;
    private Context context;
    private LinearLayoutManager lLayout;
    private AutoCompleteTextView refrededitext;

    private ReffredDoctorListAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    private PreferenceUtils pref;

    private boolean newfirst = true;
    private int doctorId = -1;
    private String fromdatestr = "", todatestr = "", doctorNamestr = "";
    private long To_timeInMilliseconds, From_timeInMilliseconds, advanceFrom_timeInMilliseconds, advanceTo_timeInMilliseconds;
    private AlertDialog alertDialog, doctorIdreffredalertDialog, AdvanceSearchalertDialog, customeSearchalertDialog;

    private List<ReffredDoctorPojo> list;
    private List<ShowDoctorListFromHospitalActivityPojo> doctorList;
    private String url;
    private RequestQueue mQueue;
    private boolean isStringsearchByDocid = false;
    private DoctorListAutoCompleatBoxAdapter doctorListAutoCompleatBoxAdapter;
    private AutoCompleteTextView doctorSearch;
    private boolean refrededitextboolean = false;
    List<ShowDoctorListFromHospitalActivityPojo> docList = new ArrayList<>();
    private EditText searchBox;
    private String favstr = "";
    private LinearLayout mainCointenerToreplace;

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.new_reffred_me_fragment_layout);
        context = this;
        ///------------New Toolbar Code----------------
        ImageView txtLocation = findViewById(R.id.txtLocation);
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        txtLocation.setOnClickListener(v -> getDialogFilter());
        ///------------New Toolbar code------------------------------
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        searchBox = findViewById(R.id.auto_complete_textview);
        searchBox.setHint(getString(R.string.EnterDoctorName));
        LinearLayout findPlacelayout = findViewById(R.id.findPlacelayout);
        findPlacelayout.setVisibility(View.INVISIBLE);
        pref = new PreferenceUtils(context);
        list = new ArrayList<>();
        doctorList = new ArrayList<>();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        recyclerView = findViewById(R.id.reffredDoctorRecycleView);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        adapter = new ReffredDoctorListAdapter(context, list);
        recyclerView.setAdapter(adapter);

        if (new ConnectionDector(context).isConnectingToInternet()) {
            //   getAllDoctorList();
            GetAllMyReffered(pref.getUID(), "", "", -1, true);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                adapter.getFilter(query.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            GetAllMyReffered(pref.getUID(), "", "", -1, true);
        }
        if (requestCode == GlobalVar.NO_DATA_AVAILABLE) {
            onBackPressed();
        }
    }


    String TAG = getClass().getSimpleName();

    public void getAllDoctorList() {
        String urlJsonArry = GlobalVar.ServerAddress + "AndroidNew/GetDoctorsList";
        GlobalVar.errorLog(TAG, "url", urlJsonArry);

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, urlJsonArry, null, response -> {
            GlobalVar.errorLog(TAG, "getAllDoctorList:=>", response.toString());
            pref.setDoctorList("");
            // GlobalVar.hideProgressDialog();
            Gson gson = new Gson();
            JSONArray jsonArray1 = null;
            try {
                pref.setDoctorList(response.toString());
                jsonArray1 = response.getJSONArray("doctorlist");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject json = jsonArray1.getJSONObject(i);
                    ShowDoctorListFromHospitalActivityPojo pojo = gson.fromJson(json.toString(), ShowDoctorListFromHospitalActivityPojo.class);
                    doctorList.add(pojo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red));
        mQueue.add(jsonRequest);
    }

    private void GetAllMyReffered(String uid, String fromdatestr, String todatestr, int doctorId, boolean newfirst) {
        url = GlobalVar.ServerAddress + "AndroidNew/GetAllMyReffered";
        GlobalVar.showProgressDialog(this, "Loading....", true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", uid);
            obj.put("fromdate", fromdatestr);
            obj.put("todate", todatestr);
            obj.put("did", doctorId);
            obj.put("newfirst", newfirst);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, response -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.errorLog(TAG, "GetAllMyReffered", response.toString());
            list.clear();
            Gson gson = new Gson();
            JSONArray jsonArray1 = null;
            try {
                jsonArray1 = response.getJSONArray("result");
                if (jsonArray1.length() > 0) {

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json = jsonArray1.getJSONObject(i);
                        ReffredDoctorPojo pojo = gson.fromJson(json.toString(), ReffredDoctorPojo.class);
                        list.add(pojo);
                    }
                    ReferDoctorList.this.newfirst = false;
                    ReferDoctorList.this.fromdatestr = "";
                    ReferDoctorList.this.todatestr = "";
                    ReferDoctorList.this.doctorId = -1;

                    //adapter.notifyDataSetChanged();
                } else {
                    startActivityForResult(new Intent(context, NoDataAvailableActivity.class), GlobalVar.NO_DATA_AVAILABLE);
                    overridePendingTransitionEnter();
                    // GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                }
                adapter.notifyDataSetChanged();

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

    private void AdvanceSearchAlertBox() {
        AlertDialog.Builder AdvanceSearchbuilder = new AlertDialog.Builder(ReferDoctorList.this);
        LayoutInflater AdvanceSearchinflater = getLayoutInflater();

        final View dialogView = AdvanceSearchinflater.inflate(R.layout.reffred_doctor_custom_date_picker_dialog, null);
        AdvanceSearchbuilder.setView(dialogView);
        fromDate = (TextView) dialogView.findViewById(R.id.from_customdialog_datetext);
        toDate = (TextView) dialogView.findViewById(R.id.tocustomdialog_datetext);
        Button advancesearchbutton = (Button) dialogView.findViewById(R.id.searchbutton);

        TextView title = (TextView) dialogView.findViewById(R.id.alterboxtitleBox);
        ImageView close = (ImageView) dialogView.findViewById(R.id.imageView8);
        LinearLayout doctorNameEdittextDialog = (LinearLayout) dialogView.findViewById(R.id.doctorNameEdittextDialog);
        refrededitext = (AutoCompleteTextView) dialogView.findViewById(R.id.ReffredFromeditText);
        title.setText(getString(R.string.AdvanceSearch));
        doctorNameEdittextDialog.setVisibility(View.VISIBLE);

        refrededitext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                docList.clear();
                String respStr = pref.getDoctoList();
                if (respStr != null) {
                    if (respStr.length() > 0) {
                        Gson gson = new Gson();
                        JSONArray jsonArray1 = null;
                        try {
                            JSONObject jsonObject = new JSONObject(respStr);
                            jsonArray1 = jsonObject.getJSONArray("doctorlist");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                ShowDoctorListFromHospitalActivityPojo pojo = gson.fromJson(json.toString(), ShowDoctorListFromHospitalActivityPojo.class);
                                docList.add(pojo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                String someText = String.valueOf(s);
                refrededitextboolean = someText.matches(".*\\d+.*");
                doctorListAutoCompleatBoxAdapter = new DoctorListAutoCompleatBoxAdapter(context, 0, docList, refrededitextboolean);
                refrededitext.setAdapter(doctorListAutoCompleatBoxAdapter);
                refrededitext.setThreshold(0);
                refrededitext.showDropDown();
                refrededitext.setOnItemClickListener((adapterView, view, position, id) -> {
                    ShowDoctorListFromHospitalActivityPojo model = (ShowDoctorListFromHospitalActivityPojo) adapterView.getItemAtPosition(position);

                    doctorNamestr = "";
                    doctorId = 0;

                    String unameSttter = ((TextView) ((LinearLayout) view).getChildAt(0)).getText().toString();
                    String uisStr = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
                    refrededitext.setText(unameSttter + "");
                    doctorNamestr = unameSttter + "";
                    doctorId = Integer.parseInt(uisStr);
                    Log.e("doctor_NAme_Id", doctorNamestr + " " + doctorId);
                });


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        today = Calendar.getInstance();
        fromDate.setOnClickListener(v -> {
            DatePickerDialog dp = new DatePickerDialog(ReferDoctorList.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    DecimalFormat formatter = new DecimalFormat("00");
                    today.set(year, monthOfYear, dayOfMonth);
                    fromDate.setText(formatter.format(dayOfMonth) + "-" + formatter.format(monthOfYear + 1) + "-" + year);
                    fromdatestr = fromDate.getText().toString().trim();
                    advanceFrom_timeInMilliseconds = today.getTimeInMillis();
                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            dp.getDatePicker().setMaxDate(System.currentTimeMillis());
            dp.getDatePicker().setMinDate(1965);
            dp.show();
        });
        toDate.setOnClickListener(v -> {
            DatePickerDialog dp = new DatePickerDialog(ReferDoctorList.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    DecimalFormat formatter = new DecimalFormat("00");
                    today.set(year, monthOfYear, dayOfMonth);
                    advanceTo_timeInMilliseconds = today.getTimeInMillis();
                    Log.e("From_to", advanceFrom_timeInMilliseconds + "  to " + advanceTo_timeInMilliseconds);
                    if (advanceFrom_timeInMilliseconds <= advanceTo_timeInMilliseconds) {
                        Log.e("From_to", "True");
                        today.set(year, monthOfYear, dayOfMonth);
                        toDate.setText(formatter.format(dayOfMonth) + "-" + formatter.format(monthOfYear + 1) + "-" + year);
                        todatestr = toDate.getText().toString().trim();
                    } else {
                        Log.e("From_to", "False");
                        Toast.makeText(ReferDoctorList.this, "Please Select Max Date", Toast.LENGTH_SHORT).show();
                    }
                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            dp.getDatePicker().setMaxDate(System.currentTimeMillis());
            dp.getDatePicker().setMinDate(advanceFrom_timeInMilliseconds);
            dp.show();
        });
        close.setOnClickListener(v -> {
            advanceFrom_timeInMilliseconds = 0;
            advanceTo_timeInMilliseconds = 0;
            AdvanceSearchalertDialog.cancel();
        });
        AdvanceSearchalertDialog = AdvanceSearchbuilder.create();
        AdvanceSearchalertDialog.show();
        advancesearchbutton.setOnClickListener(v -> {
            if (CommanUtils.isEmpty(refrededitext.getText().toString())) {
                GlobalVar.showSnackBarView(dialogView, getString(R.string.PleaseEnterDoctorName), context, R.color.red);
                return;
            } else if (CommanUtils.isEmpty(fromDate.getText().toString().trim())) {
                GlobalVar.showSnackBarView(dialogView, getString(R.string.PleaseSelectFromDate), context, R.color.red);
                return;
            } else if (CommanUtils.isEmpty(toDate.getText().toString().trim())) {
                GlobalVar.showSnackBarView(dialogView, getString(R.string.PleaseSelectToDate), context, R.color.red);
                return;
            } else if (doctorId == 0 || doctorId == -1) {
                GlobalVar.showSnackBarView(dialogView, getString(R.string.PleaseEnterDoctorName), context, R.color.red);
                return;
            } else {
                AdvanceSearchalertDialog.dismiss();
                advanceFrom_timeInMilliseconds = 0;
                advanceTo_timeInMilliseconds = 0;
                newfirst = true;
                GetAllMyReffered(pref.getUID(), fromdatestr, todatestr, doctorId, newfirst);
            }
        });

    }

    private void CustomIddateSearchAlertBox() {


        AlertDialog.Builder CustomIddatebuilder = new AlertDialog.Builder(ReferDoctorList.this);
        LayoutInflater CustomIddatehinflater = getLayoutInflater();
        //  LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View CustomIddatedialogView = CustomIddatehinflater.inflate(R.layout.reffred_doctor_custom_date_picker_dialog, null);
        CustomIddatebuilder.setView(CustomIddatedialogView);
        Button searchbutton = (Button) CustomIddatedialogView.findViewById(R.id.searchbutton);

        fromDate = (TextView) CustomIddatedialogView.findViewById(R.id.from_customdialog_datetext);
        toDate = (TextView) CustomIddatedialogView.findViewById(R.id.tocustomdialog_datetext);
        TextView CustomIddatedialogViewtitle = (TextView) CustomIddatedialogView.findViewById(R.id.alterboxtitleBox);
        ImageView close = (ImageView) CustomIddatedialogView.findViewById(R.id.imageView8);
        LinearLayout customedoctorNameEdittextDialog = (LinearLayout) CustomIddatedialogView.findViewById(R.id.doctorNameEdittextDialog);

        CustomIddatedialogViewtitle.setText(getString(R.string.CustomDateSearch));
        customedoctorNameEdittextDialog.setVisibility(View.GONE);
        today = Calendar.getInstance();

        fromDate.setOnClickListener(v -> {
            DatePickerDialog dp = new DatePickerDialog(ReferDoctorList.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            DecimalFormat formatter = new DecimalFormat("00");
                            today.set(year, monthOfYear, dayOfMonth);
                            fromDate.setText(formatter.format(dayOfMonth) + "-" + formatter.format(monthOfYear + 1) + "-" + year);
                            fromdatestr = fromDate.getText().toString().trim();
                            From_timeInMilliseconds = today.getTimeInMillis();
                            //==========================
                        }
                    }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            dp.getDatePicker().setMaxDate(System.currentTimeMillis());
            dp.getDatePicker().setMinDate(1965);
            dp.show();
        });


        toDate.setOnClickListener(v -> {
            DatePickerDialog dp = new DatePickerDialog(ReferDoctorList.this, (view, year, monthOfYear, dayOfMonth) -> {
                DecimalFormat formatter = new DecimalFormat("00");

                today.set(year, monthOfYear, dayOfMonth);
                To_timeInMilliseconds = today.getTimeInMillis();
                Log.e("From_to", From_timeInMilliseconds + "  to " + To_timeInMilliseconds);
                if (From_timeInMilliseconds <= To_timeInMilliseconds) {
                    Log.e("From_to", "True");
                    today.set(year, monthOfYear, dayOfMonth);
                    toDate.setText(formatter.format(dayOfMonth) + "-" + formatter.format(monthOfYear + 1) + "-" + year);
                    todatestr = toDate.getText().toString().trim();
                } else {
                    Log.e("From_to", "False");
                    Toast.makeText(ReferDoctorList.this, "Please Select Max Date", Toast.LENGTH_SHORT).show();
                }
                //=======================================================
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            dp.getDatePicker().setMaxDate(System.currentTimeMillis());
            dp.getDatePicker().setMinDate(From_timeInMilliseconds);
            dp.show();
        });


        close.setOnClickListener(v -> {
            From_timeInMilliseconds = 0;
            To_timeInMilliseconds = 0;
            customeSearchalertDialog.cancel();
        });


        customeSearchalertDialog = CustomIddatebuilder.create();
        customeSearchalertDialog.show();
        searchbutton.setOnClickListener(v -> {
            if (CommanUtils.isEmpty(fromDate.getText().toString().trim()) || CommanUtils.isEmpty(toDate.getText().toString().trim())) {
                GlobalVar.showSnackBarView(CustomIddatedialogView, "Please Select From Date,To Date And Family Member", context, R.color.red);
            } else {
                customeSearchalertDialog.dismiss();
                From_timeInMilliseconds = 0;
                To_timeInMilliseconds = 0;
                newfirst = true;
                doctorId = -1;
                customeSearchalertDialog.dismiss();
                GetAllMyReffered(pref.getUID(), fromdatestr, todatestr, doctorId, newfirst);
            }
        });
    }

    private void searchByDoctorNameDialogBox() {


        AlertDialog.Builder doctorIdreffredbuilder = new AlertDialog.Builder(ReferDoctorList.this);
        LayoutInflater doctorIdreffredinflater = getLayoutInflater();
        final View doctorIdreffreddialogView = doctorIdreffredinflater.inflate(R.layout.doctor_id_name__wise_select_picker_dialog_box, null);
        doctorIdreffredbuilder.setView(doctorIdreffreddialogView);

        doctorSearch = (AutoCompleteTextView) doctorIdreffreddialogView.findViewById(R.id.searchdoctoreditText);
        Button searchByDoctor = (Button) doctorIdreffreddialogView.findViewById(R.id.searchByDoctor);

        ImageView close = (ImageView) doctorIdreffreddialogView.findViewById(R.id.imageView8);

        close.setOnClickListener(v -> doctorIdreffredalertDialog.cancel());


        doctorSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                docList.clear();
                String respStr = pref.getDoctoList();
                if (respStr != null) {
                    if (respStr.length() > 0) {
                        Gson gson = new Gson();
                        JSONArray jsonArray1 = null;
                        try {
                            JSONObject jsonObject = new JSONObject(respStr);
                            jsonArray1 = jsonObject.getJSONArray("doctorlist");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                ShowDoctorListFromHospitalActivityPojo pojo = gson.fromJson(json.toString(), ShowDoctorListFromHospitalActivityPojo.class);
                                docList.add(pojo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String someText = String.valueOf(s);
                isStringsearchByDocid = someText.matches(".*\\d+.*");
                doctorListAutoCompleatBoxAdapter = new DoctorListAutoCompleatBoxAdapter(context, 0, docList, isStringsearchByDocid);
                doctorSearch.setAdapter(doctorListAutoCompleatBoxAdapter);
                doctorSearch.setThreshold(0);
                //  doctorSearch.showDropDown();
                doctorSearch.setOnItemClickListener((adapterView, view, position, id) -> {
                    ShowDoctorListFromHospitalActivityPojo model = (ShowDoctorListFromHospitalActivityPojo) adapterView.getItemAtPosition(position);
                    String unameSttter = ((TextView) ((LinearLayout) view).getChildAt(0)).getText().toString();
                    String uisStr = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
                    //   String userPimag = ((TextView) ((LinearLayout) view).getChildAt(2)).getText().toString();
                    doctorSearch.setText(unameSttter + "");
                    doctorNamestr = unameSttter + "";
                    doctorId = Integer.parseInt(uisStr);
                    Log.e("doctor_NAme_Id", doctorNamestr + " " + doctorId);
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        doctorIdreffredalertDialog = doctorIdreffredbuilder.create();
        doctorIdreffredalertDialog.show();

        searchByDoctor.setOnClickListener(v -> {
            if (doctorId == 0 || doctorId == -1) {
                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.PleaseSelectFamilyMember), context, R.color.red);
            } else {
                doctorIdreffredalertDialog.dismiss();
                newfirst = true;
                fromdatestr = "";
                todatestr = "";
                GetAllMyReffered(pref.getUID(), fromdatestr, todatestr, doctorId, newfirst);
            }
        });

    }

    @Override
    protected void onDestroy() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void getDialogFilter() {
        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.layout_dialog_appointment_filter);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        RadioButton radioUpcoming = dialog.findViewById(R.id.radioUpcoming);
        radioUpcoming.setText("Date (Newest First)");
        RadioButton radioFollowUp = dialog.findViewById(R.id.radioFollowUp);
        radioFollowUp.setText("Date (Oldest First)");
        RadioButton radioHistory = dialog.findViewById(R.id.radioHistory);
        radioHistory.setVisibility(View.GONE);
        RadioButton radioPathology = dialog.findViewById(R.id.radioPathology);
        radioPathology.setVisibility(View.GONE);
        RadioButton radioAdvance = dialog.findViewById(R.id.radioAdvance);
        radioAdvance.setVisibility(View.GONE);

        radioUpcoming.setOnClickListener(v -> {
            dialog.cancel();
            dialog.dismiss();
            newfirst = true;
            fromdatestr = "";
            todatestr = "";
            doctorId = -1;
            GetAllMyReffered(pref.getUID(), fromdatestr, todatestr, doctorId, newfirst);
        });

        radioFollowUp.setOnClickListener(v -> {
            dialog.cancel();
            dialog.dismiss();
            newfirst = false;
            fromdatestr = "";
            todatestr = "";
            doctorId = -1;
            GetAllMyReffered(pref.getUID(), fromdatestr, todatestr, doctorId, newfirst);
        });

        radioPathology.setOnClickListener(v -> {
            dialog.cancel();
            dialog.dismiss();
            CustomIddateSearchAlertBox();
        });

        radioHistory.setOnClickListener(view -> {
            dialog.cancel();
            dialog.dismiss();
            searchByDoctorNameDialogBox();
        });

        radioAdvance.setOnClickListener(view -> {
            dialog.cancel();
            dialog.dismiss();
            AdvanceSearchAlertBox();
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransitionExit();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

}