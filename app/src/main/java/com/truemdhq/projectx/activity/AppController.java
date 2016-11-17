package com.truemdhq.projectx.activity;

/**
 * Created by yashvardhansrivastava on 25/12/15.
 */


import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.alirezaafkar.json.requester.Requester;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.tramsun.libs.prefcompat.Pref;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.multidex.MultiDexApplication;
import android.support.multidex.MultiDex;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.model.Client;
import com.truemdhq.projectx.model.Invoice;
import com.truemdhq.projectx.model.InvoiceChild;
import com.truemdhq.projectx.model.InvoiceParent;
import com.truemdhq.projectx.model.Item;

import io.paperdb.Paper;

public class AppController extends MultiDexApplication {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Hawk.init(this).build();

        Pref.init(this);
        Paper.init(this);
        //jsonRequester.init
        Map<String, String> header = new HashMap<>();
        header.put("charset", "utf-8");

        String baseUrl = "";

        if (Hawk.contains("invoiceDraft"))
        {}
        else{

            JSONObject invoiceJSONObject = new JSONObject();
            Hawk.put("invoiceDraft", invoiceJSONObject.toString());

        }

        if(Hawk.contains("invoiceList"))
        {}
        else
        {
            Item item1 = new Item("1",1.0,1000.0,1000.0,"Dongri to Dubai", "659272789", 1.14, 10.0, 5.0, true, false, false,  true);
            Item item2 = new Item("2",1.0,100.0,100.0,"Meluha", "234564676", 1.14, 10.0, 5.0, true, false, false, true);
            Item item3 = new Item("3",1.0,2000.0,2000.0,"Gerrard", "766667734", 1.14, 10.0, 5.0,  true, false, false, true);
            Item item4 = new Item("4",1.0,1000.0,1000.0,"The Client", "456243764",  1.14, 10.0, 5.0,  true, false, false, true);
            Item item5 = new Item("5",2.0, 3000.0, 6000.0 ,"Website Dev.", "157686996",  1.14, 10.0, 5.0,  true, false, false, false);

            Client client1 = new Client("","TrueMD", "209 Dhan Trident\nSc. 54, Vijaynagar\nIndore- 452010","209 Dhan Trident\nSc. 54," +
                    " Vijaynagar\nIndore- 452010", "yash6992@gmail.com", "9581649079", "2314576544");
            Client client2 = new Client("","AG Creations", "12-C AB Road\nAtal Dwar, Vijaynagar\nIndore- 452010","12-C AB Road\n" +
                    "Atal Dwar, Vijaynagar\nIndore- 452010", "deepak.soni@gmail.com", "7897286133", "8966622544");


            Invoice invoice1 = new Invoice();
            invoice1.setBalanceDue(7890.0);
            invoice1.setClient(client1);
            invoice1.setComment("The amount is for the database of medicines provided in mongodb dump format.");
            invoice1.setDiscount(100.0);
            invoice1.setInvoiceDate("12/11/2016");
            invoice1.setInvoiceDueDate("due in 21 days");
            invoice1.setItems(Arrays.asList(item1, item2, item5));
            invoice1.setInvoiceStatus("PAID");
            invoice1.setInvoiceNo("1");
            invoice1.setPaid(0.00);
            invoice1.setShipping(0.00);
            invoice1.setSubtotal(7100.00);
            invoice1.setTax1(0.00);
            invoice1.setTax2(0.00);
            invoice1.setTotal(7980.00);
            invoice1.setVat(980.00);

            Invoice invoice2 = new Invoice();
            invoice2.setBalanceDue(1140.00);
            invoice2.setClient(client2);
            invoice2.setComment("The amount is for the flyer-printing.");
            invoice2.setDiscount(100.00);
            invoice2.setInvoiceDate("12/11/2016");
            invoice2.setInvoiceDueDate("due in 30 days");
            invoice2.setItems(Arrays.asList(item1, item2));
            invoice2.setInvoiceStatus("SENT");
            invoice2.setInvoiceNo("7");
            invoice2.setPaid(0.00);
            invoice2.setShipping(0.00);
            invoice2.setSubtotal(1100.00);
            invoice2.setTax1(0.00);
            invoice2.setTax2(0.00);
            invoice2.setTotal(1140.00);
            invoice2.setVat(140.00);

            Invoice invoice3 = new Invoice();
            invoice3.setBalanceDue(7980.00);
            invoice3.setClient(client1);
            invoice3.setComment("The amount is for the database of medicines provided in mongodb dump format.");
            invoice3.setDiscount(100.00);
            invoice3.setInvoiceDate("12/11/2016");
            invoice3.setInvoiceDueDate("due in 7 days");
            invoice3.setItems(Arrays.asList(item1, item2, item5));
            invoice3.setInvoiceStatus("PAID");
            invoice3.setInvoiceNo("8");
            invoice3.setPaid(0.00);
            invoice3.setShipping(0.0);
            invoice3.setSubtotal(7100.00);
            invoice3.setTax1(0.00);
            invoice3.setTax2(0.00);
            invoice3.setTotal(7980.00);
            invoice3.setVat(980.00);

            ObjectMapper mapper = new ObjectMapper();

            try {
                String jsonItem1 = mapper.writeValueAsString(invoice1);
                String jsonItem2 = mapper.writeValueAsString(invoice2);
                String jsonItem3 = mapper.writeValueAsString(invoice3);

                JSONObject invoiceJSONObject1 = new JSONObject(jsonItem1);
                JSONObject invoiceJSONObject2 = new JSONObject(jsonItem2);
                JSONObject invoiceJSONObject3 = new JSONObject(jsonItem3);

                JSONArray invoiceJSONArray = new JSONArray();
                invoiceJSONArray.put(invoiceJSONObject1);
                invoiceJSONArray.put(invoiceJSONObject2);
                invoiceJSONArray.put(invoiceJSONObject3);

                Log.e("HawkApp:",""+jsonItem1);
                Log.e("HawkApp:",""+invoiceJSONArray.toString());
                Hawk.put("invoiceList", invoiceJSONArray.toString());


            } catch (Exception e) {
                Log.e("AppControllerJackson:",""+e.getMessage());
                e.printStackTrace();
            }


        }

