package info.truemd.android.activity;

import android.graphics.Typeface;
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

    TextView title;
    ImageButton back;
    TextView about;
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
        back = (ImageButton) rootView.findViewById(R.id.od_backImageButton);
        about = (TextView) rootView.findViewById(R.id.textabout);
        Typeface tf_l= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");

        title.setTypeface(tf_l);about.setTypeface(tf_l);

//        about.setText("Suffering from a chronic medical condition is extremely painful, and managing all your regular medication can be a very tedious task. We at TrueMD understand this problem, and are here to provide a smooth and simple solution to ensure that you never have to worry about your monthly medicines again.\n\nWe take care of everything.\nOrders | Delivery | Reminders | Refills.\n" +
//                "\n" +
//                "The team comprises of BITS Pilani graduates in Science and Engineering who are truly passionate about healthcare and believe that with modern technology and the right attitude, a lot can be achieved.");


        about.setText(""+MainActivity.about_us);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }


}
