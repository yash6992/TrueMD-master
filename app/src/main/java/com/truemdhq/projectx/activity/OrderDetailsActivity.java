package com.truemdhq.projectx.activity;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.helper.ExceptionHandler;
import com.truemdhq.projectx.helper.SimpleGestureFilter;
import com.truemdhq.projectx.helper.SimpleGestureFilter.SimpleGestureListener;
import com.truemdhq.projectx.model.Medicine;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 19/01/16.
 */
public class OrderDetailsActivity extends AppCompatActivity implements SimpleGestureListener  {

    ImageButton backImage; TextView od_title;

    ToggleSwitch toggleSwitch;

    private SimpleGestureFilter detector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_details);
        Paper.book("nav").write("selected","2");
        // Detect touched area
        detector = new SimpleGestureFilter(this,this);

        backImage = (ImageButton) findViewById(R.id.od_backImageButton);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        toggleSwitch = (ToggleSwitch) findViewById(R.id.od_toggle_switches_container);
        //position_toggle = toggleSwitch.getCheckedTogglePosition();
        //toggleSwitch.setCheckedTogglePosition(0);
        ArrayList<String> labels = new ArrayList<>();
        labels.add("UPCOMING");
        labels.add("PREVIOUS");
        toggleSwitch.setLabels(labels);

        toggleSwitch.setCheckedTogglePosition(0);

        od_title= (TextView) findViewById(R.id.od_title_tv);

        Typeface tf_l= Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");

        od_title.setTypeface(tf_l);


        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {

            @Override
            public void onToggleSwitchChangeListener(int position) {
                // Write your code ...

                //Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();

                displayView(position);
            }
        });


        new MainActivity().checkInternet(this);

        if(getIntent().getIntExtra("to",0)==1)
        { displayView(1);
        toggleSwitch.setCheckedTogglePosition(1);}
        else
        { displayView(0);
        toggleSwitch.setCheckedTogglePosition(0);}


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_search) {
            Intent intent_main_search = new Intent(this, SearchActivity.class);
            startActivityForResult(intent_main_search, 0);
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {

        finish();

        Intent intent_main = new Intent(getApplicationContext(),MainActivity.class);
        intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplicationContext().startActivity(intent_main);


        super.onBackPressed();

    }

    public void displayView(int position) {
        Fragment fragment = null;
        String title="ProjectX";// = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new UpcomingOrderFragment();
                title = "Upcoming";
                break;
            case 1:
                fragment = new PreviousOrderFragment();
                title = "Previous";
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.od_container_body_order_details, fragment);
            //fragmentTransaction.commit();
            fragmentTransaction.commitAllowingStateLoss();

            // set the toolbar title
            //getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                if(toggleSwitch.getCheckedTogglePosition()==1)
                {
                    displayView(0);
                    toggleSwitch.setCheckedTogglePosition(0);
                }
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                if(toggleSwitch.getCheckedTogglePosition()==0)
                {
                    displayView(1);
                    toggleSwitch.setCheckedTogglePosition(1);
                }
                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

        }
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {
        // Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT);
    }






}
