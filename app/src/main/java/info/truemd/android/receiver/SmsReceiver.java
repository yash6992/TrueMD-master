package info.truemd.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import info.truemd.android.activity.SmsActivity;
import info.truemd.android.activity.SmsForgotPwdActivity;
import info.truemd.android.app.ConfigOTP;
import info.truemd.android.service.HttpService;

/**
 * Created by Ravi on 09/07/15.
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.toLowerCase().contains(ConfigOTP.SMS_ORIGIN.toLowerCase())) {
                        return;
                    }

                    if(message.toLowerCase().contains("password"))
                    { // verification code from sms
                        String verificationCode = getVerificationCode(message);

                        Log.e(TAG, "OTP received: " + verificationCode);

                        if(SmsForgotPwdActivity.resetPwdToken.length()!=0) {

                            Intent hhtpIntent = new Intent(context, HttpService.class);
                            hhtpIntent.putExtra("otp", verificationCode);
                            hhtpIntent.putExtra("origin", 1);
                            hhtpIntent.putExtra("password-token",SmsForgotPwdActivity.resetPwdToken);
                            Log.e(TAG, " "+verificationCode+" "+SmsForgotPwdActivity.resetPwdToken);
                            context.startService(hhtpIntent);
                        }
                        else
                            Log.e(TAG, "OTP no otp token");
                    }
                    else
                    { // verification code from sms
                        String verificationCode = getVerificationCode(message);

                        Log.e(TAG, "OTP received: " + verificationCode);

                        if(SmsActivity.otpToken.length()!=0) {

                            Intent hhtpIntent = new Intent(context, HttpService.class);
                            hhtpIntent.putExtra("otp", verificationCode);
                            hhtpIntent.putExtra("origin", 0);
                            hhtpIntent.putExtra("otp_token", SmsActivity.otpToken);
                            Log.e(TAG, " "+verificationCode+" "+SmsActivity.otpToken);
                            context.startService(hhtpIntent);
                        }
                        else
                            Log.e(TAG, "OTP no otp token");
                    }


                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = null;
        int index = message.indexOf(ConfigOTP.OTP_DELIMITER);

        if (index != -1) {
            int start = index + 3;
            int length = 4;
            code = message.substring(start, start + length);
            return code;
        }

        return code;
    }
}