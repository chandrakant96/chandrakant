package com.whr.user.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.whr.user.com.WHR.Utils.AndroidMultiPartEntity;
import com.whr.user.BuildConfig;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.Utility;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;
import com.yalantis.ucrop.UCrop;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditInfoActivity extends AppCompatActivity {
    EditText edDob, edMobileNumber, edEmail, edName, edPassword;
    String TAG = getClass().getSimpleName();
    private ImageView imageUserProfile;
    private RadioButton radioButtonMale, radioFemale;
    private Spinner spinnerBloodGroup;
    private TextView txtSave;
    private PreferenceUtils pref;
    private String _name, _dob, _cno, _email, _gender, _bg, _pwd;
    private String[] bgrplist;
    private RequestQueue mQueue;
    private Calendar today;
    private ArrayAdapter<String> adapter;
    private Context context;
    private int REQUEST_CAMERA = 200, SELECT_FILE = 100;
    private CoordinatorLayout coordinatorLayout;
    private String sourcepathImgString;
    private long totalSize;
    private File cameraFilefile;
    private boolean iscamaraFile = false;
    private Uri outputFileUri;


    public static boolean isCheckEmailValid(final String mailAddress) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    ConnectionDector dector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        overridePendingTransitionEnter();
        bgrplist = new String[]{getString(R.string.SelectBloodGroup), "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        init();

        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), 1);

        }
    }

    private void init() {
        context = this;
        dector = new ConnectionDector(context);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        pref = new PreferenceUtils(context);
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtSave = findViewById(R.id.txtSave);
        edName = findViewById(R.id.edName);
        edDob = findViewById(R.id.edDob);
        edMobileNumber = findViewById(R.id.edMobileNumber);
        imageUserProfile = findViewById(R.id.imageUserProfile);
        edPassword = findViewById(R.id.edPassword);
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        edEmail = findViewById(R.id.edEmail);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioFemale = findViewById(R.id.radioFemale);
        today = Calendar.getInstance();
        final List<String> plantsList = new ArrayList<>(Arrays.asList(bgrplist));
        adapter = new ArrayAdapter<String>(context, R.layout.family_spinner_row, R.id.tvCategory, plantsList);
        adapter.setDropDownViewResource(R.layout.family_spinner_row);
        spinnerBloodGroup.setAdapter(adapter);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();

        String userprofImg = pref.getUserProfileImage();

        if (userprofImg != null) {
            String str = userprofImg.replace("\\", "");
            if (str.length() > 0) {
                String pStr = str.replaceAll(" ", "%20");
                if (pStr != null) {
                    Picasso.with(context).load(pStr).placeholder(R.drawable.user_blue).error(R.drawable.user_blue).fit().into(imageUserProfile);
                }
            }
        }

        HideKeyword(coordinatorLayout);

        spinnerBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) spinnerBloodGroup.getSelectedItem();
                if (position > 0) {
                    _bg = str;
                } else {
                    _bg = str;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spinnerBloodGroup.setOnTouchListener((v, event) -> {
            HideKeyword(v);
            return false;
        });


        edDob.setOnClickListener(v -> {
            HideKeyword(v);
            DatePickerDialog dp = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
                DecimalFormat formatter = new DecimalFormat("00");
                edDob.setText(formatter.format(dayOfMonth) + "/" + formatter.format(monthOfYear + 1) + "/" + year);
                _dob = year + "/" + formatter.format(monthOfYear + 1) + "/"
                        + formatter.format(dayOfMonth);
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));

            dp.getDatePicker().setMaxDate(today.getTimeInMillis());
            dp.show();
        });

        if (dector.isConnectingToInternet()) {
            GetProfile();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }


        txtSave.setOnClickListener(v -> {
            _dob = edDob.getText().toString();
            _name = edName.getText().toString().trim();
            _cno = edMobileNumber.getText().toString().trim();
            _pwd = edPassword.getText().toString().trim();
            _email = edEmail.getText().toString().trim();

            if (_name.length() < 1) {
                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.EnterName), context, R.color.red);
                edName.requestFocus();
                return;
            } else if (_cno.length() != 10) {
                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.Pleaseentervaliddigitcontactnumber), context, R.color.red);
                edMobileNumber.requestFocus();
                return;
            } else if (edEmail.getText().toString().trim().length() > 0 && (!isCheckEmailValid(edEmail.getText().toString()))) {
                GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.PleaseEnterValidEmailId), context, R.color.red);
                edEmail.requestFocus();
                return;
            } else {
                if (radioButtonMale.isChecked())
                    _gender = getString(R.string.Male);
                else if (radioFemale.isChecked())
                    _gender = getString(R.string.Female);
                else
                    _gender = getString(R.string.Other);

                updateProfile();

            }

        });
        imageUserProfile.setOnClickListener(v -> selectImage());
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.TakePhoto),
                getString(R.string.ChooseFromLibrary),
                getString(R.string.Cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(GlobalVar.changeFont(context, getString(R.string.AddPhoto)));
        builder.setItems(items, (dialog, item) -> {
            boolean result = Utility.checkPermission(context);
            if (items[item].toString().equals(getString(R.string.TakePhoto))) {
                if (result)
                    cameraIntent();
            } else if (items[item].equals(getString(R.string.ChooseFromLibrary))) {
                if (result)
                    galleryIntent();
            } else if (items[item].equals(getString(R.string.Cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void cameraIntent() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                iscamaraFile = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    try {
                        GlobalVar.errorLog(TAG, "VERSION_CODES.N", "501");
                        cameraFilefile = null;
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cameraFilefile = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
                        outputFileUri = FileProvider.getUriForFile(context,
                                BuildConfig.APPLICATION_ID + ".provider", cameraFilefile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(cameraIntent, 501);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                iscamaraFile = false;
                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.CAMERA)
                .check();
    }

    private void galleryIntent() {
        iscamaraFile = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<Intent> targets = new ArrayList<>();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            List<ResolveInfo> candidates = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);

            for (ResolveInfo candidate : candidates) {
                String packageName = candidate.activityInfo.packageName;
                if (!packageName.equals("com.google.android.apps.photos") && !packageName.equals("com.google.android.apps.plus") && !packageName.equals("com.android.documentsui")) {
                    Intent iWantThis = new Intent();
                    iWantThis.setType("image/*");
                    iWantThis.setAction(Intent.ACTION_PICK);
                    iWantThis.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    iWantThis.setPackage(packageName);
                    targets.add(iWantThis);
                }
            }
            if (targets.size() > 0) {
                Intent chooser = Intent.createChooser(targets.remove(0), "Select Picture");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[targets.size()]));
                startActivityForResult(chooser, SELECT_FILE);
            } else {
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(Intent.createChooser(intent1, "Select Picture"), SELECT_FILE);
            }
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                break;

            case 102:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        GlobalVar.errorLog(TAG, "handleCropResult", "handleCropResult");
        Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            GlobalVar.errorLog(TAG, "cameraFilefile", resultUri.getPath());
            cameraFilefile = new File(resultUri.getPath());
            if (cameraFilefile.exists()) {
                Picasso.with(context).load(resultUri).into(imageUserProfile);
                if (cameraFilefile.exists()) {
                    Picasso.with(context).load(resultUri).into(imageUserProfile);
                    new UpdateUserProfileImage(context).execute();
                }
            } else {
                GlobalVar.errorLog(TAG, "cameraFilefile", "NOT EXIST");
            }
        } else {
            GlobalVar.errorLog(TAG, "resultUri", "null");
        }
    }

    private void startCropActivity(Uri uri) {
        String destinationFileName = System.currentTimeMillis() + "_Crop.jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop = basisConfig(uCrop);
        uCrop.start(EditInfoActivity.this);
    }

    private UCrop basisConfig(@NonNull UCrop uCrop) {
        uCrop = uCrop.withAspectRatio(1, 1);
        return uCrop;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            //webservice call again here
            GetProfile();
        } else if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri uri = getImageUri(context, imageBitmap);
                startCropActivity(uri);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            } else if (requestCode == 501) {
                GlobalVar.errorLog(TAG, "REQUEST_CAMERA", "501");
                cameraFilefile = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
                final Uri selectedUri = Uri.fromFile(cameraFilefile);
                if (selectedUri != null) {
                    startCropActivity(selectedUri);
                } else {
                    GlobalVar.errorLog(TAG, "selectedUri", "null");
                }
            } else if (requestCode == SELECT_FILE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(selectedUri);
                } else {

                }
                GlobalVar.errorLog(TAG, "REQUEST_CAMERA", "NO");
            }
        } else {
            GlobalVar.errorLog(TAG, "RESULT_OK", "ELSE");
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    class UpdateUserProfileImage extends AsyncTask<String, String, String> {
        private ProgressDialog profileDialog;
        private Context activity;

        public UpdateUserProfileImage(Context activity) {
            this.activity = activity;
            profileDialog = new ProgressDialog(activity);
            profileDialog.setMessage("Please wait");
            profileDialog.setIndeterminate(false);
            profileDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            profileDialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            profileDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            profileDialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected String doInBackground(String... params) {
            String responstring = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(GlobalVar.ServerAddress + "AndroidNew/UpdateUserProfileImage");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress(String.valueOf((int) ((num / (float) totalSize) * 100)));
                    }
                });
                if (iscamaraFile) {
                    if (cameraFilefile.isFile()) {
                        entity.addPart("profile_img", new FileBody(cameraFilefile));
                    } else {
                    }

                } else {
                    File docProfilesourceFile = null;
                    if (sourcepathImgString != null) {
                        if (sourcepathImgString.length() > 0) {
                            docProfilesourceFile = new File(sourcepathImgString);
                        } else {
                            docProfilesourceFile = new File("");
                        }
                    } else {
                        docProfilesourceFile = new File("");
                    }
                    String data1 = docProfilesourceFile.getAbsolutePath();
                    File file1 = new File(data1);
                    if (file1.isFile()) {
                        entity.addPart("profile_img", new FileBody(file1));
                    }
                }
                if (cameraFilefile.isFile()) {
                    entity.addPart("profile_img", new FileBody(cameraFilefile));
                } else {
                }

                entity.addPart("uid", new StringBody(pref.getUID() + ""));
                //  entity.addPart("from", new StringBody( 0+""));
                totalSize = entity.getContentLength();

                publishProgress(String.valueOf(totalSize));
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responstring = EntityUtils.toString(r_entity);
                    Log.e("response", responstring);
                } else {
                    Log.e("response", "Error occurred! Http Status Code: " + statusCode);
                }
            } catch (ClientProtocolException e) {
                Log.e("response", e.toString());
            } catch (IOException e) {
                Log.e("response", e.toString());
            }
            return responstring;
        }


        @Override
        protected void onPostExecute(String output) {
            profileDialog.dismiss();
            Log.e("uploadProfileImg", output);
            try {

                if (output != null) {
                    if (output.length() > 0) {
                        JSONObject mainJsonObj = new JSONObject(output);
                        boolean result = mainJsonObj.getBoolean("result");
                        if (result) {
                            String doctorImageStrJson = mainJsonObj.getString("profileimage");
                            if (doctorImageStrJson != null) {
                                try {
                                    Picasso.with(context).load(doctorImageStrJson).error(R.drawable.user_blue).fit().into(imageUserProfile);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            GlobalVar.showSnackBar(coordinatorLayout,
                                    "Profile Image Successfully Updated", context, R.color.green);
                        } else {
                            GlobalVar.showSnackBar(coordinatorLayout,
                                    "Please Try Again", context, R.color.red);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProfile() {
        String url = GlobalVar.ServerAddress + "AndroidNew/UpdateProfile";
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            obj.put("uname", _name);
            obj.put("dob", _dob);
            obj.put("cno", _cno);
            obj.put("gender", _gender);
            obj.put("bgroup", _bg);
            obj.put("email", _email);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, response -> {
            GlobalVar.errorLog(TAG, "updateProfile", response.toString());
            GlobalVar.hideProgressDialog();
            try {
                if (response.getBoolean("result")) {
                    GlobalVar.showSnackBar(coordinatorLayout,
                            "Updated successfully", context, R.color.green);
                    onBackPressed();
                } else {
                    GlobalVar.showSnackBar(coordinatorLayout,
                            "Please Try Again", context, R.color.red);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    public void HideKeyword(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, response -> {
            try {
                GlobalVar.hideProgressDialog();
                Log.e("GetProfile", response.toString());
                JSONObject jsonRootobj = new JSONObject(response.toString());
                JSONObject jsonRoot = jsonRootobj.getJSONObject("Account_Details");
                _name = jsonRoot.optString("uname");
                _dob = jsonRoot.getString("dob");
                _email = jsonRoot.getString("email");
                _cno = jsonRoot.getString("cno");
                _bg = jsonRoot.getString("bloodgrp");
                _gender = jsonRoot.getString("gender");

                edName.setText(_name);
                edDob.setText(_dob);
                edEmail.setText(_email);
                edMobileNumber.setText(_cno);
                edPassword.setText(_pwd);

                if (_gender.equals("Male"))
                    radioButtonMale.setChecked(true);
                else if (_gender.equals("Female"))
                    radioFemale.setChecked(true);

                for (int i = 0; i < bgrplist.length; i++)
                    if (_bg.equals(bgrplist[i]))
                        spinnerBloodGroup.setSelection(i);

                String pImage = jsonRoot.getString("profile_img");
                if (pImage != null && !pImage.isEmpty()) {
                    pref.setUserProfileImage(pImage);
                    Picasso.with(context).load(pImage).into(imageUserProfile);
                }
            } catch (Exception e) {
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
}