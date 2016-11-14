package com.truemdhq.projectx.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.activity.ConfirmOrderActivity;
import com.truemdhq.projectx.activity.MedicineDetailsActivity2;
import com.truemdhq.projectx.activity.RefillOrderActivity;

/**
 * Created by yashvardhansrivastava on 23/04/16.
 */
public class CustomAddressBSAdapter extends BaseAdapter {

    String [] result1, result2;
    Context context;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomAddressBSAdapter(Context c1, String[] prgmNameList,String[] prgmManufacturerList) {
        // TODO Auto-generated constructor stub
        result1=prgmNameList;
        result2=prgmManufacturerList;

        context=c1;
        //imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result1.length;
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
        TextView tv1,tv2;
        //ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView = inflater.inflate(R.layout.list_item_search_null, null);


        try {
            if (result2[position].length() < 0)
                rowView = inflater.inflate(R.layout.list_item_search_null, null);
            else {
                rowView = inflater.inflate(R.layout.list_item_address_bottom_sheet, null);


                holder.tv1 = (TextView) rowView.findViewById(R.id.name_list_item_search);
                holder.tv2 = (TextView) rowView.findViewById(R.id.manufacturer_list_item_search);

                Typeface tf_r = Typeface.createFromAsset(context.getAssets(), "MarkOffcPro-Medium.ttf");
                Typeface tf_b = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                holder.tv1.setTypeface(tf_r);
                holder.tv2.setTypeface(tf_r);

                String name="";

                if(result1[position].length()!=0) {

                    if(result1[position].length()>33)
                        name=result1[position].substring(0,33)+"...";
                        //getSupportActionBar().setTitle(name.substring(0,14)+"...");
                    else
                        name=result1[position];
                    //getSupportActionBar().setTitle(name);
                }

                holder.tv1.setText(result1[position]);
                holder.tv2.setText(result2[position]);

                // holder.img.setImageResource(imageId[position]);
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        try {
                            ConfirmOrderActivity.deliveryAddressFinal=result2[position];
                            ConfirmOrderActivity.selectedAddressjobj= ConfirmOrderActivity.deliveryjarray.get(position);
                            //Toast.makeText(context, result1[position].toString(),Toast.LENGTH_SHORT).show();

                            Log.e("AddBSAdapterString: ",ConfirmOrderActivity.deliveryAddressFinal);
                            Log.e("AddBSAdapterJsonObj: ",ConfirmOrderActivity.selectedAddressjobj.toString());

                           // ConfirmOrderActivity.deliveryTVCO.setText(ConfirmOrderActivity.deliveryAddressFinal);
                           // RefillOrderActivity.deliveryTVCO.setText(RefillOrderActivity.deliveryAddressFinal);

                            if(ConfirmOrderActivity.mBottomSheetDialog.isShowing())
                                ConfirmOrderActivity.mBottomSheetDialog.dismiss();

                            // ConfirmOrderActivity.deliveryTVCO.setText(ConfirmOrderActivity.deliveryAddressFinal);
                            // RefillOrderActivity.deliveryTVCO.setText(RefillOrderActivity.deliveryAddressFinal);
                            try {
                                ConfirmOrderActivity.deliveryTVCO.setText(ConfirmOrderActivity.deliveryAddressFinal);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("Address1: ",e.getMessage());
                            }




                           // Toast.makeText(context, result1[position].toString(),Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Address3: ",e.getMessage());
                        }


                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Address4: ",e.getMessage());
            //rowView = inflater.inflate(R.layout.list_item_address_bottom_sheet, null);

        }


        return rowView;
    }

}
