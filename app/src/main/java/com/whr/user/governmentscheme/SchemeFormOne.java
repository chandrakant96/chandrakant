package com.whr.user.governmentscheme;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.whr.user.BuildConfig;
import com.whr.user.R;
import com.whr.user.activities.EditInfoActivity;
import com.whr.user.com.WHR.Utils.AndroidMultiPartEntity;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.com.WHR.Utils.Utility;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.governmentscheme.adapter.PincodeAdapter;
import com.whr.user.governmentscheme.models.PincodePojoAddress;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pojo.PincodePojo;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SchemeFormOne extends AppCompatActivity {
    private MaterialRippleLayout imgBack;
    private TextView title;
    private Context context;
    CoordinatorLayout coordinatorLayout;

    String TAG = getClass().getSimpleName();
    private List<PincodePojoAddress> list;
    private RequestQueue mQueue;
    String id, name;
    String uid;

    private ImageView f1imgUser;
    private EditText f1etxName, f1etxAadhar, f1etxPincode, f1etxVillage, f1etxTaluka, f1etxDist, f1etxMobile;
    private TextView txtNext;

    private int REQUEST_CAMERA = 200, SELECT_FILE = 100;
    private long totalSize;
    private File cameraFilefile;
    private boolean iscamaraFile = false;
    private Uri outputFileUri;
    private String sourcepathImgString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheme_forn_one);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            id = bundle.getString("id");
            name = bundle.getString("name");
        }

        context = SchemeFormOne.this;
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();

        imgBack = findViewById(R.id.imgBack);
        title = findViewById(R.id.title);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtNext = findViewById(R.id.txtNext);
        f1imgUser = findViewById(R.id.f1imgUser);
        f1etxName = findViewById(R.id.f1etxName);
        f1etxAadhar = findViewById(R.id.f1etxAadhar);
        f1etxPincode = findViewById(R.id.f1etxPincode);
        f1etxVillage = findViewById(R.id.f1etxVillage);
        f1etxTaluka = findViewById(R.id.f1etxTaluka);
        f1etxDist = findViewById(R.id.f1etxDist);
        f1etxMobile = findViewById(R.id.f1etxMobile);


        title.setText("रुग्णाचा तपशील");

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        txtNext.setOnClickListener(v -> {
//            Intent intent1 = new Intent(context, SchemeFormTwo.class);
//            intent1.putExtra("uid", "1");
//            startActivity(intent1);
            if (f1etxName.getText().toString().isEmpty()) {
                GlobalVar.showMessage("रुग्णाचे संपूर्ण नाव टाका ", context);
            } else if (f1etxAadhar.getText().toString().isEmpty()) {
                GlobalVar.showMessage("रुग्णाचा आधार क्रमांक टाका", context);
            } else if (f1etxPincode.getText().toString().isEmpty()) {
                GlobalVar.showMessage("पिनकोड टाका", context);
            } else if (f1etxVillage.getText().toString().isEmpty()) {
                GlobalVar.showMessage("रुग्णाचा पत्ता ( गाव ) टाका", context);
            } else if (f1etxTaluka.getText().toString().isEmpty()) {
                GlobalVar.showMessage("तालुका टाका", context);
            } else if (f1etxDist.getText().toString().isEmpty()) {
                GlobalVar.showMessage("जिल्हा टाका", context);
            } else if (f1etxMobile.getText().toString().isEmpty()) {
                GlobalVar.showMessage("रुग्णाच्या जवळच्या नातेवाईकाचा मोबाईल नंबर टाका", context);
            } else {
                new UpdateUserProfileImage(context).execute();
            }
        });

        f1etxPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6)
                    dialogPincode(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        f1imgUser.setOnClickListener(v -> selectImage());


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
                Picasso.with(context).load(resultUri).into(f1imgUser);
                if (cameraFilefile.exists()) {
                    Picasso.with(context).load(resultUri).into(f1imgUser);
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
        uCrop.start(SchemeFormOne.this);
    }

    private UCrop basisConfig(@NonNull UCrop uCrop) {
        uCrop = uCrop.withAspectRatio(1, 1);
        return uCrop;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
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
            HttpPost httppost = new HttpPost(GlobalVar.ServerAddress + "user/AddUserforYojana");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress(String.valueOf((int) ((num / (float) totalSize) * 100)));
                    }
                });
                if (iscamaraFile) {
                    if (cameraFilefile.isFile()) {
                        entity.addPart("image ", new FileBody(cameraFilefile));
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
                        entity.addPart("image ", new FileBody(file1));
                    }
                }
                if (cameraFilefile.isFile()) {
                    entity.addPart("image ", new FileBody(cameraFilefile));
                } else {
                }


                entity.addPart("id", new StringBody(id));
                entity.addPart("nameofyojana", new StringBody(name));
                entity.addPart("name", new StringBody(f1etxName.getText().toString()));
                entity.addPart("AddharNo", new StringBody(f1etxAadhar.getText().toString()));
                entity.addPart("pincode", new StringBody(f1etxPincode.getText().toString()));
                entity.addPart("village", new StringBody(f1etxVillage.getText().toString()));
                entity.addPart("taluka", new StringBody(f1etxTaluka.getText().toString()));
                entity.addPart("dist", new StringBody(f1etxDist.getText().toString()));
                entity.addPart("Familymobno", new StringBody(f1etxMobile.getText().toString()));
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
                        uid = mainJsonObj.getString("result");
                        if (uid.equals("0")) {
                            showSnackBar("Unable to Save");
                        } else {
                            Intent intent1 = new Intent(context, SchemeFormTwo.class);
                            intent1.putExtra("uid", uid);
                            startActivity(intent1);
                            finish();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void dialogPincode(String pincode) {
        final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_pincode);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        EditText serachview = dialog.findViewById(R.id.auto_complete_textview);

        list = new ArrayList<>();
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PincodeAdapter adapter = new PincodeAdapter(context, list);
        recyclerView.setAdapter(adapter);

        adapter.setClickLister(pojo -> {
            PincodePojoAddress pinCodeSearchModel = pojo;
            f1etxVillage.setText(pinCodeSearchModel.getCityOrVillage().toUpperCase());
            f1etxTaluka.setText(pinCodeSearchModel.getTehsil().toUpperCase());
            f1etxDist.setText(pinCodeSearchModel.getDistrict().toUpperCase());
            //f1etxPincode.setText(pinCodeSearchModel.getPincode().toUpperCase());
            dialog.dismiss();
        });

        serachview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    adapter.getFilterList(charSequence.toString());

                } else {

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imgClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        String url = "http://android.whrhealth.com/Healthcard/GetPincodeWiseStateAndCity";
        GlobalVar.showProgressDialog(this, "Loading.....", true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("pincode", pincode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "Citydata", response.toString());
                    GlobalVar.hideProgressDialog();
                    Gson gson = new Gson();
                    list.clear();
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("Citydata");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                PincodePojoAddress pojo = gson.fromJson(json.toString(), PincodePojoAddress.class);
                                list.add(pojo);
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

        dialog.show();

    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }
}