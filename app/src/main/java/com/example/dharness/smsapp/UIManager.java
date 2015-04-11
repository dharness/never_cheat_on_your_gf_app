package com.example.dharness.smsapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by dharness on 15-04-10.
 */
public class UIManager {

    ListView lv;
    Button sendButton;
    EditText msgText;
    SMSListAdapter adapter;
    ArrayList<SMS> smsList;
    ContentResolver contentResolver;
    Activity activity;

    public UIManager(Activity activity){
        this.activity = activity;
        //grab a reference to our UI components
        msgText = (EditText)activity.findViewById(R.id.editText);
        sendButton = (Button)activity.findViewById(R.id.sendButton);
        lv = (ListView)activity.findViewById(R.id.listView);

        contentResolver = activity.getContentResolver();
        smsList = new ArrayList<>();
        adapter = new SMSListAdapter(activity, smsList);
        lv.setAdapter(adapter);
    }

    public void updateList(String phoneNumber){
        //reset the array, we are about to refill
        smsList.clear();

        //set up what we want, and who we want it from
        Uri uri = Uri.parse("content://sms/");
        String[] config = new String[]{"_id","thread_id","address","person","date", "protocol", "read","status","type","reply_path_present","subject","body", "service_center", "locked"};
        String sms = "address='"+ "+" + phoneNumber + "'";

        // Loop through all the messages
        Cursor cursor = contentResolver.query(uri, config, sms, null,   null);

        // populate the list with texts
        while (cursor.moveToNext())
        {
            // SMS messages have a HashMap of all their data using config as the key
            HashMap<String, String> map = new HashMap<>();
            for(String s : config){ // add all the data for one SMS to its map
                map.put(s, cursor.getString(cursor.getColumnIndex(s)));

            }

            // save the SMS
            smsList.add(new SMS(map));
        }
        Collections.reverse(smsList);

        for(SMS testoc : smsList){
            Log.w("body", testoc.getDataByName("body"));
        }

        adapter.notifyDataSetChanged();
        Log.w("Adapter", adapter.toString());

        //scroll to the bottom of the list
        lv.post(new Runnable() {
            public void run() {
                lv.setSelection(lv.getCount() - 1);
            }
        });

    }

}
