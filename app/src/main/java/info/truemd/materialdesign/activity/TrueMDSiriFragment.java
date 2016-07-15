package info.truemd.materialdesign.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.truemd.materialdesign.R;
import info.truemd.materialdesign.adapter.ChatAdapter;
import info.truemd.materialdesign.app.ConfigOTP;
import info.truemd.materialdesign.helper.SessionManager;
import info.truemd.materialdesign.helper.T;
import info.truemd.materialdesign.model.ChatMessage;
import info.truemd.materialdesign.model.ResponseChat;
import info.truemd.materialdesign.model.SuggestGetSet;
import info.truemd.materialdesign.service.ConnectionDetector;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 07/03/16.
 */
public class TrueMDSiriFragment extends Fragment {

    public Context context;
    private EditText messageET;
    private ListView messagesContainer;
    private ImageButton sendBtn, showOptionsBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    View rootView;
    boolean text; int k;
    SweetAlertDialog pSweetDialog;
    String[] stringItems,stringCodeItems;
    String intentS1,intent_override_flagS1,step2_flagS1;
    int noOfMedicine;
    ArrayList<ResponseChat> responseS1;
    SessionManager session;
    DialogPlus dialog;
    private final int REQ_CODE_SPEECH_INPUT = 100;



    public TrueMDSiriFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_chat_2, container, false); intentS1="na";
        context = getActivity(); step2_flagS1="false"; intent_override_flagS1="true";
        session = new SessionManager(context);
        responseS1 = new ArrayList<>();

        Paper.book("nav").write("selected","3");


        new MainActivity().checkInternet(getActivity());


        initControls();

        // Inflate the layout for this fragment
        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initControls() {
        stringItems = new String[15];
        stringCodeItems = new String[15];
        text = false;
        k = 0;
        messagesContainer = (ListView) rootView.findViewById(R.id.messagesContainer);
        messageET = (EditText) rootView.findViewById(R.id.messageEdit);

        Typeface tf_r = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        messageET.setTypeface(tf_r);

        sendBtn = (ImageButton) rootView.findViewById(R.id.chatSendButton);
        showOptionsBtn = (ImageButton) rootView.findViewById(R.id.chatOptionsButton);

        showOptionsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                openBottomSheet(rootView);
            }
        });

        RelativeLayout container = (RelativeLayout) rootView.findViewById(R.id.container);

