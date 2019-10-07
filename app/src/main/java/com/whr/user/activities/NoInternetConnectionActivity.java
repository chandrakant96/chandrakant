package com.whr.user.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.pojo.GlobalVar;

import org.w3c.dom.Text;

public class NoInternetConnectionActivity extends AppCompatActivity {

    ImageView imgError;
    TextView txtError;
    ConnectionDector dector;
    Context context;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_data_available);
        context = this;
        txtError = findViewById(R.id.txtError);
        imgError = findViewById(R.id.imgError);
        imgError.setImageResource(R.drawable.no_internet_connection);
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        txtError.setText("No Internet Connectivity");
        dector = new ConnectionDector(context);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
    }

    @Override
    public void onBackPressed() {
        if (dector.isConnectingToInternet()) {
            Intent intent = getIntent();
            setResult(Activity.RESULT_OK, intent);
            finish();
            overridePendingTransitionExit();
            super.onBackPressed();
        } else {
            showSnackBar("No Internet Connection");
        }
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }


    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}