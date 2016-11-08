package com.truemdhq.projectx.activity;

/**
 * Created by yashvardhansrivastava on 25/12/15.
 */


import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.alirezaafkar.json.requester.Requester;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tramsun.libs.prefcompat.Pref;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.multidex.MultiDexApplication;
import android.support.multidex.MultiDex;
import android.util.Log;

import org.json.JSONArray;

import com.truemdhq.projectx.R;
import io.paperdb.Paper;

public class AppController extends MultiDexApplication {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Pref.init(this);
        Paper.init(this);
        //jsonRequester.init
        Map<String, String> header = new HashMap<>();
        header.put("charset", "utf-8");

        String baseUrl = "";



        List<String> allKeys1 = Paper.book("reminder_id").getAllKeys();
        if(allKeys1.contains("id"))
        {
            // Paper.book("reminder_id").write("id", "1");
        }
        else
        {
            Paper.book("reminder_id").write("id", "1");
        }
        Log.e("AppController:", ""+Paper.book("reminder_id").read("id"));


        List<String> allKeys2 = Paper.book("introduction").getAllKeys();
        if(allKeys2.contains("intro"))
        {
           // Paper.book("introduction").write("intro","0");
        }
        else
        {
            Paper.book("introduction").write("intro","0");
            Paper.book("introduction").write("preshelp","0");
            Paper.book("introduction").write("askpin","0");
            Paper.book("introduction").write("rateus","0");
            Paper.book("introduction").write("ratecounter","1");
        }
        Log.e("AppControllerIntro:", ""+Paper.book("introduction").read("intro"));

        List<String> allKeys3 = Paper.book("user").getAllKeys();
        if(allKeys3.contains("name"))
        {
            // Paper.book("introduction").write("intro","0");
        }
        else
        {
            Paper.book("user").write("name","");
        }
        Log.e("AppController:", ""+Paper.book("user").read("name"));

        List<String> allKeysp = Paper.book("user").getAllKeys();
        if(allKeysp.contains("pincode"))
        {
            // Paper.book("introduction").write("intro","0");
        }
        else
        {
            Paper.book("user").write("pincode","");
            Paper.book("user").write("pincode_city","");

        }
        Log.e("AppController:", ""+Paper.book("user").read("pincode")+Paper.book("user").read("pincode_city"));


        List<String> allKeys4 = Paper.book("nav").getAllKeys();
        if(allKeys4.contains("selected"))
        {
            // Paper.book("introduction").write("intro","0");
        }
        else
        {
            Paper.book("nav").write("selected","0");
        }
        Log.e("AppController:", ""+Paper.book("nav").read("selected"));

        List<String> allKeys5 = Paper.book("reminder").getAllKeys();
        if(allKeys5.contains("pouchesafternow"))
        {
            // Paper.book("introduction").write("intro","0");
        }
        else
        {
            Paper.book("reminder").write("pouchesafternow","[]");
        }
        Log.e("AppController:", ""+Paper.book("reminder").read("pouchesafternow"));



//
        new Requester.Config(getApplicationContext()).baseUrl(baseUrl).header(header);




    }


    public void clearApplicationData() {

        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {

            String[] fileNames = applicationDirectory.list();

            for (String fileName : fileNames) {

                if (!fileName.equals("lib")) {

                    deleteFile(new File(applicationDirectory, fileName));

                }

            }

        }

    }

    public static boolean deleteFile(File file) {

        boolean deletedAll = true;

        if (file != null) {

            if (file.isDirectory()) {

                String[] children = file.list();

                for (int i = 0; i < children.length; i++) {

                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;

                }

            } else {

                deletedAll = file.delete();

            }

        }

        return deletedAll;

    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(AppController.this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}