package com.truemdhq.projectx.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


import com.truemdhq.projectx.R;
import com.truemdhq.projectx.helper.ExceptionHandler;
import io.paperdb.Paper;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText nameText;
    EditText emailText;
    EditText passwordText; ImageButton imageViewLogin;
    Button signupButton; CheckBox checkbox;
    TextView loginLink,termstv, truemdTitle, truemdsubTitle;
    public static String passwordSignup="";
    //String otpToken;
    DilatingDotsProgressBar signupProgress;
    Context context; ScrollView sv_register;


    RequestQueue requestQueue;

    private ProgressDialog pDialog;

    private TextView txtResponse;

    // temporary string to show the parsed response
    private String jsonResponse;

    String signupURL = MainActivity.app_url+"/users.json";
    private ContentValues paramsForPostRequest;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        truemdTitle = (TextView) findViewById(R.id.truemdtitle_tv);
        truemdsubTitle = (TextView) findViewById(R.id.truemdsubtitle_tv);
        checkbox = (CheckBox) findViewById(R.id.checkBox);
        termstv = (TextView) findViewById(R.id.termstv);
        sv_register = (ScrollView) findViewById(R.id.sv_register);
        signupProgress = (DilatingDotsProgressBar) findViewById(R.id.signup_progress);

        Typeface tf_pacifico = Typeface.createFromAsset(getAssets(), "MarkOffcPro-Bold.ttf");
        Typeface tf_l = Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");

        truemdTitle.setTypeface(tf_pacifico);
        truemdsubTitle.setTypeface(tf_l);checkbox.setTypeface(tf_l);termstv.setTypeface(tf_l);

        context= getApplicationContext();


        paramsForPostRequest= new ContentValues();

        txtResponse= (TextView) findViewById(R.id.txtResponse_signup);
        txtResponse.setVisibility(View.GONE);
        nameText= (EditText)findViewById(R.id.input_name);
        emailText= (EditText)findViewById(R.id.input_email);
        passwordText= (EditText)findViewById(R.id.input_password);
        signupButton= (Button)findViewById(R.id.btn_signup);
        loginLink=(TextView)findViewById(R.id.link_login);



        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        loginLink.setTypeface(tf_l);
        signupButton.setTypeface(tf_l);

        emailText.setText(getIntent().getStringExtra("mobile"));
        emailText.setFocusable(false);
        emailText.setEnabled(false);
        emailText.setCursorVisible(false);
        emailText.setKeyListener(null);
        emailText.setBackgroundColor(Color.TRANSPARENT);

        imageViewLogin = (ImageButton) findViewById(R.id.imageButtonToPreLogin);
        imageViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nameText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(emailText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(passwordText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent signUpToLogin = new Intent(getApplicationContext(), PreLoginActivity.class);

                startActivityForResult(signUpToLogin, 0);
            }
        });

        termstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet("Terms and Conditions", "");
            }
        });
    }

    public void openBottomSheet( String heading,  String link) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_terms, null);
       // WebView wv = (WebView) view.findViewById(R.id.webView);
        TextView txth = (TextView)view.findViewById( R.id.tv2);
        TextView txt = (TextView)view.findViewById( R.id.textView3);

        txth.setTypeface(tf_r);
        txt.setTypeface(tf_r);

        String r="Terms and Conditions\n" +
                "The website and/or mobile application (hereinafter defined) is owned and operated by ProjectX technologies private limited (hereinafter referred to as “ProjectX”, “ProjectX.in”, “company”, “we”, ”our” or “us”, which term shall include its successors and permitted assigns).\n" +
                "The following are the terms and conditions, read together with the privacy policy, that govern your purchase and use of the products and services (the \"products\") from ProjectX.in, and constitute a legally binding agreement, between you (referred to as “you”, “your”, “customer\" or \"user\", which shall also include persons merely visiting the website/app and/or contacting ProjectX via third party applications including but not limited to Whatsapp messenger) and ProjectX.\n" +
                "By accessing, viewing, visiting or using the website and/or app, you agree to be bound by the terms described herein and all terms incorporated by reference. If you do not agree to all of these terms, do not use the website.\n" +
                "As per the present agreement the relationship between you and the company is only supplier and customer. Solely for the purpose to provide services. \n" +
                "\n" +
                "User account\n" +
                "To use certain features of the website  and/or app (e.g., ordering products, posting rating/reviews, receiving ProjectX notifications), you must set up an account with ProjectX and provide certain information about yourself as prompted by the account creation form, including without limitation, your name, gender, email address, account password, mobile phone number and billing/shipping address. All of your registration information is protected by our privacy policy.\n" +
                "You agree and confirm that you shall (a) create only one account; (b) provide accurate, truthful, current and complete information when creating your account. In all your dealings through the website and/or mobile application (i) maintain and promptly update your account information; (ii) maintain the security of your account by not sharing your password with others and restricting access to your account and your device; (iii) promptly notify the company if you discover or otherwise suspect any security breaches relating to your account or the website and/or mobile application; and (iv) take responsibility for all activities that occur under your account and accept all risk of unauthorized access to the same.\n" +
                "You agree and confirm that you are over 18 years of age at the time of creation of your ProjectX account. ProjectX is not intended for children under the age of 18.\n" +
                "ProjectX will not be liable for any loss or damages as a result of your failure to maintain the confidentiality of your account credentials. If you suspect any unauthorised use of your ProjectX account, you shall immediately notify ProjectX.\n" +
                "By registering, accessing or using the website, you accept the terms of this terms of use and represent and warrant to ProjectX that you are ‘competent to contract’ under the contract act and have the right, authority and capacity to use the website and agree to and abide by its terms of use.\n" +
                "Placing orders\n" +
                "In order to use the services, you must provide certain personal and medical information about yourself or those for whom you are authorized to present prescriptions (the “patient”). You may voluntarily submit, and hereby authorize ProjectX, its employees, agents and others operating on its behalf, to use and/or disclose personal and health-related information about the patient, in order to provide the services to you, in accordance with our privacy policy, including, without limitation, the patient’s name, age, gender, mobile number, email address, delivery address, image of the insurance card, image of the prescription and other information you provide through the app that is either requested by ProjectX or volunteered by you (“personal information”). Please review the privacy policy carefully, as your use of the app constitutes your agreement to the privacy policy.\n" +
                "The user can place the order for supply of medicine through whatsapp messenger, mobile application and official website only otherwise it will not be considered a valid order for the purpose of supply/delivery. \n" +
                "\n" +
                "\n" +
                "Authorisation to contact you\n" +
                "You agree to be contacted by ProjectX at the mobile telephone number you provide for the purpose of confirming your prescription and scheduling the delivery of your prescribed medication. Standard telephone minute charges may apply if we contact you at a mobile number or device.\n" +
                "By using the services, you agree to receive email, and/or sms (text) message notifications containing personal information on your mobile device via an unsecure channel. This is your personal choice, and we encourage you to protect this information carefully. You should also check with your carrier on the price and package of sms messages available with your rate plan. Your carrier may impose message or charge limitations on your telecommunications account that are outside our control. All charges are billed by and payable to your telecommunications service provider.\n" +
                "Pricing and payment\n" +
                "All our product prices include all applicable statutory taxes, fees and subject to availability.\n" +
                "We reserve the right to correct any inaccuracies or omissions related to pricing and product availability/descriptions, even after you have submitted your order, and to change or update any other information at any time without prior notice.\n" +
                "The company does not accept liability for any errors in pricing, images and product description. Pricing, product information and availability is subject to change without notice. Fulfilment of orders is subject to availability of product at the time of processing the order.\n" +
                "The Mode of payment accepted is cash on delivery only. A delivery charge of ₹ 99 is applicable for all orders placed outside of Indore (MP). There are no delivery charges for orders placed within Indore (MP).\n" +
                "User responsibilities\n" +
                "You are responsible for your use of the website/ app and for all use of your personal information, including use by others to whom you have given access to your mobile phone. You may use the app and the services for lawful, non-commercial purposes only. You agree that you shall not (and you agree not to allow any third party to):\n" +
                "•\tCopy, modify, adapt, translate, or reverse engineer any portion of the website/app, its content or materials and/or the services;\n" +
                "•\tFraudulently misuse the services by submitting a prescription which you have no intention of obtaining or by submitting a fraudulent prescription or a prescription you are not authorized to obtain;\n" +
                "•\tCreate user accounts by automated means or under false or fraudulent pretenses; or\n" +
                "•\tUse the website/app to violate any law or third party right.\n" +
                "You are not permitted to host, display, upload, modify, publish, transmit, update or share any information that:\n" +
                "A)belongs to another person and to which you do not have any right;\n" +
                "B)is grossly harmful, harassing, blasphemous, defamatory, obscene, pornographic, paedophilic, libellous, invasive of another's privacy, hateful, or racially, ethnically objectionable, disparaging, relating or encouraging money laundering or gambling, or otherwise unlawful in any manner whatever;\n" +
                "C)harms minors in any way;\n" +
                "D)infringes any patent, trademark, copyright or other proprietary rights;\n" +
                "E)violates any law for the time being in force;\n" +
                "F)deceives or misleads the addressee about the origin of such messages or communicates any information which is grossly offensive or menacing in nature;\n" +
                "G)impersonate another person;\n" +
                "H)contains software viruses or any other computer code, files or programs designed to interrupt, destroy or limit the functionality of any computer resource;\n" +
                "I)threatens the unity, integrity, defence, security or sovereignty of india, friendly relations with foreign states, or public order or causes incitement to the commission of any cognisable offence or prevents investigation of any offence or is insulting any other nation.\n" +
                "\n" +
                "In addition to our rights in these terms of use, we may take any legal action to prevent the violation of this provision and to enforce these terms of use.\n" +
                "Change of terms of use\n" +
                "We may change these terms of use at any time, as we reasonably deem appropriate. Upon any change in these terms of use, we will publish the amended terms of use on our website and such changes shall be effective immediately upon posting. You acknowledge by using the website/app and/or the services following such posting shall constitute your affirmative acknowledgement of the terms of use, the modification, and agreement to abide and be bound by the terms of use, as amended. If at any time you choose not to accept these terms of use, including following any such modifications hereto, then please do not use our services.\n" +
                "Ownership of product\n" +
                "The content of the website/app, including without limitation, text, photographs and graphics, is owned by us and our licensors and is protected by copyright, trademark, patent, and trade secret laws, other proprietary rights, and international treaties. You acknowledge that the services and any underlying technology or software used in connection with the services contain ProjectX’s proprietary information. We give you permission to use the aforementioned content for personal, non-commercial purposes only and do not transfer any intellectual property rights to you by virtue of permitting your use of the services. \n" +
                "You hereby agree that you will not reproduce, duplicate or copy the content of ProjectX for any purpose, unless you have been specifically permitted to do so in a separate agreement with this website. \n" +
                " Disclaimer of warranties and limitation of liability\n" +
                "•\tAll information, products and services included on or otherwise made available to you through this website are provided by ProjectX on an \"as is\" and \"as available\" basis, either expressed or implied, we specifically disclaim warranties of any kind to the extent allowed by the applicable law. You expressly agree that your use of this website is at your sole risk.\n" +
                "\n" +
                "•\tProjectX shall not be responsible for any adverse, desirous or any other kind of harm in the body as they are mere suppliers of ‘already manufactured’ medicines under different brands and there shall be no guarantee for the effects after consumption of the medicines and the role of ProjectX would be only to supply medicines as per demand and such medicine is purchased by ProjectX in pre-sealed and pre-packed condition from the different manufactures of the medicine.\n" +
                "\n" +
                "•\tProjectX assumes no responsibility for any damages or viruses that may infect your computer equipment or other property on account of your access to, use of, or browsing in this site.\n" +
                "\n" +
                "•\tProjectX has exerted reasonable efforts to ensure that all information published on the website is accurate at the time of posting; however, there may be errors in such information for which we shall have no liability. We reserve the right to remove or alter any of the information contained on the website at our sole discretion.\n" +
                "\n" +
                "•\tProjectX cannot guarantee the adequacy, currency or completeness of the website content. Truemd does not warrant or endorse the effectiveness, quality or safety of the products available on its website. \n" +
                "\n" +
                "•\tWe disclaim responsibility for any harm to persons resulting from any instructions or products referred to in the website. ProjectX is not associated with any manufacturer of medicines or other products on the website. We do not warrant that the website, or its content will meet your requirements.\n" +
                "\n" +
                "•\tWe may let you view our information and communicate with us through the social media services such as facebook and twitter. Truemd explicitly disclaims any responsibility for the terms of use and privacy policies that govern these third-party websites, which are in no way associated with us.\n" +
                "\n" +
                "•\tWe are not liable for any adverse effects or harm to you as a result of your consumption of the medicines as prescribed by your physician.  We are a pharmacy only, and not responsible for improper or faulty indications that may be mentioned in the prescription itself including without limitation any case of wrong diagnosis or wrong dosage. \n" +
                "\n" +
                "•\tThe company ProjectX shall not be responsible for the effects of the medicines. Their limited responsibility would be to supply and facilitate the customer by providing/ delivering the medicines strictly as per the prescription of a qualified doctor supplied to the company by the customer. The company ProjectX shall not be liable for the supply of medicines based on the false or incorrect prescription supplied by the customer. \n" +
                "\n" +
                "•\tThe company is not under any obligation to verify the authenticity of any prescription supplied by the customer as it is presumed to be a genuine document for the purpose of buying the medicines. \n" +
                "\n" +
                "•\tProjectX reserves the right to modify or withdraw any part of the website or any of its content at any time without notice.\n" +
                "Indemnification\n" +
                "•\tYou agree to indemnify, defend and hold harmless each of the company, its officers, directors, employees, associates, partners or agents (each an “indemnified party”) from and against any and all losses, liabilities, claims, damages, demands, costs and expenses (including legal fees and disbursements in connection therewith and interest chargeable thereon) asserted against or incurred by such indemnified party in connection with, arising out of or resulting from (a) any misrepresentation or inaccuracy in any representation or warranty provided by you in these terms of use, (b) breach or non-performance of any covenant or obligation to be performed by you pursuant to these terms of use or (c) non-compliance of applicable laws by you. Further, you agree to hold us harmless against any claims made by any third party due to, or arising out of, or in connection with, your use of the website and/or mobile application.\n" +
                "•\tIn no event shall the company, its officers, directors, employees, associates, partners or agents be liable to you for any special, incidental, indirect, consequential or punitive damages whatsoever arising out of or in connection with your use of or access to the website and/or mobile application.\n" +
                "•\tThe limitations and exclusions in this section shall apply to the maximum extent permitted by applicable law.\n" +
                "\n" +
                "Termination\n" +
                "We may terminate and/or suspend your user account immediately, without notice, if there has been a violation of these terms of use by you or by someone using the app from your mobile device. We may also cancel or suspend your registration for any other reason, including inactivity for an extended period, but will attempt to notify you in advance of such cancellation or suspension. ProjectX shall not be liable to you or any third party for any termination of your access to the app and/or the services.\n" +
                "\n" +
                "Governing law and dispute resolution\n" +
                "•\tAll disputes pertaining to, arising out of or in connection with the use of the website and/or mobile application by you shall be referred to arbitration by a sole arbitrator to be appointed by the mutual consent of the company and yourself. The arbitration proceedings shall be conducted in accordance with the arbitration and conciliation act, 1996, as amended or restated from time to time.\n" +
                "•\tThe competent courts in Indore shall have exclusive jurisdiction in relation to all disputes pertaining to or arising out of these terms of use.\n" +
                "\n" +
                "Severability\n" +
                "If any provision of these terms and conditions shall be unlawful, void, or for any reason unenforceable, then that provision shall be deemed severable from these terms and conditions and shall not affect the validity and enforceability of any remaining provisions.  \n" +
                "Submissions\n" +
                "\n" +
                "•\tProjectX reserves sole discretion to accept, edit or reject any and all materials you may send to us (collectively, \"submissions\") as part of the features in the website, including but not limited to, ratings & reviews, ask our pharmacist your questions and testimonials. Such submissions should not be offensive on moral, religious, racial or political grounds or of an abusive, indecent, threatening, unlawful, obscene, defamatory, menacing or otherwise objectionable nature. Transmitting such offensive materials may violate relevant laws, regulations and ethics of pharmacy.  \n" +
                "\n" +
                "•\tProjectX shall be deemed to own all known and hereafter existing rights of every kind and nature regarding the submissions. By posting, uploading, inputting or providing your submissions, you hereby grant unrestricted use of the submissions for any purpose, without compensation to you, including a non-terminable, royalty-free and non-exclusive license to use, copy, distribute, transmit, publicly display, publicly perform, reproduce, edit, translate and reformat your submission; and to publish your name in connection with your submission. \n" +
                "\n" +
                "•\tYou also represent and warrant that your submissions will not infringe, misappropriate or violate a third party’s patent, copyright, trademark, trade secret, moral rights or other intellectual property rights, or rights of publicity or privacy. \n" +
                "\n" +
                "•\tThe user/customer shall be prohibited to introduce, post, or transmit any information or software, which contains a virus, worm or other harmful components into the ProjectX website.  \n" +
                "\n" +
                "•\tProjectX does not endorse and is not responsible for any submissions on its website. We will not be liable for any loss or harm caused by the submissions or your reliance on the information, including but not limited to, statements, opinions and reviews posted by the users/customers, which may be inaccurate, offensive, obscene, threatening or harassing.  \n" +
                "Website and/or mobile application availability\n" +
                "•\tThe company provides no representation or assurance that access to the website and/or mobile application will be uninterrupted or error free.\n" +
                "•\tThe company assumes no responsibility, and shall not be liable for, any damages caused by viruses, trojans or other forms of malware, adware and other malicious programs that may infect your computer system, mobile phone or any hardware or software used by you to access or use the website and/or mobile application, or your account on the website and/or mobile application and which may have an adverse impact on your experience of browsing the website and/or mobile application. If you are dissatisfied with the website and/or mobile application, your sole remedy is to discontinue using the website and/or mobile application. You are expected to use adequate anti-virus software and firewalls in your device to guard against possible attacks by malicious software.\n" +
                "•\tThe company shall not be responsible for any delays or failures you may have in initiating, conducting or completing any transmissions or transactions in connection with the purchase of any product from the website and/or mobile application.\n" +
                "•\tAny information disclosed or uploaded by you is at your own risk. The company shall not be responsible in any manner for any direct, indirect, special or consequential damages, howsoever caused arising out of any loss of such information provided to the company.\n" +
                "•\tThe company reserves the right to modify, suspend or withdraw the whole or any part of the website and/or mobile application or any of its contents at any time without any notice or liability.\n" +
                "Prescription medication policy\n" +
                "•\tProjectX will not dispense any prescription medication without a valid prescription from a licensed physician. ProjectX may, at its discretion, share your prescription and other pertinent information with a third-party network of certified medical doctors.\n" +
                "\n" +
                "•\tIf you are ordering prescription medication(s), you hereby confirm that you will send us a scanned copy of your valid prescription(s), and this prescription shall then be subject to the scrutiny of and approval by our qualified pharmacists.  \n" +
                "\n" +
                "•\tThe drug information provided in the ProjectX website/app is for informative purposes only and this website is not intended to provide diagnosis, treatment or medical advice. We are not liable for any adverse effects or harm to you as a result of your reliance on the information in the website.  \n" +
                "\n" +
                "•\tProjectX requires either the user or customer or the caregiver to confirm he/she is completely aware of the indications, side effects, drug interactions, effects of missed dose or overdose of the medicines he/she orders from us. It is imperative to seek professional advice from your physician before purchasing or consuming any medicine from ProjectX.\n" +
                "Digital prescriptions\n" +
                "•\tWe provide you with the facility of digitizing and storing your health records for your ease of reference. However, the digitized version of the original prescription may/may not contain entire or accurate content/information. Please refer to the original prescription for the entire, accurate content/information.\n" +
                "•\tThis facility is provided to you free of cost and does not amount to a “service” provided by us and does not create a “service provider”-“consumer” relationship between the company and yourself.\n" +
                "•\tThe electronic maintenance of your health records is subject to the privacy policy.\n" +
                "\n" +
                "Delivery and returns\n" +
                "•\tIt is your obligation to enter the correct delivery address details at the time of placing an order. If you enter the wrong address, we are not obliged to re-send the order to the correct address at our expense. We will provide you with an estimated date for delivery of all parcels and we will make reasonable commercial efforts to complete the delivery by the estimated date. However, please note that the estimated dates displayed at the time of placing your order are estimates and we shall not be liable if the products purchased are not delivered by such dates.\n" +
                "•\tIt is the responsibility of the customer to promptly inform the company if an order does not arrive. Once we find out an order has not arrived by the due date, we will lodge enquiries with the relevant concerned department/ logistic provider responsible for delivering the product.\n" +
                "•\tReturns shall be accepted only in case of wrong medicines or expired medicines delivered. ProjectX shall verify such a claim and only after confirmation of genuineness of claim shall the return be set into motion.\n" +
                "Terms of sale\n" +
                "•\tProjectX may accept or decline any order placed by a customer in its absolute discretion without liability to you.\n" +
                "•\tProjectX reserves the right to discontinue any program or offer on its website.\n" +
                "•\tWe reserve the right, without prior notice, to limit the order quantity of any products available on ProjectX.\n" +
                "\n" +
                "We acknowledge and you agree that you have fully and accurately disclosed your personal information and personal health information and consent to its use by the pharmacy ProjectX and/or its affiliates. You confirm that you have had a physical examination by a physician and do not require a physical examination.\n" +
                "\n" +
                "You understand that all products shall be sold & dispensed by a registered pharmacist at ProjectX and/or its affiliates.\n" +
                "If you are the parent/legal guardian/authorised person for the patient disclosed herein, you affirm that you are over the age of majority, and have full authority to sign for and provide the above representations to the pharmacy TreuMD and/or its affiliates on the patient's behalf.\n" +
                "Note:\n" +
                "These terms of use and any supplemental terms, policies, rules and guidelines on the app, including the privacy policy, constitute the entire agreement between you and us and supersede all previous written or oral agreements. If any part of these terms of use is held invalid or unenforceable, that portion shall be construed in a manner consistent with applicable law to reflect, as nearly as possible, the original intentions of the parties, and the remaining portions shall remain in full force and effect. The failure of ProjectX to exercise or enforce any right or provision of these terms of use shall not constitute a waiver of such right or provision. The failure of either party to exercise in any respect any right provided for herein shall not be deemed a waiver of any further rights hereunder.\n" +
                "\n";

        txth.setText(heading);
        txt.setText(r);



        final Dialog mBottomSheetDialog = new Dialog (SignupActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


    }

    @Override
    public void onBackPressed(){

        finish();


//        Intent signUpToLogin = new Intent(getApplicationContext(), LoginActivity.class);
//
//        startActivityForResult(signUpToLogin, 0);

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            {
                //onSignupFailed("Please enter the details correctly");
                return;
            }




        }

        termstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open bottom sheet with web view.
            }
        });

        signupButton.setEnabled(false);

        pDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Creating Account...");
        //pDialog.show();

        String name = nameText.getText().toString();
        String email =emailText.getText().toString();
        String password = passwordText.getText().toString();





        // TODO: Implement your own signup logic here.

       // LoginActivity.checkInternetNotLoggedIn(this);

      registerRequest(name, email, password);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        //onSignupSuccess();
                        // onSignupFailed();
                        //pDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess(String otpToken) {

        signupButton.setEnabled(true);
        hidepDialog();
        Paper.book("RegisterPassword").write("password",passwordText.getText().toString());
        Toast.makeText(getApplicationContext(), "Sign Up successful, Verifying mobile number now", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK, null);

        Intent signUpToSms = new Intent(getApplicationContext(), SmsActivity.class);
        signUpToSms.putExtra("otp_token", otpToken);
        startActivityForResult(signUpToSms, 0);
    }

    public void onSignupFailed(String messages) {
        signupButton.setEnabled(true);
        hidepDialog();
        //Toast.makeText(getApplicationContext(), "Sign Up failed, Try Again !!", Toast.LENGTH_SHORT).show();
        if (messages.contains("Email is")) {
            Toast.makeText(getApplicationContext(), "Uh-oh… Looks like this Mobile Number is already registered!", Toast.LENGTH_SHORT).show();

        }
        else
            Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_SHORT).show();


