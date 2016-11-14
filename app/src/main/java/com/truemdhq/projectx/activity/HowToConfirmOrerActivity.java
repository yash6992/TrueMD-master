package com.truemdhq.projectx.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.helper.SessionManager;

/**
 * Created by yashvardhansrivastava on 20/04/16.
 */
public class HowToConfirmOrerActivity extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4; Button tv5; ArrayList<Uri> images; ImageButton b; DilatingDotsProgressBar howtoProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_confirm_order);


       images = getIntent().getParcelableArrayListExtra("images");
        tv1 = (TextView) findViewById(R.id.tv1);

        tv5 = (Button) findViewById(R.id.txt_2);
        b=(ImageButton) findViewById(R.id.backImageButtonhow);

        Typeface tf_l = Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");
        Typeface tf_pacifico = Typeface.createFromAsset(getAssets(), "MarkOffcPro-Bold.ttf");

        tv1.setTypeface(tf_l);

        tv5.setTypeface(tf_l);
        howtoProgress = (DilatingDotsProgressBar) findViewById(R.id.howto_progress);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                howtoProgress.showNow();

                option2Selected(13, -1, images);
                Log.e("OrderChoice", "2");

//                if (mBottomSheetDialog3.isShowing()) {
//                    mBottomSheetDialog3.dismiss();
//                    //Log.e("OrderChoice","2: dialog closed");
//                }


            }
        });
    }




    public void option2Selected(int requestCode, int resuleCode, ArrayList<Uri> image_uris) // i want to specify medicines by phone call
    {

//        if(mBottomSheetDialog3.isShowing()) {
//            mBottomSheetDialog3.dismiss();
//            Log.e("OrderChoice","2: dialog closed");
//        }
        if (requestCode == 13 && resuleCode == Activity.RESULT_OK ) {

            for (Uri u: image_uris)
            {
                Log.e("Image URIs 2", "" + u.toString());
            }

            //do something

            for (int i=0;i<image_uris.size();i++)
            {
                image_uris.set(i, compressImageFromUri(image_uris.get(i)));
            }

//            Intent toMark = new Intent(OrderMedicineActivity.this,MarkImageActivity.class);
//            toMark.putExtra("ImageUris",image_uris);
//            startActivity(toMark);

            Intent toConfirm = new Intent(HowToConfirmOrerActivity.this, ConfirmOrderActivity.class);
            toConfirm.putExtra("imageList", image_uris);
           // howtoProgress.hideNow();
            startActivity(toConfirm);
        }
    }
    public Uri compressImageFromUri( Uri absPathUri)
    {
        if(absPathUri!=null)
        {
            Bitmap myImg = BitmapFactory.decodeFile(absPathUri.getPath());
            Matrix matrix = new Matrix();
            matrix.postRotate(0);
            Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(),myImg.getHeight(),
                    matrix, true);

            Bitmap converted_resized = getResizedBitmap(rotated, 786);

            return absPathUri;
        }
        else
            return absPathUri;

    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }





    @Override
    public void onBackPressed(){


        finish();
        startActivity(new Intent(HowToConfirmOrerActivity.this, OrderMedicineActivity.class));

    }




}
