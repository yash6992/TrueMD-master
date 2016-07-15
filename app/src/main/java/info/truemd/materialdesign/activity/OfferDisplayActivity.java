package info.truemd.materialdesign.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import info.truemd.materialdesign.R;

/**
 * Created by yashvardhansrivastava on 02/07/16.
 */
public class OfferDisplayActivity extends AppCompatActivity {
    TextView title;
    ImageButton back;
    WebView wv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_display);
        title = (TextView) findViewById(R.id.od_title_tv);
        back = (ImageButton) findViewById(R.id.od_backImageButton);
        wv=(WebView) findViewById(R.id.webViewoffer);

        Typeface tf_l= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        title.setTypeface(tf_l);

        String offerUrl = getIntent().getStringExtra("offerurl");
        Log.e("offerUrl: ",""+offerUrl);
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
