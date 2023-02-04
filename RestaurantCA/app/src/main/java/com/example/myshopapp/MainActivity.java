package com.example.myshopapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

        public class MainActivity extends Activity {
            Button btnItem,btnCus,btnSup,btnAccount,btnsetting,btnorders;
            SharedPreferences sp;
            Dialog dialog;
            String lang;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                this.requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.activity_main);
                btnItem=(Button)findViewById(R.id.btnitem);
                btnCus=(Button)findViewById(R.id.btnCostumer);
                btnSup=(Button)findViewById(R.id.btnSupplier);
                btnAccount=(Button)findViewById(R.id.btnAcountSection);
                btnsetting=(Button)findViewById(R.id.btnsetting);

                btnorders=(Button)findViewById(R.id.btnOrders);

                registerReceiver(new NetworkMonitor(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

btnorders.setOnClickListener ( new View.OnClickListener ( ) {
    @Override
    public void onClick(View v) {
Intent intent=new Intent ( getApplicationContext (),Orders_Managment.class);
startActivity ( intent );
    }
} );
                btnAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pass=sp.getString("userPhoneN","");
                        Boolean passset=true;
                        if(pass.equals(""))
                        {
                            passset=false;

                        }
                        if (passset==false)
                        {
                            Toast.makeText(getApplicationContext(),"please set your password",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            ShowPassDialoge2(v);
                        }

                    }
                });

                sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                lang=sp.getString("lang","");
                if(lang.equals(""))
                {
                    //showDialoge();
                }
