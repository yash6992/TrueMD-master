package com.truemdhq.projectx.activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.adapter.CustomFAQsQuestionAdapter;

public class FAQsQuestionActivity extends AppCompatActivity {

    JSONArray qa;
    ListView lv;
    TextView tv;
    ImageButton ib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs_question);
        lv=(ListView) findViewById(R.id.faq_listView);
        tv=(TextView) findViewById(R.id.faq_title_tv);
        ib=(ImageButton) findViewById(R.id.faq_backImageButton);

        Typeface tf_l= Typeface.createFromAsset(getAssets(),"MarkOffcPro-Medium.ttf");

        tv.setTypeface(tf_l);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        try {
            String qas=getIntent().getStringExtra("FaqsForTopics");
            Log.e("qasErr: ",""+qas);
            qa = new JSONArray(qas);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("qasErr: ",""+e.getMessage());
        }

        lv.setAdapter(new CustomFAQsQuestionAdapter(FAQsQuestionActivity.this, qa));

    }
}
