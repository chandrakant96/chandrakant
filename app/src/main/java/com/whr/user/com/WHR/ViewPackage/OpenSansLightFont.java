package com.whr.user.com.WHR.ViewPackage;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Etech001 on 18/11/2016.
 */

public class OpenSansLightFont extends android.support.v7.widget.AppCompatTextView {
    public OpenSansLightFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public OpenSansLightFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpenSansLightFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "OpenSans-Light.ttf");
        setTypeface(tf);
    }
}
