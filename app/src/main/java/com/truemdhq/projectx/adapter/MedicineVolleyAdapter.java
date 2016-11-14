package com.truemdhq.projectx.adapter;

import android.content.ContentValues;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import com.truemdhq.projectx.activity.AppController;
import com.truemdhq.projectx.model.Generic;
import com.truemdhq.projectx.model.Medicine;
import com.truemdhq.projectx.model.Medline;

import static com.android.volley.Request.Method.GET;

/**
 * Created by yashvardhansrivastava on 20/02/16.
 */
public class MedicineVolleyAdapter {

    static Medicine med;


    public MedicineVolleyAdapter() {
    }

    public static Medicine makeMedicineJsonObjectRequest(ContentValues params, String loginURL, final VolleyCallback callback) throws JSONException {

        JSONObject jo;


        jo= getJsonObjectFromContentValues("user", params);

        JsonObjectRequest jsonObjReq;
        jsonObjReq = new JsonObjectRequest(GET,
                loginURL,jo, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("VolleyObjectRequest", "in MedicineVolleyAdapter");
                Log.d("VolleyObjectRequest", response.toString());

                try {
                    //result=response;

                    JSONObject medicineJsonObject = response.getJSONObject("medicine");
                    med = new Medicine();

                    Log.d("VolleyObjectRequest", ""+medicineJsonObject.toString());

                    Log.d("VolleyObjectRequest", "inside try for makeJsonObjectRequest after mVolley");
                    med.setName(medicineJsonObject.getString("name"));
                    med.setpForm(medicineJsonObject.getString("form"));
                    med.setpSize(medicineJsonObject.getString("size"));
                    med.setMrp(medicineJsonObject.getString("mrp"));
                    med.setMf(medicineJsonObject.getString("manufacturer"));

                    JSONObject schedule = medicineJsonObject.getJSONObject("schedule");

                    String scheduleS=""+ schedule.getString("category")+": "+schedule.getString("label");
                    med.setSchedule(scheduleS);

                    JSONArray genericsJsonArray = medicineJsonObject.getJSONArray("generics");
                    int k=0;
                    Generic[] generics= new Generic[genericsJsonArray.length()];

                    for(int i = 0; i < genericsJsonArray.length(); i++){
                        JSONObject r = genericsJsonArray.getJSONObject(i);
                        Generic genMed = new Generic();
                        genMed.setName(r.getString("name"));
                        genMed.setFaqs(r.getString("faqs"));
                        genMed.setSe(r.getString("sideEffects"));
                        genMed.setStrength(r.getString("strength"));

                        try {
                            JSONObject ml = r.getJSONObject("medline");
                            Medline medline = new Medline(ml.optString("why"),ml.optString("precaution"),ml.optString("storage"),ml.optString("diet"),ml.optString("how"));
                            genMed.setMedline(medline);
                        } catch (JSONException e) {
                            Log.e("Medline in mVolley: ", r.getJSONObject("medline").toString());
                            e.printStackTrace();
                        }

                        generics[k]=genMed;
                        k++;

                    }
                    med.setGenerics(generics);



                    JSONArray constituentsArr = medicineJsonObject.getJSONArray("constituents");
                    ArrayList<String> constituentsAL = new ArrayList<String>();
                    for(int i = 0; i < constituentsArr.length(); i++){
                        String r = constituentsArr.getString(i);
                        String mg = generics[i].getStrength();
                        if(mg.equalsIgnoreCase("NA"))
                            mg=" ";
                        constituentsAL.add(r+" "+mg);
                    }
                    med.setConstituents(constituentsAL);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callback.onSuccess(med);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("mVolley adapter ", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

        Log.d("VolleyObjectRequest", "just before MedicineVolleyAdapter return");

        return med;
    }



    public interface VolleyCallback{
        void onSuccess(Medicine result);
    }

    private static JSONObject getJsonObjectFromContentValues(String key_tag, ContentValues params) throws JSONException {

        //all the passed parameters from the post request
        //iterator used to loop through all the parameters
        //passed in the post request
        //Iterator iter = params.entrySet().iterator();

        //Stores JSON
        JSONObject holder = new JSONObject();


        //object for storing Json
        JSONObject data = new JSONObject();


        for (Map.Entry<String, Object> entry : params.valueSet()) {
            String key = entry.getKey(); // name
            String value = entry.getValue().toString(); // value
            data.put(key, value);
        }

        //puts email and 'foo@bar.com'  together in map
        holder.put(key_tag, data);

        return holder;
    }
}
