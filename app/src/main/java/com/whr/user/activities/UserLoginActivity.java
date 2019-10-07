package com.whr.user.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.content.res.AppCompatResources;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

public class UserLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static int count = 0;
    int randomPIN;
    String id = "";
    private EditText txtMobileNumber;
    private PreferenceUtils preference;
    private Context context;
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    TextView txtLogin;
    String TAG = getClass().getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public void onButtonClickGoogleLogin(View view) {
        signIn();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void onButtonClickFacebookLogin(View view) {
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(UserLoginActivity.this, Collections.singletonList("user_photos, public_profile," + " email, user_birthday, user_friends"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //  Intent intent = getIntent();
            // startActivity(intent);
            //   finish();
        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            GlobalVar.errorLog(TAG, "onActivityResult", "callbackManager");
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        GlobalVar.errorLog(TAG, "handleSignInResult:", String.valueOf(result.isSuccess()));
        GlobalVar.errorLog(TAG, "handleSignInResult:", result.toString());

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            strName = acct.getDisplayName();
            strUid = acct.getId();
            strEmail = acct.getEmail();
            if (dector.isConnectingToInternet()) {
                doSocialLogin();
            } else {
                startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
                overridePendingTransitionExit();
            }
        } else {
        }
    }

    CallbackManager callbackManager;

    String strUid = "";

    String strName = "";

    String strEmail = "";

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            String packageName = context.getApplicationContext().getPackageName();
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Log.e("printKeyHash", context.getApplicationContext().getPackageName());
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return key;
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    ConnectionDector dector;

    String token;
    private static final int REQUEST_CODE_READ_SMS = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        overridePendingTransitionEnter();
        txtLogin = findViewById(R.id.txtLogin);
        token = FirebaseInstanceId.getInstance().getToken();

        //---------------------------Facebook Login----------------------------------------
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        AppEventsLogger.activateApp(this);

        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        callbackManager = CallbackManager.Factory.create();
        printKeyHash(this);
        preference = new PreferenceUtils(UserLoginActivity.this);


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("onSuccess");
                        String accessToken = loginResult.getAccessToken().getToken();
                        Profile profile = Profile.getCurrentProfile();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                (object, response) -> {
                                    try {
                                        GlobalVar.errorLog(TAG, "LoginResult", response.toString());
                                        GlobalVar.errorLog(TAG, "LoginResult", object.toString());
                                        strUid = object.getString("id");
                                        GlobalVar.errorLog(TAG, "strUid", strUid);
                                        strName = object.optString("name");
                                        GlobalVar.errorLog(TAG, "strName", strName);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (dector.isConnectingToInternet()) {
                                        doSocialLogin();
                                    } else {
                                        startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
                                        overridePendingTransitionExit();
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link,verified");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        try {
                            Log.e(TAG, exception.getCause().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


        //--------------Google Login-------------
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        //--------------Google Login-------------
        context = UserLoginActivity.this;
        txtMobileNumber = findViewById(R.id.txtMobileNumber);
        dector = new ConnectionDector(context);

        txtMobileNumber.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_profile), null, null, null);
        coordinatorLayout = findViewById(R.id.loginlayout);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        txtLogin.setOnClickListener(v -> login());

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_READ_SMS);
    }

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void login() {
        strUid = txtMobileNumber.getText().toString();
        if (strUid.isEmpty()) {
            GlobalVar.showSnackBar(coordinatorLayout, "Please Enter Mobile Number", context, R.color.red);
        } else if (strUid.length() <= 9) {
            GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.PleaseEnterValidMobileNumber), context, R.color.red);
        } else {
            if (dector.isConnectingToInternet()) {
                getOTP();
            } else {
                startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
                overridePendingTransitionEnter();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransitionExit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    //----------webservice call--------

    private void doSocialLogin() {
        GlobalVar.showProgressDialog(context, "Please Wait.....", false);
        JSONObject obj = new JSONObject();
        try {
            obj.put("logintype", "ANDROID");
            obj.put("uid", strUid);
            obj.put("name", strName);
            obj.put("email", strEmail);
            obj.put("deviceid", token);
            obj.put("cno", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String url = GlobalVar.ServerAddress + "AndroidNew/CheckUserExistUsingContactnoV1";
        Log.e("url", url);
        Log.e("obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    Log.e("doSocialLogin:", response.toString());
                    try {
                        boolean isExist = response.getString("result").equals("true");
                        preference.setUID(strUid);
                        preference.setLogin(true);
                        preference.setKeepMeLoin(true);
                        Intent i = new Intent(UserLoginActivity.this, NevigationDrawerDashBordActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransitionEnter();
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

    private void getOTP() {
        GlobalVar.showProgressDialog(context, "Sending OTP to " + strUid, false);
        String url = GlobalVar.ServerAddress + "AndroidNew/SendOtpToAllNumber";
        JSONObject params = new JSONObject();
        try {
            randomPIN = (int) (Math.random() * 9000) + 1000;
            params.put("cno", strUid);
            params.put("key", randomPIN);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "params", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, params,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "otpWebSerViceCall", response.toString());
                    try {
                        boolean result = response.getBoolean("result");
                        Intent i = new Intent(context, OtpScreenActivity.class);
                        i.putExtra("mobNumber", strUid);
                        i.putExtra("key", String.valueOf(randomPIN));
                        i.putExtra("status", "mobile");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                        finish();
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
}
