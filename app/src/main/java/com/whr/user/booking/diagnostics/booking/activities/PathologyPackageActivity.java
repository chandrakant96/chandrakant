package com.whr.user.booking.diagnostics.booking.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.PreferenceUtils;

public class PathologyPackageActivity extends AppCompatActivity {
    private MaterialRippleLayout imgBack;
    private TextView txtTitle;
    private String typeid = "", pathid = "", pathname = "";
    private PreferenceUtils preferenceUtils;
    private Context context;
    private ConnectionDector connectionDector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathology_package);

        context = PathologyPackageActivity.this;
        connectionDector = new ConnectionDector(context);
        preferenceUtils = new PreferenceUtils(context);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            typeid = bundle.getString("typeid");
            pathid = bundle.getString("pathid");
            pathname = bundle.getString("pathname");

        }

        imgBack = findViewById(R.id.imgBack);
        txtTitle = findViewById(R.id.txtTitle);


        imgBack.setOnClickListener(v -> onBackPressed());
        txtTitle.setText(pathname);
    }
}
