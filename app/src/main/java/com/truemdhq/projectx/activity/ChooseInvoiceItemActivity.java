package com.truemdhq.projectx.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.model.Invoice;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yashvardhansrivastava on 15/11/16.
 */
public class ChooseInvoiceItemActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @OnClick(R.id.btn_back)
    public void onClick1(ImageView imageView){
        onBackPressed();
    }

    public static Invoice  invoiceDraft;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_item_projectx);
        ButterKnife.bind(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont();

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            invoiceDraft = objectMapper.readValue(Hawk.get("invoiceDraft").toString(), Invoice.class);


        }
        catch (Exception e){
            Log.e("DateError: ",""+e.getMessage());
        }

    }

    @Override
    public void onBackPressed(){
        Intent newIntent = new Intent(ChooseInvoiceItemActivity.this, InvoiceCreateActivity.class);
        startActivityForResult(newIntent, 4);
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InvoiceAddItemFragment(), "ADD ITEM");
        adapter.addFragment(new InvoiceChooseItemFragment(), " CHOOSE ITEM");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void checkValidAndProceed(){

    }
    private void changeTabsFont() {
        Typeface tf_l= Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(tf_l);
                }
            }
        }
    }
}
