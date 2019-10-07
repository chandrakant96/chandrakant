package com.whr.user.com.WHR.ViewPackage;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by lenovo on 4/21/2017.
 */

public class OpenSans_Bold extends android.support.v7.widget.AppCompatTextView {

    public OpenSans_Bold(Context context) {
        super(context);
        init();
    }

    public OpenSans_Bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpenSans_Bold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "OpenSans-Bold.ttf");
        setTypeface(tf);
    }


}