        if(Hawk.contains("itemList"))
        {

        }
        else
        {
            Item item1 = new Item("1",1.0,1000.0,1000.0,"Dongri to Dubai", "659272789", 1.14, 10.0, 5.0, true, false, false,  true);
            Item item2 = new Item("2",1.0,100.0,100.0,"Meluha", "234564676", 1.14, 10.0, 5.0, true, false, false, true);
            Item item3 = new Item("3",1.0,2000.0,2000.0,"Gerrard", "766667734", 1.14, 10.0, 5.0,  true, false, false, true);
            Item item4 = new Item("4",1.0,1000.0,1000.0,"The Client", "456243764",  1.14, 10.0, 5.0,  true, false, false, true);
            Item item5 = new Item("5",2.0, 3000.0, 6000.0 ,"Website Dev.", "157686996",  1.14, 10.0, 5.0,  true, false, false, false);

            ObjectMapper mapper = new ObjectMapper();

            try {
                String jsonItem1 = mapper.writeValueAsString(item1);
                String jsonItem2 = mapper.writeValueAsString(item2);
                String jsonItem3 = mapper.writeValueAsString(item3);
                String jsonItem4 = mapper.writeValueAsString(item4);
                String jsonItem5 = mapper.writeValueAsString(item5);

                JSONObject itemJSONObject1 = new JSONObject(jsonItem1);
                JSONObject itemJSONObject2 = new JSONObject(jsonItem2);
                JSONObject itemJSONObject3 = new JSONObject(jsonItem3);
                JSONObject itemJSONObject4 = new JSONObject(jsonItem4);
                JSONObject itemJSONObject5 = new JSONObject(jsonItem5);

                JSONArray itemJSONArray = new JSONArray();
                itemJSONArray.put(jsonItem1);
                itemJSONArray.put(jsonItem2);
                itemJSONArray.put(jsonItem3);
                itemJSONArray.put(jsonItem4);
                itemJSONArray.put(jsonItem5);

                Hawk.put("itemList", itemJSONArray.toString());


            } catch (Exception e) {
                Log.e("AppControllerJackson:",""+e.getMessage());
                e.printStackTrace();
            }

        }

