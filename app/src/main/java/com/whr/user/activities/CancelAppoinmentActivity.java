package com.whr.user.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CancelAppoinmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView imgBack;
    private TextView title, txtRescheduleAppoinment, txtCancelAppoinment;
    private EditText etxMessage;
    private Spinner famillyMemberSpinner;
    Context context;
    LinearLayout layoutMessage;
    String item = "";
    CoordinatorLayout coordinatorLayout;
    String bookingid,did,hid,specilization;
    String TAG = getClass().getSimpleName();
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appoinment);

        context = CancelAppoinmentActivity.this;
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();


        title = findViewById(R.id.title);
        imgBack = findViewById(R.id.imgBack);
        txtRescheduleAppoinment = findViewById(R.id.txtRescheduleAppoinment);
        txtCancelAppoinment = findViewById(R.id.txtCancelAppoinment);
        etxMessage = findViewById(R.id.etxMessage);
        famillyMemberSpinner = findViewById(R.id.famillyMemberSpinner);
        layoutMessage = findViewById(R.id.layoutMessage);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        title.setText("Cancel Appointment");
        imgBack.setOnClickListener(v -> onBackPressed());


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            bookingid = (String) bundle.get("bookingid");
            did = (String) bundle.get("did");
            hid = (String) bundle.get("hid");
            specilization = (String) bundle.get("specilization");

        }

        txtRescheduleAppoinment.setOnClickListener(v -> {
            Intent i= new Intent(context, RescheduleActivity.class);
            i.putExtra("bookingid",bookingid);
            i.putExtra("did", did);
            i.putExtra("hid", hid);
            i.putExtra("specilization",specilization);
            startActivity(i);
            finish();
        });

        famillyMemberSpinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Not available at that time");
        categories.add("Found another Doctor/pathology lab");
        categories.add("Other");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        famillyMemberSpinner.setAdapter(dataAdapter);

        txtCancelAppoinment.setOnClickListener(v -> {
            if (item.contains("Other")) {
                if (etxMessage.getText().toString().isEmpty()) {
                    showSnackBar("Enter Reason for cancellation*");
                } else {
                    item = etxMessage.getText().toString();
                    dialogCancel();
                }
            } else {
                dialogCancel();
            }


        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();

        if (item.contains("Other")) {
            layoutMessage.setVisibility(View.VISIBLE);
        } else {
            layoutMessage.setVisibility(View.GONE);
        }


    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    private void dialogCancel() {
        final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.appoinment_cancelation_dialog_first);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        TextView txtNo = dialog.findViewById(R.id.txtNo);
        TextView txtYes = dialog.findViewById(R.id.txtYes);


        txtNo.setOnClickListener(v -> {
            startActivity(new Intent(context, AppointmentHistoryActivity.class));

        });

        txtYes.setOnClickListener(v -> {
            dialog.dismiss();

            cancelAppoinment();

        });

        dialog.show();
    }

    private void cancelAppoinment() {
        String url = GlobalVar.ServerAddress + "AndroidNew/CancelAppointment";
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject params = new JSONObject();
        try {
            params.put("bookingid", bookingid);
            params.put("reson ", item);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "params", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, params,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "Canccelation :=>", response.toString());
                    try {
                        JSONObject jsonRootObject = new JSONObject(response.toString());
                        if (jsonRootObject.length() > 0) {
                            boolean result = jsonRootObject.getBoolean("result");
                            Log.e("User_Reg_result", Boolean.toString(result));
                            if (result) {
                                dialogCancelFirst();
                                finish();

                            } else if (response.toString().contains("Duplicate")) {
                            }
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

    private void dialogCancelFirst() {
        final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.appoinment_cancelation_dialog_second);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        dialog.show();
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

}
