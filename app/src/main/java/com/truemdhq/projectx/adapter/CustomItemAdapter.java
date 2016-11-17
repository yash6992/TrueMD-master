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
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.activity.ChooseInvoiceItemActivity;
import com.truemdhq.projectx.activity.InvoiceCreateActivity;
import com.truemdhq.projectx.activity.InvoiceViewActivity;
import com.truemdhq.projectx.model.Item;
import com.truemdhq.projectx.views.TextViewFont2Medium;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yashvardhansrivastava on 23/04/16.
 */
public class CustomItemAdapter extends BaseAdapter {

    List<Item> result1; Double q;
    Context context;Holder holder;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomItemAdapter(Context c1, List<Item> prgmNameList) {
        // TODO Auto-generated constructor stub
        result1=prgmNameList;

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
        TextView itemName, itemQuantity;
        TextView itemBarcode;
        TextViewFont2Medium itemAmount;

        ImageView productOrService, plus, minus;

        Holder (View rowView){
            itemName = (TextView) rowView.findViewById(R.id.item_name);
            itemBarcode = (TextView) rowView.findViewById(R.id.item_barcode);
            itemQuantity = (TextView) rowView.findViewById(R.id.item_quantity);
            itemAmount = (TextViewFont2Medium) rowView.findViewById(R.id.item_amount);
            productOrService = (ImageView) rowView.findViewById(R.id.product_or_service);
            plus = (ImageView) rowView.findViewById(R.id.plus);
            minus = (ImageView) rowView.findViewById(R.id.minus);


        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Item item = (Item) result1.get(position);
         q = 1.0d;
        View rowView = convertView;

         holder = null;

        if(rowView==null)
        {
            rowView = inflater.inflate(R.layout.list_item_item_projectx,parent, false);
            holder = new Holder(rowView);
            rowView.setTag(holder);
        }
        else{
            holder = (Holder) rowView.getTag();
        }

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Plus: ",""+q+" ");
                q=q+1.0d;
                item.setItemQuantity(q);
                item.setItemAmount(item.getItemRate()*item.getItemQuantity());
                holder.itemQuantity.setText(q.toString().substring(0,q.toString().indexOf('.')));
                holder.itemAmount.setText(item.getItemAmount(),2);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (q<=1){

                }
                else {
                    Log.e("Minus: ",""+q+" ");
                    q=1-1.0d;
                    item.setItemQuantity(q);
                    item.setItemAmount(item.getItemRate()*item.getItemQuantity());
                    holder.itemQuantity.setText(q.toString().substring(0,q.toString().indexOf('.')));
                    holder.itemAmount.setText(item.getItemAmount(),2);
                }

            }
        });


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = (Activity) context;

                Log.e("ClientAdapter: ","rowClick: "+activity.getLocalClassName());

                if(activity.getLocalClassName().equals("activity.ItemListActivity"))
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
                else if(activity.getLocalClassName().equals("activity.ChooseInvoiceItemActivity"))
                {
                    Log.e("ClientAdapter: ","ChooseClientActivity");

                    List<Item> items = ChooseInvoiceItemActivity.invoiceDraft.getItems();

                    try {

                        ObjectMapper mapper = new ObjectMapper();

                        if(items!=null)
                        {
                            items.add(item);
                        }
                        else
                        {
                            items = new ArrayList<Item>();
                            items.add(item);
                        }
                        ChooseInvoiceItemActivity.invoiceDraft.setItems(items);

                        String jsonInvoiceDraft = mapper.writeValueAsString(ChooseInvoiceItemActivity.invoiceDraft);
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


        if (item.isProduct())
        {
            Item  p1 =item;

            Log.e("InvoiceItem: ","item: "+p1.getItemName());

            try {

                holder.itemAmount.setText(p1.getItemAmount(),2);
                holder.itemBarcode.setText("ISBN"+p1.getItemBarcode());
                holder.itemName.setText(""+p1.getItemName());
                holder.itemQuantity.setText("1");



            } catch (Exception e) {
                e.printStackTrace();
                //rowView = inflater.inflate(R.layout.list_item_address_bottom_sheet, null);

            }


        }
        else if (!item.isProduct())
        {
            Item s1 =  item;
            Log.e("InvoiceItem: ","item: "+s1.getItemName());
            try {

                holder.itemAmount.setText(s1.getItemAmount(),2);
                holder.itemBarcode.setText("ISBN"+s1.getItemBarcode());
                holder.itemName.setText(""+s1.getItemName());
                holder.itemQuantity.setText("1");

            } catch (Exception e) {
                e.printStackTrace();
                //rowView = inflater.inflate(R.layout.list_item_address_bottom_sheet, null);

            }


        }




        return rowView;
    }

}
