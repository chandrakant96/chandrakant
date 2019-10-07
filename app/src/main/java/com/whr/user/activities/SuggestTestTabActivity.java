package com.whr.user.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.treatement_suggested.fragments.SuggestTestFrgment;
import com.whr.user.treatement_suggested.fragments.SuggestTrearmentFragment;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.FamilyMemberListpojo;
import com.whr.user.pojo.GlobalVar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SuggestTestTabActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ConnectionDector connectionDector;
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue mQueue;
    private Context context;
    private List<FamilyMemberListpojo> familyMemberList;
    private PreferenceUtils pref;
    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_aid_tab_xml);
        overridePendingTransitionEnter();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = SuggestTestTabActivity.this;
        ///------------New Toolbar Code----------------
        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        ///------------New Toolbar code------------------------------
        familyMemberList = new ArrayList<>();
        pref = new PreferenceUtils(getApplicationContext());
        connectionDector = new ConnectionDector(SuggestTestTabActivity.this);
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00baf2"));
        tabLayout.setTabTextColors(Color.parseColor("#707070"), Color.parseColor("#000000"));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        if (connectionDector.isConnectingToInternet()) {
            getFamilyMembers(viewPager);
        } else {
            GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoInternetConnection), context, R.color.red);
        }
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

    private void getFamilyMembers(final ViewPager viewPager) {
        String url = GlobalVar.ServerAddress + "AndroidNew/MyFamilyNew";

        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            GlobalVar.errorLog(TAG, "obj", obj.toString());
            GlobalVar.errorLog(TAG, "url", url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "getFamilyMembers", response.toString());
                    Gson gson = new Gson();
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
                            GlobalVar.showSnackBar(coordinatorLayout, getString(R.string.NoDataAvailable), context, R.color.red);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

                    Bundle suggestTestBundle = new Bundle();
                    suggestTestBundle.putString("suggest", "Suggest_Test");
                    suggestTestBundle.putParcelableArrayList("familyArray",
                            (ArrayList<? extends Parcelable>) familyMemberList);

                    SuggestTestFrgment suggestTestFrgment = new SuggestTestFrgment();
                    suggestTestFrgment.setArguments(suggestTestBundle);
                    adapter.addFragment(suggestTestFrgment, "Suggest Test");

                    Bundle suggestTreatmentBundle = new Bundle();
                    suggestTreatmentBundle.putString("suggest", "Suggest_Treatment");
                    suggestTreatmentBundle.putParcelableArrayList("familyArray",
                            (ArrayList<? extends Parcelable>) familyMemberList);

                    SuggestTrearmentFragment suggestTrearmentFragment = new SuggestTrearmentFragment();
                    suggestTrearmentFragment.setArguments(suggestTreatmentBundle);
                    adapter.addFragment(suggestTrearmentFragment, "Suggest Treatment");

                    viewPager.setAdapter(adapter);

                    TextView tabOne = (TextView) LayoutInflater.from(context).inflate(R.layout.lifestyle_fistaid_tab_header_layout, null);
                    tabOne.setText(getString(R.string.Test));
                    tabLayout.getTabAt(0).setCustomView(tabOne);

                    TextView tabtwo = (TextView) LayoutInflater.from(context).inflate(R.layout.lifestyle_fistaid_tab_header_layout, null);
                    tabtwo.setText(getString(R.string.Treatment));
                    tabLayout.getTabAt(1).setCustomView(tabtwo);

                }, error -> GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red));
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            this.finish();
            overridePendingTransitionExit();
        }
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}




