package com.example.dharness.smsapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dharness on 15-04-08.
 */
public class SMSListAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater layoutInflater = null;
    private ArrayList<SMS> messages;

    public SMSListAdapter(Activity activity, ArrayList messages) {
        this.activity = activity;
        this.messages = messages;
        //get a layout inflater so out little layout is configured for same device as main activity
        layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    // I'm not really sure what this does right now, so this is just filler
    public Object getItem(int position) {
        return position;
    }

    @Override
    // I'm not really sure what this does right now, so this is just filler
    public long getItemId(int position) {
        return position;
    }

    @Override
    //This is the place where you build up the actual view tou wish to inflate
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = layoutInflater.inflate(R.layout.received_sms_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title);
        title.setText(messages.get(position).getDataByName("body"));
        return vi;
    }
}
