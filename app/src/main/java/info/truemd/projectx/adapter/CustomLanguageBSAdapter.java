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
import com.truemdhq.projectx.activity.OrderMedicineActivity;

/**
 * Created by yashvardhansrivastava on 23/04/16.
 */
public class CustomLanguageBSAdapter extends BaseAdapter {

    String [] result1;
    Context context;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomLanguageBSAdapter(Context c1, String[] prgmNameList) {
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
        TextView tv1;
        //ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView = inflater.inflate(R.layout.list_item_search_null, null);


        try {
            if (result1[position].length() < 0)
                rowView = inflater.inflate(R.layout.list_item_search_null, null);
            else {
                rowView = inflater.inflate(R.layout.list_item_language_bottom_sheet, null);


                holder.tv1 = (TextView) rowView.findViewById(R.id.name_list_item_search);

                Typeface tf_r = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                Typeface tf_b = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                holder.tv1.setTypeface(tf_r);
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

                // holder.img.setImageResource(imageId[position]);
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        ConfirmOrderActivity.languageFinal=result1[position];
                        //Toast.makeText(context, result1[position].toString(),Toast.LENGTH_SHORT).show();

                        if(ConfirmOrderActivity.mBottomSheetDialog.isShowing())
                            ConfirmOrderActivity.mBottomSheetDialog.dismiss();



                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            //rowView = inflater.inflate(R.layout.list_item_address_bottom_sheet, null);

        }


        return rowView;
    }

}
