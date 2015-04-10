package com.example.dharness.smsapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    ListView lv;
    Button sendButton;
    EditText msgText;
    SMSListAdapter adapter;
    ArrayList<SMS> smsList;
    ContentResolver contentResolver;
    SMSReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiver =  new SMSReceiver();
        registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        //grab a reference to our UI components
        msgText = (EditText)findViewById(R.id.editText);
        sendButton = (Button)findViewById(R.id.sendButton);
        lv = (ListView)findViewById(R.id.listView);

        contentResolver = getContentResolver();
        bindList();
        updateList("15198721420");

        adapter = new SMSListAdapter(this, smsList);
        lv.setAdapter(adapter);

        sendButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        sendBroadcast(new Intent("some.action"));
                        sendSMS("12264483072", msgText.getText().toString());
                        lv.post(new Runnable() {
                            public void run() {
                                lv.setSelection(lv.getCount() - 1);
                            }
                        });
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private  void sendSMS(String boop, String message){}

    private void sendSMSS(String phoneNumber, String message) {

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        }, new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    // This function should only ever be called once
    private void bindList(){
        smsList = new ArrayList<>();
        adapter = new SMSListAdapter(this, smsList);
        lv.setAdapter(adapter);
    }

    private void updateList(String phoneNumber){
        //reset the array, we are about to refill
        smsList = new ArrayList<>();

        //set up what we want, and who we want it from
        Uri uri = Uri.parse("content://sms/");
        String[] config = new String[]{"_id","thread_id","address","person","date", "protocol", "read","status","type","reply_path_present","subject","body", "service_center", "locked"};
        String sms = "address='"+ "+" + phoneNumber + "'";

        // Loop through all the messages
        Cursor cursor = contentResolver.query(uri, config, sms, null,   null);

        // populate the list with texts
        while (cursor.moveToNext())
        {
            // SMS messages have a HashMap of all their data
            HashMap<String, String> map = new HashMap<>();
            for(String s : config){ // add all the data for one SMS to its map
                map.put(s, cursor.getString(cursor.getColumnIndex(s)));
            }

            // save the SMS
            smsList.add(new SMS(map));
        }
        Collections.reverse(smsList);

        //scroll to the bottom of the list
        lv.post(new Runnable() {
            public void run() {
                lv.setSelection(lv.getCount() - 1);
            }
        });

    }

}