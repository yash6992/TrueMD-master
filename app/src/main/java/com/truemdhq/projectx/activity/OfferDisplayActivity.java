package com.truemdhq.projectx.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.helper.ExceptionHandler;

/**
 * Created by yashvardhansrivastava on 02/07/16.
 */
public class OfferDisplayActivity extends AppCompatActivity {
    TextView title;
    ImageButton back;
    WebView wv;
    SweetAlertDialog progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offer_display);
        title = (TextView) findViewById(R.id.od_title_tv);
        back = (ImageButton) findViewById(R.id.od_backImageButton);
        wv=(WebView) findViewById(R.id.webViewoffer);

        Typeface tf_l= Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");

        title.setTypeface(tf_l);

        String offerUrl = getIntent().getStringExtra("offerurl");
        Log.e("offerUrl: ",""+offerUrl);
        wv = (WebView)findViewById(R.id.webViewoffer);

        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

      // progressBar = ProgressDialog.show(Main.this, "WebView Example", "Loading...");

        progressBar= new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressBar.setTitleText("Loading");
        progressBar.setCancelable(false);
        progressBar.show();

        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("Offer: ", "Getting offer details...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i("Offer: ", "Finished loading URL: " +url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("Offer: ", "Error: " + description);

            }
        });
        wv.loadUrl(offerUrl);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if(wv.canGoBack()) {
            wv.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }



}
