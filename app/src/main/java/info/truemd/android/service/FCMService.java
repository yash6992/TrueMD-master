package info.truemd.android.service;

import android.content.Context;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.util.HashMap;

import info.truemd.android.activity.MainActivity;
import info.truemd.android.helper.SessionManager;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 22/05/16.
 */
public class FCMService extends FirebaseInstanceIdService {

    SessionManager session;
    Context context;

    @Override
    public void onTokenRefresh() {

        context = getApplicationContext();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCMService:: ", "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    public void sendRegistrationToServer(String refreshedToken) {

        Log.e("FCM: ", "in sending refreshedToken: " + refreshedToken);

        Paper.book("FCM").write("refreshedToken",refreshedToken);

    }

}
