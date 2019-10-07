package com.whr.user.pojo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.design.widget.CoordinatorLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.activities.NevigationDrawerDashBordActivity;
import com.whr.user.com.WHR.ViewPackage.CustomTypefaceSpan;
import com.whr.user.activities.LocationFindActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GlobalVar {
    //http://192.168.1.232:82/AndroidNew/ValidatePromoCode
    //vidya pc
    // public static String ServerAddress = "http://192.168.1.218/";
    //girish pc
    //public static String ServerAddress = "http://192.168.1.232:82/";
    public static String ServerAddress = "http://android.whrhealth.com/";
    public static boolean categoriesSpinnerFlage = false;

    public static double latitude;
    public static double longitude;
    public static String placesname;
    public static String MID = "LivWor74836176531967";
    public static String Website = "LivWorWAP";
    public static String Merchant_Key = "IEVtRc4_W74R_27o";
    public static String Industry_type_ID = "Retail109";
    public static String Channel_ID = "WAP";
    public static String CALLBACK_URL = "https://secure.paytm.in/oltp-web/processTransaction";

    public static String Call_number = "+918698210210";
    public static String ivr_auth_key = "vIbtD-Y3Zpr0Z1ouA-lyabJQ0X-mhvAaZIP";

    public static Dialog pDialog;
    static String TAG = "GlobalVar";
    public static int countProgress = 0;
    public static String WHR_SUPPORT_EMAIL = "support@whrhelath.com";
    public static String PAY_STATUS_DOCTOR = "1";
    public static String PAY_STATUS_PATHOLOGY = "2";
    public static String PAY_STATUS_HOSPITAL = "3";
    public static String PAY_STATUS_HOSPITAL_PACKAGES = "4";
    public static int NO_INTERNET_REQUEST_CODE = 1111;
    public static int NO_DATA_AVAILABLE = 2222;


    public static void showProgressDialog(Activity activity, String message, boolean isCancelable) {
        if (message.isEmpty()) {
            message = "Loading....";
        }
        countProgress++;
        pDialog = new Dialog(activity, R.style.full_screen_dialog);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setCancelable(isCancelable);
        pDialog.setCanceledOnTouchOutside(isCancelable);
        pDialog.setContentView(R.layout.dialog_loading);
        Window window = pDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);
        ProgressBar progressBar = pDialog.findViewById(R.id.pb_loading);
        TextView txtTitle = pDialog.findViewById(R.id.txtTitle);
        txtTitle.setText(message);
        progressBar.incrementProgressBy(10);
        progressBar.setIndeterminate(true);
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    public static void showProgressDialog(Context activity, String message, boolean isCancelable) {
        if (message.isEmpty()) {
            message = "Loading....";
        }
        countProgress++;
        pDialog = new Dialog(activity, R.style.full_screen_dialog);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setCancelable(isCancelable);
        pDialog.setCanceledOnTouchOutside(isCancelable);
        pDialog.setContentView(R.layout.dialog_loading);
        Window window = pDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);
        ProgressBar progressBar = pDialog.findViewById(R.id.pb_loading);
        TextView txtTitle = pDialog.findViewById(R.id.txtTitle);
        txtTitle.setText(message);
        progressBar.incrementProgressBy(10);
        progressBar.setIndeterminate(true);
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    public static SpannableString changeFont(Activity activity, String str) {
        Typeface myFont = Typeface.createFromAsset(activity.getAssets(), "OpenSans-Bold.ttf");
        SpannableString mNewTitle = new SpannableString(str);
        mNewTitle.setSpan(new CustomTypefaceSpan("", myFont), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.primary)), 0, mNewTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return mNewTitle;
    }

    public static SpannableString changeFont(Context activity, String str) {
        Typeface myFont = Typeface.createFromAsset(activity.getAssets(), "OpenSans-Bold.ttf");
        SpannableString mNewTitle = new SpannableString(str);
        mNewTitle.setSpan(new CustomTypefaceSpan("", myFont), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.primary)), 0, mNewTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return mNewTitle;
    }

    public static SpannableString changeFontForSnack(Context activity, String str) {
        Typeface myFont = Typeface.createFromAsset(activity.getAssets(), "OpenSans-Bold.ttf");
        SpannableString mNewTitle = new SpannableString(str);
        mNewTitle.setSpan(new CustomTypefaceSpan("", myFont), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.white)), 0, mNewTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return mNewTitle;
    }

    public static void hideProgressDialog() {
        if (pDialog != null) {
            if (pDialog.isShowing()) {
                countProgress--;
                errorLog(TAG, "hideProgressDialog", String.valueOf(countProgress));
                pDialog.dismiss();
            }
        }
    }

    public static void showSnackBar(CoordinatorLayout coordinatorLayout, String message, Context ctx, int color) {
       /* Snackbar snack = Snackbar.make(coordinatorLayout, changeFontForSnack(ctx, message), Snackbar.LENGTH_LONG);
        View snackBarView = snack.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(ctx, color));
        snack.show();*/

        final Dialog pDialog = new Dialog(ctx, R.style.full_screen_dialog);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);
        pDialog.setContentView(R.layout.dialog_validations);
        Window window = pDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);
        ImageView imgClose = pDialog.findViewById(R.id.imgClose);
        TextView txtTitle = pDialog.findViewById(R.id.txtTitle);
        txtTitle.setText(message);
        //    txtTitle.setTextColor(ctx.getResources().getColor(color));

        if (!pDialog.isShowing()) {
            pDialog.show();
        }

        imgClose.setOnClickListener(v -> pDialog.dismiss());

        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }

    public static void showSnackBarLinearLayout(LinearLayout linearLayout, String message, Context ctx, int color) {
      /*  Snackbar snack = Snackbar.make(linearLayout, changeFontForSnack(ctx, message), Snackbar.LENGTH_LONG);
        View snackBarView = snack.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(ctx, color));
        snack.show();*/

        final Dialog pDialog = new Dialog(ctx, R.style.full_screen_dialog);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);
        pDialog.setContentView(R.layout.dialog_validations);
        Window window = pDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);
        ImageView imgClose = pDialog.findViewById(R.id.imgClose);
        TextView txtTitle = pDialog.findViewById(R.id.txtTitle);
        txtTitle.setText(message);
        //    txtTitle.setTextColor(ctx.getResources().getColor(color));

        if (!pDialog.isShowing()) {
            pDialog.show();
        }

        imgClose.setOnClickListener(v -> pDialog.dismiss());
    }

    public static void showSnackBarView(View dview, String message, Context context, int color) {
     /*   Snackbar snack = Snackbar.make(dview, changeFontForSnack(context, message), Snackbar.LENGTH_LONG);
        View snackBarView = snack.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, color));
        snack.show();*/

        final Dialog pDialog = new Dialog(context, R.style.full_screen_dialog);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);
        pDialog.setContentView(R.layout.dialog_validations);
        Window window = pDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);
        ImageView imgClose = pDialog.findViewById(R.id.imgClose);
        TextView txtTitle = pDialog.findViewById(R.id.txtTitle);
        txtTitle.setText(message);
        //  txtTitle.setTextColor(context.getResources().getColor(color));

        if (!pDialog.isShowing()) {
            pDialog.show();
        }

        imgClose.setOnClickListener(v -> pDialog.dismiss());
    }

    public static void errorLog(String TAG, String tag, String message) {
        Log.e("*" + TAG + "* " + tag, message);
    }

    public static String getTime() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        return df.format(c);
    }

    public static double distance(double lat2, double lon2) {
        if (LocationFindActivity.lat != 0 && LocationFindActivity.lon != 0) {
            double theta = LocationFindActivity.lon - lon2;
            double dist = Math.sin(deg2rad(LocationFindActivity.lat))
                    * Math.sin(deg2rad(lat2))
                    + Math.cos(deg2rad(LocationFindActivity.lat))
                    * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            return Math.floor(dist / 0.62137);
        }
        return 0;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static String priceWithoutDecimal(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        return formatter.format(price);
    }

    public static void showMessage(String message, Context ctx) {
        final Dialog pDialog = new Dialog(ctx, R.style.full_screen_dialog);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);
        pDialog.setContentView(R.layout.dialog_validations);
        Window window = pDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);
        ImageView imgClose = pDialog.findViewById(R.id.imgClose);
        TextView txtTitle = pDialog.findViewById(R.id.txtTitle);
        txtTitle.setText(message);

        if (!pDialog.isShowing()) {
            pDialog.show();
        }
        imgClose.setOnClickListener(v -> pDialog.dismiss());

    }

    public static void toast(Context context, String msg) {
        //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}