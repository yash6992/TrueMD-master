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

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.activity.InvoiceCreateActivity;
import com.truemdhq.projectx.activity.InvoiceViewActivity;
import com.truemdhq.projectx.model.Item;
import com.truemdhq.projectx.views.TextViewFont2Medium;

import java.util.List;

/**
 * Created by yashvardhansrivastava on 23/04/16.
 */
public class CustomInvoiceItemListAdapter extends BaseAdapter {

    List<Item> result1;
    Context context;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomInvoiceItemListAdapter(Context c1, List<Item> prgmNameList) {
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
        TextView itemName;
        TextView itemBarcode;
        TextViewFont2Medium itemAmount;

        ImageView productOrService;

        Holder (View rowView){
            itemName = (TextView) rowView.findViewById(R.id.item_name);
            itemBarcode = (TextView) rowView.findViewById(R.id.item_barcode);
            itemAmount = (TextViewFont2Medium) rowView.findViewById(R.id.item_amount);
            productOrService = (ImageView) rowView.findViewById(R.id.product_or_service);

        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
         Item item = (Item) result1.get(position);

        View rowView = convertView;

        Holder holder = null;

        if(rowView==null)
        {
            rowView = inflater.inflate(R.layout.list_item_invoice_item_projectx,parent, false);
            holder = new Holder(rowView);
            rowView.setTag(holder);
        }
        else{
            holder = (Holder) rowView.getTag();
        }




        if (item.isProduct())
        {
             Item  p1 =item;

            Log.e("InvoiceItem: ","item: "+p1.getItemName());

            try {
                if (p1.getItemName().length() < 0 )

                    holder.itemAmount.setText(p1.getItemAmount(),2);
                    holder.itemBarcode.setText("ISBN"+p1.getItemBarcode());
                    holder.itemName.setText(""+p1.getItemName()+" ("+p1.getItemQuantity()+" Nos.)");


                    // holder.img.setImageResource(imageId[position]);
                    rowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           //bottom sheet for editing particular item
                        }
                    });

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
                    holder.itemName.setText(""+s1.getItemName()+" ("+s1.getItemQuantity()+" Hours)");




                    // holder.img.setImageResource(imageId[position]);
                    rowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //bottom sheet for editing particular item


                        }
                    });

            } catch (Exception e) {
                e.printStackTrace();
                //rowView = inflater.inflate(R.layout.list_item_address_bottom_sheet, null);

            }


        }




        return rowView;
    }

}
