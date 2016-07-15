package info.truemd.materialdesign.service;

/**
 * Created by yashvardhansrivastava on 01/03/16.
 */

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import info.truemd.materialdesign.activity.ForgetPwdActivity;
import info.truemd.materialdesign.activity.MainActivity;
import info.truemd.materialdesign.activity.PreLoginActivity;
import info.truemd.materialdesign.activity.SignupActivity;
import info.truemd.materialdesign.app.ConfigOTP;
import info.truemd.materialdesign.helper.SessionManager;
import info.truemd.materialdesign.model.UserObject;
import io.paperdb.Paper;
//import info.androidhive.materialdesign.app.MyApplication;


/**
 * Created by Ravi on 04/04/15.
 */
public class HttpService extends IntentService {

    SessionManager session;

    private static String TAG = HttpService.class.getSimpleName();

    public HttpService() {
        super(HttpService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String otp = intent.getStringExtra("otp");

            int origin = intent.getIntExtra("origin",0);

            if(origin==0)
            {
                String otpToken = intent.getStringExtra("otp_token");
            verifyOtp(otp, otpToken, origin);

            }
            else if(origin==1) {

                String pwdToken = intent.getStringExtra("password-token");
                verifyOtp(otp, pwdToken, origin);


            }
                 }
    }

    /**
     * Posting the OTP to server and activating the user
     *
     * @param otp otp received in the SMS
     */
    private void verifyOtp(final String otp, final String otpToken, final int origin) {

        if (origin == 0) {
            try {
                URL js = new URL(ConfigOTP.URL_VERIFY_OTP + "&otp_token=" + otpToken + "&otp=" + otp);

                URLConnection jc = js.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                String line = reader.readLine();
                if (line.length() > 5) {
                    JSONObject jsonResponse = new JSONObject(line);
                    String verification = jsonResponse.getString("verification");
                    if (verification.equalsIgnoreCase("verified")) {
                        Log.e("HttpService_verifyOtp", "" + line);
                        //Toast.makeText(getApplicationContext(), "Phone Number Verified", Toast.LENGTH_SHORT).show();
                        Intent smsToMain = new Intent(getApplicationContext(), MainActivity.class);
                        smsToMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        session = new SessionManager(getApplicationContext());

                        JSONObject data = jsonResponse.getJSONObject("data");

                        UserObject obj = new UserObject();
                        obj.setMobile(data.getString("mobile"));
                        obj.setAuthentication_token(data.getString("authentication_token"));
                        Log.e("",data.getString("mobile")+" : "+data.getString("authentication_token"));

                        session.createLoginSession(obj.getMobile(), obj.getAuthentication_token(), Paper.book("RegisterPassword").read("password").toString(), "chat"+(int) Math.ceil(Math.random() * 100000000));

                        startActivity(smsToMain);

                    } else if (verification.equalsIgnoreCase("expired")) {
                        Log.e("HttpService_verifyOtp", "" + line);
                        Toast.makeText(getApplicationContext(), "OTP has expired. Try with new OTP", Toast.LENGTH_SHORT).show();

                    } else if (verification.equalsIgnoreCase("invalid")) {
                        Log.e("HttpService_verifyOtp", "" + line);
                        Toast.makeText(getApplicationContext(), "Wrong OTP. Try Again.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.e("HttpService_verifyOtp", "" + line);
                        Toast.makeText(getApplicationContext(), "Some error occured. Going back to SignUp Screen", Toast.LENGTH_SHORT).show();
                        Intent smsToSignup = new Intent(getApplicationContext(), PreLoginActivity.class);
                        smsToSignup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(smsToSignup);

                    }


                } else {
                    //OTP json received is blank
                    Log.e("HttpService_verifyOtp", "" + line);
                }
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else if (origin == 1) {
            Log.e("HttpService_verifyOtp", "forwarding reset pwd OTP code to changePwdActivity: " + otp);
            //Toast.makeText(getApplicationContext(), "forwarding reset pwd OTP code to changePwdActivity: " + otp, Toast.LENGTH_SHORT).show();
            Intent smsToForgotPwd = new Intent(getApplicationContext(), ForgetPwdActivity.class);
            smsToForgotPwd.putExtra("otp", otp);
            smsToForgotPwd.putExtra("password-token", otpToken);
            smsToForgotPwd.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(smsToForgotPwd);

        }
    }


    }

