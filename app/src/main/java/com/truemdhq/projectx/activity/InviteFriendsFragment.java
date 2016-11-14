package com.truemdhq.projectx.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.truemdhq.projectx.R;

/**
 * Created by yashvardhansrivastava on 02/07/16.
 */
public class InviteFriendsFragment extends Fragment {

    TextView title, rules; Button share;
    ImageButton back; String downloadurl="http://bit.ly/29myyux";
    public InviteFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invite_friends, container, false);

        title = (TextView) rootView.findViewById(R.id.od_title_tv);
        back = (ImageButton) rootView.findViewById(R.id.od_backImageButton);
        share = (Button) rootView.findViewById(R.id.btn_share);
        rules =(TextView) rootView.findViewById(R.id.textView_invite_rules);

        Typeface tf_l= Typeface.createFromAsset(getActivity().getAssets(), "MarkOffcPro-Medium.ttf");

        title.setTypeface(tf_l); rules.setTypeface(tf_l);

        //rules.setText("The rules of the in app referral system goes here.\nNow here is some random text for display purposes.!");
        rules.setText(MainActivity.referralMsg);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String shareBody = "Download the ProjectX app from Playstore. "+downloadurl+"\nWe both will get a Rs. 50 coupon. \nJust use the coupon code: "+MainActivity.userName_mobile+" on your first order.";
                String shareBody = MainActivity.inviteMsg;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via "));
            }
        });

        return rootView;
    }
}
