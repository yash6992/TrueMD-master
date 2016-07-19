package info.truemd.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import info.truemd.android.R;
import info.truemd.android.activity.MedicineDetailsActivity2;
import info.truemd.android.activity.PrescriptionDetailsActivity;
import info.truemd.android.activity.PreviousOrderActivity;
import info.truemd.android.helper.TrueMDJSONUtils;

/**
 * Created by yashvardhansrivastava on 27/04/16.
 */
public class CustomPrescriptionMedAdapter extends BaseAdapter {
     JSONObject[] medObjArray;
    Context context;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomPrescriptionMedAdapter(Context c1, JSONObject [] medObjA) {
        // TODO Auto-generated constructor stub
        
        medObjArray= medObjA;

        Log.e("PMAdapter: ", ""+medObjArray.length);


        context=c1;
        //imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return medObjArray.length;
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
        TextView tv1,tv2,tv3,tv4,tv5,tv6;
        ImageView img1, img2, img3, img4;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        Holder holder=new Holder(); JSONObject med = new JSONObject();
        View rowView = inflater.inflate(R.layout.list_item_search_null, null);
        String  duration_string, frequency_string;

        try {

            Log.e("PMAdapter: ", medObjArray[position].toString());


            if (medObjArray[position].length() < 1)
                rowView = inflater.inflate(R.layout.list_item_search_null, null);
            else {
                med=medObjArray[position];

                rowView = inflater.inflate(R.layout.list_item_prescription_med, null);

                Log.e("PMAdapter in else: ",medObjArray[position].toString());

                holder.img1 = (ImageView) rowView.findViewById(R.id.pm_morning);
                holder.img2 = (ImageView) rowView.findViewById(R.id.pm_afternoon);
                holder.img3 = (ImageView) rowView.findViewById(R.id.pm_evening);
                holder.img4 = (ImageView) rowView.findViewById(R.id.pm_night);

                holder.tv1 = (TextView) rowView.findViewById(R.id.pm_med_name);
                holder.tv2 = (TextView) rowView.findViewById(R.id.pm_med_type);
                holder.tv3 = (TextView) rowView.findViewById(R.id.pm_frequency);
                holder.tv4 = (TextView) rowView.findViewById(R.id.pm_duration_h);
                holder.tv5 = (TextView) rowView.findViewById(R.id.pm_duration);
                holder.tv6 = (TextView) rowView.findViewById(R.id.pm_meal);
                
                Typeface tf_r = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                Typeface tf_b = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                holder.tv1.setTypeface(tf_b);
                holder.tv2.setTypeface(tf_r);
                holder.tv3.setTypeface(tf_r);
                holder.tv4.setTypeface(tf_r);
                holder.tv5.setTypeface(tf_r);
                holder.tv6.setTypeface(tf_r);

                String med_name= med.getString("med_name");
                String type=med.getString("type");
                String frequency_daily=""+ TrueMDJSONUtils.goThroughNullCheck(med.getString("daily_frequency"));
                String frequency_weekly=""+TrueMDJSONUtils.goThroughNullCheck(med.getString("weekly_frequency"));
                String frequency_monthly=""+TrueMDJSONUtils.goThroughNullCheck(med.getString("monthly_frequency"));

                Log.e("", frequency_daily+":"+frequency_weekly+":"+frequency_monthly+":");

                String quantity= ""+med.getString("quantity")+" "+type+" ";


                String duration_type=med.getString("duration_type");

                if(duration_type.equalsIgnoreCase("sos"))
                {
                    duration_string="SOS";
                    frequency_string="to be taken when needed";
                }
                else
                {
                    int duration=med.getInt("duration");
                    duration_string=""+duration+" "+ duration_type;

                    if (frequency_daily.equalsIgnoreCase("0.5")&&frequency_weekly.equalsIgnoreCase("")&&frequency_monthly.equalsIgnoreCase(""))
                    {
                        frequency_string=quantity+"to be taken once in 2 days";
                    }
                    else  if (frequency_daily.equalsIgnoreCase("0.25")&&frequency_weekly.equalsIgnoreCase("")&&frequency_monthly.equalsIgnoreCase(""))
                    {
                        frequency_string=quantity+"to be taken once in 4 days";
                    }
                    else  if (frequency_daily.equalsIgnoreCase("0.33")&&frequency_weekly.equalsIgnoreCase("")&&frequency_monthly.equalsIgnoreCase(""))
                    {
                        frequency_string=quantity+"to be taken once in 3 days";
                    }
                    else  if (frequency_daily.equalsIgnoreCase("0.2")&&frequency_weekly.equalsIgnoreCase("")&&frequency_monthly.equalsIgnoreCase(""))
                    {
                        frequency_string=quantity+"to be taken once in 5 days";
                    }
                    else  if (!frequency_daily.equalsIgnoreCase("")&&frequency_weekly.equalsIgnoreCase("")&&frequency_monthly.equalsIgnoreCase(""))
                    {
                        frequency_string=quantity+"to be taken "+frequency_daily+" times a day ";
                    }
                    else  if (frequency_daily.equalsIgnoreCase("")&&!frequency_weekly.equalsIgnoreCase("")&&frequency_monthly.equalsIgnoreCase(""))
                    {
                        frequency_string=quantity+"to be taken "+frequency_weekly+" times a week ";
                    }
                    else  if (frequency_daily.equalsIgnoreCase("")&&frequency_weekly.equalsIgnoreCase("")&&!frequency_monthly.equalsIgnoreCase(""))
                    {
                        frequency_string=quantity+"to be taken "+frequency_monthly+" times a month ";
                    }
                    else  if (!frequency_daily.equalsIgnoreCase("")&&!frequency_weekly.equalsIgnoreCase("")&&frequency_monthly.equalsIgnoreCase(""))
                    {
                        frequency_string=quantity+"to be taken "+frequency_weekly+" times a week, "+ frequency_daily +" times a day";
                    }
                    else
                    {
                        frequency_string="As prescribed by the doctor";
                    }

                }



                String regime=med.getString("regime");
                String rm=""+regime.charAt(0);String ra=""+regime.charAt(1);
                String re=""+regime.charAt(2);String rn=""+regime.charAt(3);

                if(rm.equalsIgnoreCase("0"))
                {
                    holder.img1.setImageDrawable(context.getResources().getDrawable(R.drawable.morning_g));
                    holder.img1.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_afternoon));
                }
                if(ra.equalsIgnoreCase("0"))
                {
                    holder.img2.setImageDrawable(context.getResources().getDrawable(R.drawable.afternoon_g));
                    holder.img2.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_afternoon));
                }
                if(re.equalsIgnoreCase("0"))
                {
                    holder.img3.setImageDrawable(context.getResources().getDrawable(R.drawable.evening_g));
                    holder.img3.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_afternoon));
                }
                if(rn.equalsIgnoreCase("0"))
                {
                    holder.img4.setImageDrawable(context.getResources().getDrawable(R.drawable.night_g));
                    holder.img4.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_afternoon));
                }



                String meal=med.getString("meal") +" meals";//"to be taken after meals";
                if(meal.equalsIgnoreCase("na meals"))
                {
                    meal=med.getString("comments");
                }



                
                holder.tv1.setText(med_name);
                holder.tv2.setText(type);
                holder.tv3.setText(frequency_string);
                //holder.tv4.setText(tf_r);
                holder.tv5.setText(duration_string);
                holder.tv6.setText(meal);

                // holder.img.setImageResource(imageId[position]);
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        try {

                            JSONObject obj = medObjArray[position];
                            //Toast.makeText(context, "You Clicked " + "" + obj.getString("truemdCode") + ":" + obj.getString("med_name"), Toast.LENGTH_SHORT).show();

                            Intent intent_main_search = new Intent(context, MedicineDetailsActivity2.class);
                            intent_main_search.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent_main_search.putExtra("truemdCode", "" + obj.getString("truemdCode") + ":" + obj.getString("med_name"));

                            Log.e("PMAdapter", "" + obj.getString("truemdCode") + ":" + obj.getString("med_name"));

                            context.startActivity(intent_main_search);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PMError", e.getMessage());
            rowView = inflater.inflate(R.layout.list_item_search_null, null);

        }


        return rowView;
    }

}