//        dialog = DialogPlus.newDialog(context)
//                .setAdapter(adapter)
//                .setOnItemClickListener(new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
//                    }
//                })
//                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
//                .create();


        loadDummyHistory();


            //voice command text to speech and then send
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (messageET.getText().toString().length() == 0) {

                        new MainActivity().checkInternet(getActivity());
                        promptSpeechInput();
                    } else {

                                String messageText = messageET.getText().toString();

                                new MainActivity().checkInternet(getActivity());

                                if (TextUtils.isEmpty(messageText)) {
                                    return;
                                }

                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.setId(++k);//dummy
                                chatMessage.setMessage(messageText);
                                chatMessage.setDate(System.currentTimeMillis());
                                chatMessage.setMe(true);
                                chatMessage.setOnClickable(false);
                                chatMessage.setOnLongPressable(false);


                                messageET.setText("");

                                displayMessage(chatMessage);

                                Log.e("Siri input: ", messageText);

                                new ExecuteNetworkOperation().execute(messageText);

                    }
                }
            });


            messageET.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {

                    // you can call or do what you want with your EditText here
                    if (messageET.getText().toString().length() == 0) {
                        sendBtn.setBackgroundResource(R.drawable.ic_keyboard_voice_black_24dp);
                        text = false;
                    } else {
                        sendBtn.setBackgroundResource(R.drawable.ic_send_black_24dp);
                        text = true;
                    }


                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    if (messageET.getText().toString().length() == 0) {
                        sendBtn.setBackgroundResource(R.drawable.ic_keyboard_voice_black_24dp);
                        text = false;
                    } else {
                        sendBtn.setBackgroundResource(R.drawable.ic_send_black_24dp);
                        text = true;
                    }

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (messageET.getText().toString().length() == 0) {
                        sendBtn.setBackgroundResource(R.drawable.ic_keyboard_voice_black_24dp);
                        text = false;
                    } else {
                        sendBtn.setBackgroundResource(R.drawable.ic_send_black_24dp);
                        text = true;
                    }


                }
            });



    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }
    public void deleteLastMessage() {
        adapter.deleteLastMessage();
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){

        chatHistory = new ArrayList<ChatMessage>();

        adapter = new ChatAdapter((MainActivity)context, new ArrayList<ChatMessage>(), TrueMDSiriFragment.this);
        messagesContainer.setAdapter(adapter);

        new InitializeNetworkOperation().execute(MainActivity.chatInitializerTrueMDCode);
        MainActivity.fromThankYou=false; MainActivity.fromHomeToChat = true;


//        ChatMessage msg = new ChatMessage();
//        msg.setId(++k);
//        msg.setMe(false);
//        msg.setMessage("Hi...<br>I can help you find information on medicine and diseases.");
//        msg.setDate(System.currentTimeMillis());
//        chatHistory.add(msg);
//        msg.setOnClickable(false);
//        msg.setOnLongPressable(false);
//        displayMessage(msg);
//
//
//        ChatMessage msg1 = new ChatMessage();
//        msg1.setId(++k);
//        msg1.setMe(false);
//        msg1.setMessage("Try asking: What is the price of Crocin?");
//        msg1.setDate(System.currentTimeMillis());
//        chatHistory.add(msg1);
//        msg1.setOnClickable(false);
//        msg1.setOnLongPressable(false);
//        displayMessage(msg1);
//
//
//
//        ChatMessage msg2 = new ChatMessage();
//        msg2.setId(1);
//        msg2.setMe(false);
//        msg2.setMessage("Long press any word to get more info about it.");
//        msg2.setDate(System.currentTimeMillis());
//        chatHistory.add(msg2);
//        msg2.setOnClickable(false);
//        msg2.setOnLongPressable(false);
//        displayMessage(msg2);
//



    }
    private void ActionSheetDialog() {

        //stringItems = {"接收消息并提醒", "接收消息但不提醒", "收进群助手且不提醒", "屏蔽群消息"};
        final ActionSheetDialog dialog = new ActionSheetDialog(context, stringItems, null);
        dialog.title(""+"Please select the medicine.")//
                .titleTextSize_SP(14.5f)//
                .cancelText("Try again.")
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                T.showShort(context, stringItems[position]);
                dialog.dismiss();

                new ExecuteNetworkOperation2().execute(stringCodeItems[position],stringItems[position]);




            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                Log.e("Dialog: ", "cancel");
            }
        });

        //;mTvCancel.setOnClickListener(new View.OnClickListener() {


    }



    public class ExecuteNetworkOperation extends AsyncTask<String, Integer, String[]> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            /**
             * show dialog
             */
            ChatMessage respMessage = new ChatMessage();
            respMessage.setId(++k);//dummy
            respMessage.setMessage("...");
            respMessage.setDate(System.currentTimeMillis());
            respMessage.setMe(false);
            respMessage.setOnClickable(false);
            respMessage.setOnLongPressable(false);



            displayMessage(respMessage);

            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... messageText) {
            // TODO Auto-generated method stub
            /**
             * Do network related stuff
             * return string response.
             */
            String[] result = new String [15]; stringCodeItems= new String [15];
            int count = messageText.length;
            String temp,message = new String(); String fuzzi ="0";
            //String response = "Didn't get you. Can you rephrase.";

            try {

               if(messageText[0].contains("#")) {
                     fuzzi = messageText[0].trim().substring(0, messageText[0].indexOf('#'));

                   if(fuzzi.equalsIgnoreCase("1")||fuzzi.equalsIgnoreCase("2")||fuzzi.equalsIgnoreCase("3")||fuzzi.equalsIgnoreCase("AUTO")||fuzzi.equalsIgnoreCase("4"))
                       {}
                   else
                        {fuzzi="0";}

                       message = messageText[0].substring(messageText[0].indexOf('#') + 1);

                     //temp=message.replace(" ", "%20");
                  temp = java.net.URLEncoder.encode(message, "UTF-8");
               }
                else{

                     //temp=messageText[0].replace(" ", "%20");
                   temp = java.net.URLEncoder.encode(messageText[0], "UTF-8");
               }



                HashMap<String, String> user = session.getUserDetails();
                //URL js = new URL("http://truemd-carebook.rhcloud.com/api/v2/medicines?key="+MainActivity.dev_key+"&search="+messageText[0]);
                //URL js= new URL("http://truemd-carebook.rhcloud.com/getentity.json?key="+MainActivity.dev_key+"&q="+temp+"&fuzziness="+fuzzi+"&size=15&chat_id="+user.get(SessionManager.KEY_CHAT_ID_UM));
                URL js1= new URL("http://truemd-carebook.rhcloud.com/step1.json?key="+MainActivity.dev_key+"&q="+temp+"&fuzziness="+fuzzi+"&intent_override_flag="+intent_override_flagS1+"&size=5&chat_id="+user.get(SessionManager.KEY_CHAT_ID_UM));
                URLConnection jc = js1.openConnection();

                if(intent_override_flagS1.equalsIgnoreCase("false"))
                    intent_override_flagS1 ="true";
                else
                    intent_override_flagS1="true";


                Log.e("Siri resp: ",""+messageText[0]);
                //Log.e("Siri resp: ", line);
                BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                String line = reader.readLine();
                if (line.length() > 5) {
                    Log.e("Siri resp: ", js1.toString());
                    Log.e("Siri resp: ", line);
                    JSONObject jsonResponse = new JSONObject(line);

                    JSONObject intent1 = jsonResponse.getJSONObject("intent");

                    step2_flagS1 = jsonResponse.getString("step2_flag");
                    intentS1 = intent1.getString("intent");

                    JSONArray jsonArray1= new JSONArray();

                    jsonArray1 = jsonResponse.getJSONArray("response");

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject r = jsonArray1.getJSONObject(i);

                       ResponseChat rco = new ResponseChat();

                        rco.setResponse_text(r.getString("response_text"));
                        rco.setOn_clickable(r.getBoolean("on_clickable"));
                        rco.setOn_longpressable(r.getBoolean("on_longpressable"));
                        rco.setColor(r.getString("color"));

                        responseS1.add(rco);


                    }

                    intent_override_flagS1 = jsonResponse.getString("intent_override_flag");

                    int m=0;
                    JSONObject entities= new JSONObject();
                    JSONArray jsonArray2= new JSONArray();



                    if(step2_flagS1.equalsIgnoreCase("true"))
                    {
                        entities = jsonResponse.getJSONObject("entities");
                        jsonArray2 = entities.getJSONArray("entities");
                        noOfMedicine=jsonArray2.length();
                    }



                    if (noOfMedicine!=0&&step2_flagS1.equalsIgnoreCase("true")) {
                        for (int i = 0; i < noOfMedicine; i++) {
                            JSONObject r = jsonArray2.getJSONObject(i);

                            String name = r.getString("name");
                            String truemdCode = r.getString("truemdCode");


                            if(name.length()!=0) {

                                if(name.length()>30)
                                {   stringItems[m] = name.substring(0,29)+"...";
                                    stringCodeItems[m] = truemdCode;
                                }
                                else
                                {   stringItems[m] = name;
                                    stringCodeItems[m] = truemdCode;
                                }
                                //getSupportActionBar().setTitle(name);
                            }
                            m++;
                        }
                    }else if(intentS1.equalsIgnoreCase("na")) {

                        stringItems= new String[15]; stringCodeItems= new String [15];

                        //step2_flagS1 = "false";
                    }
                    else  {

                        stringItems= new String[15]; stringCodeItems= new String [15];

                        //entities_flag = "false";
                    }

                     result= stringItems;


                }
            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();

            } catch (Exception e) {
                ChatMessage respMessage = new ChatMessage();
                respMessage.setId(++k); //dummy
                respMessage.setMessage("Sorry, there's something wrong. We'll get back to you later.");
                respMessage.setDate(System.currentTimeMillis());
                respMessage.setMe(false);
                respMessage.setOnClickable(false);
                respMessage.setOnLongPressable(false);
                displayMessage(respMessage);
                e.printStackTrace();

            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);

        }

        @Override
        protected void onPostExecute(String[] result) {
            // TODO Auto-generated method stub
            /**
             * update ui thread and remove dialog
             */
            deleteLastMessage();

           if(step2_flagS1.equalsIgnoreCase("false")) {

                displayResponseChat(responseS1);

                responseS1= new ArrayList<>();



                stringItems= new String[15]; stringCodeItems= new String [15];


            } else if(step2_flagS1.equalsIgnoreCase("true") && noOfMedicine!=0) {

               responseS1= new ArrayList<>();
               ActionSheetDialog();

            }
           else
            {
                responseS1= new ArrayList<>();
            }

            super.onPostExecute(result);
        }
    }

    public class ExecuteNetworkOperation2 extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            /**
             * show dialog
             */
            new MainActivity().checkInternet(getActivity());

            ChatMessage respMessage = new ChatMessage();
            respMessage.setId(++k); //dummy
            respMessage.setMessage("...");
            respMessage.setDate(System.currentTimeMillis());
            respMessage.setMe(false);
            respMessage.setOnClickable(false);
            respMessage.setOnLongPressable(false);


            displayMessage(respMessage);


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... messageText) {
            // TODO Auto-generated method stub
            /**
             * Do network related stuff
             * return string response.
             */
            String result = new String ();
            int count = messageText.length;

            try {

                String trueMDCode = java.net.URLEncoder.encode(messageText[0], "UTF-8");
                String trueMDName = java.net.URLEncoder.encode(messageText[1], "UTF-8");



                HashMap<String, String> user = session.getUserDetails();
                 URL js= new URL("http://truemd-carebook.rhcloud.com/step2.json?key="+MainActivity.dev_key+"&chat_id="+user.get(SessionManager.KEY_CHAT_ID_UM)+"&truemdCode="+trueMDCode+"&intent="+intentS1+"&name="+trueMDName);

                URLConnection jc = js.openConnection();
                Log.e("Siri resp: ",""+trueMDCode);
                //Log.e("Siri resp: ", line);
                BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                String line = reader.readLine();
                if (line.length() > 5) {

                    Log.e("Siri resp: ", js.toString());
                    Log.e("Siri resp: ", line);
                    JSONObject jsonResponse = new JSONObject(line);

                    JSONArray jsonArray1= new JSONArray();

                    jsonArray1 = jsonResponse.getJSONArray("response");

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject r = jsonArray1.getJSONObject(i);

                        ResponseChat rco = new ResponseChat();

                        rco.setResponse_text(r.getString("response_text"));
                        rco.setOn_clickable(r.getBoolean("on_clickable"));
                        rco.setOn_longpressable(r.getBoolean("on_longpressable"));
                        rco.setColor(r.getString("color"));

                        responseS1.add(rco);


                    }

                    result=responseS1.get(0).getResponse_text();

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                ChatMessage respMessage = new ChatMessage();
                respMessage.setId(++k); //dummy
                respMessage.setMessage("Sorry, there's something wrong. We'll get back to you later.");
                respMessage.setDate(System.currentTimeMillis());
                respMessage.setMe(false);
                respMessage.setOnClickable(false);
                respMessage.setOnLongPressable(false);
                displayMessage(respMessage);
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            /**
             * update ui thread and remove dialog
             */

            deleteLastMessage();

            displayResponseChat(responseS1);

            responseS1.clear();

            stringItems = new String[15]; stringCodeItems= new String [15];



            super.onPostExecute(result);
        }
    }
    public class InitializeNetworkOperation extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            /**
             * show dialog
             */
            new MainActivity().checkInternet(getActivity());

            ChatMessage respMessage = new ChatMessage();
            respMessage.setId(++k); //dummy
            respMessage.setMessage("...");
            respMessage.setDate(System.currentTimeMillis());
            respMessage.setMe(false);
            respMessage.setOnClickable(false);
            respMessage.setOnLongPressable(false);


            displayMessage(respMessage);


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... messageText) {
            // TODO Auto-generated method stub
            /**
             * Do network related stuff
             * return string response.
             */
            String result = new String ();
            int count = messageText.length;


            try {

                String trueMDCode = java.net.URLEncoder.encode(messageText[0], "UTF-8");
                //String trueMDName = java.net.URLEncoder.encode(messageText[1], "UTF-8");



                HashMap<String, String> user = session.getUserDetails();
                URL js= new URL("http://truemd-carebook.rhcloud.com/initialize_chat.json?chat_id="+user.get(SessionManager.KEY_CHAT_ID_UM)+"&truemdCode="+trueMDCode+"&key="+MainActivity.dev_key);
                Log.e("Siri query: ",""+js.toString());
                URLConnection jc = js.openConnection();
                Log.e("Siri resp: ",""+trueMDCode);
                //Log.e("Siri resp: ", line);
                BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                String line = reader.readLine();
                if (line.length() > 5) {

                    Log.e("Siri resp: ", js.toString());
                    Log.e("Siri resp: ", line);
                    JSONObject jsonResponse = new JSONObject(line);

                    JSONArray jsonArray1= new JSONArray();

                    JSONObject chat,entity;
                    chat=jsonResponse.getJSONObject("chat");
                    entity=chat.getJSONObject("entity");
                    MainActivity.chatInitializerTrueMDName=entity.getString("primary_entity_name");

                    jsonArray1 = jsonResponse.getJSONArray("response");

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject r = jsonArray1.getJSONObject(i);

                        ResponseChat rco = new ResponseChat();

                        rco.setResponse_text(r.getString("response_text"));
                        rco.setOn_clickable(r.getBoolean("on_clickable"));
                        rco.setOn_longpressable(r.getBoolean("on_longpressable"));
                        rco.setColor(r.getString("color"));

                        responseS1.add(rco);


                    }

                    result=responseS1.get(0).getResponse_text();

                }
            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                ChatMessage respMessage = new ChatMessage();
                respMessage.setId(++k); //dummy
                respMessage.setMessage("Sorry, there's something wrong. We'll get back to you later.");
                respMessage.setDate(System.currentTimeMillis());
                respMessage.setMe(false);
                respMessage.setOnClickable(false);
                respMessage.setOnLongPressable(false);
                displayMessage(respMessage);
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            /**
             * update ui thread and remove dialog
             */

            deleteLastMessage();

            displayResponseChat(responseS1);

            responseS1.clear();

            stringItems = new String[15]; stringCodeItems= new String [15];



            super.onPostExecute(result);
        }
    }
    public void openBottomSheet (View v) {

        Typeface tf_r= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");

        View view = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        TextView txtse = (TextView)view.findViewById( R.id.txt_sideEffects);
        TextView txtuf = (TextView)view.findViewById( R.id.txt_usedFor);
        TextView txtps = (TextView)view.findViewById( R.id.txt_pregnancySafe);
        EditText et = (EditText)view.findViewById(R.id.messageEditBS);
        ImageButton sib = (ImageButton) view.findViewById(R.id.chatSendButtonBS);
        ImageButton cob = (ImageButton) view.findViewById(R.id.chatOptionsButtonBS);
        TextView txtp = (TextView)view.findViewById( R.id.txt_price);
        TextView txts = (TextView)view.findViewById( R.id.txt_substitutes);
        TextView txttc = (TextView)view.findViewById( R.id.txt_tClass);

        txtuf.setTypeface(tf_r);
        txtps.setTypeface(tf_r);
        txtp.setTypeface(tf_r);
        et.setTypeface(tf_r);
        txtse.setTypeface(tf_r);
        txts.setTypeface(tf_r);
        txttc.setTypeface(tf_r);


        final Dialog mBottomSheetDialog = new Dialog (context,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow ().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //  Toast.makeText(context,"Clicked Backup",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.cancel();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        });
        cob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //  Toast.makeText(context,"Clicked Backup",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.cancel();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        });

        txtse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //  Toast.makeText(context,"Clicked Backup",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();

                new MainActivity().checkInternet(getActivity());
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(++k);//dummy
                chatMessage.setMessage("What are it's side effects?");
                chatMessage.setDate(System.currentTimeMillis());
                chatMessage.setMe(true);
                chatMessage.setOnClickable(false);
                chatMessage.setOnLongPressable(false);


                messageET.setText("");

                displayMessage(chatMessage);

                Log.e("Siri input: ", "What are it's side effects?");

                new ExecuteNetworkOperation().execute("What are it's side effects?");
            }
        });

        txtuf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //  Toast.makeText(context,"Clicked Detail",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
                new MainActivity().checkInternet(getActivity());
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(++k);//dummy
                chatMessage.setMessage("what is it used for?");
                chatMessage.setDate(System.currentTimeMillis());
                chatMessage.setMe(true);
                chatMessage.setOnClickable(false);
                chatMessage.setOnLongPressable(false);

                messageET.setText("");

                displayMessage(chatMessage);

                Log.e("Siri input: ", "what is it used for?");

                new ExecuteNetworkOperation().execute("what is it used for?");
            }
        });

        txtps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // Toast.makeText(context,"Clicked Open",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
                new MainActivity().checkInternet(getActivity());
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(++k);//dummy
                chatMessage.setMessage("Is it safe during pregnancy?");
                chatMessage.setDate(System.currentTimeMillis());
                chatMessage.setMe(true);
                chatMessage.setOnClickable(false);
                chatMessage.setOnLongPressable(false);

                messageET.setText("");

                displayMessage(chatMessage);

                Log.e("Siri input: ", "Is it safe during pregnancy?");

                new ExecuteNetworkOperation().execute("Is it safe during pregnancy?");
            }
        });

        txtp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // Toast.makeText(context,"Clicked Uninstall",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
                new MainActivity().checkInternet(getActivity());
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(++k);//dummy
                chatMessage.setMessage("what is it's price?");
                chatMessage.setDate(System.currentTimeMillis());
                chatMessage.setMe(true);
                chatMessage.setOnClickable(false);
                chatMessage.setOnLongPressable(false);

                messageET.setText("");

                displayMessage(chatMessage);

                Log.e("Siri input: ", "what is it's price?");

                new ExecuteNetworkOperation().execute("what is it's price?");
            }
        });
        txts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Toast.makeText(context,"Clicked Uninstall",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
                new MainActivity().checkInternet(getActivity());
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(++k);//dummy
                chatMessage.setMessage("what are it's substitutes?");
                chatMessage.setDate(System.currentTimeMillis());
                chatMessage.setMe(true);
                chatMessage.setOnClickable(false);
                chatMessage.setOnLongPressable(false);

                messageET.setText("");

                displayMessage(chatMessage);

                Log.e("Siri input: ", "what are it's substitutes?");

                new ExecuteNetworkOperation().execute("what are it's substitutes?");
            }
        });
        txttc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Toast.makeText(context,"Clicked Uninstall",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
                new MainActivity().checkInternet(getActivity());
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(++k);//dummy
                chatMessage.setMessage("what type of medicine is this?");
                chatMessage.setDate(System.currentTimeMillis());
                chatMessage.setMe(true);
                chatMessage.setOnClickable(false);
                chatMessage.setOnLongPressable(false);

                messageET.setText("");

                displayMessage(chatMessage);

                Log.e("Siri input: ", "what type of medicine is this?");

                new ExecuteNetworkOperation().execute("what type of medicine is this?");
            }
        });
    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txtSpeechInput.setText(result.get(0));

                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setId(++k);//dummy
                    chatMessage.setMessage(result.get(0));
                    chatMessage.setDate(System.currentTimeMillis());
                    chatMessage.setMe(true);
                    chatMessage.setOnClickable(false);
                    chatMessage.setOnLongPressable(false);

                    messageET.setText("");

                    displayMessage(chatMessage);

                    Log.e("Siri input: ", result.get(0));

                    new ExecuteNetworkOperation().execute(result.get(0));



                }
                break;
            }

        }
    }

    public void displayResponseChat (ArrayList<ResponseChat> rcal) {

        for (ResponseChat rc : rcal) {

            ChatMessage respMessage = new ChatMessage();
            respMessage.setId(++k); //dummy
            respMessage.setMessage(rc.getResponse_text());
            respMessage.setDate(System.currentTimeMillis());
            respMessage.setMe(false);
            respMessage.setOnClickable(rc.isOn_clickable());
            respMessage.setOnLongPressable(rc.isOn_longpressable());
            displayMessage(respMessage);
        }
    }

    public void askFromAdapter(String msg)
    {
        new MainActivity().checkInternet(getActivity());
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(++k);//dummy
        chatMessage.setMessage(msg);
        chatMessage.setDate(System.currentTimeMillis());
        chatMessage.setMe(true);
        chatMessage.setOnClickable(false);
        chatMessage.setOnLongPressable(false);


        displayMessage(chatMessage);

        Log.e("Siri input: ", msg);

        new ExecuteNetworkOperation().execute(msg);
    }


}
