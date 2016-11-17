package com.truemdhq.projectx.activity;

/**
 * Created by yashvardhansrivastava on 15/11/16.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.model.Client;
import com.truemdhq.projectx.model.Invoice;
import com.truemdhq.projectx.model.Item;
import com.truemdhq.projectx.views.EditTextProjectX;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class InvoiceAddItemFragment extends Fragment{


    @BindView(R.id.nameET)    EditTextProjectX name;
    @BindView(R.id.rateET) EditTextProjectX rate;
    @BindView(R.id.quantityET) EditTextProjectX quantity;

    @BindView(R.id.btn_bottom)
    RelativeLayout btnBottom;
    @OnClick(R.id.btn_bottom)
    void onClick(RelativeLayout view){
            checkValidAndProceed();
    }

    JSONArray itemJSONArray;

    Invoice invoiceDraft;

    public InvoiceAddItemFragment() {
        // Required empty public constructor


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private Unbinder unbinder;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice_add_item, container, false);
        unbinder = ButterKnife.bind(this, view);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        try {

            String sJSONObjectHavingJSONArray = "{\"items\":"+ Hawk.get("itemList")+"}";
            JSONObject jsonObjectHavingJSONArray = new JSONObject(sJSONObjectHavingJSONArray);

            itemJSONArray = jsonObjectHavingJSONArray.getJSONArray("items");

        } catch (Exception e) {
            Log.e("Hawk:",""+e.getMessage());

            e.printStackTrace();
        }

        name.edit_text.setHint("Name");
        name.edit_text.setMaxLines(1);
        name.edit_text.setInputType(InputType.TYPE_CLASS_TEXT);

        rate.edit_text.setHint("Rate");
        rate.edit_text.setMaxLines(1);
        rate.edit_text.setInputType(InputType.TYPE_CLASS_NUMBER);

        quantity.edit_text.setHint("Quantity");
        quantity.edit_text.setText("1");
        quantity.edit_text.setMaxLines(1);
        quantity.edit_text.setInputType(InputType.TYPE_CLASS_NUMBER);


        return view;
    }

    public void checkValidAndProceed(){

        String sname, srate, squantity;
        sname = name.edit_text.getText().toString();
        srate = rate.edit_text.getText().toString();
        squantity = quantity.edit_text.getText().toString();

        if(sname.length()<2)
        {
            name.setError("Item name should be longer than 3 characters");
        }
        else if (srate.length()<1)
        {
            rate.setError("Address should be longer than 5 characters");
        }
        else if (squantity.length()<1)
        {
            quantity.setError("TIN should be longer than 8 Digits");
        }
        else
        {
            name.allFine();rate.allFine();
            quantity.allFine();

            double q = Double.parseDouble(squantity);
            double r = Double.parseDouble(srate);
            double a = q*r;

            double vat = Double.parseDouble("1.14");
            double tax1 = Double.parseDouble("1.05");
            double tax2 = Double.parseDouble("1.10");

            boolean v = true;
            boolean t1 = false;
            boolean t2 = false;
            boolean p = true;

            Item newItemForList = null; Item newItem = null;
            try {

                newItemForList = new Item("", 1.0d, r, r, sname, "", vat, tax1, tax2, v, t1, t2, p );

            } catch (Exception e) {
                Log.e("addItem: ",""+e.getMessage());
                e.printStackTrace();
            }

            ObjectMapper mapper = new ObjectMapper();

            try {
                String jsonItem1 = mapper.writeValueAsString(newItemForList);
                JSONObject itemJSONObject1 = new JSONObject(jsonItem1);

                itemJSONArray.put(itemJSONObject1);

                Hawk.put("itemList", itemJSONArray.toString());


                newItem = new Item("", q, r, a, sname, "", vat, tax1, tax2, v, t1, t2, p );


                List<Item> items = ChooseInvoiceItemActivity.invoiceDraft.getItems();

                items.add(newItem);

                ChooseInvoiceItemActivity.invoiceDraft.setItems(items);

                String jsonInvoiceDraft = mapper.writeValueAsString(ChooseInvoiceItemActivity.invoiceDraft);
                Hawk.put("invoiceDraft",jsonInvoiceDraft);



                Intent newIntent = new Intent(getActivity(), InvoiceCreateActivity.class);
                startActivityForResult(newIntent,3);
                //push from top to bottom
                //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                //slide from left to right
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                getActivity().finish();



            } catch (Exception e) {
                Log.e("AddClientJackson: ",""+e.getMessage());
                e.printStackTrace();
            }

        }




    }


    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }




}