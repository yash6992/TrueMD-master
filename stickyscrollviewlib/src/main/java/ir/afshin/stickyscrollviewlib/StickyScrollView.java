package ir.afshin.stickyscrollviewlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.support.annotation.IdRes;


/**
 * Created by afshinhoseini on 1/8/16.
 */
public class StickyScrollView extends ScrollView {

    private StickyHeader stickyHeader = null;
    private FrameLayout scrollContent = null;
    private final String scrollContentTag = "ScrollContentTag";

// ____________________________________________________________________

    public StickyScrollView(Context context) {
        super(context);
    }

    public StickyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public StickyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

// ____________________________________________________________________

    @Override
    public void onViewAdded(View child) {

        super.onViewAdded(child);

        if(scrollContent == null) {

            scrollContent = new FrameLayout(getContext());
            ScrollView.LayoutParams contentLayoutParams = new ScrollView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            scrollContent.setLayoutParams(contentLayoutParams);

            removeView(child);
            child.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            child.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            scrollContent.addView(child);
            scrollContent.setTag(scrollContentTag);
            addView(scrollContent);


        }
        else {

            if(stickyHeader == null)
                setStickyHeader((StickyHeader) child.findViewWithTag(StickyHeader.headerTag));
        }
    }

// ____________________________________________________________________

    public void setStickyHeader(@IdRes int viewId) {


         throw  new RuntimeException("Not implemented yet...!");
        //setStickyHeader((StickyHeader) findViewById(viewId));
    }

// ____________________________________________________________________

    public void setStickyHeader(StickyHeader stickyHeader) {

        this.stickyHeader = stickyHeader;

        if(stickyHeader != null) {

            stickyHeader.sizeTofit();
            scrollContent.addView(stickyHeader.headerContent);
            getViewTreeObserver().addOnGlobalLayoutListener(onLayoutListener);
        }
    }

// ____________________________________________________________________

    private ViewTreeObserver.OnGlobalLayoutListener onLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            Log.e("GlobalLayout","Some logs");
            repositionStickyHeader();
        }
    };

// ____________________________________________________________________

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        repositionStickyHeader();
    }


// ____________________________________________________________________

    private void repositionStickyHeader() {

        if(stickyHeader != null) {

            stickyHeader.headerContent.setTranslationY(Math.max(getScrollY(), stickyHeader.getTop()));
        }
    }

// ____________________________________________________________________
}
