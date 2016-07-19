package info.truemd.android.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by yashvardhansrivastava on 01/07/16.
 */
public final class SnappingHorizontalScrollView extends ViewGroup {
    /*
     * How long to animate between screens when programmatically setting with
     * setCurrentScreen using the animate parameter
     */
    private static final int ANIMATION_SCREEN_SET_DURATION_MILLIS = 500;
    private static final int FRACTION_OF_SCREEN_WIDTH_FOR_SWIPE = 6;
    private static final int INVALID_SCREEN = -1;
    private static final int SNAP_VELOCITY_DIP_PER_SECOND = 600;
    private static final int VELOCITY_UNIT_PIXELS_PER_SECOND = 800;
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_HORIZONTAL_SCROLLING = 1;
    private static final int TOUCH_STATE_VERTICAL_SCROLLING = -1;

    private int currentScreen;
    private int densityAdjustedSnapVelocity;
    private boolean firstLayout = true;
    private float lastMotionX;
    private float lastMotionY;
    private int maximumVelocity;
    private int nextScreen = INVALID_SCREEN;
    private Scroller scroller;
    private int touchSlop;
    private int touchState = TOUCH_STATE_REST;
    private VelocityTracker vvelocityTracker;
    private int lastSeenLayoutWidth = -1;

    private OnScreenSwitchListener screenSwitchListener; //on screen switch listener

    public SnappingHorizontalScrollView(final Context context) {
        super(context);
        init();
    }

    public SnappingHorizontalScrollView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Declaring the scroller and touching sensitivity parameters for the
     * scrollview.
     */
    private void init() {
        scroller = new Scroller(getContext());

        // Calculate the density-dependent snap velocity in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        densityAdjustedSnapVelocity = (int) (displayMetrics.density * SNAP_VELOCITY_DIP_PER_SECOND);

        final ViewConfiguration configuration = ViewConfiguration
                .get(getContext());
        touchSlop = configuration.getScaledTouchSlop();
        maximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onMeasure(final int widthMeasureSpec,
                             final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException(
                    "ViewSwitcher can only be used in EXACTLY mode.");
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException(
                    "ViewSwitcher can only be used in EXACTLY mode.");
        }

        // The children are given the same width and height as the workspace
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }

        if (firstLayout) {
            scrollTo(currentScreen * width, 0);
            firstLayout = false;
        }

        else if (width != lastSeenLayoutWidth) { // Width has changed
	    /*
	     * Recalculate the width and scroll to the right position to be sure
	     * we're in the right place in the event that we had a rotation that
	     * didn't result in an activity restart (code by aveyD). Without
	     * this you can end up between two pages after a rotation.
	     */
            Display display = ((WindowManager) getContext().getSystemService(
                    Context.WINDOW_SERVICE)).getDefaultDisplay();

            int displayWidth = display.getWidth();
            // get screen size
            nextScreen = Math.max(0,
                    Math.min(getCurrentScreen(), getChildCount() - 1));
            final int newX = nextScreen * displayWidth;
            final int delta = newX - getScrollX();

            scroller.startScroll(getScrollX(), 0, delta, 0, 0);
        }

