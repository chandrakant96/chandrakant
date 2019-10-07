package com.whr.user.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.whr.user.com.WHR.Utils.AndroidMultiPartEntity;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.booking.diagnostics.booking.activities.NewPathalogyActivity;
import com.whr.user.booking.doctor.booking.DoctorSpecialtiesActivity;
import com.whr.user.booking.packages.hospital.activities.NewSurgeryPackageActivity;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.com.WHR.ViewPackage.CustomTypefaceSpan;


import com.whr.user.first_aid.activities.FirstAidActivity;
import com.whr.user.governmentscheme.GovernmentSchemeActivity;
import com.whr.user.pay_bill.PayBillTabActivity;
import com.whr.user.pojo.CustomSliderView;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.wallet.WalletTabActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NevigationDrawerDashBordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    private TextView dashbordUID, heder_name, wallet_pointtxt, favourite, searchBox, badge_notificatio_counter, notification;
    private CircleImageView userProfileImage;
    private LinearLayout EmergencyContactLayout, mainlayoutone, searchImaglayout, linearLayoutNotifacation;
    private RelativeLayout hospitalListlayout, doctor_layout;
    private CoordinatorLayout coordinatorLayout;
    private MaterialRippleLayout pathologyBooking, firstAidTips, governmentSchemes;

    private ConnectionDector connectionDector;
    private ActionBarDrawerToggle toggle;
    private PreferenceUtils pref;
    private Context context;
    private RequestQueue mQueue;
    String strWhrLink;
    String TAG = NevigationDrawerDashBordActivity.class.getSimpleName();
    TextView txtLocation;
    public static JSONObject jsonObject = null;


    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nevigation_drawer_dash_bord);

        overridePendingTransitionEnter();
        context = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        pref = new PreferenceUtils(context);
        connectionDector = new ConnectionDector(context);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notification = findViewById(R.id.notification);
        firstAidTips = findViewById(R.id.firstAidTips);
        governmentSchemes = findViewById(R.id.governmentSchemes);
        txtLocation = findViewById(R.id.txtLocation);
        searchBox = findViewById(R.id.auto_complete_textview);
        HideSoft(searchBox);
        searchImaglayout = findViewById(R.id.searchImaglayout);
        searchImaglayout.setVisibility(View.VISIBLE);
        coordinatorLayout = findViewById(R.id.dashbordContainer);
        mDemoSlider = findViewById(R.id.slider);
        badge_notificatio_counter = findViewById(R.id.badge_notificatio_counter);
        linearLayoutNotifacation = toolbar.findViewById(R.id.linearLayoutNotifacation);
        favourite = toolbar.findViewById(R.id.favourite);

        NavigationView navigationView = findViewById(R.id.new_navigationView_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View header = navigationView.getHeaderView(0);

        dashbordUID = header.findViewById(R.id.daashbordUID);
        heder_name = header.findViewById(R.id.user_heder_name);
        wallet_pointtxt = header.findViewById(R.id.wallet_pointtxt);
        userProfileImage = header.findViewById(R.id.userProfileImage);
        EmergencyContactLayout = findViewById(R.id.EmergencyContactLayout);
        doctor_layout = findViewById(R.id.nev_doctor_layout);
        hospitalListlayout = findViewById(R.id.hospitalListlayout);
        pathologyBooking = findViewById(R.id.pathologyBooking);
        mainlayoutone = findViewById(R.id.mainlayoutone);
        mainlayoutone.setVisibility(View.VISIBLE);
        LinearLayout findPlacelayout = findViewById(R.id.findPlacelayout);

        pref.setLogin(true);
        txtLocation.setText(LocationFindActivity.myLocation);

        strWhrLink = getString(R.string.hi_friends_i_discover_new_health_care_app_whr_user_app_please_download_it) + " " + "\n" +
                "\n" + "https://play.google.com/store/apps/details?id=com.whr.user" + "\n\n" +
                "https://itunes.apple.com/in/app/whr/id1389789779?mt=8";

        ArrayList<String> list = new ArrayList<String>();
        list.add("Pathology");
        list.add("NearBy");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        notification.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_notification), null, null, null);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        txtLocation.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_location), null, null, null);

        mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

        badge_notificatio_counter.setVisibility(View.GONE);

        favourite.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_favourite), null, null, null);

        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            applyFontToMenuItem(mi);
        }

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (txtLocation.getText().toString().isEmpty()) {
                    getLocationActivityDialog();
                } else {

                }

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check();


        if (connectionDector.isConnectingToInternet()) {
            getUserDetails();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }

        firstAidTips.setOnClickListener(v -> {
            Intent i = new Intent(NevigationDrawerDashBordActivity.this, FirstAidActivity.class);
            String strName = null;
            i.putExtra("FirstAddLayout", "firstAid");
            startActivity(i);
        });

        findPlacelayout.setOnClickListener(v -> {
            Intent i = new Intent(context, LocationFindActivity.class);
            startActivityForResult(i, 1);
            overridePendingTransitionEnter();
        });

        txtLocation.setOnClickListener(v -> {
            Intent i = new Intent(context, LocationFindActivity.class);
            startActivityForResult(i, 1);
            overridePendingTransitionEnter();
        });

        linearLayoutNotifacation.setOnClickListener(v -> {
            Intent myIntent = new Intent(context, NotificationActivity.class);
            startNewActivity(myIntent);
        });

        favourite.setOnClickListener(v -> {
            Intent myIntent = new Intent(context, NewFavouriteActivity.class);
            startNewActivity(myIntent);
        });

        EmergencyContactLayout.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.activity_emergency);
            Window window = dialog.getWindow();
            assert window != null;
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            window.setAttributes(wlp);

            MaterialRippleLayout paid, free;
            paid = dialog.findViewById(R.id.callPaidAmbulanceService);
            free = dialog.findViewById(R.id.callFreeAmbulanceService);

            paid.setOnClickListener(v13 -> {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+918698210210"));
                startActivity(intent);

            });

            free.setOnClickListener(v14 -> {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "108"));
                startActivity(intent);
            });


            dialog.show();
        });

        doctor_layout.setOnClickListener(v -> {
            Intent i = new Intent(context, DoctorSpecialtiesActivity.class);
            i.putExtra("favarouteCategories", "normal");
            startNewActivity(i);
        });

        hospitalListlayout.setOnClickListener(v -> {

        });

        pathologyBooking.setOnClickListener(v -> {
            Intent i = new Intent(NevigationDrawerDashBordActivity.this, NewPathalogyActivity.class);
            i.putExtra("fromCashless", "");
            i.putExtra("favarouteCategories", "");
            i.putExtra("favarouteCategories", "");
            i.putExtra("status", "");
            startNewActivity(i);

        });

        EmergencyContactLayout.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.activity_emergency);
            Window window = dialog.getWindow();
            assert window != null;
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            window.setAttributes(wlp);

            MaterialRippleLayout paid, free;
            paid = dialog.findViewById(R.id.callPaidAmbulanceService);
            free = dialog.findViewById(R.id.callFreeAmbulanceService);

            paid.setOnClickListener(v1 -> {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+918698210210"));
                startActivity(intent);

            });

            free.setOnClickListener(v12 -> {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "108"));
                startActivity(intent);
            });


            dialog.show();
        });

        hospitalListlayout.setOnClickListener(v -> {
            Intent i2 = new Intent(context, NewSurgeryPackageActivity.class);
            i2.putExtra("text", "");
            i2.putExtra("status", "normal");
            i2.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            startNewActivity(i2);
        });

        searchBox.setOnClickListener(view -> startNewActivity(new Intent(context, RecentTopSearchActivity.class)));

        governmentSchemes.setOnClickListener(v -> {
            startNewActivity(new Intent(context, GovernmentSchemeActivity.class));
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getUserDetails();
        } else
            txtLocation.setText(LocationFindActivity.myLocation);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface myFont = Typeface.createFromAsset(getAssets(), "OpenSans-Semibold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", myFont), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer1 = findViewById(R.id.drawer_layout);
        if (drawer1.isDrawerOpen(GravityCompat.START)) {
            drawer1.closeDrawer(GravityCompat.START);
        } else {
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.                                                                                       New
        int id = item.getItemId();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //initializing the fragment object which is selected
        item.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
        switch (id) {
            case R.id.nav_myProfile:
                Intent myAccountActivity = new Intent(context, MyAccountActivity.class);
                startNewActivity(myAccountActivity);
            break;
            case R.id.nav_AddEmergency:
                Intent AddEmergencyFragmentintent = new Intent(context, AddContactEmergencyActivity.class);
                startNewActivity(AddEmergencyFragmentintent);
                break;
            case R.id.nav_treatment_Suggest:
                Intent i = new Intent(context, SuggestTestTabActivity.class);
                startNewActivity(i);
                break;
            case R.id.nav_appointments: {
                Intent i1 = new Intent(context, AppointmentHistoryActivity.class);
                startNewActivity(i1);
                break;
            }
            //
            case R.id.nav_refereedDoctor: {
                Intent i3 = new Intent(context, ReferDoctorList.class);
                startNewActivity(i3);
                break;
            }
            case R.id.nav_invite: {
                dialogShareAndEarn("");
                break;
            }

            case R.id.nav_Family: {
                Intent Family_MemberFragmenti = new Intent(context, FamilyMemberListActivity.class);
                startNewActivity(Family_MemberFragmenti);
                break;
            }
            case R.id.nav_reports: {
                Intent i4 = new Intent(context, ReportActivity.class);
                startNewActivity(i4);
                break;
            }
            case R.id.nav_life_style: {

                break;
            }
            case R.id.nav_setting: {
                Intent i5 = new Intent(context, SettingsActivity.class);
                startNewActivity(i5);
                break;
            }
            case R.id.nav_PayBill: {

                Intent i5 = new Intent(context, PayBillTabActivity.class);
                startNewActivity(i5);
                break;
            }
            case R.id.nav_wallet: {
                Intent i5 = new Intent(context, WalletTabActivity.class);
                startNewActivity(i5);
                break;
            }
        }
        //replacing the fragment
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void getUserDetails() {
        JSONObject obj = new JSONObject();
        String url = GlobalVar.ServerAddress + "AndroidNew/GetUserInfoNew";

        try {
            obj.put("uid", Long.parseLong(pref.getUID()));
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, response -> {
            jsonObject = response;
            parseUserResponse(response);
        }, error -> GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context),
                context, R.color.red
        ));
        customJSONObjectRequest.setTag("get");
        mQueue.add(customJSONObjectRequest);
    }


    private void parseUserResponse(JSONObject response) {
        try {
            JSONObject jsonRootobj = new JSONObject(response.toString());
            JSONObject jsonObject = jsonRootobj.getJSONObject("result");
            String uname = jsonObject.getString("uname");
            String uid = jsonObject.getString("uid");
            String wallet_point = jsonObject.getString("wallet_point");
            String wallet_discount = jsonObject.getString("wallet_discount");
            String customercareNo = jsonObject.getString("customercareNo");
            String consultationfreestatus = jsonObject.getString("consultationfreestatus");
            String flashmsg = jsonObject.getString("flashmsg");
            String cno = jsonObject.getString("cno");
            String profile_img = jsonObject.getString("profile_img");
            pref.setCno(cno);
            pref.setUserName(uname);
            pref.setconsultationfreestatus(consultationfreestatus);
            pref.setFlashmsg(flashmsg);

            if (cno.isEmpty()) {
                dialogFacebook();
            }

            if (customercareNo != null) {
                if (customercareNo.length() > 0) {
                    GlobalVar.Call_number = customercareNo;
                } else {
                    GlobalVar.Call_number = "918698210210";
                }
            } else {
                GlobalVar.Call_number = "918698210210";
            }
            if (wallet_discount != null) {
                if (wallet_discount.length() > 0) {
                    pref.setwallet_discount("");
                    pref.setwallet_discount(wallet_discount);
                }
            }
            if (uid != null) {
                dashbordUID.setText("WHR Id : " + uid);
            } else {
                dashbordUID.setText(pref.getUID());

            }
            if (uname != null) {
                heder_name.setText(uname);
            } else {
                heder_name.setVisibility(View.GONE);
            }
            if (wallet_point != null) {
                if (wallet_point.length() > 0) {
                    pref.setwallet_point("");
                    wallet_pointtxt.setText("WHR Wallet Points : " + wallet_point);
                    pref.setwallet_point(wallet_point);
                } else {
                    wallet_pointtxt.setVisibility(View.GONE);
                }
            } else {
                wallet_pointtxt.setVisibility(View.GONE);
            }

            if (profile_img != null && !profile_img.isEmpty()) {
                Picasso.with(context).load(profile_img)
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(userProfileImage);
            } else {
                userProfileImage.setImageResource(R.drawable.user_blue);
            }

            if (response.getString("noti_count") != null) {
                if (response.getString("noti_count").length() > 0) {
                    int noticount = Integer.parseInt(response.getString("noti_count"));
                    if (noticount == 0) {
                        badge_notificatio_counter.setVisibility(View.GONE);
                    } else {
                        badge_notificatio_counter.setVisibility(View.VISIBLE);
                        badge_notificatio_counter.setText(response.getString("noti_count") + "");
                    }
                } else {
                    badge_notificatio_counter.setVisibility(View.GONE);
                }
            } else {
                badge_notificatio_counter.setVisibility(View.GONE);
            }

            mDemoSlider.removeAllSliders();
            JSONArray jsonArray1 = response.getJSONArray("dashbordImageArry");

            GlobalVar.errorLog(TAG, "jsonArray1", jsonArray1.toString());
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject json = jsonArray1.getJSONObject(i);
                String slideUrl = json.getString("sliderImage");
                slideUrl = slideUrl.replaceAll(" ", "%20");
                CustomSliderView textSliderView = new CustomSliderView(this);
                textSliderView
                        .image(slideUrl)
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                mDemoSlider.addSlider(textSliderView);
            }

            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AlertDialog hospitalLocationConfornDialog;

    public void getOfferConsultationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.wallet_point_add, null);
        builder.setView(view);
        builder.setCancelable(true);

        TextView alterboxtitleBox = view.findViewById(R.id.alterboxtitleBox);
        alterboxtitleBox.setText(getString(R.string.das_offers));

        TextView newpastextshowtext = view.findViewById(R.id.newpastextshowtext);
        newpastextshowtext.setText(pref.getFlashmsg());


        Button yesBtn = view.findViewById(R.id.forgotsubmit);
        ImageView closeBtn = view.findViewById(R.id.fotgetclose);
        yesBtn.setOnClickListener(v -> {
            if (hospitalLocationConfornDialog != null && hospitalLocationConfornDialog.isShowing()) {
                hospitalLocationConfornDialog.dismiss();
            }
        });

        closeBtn.setOnClickListener(v -> {
            if (hospitalLocationConfornDialog != null && hospitalLocationConfornDialog.isShowing()) {
                hospitalLocationConfornDialog.dismiss();
            }
        });

        hospitalLocationConfornDialog = builder.create();
        hospitalLocationConfornDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void startNewActivity(Intent intent) {
        HideSoft(searchBox);
        startActivity(intent);
        overridePendingTransitionEnter();
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.startAutoCycle();
        GlobalVar.errorLog(TAG, "OnStart", "onstart");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");

        if (connectionDector.isConnectingToInternet()) {
            getUserDetails();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (txtLocation.getText().toString().isEmpty()) {
                    getLocationActivityDialog();
                } else {

                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        new TedPermission(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

    }


    private void getLocationActivityDialog() {
        try {

            final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.facebook_verify_activity2);
            Window window = dialog.getWindow();
            assert window != null;
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            window.setAttributes(wlp);

            TextView alterboxtitleBox = dialog.findViewById(R.id.alterboxtitleBox);
            Button forgotsubmit = dialog.findViewById(R.id.forgotsubmit);
            ImageView forgetetclose = dialog.findViewById(R.id.fotgetclose);
            alterboxtitleBox.setText(getString(R.string.location));


            forgetetclose.setOnClickListener(v -> {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            });

            forgotsubmit.setOnClickListener(v -> {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Intent i = new Intent(context, LocationFindActivity.class);
                startActivityForResult(i, 1);
                overridePendingTransitionEnter();
            });
        } catch (Exception ex) {
            Log.e("Alert Method", ex.toString());
        }
    }

    public void dialogShareAndEarn(String strAboutUs) {

        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.share_app_layout);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);


        ImageView imageClose = dialog.findViewById(R.id.imageClose);
        TextView txtSocial = dialog.findViewById(R.id.txtSocial);
        TextView txtContacts = dialog.findViewById(R.id.txtContacts);
        TextView txtDescription = dialog.findViewById(R.id.txtDescription);
        dialog.show();

        txtSocial.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, strWhrLink);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        });


        txtContacts.setOnClickListener(v -> {
            dialog.dismiss();
            if (new ConnectionDector(context).isConnectingToInternet()) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        new SendSMSUserContactList().execute();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        GlobalVar.showSnackBar(coordinatorLayout, "Permission Denied\n" + deniedPermissions.toString(), context, R.color.red);
                    }
                };
                new TedPermission(context)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.READ_CONTACTS)
                        .check();
            } else {
                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
            }
        });

        imageClose.setOnClickListener(v -> dialog.dismiss());

    }

    public class SendSMSUserContactList extends AsyncTask<Object, Object, String> {

        boolean running;
        private long totalSize;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            running = true;
            //progress = ProgressDialog.show(context, "Getting Contact", "Wait!");
            progress = new ProgressDialog(context);
            progress.setMessage("Getting Contact Please wait");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(Object... params) {

            String responstring = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(GlobalVar.ServerAddress + "User/SendSMSUserContactList");

            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(num -> {
                //   publishProgress(num);
                //  publishProgress(String.valueOf((int) ((num / (float) totalSize) * 100)));
            });

            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cur.getCount() > 0) {
                progress.setMax(cur.getCount());
                int ctr = 0;
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) + ",";
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Log.e("name", name + ", ID : " + id);
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            System.out.println("user_phoneno_list" + phone);

                            Log.e("name", "user_phoneno_list" + phone);
                            contactnolist.add(phone);
                        }
                        pCur.close();
                    }
                    ctr++;
                    publishProgress(ctr);
                }

            }

            if (contactnolist.size() > 0) {
                String nostr = "";
                if (contactnolist.size() > 0) {

                    for (String numberstr : contactnolist) {
                        nostr += numberstr + ",";
                    }
                    Log.e("onPostExecute_nostr", nostr);
                }
                // root= ;
                try {
                    entity.addPart("uid", new StringBody(pref.getUID() + ""));
                    entity.addPart("appurl", new StringBody(strWhrLink));
                    entity.addPart("cno", new FileBody(generateNoteOnSD("contactFile.txt", nostr)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            totalSize = entity.getContentLength();
            httppost.setEntity(entity);
            HttpResponse response = null;
            try {
                response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responstring = EntityUtils.toString(r_entity);
                    Log.e("response", responstring);
                } else {
                    Log.e("response", "Error occurred! Http Status Code: " + statusCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return responstring;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            progress.setProgress((Integer) values[0]);
        }


        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            isContactTrue = true;

            if (aVoid != null) {
                if (aVoid.length() > 0) {
                    if (senContactpdialog != null && senContactpdialog.isShowing()) {
                        senContactpdialog.dismiss();
                    }
                    try {
                        JSONObject response = new JSONObject(aVoid);
                        Log.e("bookingAppoint :=>", response.toString());
                        boolean result = response.getBoolean("result");
                        if (result) {
                            GlobalVar.showSnackBar(coordinatorLayout, "Share Successfully", context, R.color.green);
                        } else {
                            GlobalVar.showSnackBar(coordinatorLayout, "Please Try Again", context, R.color.red);
                        }
                    } catch (JSONException e) {
                        if (senContactpdialog != null && senContactpdialog.isShowing()) {
                            senContactpdialog.dismiss();
                        }
                        e.printStackTrace();
                    }
                }
            }

            if (contactnolist.size() > 0) {
                String nostr = "";
                if (contactnolist.size() > 0) {
                    for (String numberstr : contactnolist) {
                        nostr += numberstr + ",";
                    }
                    Log.e("onPostExecute_nostr", nostr);
                }
                //  senContact(nostr);
            }
        }
    }

    private List<String> contactnolist = new ArrayList<>();
    private File root;
    private ProgressDialog progress, senContactpdialog;
    private boolean isContactTrue = true;

    public File generateNoteOnSD(String sFileName, String sBody) {
        try {
            root = new File(Environment.getExternalStorageDirectory(), "Contacts");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile, false);
            writer.append(sBody);
            writer.flush();
            writer.close();
            return gpxfile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void dialogFacebook() {

        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.facebook_verify_activity);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);


        ImageView imageClose = dialog.findViewById(R.id.imgClose);
        TextView txtverifyNow = dialog.findViewById(R.id.txtverifyNow);
        EditText etxmobileNumber = dialog.findViewById(R.id.etxmobileNumber);
        dialog.show();

        txtverifyNow.setOnClickListener(v -> {
            dialog.dismiss();
            int randomPIN;
            randomPIN = (int) (Math.random() * 9000) + 1000;
            Log.e("Random Otp", Integer.toString(randomPIN));
            otpWebSerViceCall(randomPIN, etxmobileNumber.getText().toString());

        });

        imageClose.setOnClickListener(v -> dialog.dismiss());

    }

    private void otpWebSerViceCall(final int otpCode, String number) {
        GlobalVar.showProgressDialog(this, "Loading....", false);
        String url = GlobalVar.ServerAddress + "Android/SendOtpToAllNumber";
        JSONObject params = new JSONObject();
        try {
            params.put("cno", number);
            params.put("key", otpCode);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "params", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, params,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "SendOtpToUser", response.toString());
                    try {
                        boolean result = response.getBoolean("result");
                        if (result) {
                            if (checkAndRequestPermissions()) {
                                GlobalVar.errorLog(TAG, "permission granted", "lll");
                            } else {
                                GlobalVar.errorLog(TAG, "permission denied", "lll");
                            }
                            Intent i = new Intent(context, OtpScreenActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putString("mobNumber", number);
                            mBundle.putString("key", String.valueOf(otpCode));
                            mBundle.putString("status", "fbgoogle");
                            i.putExtras(mBundle);
                            i.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                            context.startActivity(i);
                        } else {
                            GlobalVar.showSnackBar(coordinatorLayout, "User Already Registered", context, R.color.red);
                        }
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

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}