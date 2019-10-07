package com.whr.user.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewScreen extends AppCompatActivity {
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private TextView cardSubmit;
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    Context context;
    private String doctorId;
    private PreferenceUtils pref;

    //screen one
    private TextView txtYesOne, txtNoOne;
    String valueone = "Yes";
    //screen two
    private TextView txtYesTwo, txtNoTwo;
    String valuetwo = "Yes";
    //screen three
    private CheckBox chktreatment, chkExplanation, chkdoctor;
    private String valuethree = "", chkvalueone = "", chkvaluetwo = "", chkvaluethree = "";
    //screen four
    private EditText name, experience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_screen);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        cardSubmit = findViewById(R.id.txtLogin);


        layouts = new int[]{
                R.layout.review_screen_first,
                R.layout.review_screen_second,
                R.layout.review_screen_three,
                R.layout.review_screen_four
        };

        addBottomDots(0);
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutReview);
        context = ReviewScreen.this;
        pref = new PreferenceUtils(context);

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        doctorId = bundle.getString("docterIdkey");


        cardSubmit.setOnClickListener(v -> {
            int current = getItem(+1);
            if (current < layouts.length) {
                // move to next screen
                viewPager.setCurrentItem(current);
            } else {
                if (name.getText().toString().isEmpty()) {
                    GlobalVar.showSnackBar(coordinatorLayout, "Please Enter Your Name", context, R.color.red);
                } else if (experience.getText().toString().isEmpty()) {
                    GlobalVar.showSnackBar(coordinatorLayout, "Please Enter Your Experience", context, R.color.red);
                } else {
                    submitReview();
                }
            }
        });


    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length - 1) {
                cardSubmit.setVisibility(View.VISIBLE);

            } else {
                cardSubmit.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            if (position == 0) {
                txtYesOne = findViewById(R.id.txtYesRevieveScreenOne);
                txtNoOne = findViewById(R.id.txtNoRevieveScreenOne);

                txtYesOne.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        valueone = "Yes";
                        txtYesOne.setBackgroundResource(R.drawable.square_border_green);
                        txtYesOne.setTextColor(getResources().getColor(R.color.white));
                        //txtYesOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_review_tick_white, 0);
                        txtNoOne.setBackground(getResources().getDrawable(R.drawable.square_border_one));
                        txtNoOne.setTextColor(getResources().getColor(R.color.primary));
                        //txtNoOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_review_cross_blue, 0);


                    }
                });
                txtNoOne.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        valueone = "No";
                        txtNoOne.setTextColor(getResources().getColor(R.color.white));
                        txtNoOne.setBackgroundResource(R.drawable.square_border_red);
                        //txtNoOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_review_cross_white, 0);
                        txtYesOne.setBackground(getResources().getDrawable(R.drawable.square_border_one));
                        txtYesOne.setTextColor(getResources().getColor(R.color.primary));
                        // txtYesOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_review_tick_blue, 0);

                    }
                });
            }

            if (position == 1) {
                txtYesTwo = findViewById(R.id.txtYesRevieveScreenTwo);
                txtNoTwo = findViewById(R.id.txtNoRevieveScreenTwo);

                txtYesTwo.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        valuetwo = "Yes";
                        txtYesTwo.setBackgroundResource(R.drawable.square_border_green);
                        txtYesTwo.setTextColor(getResources().getColor(R.color.white));
                        //txtYesTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_review_tick_white, 0);
                        txtNoTwo.setBackground(getResources().getDrawable(R.drawable.square_border_one));
                        txtNoTwo.setTextColor(getResources().getColor(R.color.primary));
                        //txtNoTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_review_cross_blue, 0);


                    }
                });
                txtNoTwo.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        valuetwo = "No";
                        txtNoTwo.setTextColor(getResources().getColor(R.color.white));
                        txtNoTwo.setBackgroundResource(R.drawable.square_border_red);
                        // txtNoTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_review_cross_white, 0);
                        txtYesTwo.setBackground(getResources().getDrawable(R.drawable.square_border_one));
                        txtYesTwo.setTextColor(getResources().getColor(R.color.primary));
                        //txtYesTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_review_tick_blue, 0);

                    }
                });


            }
            if (position == 2) {
                chktreatment = findViewById(R.id.chktreatment);
                chkExplanation = findViewById(R.id.chkExplanation);
                chkdoctor = findViewById(R.id.chkdoctor);

                chktreatment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        chkvalueone = "Treatment Satisfaction";
                    }
                });
                chkExplanation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        chkvaluetwo = "Explanation regarding your Health Issues";
                    }
                });
                chkdoctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        chkvaluethree = "Doctor Friendliness";
                    }
                });


            }

            if (position == 3) {
                name = findViewById(R.id.name);
                experience = findViewById(R.id.yourExperience);
            }
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    String TAG = getClass().getSimpleName();

    private void submitReview() {
        String url = GlobalVar.ServerAddress + "AndroidNew/AddReview";
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject params = new JSONObject();
        try {
            valuethree = chkvalueone + "\n" + chkvaluetwo + "\n" + chkvaluethree;
            params.put("userid", pref.getUID());
            params.put("name", name.getText().toString().trim());
            params.put("doctorId", doctorId);
            params.put("recommend", valueone);
            params.put("valueformoney", valuetwo);
            params.put("thingsimproved", valuethree);
            params.put("userexperience", experience.getText().toString().trim());

            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "params", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, params,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "Review Submit :=>", response.toString());
                    try {
                        JSONObject jsonRootObject = new JSONObject(response.toString());
                        if (jsonRootObject.length() > 0) {
                            boolean result = jsonRootObject.getBoolean("result");
                            //boolean result = jsonRootObject.getBoolean("result");
                            Log.e("Review_result", Boolean.toString(result));
                            if (result) {
                                Log.e("Review_result", Boolean.toString(result));
                                GlobalVar.showSnackBar(coordinatorLayout, "Thank You For Your Feedback", context, R.color.green);
                                onBackPressed();

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalVar.hideProgressDialog();
                GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
            }
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }
}