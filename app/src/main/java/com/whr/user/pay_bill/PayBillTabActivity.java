package com.whr.user.pay_bill;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.pay_bill.fragments.PayBillFragment;
import com.whr.user.pay_bill.fragments.PaymentHistoryFragment;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PayBillTabActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PreferenceUtils pref;
    String TAG = getClass().getSimpleName();
    Context context;
    RequestQueue mQueue;
    private ConnectionDector connectionDector;
    private List<FamilyMemberListpojo> familyMemberList = new ArrayList<>();

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill_tab);
        context = this;
        GlobalVar.errorLog(TAG, "TAG", "Activity");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        connectionDector = new ConnectionDector(context);

        ///------------New Toolbar Code----------------
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        ///------------New Toolbar code------------------------------
        pref = new PreferenceUtils(PayBillTabActivity.this);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00baf2"));
        tabLayout.setTabTextColors(Color.parseColor("#707070"), Color.parseColor("#000000"));
        tabLayout.setupWithViewPager(viewPager);

        if (connectionDector.isConnectingToInternet()) {
            getFamilyMembers(viewPager);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            getFamilyMembers(viewPager);
        }
    }

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pref.setMedicineName("");
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    Gson gson = new Gson();

    private void getFamilyMembers(final ViewPager viewPager) {
        String url = GlobalVar.ServerAddress + "AndroidNew/MyFamilyNew";
        GlobalVar.showProgressDialog(context, "", true);

        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            // obj.put("uid", "7588249500");
            GlobalVar.errorLog(TAG, "obj", obj.toString());
            GlobalVar.errorLog(TAG, "url", url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "getFamilyMembers", response.toString());
                    GlobalVar.hideProgressDialog();
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = response.getJSONArray("result");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                FamilyMemberListpojo model = gson.fromJson(json.toString(), FamilyMemberListpojo.class);
                                familyMemberList.add(model);
                            }
                        } else {
                            //GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                    Bundle suggestTestBundle = new Bundle();
                    suggestTestBundle.putParcelableArrayList("familyArray",
                            (ArrayList<? extends Parcelable>) familyMemberList);

                    PayBillFragment suggestTestFrgment = new PayBillFragment();
                    suggestTestFrgment.setArguments(suggestTestBundle);
                    adapter.addFragment(suggestTestFrgment, "Payment");

                    Bundle suggestTreatmentBundle = new Bundle();
                    suggestTreatmentBundle.putParcelableArrayList("familyArray",
                            (ArrayList<? extends Parcelable>) familyMemberList);

                    PaymentHistoryFragment suggestTrearmentFragment = new PaymentHistoryFragment();
                    suggestTrearmentFragment.setArguments(suggestTreatmentBundle);
                    adapter.addFragment(suggestTrearmentFragment, "Payment History");
                    viewPager.setAdapter(adapter);
                    TextView tabOne = (TextView) LayoutInflater.from(PayBillTabActivity.this).inflate(R.layout.lifestyle_fistaid_tab_header_layout, null);
                    tabOne.setText(getString(R.string.Pay));
                    tabLayout.getTabAt(0).setCustomView(tabOne);
                    TextView tabtwo = (TextView) LayoutInflater.from(PayBillTabActivity.this).inflate(R.layout.lifestyle_fistaid_tab_header_layout, null);
                    tabtwo.setText(getString(R.string.PaymentHistory));
                    tabLayout.getTabAt(1).setCustomView(tabtwo);

                }, error -> {
            GlobalVar.hideProgressDialog();
            // GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);

    }
}