package com.truemdhq.projectx.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by visheshagarwal on 13/09/16.
 */
public class TextViewFontRupeeMedium extends TextView {

    public TextViewFontRupeeMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewFontRupeeMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewFontRupeeMedium(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Rupee.ttf");
        setTypeface(tf ,1);
        setTextSize(20.0f);

    }

}
