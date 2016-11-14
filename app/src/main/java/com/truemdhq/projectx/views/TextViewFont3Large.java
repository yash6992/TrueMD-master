package com.truemdhq.projectx.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by visheshagarwal on 13/09/16.
 */
public class TextViewFont3Large extends TextView {

    public TextViewFont3Large(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewFont3Large(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewFont3Large(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "MarkOffcPro-Bold.ttf");
        setTypeface(tf ,1);
        setTextSize(22.0f);

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
