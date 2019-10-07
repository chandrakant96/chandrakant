package com.whr.user.pojo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

public class CustomSliderView extends BaseSliderView {
    public CustomSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(com.daimajia.slider.library.R.layout.render_type_text,null);
        ImageView target = v.findViewById(com.daimajia.slider.library.R.id.daimajia_slider_image);
        LinearLayout frame = v.findViewById(com.daimajia.slider.library.R.id.description_layout);
        frame.setBackgroundColor(Color.TRANSPARENT);

        bindEventAndShow(v, target);
        return v;
    }
}

