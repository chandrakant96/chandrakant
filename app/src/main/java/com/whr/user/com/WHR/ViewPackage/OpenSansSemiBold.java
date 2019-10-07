package com.whr.user.com.WHR.ViewPackage;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by lenovo on 5/13/2017.
 */

public class OpenSansSemiBold extends android.support.v7.widget.AppCompatTextView {

    public OpenSansSemiBold(Context context) {
        super(context);
        init();
    }

    public OpenSansSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpenSansSemiBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "OpenSans-Semibold.ttf");
        setTypeface(tf);
    }


}
