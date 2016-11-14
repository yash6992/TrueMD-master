package com.truemdhq.projectx.activity;

import android.content.Context;

import android.os.Handler;
import android.support.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.adapter.CustomClientAdapter;
import com.truemdhq.projectx.model.Client;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yashvardhansrivastava on 19/01/16.
 */
public class ClientListActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @OnClick(R.id.btn_back)
    public void onClick1(ImageView imageView){
        onBackPressed();
    }

    @BindView(R.id.list)
    ListView mClientListView;
    @BindView(R.id.refresh_layout)
    CircleRefreshLayout crl;
    @BindView(R.id.add_client_bar)
    RelativeLayout addClient;
    @OnClick(R.id.add_client_bar)
    public void onClick3(RelativeLayout imageView){
        onAddClientPressed();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_client_list_projectx);

        ButterKnife.bind(this);

        List<Client> clients = null;
        try {

            String sJSONObjectHavingJSONArray = "{\"clients\":"+ Hawk.get("clientList")+"}";
            JSONObject jsonObjectHavingJSONArray = new JSONObject(sJSONObjectHavingJSONArray);

            JSONArray clientJSONArray = jsonObjectHavingJSONArray.getJSONArray("clients");

            Log.e("ClientList: ",""+clientJSONArray.toString());
            clients = new ArrayList<>();
            for(int i =0; i<clientJSONArray.length();i++){
                ObjectMapper objectMapper = new ObjectMapper();
                Client client = objectMapper.readValue(clientJSONArray.get(i).toString(), Client.class);
                Log.e("ClientList: "+i,": "+client.getVendorName());
                clients.add(client);
            }
        } catch (Exception e) {
            Log.e("Hawk:",""+e.getMessage());

            e.printStackTrace();
        }

        Log.e("ClientList: ",""+clients.size());

        mClientListView.setAdapter(new CustomClientAdapter(ClientListActivity.this, clients ));


        crl.setOnRefreshListener(
                new CircleRefreshLayout.OnCircleRefreshListener() {
                    @Override
                    public void refreshing() {
                        // do something when refresh starts
                        loopRefresh();
                    }

                    @Override
                    public void completeRefresh() {
                        // do something when refresh complete
                    }
                });

    }





    @Override
    public void onBackPressed(){
        finish();
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
    public void onAddClientPressed(){
        Intent nextActivity = new Intent(ClientListActivity.this, AddClientActivity.class);
        startActivity(nextActivity);
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
    public void loopRefresh(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                crl.finishRefreshing();
            }
        }, 1);




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {

        }
    }



}
