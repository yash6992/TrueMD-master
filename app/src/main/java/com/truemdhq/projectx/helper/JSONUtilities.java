package com.truemdhq.projectx.helper;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yashvardhansrivastava on 22/10/16.
 */
public class JSONUtilities {


    public static JSONObject getJsonObjectFromContentValuesWithTag(String key_tag, ContentValues params) {

        //all the passed parameters from the post request
        //iterator used to loop through all the parameters
        //passed in the post request
        //Iterator iter = params.entrySet().iterator();

        //Stores JSON
        JSONObject holder = null;
        try {
            holder = new JSONObject();


            //object for storing Json
            JSONObject data = new JSONObject();


            for (Map.Entry<String, Object> entry : params.valueSet()) {
                String key = entry.getKey(); // name
                Object value = entry.getValue(); // value
                data.put(key, value);
            }

            //puts email and 'foo@bar.com'  together in map
            holder.put(key_tag, data);
        } catch (JSONException e) {
            Log.e("JSONUtilities: ",""+e.getMessage());
            e.printStackTrace();
        }

        return holder;
    }
    public static JSONObject getJsonObjectFromContentValuesNoTag( ContentValues params) {

        //all the passed parameters from the post request
        //iterator used to loop through all the parameters
        //passed in the post request
        //Iterator iter = params.entrySet().iterator();

        //object for storing Json
        JSONObject data = null;
        try {
            data = new JSONObject();


            for (Map.Entry<String, Object> entry : params.valueSet()) {
                String key = entry.getKey(); // name
                Object value = entry.getValue(); // value
                data.put(key, value);
            }
        } catch (JSONException e) {
            Log.e("JSONUtilities: ",""+e.getMessage());
            e.printStackTrace();
        }


        return data;
    }





}
