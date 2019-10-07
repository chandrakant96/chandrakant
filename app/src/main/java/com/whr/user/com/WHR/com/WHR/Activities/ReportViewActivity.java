package com.whr.user.com.WHR.com.WHR.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;
import com.whr.user.com.WHR.Utils.CommanUtils;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;

public class ReportViewActivity extends AppCompatActivity {

    private int report_id;
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    private Context context;
    private TextView userNameTital, userName, datetxt, labNameIdTxt, doctornameTxt, refredByDocNameText, whrIdtxt, testNametext, ageGenderText, testNametext2;
    private ImageView imageView;
    private WebView webView;
    String GoogleDocs = "http://docs.google.com/gview?embedded=true&url=";
    private ReportViewActivity activity;
    private long patientid;
    private String date, reportpath, pname, sname, gender;
    private boolean isPdf;
    private int age, testid;
    private String docName, uname;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();
    String TAG = getClass().getSimpleName();
    TextView title;

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);

        ///------------New Toolbar Code----------------
        title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.Reports));
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        //-----------New Toolbar code------------------------------
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        context = ReportViewActivity.this;
        userNameTital = findViewById(R.id.userNameTital);
        userName = findViewById(R.id.userName);
        datetxt = findViewById(R.id.datetxt);
        labNameIdTxt = findViewById(R.id.labNameIdTxt);
        doctornameTxt = findViewById(R.id.doctornameTxt);
        refredByDocNameText = findViewById(R.id.refredByDocNameText);
        whrIdtxt = findViewById(R.id.whrIdtxt);
        ageGenderText = findViewById(R.id.ageGenderText);
        testNametext2 = findViewById(R.id.testNametext2);
        testNametext = findViewById(R.id.testNametext);

        imageView = (ImageView) findViewById(R.id.imageView);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        imageView.setVisibility(View.GONE);


        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (!bundle.isEmpty()) {
            boolean report_idkey = bundle.containsKey("report_id");
            boolean patientidkey = bundle.containsKey("patientid");
            boolean testidkey = bundle.containsKey("testid");
            boolean datekey = bundle.containsKey("date");
            boolean reportpathkey = bundle.containsKey("reportpath");
            boolean isPdfkey = bundle.containsKey("isPdf");
            boolean pnamekey = bundle.containsKey("pname");
            boolean snamekey = bundle.containsKey("sname");
            boolean agekey = bundle.containsKey("age");
            boolean genderkey = bundle.containsKey("gender");
            boolean docNamekey = bundle.containsKey("docName");
            boolean unamekey = bundle.containsKey("uname");
            boolean dobkey = bundle.containsKey("dob");

            if (report_idkey) {
                report_id = bundle.getInt("report_id", 0);
            }

            if (patientidkey) {
                patientid = bundle.getLong("patientid", 0);
                whrIdtxt.setText(patientid + "");
            }
            if (testidkey) {
                testid = bundle.getInt("testid");
            }
            if (docNamekey) {
                docName = bundle.getString("docName");
                refredByDocNameText.setText(docName + "");
            }
            if (unamekey) {
                uname = bundle.getString("uname");
                userName.setText(uname + "");
                userNameTital.setText(uname + "");
            }
            if (datekey) {
                date = bundle.getString("date");
                datetxt.setText(" " + date);

            }
            if (reportpathkey) {
                reportpath = bundle.getString("reportpath");

            }
            if (pnamekey) {
                pname = bundle.getString("pname");
                labNameIdTxt.setText(pname);
            }
            if (snamekey) {
                sname = bundle.getString("sname");
                testNametext.setText(sname + "");
                testNametext2.setText(sname + "");

            }
            if (genderkey) {
                gender = bundle.getString("gender");
            }
            if (agekey) {
                if (gender == null) {
                    ageGenderText.setText(age + "");
                } else {
                    ageGenderText.setText(age + "/" + gender);
                }
            }

            if (dobkey) {
                String dob = bundle.getString("dob");
                age = CommanUtils.getAge(dob);
                if (gender == null && age > 0) {
                    ageGenderText.setText(age + "");
                } else {
                    ageGenderText.setText(age + "/" + gender);
                }

            } else {
                ageGenderText.setVisibility(View.GONE);
            }

            if (isPdfkey) {
                isPdf = bundle.getBoolean("isPdf");
            }

        }


        activity = this;
        webView = findViewById(R.id.webview_compontent);

        if (isPdf) {
            imageView.setVisibility(View.GONE);
            if (reportpath != null) {
                if (reportpath.length() > 0) {
                    imageView.setVisibility(View.GONE);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setLoadWithOverviewMode(true);
                    webView.getSettings().setUseWideViewPort(true);
                    webView.getSettings().setBuiltInZoomControls(true);
                    webView.getSettings().setDisplayZoomControls(false);

                    webView.setWebViewClient(new WebViewClient() {

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }

                        @Override
                        public void onPageFinished(WebView view, final String url) {
                        }
                    });

                    webView.loadUrl(GoogleDocs + reportpath);
                }
            } else {

            }
        } else {
            imageView.setVisibility(View.VISIBLE);
            if (reportpath != null) {
                if (reportpath.length() > 0) {
                    String str = reportpath.replace("\\", "");
                    if (str != null) {
                        if (str.length() > 0) {
                            try {
                                Picasso.with(context).load(Uri.parse(str)).into(imageView);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        }
    }


    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            matrix.setScale(scaleFactor, scaleFactor);
            imageView.setImageMatrix(matrix);
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        scaleGestureDetector.onTouchEvent(ev);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
