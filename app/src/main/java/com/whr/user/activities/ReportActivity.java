package com.whr.user.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.whr.user.adapter.NewReportAdapter;
import com.whr.user.com.WHR.Utils.AndroidMultiPartEntity;
import com.whr.user.BuildConfig;
import com.whr.user.com.WHR.Utils.CommanUtils;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.DoctorSpecialitiesModel;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.FilePath;
import com.whr.user.com.WHR.Utils.Utility;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.com.WHR.adapters.DoctorListAutoCompleatBoxAdapter;
import com.whr.user.com.WHR.adapters.ReportFragmentAdapter;
import com.whr.user.com.WHR.adapters.SpecializationFilterAdapter;
import com.whr.user.first_aid.activities.FirstAidActivity;
import com.whr.user.first_aid.adapters.FirstAidAdapter;
import com.whr.user.first_aid.model.FirstAidPojo;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pojo.NewReportPojo;
import com.whr.user.pojo.ReportGetReportPojo;
import com.whr.user.pojo.ShowDoctorListFromHospitalActivityPojo;
import com.whr.user.wallet.FileDownloader;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    MaterialRippleLayout imgBack;
    Context context;
    PreferenceUtils pref;
    String url=GlobalVar.ServerAddress +"Krasna/GetNewReports";

    private LinearLayoutManager lLayout;
    private List<NewReportPojo> list;
    NewReportAdapter adapter;
    private RequestQueue mQueue;
    RecyclerView recyclerView;

    Button button;
    ImageView imageView;
    String image_url = "http://android.whrhealth.com/HealthcardImg/220520190256569545013121_CHANDRAKANT_DATTATRYA_WALKE_sandip_Report_Heart%20Attack_1558518968943.jpg";

    public void downloadImage(View view) {
        Log.e("Info", "Button pressed");
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(image_url);
    }

    public void download(View v)
    {
        new DownloadFile().execute("https://www.mahaurja.com/gcrt1819/download/GeneralGuidelinesfortheconsumersMSDCL.pdf", "maven1.pdf");
    }

    public void view(View v)
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/WHR/" + "maven1.pdf");
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(ReportActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_report_fragment_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        context= ReportActivity.this;
        pref = new PreferenceUtils(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        recyclerView=findViewById(R.id.reportFragmentrecyclerview);
        imgBack=findViewById(R.id.imgBack);


        list = new ArrayList<>();
        adapter = new NewReportAdapter(context, list);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(adapter);

        imgBack.setOnClickListener(v ->{
            onBackPressed();
        });

        isStoragePermissionGranted();
        getData();

        adapter.setClickLister(new NewReportAdapter.ClickLister() {
            @Override
            public void viewClick(NewReportPojo NewReportPojo) {

            }

            @Override
            public void downloadClick(NewReportPojo NewReportPojo) {

            }

            @Override
            public void layoutClick(NewReportPojo NewReportPojo) {

            }
        });


    }

    private void getData() {
        GlobalVar.showProgressDialog(this, "Loading.....", true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "GetReport", response.toString());
                    GlobalVar.hideProgressDialog();
                    Gson gson = new Gson();
                    list.clear();
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("result");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                NewReportPojo pojo = gson.fromJson(json.toString(), NewReportPojo.class);
                                list.add(pojo);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            GlobalVar.showMessage("No Data Available",context);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showMessage(VolleyErrorHelper.getMessage(error, context),context);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    class DownloadTask extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ReportActivity.this);
            progressDialog.setTitle("Download in progress...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            int file_length;

            Log.e("Info: path", path);
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();

                File new_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "myfolder");
                if (!new_folder.exists()) {
                    if (new_folder.mkdir()) {
                        Log.e("Info", "Folder succesfully created");
                    } else {
                        Log.e("Info", "Failed to create folder");
                    }
                } else {
                    Log.e("Info", "Folder already exists");
                }

                File output_file = new File(new_folder, "downloaded_image.jpg");
                OutputStream outputStream = new FileOutputStream(output_file);

                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;

                    outputStream.write(data, 0, count);
                    int progress = 100 * total / file_length;
                    publishProgress(progress);

                    Log.e("Info", "Progress: " + progress);
                }
                inputStream.close();
                outputStream.close();

                Log.e("Info", "file_length: " + file_length);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download complete.";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.hide();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "myfolder");
            File output_file = new File(folder, "downloaded_image.jpg");
            String path = output_file.toString();
            imageView.setImageDrawable(Drawable.createFromPath(path));
            Log.e("Info", "Path: " + path);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("Tag1", "Permission is granted");
                return true;
            } else {
                Log.d("Tag2", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.d("Tag3", "Permission is granted");
            return true;
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "WHR");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }

}