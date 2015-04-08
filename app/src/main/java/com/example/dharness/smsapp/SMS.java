package com.example.dharness.smsapp;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by dharness on 15-04-08.
 *
 0  : _id
 1  : thread_id
 2  : address
 3  : person
 4  : date
 5  : protocol
 6  : read
 7  : status
 8  : type
 9  : reply_path_present
 10 : subject
 11 : body
 12 : service_center
 13 : locked
 */
public class SMS {

    int _id;
    int thread_id;
    String address;
    String person;
    String date;
    String protocol;
    String read;
    String status;
    String type;
    String reply_path_present;
    String subject;
    String body;
    String service_center;
    String locked;
    private HashMap<String, String> data;

    public  SMS(String s){
        Log.d("SOMETAG", "MESSAGE");
    }

    public SMS(HashMap<String, String> data){
        this.data = data;
    }

    public SMS(int _id, int thread_id, String address, String person, String date, String protocol, String read, String status, String type, String reply_path_present, String subject, String body, String service_center, String locked){
        this._id = _id;
        this.thread_id = thread_id;
        this.address = address;
        this.person = person;
        this.date = date;
        this.protocol = protocol;
        this.read = read;
        this.status = status;
        this.type = type;
        this.reply_path_present = reply_path_present;
        this.subject = subject;
        this.body = body;
        this.service_center = service_center;
        this.locked = locked;
    }

    public String getDataByName(String name){
        return this.data.get(name);
    }


    public String toString(){
        return _id + " " + thread_id + " " + address + " " + person + " " + date + " " + protocol + " " + read + " " + status + " " + type + " " + reply_path_present + " " + subject + " " + body + " " + service_center + " " + locked;

    }
}
