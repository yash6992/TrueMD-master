package com.truemdhq.projectx.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import com.truemdhq.projectx.R;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 14/06/16.
 */
public class Intro extends AppIntro {

    Context c;

    @Override
    public void init(Bundle savedInstanceState) {

        c= getApplicationContext();

        addSlide(new IntroAFragment());
        addSlide(new IntroBFragment());
        addSlide(new IntroCFragment());
        addSlide(new IntroDFragment());
        addSlide(new IntroEFragment());

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        //addSlide(AppIntroFragment.newInstance("Welcome to ProjectX", "Your Business Money, Managed",R.mipmap.ic_launcher,c.getResources().getColor(R.color.windowBackground) ));

// Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        //addSlide(AppIntroFragment.newInstance("Welcome to ProjectX", "Your Business Money, Managed",R.drawable.sunrise,c.getResources().getColor(R.color.windowBackground) ));

// Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        //addSlide(AppIntroFragment.newInstance("Welcome to ProjectX", "Your Business Money, Managed",R.drawable.sunrise,c.getResources().getColor(R.color.windowBackground) ));
        setSeparatorColor(Color.parseColor("#FFFFFF"));


    }

    private void loadMainActivity(){
        Paper.book("introduction").write("intro","1");
        finish();
        Intent i = new Intent(this,PreLoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
        //Toast.makeText(getApplicationContext(),getString(R.string.skip),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}