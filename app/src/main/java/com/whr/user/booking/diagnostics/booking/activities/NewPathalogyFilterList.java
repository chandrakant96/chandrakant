package com.whr.user.booking.diagnostics.booking.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.whr.user.R;

import java.util.ArrayList;

public class NewPathalogyFilterList extends AppCompatActivity {
    private RadioGroup radioGroup, radioGroupOne;
    private RadioButton pathology, radiology, feesLow, feesMiddle, feesHigh;
    private RadioButton nearBy;
    private String type = "", nearby = "", minfee = "", maxfee = "";
    private TextView txtFilter;
    private Context context;
    private TextView close;
    ArrayList<String> list = new ArrayList<String>();
    private TextView txtResetFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pathalogy_filter_list);

        context = NewPathalogyFilterList.this;
        radioGroup = findViewById(R.id.type);
        pathology = findViewById(R.id.pathology);
        radiology = findViewById(R.id.radiology);
        nearBy = findViewById(R.id.nearBy);
        radioGroupOne = findViewById(R.id.fees);
        feesLow = findViewById(R.id.feesLow);
        feesMiddle = findViewById(R.id.feesMidle);
        feesHigh = findViewById(R.id.feesHigh);
        txtFilter = findViewById(R.id.txtFilter);
        close = findViewById(R.id.close);
        txtResetFilter = findViewById(R.id.txtResetFilter);


        close.setOnClickListener(v -> finish());

        txtResetFilter.setOnClickListener(v -> {
            nearBy.setChecked(false);
            pathology.setChecked(false);
            radiology.setChecked(false);
            feesMiddle.setChecked(false);
            feesHigh.setChecked(false);
            feesLow.setChecked(false);
            list.clear();
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.pathology) {
                type = "1";
                list.add("Pathology");

            } else {
                type = "2";
                list.add("Radiology");

            }

        });
        nearBy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (nearBy.isChecked()) {
                nearby = "Asc";
                list.add("NearBy");
            } else {
                nearby = "";

            }
        });

        radioGroupOne.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.feesLow) {
                minfee = "1";
                maxfee = "300";
                list.add(minfee + "-" + maxfee);
            } else if (checkedId == R.id.feesMidle) {
                minfee = "300 ";
                maxfee = "500";
                list.add(minfee + "-" + maxfee);
            } else {
                minfee = "500";
                maxfee = "10000";
                list.add(minfee + "-" + maxfee);

            }
        });

        txtFilter.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewPathalogyActivity.class);
            intent.putExtra("status", "filter");
            intent.putExtra("type", type);
            intent.putExtra("nearby", nearby);
            intent.putExtra("minfee", minfee);
            intent.putExtra("maxfee", maxfee);
            intent.putStringArrayListExtra("list", list);
            startActivity(intent);
            finish();
        });

    }
}
