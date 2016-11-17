package com.truemdhq.projectx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.model.Client;
import com.truemdhq.projectx.views.EditTextProjectX;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yashvardhansrivastava on 13/11/16.
 */
public class AddClientActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @OnClick(R.id.btn_back)
    public void onClick1(ImageView imageView){
        onBackPressed();
    }

    @BindView(R.id.btn_bottom)
    RelativeLayout btnBottom;
    @OnClick(R.id.btn_bottom)
    public void onClick2(RelativeLayout imageView){
        checkValidAndProceed();
    }

    @BindView(R.id.phoneNoET)
    EditTextProjectX phoneNo;
    @BindView(R.id.emailET)
    EditTextProjectX email;
    @BindView(R.id.addressET)
    EditTextProjectX address;
    @BindView(R.id.nameET)
    EditTextProjectX name;
    @BindView(R.id.taxET)
    EditTextProjectX tax;


    JSONArray clientJSONArray;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_client_projectx);

        ButterKnife.bind(this);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        try {

            String sJSONObjectHavingJSONArray = "{\"clients\":"+ Hawk.get("clientList")+"}";
            JSONObject jsonObjectHavingJSONArray = new JSONObject(sJSONObjectHavingJSONArray);

             clientJSONArray = jsonObjectHavingJSONArray.getJSONArray("clients");

        } catch (Exception e) {
            Log.e("Hawk:",""+e.getMessage());

            e.printStackTrace();
        }

        name.edit_text.setHint("Name");
        name.edit_text.setMaxLines(1);
        name.edit_text.setInputType(InputType.TYPE_CLASS_TEXT);

        address.edit_text.setHint("Address");
        address.edit_text.setMaxLines(4);

        tax.edit_text.setHint("TIN");
        tax.edit_text.setMaxLines(1);
        tax.edit_text.setInputType(InputType.TYPE_CLASS_NUMBER);


        email.edit_text.setHint("Email address");
        email.edit_text.setMaxLines(1);
        email.edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        phoneNo.edit_text.setHint("Phone No");
        phoneNo.edit_text.setInputType(InputType.TYPE_CLASS_PHONE);
        phoneNo.edit_text.setMaxLines(1);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(10);
        phoneNo.edit_text.setFilters(FilterArray);


    }


    public void checkValidAndProceed(){
        String regExP = "^[0-9]{10}$";
        String sname, saddress, stax, sphone, semail;
        sname = name.edit_text.getText().toString();
        saddress = address.edit_text.getText().toString();
        stax = tax.edit_text.getText().toString();
        sphone = phoneNo.edit_text.getText().toString();
        semail = email.edit_text.getText().toString();

        if(sname.length()<3)
             {
            name.setError("Name should be longer than 3 characters");
                }
        else if (saddress.length()<5)
        {
            address.setError("Address should be longer than 5 characters");
        }
        else if (stax.length()<8)
        {
            tax.setError("TIN should be longer than 8 Digits");
        }

       else if (sphone.length()!=10 || !(sphone.matches(regExP)))
        {
            phoneNo.setError("Enter a valid mobile number");
        }
        else if (!validateEmail(semail))
        {
            email.setError("Enter a valid email address");
        }
        else
        {
            name.allFine();email.allFine();
            phoneNo.allFine();tax.allFine();
            address.allFine();

            Client newClient = new Client("", sname, saddress, saddress, semail, sphone, stax);

            ObjectMapper mapper = new ObjectMapper();

            try {
                String jsonClient1 = mapper.writeValueAsString(newClient);
                JSONObject clientJSONObject1 = new JSONObject(jsonClient1);

                clientJSONArray.put(clientJSONObject1);

                Hawk.put("clientList", clientJSONArray.toString());

                Intent newIntent = new Intent(AddClientActivity.this, ClientListActivity.class);
                startActivity(newIntent);
                //push from top to bottom
                //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                //slide from left to right
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                AddClientActivity.this.finish();



            } catch (Exception e) {
                Log.e("AddClientJackson: ",""+e.getMessage());
                e.printStackTrace();
            }

        }




    }

    public boolean validateEmail(final String hex) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



        pattern = Pattern.compile(EMAIL_PATTERN);

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }


    @Override
    public void onBackPressed(){
        finish();
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
