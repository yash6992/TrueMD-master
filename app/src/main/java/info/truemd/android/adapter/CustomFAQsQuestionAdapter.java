package info.truemd.android.adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.truemd.android.R;
import info.truemd.android.activity.MedicineDetailsActivity2;
import info.truemd.android.activity.PrescriptionDetailsActivity;
import info.truemd.android.activity.PreviousOrderActivity;
import info.truemd.android.helper.CustomHtmlTextParsing;
import info.truemd.android.helper.TrueMDJSONUtils;

/**
 * Created by yashvardhansrivastava on 27/04/16.
 */
public class CustomFAQsQuestionAdapter extends BaseAdapter {
    JSONArray result7;
    Activity context;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomFAQsQuestionAdapter(Activity c1, JSONArray faqJOA ) {
        // TODO Auto-generated constructor stub

        result7=faqJOA;

        context=c1;
        //imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result7.length();
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
            if (result7.getJSONObject(position).length() < 0)
                rowView = inflater.inflate(R.layout.list_item_search_null, null);
            else {
                rowView = inflater.inflate(R.layout.list_item_faqs, null);


                holder.tv1 = (TextView) rowView.findViewById(R.id.textView);


                Typeface tf_r = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                Typeface tf_b = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                holder.tv1.setTypeface(tf_r);

               final String q= result7.getJSONObject(position).optString("q");
                final String a= result7.getJSONObject(position).optString("a");
                holder.tv1.setText(q);
                //holder.tv2.setText(result2[position]);


                // holder.img.setImageResource(imageId[position]);
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        try {
                            //openbottomsheetanswer
                            openBottomSheet(q,a);
                            //Toast.makeText(context, "You Clicked " + result7.getJSONObject(position), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("FAQsERR: ",""+e.getMessage());
                        }



                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            rowView = inflater.inflate(R.layout.list_item_search_null, null);

        }


        return rowView;
    }

    public void openBottomSheet (String heading, String content) {

        Typeface tf_r= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = context.getLayoutInflater().inflate(R.layout.bottom_sheet_with_answer, null);
        TextView txth = (TextView)view.findViewById( R.id.tvq);
        TextView txtc = (TextView)view.findViewById( R.id.tva);
      //  TextView txtl = (TextView)view.findViewById( R.id.txt_link);

        txth.setTypeface(tf_b);
        txtc.setTypeface(tf_r);
       // txtl.setTypeface(tf_r);

        txth.setText(heading);
        txtc.setText(CustomHtmlTextParsing.parse(content));




        final Dialog mBottomSheetDialog = new Dialog (context,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


    }

}