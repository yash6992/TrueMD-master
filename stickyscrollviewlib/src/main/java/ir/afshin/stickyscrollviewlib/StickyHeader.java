package ir.afshin.stickyscrollviewlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by afshinhoseini on 1/8/16.
 */
public class StickyHeader extends LinearLayout {

    View headerContent = null;
    static final String headerTag = "This is unique tag to find this header.";
    private boolean didSizedToFit = false;

// ____________________________________________________________________

    public StickyHeader(Context context) {
        super(context);
        init();
    }

    public StickyHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StickyHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public StickyHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

// ____________________________________________________________________

    private void init() {

        super.setTag(headerTag);
    }
// ____________________________________________________________________

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        if(headerContent == null) {

            headerContent = child;
        }
        else {

            throw new RuntimeException("StickyHeader only can have one child view.");
        }
    }

// ____________________________________________________________________
    void sizeTofit() {


        int childHeight = 0;
        int lpHeight = headerContent.getLayoutParams().height;
        if(lpHeight == ViewGroup.LayoutParams.MATCH_PARENT) {

            headerContent.getLayoutParams().height = lpHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        if(lpHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {

            headerContent.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            childHeight = headerContent.getMeasuredHeight();
        }
        else
            childHeight = lpHeight;


        getLayoutParams().height = childHeight;
        getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        requestLayout();
        removeView(headerContent);
    }

// ____________________________________________________________________


}
