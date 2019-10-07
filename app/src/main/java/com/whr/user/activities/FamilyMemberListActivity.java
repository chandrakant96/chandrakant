package com.whr.user.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.AndroidMultiPartEntity;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.adapter.AddFamilyAdapter;
import com.whr.user.model.AddFamilyMemberPojo;
import com.whr.user.pojo.GlobalVar;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FamilyMemberListActivity extends AppCompatActivity {
    private TextView addFamily;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private AddFamilyAdapter adapter;
    private Context context;
    private PreferenceUtils pref;
    private List<AddFamilyMemberPojo> list = new ArrayList<>();
    private static int RequsteCodeFamily = 111;
    private ConnectionDector connectionDector;
    private RequestQueue mQueue;
    private LinearLayout coordinatorLayout;
    private AppCompatActivity activity;
    private String emailStr, bloodGroupStr, sdob, sname, spolicy, emergencynostr, usrcontactstr, checkBoxString, relationshipStr, password;
    private File profileImageFile;
    private EditText searchBox;
    Snackbar snackbar;
    private MaterialRippleLayout imgBack;
    private TextView txtTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_memberfragment_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        activity = FamilyMemberListActivity.this;
        context = FamilyMemberListActivity.this;
        init();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void init() {
        pref = new PreferenceUtils(FamilyMemberListActivity.this);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        snackbar = Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_LONG);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        connectionDector = new ConnectionDector(context);
        searchBox = findViewById(R.id.editText);
        addFamily = findViewById(R.id.txtAddMember);
        imgBack = findViewById(R.id.imgBack);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Family Member");

        imgBack.setOnClickListener(v -> onBackPressed());

        addFamily.setOnClickListener(v -> {
            if (list.size() < 5) {
                Intent i = new Intent(FamilyMemberListActivity.this, AddFamilyMemberActivity.class);
                startActivityForResult(i, RequsteCodeFamily);
            } else {
                showSnackBar(getString(R.string.Youcanaddonlyfamilymember), R.color.red);
            }
        });

        recyclerView = findViewById(R.id.familyidrecycleView);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);

        if (connectionDector.isConnectingToInternet()) {
            getAllFamillyMember();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                boolean isString = false;
                String someText = String.valueOf(query);
                isString = someText.matches(".*\\d+.*");

                if (isString) {
                    query = query.toString().toLowerCase();
                    List<AddFamilyMemberPojo> filterList = new ArrayList<AddFamilyMemberPojo>();
                    for (int i = 0; i < list.size(); i++) {
                        String strLong = Long.toString(list.get(i).getFamilyId());
                        if (strLong.contains(query)) {
                            filterList.add(list.get(i));
                        }
                    }
                    adapter = new AddFamilyAdapter(context, filterList, context, activity, coordinatorLayout);
                    recyclerView.setAdapter(adapter);

                } else {
                    query = query.toString().toLowerCase();
                    List<AddFamilyMemberPojo> filterList = new ArrayList<AddFamilyMemberPojo>();
                    for (int i = 0; i < list.size(); i++) {
                        final String text = list.get(i).getFamilyName().toLowerCase();
                        if (text.contains(query)) {
                            filterList.add(list.get(i));
                        }
                    }
                    adapter = new AddFamilyAdapter(context, filterList, context, activity, coordinatorLayout);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RequsteCodeFamily && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            emailStr = extras.getString("emailStr");
            sname = extras.getString("sname");
            sdob = extras.getString("sdob");
            usrcontactstr = extras.getString("usrcontactstr");
            emergencynostr = extras.getString("emergencynostr");
            bloodGroupStr = extras.getString("bloodGroupStr");
            spolicy = extras.getString("spolicy");
            relationshipStr = extras.getString("relationshipStr");
            checkBoxString = extras.getString("checkBoxString");
            String sourcepathImgString = extras.getString("sourcepathImgString");
            boolean iscamaraFile = extras.getBoolean("iscamaraFile");
            password = extras.getString("password");

            if (iscamaraFile) {
            }

            File docProfilesourceFile = null;
            if (sourcepathImgString != null) {
                if (sourcepathImgString.length() > 0) {
                    docProfilesourceFile = new File(sourcepathImgString);
                    GlobalVar.errorLog(TAG, "TAG_IF", sourcepathImgString);
                } else {
                    docProfilesourceFile = new File("");
                    GlobalVar.errorLog(TAG, "TAG_IF", sourcepathImgString);
                }
            } else {
                docProfilesourceFile = new File("");
            }

            String data1 = docProfilesourceFile.getAbsolutePath();
            profileImageFile = new File(data1);

            if (connectionDector.isConnectingToInternet()) {
                new addNewFamillyMenber_withImage().execute();
            } else {
                showSnackBar(getString(R.string.NoInternetConnection), R.color.red);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    String TAG = getClass().getSimpleName();

    public void getAllFamillyMember() {
        GlobalVar.showProgressDialog(this, "Loading....", false);

        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String url = GlobalVar.ServerAddress + "AndroidNew/MyFamilyNew";
        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GlobalVar.errorLog(TAG, "getAllFamillyMember:=>", response.toString());
                GlobalVar.hideProgressDialog();
                Gson gson = new Gson();
                JSONArray jsonArray1 = null;
                try {
                    list.clear();
                    jsonArray1 = response.getJSONArray("result");
                    if (jsonArray1.length() > 0) {
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject json = jsonArray1.getJSONObject(i);
                            AddFamilyMemberPojo pojo = gson.fromJson(json.toString(), AddFamilyMemberPojo.class);
                            list.add(pojo);
                        }
                        adapter = new AddFamilyAdapter(context, list, context, activity, coordinatorLayout);
                        recyclerView.setAdapter(adapter);
                    } else {
                        startActivityForResult(new Intent(context, NoDataAvailableActivity.class), GlobalVar.NO_DATA_AVAILABLE);
                        overridePendingTransitionEnter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            GlobalVar.hideProgressDialog();
            showSnackBar(VolleyErrorHelper.getMessage(error, context), R.color.red);
        });
        mQueue.add(jsonRequest);
    }

    class addNewFamillyMenber_withImage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String responstring = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(GlobalVar.ServerAddress + "AndroidNew/AddMyFamilyMember");
            GlobalVar.errorLog(TAG, "url", GlobalVar.ServerAddress + "AndroidNew/AddMyFamilyMember");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress();
                    }
                });

                if (profileImageFile.isFile()) {
                    entity.addPart("familyprofile", new FileBody(profileImageFile));
                } else {
                    entity.addPart("familyprofile", new StringBody(""));
                }
                entity.addPart("uid", new StringBody(pref.getUID() + ""));
                entity.addPart("email", new StringBody(emailStr + ""));
                entity.addPart("uname", new StringBody(sname + ""));
                entity.addPart("dob", new StringBody(sdob + ""));
                entity.addPart("gender", new StringBody(checkBoxString + ""));
                entity.addPart("cno", new StringBody(usrcontactstr + ""));
                entity.addPart("emr_cno", new StringBody(emergencynostr + ""));
                entity.addPart("bgroup", new StringBody(bloodGroupStr + ""));
                entity.addPart("policy", new StringBody(spolicy + ""));
                entity.addPart("relation", new StringBody(relationshipStr + ""));
                entity.addPart("pwd", new StringBody(password + ""));


                if (profileImageFile.isFile()) {
                    GlobalVar.errorLog(TAG, "familyprofile", profileImageFile.toString());
                } else {
                    GlobalVar.errorLog(TAG, "familyprofile", "");
                }
                GlobalVar.errorLog(TAG, "uid", pref.getUID());
                GlobalVar.errorLog(TAG, "email", emailStr);
                GlobalVar.errorLog(TAG, "uname", sname);
                GlobalVar.errorLog(TAG, "dob", sdob);
                GlobalVar.errorLog(TAG, "gender", checkBoxString);
                GlobalVar.errorLog(TAG, "cno", usrcontactstr);
                GlobalVar.errorLog(TAG, "emr_cno", emergencynostr);
                GlobalVar.errorLog(TAG, "bgroup", bloodGroupStr);
                GlobalVar.errorLog(TAG, "policy", spolicy);
                GlobalVar.errorLog(TAG, "relation", relationshipStr);
                GlobalVar.errorLog(TAG, "pwd", password);

                GlobalVar.errorLog(TAG, "addNewFamillyMenbery", usrcontactstr + " " + checkBoxString + "" + " " + emailStr + " " + bloodGroupStr + " " + sdob + " " + spolicy + " " + sname);

                long totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responstring = EntityUtils.toString(r_entity);
                    GlobalVar.errorLog(TAG, "response", responstring);
                } else {
                    GlobalVar.errorLog(TAG, "response", "Error occurred! Http Status Code: " + statusCode);
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

            GlobalVar.errorLog(TAG, "addNewFamionPostExecute", output);
            if (output != null) {
                if (output.length() > 0) {
                    try {
                        // JSONObject mainJsonObj = new JSONObject(output);
                        JSONObject jsonRootobj = new JSONObject(output.toString());
                        boolean result = jsonRootobj.getBoolean("result");
                        if (result) {
                            showSnackBar(getString(R.string.FamilyMemberSuccessfullyUpdated), R.color.green);
                            getAllFamillyMember();
                        } else {
                            showSnackBar(getString(R.string.UserAlreadyRegistered), R.color.red);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showSnackBar(getString(R.string.PleaseTryAgain), R.color.red);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSnackBar(String message, int color) {
        /*snackbar.setText(message);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, color));
        snackbar.show();*/

        GlobalVar.showSnackBarView(coordinatorLayout, message, context, color);

    }

}
