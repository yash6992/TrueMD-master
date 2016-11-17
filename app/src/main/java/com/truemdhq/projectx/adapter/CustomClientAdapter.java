package com.truemdhq.projectx.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.activity.ChooseClientActivity;
import com.truemdhq.projectx.activity.ClientListActivity;
import com.truemdhq.projectx.activity.InvoiceCreateActivity;
import com.truemdhq.projectx.activity.InvoiceViewActivity;
import com.truemdhq.projectx.activity.UpdateClientActivity;
import com.truemdhq.projectx.model.Client;
import com.truemdhq.projectx.model.Invoice;
import com.truemdhq.projectx.model.Item;
import com.truemdhq.projectx.views.TextViewFont2Medium;
import com.truemdhq.projectx.views.TextViewFont3Large;

import java.util.List;

/**
 * Created by yashvardhansrivastava on 23/04/16.
 */
public class CustomClientAdapter extends BaseAdapter {

    List<Client> result1;
    Invoice invoiceDraft;
    Context context;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomClientAdapter(Context c1, List<Client> prgmNameList) {
        // TODO Auto-generated constructor stub
        result1=prgmNameList;

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            invoiceDraft = objectMapper.readValue(Hawk.get("invoiceDraft").toString(), Invoice.class);


        }
        catch (Exception e){
            Log.e("DateError: ",""+e.getMessage());
        }


        context=c1;
        //imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result1.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }




    public class Holder
    {
        TextView vendorName;
        TextView vendorEmail;
        TextViewFont3Large vendorAmount;

        LinearLayout edit;

        Holder(View rowView){
            vendorName = (TextView) rowView.findViewById(R.id.invoice_no_h);
            vendorEmail = (TextView) rowView.findViewById(R.id.vendor_name);
            vendorAmount = (TextViewFont3Large) rowView.findViewById(R.id.balance_due);
            edit = (LinearLayout) rowView.findViewById(R.id.edit);

        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = convertView;

        Holder holder = null;

        if(rowView==null)
        {
            rowView = inflater.inflate(R.layout.list_item_client_projectx,parent, false);
            holder = new Holder(rowView);
            rowView.setTag(holder);
        }
        else{
            holder = (Holder) rowView.getTag();
        }

        final Client client = (Client) result1.get(position);



            Log.e("InvoiceItem: ","item: " + client.getVendorName());

            try {

                    holder.vendorName.setText(client.getVendorName());
                    holder.vendorEmail.setText(client.getVendorEmail());

                    // holder.img.setImageResource(imageId[position]);
                    rowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Activity activity = (Activity) context;

                            Log.e("ClientAdapter: ","rowClick: "+activity.getLocalClassName());

                            if(activity.getLocalClassName().equals("activity.ClientListActivity"))
                            {
                                Log.e("ClientAdapter: ","ClientListActivity");
                                //Intent nextActivity = new Intent(context, ClientViewActivity.class);
                                //nextActivity.putExtra("item", position);
                                //activity.startActivity(nextActivity);
                                //push from bottom to top
                                //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                                //slide from right to left
                                //activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            }
                            else if(activity.getLocalClassName().equals("activity.ChooseClientActivity"))
                            {
                                Log.e("ClientAdapter: ","ChooseClientActivity");

                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    invoiceDraft.setClient(client);
                                    String jsonInvoiceDraft = mapper.writeValueAsString(invoiceDraft);
                                    Hawk.put("invoiceDraft",jsonInvoiceDraft);
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }


                                Intent nextActivity = new Intent(context, InvoiceCreateActivity.class);
                                nextActivity.putExtra("clientPosition", position);
                                activity.startActivityForResult(nextActivity, 1);
                                //push from bottom to top
                                //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                                //slide from right to left
                                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                activity.finish();

                            }



                        }
                    });

                    holder.edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Activity activity = (Activity) context;

                            Log.e("ClientAdapter: ","editClick: "+activity.getLocalClassName());

                            if(activity.getLocalClassName().equals("activity.ClientListActivity"))
                            {
                                Log.e("ClientAdapter: ","ClientListActivity");
                                Intent nextActivity = new Intent(context, UpdateClientActivity.class);
                                nextActivity.putExtra("clientPosition", position);
                                //activity.startActivity(nextActivity);
                                //push from bottom to top
                                //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                                //slide from right to left
                                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            }
                            else if(activity.getLocalClassName().equals("activity.ChooseClientActivity"))
                            {
                                //openBottomSheet for editing customer
                            }

                        }
                    });


            } catch (Exception e) {
                e.printStackTrace();
                //rowView = inflater.inflate(R.layout.list_item_address_bottom_sheet, null);

            }








        return rowView;
    }

}
