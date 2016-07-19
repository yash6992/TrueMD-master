package info.truemd.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import info.truemd.android.R;
import info.truemd.android.adapter.CustomAndroidGridViewAdapter;
import info.truemd.android.helper.SessionManager;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 02/07/16.
 */
public class HelpFragment extends Fragment {

    TextView title;
    ImageButton back;
    GridView gridView;
    Context context;
    ArrayList arrayList;
    JSONObject faqsJO;
    JSONArray faqsJA;
    DilatingDotsProgressBar help_progress;
    ArrayList<String> faqsUrls;
    ArrayList<String>  faqsStrings;
    ArrayList<JSONArray> faqsFaqs;
    List<Object> listFaqs;
    Button call;

    public static String[] gridViewStrings ;

    public static String[] gridViewImages ;


    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        title = (TextView) rootView.findViewById(R.id.od_title_tv);
        back = (ImageButton) rootView.findViewById(R.id.od_backImageButton);
        help_progress = (DilatingDotsProgressBar)rootView.findViewById(R.id.help_progress);
        faqsJA = new JSONArray();
        faqsJO = new JSONObject();
        faqsStrings=new ArrayList<>();
        faqsUrls=new ArrayList<>();
        faqsFaqs=new ArrayList<>();

        Typeface tf_l= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");

        title.setTypeface(tf_l);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        call=(Button) rootView.findViewById(R.id.btn_call);
        gridView = (GridView) rootView.findViewById(R.id.grid);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:07400074005"));
                startActivity(callIntent);
            }
        });
        getFAQs();
        return rootView;
    }

    public void getFAQs(){
        String result = new String ();
        String line="";


        SessionManager session = new SessionManager(context);
        HashMap<String, String> user = session.getUserDetails();

        try {

            JsonObjectRequester mRequester = new RequestBuilder(context)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .timeOut(20000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToGetFAQs()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, MainActivity.app_url+"/static/faqs.json");

        }  catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class JsonObjectListenerToGetFAQs extends Response.SimpleObjectResponse {


        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                SessionManager session = new SessionManager(context);
                if (jsonObject.toString().length() > 5) {


                    Log.e("UOrder resp: ", "in else for length=0");
                    try {
                        HashMap<String, String> user = session.getUserDetails();
                        faqsJO= jsonObject;
                        faqsJA = faqsJO.getJSONArray("topics");

                       // listFaqs = toList(faqsJA);

                        for(int l=0; l<faqsJA.length();l++){
                            JSONObject JO_l = faqsJA.getJSONObject(l);
                            faqsStrings.add(JO_l.optString("name"));
                            faqsUrls.add(JO_l.optString("icon"));
                            faqsFaqs.add(JO_l.getJSONArray("faqs"));

                        }

                        gridViewStrings = faqsStrings.toArray(new String[faqsStrings.size()]);
                        gridViewImages = faqsUrls.toArray(new String[faqsUrls.size()]);

                        gridView.setAdapter(new CustomAndroidGridViewAdapter(getActivity(), gridViewStrings, gridViewImages, faqsFaqs));

                        help_progress.hideNow();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                     //lv.setAdapter(new CustomUpcomingOrderAdapter(getApplicationContext(),dateArray,orderNoArray,nameArray, statusArray, dotsArray, gTotalArray, deliveryTimeArray,aljoUOrders  ));
                }



                else {
                    help_progress.hideNow();
                }
            } catch (Exception e) {

                Log.e("UOrderWeberr: ", e.getMessage());

                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());

            help_progress.hideNow();
        }



        @Override
        public void onRequestStart(int requestCode) {
            //placeholder.setVisibility(View.INVISIBLE);
            Log.e("UOrder resp: ", "onRequestStarted()");
            help_progress.showNow();
        }




        @Override
        public void onRequestFinish(int requestCode) {
            Log.e("UOrder resp: ", "onRequestFinish()");



        }
        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            Log.e ("onErrorResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());

            help_progress.hideNow();

        }



    }


    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }


}
