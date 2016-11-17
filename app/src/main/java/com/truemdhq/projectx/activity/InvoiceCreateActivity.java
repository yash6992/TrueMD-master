package com.truemdhq.projectx.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.model.Client;
import com.truemdhq.projectx.model.Invoice;
import com.truemdhq.projectx.model.InvoiceParent;
import com.truemdhq.projectx.model.Item;
import com.truemdhq.projectx.views.TextViewFont2Medium;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yashvardhansrivastava on 11/11/16.
 */
public class InvoiceCreateActivity extends AppCompatActivity {

    @BindView(R.id.vendor_name)    TextView vendorName;
    @BindView(R.id.vendor_address)    TextView vendorAddress;
    @BindView(R.id.vendor_contact)    TextView vendorContact;
    @BindView(R.id.client_detail)    LinearLayout clientDetail;

    @BindView(R.id.invoice_date_)    TextView invoicedate;
    @BindView(R.id.invoice_due_in_)    TextView invoiceduein;

    @BindView(R.id.no_items_tv)    TextView noOfItems;

    @BindView(R.id.item_list) LinearLayout itemListLL;



    @BindView(R.id.detail)
    RelativeLayout detail;


    @BindView(R.id.btn_back)
    ImageView btnBack;
    @OnClick(R.id.btn_back)
    public void onClick1(ImageView imageView){
        onBackPressed();
    }

    @BindView(R.id.add_item)
    RelativeLayout addItem;
    @OnClick(R.id.add_item)
    public void onClick3(RelativeLayout imageView){
        onAddItemPressed();
    }

    @BindView(R.id.client_choose)
    RelativeLayout clientChoose;
    @OnClick(R.id.client_choose)
    public void onClick4(RelativeLayout imageView){
        onClientChoosePressed();
    }

    @BindView(R.id.detail_choose)
    RelativeLayout detailChoose;
    @OnClick(R.id.detail_choose)
    public void onClick5(RelativeLayout imageView){
        onDateChoosePressed();
    }


    @OnClick(R.id.client_detail)
    public void onClick6(RelativeLayout imageView){
        onClientChoosePressed();
    }


    @OnClick(R.id.detail)
    public void onClick7(RelativeLayout imageView){
        onDateChoosePressed();
    }




    @BindView(R.id.btn_bottom)
    RelativeLayout btnBottom;
    @OnClick(R.id.btn_bottom)

    public void onClick2(RelativeLayout relativeLayout){
        Intent nextActivity = new Intent(InvoiceCreateActivity.this, InvoiceViewActivity.class);


        nextActivity.putExtra("invoice", 0);

        startActivity(nextActivity);
        //push from bottom to top
        //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        //slide from right to left
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    JSONArray clientJSONArray, itemJSONArray, invoiceJSONArray;
    View rowView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_invoice_projectx);
        ButterKnife.bind(this);

        LayoutInflater inflater = getLayoutInflater();