        if(Hawk.contains("clientList"))
        {}
        else
        {

            Client client1 = new Client("","TrueMD", "209 Dhan Trident\nSc. 54, Vijaynagar\nIndore- 452010","209 Dhan Trident\nSc. 54," +
                    " Vijaynagar\nIndore- 452010", "yash6992@gmail.com", "9581649079", "2314576544");
            Client client2 = new Client("","AG Creations", "12-C AB Road\nAtal Dwar, Vijaynagar\nIndore- 452010","12-C AB Road\n" +
                    "Atal Dwar, Vijaynagar\nIndore- 452010", "deepak.soni@gmail.com", "7897286133", "8966622544");
            ObjectMapper mapper = new ObjectMapper();

            try {
                String jsonClient1 = mapper.writeValueAsString(client1);
                String jsonClient2 = mapper.writeValueAsString(client2);

                JSONObject clientJSONObject1 = new JSONObject(jsonClient1);
                JSONObject clientJSONObject2 = new JSONObject(jsonClient2);

                JSONArray clientJSONArray = new JSONArray();
                clientJSONArray.put(clientJSONObject1);
                clientJSONArray.put(clientJSONObject2);

                Hawk.put("clientList", clientJSONArray.toString());


            } catch (Exception e) {
                Log.e("AppControllerJackson:",""+e.getMessage());
                e.printStackTrace();
            }

        }



        List<String> allKeys1 = Paper.book("reminder_id").getAllKeys();
        if(allKeys1.contains("id"))
        {
            // Paper.book("reminder_id").write("id", "1");
        }
        else
        {
            Paper.book("reminder_id").write("id", "1");
        }
        Log.e("**1AppController:", ""+Paper.book("reminder_id").read("id"));


        List<String> allKeys2 = Paper.book("introduction").getAllKeys();
        if(allKeys2.contains("intro"))
        {
           // Paper.book("introduction").write("intro","0");
        }
        else
        {
            Paper.book("introduction").write("intro","0");
            Paper.book("introduction").write("preshelp","0");
            Paper.book("introduction").write("askpin","0");
            Paper.book("introduction").write("rateus","0");
            Paper.book("introduction").write("ratecounter","1");
        }
        Log.e("**2AppControllerIntro:", ""+Paper.book("introduction").read("intro"));

        List<String> allKeys3 = Paper.book("user").getAllKeys();
        if(allKeys3.contains("name"))
        {
            // Paper.book("introduction").write("intro","0");
        }
        else
        {
            Paper.book("user").write("name","");
        }
        Log.e("**3AppController:", ""+Paper.book("user").read("name"));

        List<String> allKeysp = Paper.book("user").getAllKeys();
        if(allKeysp.contains("pincode"))
        {
            // Paper.book("introduction").write("intro","0");
        }
        else
        {
            Paper.book("user").write("pincode","");
            Paper.book("user").write("pincode_city","");

        }
        Log.e("**4AppController:", ""+Paper.book("user").read("pincode")+Paper.book("user").read("pincode_city"));


        List<String> allKeys4 = Paper.book("nav").getAllKeys();
        if(allKeys4.contains("selected"))
        {
            // Paper.book("introduction").write("intro","0");
        }
        else
        {
            Paper.book("nav").write("selected","0");
        }
        Log.e("**5AppController:", ""+Paper.book("nav").read("selected"));

