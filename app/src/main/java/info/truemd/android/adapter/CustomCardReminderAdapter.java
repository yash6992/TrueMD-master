package info.truemd.android.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import info.truemd.android.R;

public class CustomCardReminderAdapter extends BaseAdapter {

    private JSONArray pouchesToDisplay;
    Context context;
    private static LayoutInflater inflater=null;

    public CustomCardReminderAdapter(Context c1, JSONArray data) {
        this.pouchesToDisplay = data;
        this.context = c1;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pouchesToDisplay.length();
    }

    @Override
    public JSONObject getItem(int position) {
        JSONObject obj=new JSONObject();
        try {
             obj=pouchesToDisplay.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  obj;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.card_reminder, parent, false);
        JSONObject pouch =new JSONObject();

        try {
             pouch = pouchesToDisplay.getJSONObject(position);
            JSONArray meds =pouch.getJSONArray("meds");
            int medlength = meds.length();

        TextView textViewCard = (TextView) convertView.findViewById(R.id.cr_meal);

        Typeface tf_l= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(context.getAssets(), "fonts/Aldrich-Regular.ttf");


        TextView date = (TextView) convertView.findViewById(R.id.cr_date);
        TextView time = (TextView) convertView.findViewById(R.id.cr_time);
        TextView name = (TextView) convertView.findViewById(R.id.cr_patient_name);
        TextView dayno = (TextView) convertView.findViewById(R.id.cr_dayno);

        TextView med1 = (TextView) convertView.findViewById(R.id.cr_med1);
        TextView med2 = (TextView) convertView.findViewById(R.id.cr_med2);
        TextView med3 = (TextView) convertView.findViewById(R.id.cr_med3);
        TextView med4 = (TextView) convertView.findViewById(R.id.cr_med4);
            TextView med5 = (TextView) convertView.findViewById(R.id.cr_med5);

        TextView d1 = (TextView) convertView.findViewById(R.id.cr_dosage1);
        TextView d2 = (TextView) convertView.findViewById(R.id.cr_dosage2);
        TextView d3 = (TextView) convertView.findViewById(R.id.cr_dosage3);
        TextView d4 = (TextView) convertView.findViewById(R.id.cr_dosage4);
            TextView d5 = (TextView) convertView.findViewById(R.id.cr_dosage5);

        ImageView maen = (ImageView) convertView.findViewById(R.id.cr_day_image);

        date.setTypeface(tf_l); med1.setTypeface(tf_l); d1.setTypeface(tf_l);
        time.setTypeface(tf_b); med2.setTypeface(tf_l); d2.setTypeface(tf_l);
        name.setTypeface(tf_l); med3.setTypeface(tf_l); d3.setTypeface(tf_l);
        dayno.setTypeface(tf_l); med4.setTypeface(tf_l); d4.setTypeface(tf_l);
            med5.setTypeface(tf_l); d5.setTypeface(tf_l);


        date.setText(""+pouch.getString("date")); med1.setText(""); d1.setText("");
        time.setText(""+pouch.getString("time")); med2.setText(""); d2.setText("");
        name.setText(""+pouch.getString("patient_name"));med3.setText(""); d3.setText("");
        dayno.setText("Day "+pouch.getString("day_no")); med4.setText(""); d4.setText("");
            med5.setText(""); d5.setText("");
            String tod="morning";

            if(pouch.has("time_of_day")){  tod = pouch.getString("time_of_day");}

            if(tod.equalsIgnoreCase("morning"))
            {
              maen.setBackground(context.getResources().getDrawable(R.drawable.sunrise));
            }
            else if(tod.equalsIgnoreCase("afternoon"))
            {
                maen.setBackground(context.getResources().getDrawable(R.drawable.sunny));
            }
            else if(tod.equalsIgnoreCase("evening"))
            {
                maen.setBackground(context.getResources().getDrawable(R.drawable.sunset_1));
            }
            else if(tod.equalsIgnoreCase("night"))
            {
                maen.setBackground(context.getResources().getDrawable(R.drawable.moon_9));
            }
            else
            {
                maen.setBackground(context.getResources().getDrawable(R.drawable.morning_b));
            }

            switch (medlength)
            {
                case 1:
                {
                    JSONObject jmed1 = meds.getJSONObject(0);
                    med1.setText(""+jmed1.getString("name")); d1.setText(""+jmed1.getString("quantity"));
                    med2.setVisibility(View.GONE); d2.setVisibility(View.GONE);
                    med3.setVisibility(View.GONE); d3.setVisibility(View.GONE);
                    med4.setVisibility(View.GONE); d4.setVisibility(View.GONE);
                    med5.setVisibility(View.GONE); d5.setVisibility(View.GONE);

                }
                break;
                case 2:
                {
                    JSONObject jmed1 = meds.getJSONObject(0);
                    JSONObject jmed2 = meds.getJSONObject(1);
                    med1.setText(""+jmed1.getString("name")); d1.setText(""+jmed1.getString("quantity"));
                    med2.setText(""+jmed2.getString("name")); d2.setText(""+jmed2.getString("quantity"));
                    med3.setVisibility(View.GONE); d3.setVisibility(View.GONE);
                    med4.setVisibility(View.GONE); d4.setVisibility(View.GONE);
                    med5.setVisibility(View.GONE); d5.setVisibility(View.GONE);

                }
                break;
                case 3:
                {
                    JSONObject jmed1 = meds.getJSONObject(0);
                    JSONObject jmed2 = meds.getJSONObject(1);
                    JSONObject jmed3 = meds.getJSONObject(2);
                    med1.setText(""+jmed1.getString("name")); d1.setText(""+jmed1.getString("quantity"));
                    med2.setText(""+jmed2.getString("name")); d2.setText(""+jmed2.getString("quantity"));
                    med3.setText(""+jmed3.getString("name")); d3.setText(""+jmed3.getString("quantity"));
                    med4.setVisibility(View.GONE); d4.setVisibility(View.GONE);
                    med5.setVisibility(View.GONE); d5.setVisibility(View.GONE);

                }
                break;
                case 4:
                {
                    JSONObject jmed1 = meds.getJSONObject(0);
                    JSONObject jmed2 = meds.getJSONObject(1);
                    JSONObject jmed3 = meds.getJSONObject(2);
                    JSONObject jmed4 = meds.getJSONObject(3);
                    med1.setText(""+jmed1.getString("name")); d1.setText(""+jmed1.getString("quantity"));
                    med2.setText(""+jmed2.getString("name")); d2.setText(""+jmed2.getString("quantity"));
                    med3.setText(""+jmed3.getString("name")); d3.setText(""+jmed3.getString("quantity"));
                    med4.setText(""+jmed4.getString("name")); d4.setText(""+jmed4.getString("quantity"));
                    med5.setVisibility(View.GONE); d5.setVisibility(View.GONE);

                }
                break;
                case 5:
                {
                    JSONObject jmed1 = meds.getJSONObject(0);
                    JSONObject jmed2 = meds.getJSONObject(1);
                    JSONObject jmed3 = meds.getJSONObject(2);
                    JSONObject jmed4 = meds.getJSONObject(3);
                    JSONObject jmed5 = meds.getJSONObject(4);
                    med1.setText(""+jmed1.getString("name")); d1.setText(""+jmed1.getString("quantity"));
                    med2.setText(""+jmed2.getString("name")); d2.setText(""+jmed2.getString("quantity"));
                    med3.setText(""+jmed3.getString("name")); d3.setText(""+jmed3.getString("quantity"));
                    med4.setText(""+jmed4.getString("name")); d4.setText(""+jmed4.getString("quantity"));
                    med5.setText(""+jmed5.getString("name")); d5.setText(""+jmed5.getString("quantity"));
                }
                break;
                default:
                {
                    JSONObject jmed1 = meds.getJSONObject(0);
                    JSONObject jmed2 = meds.getJSONObject(1);
                    JSONObject jmed3 = meds.getJSONObject(2);
                    JSONObject jmed4 = meds.getJSONObject(3);
                    JSONObject jmed5 = meds.getJSONObject(4);
                    med1.setText(""+jmed1.getString("name")); d1.setText(""+jmed1.getString("quantity"));
                    med2.setText(""+jmed2.getString("name")); d2.setText(""+jmed2.getString("quantity"));
                    med3.setText(""+jmed3.getString("name")); d3.setText(""+jmed3.getString("quantity"));
                    med4.setText(""+jmed4.getString("name")); d4.setText(""+jmed4.getString("quantity"));
                    med5.setText(""+jmed5.getString("name")); d5.setText(""+jmed5.getString("quantity"));

                    textViewCard.setVisibility(View.VISIBLE);
                }

                break;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //textViewCard.setText(pouchesToDisplay.get(position));

        return convertView;
    }
}