//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);


    }
    public void onHasAccountAlready(String name) {
        Toast.makeText(getApplicationContext(), name+ " already has account. Try Logging in.", Toast.LENGTH_SHORT).show();

        signupButton.setEnabled(true);

        hidepDialog();

        setResult(RESULT_OK, null);

        Intent signUpToLogin = new Intent(getApplicationContext(), LoginActivity.class);
        signUpToLogin.putExtra("mobile", emailText.getText().toString().trim());
        startActivityForResult(signUpToLogin, 0);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();



        if (email.length()!=10) {
            emailText.setError("enter a valid mobile number");
            valid = false;
        } else {
            String regEx = "^[0-9]{10}$";
            valid =  email.matches(regEx);
            emailText.setError(null);
        }


        if (password.length() < 8 || password.length() > 20) {
            passwordText.setError("Password should be a minimum of 8 chars.");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (name.length() < 3) {
            nameText.setError("name should be at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (checkbox.isChecked()==false) {
            onSignupFailed("Please agree to the terms and conditions.");
            valid = false;
        } else {

        }

        return valid;
    }


    /**
     * Method to make json object request where json response starts wtih {
     */

    private void showpDialog() {
        signupProgress.showNow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hidepDialog() {
       signupProgress.hideNow();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

    public void registerRequest(String name, String mobile, String password){

        ContentValues codecodepair = new ContentValues();
        codecodepair.put("mobile", mobile);
        codecodepair.put("password", password);
        passwordSignup = password;
        String user_type = "patient";
        codecodepair.put("name", name);
        codecodepair.put("user_type",user_type);


        try {
            JSONObject couponJsonObject = getJsonObjectFromContentValues("user", codecodepair);
            //HashMap<String, String> user = session.getUserDetails();

            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(context)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .timeOut(50000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    //.addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("Content-Type", "application/json")
                    //.addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToRegister()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.POST, signupURL, couponJsonObject);




        } catch (JSONException e) {
            Log.e("LoginError:: ", e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            Log.e("LoginError:: ", e.getMessage());
            e.printStackTrace();
        }


    }


    class JsonObjectListenerToRegister extends com.alirezaafkar.json.requester.interfaces.Response.SimpleObjectResponse {

        String authentication_token_new, name_new,status_new, user_status_new, mobile_new, password_new;
        boolean success;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length()>3) {

                    Log.e("Siri resp: ", jsonObject.toString());
                    success = true;

                    JSONObject state = jsonObject.getJSONObject("state");
                    int code = state.getInt("code");

                    String messages = state.getString("message");

                    //txtResponse.setText(jsonResponse);

                    if(code==1)
                    {
                        Log.e("RegisterCode:: ", ""+code);

                        JSONObject data = jsonObject.getJSONObject("user");
                        String name= data.getString("name");
                        String authentication_token = data.getString("authentication_token");
                        String otp_token = data.getString("otp_token");

                        jsonResponse = "";
                        jsonResponse += "code: " + code + "\n\n";
                        jsonResponse += "messages: " + messages + "\n\n";
                        jsonResponse += "name: " + name + "\n\n";
                        jsonResponse += "token: " + authentication_token + "\n\n";

                        Log.e("RegisterResp:: ", jsonResponse);

                        String status = data.getString("status");
                        if(status.equalsIgnoreCase("unverified"))
                            onSignupSuccess(otp_token);
                        else if (status.equalsIgnoreCase("verified"))
                            onHasAccountAlready(name);

                    }
                    else if(code==0)
                    {
                        jsonResponse = ""+code+messages;
                        Log.e("RegisterCode:: ", ""+code);
                        Log.e("RegisterResp:: ", jsonResponse);
                        onSignupFailed(messages);
                    }
                    else {
                        jsonResponse = ""+code+messages;
                        Log.e("RegisterResp:: ", jsonResponse);
                        onSignupFailed(messages);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                String message = e.getMessage();
                Log.e("LoginError:: ",  message );
                Intent toLogin = new Intent(context, PreLoginActivity.class);
                startActivity(toLogin);
            }
        }


        @Override
        public void onRequestStart(int requestCode) {
            showpDialog();
            //LoginActivity.checkInternetNotLoggedIn(context);


        }

        public String trimMessage(String json, String key){
            String trimmedString = null;

            try{
                JSONObject obj = new JSONObject(json);
                trimmedString = obj.getString(key);
            } catch(JSONException e){
                e.printStackTrace();
                return null;
            }

            return trimmedString;
        }

        @Override
        public void onRequestFinish(int requestCode) {

        }
        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());
            onSignupFailed("Uh-oh… There might be a network issue.");

            hidepDialog();

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            String json = null;

            hidepDialog();
            NetworkResponse response = volleyError.networkResponse;
            if(response != null && response.data != null){
                switch(response.statusCode){
                    case 400:
                        json = new String(response.data);
                        json = trimMessage(json, "message");
                        if(json != null)  Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
//
                        break;
                    case 401:
                        json = new String(response.data);
                        json = trimMessage(json, "message");
                        if(json != null)  Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
//
                        break;
                    case 500:
                        json = new String(response.data);
                        json = trimMessage(json, "message");
                        if(json != null)  Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
//
                        break;

                }
                //Additional cases
            }
//            if (ProjectXJSONUtils.goThroughNullCheck(message).contains("401")||volleyError.getMessage().contains("401"))
//            {
//                Log.e("Signup:: ", "401"+ message + " : "+ volleyError.getMessage());
//                Toast.makeText(context, "You might have entered the wrong password.", Toast.LENGTH_SHORT).show();
//
//            }
//            else
//            {
//                Log.e("Signup:: ", "401");
//                Toast.makeText(context, message+" : "+ volleyError.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }

        }

    }

}