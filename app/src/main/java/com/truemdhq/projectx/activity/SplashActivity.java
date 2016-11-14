package com.truemdhq.projectx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 26/05/16.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("introductions1:: ", ""+ Paper.book("introduction").read("intro"));

        Intent intent = new Intent(this, MainActivity.class);
        Log.e("introductions2:: ", ""+Paper.book("introduction").read("intro"));

        startActivity(intent);
        Log.e("introductions3:: ", ""+Paper.book("introduction").read("intro"));

        finish();
    }

}
