package com.example.myshopapp;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.Window;

public class SaveKey extends Activity {

    SQLiteDatabase SQLITEDATABASE;
    String Name, PhoneNumber, Subject ;
    Boolean CheckEditTextEmpty ;
    String SQLiteQuery ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_save_key);

//        DBCreate();
//
//    	SubmitData2SQLiteDB();


        Intent startingIntent =getIntent();
        String days = startingIntent.getStringExtra("days");
        String saveDetail="2aim"+"+923028817729"+"\nValid days "+days;
        String numbersend="+923028817729";


        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor spedit=sp.edit();
        spedit.putInt("ValidDays",Integer.parseInt(days));
        spedit.putInt("UseDays",1);
        spedit.commit();


        SmsManager smsM=SmsManager.getDefault();
        smsM.sendTextMessage("+923028817729",null,saveDetail,null,null);








    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
        finish();
        Intent i=new Intent(getApplicationContext(), Splash.class);
        startActivity(i);

    }
}
