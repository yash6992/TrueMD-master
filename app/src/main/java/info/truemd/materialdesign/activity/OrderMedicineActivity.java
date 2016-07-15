package info.truemd.materialdesign.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.ArrayList;

import info.truemd.materialdesign.R;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 20/04/16.
 */
public class OrderMedicineActivity extends AppCompatActivity {

    ImageView idealPres;
    ImageButton backImageButtonOM, info, help; Dialog mBottomSheetDialog1, mBottomSheetDialog3;
    TextView titleOM, t1, t2, uploadPrescription, refillOrder;
    DilatingDotsProgressBar orderProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_medicine);

        Typeface tf_l=Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface tf_pacifico=Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");

        idealPres=(ImageView)findViewById(R.id.idealPrescriptionImageViewOM);
        backImageButtonOM=(ImageButton) findViewById(R.id.backImageButtonOM);
        info=(ImageButton) findViewById(R.id.infoimageButton);
        titleOM=(TextView) findViewById(R.id.titleOM);
        t1=(TextView) findViewById(R.id.textView4OM);
        t2=(TextView) findViewById(R.id.textView5OM);
        uploadPrescription=(TextView) findViewById(R.id.uploadPrescriptionButtonOM);
        refillOrder=(TextView) findViewById(R.id.refillButtonOM);
        orderProgress = (DilatingDotsProgressBar) findViewById(R.id.order_progress);

        help = (ImageButton) findViewById(R.id.helpimageButton);
        mBottomSheetDialog1= new Dialog(OrderMedicineActivity.this,
                R.style.MaterialDialogSheet);

        titleOM.setTypeface(tf_pacifico);
        t1.setTypeface(tf_l);t2.setTypeface(tf_l);uploadPrescription.setTypeface(tf_l);refillOrder.setTypeface(tf_l);

        uploadPrescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String check =Paper.book("introduction").read("preshelp","0");

                if(check.equalsIgnoreCase("0"))
                    openBottomSheetForPrescription();
                else
                {
                    moveToChoosePrescription();
                }

            }
        });
        backImageButtonOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openBottomSheetForPharmacyRules();
            }
        });
        refillOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onrefillPressed();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetForPrescriptionHelp();
            }
        });



    }
    public void onrefillPressed(){

        Intent toPreviousOrderDetails = new Intent(OrderMedicineActivity.this, OrderDetailsActivity.class);
        toPreviousOrderDetails.putExtra("to",1);
        startActivity(toPreviousOrderDetails);
    }

    void moveToChoosePrescription(){

        if(mBottomSheetDialog1.isShowing())
        mBottomSheetDialog1.dismiss();
        orderProgress.showNow();

        Paper.book("introduction").write("preshelp","1");

        Config config = new Config();
        config.setSelectionMin(1);
        config.setSelectionLimit(10);
        config.setToolbarTitleRes(R.string.tooltextImages);
        config.setSelectedBottomHeight(R.dimen.selected_image_height);
        config.setCameraHeight(R.dimen.camera_height);

        ImagePickerActivity.setConfig(config);
        Intent intent = new Intent(OrderMedicineActivity.this, ImagePickerActivity.class);

        orderProgress.hideNow();
        startActivityForResult(intent, 13);
    }


    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);
        Log.e("onActivityresult","bs opened: " +requestCode+":"+resuleCode);

        if(resuleCode==-1)
        {
            Intent intent1 = new Intent(OrderMedicineActivity.this, HowToConfirmOrerActivity.class);
            intent1.putExtra("images", intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS));
            startActivity(intent1);
        }
        else
        {

        }
    }


    public void openBottomSheetForPrescription( ) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_ideal_prescription, null);

        TextView txth = (TextView)view.findViewById( R.id.tv2);
        //phone_number = (EditText)view.findViewById( R.id.input_phone_number_password);
        Button submit = (Button) view.findViewById( R.id.btn_next);

        txth.setTypeface(tf_r);
        //phone_number.setTypeface(tf_r);
        submit.setTypeface(tf_r);

        //txth.setText(heading);



        mBottomSheetDialog1= new Dialog(OrderMedicineActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog1.setContentView (view);
        mBottomSheetDialog1.setCancelable(true);
        mBottomSheetDialog1.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog1.getWindow ().setGravity(Gravity.CENTER);
        mBottomSheetDialog1.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToChoosePrescription();
            }
        });

    }

    public void openBottomSheetForPrescriptionHelp( ) {

        Paper.book("introduction").write("preshelp","0");

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_ideal_prescription, null);

        TextView txth = (TextView)view.findViewById( R.id.tv2);
        //phone_number = (EditText)view.findViewById( R.id.input_phone_number_password);
        Button submit = (Button) view.findViewById( R.id.btn_next);
        submit.setVisibility(View.GONE);

        txth.setTypeface(tf_r);
        //phone_number.setTypeface(tf_r);
        submit.setTypeface(tf_r);

        //txth.setText(heading);



        mBottomSheetDialog1= new Dialog(OrderMedicineActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog1.setContentView (view);
        mBottomSheetDialog1.setCancelable(true);
        mBottomSheetDialog1.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog1.getWindow ().setGravity(Gravity.CENTER);
        mBottomSheetDialog1.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToChoosePrescription();
            }
        });

    }

    public void openBottomSheetForPharmacyRules() {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pharmacy_rules, null);

        TextView txth = (TextView)view.findViewById( R.id.tv2);

        WebView wv = (WebView) view.findViewById(R.id.webViewwhy);
        wv.loadUrl(""+MainActivity.whyPrescriptionUrl);

        txth.setTypeface(tf_r);

        Dialog mBottomSheetDialog = new Dialog (OrderMedicineActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.CENTER);
        mBottomSheetDialog.show();

    }



    @Override
    public void onBackPressed() {

        finish();

        MainActivity.fromMedicineDetailsChat=false;
        MainActivity.fromHomeToChat=false;

        Intent intent_main = new Intent(getApplicationContext(),MainActivity.class);
        intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplicationContext().startActivity(intent_main);


        super.onBackPressed();

    }

    }
