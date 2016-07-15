package info.truemd.materialdesign.adapter;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.truemd.materialdesign.R;
import info.truemd.materialdesign.activity.MedicineDetailsActivity2;
import info.truemd.materialdesign.activity.PreviousOrderActivity;
import info.truemd.materialdesign.helper.CustomHtmlTextParsing;

/**
 * Created by yashvardhansrivastava on 27/04/16.
 */
public class CustomInvoiceAdapter extends BaseAdapter {
    ArrayList<JSONObject> resultIALJO;
    Context context;
    Activity contexta;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomInvoiceAdapter(Context c1,Activity ca, ArrayList<JSONObject> ialjo) {
        // TODO Auto-generated constructor stub

        resultIALJO= ialjo;
        contexta = ca;
        context=c1;
        //imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return resultIALJO.size();
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
        TextView tv1,tv2,tv3,tv4,tv5,tv6, tv7, tv8;
        RelativeLayout rl;
        ImageView img1, img2;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView = inflater.inflate(R.layout.list_item_previous_order_activity, null);
        JSONObject invoiceItem = new JSONObject();

        try {

            invoiceItem =resultIALJO.get(position);

            if (invoiceItem.toString().length() < 1)
                rowView = inflater.inflate(R.layout.list_item_search_null, null);
            else {
                rowView = inflater.inflate(R.layout.list_item_previous_order_activity, null);


                holder.tv1 = (TextView) rowView.findViewById(R.id.name_item);
                holder.tv2 = (TextView) rowView.findViewById(R.id.total_price);
                holder.tv3 = (TextView) rowView.findViewById(R.id.med_info1_h);
                holder.tv4 = (TextView) rowView.findViewById(R.id.med_info1);
                holder.tv5 = (TextView) rowView.findViewById(R.id.med_info2_h);
                holder.tv6 = (TextView) rowView.findViewById(R.id.med_info2);
                holder.tv7 = (TextView) rowView.findViewById(R.id.med_info3_h);
                holder.tv8 = (TextView) rowView.findViewById(R.id.med_info3);
                holder.rl = (RelativeLayout) rowView.findViewById(R.id.extra_invoice);

                holder.img1=(ImageView) rowView.findViewById(R.id.icon_gt);
                holder.img2=(ImageView) rowView.findViewById(R.id.icon_type);

                Typeface tf_r = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                Typeface tf_b = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                holder.tv1.setTypeface(tf_r);
                holder.tv2.setTypeface(tf_r);
                holder.tv3.setTypeface(tf_r);
                holder.tv4.setTypeface(tf_r);
                holder.tv5.setTypeface(tf_r);
                holder.tv6.setTypeface(tf_r);
                holder.tv7.setTypeface(tf_r);
                holder.tv8.setTypeface(tf_r);

                holder.rl.setVisibility(View.GONE); holder.img1.setRotation(0);


                Double price = Double.parseDouble(invoiceItem.getString("quantity")) * Double.parseDouble(invoiceItem.getString("price_per_pack"));

                holder.tv1.setText(invoiceItem.getString("name"));
                holder.tv2.setText(String.format("%.2f",price));
                holder.tv3.setText("Quantity: ");
                holder.tv4.setText(invoiceItem.getString("quantity"));
                holder.tv5.setText("Price / Unit: ");
                holder.tv6.setText(invoiceItem.getString("price_per_pack"));
                holder.tv7.setText("Package Size: ");
                holder.tv8.setText(invoiceItem.getString("pack_size"));


                // holder.img.setImageResource(imageId[position]);
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        openBottomSheet(resultIALJO.get(position));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            rowView = inflater.inflate(R.layout.list_item_search_null, null);

        }


        return rowView;
    }
    public void openBottomSheet (JSONObject invoiceItem) {

        Typeface tf_r= Typeface.createFromAsset(contexta.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(contexta.getAssets(), "fonts/OpenSans-Bold.ttf");

        TextView tv1,tv2,tv3,tv4,tv5,tv6, tv7, tv8;
        RelativeLayout rl;
        ImageView img1, img2;

        View view = contexta.getLayoutInflater().inflate(R.layout.bottom_sheet_invoice_item, null);
       tv1 = (TextView) view.findViewById(R.id.name_item);
        tv2 = (TextView) view.findViewById(R.id.total_price);
        tv3 = (TextView) view.findViewById(R.id.med_info1_h);
        tv4 = (TextView) view.findViewById(R.id.med_info1);
        tv5 = (TextView) view.findViewById(R.id.med_info2_h);
        tv6 = (TextView) view.findViewById(R.id.med_info2);
        tv7 = (TextView) view.findViewById(R.id.med_info3_h);
        tv8 = (TextView) view.findViewById(R.id.med_info3);
        rl = (RelativeLayout) view.findViewById(R.id.extra_invoice);
        img1=(ImageView) view.findViewById(R.id.icon_gt);
        img2=(ImageView) view.findViewById(R.id.icon_type);

        tv1.setTypeface(tf_r);
        tv2.setTypeface(tf_r);
        tv3.setTypeface(tf_r);
        tv4.setTypeface(tf_r);
        tv5.setTypeface(tf_r);
        tv6.setTypeface(tf_r);
        tv7.setTypeface(tf_r);
        tv8.setTypeface(tf_r);


        try {

            Double price = Double.parseDouble(invoiceItem.getString("quantity")) * Double.parseDouble(invoiceItem.getString("price_per_pack"));

            tv1.setText(invoiceItem.getString("name"));
            tv2.setText(String.format("%.2f",price));
            tv3.setText("Quantity: ");
            tv4.setText(invoiceItem.getString("quantity"));
            tv5.setText("Price / Unit: ");
            tv6.setText(invoiceItem.getString("price_per_pack"));
            tv7.setText("Package Size: ");
            tv8.setText(invoiceItem.getString("pack_size"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final Dialog mBottomSheetDialog = new Dialog (contexta,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.CENTER);
        mBottomSheetDialog.show();


    }

}