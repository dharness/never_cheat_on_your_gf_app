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


public class MainActivity extends ActionBarActivity {

    ListView lv;
    SMSListAdapter adapter;
    ArrayList<String> textList;
    ArrayList<SMS> smsList;
    MainActivity self;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self = this;

        Button mButton = (Button)findViewById(R.id.sendButton);
        final EditText mEdit   = (EditText)findViewById(R.id.editText);

        // begin to fill the list with messages from Casey
        textList = new ArrayList<>();
        smsList = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/");

        ContentResolver contentResolver = getContentResolver();

        String phoneNumber = "+12264483072";
        String sms = "address='"+ phoneNumber + "'";
        String[] config = new String[]{"_id","thread_id","address","person","date", "protocol", "read","status","type","reply_path_present","subject","body", "service_center", "locked"};
        Cursor cursor = contentResolver.query(uri, config, sms, null,   null);
        // populate the list with texts
        while (cursor.moveToNext())
        {
            String strbody = cursor.getString(cursor.getColumnIndex("address")) + " ";
            strbody += cursor.getString(cursor.getColumnIndex("type"));
            strbody += cursor.getString(cursor.getColumnIndex("body"));
            textList.add(strbody);

            // SMS messages have a hashmap of all their data
            HashMap map = new HashMap();
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

        adapter = new SMSListAdapter(this, smsList);
        lv.setAdapter(adapter);

        mButton.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    textList.add( mEdit.getText().toString());
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


    // UTILITY FUNCTIONS
}
