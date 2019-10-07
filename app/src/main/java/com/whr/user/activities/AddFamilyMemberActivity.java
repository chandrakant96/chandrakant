package com.whr.user.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.whr.user.com.WHR.Utils.AndroidMultiPartEntity;
import com.whr.user.BuildConfig;
import com.whr.user.com.WHR.Utils.CommanUtils;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
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
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddFamilyMemberActivity extends AppCompatActivity {
    private ImageView profile_image;
    private Context context;
    private boolean iscamaraFile = false;
    private File cameraFilefile = new File("");
    private Uri outputFileUri;
    private int REQUEST_CAMERA = 200, SELECT_FILE = 100;
    private String sourcepathImgString = "";
    private TextView txtTitle;
    private EditText etxName, etxMobile;
    TextView etxDob;
    private Button submitFromFamillyMember;
    private long totalSize;
    CoordinatorLayout coordinatorLayout;
    private PreferenceUtils pref;
    private MaterialRippleLayout imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family_member);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        context = AddFamilyMemberActivity.this;
        pref = new PreferenceUtils(context);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Add Family Member");
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        profile_image = findViewById(R.id.profileImageView);
        coordinatorLayout = findViewById(R.id.CoordinatorLayout);
        etxName = findViewById(R.id.usrname);
        etxMobile = findViewById(R.id.usrcontact);
        etxDob = findViewById(R.id.dob);
        submitFromFamillyMember = findViewById(R.id.submitFromFamillyMember);

        profile_image.setOnClickListener(v -> selectImage());

        submitFromFamillyMember.setOnClickListener(v -> {
            if (etxName.getText().toString().isEmpty()) {
                GlobalVar.showSnackBar(coordinatorLayout, "Enter Name", context, R.color.red);
            } else if (etxDob.getText().toString().isEmpty()) {
                GlobalVar.showSnackBar(coordinatorLayout, "Enter Name", context, R.color.red);
            } else if (etxMobile.getText().toString().isEmpty()) {
                GlobalVar.showSnackBar(coordinatorLayout, "Enter Mobile Number", context, R.color.red);
            } else {
                new AddFamilyMember(context).execute();
            }
        });


        today = Calendar.getInstance();
        etxDob.setOnClickListener(v -> {
            DatePickerDialog dp = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
                DecimalFormat formatter = new DecimalFormat("00");
                etxDob.setText(formatter.format(dayOfMonth) + "/" + formatter.format(monthOfYear + 1) + "/" + year);
                _dob = year + "-" + formatter.format(monthOfYear + 1) + "-" + formatter.format(dayOfMonth);
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));

            dp.getDatePicker().setMaxDate(today.getTimeInMillis());
            dp.show();
        });
    }

    private String _name, _dob, _cno, _email, _gender, _bg, _pwd;
    private Calendar today;

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void selectImage() {
        final CharSequence[] items = {getString(R.string.TakePhoto),
                getString(R.string.ChooseFromLibrary),
                getString(R.string.Cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFamilyMemberActivity.this);
        builder.setTitle(GlobalVar.changeFont(context, getString(R.string.AddPhoto)));
        builder.setItems(items, (dialog, item) -> {
            if (items[item].toString().equals(getString(R.string.TakePhoto))) {
                cameraIntent();
            } else if (items[item].equals(getString(R.string.ChooseFromLibrary))) {
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
                        cameraFilefile = null;
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cameraFilefile = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
                        outputFileUri = FileProvider.getUriForFile(AddFamilyMemberActivity.this, BuildConfig.APPLICATION_ID + ".provider", cameraFilefile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(cameraIntent, 501);

                    } catch (Exception e) {
                        CommanUtils.getToast(context, e.getMessage() + "Exception");
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
                    Uri uri = Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                iscamaraFile = false;
                Toast.makeText(AddFamilyMemberActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };


        new TedPermission(AddFamilyMemberActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.CAMERA)
                .check();
    }

    private void galleryIntent() {
        iscamaraFile = false;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                try {
                    final Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startCropActivity(selectedUri);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                cameraFilefile = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
                Uri uri = Uri.fromFile(cameraFilefile);

                final Uri selectedUri = Uri.fromFile(cameraFilefile);
                if (selectedUri != null) {
                    startCropActivity(selectedUri);
                } else {
                    startCropActivity(uri);
                }
                //  sourcepathImgString = getPathFromUri(uri);
                Picasso.with(context).load(uri).into(profile_image);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                // handleCropResult(data);
                final Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    // Picasso.with(context).load(resultUri).into(profile_image);
                    cameraFilefile = new File(resultUri.getPath());
                    if (cameraFilefile.exists()) {
                        Picasso.with(context).load(resultUri).into(profile_image);
                        sourcepathImgString = cameraFilefile.getAbsolutePath();
                        try {
                            Bitmap thePic = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                            Uri u = getImageUri(context, thePic);
                            Picasso.with(context).load(u).into(profile_image);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //
                }

            } else if (requestCode == 501 && resultCode == RESULT_OK) {
                try {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                        cameraFilefile = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");

                                /*cameraFilefile = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
                                final Uri selectedUri = Uri.fromFile(cameraFilefile);
                                if (selectedUri != null)
                                {
                                    startCropActivity(selectedUri);
                                }*/

                        if (data != null) {
                            Uri uri = data.getData();
                            Bitmap thePic = null;
                            try {
                                thePic = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                uri = getImageUri(context, thePic);
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                thePic.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                                Picasso.with(context).load(uri).into(profile_image);
                                sourcepathImgString = cameraFilefile.getAbsolutePath();

                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            Uri uri = outputFileUri;
                            Bitmap thePic = null;
                            try {
                                thePic = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                uri = getImageUri(context, thePic);
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                thePic.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                                Picasso.with(context).load(uri).into(profile_image);
                                sourcepathImgString = cameraFilefile.getAbsolutePath();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }

                    } else {
                        //other than N

                        if (data != null) {
                            Uri uri = outputFileUri;
                            Bitmap thePic = null;
                            try {
                                thePic = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                uri = getImageUri(context, thePic);
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                thePic.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                                Picasso.with(context).load(uri).into(profile_image);
                                sourcepathImgString = cameraFilefile.getAbsolutePath();

                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            Uri uri = outputFileUri;
                            Bitmap thePic = null;
                            try {
                                thePic = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                uri = getImageUri(context, thePic);
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                thePic.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                                Picasso.with(context).load(uri).into(profile_image);
                                sourcepathImgString = cameraFilefile.getAbsolutePath();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAH", e.getMessage());
                }
            }

        }

    }

    private void startCropActivity(Uri uri) {
        String destinationFileName = "crop.jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop = basisConfig(uCrop);
        uCrop.start(AddFamilyMemberActivity.this);
    }


    private UCrop basisConfig(@NonNull UCrop uCrop) {
        //  uCrop = uCrop.u   seSourceImageAspectRatio();
        uCrop = uCrop.withAspectRatio(1, 1);
        return uCrop;
    }

    public String getPathFromUri(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }


    public static boolean isCheckEmailValid(final String mailAddress) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class AddFamilyMember extends AsyncTask<String, String, String> {
        private ProgressDialog profileDialog;
        private Context activity;

        public AddFamilyMember(Context activity) {
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
            HttpPost httppost = new HttpPost(GlobalVar.ServerAddress + "AndroidNew/AddMyFamilyMemberNew");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(num -> publishProgress(String.valueOf((int) ((num / (float) totalSize) * 100))));
                if (iscamaraFile) {
                    if (cameraFilefile.isFile()) {
                        entity.addPart("image", new FileBody(cameraFilefile));
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
                        entity.addPart("image", new FileBody(file1));
                    }
                }
                if (cameraFilefile.isFile()) {
                    entity.addPart("image", new FileBody(cameraFilefile));
                } else {
                }

                entity.addPart("uid", new StringBody(pref.getUID() + ""));
                entity.addPart("name", new StringBody(etxName.getText().toString()));
                entity.addPart("mobile", new StringBody(etxMobile.getText().toString()));
                entity.addPart("dob", new StringBody(etxDob.getText().toString()));

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
                        //    GlobalVar.showSnackBar(coordinatorLayout, "Family Member Added Successfully", context, R.color.green);
                            ///startActivity(new Intent(context, FamilyMemberListActivity.class));
                            onBackPressed();

                            String doctorImageStrJson = mainJsonObj.getString("profileimage");
                            if (doctorImageStrJson != null) {
                                try {
                                    Picasso.with(context).load(doctorImageStrJson).error(R.drawable.user_blue).fit().into(profile_image);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            GlobalVar.showSnackBar(coordinatorLayout, "Please Try Again", context, R.color.red);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
