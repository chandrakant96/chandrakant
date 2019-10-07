package com.whr.user.com.WHR.adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CheckForSDCard;
import com.whr.user.com.WHR.Utils.CommanUtils;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.CheckStorage;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.com.WHR.com.WHR.Activities.PrescriptionViewActivity;
import com.whr.user.com.WHR.com.WHR.Activities.ReportViewActivity;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.pojo.ReportGetReportPojo;
import com.whr.user.pojo.ShowDoctorListFromHospitalActivityPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ReportFragmentAdapter extends RecyclerView.Adapter<ReportFragmentAdapter.ReportFragmentMyViewHolder> {

    private final RequestQueue mQueue;
    private List<ReportGetReportPojo> list;
    private Context context;
    private DownloadManager downloadManager;
    private LayoutInflater inflater;
    private Activity activity;
    private long downloadReference;
    private String key = "";
    private List<ShowDoctorListFromHospitalActivityPojo> doctorList;
    private AutoCompleteTextView doctorSearch;
    private int[] doctorSearchIdArray;
    private String[] doctorSearchnNameArray;
    private String doctorNamestr;
    private int doctorID = 0;
    private AlertDialog doctorIdreffredalertDialog;
    private AlertDialog sharePopUpBox;
    private ProgressDialog pDialog;
    private DoctorListAutoCompleatBoxAdapter doctorListAutoCompleatBoxAdapter;
    private PreferenceUtils pref;
    boolean isString = false;
    private int status;
    private boolean repotType;
    private String familyNameSelectedBySpinner = "";
    private long familyid;
    private CoordinatorLayout coordinatorLayout;

    public ReportFragmentAdapter(Context context, List<ReportGetReportPojo> list, List<ShowDoctorListFromHospitalActivityPojo> doctorList, Activity activity, int status, boolean repotType, String familyNameSelectedBySpinner, long familyid
            , CoordinatorLayout coordinatorLayout

    ) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.doctorList = doctorList;
        this.activity = activity;
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        this.status = status;
        this.repotType = repotType;
        this.familyid = familyid;
        this.coordinatorLayout = coordinatorLayout;
        this.familyNameSelectedBySpinner = familyNameSelectedBySpinner;
        pref = new PreferenceUtils(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
    }

    @Override
    public ReportFragmentMyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.report_fragment_adapter_row, parent, false);
        ReportFragmentMyViewHolder holder = new ReportFragmentMyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ReportFragmentMyViewHolder holder, final int position) {

        if (status == 0) {
            if (repotType) {
                holder.viewDownloadLayout.setVisibility(View.VISIBLE);
                holder.download.setVisibility(View.GONE);
            } else {
                holder.viewDownloadLayout.setVisibility(View.GONE);
                holder.download.setVisibility(View.VISIBLE);
            }
        } else {
            holder.viewDownloadLayout.setVisibility(View.VISIBLE);
        }


        if (repotType) {
            holder.doctorName.setText(list.get(position).getDocname() + "");
            holder.checkupforLayout.setVisibility(View.VISIBLE);
        } else {
            holder.doctorName.setText(list.get(position).getPname() + "");
            holder.checkupforLayout.setVisibility(View.VISIBLE);

            if (list.get(position).getDocname() != null) {
                if (list.get(position).getDocname().length() > 0) {
                    holder.cname.setVisibility(View.VISIBLE);
                    holder.cname.setText(list.get(position).getDocname() + "");
                }
            }
        }

        holder.checkupOn.setText(list.get(position).getDate() + "");
        holder.checkUpfor.setText(list.get(position).getSname() + "");

        holder.viewbtn.setOnClickListener(v -> {

            if (repotType) {
                Intent i = new Intent(context, PrescriptionViewActivity.class);
                i.putExtra("report_id", list.get(position).getId());
                i.putExtra("patientid", list.get(position).getUid());
                i.putExtra("doctorId", list.get(position).getDid());
                i.putExtra("docName", list.get(position).getDocname());
                i.putExtra("uname", list.get(position).getUname());
                i.putExtra("date", list.get(position).getDate());
                i.putExtra("isPresHistory", false);
                i.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                context.startActivity(i);
                activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {

                String str = "";
                if (list.get(position).getDateofbirth() != null) {
                    if (list.get(position).getDateofbirth().length() > 0) {
                        str = list.get(position).getDateofbirth().replace("\\", "");
                    }
                }
                Intent i = new Intent(context, ReportViewActivity.class);
                i.putExtra("report_id", list.get(position).getId());
                i.putExtra("patientid", list.get(position).getPatientid());
                i.putExtra("testid", list.get(position).getTestid());
                i.putExtra("docName", list.get(position).getDocname());
                i.putExtra("uname", list.get(position).getUname());
                i.putExtra("date", list.get(position).getDate());
                i.putExtra("reportpath", list.get(position).getReportpath());
                i.putExtra("isPdf", list.get(position).isPdf());
                i.putExtra("pname", list.get(position).getPname());
                i.putExtra("sname", list.get(position).getSname());
                //  i.putExtra("age",list.get(position).getAge());
                i.putExtra("gender", list.get(position).getGender());
                i.putExtra("dob", str);
                i.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                context.startActivity(i);
                activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        holder.download.setOnClickListener(v -> {
            if (CheckForSDCard.isSDCardPresent()) {
                new DownloadFile().execute("http://android.whrhealth.com/HealthcardImg/220520190256569545013121_CHANDRAKANT_DATTATRYA_WALKE_sandip_Report_Heart%20Attack_1558518968943.jpg");
            } else {
                Toast.makeText(getApplicationContext(), "SD Card not found", Toast.LENGTH_LONG).show();
            }
        });

        holder.share.setOnClickListener(v -> {

            //     sharePopUpBox(position);
            AlertDialog.Builder doctorIdreffredbuilder = new AlertDialog.Builder(context);
            LayoutInflater doctorIdreffredinflater = activity.getLayoutInflater();
            //  LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View doctorIdreffreddialogView = doctorIdreffredinflater.inflate(R.layout.social_media_doctor_name_share_pop_box, null);
            doctorIdreffredbuilder.setView(doctorIdreffreddialogView);

            doctorSearch = doctorIdreffreddialogView.findViewById(R.id.searchdoctoreditText);
            //   Button  scoialMediaShare = (Button) doctorIdreffreddialogView.findViewById(R.id.searchByDoctor);
            Button searchByDoctor = (Button) doctorIdreffreddialogView.findViewById(R.id.searchByDoctor);
            Button socilaMediaSharebtn = (Button) doctorIdreffreddialogView.findViewById(R.id.socilaMediaSharebtn);
            ImageView closeButton = (ImageView) doctorIdreffreddialogView.findViewById(R.id.closeButton);

            closeButton.setOnClickListener(v13 -> sharePopUpBox.cancel());


            if (repotType) {
                socilaMediaSharebtn.setVisibility(View.GONE);
            } else {
                socilaMediaSharebtn.setVisibility(View.VISIBLE);
            }

            socilaMediaSharebtn.setOnClickListener(v12 -> {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                StringBuilder str = new StringBuilder();
                Uri myUri = Uri.parse(list.get(position).getReportpath());
                String path = myUri.getLastPathSegment().toString();
                Uri screenshotUri = Uri.parse(path);
                str.append(myUri.toString() + " ");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, str.toString());
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
            });
            searchByDoctor.setOnClickListener(v1 -> {
                sharePopUpBox.cancel();
                searchByDoctorNameDialogBox(position);
            });
            sharePopUpBox = doctorIdreffredbuilder.create();
            sharePopUpBox.show();
            // share End
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ReportFragmentMyViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName, checkUpfor, checkupOn, cname;
        TextView viewbtn, download, share;
        LinearLayout viewDownloadLayout, checkupforLayout;

        public ReportFragmentMyViewHolder(View itemView) {
            super(itemView);
            //reporttype = (TextView) itemView.findViewById(R.id.reporttype);
            doctorName = itemView.findViewById(R.id.doctorNamerowtext);
            checkUpfor = itemView.findViewById(R.id.checkUpforrowtext);
            checkupOn = itemView.findViewById(R.id.checkupOnrowtext);
            cname = itemView.findViewById(R.id.cname);
            viewbtn = itemView.findViewById(R.id.viewbtnrow);
            download = itemView.findViewById(R.id.downloadbtnrow);
            share = itemView.findViewById(R.id.sharebtnrow);
            viewDownloadLayout = itemView.findViewById(R.id.viewDownloadLayout);
            checkupforLayout = itemView.findViewById(R.id.checkupforLayout);
        }
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            CheckDwnloadStatus();
        }
    };

    private void CheckDwnloadStatus() {

        DownloadManager.Query query = new DownloadManager.Query();

        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor
                    .getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            int columnReason = cursor
                    .getColumnIndex(DownloadManager.COLUMN_REASON);
            int reason = cursor.getInt(columnReason);

            switch (status) {
                case DownloadManager.STATUS_FAILED:
                    String failedReason = "";
                    switch (reason) {
                        case DownloadManager.ERROR_CANNOT_RESUME:
                            failedReason = "ERROR_CANNOT_RESUME";
                            break;
                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                            failedReason = "ERROR_DEVICE_NOT_FOUND";
                            break;
                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                            failedReason = "ERROR_FILE_ALREADY_EXISTS";
                            break;
                        case DownloadManager.ERROR_FILE_ERROR:
                            failedReason = "ERROR_FILE_ERROR";
                            break;
                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
                            failedReason = "ERROR_HTTP_DATA_ERROR";
                            break;
                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                            failedReason = "ERROR_INSUFFICIENT_SPACE";
                            break;
                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                            failedReason = "ERROR_TOO_MANY_REDIRECTS";
                            break;
                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                            failedReason = "ERROR_UNHANDLED_HTTP_CODE";
                            break;
                        case DownloadManager.ERROR_UNKNOWN:
                            failedReason = "ERROR_UNKNOWN";
                            break;
                    }

                    Toast.makeText(context, "FAILED: " + failedReason, Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PAUSED:
                    String pausedReason = "";

                    switch (reason) {
                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                            pausedReason = "PAUSED_QUEUED_FOR_WIFI";
                            break;
                        case DownloadManager.PAUSED_UNKNOWN:
                            pausedReason = "PAUSED_UNKNOWN";
                            break;
                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                            pausedReason = "PAUSED_WAITING_FOR_NETWORK";
                            break;
                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
                            pausedReason = "PAUSED_WAITING_TO_RETRY";
                            break;
                    }

                    Toast.makeText(context, "PAUSED: " + pausedReason, Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PENDING:
                    Toast.makeText(context, "PENDING",
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_RUNNING:
                    // Toast.makeText(DemoDownload.this,"RUNNING",Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:

                    Toast.makeText(
                            context,
                            R.string.downlaodsuccessful,
                            Toast.LENGTH_LONG).show();


                    break;
            }
        }
    }

    private void searchByDoctorNameDialogBox(final int position) {

        AlertDialog.Builder doctorIdreffredbuilder = new AlertDialog.Builder(context);
        LayoutInflater doctorIdreffredinflater = activity.getLayoutInflater();
        //  LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View doctorIdreffreddialogView = doctorIdreffredinflater.inflate(R.layout.doctor_autocompleate_in_report_fragment_adapter_row, null);
        doctorIdreffredbuilder.setView(doctorIdreffreddialogView);
        doctorSearch = (AutoCompleteTextView) doctorIdreffreddialogView.findViewById(R.id.searchdoctoreditText);
        Button searchByDoctor = (Button) doctorIdreffreddialogView.findViewById(R.id.searchByDoctor);
        doctorSearch.setHint(context.getString(R.string.SearchByDoctor));
        final LinearLayout parentmainLayout = (LinearLayout) doctorIdreffreddialogView.findViewById(R.id.parentmainLayout);
        ImageView closeButton = (ImageView) doctorIdreffreddialogView.findViewById(R.id.closeButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorIdreffredalertDialog.cancel();
            }
        });

        final List<ShowDoctorListFromHospitalActivityPojo> docList = new ArrayList<ShowDoctorListFromHospitalActivityPojo>();

        doctorSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String respStr = pref.getDoctoList();
                docList.clear();
                if (respStr != null) {
                    if (respStr.length() > 0) {
                        Gson gson = new Gson();
                        JSONArray jsonArray1 = null;
                        try {
                            JSONObject jsonObject = new JSONObject(respStr);
                            jsonArray1 = jsonObject.getJSONArray("doctorlist");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                ShowDoctorListFromHospitalActivityPojo pojo = gson.fromJson(json.toString(), ShowDoctorListFromHospitalActivityPojo.class);
                                docList.add(pojo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String someText = String.valueOf(s);
                isString = someText.matches(".*\\d+.*");
                doctorListAutoCompleatBoxAdapter = new DoctorListAutoCompleatBoxAdapter(context, 0, docList, isString);
                doctorSearch.setAdapter(doctorListAutoCompleatBoxAdapter);
                doctorSearch.setThreshold(0);
                doctorSearch.showDropDown();

                doctorSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        ShowDoctorListFromHospitalActivityPojo model = (ShowDoctorListFromHospitalActivityPojo) adapterView.getItemAtPosition(position);
                        doctorID = 0;
                        doctorNamestr = "";
                        String docname = ((TextView) ((LinearLayout) view).getChildAt(0)).getText().toString();
                        String doctorId = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
                        // String userPimag = ((TextView) ((LinearLayout) view).getChildAt(2)).getText().toString();
                        doctorSearch.setText(docname);
                        doctorNamestr = docname;
                        doctorID = Integer.parseInt(doctorId);
                        Log.e("doctor_NAme_Id", doctorNamestr + " " + doctorID);

                        //hide keyboard
                        hideKeyboard(doctorIdreffreddialogView);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        String[] typeArray = {"Select Type", "Prescription", "Report's"};
        final List<String> plantsList = new ArrayList<>(Arrays.asList(typeArray));


        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        doctorIdreffredalertDialog = doctorIdreffredbuilder.create();
        doctorIdreffredalertDialog.show();
        searchByDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (doctorID == 0) {
                    GlobalVar.showSnackBarLinearLayout(parentmainLayout, context.getString(R.string.PleaseSelectDoctor), context, R.color.red);
                } else {
                    shareToDoctorWebCall(doctorID, list.get(position).getId(), parentmainLayout);
                }
            }
        });
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void shareToDoctorWebCall(int doctorID, int id, final LinearLayout parentmainLayout) {
        showProgressDialog();
        JSONObject obj = new JSONObject();
        try {
            Log.e("shareToDoctorWebCall", "doctorID:" + doctorID + " " + "id: " + id + " repotType " + String.valueOf(repotType));
            obj.put("rowid", id);
            obj.put("did", doctorID);
            obj.put("id", familyid);
            obj.put("share_type", repotType);
        } catch (Exception ex) {
            ex.getMessage();
        }

        String url = GlobalVar.ServerAddress + "User/ShareReport";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("shareToDoctorWebCall", response.toString());
                hideProgressDialog();
                try {
                    boolean rersult = response.getBoolean("result");
                    if (rersult) {
                        GlobalVar.showSnackBar(coordinatorLayout, context.getString(R.string.SuccessfullyShared), context, R.color.green);
                        if (doctorIdreffredalertDialog != null && doctorIdreffredalertDialog.isShowing()) {
                            doctorIdreffredalertDialog.cancel();
                        }
                        sharePopUpBox.cancel();
                    } else {
                        GlobalVar.showSnackBar(coordinatorLayout, context.getString(R.string.PleaseTryAgain), context, R.color.green);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.green);
            }
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lengthOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
                fileName = timestamp + "_" + fileName;
                folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";

                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                   // Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        protected void onProgressUpdate(String... progress) {
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            this.progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}

