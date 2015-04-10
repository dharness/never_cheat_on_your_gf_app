package com.example.dharness.smsapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;


public class MainActivity extends ActionBarActivity {

    private static final String DEBUG_TAG = "SMSapp";

    private static final int CONTACT_PICKER_RESULT = 1001;

    String phoneNumber = "";

    ListView lv;
    SMSListAdapter adapter;
    ArrayList<SMS> smsList;
    MainActivity self;
    IntentFilter intentFilter;
    Button mButton;

//    public void doLaunchContactPicker(View view) {
//        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
//                Contacts.CONTENT_URI);
//        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
//    }
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            TextView SMSes = (TextView) findViewById(R.id.textView1);
//            SMSes.setText(intent.getExtras().getString("sms"));
            Log.w("TOMORROW", "BOOP");
            updateList("15198721420");
        }
    };

//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case CONTACT_PICKER_RESULT:
//                    Cursor cursor = null;
//                    String phoneNum = "";
//                    try {
//                        // Handle contact results
//                        Uri result = data.getData();
//                        Log.v(DEBUG_TAG, "Got a contact result: " + result.toString());
//                        String id = result.getLastPathSegment();
//
//                        // Query for phone
//                        cursor = getContentResolver().query(
//                                Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=?", new String[]{id}, null
//                        );
//                        cursor.moveToFirst();
//                        String columns[] = cursor.getColumnNames();
//                        for (String column : columns) {
//                            int index = cursor.getColumnIndex(column);
//                            Log.v(DEBUG_TAG, "Column: " + column + " == [" + cursor.getString(index) + "]");
//                        }
//
//                        int phoneIdx = cursor.getColumnIndex(Phone.DATA);
//
//                        // Only selects the first phone number -- maybe give option to select their own
//                        if (cursor.moveToFirst()) {
//                            phoneNum = cursor.getString(phoneIdx);
//                            Log.v(DEBUG_TAG, "Got phone: " + phoneNum);
//                        }
//                        else {
//                            Log.w(DEBUG_TAG, "No results");
//                        }
//                    }
//                    catch (Exception e) {
//                        Log.e(DEBUG_TAG, "Failed to get phone data", e);
//                    }
//                    finally {
//                        if (cursor != null) {
//                            cursor.close();
//                        }
//                        EditText recipientEntry   = (EditText)findViewById(R.id.recipient);
//                        recipientEntry.setText(phoneNum);
//                        phoneNumber = phoneNum;
////                        updateList(phoneNumber);
//                        updateList("12264483072");
//                        if (phoneNum.length() == 0) {
//                            Toast.makeText(this, "No phone number found for contact", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    break;
//            }
//        }
//        else {
//            Log.w(DEBUG_TAG, "Warning: activity result not ok");
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self = this;

        //SMS receiving stuff
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
        registerReceiver(intentReceiver, intentFilter);

        //grab a reference to our text fields
        final EditText mEdit   = (EditText)findViewById(R.id.editText);
//        final EditText recipient   = (EditText)findViewById(R.id.recipient);
        mButton = (Button)findViewById(R.id.sendButton);

        //pull out the phone number and populate the view
//        phoneNumber = recipient.getText().toString();
        updateList("15198721420");

        adapter = new SMSListAdapter(this, smsList);
        lv.setAdapter(adapter);

        mButton.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    sendSMS("12264483072", mEdit.getText().toString());
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
//        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
//        unregisterReceiver(intentReceiver);
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

    private void updateList(String phoneNumber){
        Log.w("SHIRT", "KIK");
        //reset the array, we are about to refill
        smsList = new ArrayList<>();

        //set up what we want, and who we want it from
        Uri uri = Uri.parse("content://sms/");
        String[] config = new String[]{"_id","thread_id","address","person","date", "protocol", "read","status","type","reply_path_present","subject","body", "service_center", "locked"};
        String sms = "address='"+ "+" + phoneNumber + "'";

        // Loop throu all the messages
        ContentResolver contentResolver = getContentResolver();
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
        //Do all of your listView stuff
        lv = (ListView) findViewById(R.id.listView);


        lv.post(new Runnable() {
            public void run() {
                lv.setSelection(lv.getCount() - 1);
            }
        });

    }
}
