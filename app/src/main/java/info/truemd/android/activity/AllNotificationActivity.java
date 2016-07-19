package info.truemd.android.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import info.truemd.android.R;
import info.truemd.android.helper.CustomHtmlTextParsing;
import info.truemd.android.helper.SessionManager;
import io.paperdb.Paper;

/**
 * Sample activity demonstrating swipe to remove on recycler view functionality.
 * The interesting parts are drawing while items are animating to their new positions after some items is removed
 * and a possibility to undo the removal.
 */
public class AllNotificationActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    String notificationJarray; TextView title;
    JSONArray allNotification, reverse;
    ImageButton backImageButton, clearAll;
    SessionManager sessionManager; int lastInsertedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notification);
        sessionManager= new SessionManager(AllNotificationActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        Typeface tf_r = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        backImageButton = (ImageButton) findViewById(R.id.n_backImageButton);
        clearAll = (ImageButton) findViewById(R.id.n_clearAllImageButton);
        title = (TextView) findViewById(R.id.n_title_tv);
        title.setTypeface(tf_r);
        clearAll.setVisibility(View.INVISIBLE);
        try {
        List<String> allKeysnoti = Paper.book("notification").getAllKeys();
        if(allKeysnoti.contains(""+user.get(SessionManager.KEY_MOBILE_UM)))
        {
            notificationJarray = Paper.book("notification").read(""+user.get(SessionManager.KEY_MOBILE_UM));
            Log.e("ifAllNotification:", notificationJarray);
            allNotification= new JSONArray();
            reverse= new JSONArray(notificationJarray);
            lastInsertedIndex = reverse.length();

            for(int loop=lastInsertedIndex-1; loop>=0; loop--){
                Log.e("",""+reverse.getJSONObject(loop).toString());
                allNotification.put(reverse.getJSONObject(loop));
            }

        }
        else
        {
            Paper.book("notification").write(""+user.get(SessionManager.KEY_MOBILE_UM), "[]");
           notificationJarray = Paper.book("notification").read(""+user.get(SessionManager.KEY_MOBILE_UM));
            allNotification= new JSONArray();
            lastInsertedIndex = 0;
            Log.e("finallyAllNotification:",allNotification.length()+":"+ allNotification.toString());
            Log.e("elseAllNotification:", "");
        }

        Log.e("finallyAllNotification:", allNotification.toString());




        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("NotificationErr: ",e.getMessage());

        }

        mRecyclerView = (RecyclerView) findViewById(R.id.n_recycler_view);
        setUpRecyclerView();

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onBackPressed();
            }
        });

    }


    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new TestAdapter());
        mRecyclerView.setHasFixedSize(true);
       // setUpItemTouchHelper();
       // setUpAnimationDecoratorHelper();

        ((TestAdapter)mRecyclerView.getAdapter()).setUndoOn(true);
    }

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(getResources().getColor(R.color.windowBackground));
                xMark = ContextCompat.getDrawable(AllNotificationActivity.this, R.drawable.ic_clear_all_black_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) AllNotificationActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                TestAdapter testAdapter = (TestAdapter)recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                TestAdapter adapter = (TestAdapter)mRecyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(getResources().getColor(R.color.windowBackground));
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

    /**
     * RecyclerView adapter enabling undo on a swiped away item.
     */
    class TestAdapter extends RecyclerView.Adapter {

        private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

        List<JSONObject> items;
        List<JSONObject> itemsPendingRemoval;
        // so we can add some more items for testing purposes
        boolean undoOn; // is undo on, you can turn it on from the toolbar menu

        private Handler handler = new Handler(); // hanlder for running delayed runnables
        HashMap<JSONObject, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

        public TestAdapter() {


            items = new ArrayList<>();
            itemsPendingRemoval = new ArrayList<>();

            // let's generate some items

            // this should give us a couple of screens worth
            for (int i=0; i< lastInsertedIndex; i++) {
                try {
                    items.add(allNotification.getJSONObject(i));
                    Log.e("Notification:","Items added to notification: "+ i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            try {

                TestViewHolder viewHolder = (TestViewHolder)holder;
                final JSONObject item = items.get(position);

                if (itemsPendingRemoval.contains(item)) {
                    // we need to show the "undo" state of the row
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                    viewHolder.titleTextView.setVisibility(View.GONE);
                    viewHolder.title.setVisibility(View.GONE);
                   // viewHolder.orderh.setVisibility(View.GONE);
                    viewHolder.orderno.setVisibility(View.VISIBLE);
                    String dateTime = item.optString("timestamp");

                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
                            .withLocale(Locale.ROOT)
                            .withChronology(ISOChronology.getInstanceUTC());

                    DateTime then = formatter.parseDateTime(dateTime);

                    DateTime now = DateTime.now();

                    long diff = now.getMillis()-then.getMillis();

                    String timeAgo=" ";

                    if(diff<60000) {
                        timeAgo = "just now";
                    }
                    else if(diff>60000&&diff<3600000){
                        timeAgo =(String) DateUtils.getRelativeTimeSpanString( then.getMillis(), now.getMillis(),DateUtils.MINUTE_IN_MILLIS);
                    }
                    else if(diff>3600000&&diff<86400000){
                        timeAgo =(String) DateUtils.getRelativeTimeSpanString( then.getMillis(), now.getMillis(),DateUtils.HOUR_IN_MILLIS);
                    }
                    else if(diff>86400000&&diff<259200000){
                        timeAgo =(String) DateUtils.getRelativeTimeSpanString( then.getMillis(), now.getMillis(),DateUtils.DAY_IN_MILLIS);
                    }
                    else if(diff>259200000) {
                        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy");
                        timeAgo = dtfOut.print(then);
                    }
                    viewHolder.orderno.setText(timeAgo);
                    viewHolder.undoButton.setVisibility(View.INVISIBLE);
                    viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // user wants to undo the removal, let's cancel the pending task
                            Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                            pendingRunnables.remove(item);
                            if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                            itemsPendingRemoval.remove(item);
                            allNotification= AllNotificationActivity.remove(position,allNotification);
                            Paper.book("notification").write("notification_jarray", allNotification.toString());
                            // this will rebind the row in "normal" state
                            notifyItemChanged(items.indexOf(item));
                        }
                    });
                } else {
                    // we need to show the "normal" state
                    viewHolder.itemView.setBackgroundColor(Color.WHITE);
                    viewHolder.titleTextView.setVisibility(View.VISIBLE);
                    viewHolder.title.setVisibility(View.VISIBLE);
                    //viewHolder.orderh.setVisibility(View.VISIBLE);
                    viewHolder.orderno.setVisibility(View.VISIBLE);
                    viewHolder.titleTextView.setText(CustomHtmlTextParsing.parse(item.optString("message")));
                    viewHolder.title.setText(item.optString("title"));

                    String dateTime = item.optString("timestamp");

                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
                            .withLocale(Locale.ROOT)
                            .withChronology(ISOChronology.getInstanceUTC());

                    DateTime then = formatter.parseDateTime(dateTime);

                    DateTime now = DateTime.now();

                    long diff = now.getMillis()-then.getMillis();

                   String timeAgo=" ";

                    if(diff<60000) {
                      timeAgo = "just now";
                    }
                    else if(diff>60000&&diff<3600000){
                        timeAgo =(String) DateUtils.getRelativeTimeSpanString( then.getMillis(), now.getMillis(),DateUtils.MINUTE_IN_MILLIS);
                    }
                    else if(diff>3600000&&diff<86400000){
                        timeAgo =(String) DateUtils.getRelativeTimeSpanString( then.getMillis(), now.getMillis(),DateUtils.HOUR_IN_MILLIS);
                    }
                    else if(diff>86400000&&diff<259200000){
                        timeAgo =(String) DateUtils.getRelativeTimeSpanString( then.getMillis(), now.getMillis(),DateUtils.DAY_IN_MILLIS);
                    }
                    else if(diff>259200000) {
                        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy");
                        timeAgo = dtfOut.print(then);
                    }
                    viewHolder.orderno.setText(timeAgo);
                    viewHolder.undoButton.setVisibility(View.GONE);
                    viewHolder.undoButton.setOnClickListener(null);
                }
            } catch (Exception e) {
                Log.e("OnBindViewHolder: ",position+":"+e.getMessage());
                e.printStackTrace();
            }
        }

        // Example of use: remove(i, savedProfiles);



        @Override
        public int getItemCount() {
            return items.size();
        }

        /**
         *  Utility method to add some rows for testing purposes. You can add rows from the toolbar menu.
         */

        public void setUndoOn(boolean undoOn) {
            this.undoOn = undoOn;
        }

        public boolean isUndoOn() {
            return undoOn;
        }

        public void pendingRemoval(int position) {
            final JSONObject item = items.get(position);
            if (!itemsPendingRemoval.contains(item)) {
                itemsPendingRemoval.add(item);
                // this will redraw row in "undo" state
                notifyItemChanged(position);
                // let's create, store and post a runnable to remove the item
                Runnable pendingRemovalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        remove(items.indexOf(item));
                    }
                };
                handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
                pendingRunnables.put(item, pendingRemovalRunnable);
            }
        }

        public void remove(int position) {
            JSONObject item = items.get(position);
            if (itemsPendingRemoval.contains(item)) {
                itemsPendingRemoval.remove(item);
            }
            if (items.contains(item)) {
                items.remove(position);
                notifyItemRemoved(position);
            }
        }

        public boolean isPendingRemoval(int position) {
            JSONObject item = items.get(position);
            return itemsPendingRemoval.contains(item);
        }
    }

    /**
     * ViewHolder capable of presenting two states: "normal" and "undo" state.
     */
    static class TestViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, title, orderh, orderno;
        Button undoButton;

        public TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification, parent, false));
            titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            title = (TextView) itemView.findViewById(R.id.n_title);
            orderh = (TextView) itemView.findViewById(R.id.n_order_no_h);
            orderno = (TextView) itemView.findViewById(R.id.n_order_no);

            undoButton = (Button) itemView.findViewById(R.id.undo_button);

            Typeface tf_l= Typeface.createFromAsset(parent.getContext().getAssets(),"fonts/OpenSans-Regular.ttf");
            Typeface tf_b= Typeface.createFromAsset(parent.getContext().getAssets(),"fonts/OpenSans-Bold.ttf");

            title.setTypeface(tf_l);
            orderno.setTypeface(tf_l);
            titleTextView.setTypeface(tf_l);
            orderh.setTypeface(tf_l);
            undoButton.setTypeface(tf_l);
        }

    }

    public static JSONArray remove(final int idx, final JSONArray from) {
        final List<JSONObject> objs = asList(from);
        objs.remove(idx);

        final JSONArray ja = new JSONArray();
        for (final JSONObject obj : objs) {
            ja.put(obj);
        }

        return ja;
    }

    public static List<JSONObject> asList(final JSONArray ja) {
        final int len = ja.length();
        final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
        for (int i = 0; i < len; i++) {
            final JSONObject obj = ja.optJSONObject(i);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }

}