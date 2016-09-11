package info.truemd.android.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import info.truemd.android.R;

/**
 * Created by yashvardhansrivastava on 02/07/16.
 */
public class AboutUsFragment extends Fragment {

    TextView title, mail, goold, visit;
    ImageButton back;
    TextView about;
    ImageButton fb,insta,medium,twitter;
    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        title = (TextView) rootView.findViewById(R.id.od_title_tv);
        mail = (TextView) rootView.findViewById(R.id.mail_us);
        goold = (TextView) rootView.findViewById(R.id.goold);
        visit = (TextView) rootView.findViewById(R.id.visit_us);

        back = (ImageButton) rootView.findViewById(R.id.od_backImageButton);
        about = (TextView) rootView.findViewById(R.id.textabout);
        fb = (ImageButton) rootView.findViewById(R.id.fb);
        insta = (ImageButton) rootView.findViewById(R.id.insta);
        medium = (ImageButton) rootView.findViewById(R.id.medium);
        twitter = (ImageButton) rootView.findViewById(R.id.twitter);

        Typeface tf_l= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");

        title.setTypeface(tf_l);
        mail.setTypeface(tf_l);
        visit.setTypeface(tf_l);
        goold.setTypeface(tf_l);
        about.setTypeface(tf_l);

//        about.setText("Suffering from a chronic medical condition is extremely painful, and managing all your regular medication can be a very tedious task. We at TrueMD understand this problem, and are here to provide a smooth and simple solution to ensure that you never have to worry about your monthly medicines again.\n\nWe take care of everything.\nOrders | Delivery | Reminders | Refills.\n" +
//                "\n" +
//                "The team comprises of BITS Pilani graduates in Science and Engineering who are truly passionate about healthcare and believe that with modern technology and the right attitude, a lot can be achieved.");


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent
                        = newFacebookIntent(getActivity().getPackageManager(), "https://facebook.com/truemd.in");

                startActivity(intent);

            }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent
                        =  new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/truemd.in"));
                startActivity(intent);

            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent
                        =  new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/truemdhq"));
                startActivity(intent);

            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent
                        =  new Intent(Intent.ACTION_VIEW,Uri.parse("https://medium.com/truemd"));

                startActivity(intent);

            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, "care@truemd.in");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hello TrueMD");
                intent.putExtra(Intent.EXTRA_TEXT, "Send a mail");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        about.setText(""+MainActivity.about_us);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }

    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }


}
