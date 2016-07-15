package info.truemd.materialdesign.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.truemd.materialdesign.R;
import info.truemd.materialdesign.adapter.CustomSearchAdapter;
import info.truemd.materialdesign.adapter.CustomSubstitutesAdapter;
import info.truemd.materialdesign.helper.SessionManager;
import info.truemd.materialdesign.helper.TrueMDJSONUtils;
import info.truemd.materialdesign.model.JsonParse;
import info.truemd.materialdesign.model.ResponseChat;
import info.truemd.materialdesign.model.SuggestGetSet;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 25/03/16.
 */
public class SubstitutesFragment extends Fragment {

    View rootView;
    ListView lv; ImageView placeholder;
    DilatingDotsProgressBar mDilatingDotsProgressBar;
    String [] NameListA,ManufacturerListA,PriceListA,PackSizeListA,TrueMDCodeListA, UnitPriceListA;
    TextView nameMD, priceMD, manufacturerMD, sizeMD;

    public SubstitutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       rootView = inflater.inflate(R.layout.fragment_substitues, container, false);

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) rootView.findViewById(R.id.progress);
    placeholder=(ImageView) rootView.findViewById(R.id.placeholderImageView);

        placeholder.setVisibility(View.INVISIBLE);

        nameMD= (TextView) rootView.findViewById(R.id.name_md);
        manufacturerMD= (TextView) rootView.findViewById(R.id.manufacturer_md);
        priceMD= (TextView) rootView.findViewById(R.id.price_md);
        sizeMD= (TextView) rootView.findViewById(R.id.size_md);

        Typeface tf_l= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold.ttf");

        nameMD.setTypeface(tf_b);
        manufacturerMD.setTypeface(tf_l);
        priceMD.setTypeface(tf_l);
        sizeMD.setTypeface(tf_l);



        new ExecuteGetAlternatives().execute(MedicineDetailsActivity2.truemdCode);


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class ExecuteGetAlternatives extends AsyncTask<String, Integer, String> {
        ExecuteGetAlternatives asyncObject;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            /**
             * show dialog
             */

            mDilatingDotsProgressBar.showNow();
            nameMD.setText("                                                    ");
            manufacturerMD.setText("                               ");
            priceMD.setText("                         ");
            sizeMD.setText("                       ");

            nameMD.setBackground(getResources().getDrawable(R.drawable.shape10));
            manufacturerMD.setBackground(getResources().getDrawable(R.drawable.shape8));
            priceMD.setBackground(getResources().getDrawable(R.drawable.shape8));
            sizeMD.setBackground(getResources().getDrawable(R.drawable.shape10));


            new SearchActivity().checkInternet(getActivity());

            asyncObject = this;
            new CountDownTimer(20000, 20000) {
                public void onTick(long millisUntilFinished) {
                    // You can monitor the progress here as well by changing the onTick() time
                }
                public void onFinish() {
                    // stop async task if not in progress
                    if (asyncObject.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncObject.cancel(false);
                        // Add any specific task you wish to do as your extended class variable works here as well.



                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Oops..!!")
                                .setContentText("The server is taking too long to respond or there might be an issue with your internet connection.\n Try after some time.")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        getActivity().finish();
//                                            System.exit(0);

                                    }
                                })

                                .show();

                    }
                }
            }.start();

            mDilatingDotsProgressBar.showNow();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... messageText) {
            // TODO Auto-generated method stub
            /**
             * Do network related stuff
             * return string response.
             */
            String result = new String ();int k=0;String line="";

            try {

                Log.e("autotext message: ", messageText[0]);
                if (messageText[0].length() != 0) {

                    String trueMDCode = java.net.URLEncoder.encode(messageText[0], "UTF-8");
                    //String trueMDName = java.net.URLEncoder.encode(messageText[1], "UTF-8");


                    URL js = new URL("http://truemd-carebook.rhcloud.com/api/v2/medicines/"+trueMDCode+"/alternatives?key=" + MainActivity.dev_key);
                    List<String> allKeys = Paper.book("medicineSubstitutes").getAllKeys();
                    if(allKeys.contains(MedicineDetailsActivity2.truemdCode))
                    {
                        line = Paper.book("medicineSubstitutes").read(MedicineDetailsActivity2.truemdCode);
                    }
                    else
                    {
                        URLConnection jc = js.openConnection();
                        //Log.e("Siri resp: ", line);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                        line = reader.readLine();

                    }
//
//                    URLConnection jc = js.openConnection();
//                    Log.e("Siri resp: ", "" + trueMDCode);
//                    //Log.e("Siri resp: ", line);
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
//                    line = reader.readLine();
                    if (line.length() > 5) {

                        Log.e("Siri resp: ", js.toString());
                        Log.e("Siri resp: ", line);
                        JSONObject jsonResponse = new JSONObject(line);
                        JSONArray jsonAlternatives = jsonResponse.getJSONArray("alternatives");
                        Paper.book("medicineSubstitutes").write(MedicineDetailsActivity2.truemdCode, line);

                        NameListA = new String[jsonAlternatives.length()];
                        PriceListA = new String[jsonAlternatives.length()];
                        ManufacturerListA = new String[jsonAlternatives.length()];
                        TrueMDCodeListA = new String[jsonAlternatives.length()];
                        PackSizeListA = new String[jsonAlternatives.length()];
                        UnitPriceListA = new String[jsonAlternatives.length()];

                        for (int i = 0; i < jsonAlternatives.length(); i++) {

                            JSONObject r = jsonAlternatives.getJSONObject(i);

                            boolean toGoInSubstitues = true;

                            Log.e("",""+MedicineDetailsActivity2.medicine.getForm()+" : "+r.optString("form"));

                            if(r.optString("form").equalsIgnoreCase(MedicineDetailsActivity2.medicine.getForm()))
                                toGoInSubstitues = true;
                            else
                                toGoInSubstitues = false;

                            if(toGoInSubstitues){
                                NameListA[k]=r.getString("name");
                                PriceListA[k]=r.getString("price");
                                ManufacturerListA[k]=r.getString("manufacturer");
                                TrueMDCodeListA[k]=r.getString("truemdId");
                                PackSizeListA[k]=r.getString("size");
                                UnitPriceListA[k]=r.getString("unitPrice");
                                k++;
                            }
                        }

                        }
                }
            }catch (Exception e) {
                        e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);

        }



        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            /**
             * update ui thread and remove dialog
             */

            mDilatingDotsProgressBar.hideNow();

            try {

                nameMD.setBackground(getResources().getDrawable(R.drawable.shape9));
                manufacturerMD.setBackground(getResources().getDrawable(R.drawable.shape9));
                priceMD.setBackground(getResources().getDrawable(R.drawable.shape9));
                sizeMD.setBackground(getResources().getDrawable(R.drawable.shape2));


                nameMD.setText(MedicineDetailsActivity2.medicine.getName());
                manufacturerMD.setText(MedicineDetailsActivity2.medicine.getMf());
                priceMD.setText("\u20B9 " + MedicineDetailsActivity2.medicine.getMrp());
                sizeMD.setText(MedicineDetailsActivity2.medicine.getpSize());

                lv=(ListView) rootView.findViewById(R.id.listView);


                if(NameListA.length==0)
                {
                   placeholder.setBackground(getResources().getDrawable(R.drawable.nosubstitute));
                    placeholder.setVisibility(View.VISIBLE);
                }
                else if(NameListA.length==1)
                {
                    placeholder.setBackground(getResources().getDrawable(R.drawable.nosubstitute));
                    placeholder.setVisibility(View.VISIBLE);
                }
                else
                {
                    lv.setAdapter(new CustomSubstitutesAdapter(getActivity(), NameListA, ManufacturerListA, PriceListA, PackSizeListA, TrueMDCodeListA, UnitPriceListA));
                }

                NameListA= new String[1];
                TrueMDCodeListA = new String[1];
                PriceListA= new String[1];
                ManufacturerListA= new String[1];
                PackSizeListA= new String[1];
                UnitPriceListA= new  String[1];
            } catch (Exception e) {
                e.printStackTrace();
            }


            super.onPostExecute(result);
        }
    }

}
