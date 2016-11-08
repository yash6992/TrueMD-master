package com.truemdhq.projectx.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import com.truemdhq.projectx.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;

import com.truemdhq.projectx.activity.MainActivity;
import com.truemdhq.projectx.activity.TrueMDSiriFragment;
import com.truemdhq.projectx.helper.CustomHtmlTextParsing;
import com.truemdhq.projectx.helper.LongClickLinkMovementMethod;
import com.truemdhq.projectx.helper.TrueMDJSONUtils;
import com.truemdhq.projectx.model.ChatMessage;

/**
 * Created by Technovibe on 17-04-2015.
 */
public class ChatAdapter extends BaseAdapter {

    private final List<ChatMessage> chatMessages;
    private TrueMDSiriFragment fragment;
    private Activity context;boolean wordClicked = false;

    Vibrator vibrator ;//= (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);


    public ChatAdapter(Activity context, List<ChatMessage> chatMessages, TrueMDSiriFragment fragment) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.fragment = fragment;
        this.vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public ChatMessage getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final ChatMessage chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        boolean myMsg = chatMessage.getIsme() ;//Just a dummy check to simulate whether it me or other sender
        boolean isClickable = chatMessage.isOnClickable();
        setAlignment(holder, myMsg, isClickable);
        String htmlS= chatMessage.getMessage();
        String htmlS1=htmlS.replace("<li>", "<br /> \u2022 ");
        String htmlS2=htmlS1.replace("</li>", " ");
        String htmlS3=htmlS2.replace("</h3>", "</b>");
        String htmlS4=htmlS3.replace("<h3>", "<br /><b>");
        //Log.e("htmlS: ",htmlS);
        //htmlS = htmlS.replace("</li>"," \n");

        //StringBuffer htmlSB = new StringBuffer(chatMessage.getMessage());

       // htmlSB.replace("<li>", "\u2022 ");//.replace("</li>", " \n").replace("</h3>", "</h4>").replace("<h3>", "<h4>");

        if(chatMessage.getMessage().equals("...")) {

            holder.avloader.setVisibility(View.VISIBLE);
            holder.txtMessage.setVisibility(View.GONE);

        }
        else {
            holder.avloader.setVisibility(View.GONE);
            holder.txtMessage.setVisibility(View.VISIBLE);
            Spanned htmlAsSpanned = Html.fromHtml(htmlS4);


            //holder.txtMessage.setText(htmlAsSpanned);
            if(!chatMessage.isOnLongPressable()) {
                holder.txtMessage.setText(htmlAsSpanned);
            }
            else {
                String definition = htmlAsSpanned.toString();
                holder.txtMessage.setMovementMethod(LongClickLinkMovementMethod.getInstance());
                holder.txtMessage.setText(definition, TextView.BufferType.SPANNABLE);
                Spannable spans = (Spannable) holder.txtMessage.getText();
                BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
                iterator.setText(definition);
                int start = iterator.first();
                for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                    .next()) {
                    String possibleWord = definition.substring(start, end);
                    if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                        LongClickableSpan clickSpan = getClickableSpan(possibleWord);
                        spans.setSpan(clickSpan, start, end,
                             Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

        }


        if (!isClickable) {
            long dateLong=chatMessage.getDate();
            holder.txtInfo.setVisibility(View.VISIBLE);

            String timeAgo =(String) DateUtils.getRelativeTimeSpanString( dateLong, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);

            holder.txtInfo.setText(timeAgo);
        }
        else{
            holder.txtInfo.setVisibility(View.GONE);
        }

        holder.content.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                if (chatMessage.isOnClickable())
                {
                    //send text to step1
                    //Toast.makeText(context,chatMessage.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    fragment.askFromAdapter(chatMessage.getMessage().toString());
                }


            }
        });


        return convertView;
    }

    public void add(ChatMessage message) {
        chatMessages.add(message);
    }

    public void add(List<ChatMessage> messages) {
        chatMessages.addAll(messages);
    }

    public void deleteLastMessage(){
        chatMessages.remove(chatMessages.size() - 1);
    }

    private void setAlignment(ViewHolder holder, boolean isMe, boolean isClickable) {
        if (!isMe) {

            if (isClickable) {
                holder.contentWithBG.setBackgroundResource(R.drawable.button_purple_ghost_squ);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.contentWithBG.setLayoutParams(layoutParams);

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                holder.content.setLayoutParams(lp);
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtMessage.setTextColor(context.getResources().getColor(R.color.windowBackground));
                holder.txtMessage.setLayoutParams(layoutParams);

                layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtInfo.setLayoutParams(layoutParams);
            } else {
                holder.contentWithBG.setBackgroundResource(R.drawable.msg_in_bubble);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.contentWithBG.setLayoutParams(layoutParams);

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                holder.content.setLayoutParams(lp);
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtMessage.setLayoutParams(layoutParams);
                holder.txtMessage.setTextColor(context.getResources().getColor(R.color.white));

                layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtInfo.setLayoutParams(layoutParams);
            }
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.msg_out_bubble);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            holder.txtMessage.setTextColor(context.getResources().getColor(R.color.white));

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }

    private ViewHolder createViewHolder(View v) {
        final ViewHolder holder = new ViewHolder();


        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content_chat);
        holder.avloader = (AVLoadingIndicatorView)v.findViewById(R.id.chat_avloadingIndicatorView);
        Typeface tf_r= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        holder.txtMessage.setTypeface(tf_r);
        holder.content = (LinearLayout) v.findViewById(R.id.content_chat);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);

