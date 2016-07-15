package info.truemd.materialdesign.model;



import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yashvardhansrivastava on 03/02/16.
 */
public class RegisterJsonObjectRequest extends JsonObjectRequest
{
    public RegisterJsonObjectRequest(int method, String url, JSONObject jsonRequest,Response.Listener listener, Response.ErrorListener errorListener)
    {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        Map headers = new HashMap();
        headers.put("Content-Type", "application/json");
        //headers.put("Accept", "application/json");
        return headers;
    }

}