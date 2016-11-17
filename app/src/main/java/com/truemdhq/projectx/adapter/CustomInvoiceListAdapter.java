package com.truemdhq.projectx.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.activity.InvoiceViewActivity;
import com.truemdhq.projectx.model.Invoice;
import com.truemdhq.projectx.views.TextViewFont3Large;

import java.util.List;

/**
 * Created by yashvardhansrivastava on 23/04/16.
 */
public class CustomInvoiceListAdapter extends BaseAdapter {

    List<Invoice> result1;
    Context context;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomInvoiceListAdapter(Context c1, List<Invoice> prgmNameList) {
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
        TextView invoiceNo;
         TextView vendorName;
        TextView invoiceDate;
         TextView dueDate;
        TextView status;
         TextViewFont3Large balanceDue;

        ImageView statusImage;

       public Holder (View v){

           invoiceNo = (TextView) v.findViewById(R.id.invoice_no);
           invoiceDate = (TextView) v.findViewById(R.id.invoice_date);
           vendorName = (TextView) v.findViewById(R.id.vendor_name);
           dueDate = (TextView) v.findViewById(R.id.due_date);
           status = (TextView) v.findViewById(R.id.status_text);
           balanceDue = (TextViewFont3Large) v.findViewById(R.id.balance_due);
           statusImage = (ImageView) v.findViewById(R.id.status_img);


       }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //Holder holder=new Holder();

        final Invoice invoiceParent =result1.get(position);

        View rowView = convertView;

        Holder holder = null;

        if(rowView==null)
        {
            rowView = inflater.inflate(R.layout.list_item_invoice,parent, false);
            holder = new Holder(rowView);
            rowView.setTag(holder);
        }
        else{
            holder = (Holder) rowView.getTag();
        }




        try {

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Activity activity = (Activity) context;

                        Intent nextActivity = new Intent(context, InvoiceViewActivity.class);
                        nextActivity.putExtra("invoice", position);
                        activity.startActivity(nextActivity);
                        //push from bottom to top
                        //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                        //slide from right to left
                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
            //rowView = inflater.inflate(R.layout.list_item_address_bottom_sheet, null);

        }



        holder.invoiceNo.setText(""+invoiceParent.getInvoiceNo());
        holder.vendorName.setText(""+invoiceParent.getClient().getVendorName());
        holder.dueDate.setText(""+invoiceParent.getInvoiceDueDate());
        holder.invoiceDate.setText(""+invoiceParent.getInvoiceDate());
        holder.balanceDue.setText(invoiceParent.getBalanceDue(),2);
        holder.status.setText(""+invoiceParent.getInvoiceStatus());

        if(invoiceParent.getInvoiceStatus().equalsIgnoreCase("PAID")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.primary_green));
            holder.statusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fee_cta_25_green));
        }else{
            holder.status.setTextColor(context.getResources().getColor(R.color.primary_blue));
            holder.statusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fee_cta_25_dp));
        }


        return rowView;
    }

}
