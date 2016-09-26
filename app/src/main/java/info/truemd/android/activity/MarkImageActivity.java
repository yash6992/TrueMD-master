package info.truemd.android.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;

import info.truemd.android.R;
import info.truemd.android.helper.ExceptionHandler;

/**
 * Created by yashvardhansrivastava on 16/04/16.
 */
public class MarkImageActivity extends AppCompatActivity {

    ImageView markImage0,markImage1,markImage2,markImage3,markImage4,markImage5,markImage6,markImage7,markImage8,markImage9,markImageMain;
    EditText commentsMark;
    int focus = 0;
    ImageButton fullScreenIB; TextView rotateIB, clearIB, right, left; ImageButton backMarkIB, submitMarkIB;
    ArrayList<Uri> imageEditList;
    ArrayList<ImageView> markImageList; int angle=90;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_mark_image);

        Intent getImagesFromMain = getIntent();
        final ArrayList<Uri> image_uris = getImagesFromMain.getParcelableArrayListExtra("ImageUris");

        imageEditList = image_uris;

        markImage0=(ImageView)findViewById(R.id.markImage0);
        markImage1=(ImageView)findViewById(R.id.markImage1);
        markImage2=(ImageView)findViewById(R.id.markImage2);
        markImage3=(ImageView)findViewById(R.id.markImage3);
        markImage4=(ImageView)findViewById(R.id.markImage4);
        markImage5=(ImageView)findViewById(R.id.markImage5);
        markImage6=(ImageView)findViewById(R.id.markImage6);
        markImage7=(ImageView)findViewById(R.id.markImage7);
        markImage8=(ImageView)findViewById(R.id.markImage8);
        markImage9=(ImageView)findViewById(R.id.markImage9);
        markImageMain=(ImageView)findViewById(R.id.markImageMain);

        markImageList= new ArrayList<ImageView>();
        markImageList.add(markImage0);
        markImageList.add(markImage1);
        markImageList.add(markImage2);
        markImageList.add(markImage3);
        markImageList.add(markImage4);
        markImageList.add(markImage5);
        markImageList.add(markImage6);
        markImageList.add(markImage7);
        markImageList.add(markImage8);
        markImageList.add(markImage9);

        int i=0;
        for (i=0;i<image_uris.size();i++)
        {
            setImageFromUri(markImageList.get(i),image_uris.get(i));
        }
        for (;i<10;i++)
        {
            markImageList.get(i).setVisibility(View.GONE);
        }
        giveFocusToImage(0);

        setMarkImageMain(markImageMain, image_uris.get(0));

        rotateIB=(TextView)findViewById(R.id.rotateIB);
        fullScreenIB=(ImageButton)findViewById(R.id.fullScreenIB);
        clearIB=(TextView)findViewById(R.id.clearIB);
        right=(TextView)findViewById(R.id.right);left=(TextView)findViewById(R.id.left);
        backMarkIB=(ImageButton)findViewById(R.id.backImageButtonMark);
        submitMarkIB=(ImageButton)findViewById(R.id.CheckImageButtonMark);

        Typeface tf_l= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");


        //commentsMark=(EditText)findViewById(R.id.commentETMark);
        //commentsMark.setTypeface(tf_l);


        View.OnClickListener clicklistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";

                switch (v.getId()) {
                    case R.id.markImage0:
                        focus=0;
                        giveFocusToImage(0);
                        setMarkImageMain(markImageMain, imageEditList.get(0));
                        break;
                    case R.id.markImage1:
                        focus=1;
                        giveFocusToImage(1);
                        setMarkImageMain(markImageMain, imageEditList.get(1));
                        break;
                    case R.id.markImage2:
                        focus=2;
                        giveFocusToImage(2);
                        setMarkImageMain(markImageMain, imageEditList.get(2));
                        break;
                    case R.id.markImage3:
                        focus=3;
                        giveFocusToImage(3);
                        setMarkImageMain(markImageMain, imageEditList.get(3));
                        break;
                    case R.id.markImage4:
                        focus=4;
                        giveFocusToImage(4);
                        setMarkImageMain(markImageMain, imageEditList.get(4));
                        break;
                    case R.id.markImage5:
                        focus=5;
                        giveFocusToImage(5);
                        setMarkImageMain(markImageMain, imageEditList.get(5));
                        break;
                    case R.id.markImage6:
                        focus=6;
                        giveFocusToImage(6);
                        setMarkImageMain(markImageMain, imageEditList.get(6));
                        break;
                    case R.id.markImage7:
                        focus=7;
                        giveFocusToImage(7);
                        setMarkImageMain(markImageMain, imageEditList.get(7));
                        break;
                    case R.id.markImage8:
                        focus=8;
                        giveFocusToImage(8);
                        setMarkImageMain(markImageMain, imageEditList.get(8));
                        break;
                    case R.id.markImage9:
                        focus=9;
                        giveFocusToImage(9);
                        setMarkImageMain(markImageMain, imageEditList.get(9));
                        break;
                    case R.id.right:
                        if(focus>=0&&focus<=image_uris.size()-2){
                        focus=focus+1;
                        giveFocusToImage(focus);
                        setMarkImageMain(markImageMain, imageEditList.get(focus));
                        }
                        break;
                    case R.id.left:
                        if(focus>=1&&focus<=image_uris.size()-1){
                            focus=focus-1;
                        giveFocusToImage(focus);
                        setMarkImageMain(markImageMain, imageEditList.get(focus));
                        }
                        break;

                    case R.id.clearIB:
                        setMarkImageMain(markImageMain,imageEditList.get(focus));
                        break;
                    case R.id.rotateIB:
                        rotateMarkImageMain(imageEditList.get(focus));
                        //imageEditList.set(focus,markImageMain.getDrawable().)
                        break;
                    case R.id.fullScreenIB:
                        break;
                    case R.id.backImageButtonMark:
                        onBackPressed();
                        break;
                    case R.id.CheckImageButtonMark:
                        Intent toConfirm = new Intent(MarkImageActivity.this, ConfirmOrderActivity.class);
                        toConfirm.putExtra("imageList", imageEditList);
                        startActivity(toConfirm);
                        break;
                    default:
                        break;


                }


            }
        };

        markImage0.setOnClickListener(clicklistener);
        markImage1.setOnClickListener(clicklistener);
        markImage2.setOnClickListener(clicklistener);
        markImage3.setOnClickListener(clicklistener);
        markImage4.setOnClickListener(clicklistener);
        markImage5.setOnClickListener(clicklistener);
        markImage6.setOnClickListener(clicklistener);
        markImage7.setOnClickListener(clicklistener);
        markImage8.setOnClickListener(clicklistener);
        markImage9.setOnClickListener(clicklistener);
        rotateIB.setOnClickListener(clicklistener);
        clearIB.setOnClickListener(clicklistener);
        left.setOnClickListener(clicklistener);
        right.setOnClickListener(clicklistener);
        backMarkIB.setOnClickListener(clicklistener);
        submitMarkIB.setOnClickListener(clicklistener);







    }

    public void setMarkImageMain(ImageView imgView, Uri absPathUri)
    {
        if(absPathUri!=null)
        {
            Bitmap myImg = BitmapFactory.decodeFile(absPathUri.getPath());
            Matrix matrix = new Matrix();
            matrix.postRotate(0);
            Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(),myImg.getHeight(),
                    matrix, true);
           imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            Bitmap converted_resized = getResizedBitmap(rotated, 786);

            imgView.setImageBitmap(converted_resized);
        }
    }
    public void rotateMarkImageMain(Uri absPathUri)
    {
            Bitmap myImg = BitmapFactory.decodeFile(absPathUri.getPath());
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
        angle+=90;
            Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(),myImg.getHeight(),
                    matrix, true);
            markImageMain.setScaleType(ImageView.ScaleType.FIT_XY);
        Bitmap converted_resized = getResizedBitmap(rotated, 786);
            markImageMain.setImageBitmap(converted_resized);

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

    public void setImageFromUri(ImageView imgView, Uri absPathUri)
    {
        if(absPathUri!=null)
        {
            Bitmap myImg = BitmapFactory.decodeFile(absPathUri.getPath());
            Matrix matrix = new Matrix();
            matrix.postRotate(0);
            Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(),myImg.getHeight(),
                    matrix, true);
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            Bitmap converted_resized = getResizedBitmap(rotated, 786);

            imgView.setImageBitmap(converted_resized);
        }
    }
    public void giveFocusToImage(int focuslocal)
    {
        for(int k=0;k<markImageList.size();k++)
        {
            if(k==focuslocal)
            {
                markImageList.get(k).setBackground(getResources().getDrawable(R.drawable.shape13));
            }
            else
            {
                markImageList.get(k).setBackground(getResources().getDrawable(R.drawable.shape));
            }
        }
    }
    @Override
    public void onBackPressed(){
        finish();
        Intent back=new Intent(MarkImageActivity.this, OrderMedicineActivity.class);
        startActivity(back);
    }


}
