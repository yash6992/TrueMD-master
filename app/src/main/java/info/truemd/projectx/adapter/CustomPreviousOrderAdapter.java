package com.truemdhq.projectx.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.activity.PreviousOrderActivity;
import com.truemdhq.projectx.activity.RefillOrderActivity;

/**
 * Created by yashvardhansrivastava on 27/04/16.
 */
public class CustomPreviousOrderAdapter extends BaseAdapter {
    String [] result1, result2, result3, result4, result5, result6;
    ArrayList<JSONObject> resultALJO;
    Activity context;Dialog mBottomSheetDialog1;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomPreviousOrderAdapter(Activity c1, String[] dateList, String[] orderNoList, String[] userNameList, String[] med1List, String[] med2List, String [] gTotalList, ArrayList<JSONObject> aljo) {
        // TODO Auto-generated constructor stub
        result1=dateList;
        result2=orderNoList;
        result3=userNameList;
        result4=med1List;
        result5=med2List;
        result6=gTotalList;
        resultALJO= aljo;

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
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9, p,ph;
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
                rowView = inflater.inflate(R.layout.list_item_previous_order, null);


                holder.tv1 = (TextView) rowView.findViewById(R.id.po_date);
                holder.tv2 = (TextView) rowView.findViewById(R.id.po_order_no_h);
                holder.tv3 = (TextView) rowView.findViewById(R.id.po_order_no);
                holder.tv4 = (TextView) rowView.findViewById(R.id.po_med1);
                holder.tv5 = (TextView) rowView.findViewById(R.id.po_med2);
                holder.tv6 = (TextView) rowView.findViewById(R.id.po_patient_name);
                holder.tv7 = (TextView) rowView.findViewById(R.id.po_repeat_btn);
                holder.tv8 = (TextView) rowView.findViewById(R.id.po_three_dots);
                holder.tv9 = (TextView) rowView.findViewById(R.id.po_price);
                holder.p = (TextView) rowView.findViewById(R.id.po_payment_status);
                holder.ph = (TextView) rowView.findViewById(R.id.po_payment_status_h);

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
                //holder.tv8.setTypeface(tf_r);
                holder.tv9.setTypeface(tf_r);
                holder.p.setTypeface(tf_r);
                holder.ph.setTypeface(tf_r);


                holder.tv1.setText(result1[position]);
                //holder.tv2.setText(result2[position]);
                holder.tv3.setText(result2[position]);
                holder.tv4.setText(result4[position]);
                holder.tv5.setText(result5[position]);
                holder.tv6.setText(result3[position]);
                holder.tv7.setText("REFILL");
                //holder.tv8.setText(result4[position]);
                holder.tv9.setText(result6[position]);

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

                } catch (JSONException e) {
                    Log.e("UOAErrorupperadd: ",position+" : "+e.getMessage());

                    e.printStackTrace();
                }

                    if(!status){ //payment not done; either invoice not generated or payment pending
                        if(instamojo_flag){
                            pstatus="Invoice generated";
                        }
                        else{
                            pstatus="Invoice not generated";
                        }
                    }
                    else{ //payment done
                        pstatus="Paid through "+ mode;
                    }

                if(resultALJO.get(position).optString("status").equalsIgnoreCase("OCan"))
                {
                    pstatus  = "This order was cancelled.";
                }

                Log.e("PO: ", position+" "+ resultALJO.get(position).optString("status_msg"));

                    holder.p.setText(pstatus);

                // holder.img.setImageResource(imageId[position]);
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(context, "You Clicked " + result2[position], Toast.LENGTH_SHORT).show();
                        Intent intent_main_search = new Intent(context, PreviousOrderActivity.class);
                        intent_main_search.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_main_search.putExtra("aljo", resultALJO.get(position).toString());
                        Log.e("PreviousAdapter", resultALJO.get(position).toString());
                        context.startActivity(intent_main_search);

                    }
                });

                if(resultALJO.get(position).optString("status").equalsIgnoreCase("ODel"))
                {


                }
                else{
                    holder.tv7.setVisibility(View.GONE);
                }


                holder.tv7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
//                        Intent toRefill = new Intent(context, RefillOrderActivity.class);
//                        toRefill.putExtra("refillJOS", resultALJO.get(position).toString());
//                        context.startActivity(toRefill);
                        openBottomSheetForPreRefill(position);

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            rowView = inflater.inflate(R.layout.list_item_search_null, null);

        }


        return rowView;
    }

    public void openBottomSheetForPreRefill(final int position) {

        Typeface tf_r= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = context.getLayoutInflater().inflate(R.layout.bottom_sheet_pre_refill, null);

        TextView txth = (TextView)view.findViewById( R.id.tv2);
        TextView txt = (TextView)view.findViewById( R.id.refill_txt);
        //phone_number = (EditText)view.findViewById( R.id.input_phone_number_password);
        Button submit = (Button) view.findViewById( R.id.btn_next);

        txth.setTypeface(tf_r);txt.setTypeface(tf_r);
        //phone_number.setTypeface(tf_r);
        submit.setTypeface(tf_r);

        //txth.setText(heading);



         mBottomSheetDialog1= new Dialog(context,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog1.setContentView (view);
        mBottomSheetDialog1.setCancelable(true);
        mBottomSheetDialog1.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog1.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog1.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toRefill = new Intent(context, RefillOrderActivity.class);
                toRefill.putExtra("refillJOS", resultALJO.get(position).toString());
                mBottomSheetDialog1.dismiss();
                context.startActivity(toRefill);
            }
        });

    }





}