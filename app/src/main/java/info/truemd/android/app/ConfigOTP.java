package info.truemd.android.app;

import info.truemd.android.activity.MainActivity;

/**
 * Created by yashvardhansrivastava on 01/03/16.
 */
public class ConfigOTP {
    // server URL configuration
   // public static final String URL_REQUEST_SMS = "http://192.168.0.101/android_sms/msg91/request_sms.php";
    public static final String URL_VERIFY_OTP = MainActivity.app_url+"/verify_user.json?key="+ MainActivity.dev_key;//+"&otp_token="++"&otp=";
    //public static final String URL_VERIFY_PWD__OTP = MainActivity.app_url+"/verify_user.json?key="+ MainActivity.dev_key;//+"&otp_token="++"&otp=";

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "TRUEMD";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = " : ";
}