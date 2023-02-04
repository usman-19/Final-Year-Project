package com.example.myshopapp;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Splash extends Activity{
    int validDays=0;
    int DateofUpdate=0;
    int UseDays=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        registerReceiver(new NetworkMonitor(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        DateofUpdate = Integer.parseInt(sdf.format(new Date()));

        SharedPreferences spSGM=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //Update First Time
        if(spSGM.getInt("UpdateDate",0)==0) {
            SharedPreferences.Editor spEdit=spSGM.edit();
            spEdit.putInt("ValidDays", 10);
            spEdit.putInt("UpdateDate", DateofUpdate);
            spEdit.putInt("UseDays", 1);
            spEdit.commit();
            //Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
        }
        else {
            validDays=spSGM.getInt("ValidDays",0);
            UseDays=spSGM.getInt("UseDays",0);
            //Update On Other Days
            if (DateofUpdate == spSGM.getInt("UpdateDate", 0)) {
                //Toast.makeText(getApplicationContext(), "Today", Toast.LENGTH_SHORT).show();

            } else {
                SharedPreferences.Editor spEdit=spSGM.edit();
                //Toast.makeText(getApplicationContext(), "date and use day will be updated"+UseDays+validDays, Toast.LENGTH_SHORT).show();
                spEdit.putInt("UpdateDate", DateofUpdate);
                spEdit.putInt("UseDays",  UseDays+1);
                spEdit.commit();

            }
        }
        validDays=spSGM.getInt("ValidDays",0);
        UseDays=spSGM.getInt("UseDays",0);

        Thread timer = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(2000);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {

                    if(UseDays>validDays)
                    {

                        Intent i=new Intent(getApplicationContext(),ExpireActivity.class);
                        startActivity(i);
                    }
                    else {
                        Intent i=new Intent(getApplicationContext(), LogIn.class);
                        startActivity(i);
                        //Toast.makeText(getApplicationContext(), ""+UseDays, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        timer.start();




    }

    @Override
    protected void onResume() {
        super.onResume();

        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        DateofUpdate = Integer.parseInt(sdf.format(new Date()));

        SharedPreferences spSGM=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //Update First Time
        if(spSGM.getInt("UpdateDate",0)==0) {
            SharedPreferences.Editor spEdit=spSGM.edit();
            spEdit.putInt("ValidDays", 10);
            spEdit.putInt("UpdateDate", DateofUpdate);
            spEdit.putInt("UseDays", 1);
            spEdit.commit();
            //Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
        }
        else {
            validDays=spSGM.getInt("ValidDays",0);
            UseDays=spSGM.getInt("UseDays",0);
            //Update On Other Days
            if (DateofUpdate == spSGM.getInt("UpdateDate", 0)) {
                //Toast.makeText(getApplicationContext(), "Today", Toast.LENGTH_SHORT).show();

            } else {
                SharedPreferences.Editor spEdit=spSGM.edit();
                //Toast.makeText(getApplicationContext(), "date and use day will be updated"+UseDays+validDays, Toast.LENGTH_SHORT).show();
                spEdit.putInt("UpdateDate", DateofUpdate);
                spEdit.putInt("UseDays",  UseDays+1);
                spEdit.commit();

            }
        }
        validDays=spSGM.getInt("ValidDays",0);
        UseDays=spSGM.getInt("UseDays",0);

        Thread timer = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(2000);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {

                    if(UseDays>validDays)
                    {

                        Intent i=new Intent(getApplicationContext(),ExpireActivity.class);
                        startActivity(i);
                    }
                    else {
                        Intent i=new Intent(getApplicationContext(), LogIn.class);
                        startActivity(i);
                        //Toast.makeText(getApplicationContext(), ""+UseDays, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        timer.start();
    }
}
