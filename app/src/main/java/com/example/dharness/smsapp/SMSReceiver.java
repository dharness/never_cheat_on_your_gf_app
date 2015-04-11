package com.example.dharness.smsapp;

/**
 * Created by Casey on 08/04/2015.
 */

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SMSReceiver extends BroadcastReceiver{

    UIManager uiManager;

    public SMSReceiver(UIManager uiManager){
        this.uiManager = uiManager;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.w("TOMORROW", "In on recieve in the good version");
        uiManager.updateList("15198721420");
//        updateList("15198721420");

//        MainActivity.updateList("15198721420");
//        Bundle bundle = intent.getExtras();
//        SmsMessage[] msgs = null;
//        String str = "";
//        if (bundle != null)
//        {
//            // Retrieve SMS message
//            Object[] pdus = (Object[]) bundle.get("pdus");
//            msgs = new SmsMessage[pdus.length];
//            for (int i = 0; i < msgs.length; i++) {
//                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
//                str += "SMS from " + msgs[i].getOriginatingAddress();
//                str += " :";
//                str += msgs[i].getMessageBody().toString();
//                str += "\n";
//            }
//            // Display message
//            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
//
//            Intent broadcastIntent = new Intent();
//            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
//            broadcastIntent.putExtra("sms", str);
//            context.sendBroadcast(broadcastIntent);
//        }
    }
}
