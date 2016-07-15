package info.truemd.materialdesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.truemd.materialdesign.R;
import info.truemd.materialdesign.activity.MedicineDetailsActivity2;
import info.truemd.materialdesign.activity.PreviousOrderActivity;
import info.truemd.materialdesign.activity.UpcomingOrderActivity;

/**
 * Created by yashvardhansrivastava on 27/04/16.
 */
public class CustomUpcomingOrderAdapter extends BaseAdapter {
    String [] result1, result2, result3, result4, result5, result6, result7;
    Context context; String []shorturls;
    ArrayList<JSONObject> resultALJO;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomUpcomingOrderAdapter(Context c1, String[] dateList,String[] orderNoList,String[] userNameList,String[] statusList,String[] dotsList, String [] gTotalList, String [] deliveryTimeList, ArrayList<JSONObject> aljo) {
        // TODO Auto-generated constructor stub
        result1=dateList;
        result2=orderNoList;
        result3=userNameList;
        result4=statusList;
        result5=dotsList;
        result6=gTotalList;
        result7=deliveryTimeList;
        resultALJO = aljo;
        shorturls=new String[result1.length];

        context=c1;
        //imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.e("UOAAdapter: ","constructor finished.");
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
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv11,tv12,tv21,tv22,tv31,tv32,tv41,tv42, ph,p,btn_pn;
        //ImageView img;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView = inflater.inflate(R.layout.list_item_search_null, null);
        Log.e("UOAAdapter: ",position+" : called");


        try {
            if (result5[position].length() < 0) {
                rowView = inflater.inflate(R.layout.list_item_search_null, null);
                Log.e("UOAAdapter: ",position+" : else (<5) called");
            }
            else {
                rowView = inflater.inflate(R.layout.list_item_upcoming_order, null);
                Log.e("UOAAdapter: ",position+" : "+resultALJO.get(position).toString());
                JSONObject ujo = resultALJO.get(position);
                boolean status=false;
                boolean instamojo_flag=false;
                String mode="";
                String pstatus = "N.A.";
                String shorturl="";

                try {
                    if (ujo.has("payment")){
                        JSONObject payment = ujo.getJSONObject("payment");
                        if (payment.has("status")){
                             status = payment.getBoolean("status");
                            Log.e("Order",position+": if status"+status);
                        }
                        if (payment.has("mode")){
                             mode = payment.getString("mode");
                            Log.e("Order",position+": if status"+mode);
                        }
                        if (payment.has("instamojo_flag")){
                             instamojo_flag = payment.getBoolean("instamojo_flag");
                            Log.e("Order",position+": if status"+status);
                        }
                        if (payment.has("instamojo")){
                            JSONObject instamojo=payment.getJSONObject("instamojo");
                            if (instamojo.has("payment_request")){
                                JSONObject payment_request=instamojo.getJSONObject("payment_request");
                                if (payment_request.has("shorturl")){
                                    shorturl = payment_request.getString("shorturl");
                                }
                            }
                        }
                    }
                    Log.e("OrderObject: ",""+position+" : "+ujo.optString("order_bucket")+" url: "+shorturl+" s: "+status+" if: "+instamojo_flag+" : "+mode);
                    shorturls[position]=shorturl;
                } catch (JSONException e) {
                    Log.e("UOAErrorupperadd: ",position+" : "+e.getMessage());

                    e.printStackTrace();
                }


                holder.tv1 = (TextView) rowView.findViewById(R.id.uo_date);
                holder.tv2 = (TextView) rowView.findViewById(R.id.uo_order_no_h);
                holder.tv3 = (TextView) rowView.findViewById(R.id.uo_order_no);
                holder.tv4 = (TextView) rowView.findViewById(R.id.uo_status_h);
                holder.tv5 = (TextView) rowView.findViewById(R.id.uo_status);
                holder.tv6 = (TextView) rowView.findViewById(R.id.uo_patient_name);
                holder.tv7 = (TextView) rowView.findViewById(R.id.uo_expected_delivery_h);
                holder.tv8 = (TextView) rowView.findViewById(R.id.uo_expected_delivery);
                holder.tv9 = (TextView) rowView.findViewById(R.id.uo_price);

                holder.tv11 = (TextView) rowView.findViewById(R.id.uo_textView11);
                holder.tv12 = (TextView) rowView.findViewById(R.id.uo_textView12);
                holder.tv21 = (TextView) rowView.findViewById(R.id.uo_textView21);
                holder.tv22 = (TextView) rowView.findViewById(R.id.uo_textView22);

                holder.tv31 = (TextView) rowView.findViewById(R.id.uo_textView31);
                holder.tv32 = (TextView) rowView.findViewById(R.id.uo_textView32);
                holder.tv41 = (TextView) rowView.findViewById(R.id.uo_textView41);
                holder.tv42 = (TextView) rowView.findViewById(R.id.uo_textView42);

                holder.p = (TextView) rowView.findViewById(R.id.payment_status);
                holder.ph = (TextView) rowView.findViewById(R.id.payment_status_h);
                holder.btn_pn = (TextView) rowView.findViewById(R.id.btn_pay_now);

                //holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

                Typeface tf_r = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                Typeface tf_b = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                holder.tv1.setTypeface(tf_b);
                holder.tv2.setTypeface(tf_r);
                holder.tv3.setTypeface(tf_r);
                holder.tv4.setTypeface(tf_r);
                holder.tv5.setTypeface(tf_r);
                holder.tv6.setTypeface(tf_r);
                holder.tv7.setTypeface(tf_r);
                holder.tv8.setTypeface(tf_r);
                holder.tv9.setTypeface(tf_r);
                holder.tv11.setTypeface(tf_b);
                holder.tv12.setTypeface(tf_r);
                holder.tv21.setTypeface(tf_b);
                holder.tv22.setTypeface(tf_r);
                holder.tv31.setTypeface(tf_b);
                holder.tv32.setTypeface(tf_r);
                holder.tv41.setTypeface(tf_b);
                holder.tv42.setTypeface(tf_r);
                holder.btn_pn.setTypeface(tf_r);
                holder.p.setTypeface(tf_r);
                holder.ph.setTypeface(tf_r);


                holder.tv1.setText(result1[position]);
                holder.tv3.setText(result2[position]);
                holder.tv5.setText(result4[position]);
                holder.tv6.setText(result3[position]);
                holder.tv8.setText(result7[position]);
                holder.tv9.setText(result6[position]);

                if(result5[position].equalsIgnoreCase("1"))
                {

                    holder.tv11.setBackground(context.getResources().getDrawable(R.drawable.shape_dark_dots));
                    holder.tv12.setTextColor(context.getResources().getColor(R.color.windowBackground));
                    holder.tv21.setBackground(context.getResources().getDrawable(R.drawable.shape_solid_circle_grey));
                    holder.tv22.setTextColor(context.getResources().getColor(R.color.steel));
                    holder.tv31.setBackground(context.getResources().getDrawable(R.drawable.shape_solid_circle_grey));
                    holder.tv32.setTextColor(context.getResources().getColor(R.color.steel));
                    holder.tv41.setBackground(context.getResources().getDrawable(R.drawable.shape_solid_circle_grey));
                    holder.tv42.setTextColor(context.getResources().getColor(R.color.steel));
                }
                else if(result5[position].equalsIgnoreCase("2"))
                {
                    holder.tv11.setBackground(context.getResources().getDrawable(R.drawable.shape_dark_dots));
                    holder.tv12.setTextColor(context.getResources().getColor(R.color.windowBackground));
                    holder.tv21.setBackground(context.getResources().getDrawable(R.drawable.shape_dark_dots));
                    holder.tv22.setTextColor(context.getResources().getColor(R.color.windowBackground));
                    holder.tv31.setBackground(context.getResources().getDrawable(R.drawable.shape_solid_circle_grey));
                    holder.tv32.setTextColor(context.getResources().getColor(R.color.steel));
                    holder.tv41.setBackground(context.getResources().getDrawable(R.drawable.shape_solid_circle_grey));
                    holder.tv42.setTextColor(context.getResources().getColor(R.color.steel));

                }
                else if(result5[position].equalsIgnoreCase("3"))
                {
                    holder.tv11.setBackground(context.getResources().getDrawable(R.drawable.shape_dark_dots));
                    holder.tv12.setTextColor(context.getResources().getColor(R.color.windowBackground));
                    holder.tv21.setBackground(context.getResources().getDrawable(R.drawable.shape_dark_dots));
                    holder.tv22.setTextColor(context.getResources().getColor(R.color.windowBackground));
                    holder.tv31.setBackground(context.getResources().getDrawable(R.drawable.shape_dark_dots));
                    holder.tv32.setTextColor(context.getResources().getColor(R.color.windowBackground));
                    holder.tv41.setBackground(context.getResources().getDrawable(R.drawable.shape_solid_circle_grey));
                    holder.tv42.setTextColor(context.getResources().getColor(R.color.steel));
                }
                else if(result5[position].equalsIgnoreCase("4"))
                {
                    holder.tv11.setBackground(context.getResources().getDrawable(R.drawable.shape_dark_dots));
                    holder.tv12.setTextColor(context.getResources().getColor(R.color.windowBackground));
                    holder.tv21.setBackground(context.getResources().getDrawable(R.drawable.shape_dark_dots));
                    holder.tv22.setTextColor(context.getResources().getColor(R.color.windowBackground));
                    holder.tv31.setBackground(context.getResources().getDrawable(R.drawable.shape_dark_dots));
                    holder.tv32.setTextColor(context.getResources().getColor(R.color.windowBackground));
                    holder.tv41.setBackground(context.getResources().getDrawable(R.drawable.shape_dark_dots));
                    holder.tv42.setTextColor(context.getResources().getColor(R.color.windowBackground));
                }


                try {
                    if(!status){ //payment not done; either invoice not generated or payment pending
                        if(instamojo_flag){
                            holder.btn_pn.setVisibility(View.VISIBLE);
                            pstatus=""+result6[position];
                        }
                        else{
                            holder.btn_pn.setVisibility(View.GONE);
                            pstatus="You'll be notified after order digitization.";
                        }
                    }
                    else{ //payment done
                        holder.btn_pn.setVisibility(View.GONE);
                        pstatus="Paid through "+mode;
                    }

                    holder.p.setText(pstatus);

                    holder.btn_pn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (!shorturls[position].startsWith("http://") && !shorturls[position].startsWith("https://"))
                                shorturls[position]= "http://" + shorturls[position];

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shorturls[position]));
                            ((Activity)context).finish();
                            context.startActivity(browserIntent);

                        }
                    });
                } catch (Exception e) {
                    Log.e("UOAErrorloweradd: ",position+" : "+e.getMessage());
                    e.printStackTrace();
                }

                // holder.img.setImageResource(imageId[position]);
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(context, "You Clicked " + result5[position], Toast.LENGTH_LONG).show();
//                        Intent intent_main_search = new Intent(context, UpcomingOrderActivity.class);
//                        intent_main_search.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent_main_search.putExtra("truemdCode", result5[position] + ":" + result1[position]);
//                        Log.e("PreviousAdapter", "" + result5[position] + ":" + result1[position]);
//                        context.startActivity(intent_main_search);

                       // Toast.makeText(context, "You Clicked " + result2[position], Toast.LENGTH_SHORT).show();
                        Intent intent_main_search = new Intent(context, UpcomingOrderActivity.class);
                        intent_main_search.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_main_search.putExtra("aljo", resultALJO.get(position).toString());
                        Log.e("UpcomingOrder", resultALJO.get(position).toString());
                        context.startActivity(intent_main_search);

                    }
                });




            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("UOAAdapterError: ",position+" : "+e.getMessage());
            rowView = inflater.inflate(R.layout.list_item_search_null, null);

        }


        return rowView;
    }

}