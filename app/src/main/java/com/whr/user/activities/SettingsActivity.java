package com.whr.user.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.com.WHR.adapters.NewFAQActivityAdapter;
import com.whr.user.pojo.FAQPOJO;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    String strWhrLink;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransitionExit();
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void configViews() {
        ///------------New Toolbar Code----------------
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        ///------------New Toolbar code------------------------------

        strWhrLink = getString(R.string.hi_friends_i_discover_new_health_care_app_whr_user_app_please_download_it) + " " + "\n" +
                "https://play.google.com/store/apps/details?id=com.whr.user" + "\n" + "And" + "\n" +
                "https://itunes.apple.com/in/app/whr/id1389789779?mt=8";
    }

    Context context;
    ConnectionDector connectionDector;
    RequestQueue mQueue;
    String TAG = getClass().getSimpleName();
    PreferenceUtils pref;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;
        connectionDector = new ConnectionDector(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        configViews();
        pref = new PreferenceUtils(context);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
    }

    public void onButtonClickLogout(View view) {
        logoutUser();
    }

    public void onButtonClickRateApp(View view) {
        if (connectionDector.isConnectingToInternet()) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.whr.user");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.whr.user")));
            }
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }
    }

    public void onButtonClickChangeLanguage(View view) {
        dialogChangeLanguage("");
    }

    public void onButtonClickFaq(View view) {
        dialogFAQ("");
    }

    public void onButtonClickLiveChat(View view) {
        //Toast.makeText(this, "Live Chat", Toast.LENGTH_SHORT).show();
    }

    public void onButtonClickCallNow(View view) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + GlobalVar.Call_number));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        new TedPermission(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check();
    }

    public void onButtonClickServiceRequest(View view) {
        if (connectionDector.isConnectingToInternet()) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{GlobalVar.WHR_SUPPORT_EMAIL});
            startActivity(emailIntent);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }


    }

    public void onButtonClickServiceReportIssue(View view) {
        if (connectionDector.isConnectingToInternet()) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{GlobalVar.WHR_SUPPORT_EMAIL});
            startActivity(emailIntent);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }


    }

    private List<FAQPOJO> rowListItem = new ArrayList<>();
    NewFAQActivityAdapter newFAQActivityAdapter;
    Gson gson = new Gson();
    AlertDialog alertDialogAndroid;

    public void dialogAboutUs(View view) {
        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.layout_dialog_about_us);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        ImageView imageClose = dialog.findViewById(R.id.imageClose);
        final TextView txtAboutUs = dialog.findViewById(R.id.txtAboutUs);
        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.incrementProgressBy(10);
        progressBar.setIndeterminate(true);
        dialog.show();

        imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        if (connectionDector.isConnectingToInternet()) {
            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, GlobalVar.ServerAddress + "user/AboutUs", null,
                    response -> {
                        progressBar.setVisibility(View.GONE);
                        GlobalVar.errorLog(TAG, "getAboutUs :=>", response.toString());
                        JSONObject jobj = null;
                        try {
                            jobj = response.getJSONObject("AboutUs");
                            if (jobj != null && jobj.length() > 0) {
                                String text = jobj.getString("AboutUs");
                                txtAboutUs.setText(text);
                            } else {
                                GlobalVar.showSnackBar(coordinatorLayout, "No Data Available", context, R.color.red);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                progressBar.setVisibility(View.GONE);
                GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
            });
            jsonRequest.setTag("get");
            mQueue.add(jsonRequest);
        } else {
            progressBar.setVisibility(View.GONE);
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }

    }

    public void dialogRateWhrApp(String strAboutUs) {
        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.rating_layout);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        ImageView imageClose = dialog.findViewById(R.id.imageClose);
        TextView txtSocial = dialog.findViewById(R.id.txtSocial);
        dialog.show();


        txtSocial.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.whr.user");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.whr.user")));
            }
        });

        imageClose.setOnClickListener(v -> {
            dialog.cancel();
            dialog.dismiss();
        });

    }

    public void dialogChangeLanguage(String strAboutUs) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_layout_change_language, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setCancelable(true);
        alertDialogBuilderUserInput.setView(mView);

        ImageView imageClose = mView.findViewById(R.id.imageClose);
        TextView txtApply = mView.findViewById(R.id.txtApply);
        final CheckBox checkBoxEnglish = mView.findViewById(R.id.checkBoxEnglish);
        final CheckBox checkBoxMarathi = mView.findViewById(R.id.checkBoxMarathi);

        checkBoxMarathi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkBoxEnglish.setChecked(false);
            checkBoxMarathi.setChecked(isChecked);

        });

        checkBoxEnglish.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkBoxMarathi.setChecked(false);
            checkBoxEnglish.setChecked(isChecked);
        });

        if (pref.getLanguage().equalsIgnoreCase("en")) {
            checkBoxMarathi.setChecked(false);
            checkBoxEnglish.setChecked(true);
        } else if (pref.getLanguage().equalsIgnoreCase("mr")) {
            checkBoxMarathi.setChecked(true);
            checkBoxEnglish.setChecked(false);

        }

        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialogAndroid.setCancelable(true);
        alertDialogAndroid.show();

        txtApply.setOnClickListener(v -> {
            //Shared Preference For Language Change
            if (alertDialogAndroid != null && alertDialogAndroid.isShowing()) {
                alertDialogAndroid.cancel();
            }
            if (checkBoxMarathi.isChecked()) {
                setLocale("mr");
            } else if (checkBoxEnglish.isChecked()) {
                setLocale("en");
            }
        });

        imageClose.setOnClickListener(v -> {
            if (alertDialogAndroid != null && alertDialogAndroid.isShowing()) {
                alertDialogAndroid.cancel();
            }
        });

    }

    public void dialogFAQ(String strAboutUs) {
        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.activity_new_faq);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);
        final ImageView imageClose = dialog.findViewById(R.id.imageClose);

        final RecyclerView recyclerView = dialog.findViewById(R.id.hospitalListRecycleView);
        LinearLayoutManager lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        dialog.show();

        imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });


        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        progressBar.incrementProgressBy(10);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        if (connectionDector.isConnectingToInternet()) {
            String url = GlobalVar.ServerAddress + "User/DisplayAllFAQ";
            JSONObject obj = new JSONObject();
            try {
                obj.put("type", 3);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url,
                    obj, response -> {
                Log.e("getFAQ :=>", response.toString());
                progressBar.setVisibility(View.GONE);

                JSONArray jsonArray1 = null;
                try {
                    rowListItem.clear();
                    jsonArray1 = response.getJSONArray("result");
                    if (jsonArray1 != null && jsonArray1.length() > 0) {
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject json = jsonArray1.getJSONObject(i);
                            FAQPOJO pojo = gson.fromJson(json.toString(), FAQPOJO.class);
                            rowListItem.add(pojo);
                        }
                        newFAQActivityAdapter = new NewFAQActivityAdapter(context, rowListItem);
                        recyclerView.setAdapter(newFAQActivityAdapter);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                progressBar.setVisibility(View.GONE);
                GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);

            });
            jsonRequest.setTag("get");
            mQueue.add(jsonRequest);
        } else {
            progressBar.setVisibility(View.GONE);
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }

    }

    public void onButtonClickHelp(View view) {

        final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_help);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        ImageView imageClose=dialog.findViewById(R.id.imageClose);
        imageClose.setOnClickListener(v->{
            dialog.dismiss();
        });


        dialog.show();


    }


    public void logoutUser() {
        PreferenceUtils pref = new PreferenceUtils(context);
        pref.setClear();
        Intent i = new Intent(context, SplashScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    public void setLocale(String localeName) {
        pref.setLanguage(localeName);
        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, NevigationDrawerDashBordActivity.class);
        refresh.putExtra(currentLang, localeName);
        startActivity(refresh);
        finish();
    }

    String currentLanguage = "en", currentLang;
    Locale myLocale;
}