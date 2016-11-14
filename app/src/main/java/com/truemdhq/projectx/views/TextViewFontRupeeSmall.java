package com.truemdhq.projectx.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by visheshagarwal on 13/09/16.
 */
public class TextViewFontRupeeSmall extends TextView {

    public TextViewFontRupeeSmall(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewFontRupeeSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewFontRupeeSmall(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Rupee.ttf");
        setTypeface(tf ,1);
        setTextSize(14.0f);

    }

}
