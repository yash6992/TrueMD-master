package info.truemd.materialdesign.activity;

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
import android.widget.ImageButton;
import android.widget.Toast;


import java.util.ArrayList;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import info.truemd.materialdesign.R;
import info.truemd.materialdesign.helper.SimpleGestureFilter;
import info.truemd.materialdesign.helper.SimpleGestureFilter.SimpleGestureListener;
import info.truemd.materialdesign.model.Medicine;

/**
 * Created by yashvardhansrivastava on 19/01/16.
 */
public class MedicineDetailsActivity2 extends AppCompatActivity implements SimpleGestureListener  {


    public static  Medicine medicine;

    static  String name, truemdCode;

    static String loginURL ;

    ImageButton backImage;

    ToggleSwitch toggleSwitch;

    private SimpleGestureFilter detector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details_3);

        // Detect touched area
        detector = new SimpleGestureFilter(this,this);



        Intent getIntentforMedicineDetails = getIntent() ;
        String getMedicineRequest = getIntentforMedicineDetails.getStringExtra("truemdCode");

        truemdCode = getMedicineRequest.substring(0, getMedicineRequest.indexOf(':'));
        Log.d("Medicine truemdCode",truemdCode);


        name = getMedicineRequest.substring(getMedicineRequest.indexOf(':') + 1);

        backImage = (ImageButton) findViewById(R.id.backImageButton);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        toggleSwitch = (ToggleSwitch) findViewById(R.id.toggle_switches_container);
        //position_toggle = toggleSwitch.getCheckedTogglePosition();
        //toggleSwitch.setCheckedTogglePosition(0);
        ArrayList<String> labels = new ArrayList<>();
        labels.add("DETAILS");
        labels.add("SUBSTITUTES");
        toggleSwitch.setLabels(labels);

        toggleSwitch.setCheckedTogglePosition(0);

        Typeface tf_l= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");


        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {

            @Override
            public void onToggleSwitchChangeListener(int position) {
                // Write your code ...

                Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();

                displayView(position);
            }
        });


        loginURL = "http://truemd-carebook.rhcloud.com/api/v2/medicines/"+truemdCode+"?key="+MainActivity.dev_key+"&medline=true&medline_criteria="+"why,precaution,storage,diet";



        new SearchActivity().checkInternet(this);

        displayView(0);


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

        MainActivity.fromMedicineDetailsChat=false;
//
            Intent intent_main = new Intent(getApplicationContext(),MainActivity.class);
            intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getApplicationContext().startActivity(intent_main);


            super.onBackPressed();

    }

    public void displayView(int position) {
        Fragment fragment = null;
        String title="TrueMD";// = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new DetailsFragment();
                title = "Details";
                break;
            case 1:
                fragment = new SubstitutesFragment();
                title = "Substitutes";
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body_medicine_details, fragment);
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