        List<String> allKeys5 = Paper.book("reminder").getAllKeys();
        if(allKeys5.contains("pouchesafternow"))
        {
            // Paper.book("introduction").write("intro","0");
        }
        else
        {
            Paper.book("reminder").write("pouchesafternow","[]");
        }
        Log.e("**6AppController:", ""+Paper.book("reminder").read("pouchesafternow"));



//
        new Requester.Config(getApplicationContext()).baseUrl(baseUrl).header(header);




    }


    public void clearApplicationData() {

        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {

            String[] fileNames = applicationDirectory.list();

            for (String fileName : fileNames) {

                if (!fileName.equals("lib")) {

                    deleteFile(new File(applicationDirectory, fileName));

                }

            }

        }

    }

    public static boolean deleteFile(File file) {

        boolean deletedAll = true;

        if (file != null) {

            if (file.isDirectory()) {

                String[] children = file.list();

                for (int i = 0; i < children.length; i++) {

                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;

                }

            } else {

                deletedAll = file.delete();

            }

        }

        return deletedAll;

    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(AppController.this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private List<ParentListItem> generateInvoices() {


        Item item1 = new Item("1",1.0,1000.0,1000.0,"Dongri to Dubai", "659272789", 1.14, 10.0, 5.0, true, false, false,  true);
        Item item2 = new Item("2",1.0,100.0,100.0,"Meluha", "234564676", 1.14, 10.0, 5.0, true, false, false, true);
        Item item3 = new Item("3",1.0,2000.0,2000.0,"Gerrard", "766667734", 1.14, 10.0, 5.0,  true, false, false, true);
        Item item4 = new Item("4",1.0,1000.0,1000.0,"The Client", "456243764",  1.14, 10.0, 5.0,  true, false, false, true);
        Item item5 = new Item("5",2.0, 3000.0, 6000.0 ,"Website Dev.", "157686996",  1.14, 10.0, 5.0,  true, false, false, false);

        Client client1 = new Client("","TrueMD", "209 Dhan Trident\nSc. 54, Vijaynagar\nIndore- 452010","209 Dhan Trident\nSc. 54," +
                " Vijaynagar\nIndore- 452010", "yash6992@gmail.com", "9581649079", "2314576544");
        Client client2 = new Client("","AG Creations", "12-C AB Road\nAtal Dwar, Vijaynagar\nIndore- 452010","12-C AB Road\n" +
                "Atal Dwar, Vijaynagar\nIndore- 452010", "deepak.soni@gmail.com", "7897286133", "8966622544");


        Invoice invoice1 = new Invoice();
        invoice1.setBalanceDue(7890.0);
        invoice1.setClient(client1);
        invoice1.setComment("The amount is for the database of medicines provided in mongodb dump format.");
        invoice1.setDiscount(100.0);
        invoice1.setInvoiceDate("12/11/2016");
        invoice1.setInvoiceDueDate("due in 21 days");
        invoice1.setItems(Arrays.asList(item1, item2, item5));
        invoice1.setInvoiceStatus("PAID");
        invoice1.setInvoiceNo("1");
        invoice1.setPaid(0.00);
        invoice1.setShipping(0.00);
        invoice1.setSubtotal(7100.00);
        invoice1.setTax1(0.00);
        invoice1.setTax2(0.00);
        invoice1.setTotal(7980.00);
        invoice1.setVat(980.00);

        Invoice invoice2 = new Invoice();
        invoice2.setBalanceDue(1140.00);
        invoice2.setClient(client2);
        invoice2.setComment("The amount is for the flyer-printing.");
        invoice2.setDiscount(100.00);
        invoice2.setInvoiceDate("12/11/2016");
        invoice2.setInvoiceDueDate("due in 30 days");
        invoice2.setItems(Arrays.asList(item1, item2));
        invoice2.setInvoiceStatus("SENT");
        invoice2.setInvoiceNo("7");
        invoice2.setPaid(0.00);
        invoice2.setShipping(0.00);
        invoice2.setSubtotal(1100.00);
        invoice2.setTax1(0.00);
        invoice2.setTax2(0.00);
        invoice2.setTotal(1140.00);
        invoice2.setVat(140.00);

        Invoice invoice3 = new Invoice();
        invoice3.setBalanceDue(7980.00);
        invoice3.setClient(client1);
        invoice3.setComment("The amount is for the database of medicines provided in mongodb dump format.");
        invoice3.setDiscount(100.00);
        invoice3.setInvoiceDate("12/11/2016");
        invoice3.setInvoiceDueDate("due in 7 days");
        invoice3.setItems(Arrays.asList(item1, item2, item5));
        invoice3.setInvoiceStatus("PAID");
        invoice3.setInvoiceNo("8");
        invoice3.setPaid(0.00);
        invoice3.setShipping(0.0);
        invoice3.setSubtotal(7100.00);
        invoice3.setTax1(0.00);
        invoice3.setTax2(0.00);
        invoice3.setTotal(7980.00);
        invoice3.setVat(980.00);

        InvoiceChild child1 = new InvoiceChild(invoice1);
        InvoiceChild child2 = new InvoiceChild(invoice2);
        InvoiceChild child3 = new InvoiceChild(invoice3);

        List<Object> childList1 = new ArrayList<>();
        childList1.add(child1);
        List<Object> childList2 = new ArrayList<>();
        childList2.add(child2);
        List<Object> childList3 = new ArrayList<>();
        childList3.add(child3);


        InvoiceParent parent1 = new InvoiceParent(invoice1);
        parent1.setChildItemList(childList1);
        InvoiceParent parent2 = new InvoiceParent(invoice2);
        parent2.setChildItemList(childList2);
        InvoiceParent parent3 = new InvoiceParent(invoice3);
        parent3.setChildItemList(childList3);




        List<ParentListItem> parentObjects = new ArrayList<>();

        parentObjects.add(parent1);
        parentObjects.add(parent2);
        parentObjects.add(parent3);
        parentObjects.add(parent2);
        parentObjects.add(parent1);
        parentObjects.add(parent3);


        return parentObjects;
    }


}