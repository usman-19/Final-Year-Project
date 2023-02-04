package com.example.myshopapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;

import java.util.Arrays;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class SmsListener extends BroadcastReceiver{
    String[] SenderPin = new String[3];
    String[] SenderName = new String[3];
    String[] SenderName2 = new String[3];
    String pin,days,days2,vdays;
    String fSenderPin;



    String msgBody;



    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from = null;
            String fSenderName = null;

            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msgBody = msgs[i].getMessageBody();
                        // Toast.makeText(context, "sender no detected  " , Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
//	                            Log.d("Exception caught",e.getMessage());
                }

                try {
                    // Separat pin and name in sms
                    char[] first = new char[25];
                    first = msgBody.toCharArray();


                    String[] smsbodyarray = new String[first.length];

                    for (int i = 0; i < first.length; i++) {

                        smsbodyarray[i] = Character.toString(first[i]);
                    }


                    //seperate pine from sms body
                    for (int x = 0; x <= 2; x++) {
                        SenderPin[x] = smsbodyarray[x];
                    }
                    pin = Arrays.toString(SenderPin);
                    pin = pin.replace(",", "");
                    pin = pin.replace(" ", "");
                    pin = pin.replace("[", "");
                    pin = pin.replace("]", "");
                    //Toast.makeText(context, "pin "+pin, Toast.LENGTH_LONG).show();

                    //seperate name from sms body

                    for (int z = 3; z <= 5; z++) {

                        SenderName[z - 3] = smsbodyarray[z];


                    }
                    days = Arrays.toString(SenderName);
                    days = days.replace(",", "");
                    days = days.replace(" ", "");
                    days = days.replace("[", "");
                    days = days.replace("]", "");
                    for (int z = 6; z <= 8; z++) {

                        SenderName2[z - 6] = smsbodyarray[z];


                    }
                    days2 = Arrays.toString(SenderName2);
                    days2 = days2.replace(",", "");
                    days2 = days2.replace(" ", "");
                    days2 = days2.replace("[", "");
                    days2 = days2.replace("]", "");
                    //Toast.makeText(context, "Days: "+days, Toast.LENGTH_LONG).show();

                    vdays = "" + (Integer.parseInt(days2) - Integer.parseInt(days));
                    if (pin.equals("key") || pin.equals("Key") || pin.equals("KEY") && (msg_from.equals("+923028817729") || msg_from.equals("+923123332138"))) {

                        //pass sender number through intent
                        Intent myIntent = new Intent(context, SaveKey.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //Optional parameters
                        myIntent.putExtra("days", vdays);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        //Toast.makeText(context, "SMS Sender is Saved To Contact List", Toast.LENGTH_LONG).show();
                        context.startActivity(myIntent);

                    }


                }catch (Exception e)
                {}

            }

        }


        // Bitmap bitmap=textAsBitmap("hello",12,455454);


    }

//    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setTextSize(textSize);
//        paint.setColor(textColor);
//        paint.setTextAlign(Paint.Align.LEFT);
//        float baseline = -paint.ascent(); // ascent() is negative
//        int width = (int) (paint.measureText(text) + 0.5f); // round
//        int height = (int) (baseline + paint.descent() + 0.5f);
//        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(image);
//        canvas.drawText(text, 0, baseline, paint);
//        return image;
//    }



}


