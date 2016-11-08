package com.truemdhq.projectx.adapter;

/**
 * Created by yashvardhansrivastava on 14/03/16.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.activity.MedicineDetailsActivity2;

public class CustomSubstitutesAdapter extends BaseAdapter{
    String [] result1, result2, result3, result4, result5, result6;
    Context context;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomSubstitutesAdapter(Context c1, String[] prgmNameList,String[] prgmManufacturerList,String[] prgmPriceList,String[] prgmPacketSizeList,String[] prgmTrueMDCodeList, String [] prgmUnitPriceList) {
        // TODO Auto-generated constructor stub
        result1=prgmNameList;
        result2=prgmManufacturerList;
        result3=prgmPriceList;
        result4=prgmPacketSizeList;
        result5=prgmTrueMDCodeList;
        result6=prgmUnitPriceList;

        context=c1;
        //imageId=prgmImages;
        try {
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        TextView tv1,tv2,tv3,tv4,tv5;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView = inflater.inflate(R.layout.list_item_search_null, null);


        try {
            if (result5[position].length() < 0)
                rowView = inflater.inflate(R.layout.list_item_search_null, null);
            else {
                rowView = inflater.inflate(R.layout.list_item_substitutes, null);


                holder.tv1 = (TextView) rowView.findViewById(R.id.name_list_item_search);
                holder.tv2 = (TextView) rowView.findViewById(R.id.manufacturer_list_item_search);
                holder.tv3 = (TextView) rowView.findViewById(R.id.price_list_item_search);
                holder.tv4 = (TextView) rowView.findViewById(R.id.packet_size_list_item_search);
                holder.tv5 = (TextView) rowView.findViewById(R.id.price_diff_list_item);

                holder.img=(ImageView) rowView.findViewById(R.id.thumbs_image);

                Typeface tf_r = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                Typeface tf_b = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                holder.tv1.setTypeface(tf_r);
                holder.tv2.setTypeface(tf_r);
                holder.tv3.setTypeface(tf_r);
                holder.tv4.setTypeface(tf_r);
                holder.tv5.setTypeface(tf_r);

                String name="";

                if(result1[position].length()!=0) {

                    if(result1[position].length()>27)
                        name=result1[position].substring(0,26)+"...";
                        //getSupportActionBar().setTitle(name.substring(0,14)+"...");
                    else
                        name=result1[position];
                    //getSupportActionBar().setTitle(name);
                }

                holder.tv1.setText(name);
                holder.tv2.setText(result2[position]);
                holder.tv3.setText("\u20B9 " + result3[position]);
                holder.tv4.setText(result4[position]);


                float uPriceListItem =(float) Float.parseFloat(result6[position]) ;
                float uPriceMed = (float) Float.parseFloat(MedicineDetailsActivity2.medicine.getuPrice());

                if(uPriceListItem<uPriceMed)
                {
                    holder.img.setImageResource(R.drawable.ic_thumb_up);
                    float diff = (float)uPriceMed-(float)uPriceListItem;
                    float diffp = (float)(diff/uPriceMed)*100;
                    int idiffp = (int) diffp;
                    holder.tv5.setTextColor(context.getResources().getColor(R.color.green));
                    holder.tv5.setText(idiffp+"%");

                }
                else if(uPriceListItem>uPriceMed)
                {
                    holder.img.setImageResource(R.drawable.ic_thumb_down);
                    float diff = (float)uPriceListItem-(float)uPriceMed;
                    float diffp = (float)(diff/uPriceMed)*100;
                    int idiffp = (int) diffp;
                    holder.tv5.setTextColor(Color.RED);
                    holder.tv5.setText("-"+idiffp+"%");

                }
                else
                {
//                    holder.img.setImageResource(R.drawable.ic_thumb_down);
//                    float diff = (float)uPriceListItem-(float)uPriceMed;
//                    float diffp = (float)(diff/uPriceMed)*100;
//                    int idiffp = (int) diffp;
//                    holder.tv5.setText("-"+idiffp+"%");

                }


                rowView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                       // Toast.makeText(context, "Clicked " + result5[position], Toast.LENGTH_LONG).show();
                        Intent intent_main_s = new Intent((MedicineDetailsActivity2)context, MedicineDetailsActivity2.class);
                        intent_main_s.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent_main_s.putExtra("truemdCode", result5[position] + ":" + result1[position]);
                        Log.e("Substitutes", "" + result5[position] + ":" + result1[position]);
                        ((MedicineDetailsActivity2)context).startActivity(intent_main_s);
                        ((MedicineDetailsActivity2)context).finish();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            rowView = inflater.inflate(R.layout.list_item_search_null, null);

        }


        return rowView;
    }

}