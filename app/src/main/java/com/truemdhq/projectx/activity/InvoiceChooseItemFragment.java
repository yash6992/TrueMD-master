package com.truemdhq.projectx.activity;

/**
 * Created by yashvardhansrivastava on 15/11/16.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.adapter.CustomClientAdapter;
import com.truemdhq.projectx.adapter.CustomItemAdapter;
import com.truemdhq.projectx.model.Client;
import com.truemdhq.projectx.model.Item;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class InvoiceChooseItemFragment extends Fragment{


    @BindView(R.id.list_item)
    ListView itemListView;

    Unbinder unbinder;

    public InvoiceChooseItemFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_invoice_choose_item, container, false);

        unbinder = ButterKnife.bind(this, view);
        List<Item> items = null;
        try {

            String sJSONObjectHavingJSONArray = "{\"items\":"+ Hawk.get("itemList")+"}";
            JSONObject jsonObjectHavingJSONArray = new JSONObject(sJSONObjectHavingJSONArray);

            JSONArray itemJSONArray = jsonObjectHavingJSONArray.getJSONArray("items");

            Log.e("ItemList: ",""+itemJSONArray.toString());
            items = new ArrayList<>();
            for(int i =0; i<itemJSONArray.length();i++){
                ObjectMapper objectMapper = new ObjectMapper();
                Item item = objectMapper.readValue(itemJSONArray.get(i).toString(), Item.class);
                Log.e("ItemList: "+i,": "+item.getItemName());
                items.add(item);
            }
        } catch (Exception e) {
            Log.e("Hawk:",""+e.getMessage());

            e.printStackTrace();
        }

        Log.e("ItemList: ",""+items.size());

        itemListView.setAdapter(new CustomItemAdapter(getActivity(), items ));


        // Inflate the layout for this fragment
        return view;
    }

}