package com.truemdhq.projectx.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.adapter.CustomSearchAdapter;
import com.truemdhq.projectx.adapter.SuggestionAdapter;
import com.truemdhq.projectx.helper.ExceptionHandler;
import com.truemdhq.projectx.model.JsonParse;
import com.truemdhq.projectx.model.SuggestGetSet;
import com.truemdhq.projectx.service.ConnectionDetector;

/**
 * Created by yashvardhansrivastava on 19/01/16.
 */
public class SearchActivity extends AppCompatActivity {

    //ListView lv;
    //Context context;
    TextView txtResponse;
    EditText searchBar;
    Button searchButton;
    List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
    DilatingDotsProgressBar mdilatingDotsProgress;
    ImageView placeholder;


    static int k;

    ListView lv;
    Context context;

    //ArrayList prgmName;

    //public static String [] prgmNameList;
    ArrayList<HashMap<String, String>> suggestions;


    //public static String [] prgmPriceList;
    public static String [] prgmProjectXCodeList;
    public static String [] prgmPackSizeList;
    //public static String [] prgmManufacturerList;
//
    ArrayList prgmName;
   // public static int [] prgmImages={R.drawable.images,R.drawable.images1,R.drawable.images2,R.drawable.images3,R.drawable.images4,R.drawable.images5,R.drawable.images6,R.drawable.images7,R.drawable.images8};
    public static String [] prgmNameList;
    public static String [] prgmPriceList;
    public static String [] prgmManufacturerList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_activity);

        new MainActivity().checkInternet(this);



        context= this;
        k=0;  ListData.clear();

