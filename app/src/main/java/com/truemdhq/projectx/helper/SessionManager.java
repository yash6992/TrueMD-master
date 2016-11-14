package com.truemdhq.projectx.helper;

/**
 * Created by yashvardhansrivastava on 01/03/16.
 */

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import com.truemdhq.projectx.activity.LoginActivity;
import com.truemdhq.projectx.activity.MainActivity;
import com.truemdhq.projectx.activity.PreLoginActivity;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME_UM = "ProjectXUserManangement";

    // All Shared Preferences Keys
    private static final String IS_LOGIN_UM = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_MOBILE_UM = "mobile";

    // User password (make variable public to access from outside)
    public static final String KEY_PASSWORD_UM = "password";

    // authentication address (make variable public to access from outside)
    public static final String KEY_AUTHENTICATION_UM = "authentication";

    // authentication chat id
    public static final String KEY_CHAT_ID_UM = "chat_id";



    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME_UM, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String mobile, String authentication, String password, String chatId){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN_UM, true);

        // Storing name in pref
        editor.putString(KEY_MOBILE_UM, mobile);

        // Storing pwd in pref
        editor.putString(KEY_PASSWORD_UM, password);

        // Storing authentication in pref
        editor.putString(KEY_AUTHENTICATION_UM, authentication);

        // Storing authentication in pref
        editor.putString(KEY_CHAT_ID_UM, chatId);

        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_MOBILE_UM, pref.getString(KEY_MOBILE_UM, ""));

        // user pwd id
        user.put(KEY_PASSWORD_UM, pref.getString(KEY_PASSWORD_UM, ""));

        // user authentication id
        user.put(KEY_AUTHENTICATION_UM, pref.getString(KEY_AUTHENTICATION_UM, ""));

        // user authentication id
        user.put(KEY_CHAT_ID_UM, pref.getString(KEY_CHAT_ID_UM, ""));

        // return user
        return user;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status

        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, PreLoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }
    
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        // Log.e("Logout resp: ", jsonObject.toString());
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, PreLoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



        // Staring Login Activity
        _context.startActivity(i);

        runLogoutOperation();


    }
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN_UM, false);
    }

    public void runLogoutOperation()
    {
        try {
            SessionManager sessionManager = new SessionManager(_context.getApplicationContext());

            HashMap<String, String> user = sessionManager.getUserDetails();

            // name
            String mobile = user.get(SessionManager.KEY_MOBILE_UM);
            String at = user.get(SessionManager.KEY_AUTHENTICATION_UM);


            JsonObjectRequester mRequester = new RequestBuilder(_context)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(false)
                   .timeOut(50000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                   .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                   .addToHeader("Content-Type", "application/json")
                   .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    //.tag(REQUEST_TAG)
                    //.addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    //.addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToGetLogout()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, MainActivity.app_url+"/users/sign_out.json");

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class JsonObjectListenerToGetLogout extends Response.SimpleObjectResponse {

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {

               Log.e("Logout resp: ", ":"+jsonObject+":");

                SessionManager sessionManager = new SessionManager(_context.getApplicationContext());

                HashMap<String, String> user = sessionManager.getUserDetails();

                // name
                String mobile = user.get(SessionManager.KEY_MOBILE_UM);
                String at = user.get(SessionManager.KEY_AUTHENTICATION_UM);


                Log.e("Logout resp: ", "logout:"+ mobile+":"+at+":");



            } catch (Exception e) {

                Log.e("Logout resp: ", e.getMessage());
                Toast.makeText(_context,"Network Issue",Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
        }


        @Override
        public void onRequestStart(int requestCode) {


        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)


            Log.e ("onErrorResponseLogout: ", requestCode +" :: "+volleyError.getMessage());

            editor.clear();
            editor.commit();

            // After logout redirect user to Login Activity
            Intent i = new Intent(_context, PreLoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

        }




        @Override
        public void onRequestFinish(int requestCode) {


        }
        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            Log.e("onFinishResp: ", message);

        }

    }


}