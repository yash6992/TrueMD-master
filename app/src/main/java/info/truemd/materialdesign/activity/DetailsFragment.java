package info.truemd.materialdesign.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.truemd.materialdesign.R;
import info.truemd.materialdesign.helper.CustomHtmlTextParsing;
import info.truemd.materialdesign.helper.TrueMDJSONUtils;
import info.truemd.materialdesign.model.Generic;
import info.truemd.materialdesign.model.Medicine;
import info.truemd.materialdesign.model.Medline;
import io.paperdb.Paper;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by yashvardhansrivastava on 25/03/16.
 */
public class DetailsFragment extends Fragment{

    SweetAlertDialog pSweetDialog;
    Medicine med;
    DilatingDotsProgressBar mDilatingDotsProgressBar;

    String genericMedString;
    // temporary string to show the parsed response
    private String jsonResponse, sideEffects, precautions,how, why, storage, diet, faqs, medName;
    TextView nameMD, priceMD, manufacturerMD, scheduleMD, constituentsMD, sizeMD, pregh,lacth,conmd;
    FancyButton sideEffectsMD, precautionsMD, allergiesMD, reactionsMD,b1MD,b2MD;
    ImageButton pregnancyMD, lactationMD;

    private static final String TAG = "MedicineDetails"; String preg,lact;
    Typeface tf_l;//= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
    Typeface tf_b;//= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold.ttf");
    View rootView;


    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_details, container, false);

        medName=MedicineDetailsActivity2.name;
        med= new Medicine();
        sideEffects=""; precautions=""; why="";how="";faqs = ""; storage=""; diet=""; genericMedString="";
        pSweetDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pSweetDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pSweetDialog.setTitleText("Loading");
        pSweetDialog.setCancelable(false);
        tf_l= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        tf_b= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold.ttf");

         preg="Loading ...";lact="Loading ...";


        mDilatingDotsProgressBar = (DilatingDotsProgressBar) rootView.findViewById(R.id.progress_1);

        final FloatingActionMenu menu2 = (FloatingActionMenu) rootView.findViewById(R.id.menu_down);

       final FloatingActionButton menuChat = (FloatingActionButton) rootView.findViewById(R.id.menu_chat);
        final FloatingActionButton menuRemind = (FloatingActionButton) rootView.findViewById(R.id.menu_remind);
        //fab32 = (FloatingActionButton) rootView.findViewById(R.id.fab32);



        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";

                switch (v.getId()) {
                    case R.id.menu_chat:
                        MainActivity.fromMedicineDetailsChat = true;
                        MainActivity.chatInitializerTrueMDCode = MedicineDetailsActivity2.truemdCode;
                        Intent toMainTrueMDSiriFragment = new Intent(getActivity(), MainActivity.class);
                        startActivity(toMainTrueMDSiriFragment);
                        break;
                    case R.id.menu_remind:
                        Intent toOrder = new Intent(getActivity(), OrderMedicineActivity.class);
                        startActivity(toOrder);
                        break;
                    case R.id.lactationImageButton:
                        openBottomSheetSideEffects("Lactation Safety",lact, medName, "National Library of Medicine® (NLM)"  );

                        break;
                    case R.id.pregnancyImageButton:
//                        ToolTip toolTip = new ToolTip()
//                                .withText(preg)
//                                .withColor(getResources().getColor(R.color.window100))
//                                .withShadow()
//                                .withAnimationType(ToolTip.AnimationType.FROM_MASTER_VIEW);
//                        toolTip.withTypeface(tf_l);
//
//                        ToolTipView myToolTipView = toolTipRelativeLayout.showToolTipForView(toolTip, rootView.findViewById(R.id.preg_ll));
//                        //myToolTipView.setOnToolTipViewClickedListener((MedicineDetailsActivity2)getActivity());

                        openBottomSheetSideEffects("Pregnancy Safety", preg, medName, "National Library of Medicine® (NLM)"  );
                        break;
                    case R.id.btn_side_effects:
                        openBottomSheetSideEffects("Side effects", sideEffects, medName,"Source: FDA adverse event database via openFDA.gov"  );
                        break;
                    case R.id.btn_precautions:
                        openBottomSheetSideEffects("Precautions", precautions, medName, "National Library of Medicine® (NLM)" );
                        break;
                    case R.id.btn_allergies:
                        openBottomSheetSideEffects("Uses of", why , medName, "National Library of Medicine® (NLM)" );
                        break;
                    case R.id.btn_blah_blah_1:
                        openBottomSheetSideEffects("Diet control", diet, medName, "National Library of Medicine® (NLM)" );
                        break;
                    case R.id.btn_blah_blah_2:
                        openBottomSheetSideEffects("Storage", storage, medName, "National Library of Medicine® (NLM)" );
                        break;
                    case R.id.btn_reactions:
                        openBottomSheetSideEffects("How?", how, medName ,"National Library of Medicine® (NLM)");
                        break;
                    /* case R.id.fab32:
                        text = fab32.getLabelText();*/

                }


            }
        };
        menu2.setClosedOnTouchOutside(true);