//        getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//        );

        mdilatingDotsProgress = (DilatingDotsProgressBar) findViewById(R.id.sa_progress);

        placeholder=(ImageView) findViewById(R.id.placeholder);

        suggestions = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> check = new HashMap<String, String>();

        placeholder.setVisibility(View.INVISIBLE);


        //((AppCompatActivity) getActivity()).getSupportActionBar().show();

        searchBar= (EditText) findViewById(R.id.autoComplete);
        Typeface tf_l= Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");
        searchBar.setTypeface(tf_l);

        prgmNameList= new String[30];
        prgmProjectXCodeList = new String[30];
        prgmPriceList= new String[30];
        prgmManufacturerList= new String[30];
        prgmPackSizeList= new String[30];

        lv=(ListView) findViewById(R.id.listView);

        searchBar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                prgmNameList= new String[30];
                prgmProjectXCodeList = new String[30];
                prgmPriceList= new String[30];
                prgmManufacturerList= new String[30];
                prgmPackSizeList= new String[30];
                lv.setAdapter(new CustomSearchAdapter(context, prgmNameList, prgmManufacturerList, prgmPriceList, prgmPackSizeList,prgmProjectXCodeList));

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prgmNameList= new String[30];
                prgmProjectXCodeList = new String[30];
                prgmPriceList= new String[30];
                prgmManufacturerList= new String[30];
                prgmPackSizeList= new String[30];
                lv.setAdapter(new CustomSearchAdapter(context, prgmNameList, prgmManufacturerList, prgmPriceList, prgmPackSizeList,prgmProjectXCodeList));


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(searchBar.getText().toString().length()>2) {
                  // new ExecuteNetworkOperation3().execute(searchBar.getText().toString());

                    prgmNameList= new String[30];
                    prgmProjectXCodeList = new String[30];
                    prgmPriceList= new String[30];
                    prgmManufacturerList= new String[30];
                    prgmPackSizeList= new String[30];
                    lv.setAdapter(new CustomSearchAdapter(context, prgmNameList, prgmManufacturerList, prgmPriceList, prgmPackSizeList,prgmProjectXCodeList));


                    k=0;  ListData.clear();
                    Log.e("k:: ",""+k);
                    getAutocorrectMeds(searchBar.getText().toString());

                }

            }
        });



        lv.setAdapter(new CustomSearchAdapter(context, prgmNameList, prgmManufacturerList, prgmPriceList, prgmPackSizeList,prgmProjectXCodeList));


    }


    public  void getAutocorrectMeds(String message)
    {
        try {

            JsonObjectRequester mRequester = new RequestBuilder(context)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .timeOut(50000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    //.addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    //.addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToGetLogout()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, MainActivity.app_url+"/api/v2/medicines?key="+MainActivity.dev_key+"&search="+message);
            //URL js = new URL(MainActivity.app_url+"/api/v2/medicines?key="+MainActivity.dev_key+"&search="+temp);
            Log.e("message::" , message);

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class JsonObjectListenerToGetLogout extends Response.SimpleObjectResponse {



        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                Log.e("PL resp: ", jsonObject.toString());

                //JSONObject jsonResponse = new JSONObject(line);
                JSONArray jsonArray = jsonObject.getJSONArray("suggestions");
                if(jsonArray.length()==0)
                    placeholder.setVisibility(View.VISIBLE);
                else
                    placeholder.setVisibility(View.INVISIBLE);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject r = jsonArray.getJSONObject(i);

                    ListData.add(new SuggestGetSet(r.getString("truemdCode"),r.getString("name"),r.getString("manufacturer"),r.getString("packSize"),r.getString("mrp")));
                }

                k=0;
                prgmNameList= new String[30];
                prgmProjectXCodeList = new String[30];
                prgmPriceList= new String[30];
                prgmManufacturerList= new String[30];
                prgmPackSizeList= new String[30];


                for (int i=0;i<ListData.size();i++) {
//
//

                    if(k<29){
                        Log.e("autotext: ",k+":: "+ListData.get(i).getId()+":: "+ListData.get(i).getName());
                        prgmProjectXCodeList[k] = ListData.get(i).getId();
                        prgmNameList[k] = ListData.get(i).getName();
                        prgmManufacturerList[k] = ListData.get(i).getManufacturer();
                        prgmPackSizeList[k] = ListData.get(i).getPackSize();
                        prgmPriceList[k] = ListData.get(i).getMrp();
                        k++;

                    }
                    else{
                        Log.e("autotext: ",k+":: "+ListData.get(i).getId()+":: "+ListData.get(i).getName());
                        k++;

                    }

                }
                lv=(ListView) findViewById(R.id.listView);
                lv.setAdapter(new CustomSearchAdapter(context, prgmNameList, prgmManufacturerList, prgmPriceList, prgmPackSizeList,prgmProjectXCodeList));

            } catch (Exception e) {

                placeholder.setVisibility(View.VISIBLE);
                Log.e("PL resp: ", e.getMessage());
                Toast.makeText(context,"Network Issue",Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
        }


        @Override
        public void onRequestStart(int requestCode) {
mdilatingDotsProgress.showNow();
            prgmNameList= new String[30];
            prgmProjectXCodeList = new String[30];
            prgmPriceList= new String[30];
            prgmManufacturerList= new String[30];
            prgmPackSizeList= new String[30];
            k=0;  ListData.clear();

        }




        @Override
        public void onRequestFinish(int requestCode) {

mdilatingDotsProgress.hideNow();
            prgmNameList= new String[30];
            prgmProjectXCodeList = new String[30];
            prgmPriceList= new String[30];
            prgmManufacturerList= new String[30];
            prgmPackSizeList= new String[30];
            k=0;  ListData.clear();
        }
        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            prgmNameList= new String[30];
            prgmProjectXCodeList = new String[30];
            prgmPriceList= new String[30];
            prgmManufacturerList= new String[30];
            prgmPackSizeList= new String[30];
            k=0;  ListData.clear();

        }

    }

    @Override
    public void onBackPressed() {

        finish();

        Intent intent_main = new Intent(getApplicationContext(),MainActivity.class);
        intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplicationContext().startActivity(intent_main);



    }
    public void checkInternet(final Context context){
        ConnectionDetector cd = new ConnectionDetector(context);

        Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
        if (!isInternetPresent) {

            SweetAlertDialog sdialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            sdialog.setTitleText("Oops...No Internet Connection!");
            sdialog.setContentText("This service needs an internet connection. Please try again");

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
//                            getSupportActionBar().setTitle("ProjectX");

                    finish();

                    Intent intent_main = new Intent(context, SearchActivity.class);
                    intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    if (context.getClass().equals(MainActivity.class))
                        context.startActivity(intent_main);
                    else
                        context.startActivity(intent_main);
                }
            });

            sdialog.show();

            sdialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        finish();

                        Intent intent_main = new Intent(context, SearchActivity.class);
                        intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        if (context.getClass().equals(MainActivity.class))
                            context.startActivity(intent_main);
                        else
                            context.startActivity(intent_main);
                    }
                    return true;
                }
            });
        }
    }


}
