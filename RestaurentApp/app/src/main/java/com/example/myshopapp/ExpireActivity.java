package com.example.myshopapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;

public class ExpireActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expire);

        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String appinfo="Number of UseDays"+sp.getInt("UseDays",0)+"\n"
                +"Number of Valide days was"+sp.getInt("ValidDays",0);

        SmsManager smsM=SmsManager.getDefault();
        smsM.sendTextMessage("+923028817729",null,appinfo,null,null);

    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }
}