//        holder.txtMessage.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menu_item_share:
//                        String selectedText = getSelectedText(holder);
//                        shareIntent(selectedText);
//                        return true;
//                    default:
//                        break;
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                // TODO Auto-generated method stub
//                ((MainActivity)context).getMenuInflater().inflate(R.menu.share_menu, menu);
//                return true;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode arg0) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                /**
//                 * Use the following code if you want to remove the default icons (select all, cut or copy).
//                 */
//                // Remove the "select all" option
//                menu.removeItem(android.R.id.selectAll);
//                // Remove the "cut" option
//                menu.removeItem(android.R.id.cut);
//                // Remove the "copy all" option
//                menu.removeItem(android.R.id.copy);
//                return false;
//            }
//
//        });
//
         return holder;
    }


    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
        public AVLoadingIndicatorView avloader;
    }

    private LongClickableSpan getClickableSpan(final String word) {
        return new LongClickableSpan() {

            final String mWord;
            {
                mWord = word;
            }

            @Override
            public void onClick(View widget) {
//                Log.d("tapped on:", mWord);
//                Toast.makeText(widget.getContext(), mWord, Toast.LENGTH_SHORT)
//                        .show();

            }

            @Override
            public void onLongClick(View widget) {
                Log.d("tapped on:", mWord);
                Toast.makeText(widget.getContext(), "Searching for: "+mWord, Toast.LENGTH_SHORT)
                        .show();

                vibrator.vibrate(150);
                new DictionaryTask().execute(mWord);
            }

            public void updateDrawState(TextPaint ds) {



                //super.updateDrawState(ds);
            }
        };
    }

    public abstract class LongClickableSpan extends ClickableSpan {

        abstract public void onLongClick(View view);

    }

    public void openBottomSheet (String heading, String content, String link) {

        Typeface tf_r= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = context.getLayoutInflater().inflate(R.layout.bottom_sheet_dictionary, null);
        TextView txth = (TextView)view.findViewById( R.id.tv2);
        TextView txtc = (TextView)view.findViewById( R.id.txt_content);
        TextView txtl = (TextView)view.findViewById( R.id.txt_link);

        txth.setTypeface(tf_b);
        txtc.setTypeface(tf_r);
        txtl.setTypeface(tf_r);

        txth.setText(heading);
        txtc.setText(CustomHtmlTextParsing.parse(content));
        txtl.setText(link);



        final Dialog mBottomSheetDialog = new Dialog (context,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


    }
    private class DictionaryTask extends AsyncTask<String, String, String[]> {

        private String[] resp=new String[3];

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog


        }

        @Override
        protected String[] doInBackground(String... params) {

            try {
                String temp=java.net.URLEncoder.encode(params[0], "UTF-8");
                URL js = new URL(MainActivity.app_url+"/dictionary.json?q="+temp+"&source=0"+"&key="+MainActivity.dev_key);
                URLConnection jc = js.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                String line = reader.readLine();
                JSONObject jsonResponse = new JSONObject(line);

                Log.e("Siri resp: ", js.toString());
                Log.e("Siri resp: ", line);

                JSONObject response = jsonResponse.getJSONObject("response");

                resp[0] = TrueMDJSONUtils.goThroughNullCheck(response.getString("heading"));
                resp[1] =  TrueMDJSONUtils.goThroughNullCheck(response.getString("content"));
                resp[2] =  TrueMDJSONUtils.goThroughNullCheck(response.getString("link"));


            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            return resp;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // execution of result of Long time consuming operation
            //finalResult.setText(result);

            openBottomSheet(resp[0],resp[1],resp[2]);

            resp= new String[3];

        }

        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }
}
