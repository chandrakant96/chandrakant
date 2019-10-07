package com.whr.user.com.WHR.Utils;

/**
 * Created by lenovo on 12/10/2016.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whr.user.R;
import com.squareup.picasso.Picasso;

public class CustomSwipViewAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String clinicArray[];

    public CustomSwipViewAdapter(Context context, String[] clinicArray) {
        this.context = context;
        this.clinicArray = clinicArray;
    }

    @Override
    public int getCount() {
        return clinicArray.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == (ImageView) arg1);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View iteamview = inflater.inflate(R.layout.slidingimages_layout, container, false);
        ImageView imageview = (ImageView) iteamview.findViewById(R.id.slingingImage);
        if (clinicArray[position] != null) {
            if (clinicArray[position].length() > 0) {

                String profile_img = clinicArray[position];
                String str = profile_img.replace("\\", "");
                if (profile_img.length() > 0) {
                    String pStr = str.replaceAll(" ", "%20");
                    if (pStr != null) {
                        Picasso.with(context).load(clinicArray[position]).fit().into(imageview);
                    }
                }
            }
        }
        container.addView(iteamview);
        return iteamview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}