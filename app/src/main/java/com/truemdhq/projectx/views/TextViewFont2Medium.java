package com.truemdhq.projectx.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by visheshagarwal on 13/09/16.
 */
public class TextViewFont2Medium extends TextView {

    public TextViewFont2Medium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewFont2Medium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewFont2Medium(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "MarkOffcPro-Medium.ttf");
        setTypeface(tf ,1);
        setTextSize(14.0f);

    }
    public void setText (double text, int type) {
        if (type==2)
            super.setText("Rs. "+getViewValue(text));
    }



    public  String getViewValue(double value) {

        String returnValue = String.format( "%.2f", value );
        return  returnValue;
    }


}
