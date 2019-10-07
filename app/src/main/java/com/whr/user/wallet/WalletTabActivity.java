package com.whr.user.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.whr.user.activities.NoDataAvailableActivity;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WalletTabActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PreferenceUtils pref;
    private List<WallatPojo> rowListItem = new ArrayList<>();
    private List<WallatPojo> creadetedList = new ArrayList<>();
    private List<WallatPojo> debedetedList = new ArrayList<>();

    private CoordinatorLayout coordinatorLayout;
    private Context context;
    private RequestQueue mQueue;
    private ConnectionDector connectionDector;
    String TAG = getClass().getSimpleName();
    private TextView txtTotalPoints;

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_tab);
        context = WalletTabActivity.this;
        connectionDector = new ConnectionDector(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtTotalPoints = findViewById(R.id.txtTotalPoints);

        MaterialRippleLayout imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        pref = new PreferenceUtils(WalletTabActivity.this);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {

        if (connectionDector.isConnectingToInternet()) {
            methodVoid(viewPager);
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
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

    public void methodVoid(final ViewPager view_Pager) {
        String url = GlobalVar.ServerAddress + "AndroidNew/Wallet";
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", pref.getUID());
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, response -> {
            rowListItem.clear();
            Log.e("methodVoid>", response.toString());
            Gson gson = new Gson();
            JSONArray jsonArray1 = null;
            rowListItem.clear();
            try {
                jsonArray1 = response.getJSONArray("result");
                if (jsonArray1 != null && jsonArray1.length() > 0) {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json = jsonArray1.getJSONObject(i);
                        WallatPojo pojo = gson.fromJson(json.toString(), WallatPojo.class);
                        rowListItem.add(pojo);
                        txtTotalPoints.setText(pojo.getTotalPoints());
                        if (pojo.getType() != null) {
                            if (pojo.getType().length() > 0) {
                                if (pojo.getType().equals("C")) {
                                    creadetedList.add(pojo);
                                } else if (pojo.getType().equals("D")) {
                                    debedetedList.add(pojo);
                                }
                            }
                        }
                    }
                } else {
                    rowListItem = new ArrayList<>();

                }

                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                EarnFragment earnFragment = new EarnFragment();
                CreatedFragment lifeStyleTextFragment = new CreatedFragment();
                DebitedFragment debitedFragment = new DebitedFragment();

                if (rowListItem.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) rowListItem);
                    earnFragment.setArguments(bundle);
                }
                if (creadetedList.size() > 0) {
                    Bundle cbundle = new Bundle();
                    cbundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) creadetedList);
                    lifeStyleTextFragment.setArguments(cbundle);
                }
                if (debedetedList.size() > 0) {
                    Bundle dbundle = new Bundle();
                    dbundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) debedetedList);
                    debitedFragment.setArguments(dbundle);
                }


                adapter.addFragment(earnFragment, "All");
                adapter.addFragment(lifeStyleTextFragment, "Credited");
                adapter.addFragment(debitedFragment, "Debited");

                view_Pager.setAdapter(adapter);
                tabLayout = findViewById(R.id.tabs);

                tabLayout.setupWithViewPager(view_Pager);

                TextView tabOne = (TextView) LayoutInflater.from(WalletTabActivity.this).inflate(R.layout.lifestyle_fistaid_tab_header_layout, null);
                tabOne.setText(getString(R.string.All));
                tabLayout.getTabAt(0).setCustomView(tabOne);

                TextView tabtwo = (TextView) LayoutInflater.from(WalletTabActivity.this).inflate(R.layout.lifestyle_fistaid_tab_header_layout, null);
                tabtwo.setText(R.string.Credited);
                tabLayout.getTabAt(1).setCustomView(tabtwo);

                TextView tabthree = (TextView) LayoutInflater.from(WalletTabActivity.this).inflate(R.layout.lifestyle_fistaid_tab_header_layout, null);
                tabthree.setText(R.string.Debited);
                tabLayout.getTabAt(2).setCustomView(tabthree);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> showSnackBar(VolleyErrorHelper.getMessage(error, context)));
        mQueue.add(jsonRequest);
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            methodVoid(viewPager);
        }
    }
}