        try {

            Intent i=getIntent();


            Log.e("CreateInvoiceCalling: ",""+this.getCallingActivity().getClassName());


            String sJSONObjectHavingJSONArray1 = "{\"clients\":"+ Hawk.get("clientList")+"}";
            JSONObject jsonObjectHavingJSONArray1 = new JSONObject(sJSONObjectHavingJSONArray1);

            clientJSONArray = jsonObjectHavingJSONArray1.getJSONArray("clients");

            String sJSONObjectHavingJSONArray2 = "{\"items\":"+ Hawk.get("itemList")+"}";
            JSONObject jsonObjectHavingJSONArray2 = new JSONObject(sJSONObjectHavingJSONArray2);

            itemJSONArray = jsonObjectHavingJSONArray2.getJSONArray("items");

            String sJSONObjectHavingJSONArray3 = "{\"invoices\":"+ Hawk.get("invoiceList")+"}";
            JSONObject jsonObjectHavingJSONArray3 = new JSONObject(sJSONObjectHavingJSONArray3);

            invoiceJSONArray = jsonObjectHavingJSONArray3.getJSONArray("invoices");



            if(Hawk.get("invoiceDraft").toString().length()>5){


                ObjectMapper objectMapper = new ObjectMapper();
                Invoice invoiceDraft = objectMapper.readValue(Hawk.get("invoiceDraft").toString(), Invoice.class);

                if(invoiceDraft.getClient()!=null){
                    vendorAddress.setText(invoiceDraft.getClient().getVendorBillingAddress());
                    vendorName.setText(invoiceDraft.getClient().getVendorName());
                    vendorContact.setText(invoiceDraft.getClient().getVendorPhoneNo()+" | "+invoiceDraft.getClient().getVendorEmail());
                    clientChoose.setVisibility(View.INVISIBLE);
                    clientDetail.setVisibility(View.VISIBLE);
                }
                else{
                    Log.e("InvoiceCreate: ","Unable to get client.");
                }


                if(invoiceDraft.getInvoiceDueDate()!=null){
                    invoicedate.setText(invoiceDraft.getInvoiceDate());
                    invoiceduein.setText("due in "+invoiceDraft.getInvoiceDueDate());
                    detailChoose.setVisibility(View.INVISIBLE);
                    detail.setVisibility(View.VISIBLE);
                }
                else{
                    Log.e("InvoiceCreate: ","Unable to get date.");
                }

                if (invoiceDraft.getItems()!=null)
                {
                    if (invoiceDraft.getItems().size()==1)
                         noOfItems.setText("1 item selected");
                    else if (invoiceDraft.getItems().size()==0)
                        noOfItems.setText("no item selected");
                    else
                        noOfItems.setText(invoiceDraft.getItems().size()+" items selected");

                    List<Item> items = invoiceDraft.getItems();
                    for (Item item: items) {
                        Log.d("CreateInvoice: ", "Adding item: " + item.getItemName());

                        rowView = inflater.inflate(R.layout.list_item_invoice_item_projectx, null);

                        TextView itemName = (TextView) rowView.findViewById(R.id.item_name);
                        TextView itemBarcode = (TextView) rowView.findViewById(R.id.item_barcode);
                        TextViewFont2Medium itemAmount = (TextViewFont2Medium) rowView.findViewById(R.id.item_amount);
                        ImageView productOrService = (ImageView) rowView.findViewById(R.id.product_or_service);

                        if(item.isProduct())
                        {
                            itemAmount.setText(item.getItemAmount(),2);
                            itemBarcode.setText("ISBN"+item.getItemBarcode());
                            itemName.setText(""+item.getItemName()+" ("+item.getItemQuantity()+" Nos.)");

                        }
                        else{
                            itemAmount.setText(item.getItemAmount(),2);
                            itemBarcode.setText("ISBN"+item.getItemBarcode());
                            itemName.setText(""+item.getItemName()+" ("+item.getItemQuantity()+" Hours)");

                        }


                        itemListLL.addView(rowView);
                    }
                }
                else
                {
                    Log.e("InvoiceCreate: ","Unable to get items.");

                }





            }



        } catch (Exception e) {
            Log.e("Hawk:",""+e.getMessage());

            e.printStackTrace();
        }







    }
    @Override
    public void onBackPressed(){
        finish();
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
    public void onAddItemPressed(){
        Intent nextActivity = new Intent(InvoiceCreateActivity.this, ChooseInvoiceItemActivity.class);
        startActivity(nextActivity);
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();

    }

    public void onClientChoosePressed(){
        Intent nextActivity = new Intent(InvoiceCreateActivity.this, ChooseClientActivity.class);
        startActivity(nextActivity);
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();

    }
    public void onDateChoosePressed(){
        Intent nextActivity = new Intent(InvoiceCreateActivity.this, ChooseInvoiceDateActivity.class);
        startActivity(nextActivity);
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();

    }

}
