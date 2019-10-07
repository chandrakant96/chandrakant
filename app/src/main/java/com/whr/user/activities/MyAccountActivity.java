package com.whr.user.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyAccountActivity extends AppCompatActivity {
    private PreferenceUtils pref;
    private RequestQueue mQueue;
    private Context context;
    private ImageView imageUserProfile;
    private TextView txtUserName, txtDob, txtBloodGroup, txtMobileNumber, txtEmail;
    TextView txtEditInfo;
    private CoordinatorLayout coordinatorLayout;
    String TAG = getClass().getSimpleName();
    TextView txtGender, txtWalletPoints;

    String name = "", dob = "", gender = "", bg = "", uid = "",
            profilephoto = "", mobileno = "", emergencyCno = "", Address = "", title = "", subtitle = "", strHealthId = "";
    private TextView txtName, txtDobHealth, txtGenderHealth, txtBloodGroupHealth, txtHealtId,
            txtAddress, txtMobile, txtTitle, txtsubTitle, txtEmergencyMobile;
    private ImageView imgBarcode, imgUser;
    RelativeLayout layoutCard;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        overridePendingTransitionEnter();
        init();
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), 1);
        }
    }

    private void init() {
        txtGender = findViewById(R.id.txtGender);
        txtWalletPoints = findViewById(R.id.txtWalletPoints);
        context = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        pref = new PreferenceUtils(context);
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtEditInfo = findViewById(R.id.txtEditInfo);
        txtUserName = findViewById(R.id.txtUserName);
        txtDob = findViewById(R.id.txtDob);
        txtBloodGroup = findViewById(R.id.txtBloodGroup);
        txtMobileNumber = findViewById(R.id.txtMobileNumber);
        txtEmail = findViewById(R.id.txtEmail);
        imageUserProfile = findViewById(R.id.imageUserProfile);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        if (new ConnectionDector(context).isConnectingToInternet()) {
            GetProfile();
            getUserDetails();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
        txtEditInfo.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditInfoActivity.class);
            startActivityForResult(intent, 111);
        });


        txtName = findViewById(R.id.txtName);
        txtDobHealth = findViewById(R.id.txtDobHealth);
        txtGenderHealth = findViewById(R.id.txtGenderHealth);
        txtBloodGroupHealth = findViewById(R.id.txtBloodGroupHealth);
        txtHealtId = findViewById(R.id.txtHealtId);
        imgBarcode = findViewById(R.id.imgBarcode);
        imgUser = findViewById(R.id.imgUser);
        layoutCard = findViewById(R.id.layoutCard);
        txtAddress = findViewById(R.id.txtAddress);
        txtMobile = findViewById(R.id.txtMobile);
        txtTitle = findViewById(R.id.txtTitle);
        txtsubTitle = findViewById(R.id.txtsubTitle);
        txtEmergencyMobile = findViewById(R.id.txtEmergencyMobile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GetProfile();
    }

    private void GetProfile() {
        String url = GlobalVar.ServerAddress + "AndroidNew/GetAccountDetails";
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj, response -> {
            try {
                GlobalVar.hideProgressDialog();
                Log.e("GetProfile", response.toString());
                JSONObject jsonRootobj = new JSONObject(response.toString());
                JSONObject jsonRoot = jsonRootobj.getJSONObject("Account_Details");
                pref.setUID(jsonRoot.getString("userid") + "");
                pref.setUserName(jsonRoot.getString("uname") + "");

                txtUserName.setText(jsonRoot.getString("uname"));
                txtBloodGroup.setText(jsonRoot.getString("bloodgrp"));
                txtMobileNumber.setText(jsonRoot.getString("cno"));
                txtEmail.setText(jsonRoot.getString("email"));
                txtGender.setText(jsonRoot.getString("gender"));
                txtDob.setText(jsonRoot.getString("dob"));
                String pImage = jsonRoot.optString("profileimage");
                if (pImage != null && !pImage.isEmpty()) {
                    pref.setUserProfileImage(pImage);
                    Picasso.with(context).load(pImage).into(imageUserProfile);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    private void getUserDetails() {
        JSONObject obj = new JSONObject();
        String url = GlobalVar.ServerAddress + "AndroidNew/GetAccountDetails";
        //GlobalVar.showProgressDialog(context, "Loading......", false);
        try {
            obj.put("uid", pref.getUID());
            GlobalVar.errorLog(TAG, "url", url);
            Log.e("obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, response -> {
            parseUserResponse(response);
        }, error -> {
            //GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        });

        mQueue.add(customJSONObjectRequest);
    }


    private void parseUserResponse(JSONObject jsonRootobj) {
        try {
            JSONObject jsonObject = jsonRootobj.getJSONObject("Healthcardprofile");
            name = jsonObject.getString("Name");
            mobileno = jsonObject.getString("ContactNumber");
            emergencyCno = jsonObject.getString("EmergencyContactNo");
            profilephoto = jsonObject.getString("ProfilePhoto");
            dob = jsonObject.getString("DOB");
            bg = jsonObject.getString("BloodGroup");
            gender = jsonObject.getString("Gender");
            uid = jsonObject.getString("UDID");
            strHealthId = jsonObject.getString("HealthCardId");
            Address = jsonObject.getString("Address");
            title = jsonObject.getString("headline");
            subtitle = jsonObject.getString("data");

            String[] separated = subtitle.split("#");
            String one = separated[0] + "\n" + separated[1] + "\n" + separated[2] + "\n" + separated[3] +
                    "\n" + separated[4] + "\n" + separated[5] + "\n" + separated[6];

            if (strHealthId.length() < 12) {
                strHealthId = strHealthId + "0";
            } else {

            }
            strHealthId = String.format("%s %s %s", strHealthId.substring(0, 4),
                    strHealthId.substring(4, 8), strHealthId.substring(8, 12));
            txtHealtId.setText(strHealthId);

            txtName.setText(": " + name);
            txtDobHealth.setText(" : " + dob);
            txtGenderHealth.setText(" : " + gender);
            txtBloodGroupHealth.setText(" : " + bg);

            txtAddress.setText(" : " + Address);
            txtMobile.setText(" : " + mobileno);
            txtEmergencyMobile.setText(" : " + emergencyCno);
            txtTitle.setText(title);
            txtsubTitle.setText(one);


            if (profilephoto != null && !profilephoto.isEmpty()) {
                Picasso.with(context).load(profilephoto)
                        .placeholder(R.drawable.logo).error(R.drawable.logo)
                        .fit().into(imgUser);
            } else {
                imgUser.setImageResource(R.drawable.logo);
            }
            code = "Name : " + name + "\n" + "DOB : " + dob + "\n" + "Gender : " + gender + "\n"
                    + "Blood Group : " + bg + "\n" + "Mobile : " + mobileno + "\n"
                    + Address + "\n " + "Health Id :" + strHealthId
                    + "\n " + "Uid :" + uid;


            try {
                bitmap = TextToImageEncode(code);
                imgBarcode.setImageBitmap(bitmap);
                GlobalVar.hideProgressDialog();
            } catch (WriterException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            GlobalVar.hideProgressDialog();
            e.printStackTrace();
        }
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.DATA_MATRIX.QR_CODE, QRcodeWidth, QRcodeWidth, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

}