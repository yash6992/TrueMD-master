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

import org.json.JSONException;
import org.json.JSONObject;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.activity.MedicineDetailsActivity2;
import com.truemdhq.projectx.activity.PrescriptionDetailsActivity;
import com.truemdhq.projectx.activity.PreviousOrderActivity;
import com.truemdhq.projectx.helper.ProjectXJSONUtils;

/**
 * Created by yashvardhansrivastava on 27/04/16.
 */
public class CustomPrescriptionAdapter extends BaseAdapter {
    String [] result1, result2, result3, result4, result5, result6; JSONObject[] result7;
    Context context;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomPrescriptionAdapter(Context c1, String[] dateList,String[] presNoList,String[] userNameList,String[] med1List,String[] doctorList, String [] descripList, JSONObject [] oidList) {
        // TODO Auto-generated constructor stub
        result1=dateList;
        result2=presNoList;
        result3=userNameList;
        result4=med1List;
        result5=doctorList;
        result6=descripList;
        result7=oidList;

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
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
        //ImageView img;
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
                rowView = inflater.inflate(R.layout.list_item_prescription_list, null);


                holder.tv1 = (TextView) rowView.findViewById(R.id.pl_date);
                holder.tv2 = (TextView) rowView.findViewById(R.id.pl_pres_no_h);
                holder.tv3 = (TextView) rowView.findViewById(R.id.pl_pres_no);
                holder.tv4 = (TextView) rowView.findViewById(R.id.pl_username);
                holder.tv5 = (TextView) rowView.findViewById(R.id.pl_doctorname);
                holder.tv6 = (TextView) rowView.findViewById(R.id.pl_med_summary);
                holder.tv7 = (TextView) rowView.findViewById(R.id.pl_comments);
                
                Typeface tf_r = Typeface.createFromAsset(context.getAssets(), "MarkOffcPro-Medium.ttf");
                Typeface tf_b = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                holder.tv1.setTypeface(tf_b);
                holder.tv2.setTypeface(tf_r);
                holder.tv3.setTypeface(tf_r);
                holder.tv4.setTypeface(tf_r);
                holder.tv5.setTypeface(tf_r);
                holder.tv6.setTypeface(tf_r);
                holder.tv7.setTypeface(tf_r);

                holder.tv7.setVisibility(View.GONE);
                

                holder.tv1.setText(result1[position]);
                //holder.tv2.setText(result2[position]);
                holder.tv3.setText(ProjectXJSONUtils.goThroughNullCheck(result2[position]));
                holder.tv4.setText(result3[position]);
                holder.tv5.setText(result5[position]);
                holder.tv6.setText(result4[position]);
                holder.tv7.setText(result6[position]);

                // holder.img.setImageResource(imageId[position]);
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        Intent intent_main_search = new Intent(context, PrescriptionDetailsActivity.class);
                        intent_main_search.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_main_search.putExtra("pl_obj", result7[position].toString());

                        Log.e("PLAdapter", "" + result5[position] + ":" + result7[position].toString());
                        context.startActivity(intent_main_search);

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