//Toast.makeText(getApplicationContext(),""+lang,Toast.LENGTH_SHORT).show();
                if(lang.equals("Urdu")) {
                    SpannableString content = new SpannableString(getString(R.string.UrdubtnSetting));

                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    btnsetting.setText(content);
                    btnCus.setText(getString(R.string.UrdubtnCustomer));
                    btnItem.setText(getString(R.string.UrdubtnItem));
                    btnSup.setText(getString(R.string.UrdubtnSupplier));
                    btnAccount.setText(getString(R.string.UrdubtnAccount));
                    btnorders.setText ( "ارڈرزمنجمنٹ" );
                }
                else {
                    SpannableString content = new SpannableString(getString(R.string.englishbtnSetting));

                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    btnsetting.setText(content);
                    btnCus.setText(getString(R.string.englishbtnCustomer));
                    btnItem.setText(getString(R.string.englishbtnItem));
                    btnSup.setText(getString(R.string.englishbtnSupplier));
                    btnAccount.setText(getString(R.string.englishbtnAccount));
                    btnorders.setText ( "Orders Managment" );
                }
                btnsetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pass=sp.getString("userPhoneN","");
                        if(pass.equals(""))
                        {
                            Intent i=new Intent(getApplicationContext(),Setting.class);
                            startActivity(i);
                        }
                        else {
                            ShowPassDialoge(v);
                        }

                    }
                });
                btnItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(getApplicationContext(),Item_Record.class);
                        startActivity(i);
                    }
                });
                btnCus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(getApplicationContext(),Customer_Record.class);
                        startActivity(i);
                    }
                });
                btnSup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(getApplicationContext(),Supplier_Record.class);
                        startActivity(i);
                    }
                });
            }

            private void ShowPassDialoge2(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Enter Password");

// Set up the input
                final EditText input = new EditText(v.getContext());
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setText("");
                input.setHint("پاسورڈ درج کریں");
                //input.setHintTextColor(getColor(R.color.hntscolor));
                builder.setView(input);
// Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strEP = input.getText().toString();
                        String strSP=sp.getString("password","");
                        if(strEP.equals(strSP))
                        {
                            Intent i=new Intent(getApplicationContext(),Account_Section.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface abc, int which) {

                    }
                });
                builder.show();
            }

            @Override
            protected void onResume() {
                super.onResume();
                btnItem=(Button)findViewById(R.id.btnitem);
                btnCus=(Button)findViewById(R.id.btnCostumer);
                btnSup=(Button)findViewById(R.id.btnSupplier);
                sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                lang=sp.getString("lang","");
                if(lang.equals(""))
                {
                    showDialoge();
                }
//Toast.makeText(getApplicationContext(),""+lang,Toast.LENGTH_SHORT).show();
                Button btnsetting=(Button)findViewById(R.id.btnsetting);
                if(lang.equals("Urdu")) {
                    SpannableString content = new SpannableString(getString(R.string.UrdubtnSetting));

                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    btnsetting.setText(content);
                    btnCus.setText(getString(R.string.UrdubtnCustomer));
                    btnItem.setText(getString(R.string.UrdubtnItem));
                    btnSup.setText(getString(R.string.UrdubtnSupplier));
                    btnAccount.setText(getString(R.string.UrdubtnAccount));
                }
                else {
                    SpannableString content = new SpannableString(getString(R.string.englishbtnSetting));

                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    btnsetting.setText(content);
                    btnCus.setText(getString(R.string.englishbtnCustomer));
                    btnItem.setText(getString(R.string.englishbtnItem));
                    btnSup.setText(getString(R.string.englishbtnSupplier));
                    btnAccount.setText(getString(R.string.englishbtnAccount));
                }
            }

            private void ShowPassDialoge(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Enter Password");

// Set up the input
                final EditText input = new EditText(v.getContext());
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setText("");
                input.setHint("پاسورڈ درج کریں");
                //input.setHintTextColor(getColor(R.color.hntscolor));
                builder.setView(input);
// Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strEP = input.getText().toString();
                        String strSP=sp.getString("password","");
                        if(strEP.equals(strSP))
                        {
                            Intent i=new Intent(getApplicationContext(),Setting.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalide Password",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface abc, int which) {

                    }
                });
                builder.setNeutralButton("Forgot Password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface abc, int which) {
                        String strpn=sp.getString("userPhoneN","");
                        String strSP=sp.getString("password","");
                        SmsManager sm=SmsManager.getDefault();
                        String body="Your Password is "+strSP;
                        sm.sendTextMessage(strpn, null, body, null, null);
                        Toast.makeText(getApplicationContext(),"Password is sent to Admin Phone",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }

            public void onBackPressed() {
                // TODO Auto-generated method stub
                // super.onBackPressed();




                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.app_name));
                builder.setPositiveButton("Quit App",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    // Load ads into Interstitial Ads


                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                            }
                        })

                        .setNeutralButton("More Apps",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        //Try Google play
                                        intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=2AIMITSOLUTIONS&hl=en"));
                                        if (!MyStartActivity(intent)) {
                                            //Market (Google play) app seems not installed, let's try to open a webbrowser
                                            intent.setData(Uri.parse("https://play.google.com/store/search?q=ebmacs&hl=en"));
                                            if (!MyStartActivity(intent)) {
                                                //Well if this also fails, we have run out of options, inform the user.
                                                Toast.makeText(getBaseContext(), "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                                            }}


                                    }
                                })


                        .setNegativeButton("Rate Us",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //rate the app
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        //Try Google play
                                        intent.setData(Uri.parse("market://details?id=com.Twoaim.Group.Sms.Massenger&hl=en"));
                                        if (!MyStartActivity(intent)) {
                                            //Market (Google play) app seems not installed, let's try to open a webbrowser
                                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.Twoaim.Group.Sms.Massenger&hl=en"));
                                            if (!MyStartActivity(intent)) {
                                                //Well if this also fails, we have run out of options, inform the user.
                                                Toast.makeText(getBaseContext(), "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                                            }}


                                    }
                                });
                // Create the AlertDialog object and return it
                AlertDialog alertDialog = builder.create();
                alertDialog.show();



            }
            private boolean MyStartActivity(Intent aIntent) {
                try
                {
                    startActivity(aIntent);
                    return true;
                }
                catch (ActivityNotFoundException e)
                {
                    return false;
                }

            }
            private void showDialoge() {
                dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.radiobutton_dialog);
                dialog.setTitle("Pleas Select App Language");
                List<String> stringList = new ArrayList<>();
                List<String> myString2 = new ArrayList<>();  // here is list
                int count = 0;
                int z = 0;
                boolean found = false;
                stringList.add("Urdu");
                stringList.add("English");
                RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
                for (int i = 0; i < stringList.size(); i++) {
                    RadioButton rb = new RadioButton(MainActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
                    rb.setText(stringList.get(i));
                    rg.addView(rb);
                }

                dialog.show();

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int childCount = group.getChildCount();
                        for (int x = 0; x < childCount; x++) {
                            RadioButton btn = (RadioButton) group.getChildAt(x);
                            if (btn.getId() == checkedId) {
                                String selectedL = btn.getText().toString();
                                SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor spe=sp.edit();
                                spe.putString("lang",selectedL);
                                spe.commit();
                                if(selectedL.equals("Urdu"))
                                {
                                    SpannableString content = new SpannableString(getString(R.string.UrdubtnSetting));

                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                    btnsetting.setText(content);
                                    btnCus.setText(getString(R.string.UrdubtnCustomer));
                                    btnItem.setText(getString(R.string.UrdubtnItem));
                                    btnSup.setText(getString(R.string.UrdubtnSupplier));
                                    btnAccount.setText(getString(R.string.UrdubtnAccount));
                                }
                                else
                                {
                                    SpannableString content = new SpannableString(getString(R.string.englishbtnSetting));

                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                    btnsetting.setText(content);
                                    btnCus.setText(getString(R.string.englishbtnCustomer));
                                    btnItem.setText(getString(R.string.englishbtnItem));
                                    btnSup.setText(getString(R.string.englishbtnSupplier));
                                    btnAccount.setText(getString(R.string.englishbtnAccount));

                                }
                            }
                        }
                        dialog.dismiss();
                    }

                });
            }
        }
