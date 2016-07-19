package info.truemd.android.adapter;

/**
 * Created by yashvardhansrivastava on 04/07/16.
 */


        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Typeface;
        import android.os.AsyncTask;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.andexert.library.RippleView;
        import com.bumptech.glide.Glide;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;

        import info.truemd.android.R;
        import info.truemd.android.activity.FAQsQuestionActivity;

/**
 * Created by HP on 5/11/2016.
 */

public class CustomAndroidGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private final String[] string;
    private final String[] Imageid;
    private final ArrayList<JSONArray> faqs;

    public CustomAndroidGridViewAdapter(Context c, String[] string, String[] Imageid, ArrayList<JSONArray> faqslist) {
        mContext = c;
        this.Imageid = Imageid;
        this.string = string;
        this.faqs = faqslist;
    }

    @Override
    public int getCount() {
        return string.length;
    }

    @Override
    public Object getItem(int p) {
        return null;
    }

    @Override
    public long getItemId(int p) {
        return 0;
    }

    @Override
    public View getView(final int p, View convertView, ViewGroup parent) {
        View grid;
        TextView textView ;//= (TextView) grid.findViewById(R.id.gridview_text);
        ImageView imageView;// = (ImageView)grid.findViewById(R.id.gridview_image);
        RippleView ripple;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_view_item_help, null);
            textView = (TextView) grid.findViewById(R.id.gridview_text);
             imageView = (ImageView)grid.findViewById(R.id.gridview_image);
            ripple = (RippleView) grid.findViewById(R.id.more);
            Typeface tf_l= Typeface.createFromAsset(mContext.getAssets(),"fonts/OpenSans-Regular.ttf");
            textView.setTypeface(tf_l);
            textView.setText(string[p]);

           // imageView.setImageResource(Imageid[p]);

            Log.e("Offers: ",p+": "+Imageid[p]);

            Glide
                    .with(mContext)
                    .load(Imageid[p])
                    .centerCrop()
                    .placeholder(R.drawable.contact_us)
                    .crossFade()
                    .into(imageView);

            //new ImageLoadTask(Imageid[p], imageView).execute();
            grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent tofaqslist = new Intent(mContext, FAQsQuestionActivity.class);
                        Log.e("GridAdapter: ","clicked: "+p+" : "+ faqs.get(p).toString());
                        tofaqslist.putExtra("FaqsForTopics", faqs.get(p).toString() );
                        mContext.startActivity(tofaqslist);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("GridAdapterErr: ",""+e.getMessage());
                    }
                }
            });
            ripple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent tofaqslist = new Intent(mContext, FAQsQuestionActivity.class);
                        Log.e("GridAdapter: ","clicked: "+p+" : "+ faqs.get(p).toString());
                        tofaqslist.putExtra("FaqsForTopics", faqs.get(p).toString() );
                        mContext.startActivity(tofaqslist);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("GridAdapterErr: ",""+e.getMessage());
                    }
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent tofaqslist = new Intent(mContext, FAQsQuestionActivity.class);
                        Log.e("GridAdapter: ","clicked: "+p+" : "+ faqs.get(p).toString());
                        tofaqslist.putExtra("FaqsForTopics", faqs.get(p).toString() );
                        mContext.startActivity(tofaqslist);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("GridAdapterErr: ",""+e.getMessage());
                    }
                }
            });


        } else {
            grid = (View) convertView;
        }

        return grid;
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }

}