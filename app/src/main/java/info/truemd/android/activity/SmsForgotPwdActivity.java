package info.truemd.android.activity;

/**
 * Created by yashvardhansrivastava on 01/03/16.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import info.truemd.android.R;
//import info.androidhive.materialdesign.app.MyApplication;
import info.truemd.android.service.HttpService;

public class SmsForgotPwdActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = SmsActivity.class.getSimpleName();


    private Button btnRequestSms, btnVerifyOtp;
    private EditText inputName, inputEmail, inputMobile, inputOtp;
    private ProgressBar progressBar;

    private ImageButton btnEditMobile;
    private TextView txtEditMobile, sitback, msgotp, truemdTitle, truemdsubTitle;
    private LinearLayout layoutEditMobile;

    public static String resetPwdToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        Intent fromLogin = getIntent();
        resetPwdToken = fromLogin.getStringExtra("password-token");


        inputOtp = (EditText) findViewById(R.id.inputOtp);
        btnVerifyOtp = (Button) findViewById(R.id.btn_verify_otp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnEditMobile = (ImageButton) findViewById(R.id.btn_edit_mobile);
        txtEditMobile = (TextView) findViewById(R.id.txt_edit_mobile);
        layoutEditMobile = (LinearLayout) findViewById(R.id.layout_edit_mobile);

        // view click listeners
        btnEditMobile.setOnClickListener(this);

        btnVerifyOtp.setOnClickListener(this);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        // hiding the edit mobile number
        // layoutEditMobile.setVisibility(View.GONE);
        layoutEditMobile.setVisibility(View.VISIBLE);
        sitback= (TextView) findViewById(R.id.msg_sit_back_tv);
        msgotp= (TextView) findViewById(R.id.msg_manual_otp_tv);
        truemdTitle = (TextView) findViewById(R.id.truemdtitle_tv);
        truemdsubTitle = (TextView) findViewById(R.id.truemdsubtitle_tv);

        Typeface tf_pacifico = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        Typeface tf_l = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        truemdTitle.setTypeface(tf_pacifico);truemdsubTitle.setTypeface(tf_l);
        msgotp.setTypeface(tf_l); txtEditMobile.setTypeface(tf_l);
        sitback.setTypeface(tf_l);



//        // Checking for user session
//        // if user is already logged in, take him to main activity
//        if (pref.isVerified()) {
//            Intent intent = new Intent(SmsActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//
//            finish();
//        }



        /**
         * Checking if the device is waiting for sms
         * showing the user OTP screen
         */
//        if (pref.isWaitingForSms()) {
//            viewPager.setCurrentItem(1);
//            layoutEditMobile.setVisibility(View.VISIBLE);
//        }
    }
    @Override
    public void onBackPressed(){


        Intent signUpToLogin = new Intent(getApplicationContext(), PreLoginActivity.class);

        startActivityForResult(signUpToLogin, 0);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.btn_verify_otp:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputOtp.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                Log.e(TAG, "btn verify otp clicked");
                verifyOtp(resetPwdToken);
                break;

            case R.id.btn_edit_mobile:

                Intent smsForgotPwdToLogin = new Intent(getApplicationContext(), PreLoginActivity.class);
                finish();
                startActivityForResult(smsForgotPwdToLogin, 0);

                break;
        }
    }

    /**
     * sending the OTP to server and activating the user
     */
    private void verifyOtp(String otpToken) {
        String otp = inputOtp.getText().toString().trim();

        if (!otp.isEmpty()) {
            Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("otp", otp);
            grapprIntent.putExtra("password-token", otpToken);
            grapprIntent.putExtra("origin", 1);
            finish();
            startService(grapprIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Regex to validate the mobile number
     * mobile number should be of 10 digits length
     *
     * @param mobile
     * @return
     */
    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }


}