//
        menu2.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                String text = "";
                if (opened) {
                    text = "Menu opened";
                } else {
                    text = "Menu closed";
                }
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });



        menuChat.setOnClickListener(clickListener);
        menuRemind.setOnClickListener(clickListener);
        //fab32.setOnClickListener(clickListener);




        sideEffectsMD = (FancyButton)rootView.findViewById(R.id.btn_side_effects);
        precautionsMD = (FancyButton)rootView.findViewById(R.id.btn_precautions);
        allergiesMD = (FancyButton)rootView.findViewById(R.id.btn_allergies);
        reactionsMD = (FancyButton)rootView.findViewById(R.id.btn_reactions);
        b1MD = (FancyButton)rootView.findViewById(R.id.btn_blah_blah_1);
        b2MD = (FancyButton)rootView.findViewById(R.id.btn_blah_blah_2);
        pregnancyMD = (ImageButton)rootView.findViewById(R.id.pregnancyImageButton);
        lactationMD = (ImageButton)rootView.findViewById(R.id.lactationImageButton);
        lacth = (TextView) rootView.findViewById(R.id.lact_h);
        pregh = (TextView) rootView.findViewById(R.id.preg_h);
        conmd = (TextView) rootView.findViewById(R.id.con_md);

        nameMD= (TextView) rootView.findViewById(R.id.name_md);
        manufacturerMD= (TextView) rootView.findViewById(R.id.manufacturer_md);
        priceMD= (TextView) rootView.findViewById(R.id.price_md);
        sizeMD= (TextView) rootView.findViewById(R.id.size_md);
        constituentsMD= (TextView) rootView.findViewById(R.id.constituents_md);
        scheduleMD= (TextView) rootView.findViewById(R.id.schedule_md);



        nameMD.setTypeface(tf_b);
        manufacturerMD.setTypeface(tf_l);
        priceMD.setTypeface(tf_l);
        sizeMD.setTypeface(tf_l);
        constituentsMD.setTypeface(tf_l);
        scheduleMD.setTypeface(tf_l);
        lacth.setTypeface(tf_l);
        pregh.setTypeface(tf_l);
        conmd.setTypeface(tf_l);

        sideEffectsMD.setCustomTextFont("OpenSans-Regular.ttf");
        precautionsMD.setCustomTextFont("OpenSans-Regular.ttf");
        allergiesMD.setCustomTextFont("OpenSans-Regular.ttf");
        reactionsMD.setCustomTextFont("OpenSans-Regular.ttf");
        b1MD.setCustomTextFont("OpenSans-Regular.ttf");
        b2MD.setCustomTextFont("OpenSans-Regular.ttf");

        sideEffectsMD.setOnClickListener(clickListener);
        precautionsMD.setOnClickListener(clickListener);
        allergiesMD.setOnClickListener(clickListener);
        reactionsMD.setOnClickListener(clickListener);
        b1MD.setOnClickListener(clickListener);
        b2MD.setOnClickListener(clickListener);
        pregnancyMD.setOnClickListener(clickListener);
        lactationMD.setOnClickListener(clickListener);


        new MedicineNetworkOperation().execute(MedicineDetailsActivity2.truemdCode);


        return rootView;
    }




    private void showpDialog() {
        mDilatingDotsProgressBar.showNow();
    }

    private void hidepDialog() {
        mDilatingDotsProgressBar.hideNow();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void openBottomSheetSideEffects ( String heading, String content, String medname, String link) {

        Typeface tf_r= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getActivity().getLayoutInflater().inflate(R.layout.botton_sheet_side_effects, null);
        TextView txth = (TextView)view.findViewById( R.id.tv2);
        TextView txtc = (TextView)view.findViewById( R.id.txt_content);
        TextView txtl = (TextView)view.findViewById( R.id.txt_link);
        TextView txtmn = (TextView)view.findViewById( R.id.txt_medname);

        txth.setTypeface(tf_b);
        txtc.setTypeface(tf_r);
        txtl.setTypeface(tf_r);
        txtmn.setTypeface(tf_r);

        txtmn.setVisibility(View.INVISIBLE);

        txtmn.setText(medname);
        txth.setText(heading);
        txtc.setText(CustomHtmlTextParsing.parse(content));
        txtl.setText(link);



        final Dialog mBottomSheetDialog = new Dialog (getActivity(),
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


    }

    public class MedicineNetworkOperation extends AsyncTask<String, Integer, String> {
        MedicineNetworkOperation asyncObject;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            /**
             * show dialog
             */
            //showpDialog();
            mDilatingDotsProgressBar.showNow();
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            nameMD.setText("                                                    ");
            manufacturerMD.setText("                               ");
            priceMD.setText("                         ");
            sizeMD.setText("                       ");
            scheduleMD.setText("                                                     ");
            constituentsMD.setText("                                             ");
            conmd.setText("                                                 ");

            nameMD.setBackground(getResources().getDrawable(R.drawable.shape10));
            manufacturerMD.setBackground(getResources().getDrawable(R.drawable.shape8));
            priceMD.setBackground(getResources().getDrawable(R.drawable.shape8));
            constituentsMD.setBackground(getResources().getDrawable(R.drawable.shape8));
            scheduleMD.setBackground(getResources().getDrawable(R.drawable.shape8));
            sizeMD.setBackground(getResources().getDrawable(R.drawable.shape10));

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
            new MainActivity().checkInternet(getActivity());
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... messageText) {
            // TODO Auto-generated method stub
            /**
             * Do network related stuff
             * return string response.
             */
            String result = new String ();
            int count = messageText.length;String line="";


            try {
                URL js= new URL(MedicineDetailsActivity2.loginURL);

                List<String> allKeys = Paper.book("medicineDetails").getAllKeys();
                if(allKeys.contains(MedicineDetailsActivity2.truemdCode))
                {
                    line = Paper.book("medicineDetails").read(MedicineDetailsActivity2.truemdCode);
                }
                else
                {
                    URLConnection jc = js.openConnection();
                    //Log.e("Siri resp: ", line);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                    line = reader.readLine();
                    //Paper.book("medicineDetails").write(MedicineDetailsActivity2.truemdCode, line);

                }

                if (line.length() > 5) {

                    Log.e("Siri resp: ", js.toString());
                    Log.e("Siri resp: ", line);
                    JSONObject jsonResponse = new JSONObject(line);
                    Paper.book("medicineDetails").write(MedicineDetailsActivity2.truemdCode, line);

                    JSONObject medicineJsonObject = jsonResponse.getJSONObject("medicine");
                    med = new Medicine();

                    Log.d("VolleyObjectRequest", "" + medicineJsonObject.toString());

                    Log.d("VolleyObjectRequest", "inside try for makeJsonObjectRequest after mVolley");
                    med.setName(medicineJsonObject.getString("name"));
                    med.setpForm(medicineJsonObject.getString("form"));
                    med.setpSize(medicineJsonObject.getString("size"));
                    med.setMrp(medicineJsonObject.getString("mrp"));
                    med.setMf(medicineJsonObject.getString("manufacturer"));
                    med.setuPrice(medicineJsonObject.getString("unitPrice"));
                    med.setForm(medicineJsonObject.getString("form"));

                    preg = "\n \n <b> Pregnancy Category: "+ TrueMDJSONUtils.goThroughNullCheck(medicineJsonObject.getJSONObject("pregnancy").getString("category"))+": "+
                            TrueMDJSONUtils.goThroughNullCheck(medicineJsonObject.getJSONObject("pregnancy").getString("label")) +"</b>";


                    preg += "\n"+ TrueMDJSONUtils.goThroughNullCheck(medicineJsonObject.getJSONObject("pregnancy").getString("description")) ;

                    lact = "\n \n <b> Lactation Category: "+ TrueMDJSONUtils.goThroughNullCheck(medicineJsonObject.getJSONObject("lactation").getString("category"))+": "+
                            TrueMDJSONUtils.goThroughNullCheck(medicineJsonObject.getJSONObject("lactation").getString("label")) +"</b>";


                    lact += "\n"+ TrueMDJSONUtils.goThroughNullCheck(medicineJsonObject.getJSONObject("lactation").getString("description")) ;



                    JSONObject schedule = medicineJsonObject.getJSONObject("schedule");

                    String scheduleS="This is a schedule "+ schedule.getString("category")+" drug: "+schedule.getString("label");
                    med.setSchedule(scheduleS);

                    JSONArray genericsJsonArray = medicineJsonObject.getJSONArray("generics");
                    Log.e("genericJA: ",""+genericsJsonArray.length()+" : "+genericsJsonArray.toString());
                    int k=0;
                    Generic[] generics= new Generic[genericsJsonArray.length()];

                    for(int i = 0; i < genericsJsonArray.length(); i++){
                        JSONObject r = genericsJsonArray.getJSONObject(i);
                        Generic genMed = new Generic();
                        genMed.setName(r.optString("name"));
                        genMed.setFaqs(r.optString("howItWorks"));
                       //genMed.setHow(r.optString("how"));
                        genMed.setSe(r.optString("sideEffects"));
                        genMed.setStrength(r.optString("strength"));
                        genMed.setUses(r.optString("usedFor"));

                        try {
                            JSONObject ml = TrueMDJSONUtils.nullSafeJSONObject(r.getJSONObject("medline"));
                            Log.e("genericMedlineJA: ",i+" : "+ml.toString());
                            if(ml.toString().length()>3)
                            {
                                Medline medline = new Medline(ml.optString("why"),ml.optString("precaution"),ml.optString("storage"),ml.optString("diet"), ml.optString("how"));
                                genMed.setMedline(medline);
                                Log.e("MedlineObject: ", medline.getWhy().toString());
                            }
                            else
                            {
                                Medline medline = new Medline(""+genMed.getUses(),"","","",""+genMed.getFaqs());
                                genMed.setMedline(medline);
                                Log.e("MedlineObject: ", medline.getWhy().toString());
                            }
                        } catch (JSONException e) {
                            Log.e("Medline in mVolley0: ", e.getMessage());
                            e.printStackTrace();
                        }

                        generics[k]=genMed;
                        Log.e("generics[k]: ", ""+k+" : "+genMed.getName().toString());
                        k++;

                    }
                    med.setGenerics(generics);

                    Log.e("Generic for", "out of loop "+k);

                    JSONArray constituentsArr = medicineJsonObject.getJSONArray("constituents");
                    ArrayList<String> constituentsAL = new ArrayList<String>();
                    for(int t = 0; t < constituentsArr.length(); t++){
                        String r = constituentsArr.getString(t);
                        String mg = generics[t].getStrength();
                        if(mg.equalsIgnoreCase("NA"))
                            mg=" ";
                        constituentsAL.add(r+" "+mg);
                    }
                    Log.e("Constituents: ", constituentsAL.toString());
                    med.setConstituents(constituentsAL);




                }
            } catch (IOException e) {
                Log.e("Error in mVolley1: ",""+e.getMessage());
                SweetAlertDialog sdialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                sdialog.setTitleText("Oops...!");
                sdialog.setContentText("There server is taking too long to respond or there might be some issues with your internet connection.");

                sdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
//                            sDialog
//                                    .setTitleText("Deleted!")
//                                    .setContentText("Your imaginary file has been deleted!")
//                                    .setConfirmText("OK")
//                                    .setConfirmClickListener(null)
//                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);

//                            FragmentManager fragmentManager = getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, new HomeFragment());
//                            //fragmentTransaction.commit();
//                            fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commitAllowingStateLoss();
//
//                            // set the toolbar title
//                            getSupportActionBar().setTitle("TrueMD");

                        getActivity().finish();

                        Intent intent_main = new Intent(getActivity(), MainActivity.class);
                        intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        if (getContext().getClass().equals(MainActivity.class))
                            getContext().startActivity(intent_main);
                        else
                            getContext().startActivity(intent_main);
                    }
                });

                sdialog.show();
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("Error in mVolley2: ",""+e.getMessage() );

                e.printStackTrace();
            } catch (Exception e) {
                Log.e("Error in mVolley3: ","" +e.getMessage());
                SweetAlertDialog sdialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                sdialog.setTitleText("Oops...!");
                sdialog.setContentText("There server is taking too long to respond or there might be some issues with your internet connection.");

                sdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {


                        getActivity().finish();

                        Intent intent_main = new Intent(getActivity(), MainActivity.class);
                        intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        if (getContext().getClass().equals(MainActivity.class))
                            getContext().startActivity(intent_main);
                        else
                            getContext().startActivity(intent_main);
                    }
                });

                sdialog.show();

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
            try {

                MedicineDetailsActivity2.medicine = med;
                nameMD.setBackground(getResources().getDrawable(R.drawable.shape9));
                manufacturerMD.setBackground(getResources().getDrawable(R.drawable.shape9));
                priceMD.setBackground(getResources().getDrawable(R.drawable.shape9));
                constituentsMD.setBackground(getResources().getDrawable(R.drawable.shape9));
                scheduleMD.setBackground(getResources().getDrawable(R.drawable.shape9));
                sizeMD.setBackground(getResources().getDrawable(R.drawable.shape2));

                nameMD.setText(TrueMDJSONUtils.goThroughNullCheck(med.getName()));
                manufacturerMD.setText(TrueMDJSONUtils.goThroughNullCheck(med.getMf()));
                priceMD.setText("\u20B9 "+TrueMDJSONUtils.goThroughNullCheck(med.getMrp()));
                sizeMD.setText(TrueMDJSONUtils.goThroughNullCheck(med.getpSize()));
                scheduleMD.setText(TrueMDJSONUtils.goThroughNullCheck(med.getSchedule()));
                conmd.setText("Prices shown are indicative and subject to change.");


            String cons="";

            for(String str: med.getConstituents())
            {
                cons=cons+" "+str;

            }


            constituentsMD.setText("Contains: " + TrueMDJSONUtils.goThroughNullCheck(cons));

            for(Generic genmed: med.getGenerics())
            {
                sideEffects+= "<b>"+ TrueMDJSONUtils.goThroughNullCheck(genmed.getName())+"</b>"+"\n";
                sideEffects+= TrueMDJSONUtils.goThroughNullCheck(genmed.getSe())+"\n\n";

                precautions+= "<b>"+TrueMDJSONUtils.goThroughNullCheck(genmed.getName())+"</b>"+"\n";
                precautions+= TrueMDJSONUtils.goThroughNullCheck(genmed.getMedline().getPrecaution())+"\n\n";

                why+= "<b>"+TrueMDJSONUtils.goThroughNullCheck(genmed.getName())+"</b>"+"\n";
                why+= TrueMDJSONUtils.goThroughNullCheck(genmed.getMedline().getWhy())+"\n\n";

                how+= "<b>"+TrueMDJSONUtils.goThroughNullCheck(genmed.getName())+"</b>"+"\n";
                how+= TrueMDJSONUtils.goThroughNullCheck(genmed.getMedline().getHow())+"\n\n";

                diet+= "<b>"+TrueMDJSONUtils.goThroughNullCheck(genmed.getName())+"</b>"+"\n";
                diet+= TrueMDJSONUtils.goThroughNullCheck(genmed.getMedline().getDiet())+"\n\n";

                storage+= "<b>"+TrueMDJSONUtils.goThroughNullCheck(genmed.getName())+"</b>"+"\n";
                storage+= TrueMDJSONUtils.goThroughNullCheck(genmed.getMedline().getStorage())+"\n\n";

                faqs+= "<b>"+TrueMDJSONUtils.goThroughNullCheck(genmed.getName())+"</b>"+"\n";
                faqs+= TrueMDJSONUtils.goThroughNullCheck(genmed.getFaqs())+"\n\n";

            }

            MedicineDetailsActivity2.medicine = med;

            } catch (Exception e) {
                e.printStackTrace();
            }

            //hidepDialog();
            mDilatingDotsProgressBar.hideNow();
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



            super.onPostExecute(result);
        }
    }


}