        lastSeenLayoutWidth = width;
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t,
                            final int r, final int b) {
        int childLeft = 0;
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();
                child.layout(childLeft, 0, childLeft + childWidth,
                        child.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {

        final int action = event.getAction();
        boolean intercept = false;

        switch (action) {
            case MotionEvent.ACTION_MOVE:

                if (touchState == TOUCH_STATE_HORIZONTAL_SCROLLING) {

                    intercept = true;
                } else if (touchState == TOUCH_STATE_VERTICAL_SCROLLING) {

                    intercept = false;
                } else {

                    final float x = event.getX();
                    final int xDiff = (int) Math.abs(x - lastMotionX);
                    boolean xMoved = xDiff > touchSlop;

                    if (xMoved) {
                        touchState = TOUCH_STATE_HORIZONTAL_SCROLLING;
                        lastMotionX = x;
                    }

                    final float y = event.getY();
                    final int yDiff = (int) Math.abs(y - lastMotionY);
                    boolean yMoved = yDiff > touchSlop;

                    if (yMoved) {
                        touchState = TOUCH_STATE_VERTICAL_SCROLLING;
                    }
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // Release the drag.
                touchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_DOWN:

                lastMotionY = event.getY();
                lastMotionX = event.getX();
                break;
            default:
                break;
        }

        return intercept;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(final MotionEvent ev) {

//        ViewParent viewParent = getParent();
//
//        if (viewParent != null) {
//
//            viewParent.requestDisallowInterceptTouchEvent(true);
//
//        }

        if (vvelocityTracker == null) {
            vvelocityTracker = VelocityTracker.obtain();
        }
        vvelocityTracker.addMovement(ev);

        final int action = ev.getAction();
        final float x = ev.getX();

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }

                lastMotionX = x;

                if (scroller.isFinished()) {
                    touchState = TOUCH_STATE_REST;
                } else {
                    touchState = TOUCH_STATE_HORIZONTAL_SCROLLING;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(x - lastMotionX);
                boolean xMoved = xDiff > touchSlop;

                if (xMoved) {
                    // Scroll if the user moved far enough along the X axis
                    touchState = TOUCH_STATE_HORIZONTAL_SCROLLING;
                }

                if (touchState == TOUCH_STATE_HORIZONTAL_SCROLLING) {
                    // Scroll to follow the motion event
                    final int deltaX = (int) (lastMotionX - x);
                    lastMotionX = x;
                    final int scrollX = getScrollX();

                    if (deltaX < 0) {
                        if (scrollX > 0) {
                            scrollBy(Math.max(-scrollX, deltaX), 0);
                        }
                    } else if (deltaX > 0) {
                        final int availableToScroll = getChildAt(
                                getChildCount() - 1).getRight()
                                - scrollX - getWidth();

                        if (availableToScroll > 0) {
                            scrollBy(Math.min(availableToScroll, deltaX), 0);
                        }
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                if (touchState == TOUCH_STATE_HORIZONTAL_SCROLLING) {
                    final VelocityTracker velocityTracker = vvelocityTracker;
                    velocityTracker.computeCurrentVelocity(
                            VELOCITY_UNIT_PIXELS_PER_SECOND, maximumVelocity);
                    int velocityX = (int) velocityTracker.getXVelocity();

                    if (velocityX > densityAdjustedSnapVelocity
                            && currentScreen > 0) {
                        // Fling hard enough to move left
                        switchToScreen(currentScreen - 1);
                    } else if (velocityX < -densityAdjustedSnapVelocity
                            && currentScreen < getChildCount() - 1) {
                        // Fling hard enough to move right
                        switchToScreen(currentScreen + 1);
                    } else {
                        switchToDestination();
                    }

                    if (vvelocityTracker != null) {
                        vvelocityTracker.recycle();
                        vvelocityTracker = null;
                    }
                }

                touchState = TOUCH_STATE_REST;

                break;
            case MotionEvent.ACTION_CANCEL:
                touchState = TOUCH_STATE_REST;
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        } else if (nextScreen != INVALID_SCREEN) {
            currentScreen = Math.max(0,
                    Math.min(nextScreen, getChildCount() - 1));

            // Notify observer about screen change
            if (screenSwitchListener != null) {
                screenSwitchListener.onScreenSwitched(currentScreen);
            }

            nextScreen = INVALID_SCREEN;
        }
    }

    /**
     * Snaps to the screen we think the user wants (the current screen for very
     * small movements; the next/prev screen for bigger movements).
     */
    private void switchToDestination() {
        final int screenWidth = getWidth();
        int scrollX = getScrollX();
        int screen = currentScreen;
        int deltaX = scrollX - (screenWidth * currentScreen);

        // Check if they want to go to the previous screen
        if ((deltaX < 0)
                && currentScreen != 0
                && ((screenWidth / FRACTION_OF_SCREEN_WIDTH_FOR_SWIPE) < -deltaX)) {
            screen--;

            // Check if they want to go to the next screen
        } else if ((deltaX > 0)
                && (currentScreen + 1 != getChildCount())
                && ((screenWidth / FRACTION_OF_SCREEN_WIDTH_FOR_SWIPE) < deltaX)) {
            screen++;
        }

        switchToScreen(screen);
    }

    /**
     * Snap to a specific screen, animating automatically for a duration proportional to the
     * distance left to scroll.
     */
    private void switchToScreen(final int whichScreen) {
        switchToScreen(whichScreen, -1);
    }

    /**
     * Snaps to a specific screen, animating for a specific amount of time to get there.
     */
    private void switchToScreen(final int whichScreen, final int duration) {

        nextScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        final int newX = nextScreen * getWidth();
        final int delta = newX - getScrollX();

        if (duration < 0) {
            // E.g. if they've scrolled 80% of the way, only animation for 20% of the duration
            scroller.startScroll(getScrollX(), 0, delta, 0, (int) (Math.abs(delta)
                    / (float) getWidth() * ANIMATION_SCREEN_SET_DURATION_MILLIS));
        } else {
            scroller.startScroll(getScrollX(), 0, delta, 0, duration);
        }

        invalidate();
    }

    /**
     * @return The index of the currently screen.
     */
    public int getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Sets the current screen.
     *
     * @param currentScreen
     * @param animate
     */
    public void setCurrentScreen(final int currentScreen, final boolean animate) {
        this.currentScreen = Math.max(0,
                Math.min(currentScreen, getChildCount() - 1));
        if (animate) {
            switchToScreen(currentScreen);
        } else {
            scrollTo(currentScreen * getWidth(), 0);
        }
        invalidate();
    }

    /**
     * Sets the OnScreenSwitchListener
     *
     */
    public void setOnScreenSwitchListener(
            final OnScreenSwitchListener onScreenSwitchListener) {
        screenSwitchListener = onScreenSwitchListener;
    }

    /**
     * Listener for the event that the HorizontalPager switches to a new view.
     */
    public static interface OnScreenSwitchListener {
        /**
         * Notifies listeners about the new screen. Runs after the animation
         * completed.
         */
        void onScreenSwitched(int screen);
    }
}