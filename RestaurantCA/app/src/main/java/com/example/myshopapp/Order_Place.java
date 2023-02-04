package com.example.myshopapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Order_Place extends Activity {
    String selectedname = "";
    SQLiteDatabase SQLITEDATABASE;
    TextView tvtotalqarza,tvtotalqarzaText;
    TextView tvorder, tvtotal;
    String userid;
    Button btnsellnewitem, btnsubmit;
    String a;
    private DatabaseHelper db;
    String tempitemid = "";
    EditText etUsername;
    String myString;
    Dialog dialog;
    Cursor itemcorsore,cursortransectio;
    Boolean add = false;
    TextView edtpaid;
    int NumberOfProducts = 1, Total = 0, itemOrderno = 1;
    String GetSQliteQuery, GetSQLiteQueryItems;
    int ItemsRemaining;
    ArrayList<String> itemid = new ArrayList<String>();
    ArrayList<String> NumberOItem = new ArrayList<String>();
    TextView tvbaqaya;
    int counter = 1;
    TextView tvpaidtext;
    String cusOrderdetail = "";
    int totalsell = 0, totalbuy = 0;
    String selectednameId="";
    String oldamonut="";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order__place);
        selectedname = i.getStringExtra("name");
        tvtotalqarza = (TextView) findViewById(R.id.totalqarza);
        tvtotalqarzaText = (TextView) findViewById(R.id.totalqarzatext);
        edtpaid = (TextView) findViewById(R.id.edtpaid);
        btnsellnewitem = (Button) findViewById(R.id.btnsellitem);
        tvbaqaya = (TextView) findViewById(R.id.tvBaqaya);
        tvtotal = (TextView) findViewById(R.id.tvtotalbill);
        tvorder = (TextView) findViewById(R.id.tvorder);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        tvpaidtext = (TextView) findViewById(R.id.tvPaidText);
        GetSQliteQuery = "SELECT * FROM SupplierTable";
        GetSQliteQuery = "SELECT * FROM itemsTable";
        db = new DatabaseHelper(this);
        if (selectedname != "") {
            showtotalPuranaQarza(selectedname);
        }
        if (selectedname.equals("کیش : سپلائیر")) {
            tvtotalqarzaText.setText("");
            tvtotalqarza.setText("" + selectedname);
            View v = findViewById(R.id.lcheck);
            v.setVisibility(View.INVISIBLE);
        }
        Button btnRecDetail = (Button) findViewById(R.id.btnrecDetail);
        btnRecDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Order_Place.this).create();
                alertDialog.setTitle("Last Order");
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                alertDialog.setMessage(sp.getString("lastOrder", "empty"));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add == true) {
// //                   String GetSQliteQuery = "SELECT * FROM SupplierTable";
//   //                 SQLITEDATABASE = openOrCreateDatabase("MyShopDB", Context.MODE_PRIVATE, null);
//     //               Cursor cursor = SQLITEDATABASE.rawQuery(GetSQliteQuery, null);

                    Cursor cursor = db.getSupplierNames();
                    if (cursor.moveToFirst()) {
                        do {
                            String a = (cursor.getString(0).toString());

                            String b = (cursor.getString(1).toString());

                            String c = (cursor.getString(2).toString());

                            String d = (cursor.getString(3).toString());
                            if ((b + " : " + d).equals(selectedname)) {
                                SendSMSandsaveLastOrder(b, c, d);

                            }

                            counter++;
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                } else {
                    Toast.makeText(getApplicationContext(), "Add Atleast One Item First", Toast.LENGTH_LONG).show();
                }
            }
        });


        btnsellnewitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtpaid.getText().toString().equals("")) {
                    showDialoge(v);

                } else {
                    Toast.makeText(getApplicationContext(), "First Submit the Record", Toast.LENGTH_LONG).show();
                }


            }

        });

    }
    private void SendSMSandsaveLastOrder(String b, String c, String d) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String pass;
            //paid detail
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            pass = tvorder.getText().toString() + "\n" + sharedPreferences.getString("userShopName", getResources().getString(R.string.app_name)) + "\n" + mydate;

            Toast.makeText(getApplicationContext(), "Order is sent to '" + b + "' Successfully", Toast.LENGTH_LONG).show();
            ArrayList<String> parts = smsManager.divideMessage(pass);
            smsManager.sendMultipartTextMessage(c, null, parts, null, null);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor spedit = sp.edit();
            spedit.putString("lastOrder", pass);
            spedit.commit();
            tvorder.setText("نیاارڈر :");
            add = false;
        }catch (Exception e)
        {
            Toast.makeText(this, "Please Grant Permisson to Send SMS", Toast.LENGTH_SHORT).show();
            Intent intentp = new Intent();
            intentp.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intentp.setData(uri);
            startActivity(intentp);
        }
    }


    private String showDialoge(final View view) {
        try {
////            String GetSQliteQuery = "SELECT * FROM itemsTable";
//  //          SQLITEDATABASE = openOrCreateDatabase("MyShopDB", Context.MODE_PRIVATE, null);
//    //        itemcorsore = SQLITEDATABASE.rawQuery(GetSQliteQuery, null);

            itemcorsore = db.getItemNames();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }

        // custom dialog
        dialog = new Dialog(Order_Place.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        List<String> stringList = new ArrayList<>();  // here is list
        List<String> myString2 = new ArrayList<>();
        int count = 0;
        int z = 0;
        boolean found = false;
        if (itemcorsore.moveToFirst()) {
            do {

                String b = (itemcorsore.getString(1).toString());
                String c = (itemcorsore.getString(2).toString());

                myString2.add(b + ":" + c);
            }
            while (itemcorsore.moveToNext());

        }
        itemcorsore.close();
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        IgnoreCaseComparator icc = new IgnoreCaseComparator();

        java.util.Collections.sort(myString2, icc);

        for (int i = 0; i < myString2.size(); i++) {
            stringList.add(myString2.get(i));
        }
        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(Order_Place.this); // dynamically creating RadioButton and adding to RadioGroup.
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

                        a = btn.getText().toString();

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("How many: " + a + "?");

// Set up the input
                        final EditText input = new EditText(view.getContext());
                        input.setInputType(InputType.TYPE_CLASS_PHONE);
                        input.setHint("How many Enter Number");
                        builder.setView(input);
// Set up the buttons
                        builder.setPositiveButton("Finished", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strNOI = input.getText().toString();
//                                if(Integer.parseInt(strNOI)<=Integer.parseInt(getNumber(a))) {
                                NumberOfProducts = Integer.parseInt(strNOI);
                                tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + ", " + a + " :" + NumberOfProducts );
                                cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + ", " + a + "     : " + NumberOfProducts ;
                                itemOrderno = itemOrderno + 1;
                                int remainingitem = Integer.parseInt(getNumber(a)) + NumberOfProducts;
                                TempereryList("" + getID(a), "" + remainingitem);
                                //Toast.makeText(getApplicationContext(),""+getPrice(),Toast.LENGTH_SHORT).show();
                                add = true;
                            }
                        });
                        builder.setNegativeButton("AddNew", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface abc, int which) {
                                String strNOI = input.getText().toString();
                                // if(Integer.parseInt(strNOI)<=Integer.parseInt(getNumber(a))) {
                                NumberOfProducts = Integer.parseInt(strNOI);
                                tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + ", " + a + " :" + NumberOfProducts );
                                cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + ", " + a + "     : " + NumberOfProducts ;
                                itemOrderno = itemOrderno + 1;
                                int remainingitem = Integer.parseInt(getNumber(a)) + NumberOfProducts;
                                TempereryList("" + getID(a), "" + remainingitem);
                                //Toast.makeText(getApplicationContext(),""+getPrice(),Toast.LENGTH_SHORT).show();
                                add = true;
                                showDialoge(view);
                            }
                        });

                        builder.show();

                        dialog.dismiss();
                    }
                }
            }

        });
        return a;
    }

    private String getID(String a) {
        String price = "";
        try {
// //           String GetSQliteQuery = "SELECT * FROM itemsTable";
//   //         SQLITEDATABASE = openOrCreateDatabase("MyShopDB", Context.MODE_PRIVATE, null);
//     //       itemcorsore = SQLITEDATABASE.rawQuery(GetSQliteQuery, null);

       itemcorsore = db.getItemNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }
        if (itemcorsore.moveToFirst()) {
            do {
                String d = (itemcorsore.getString(1).toString());
                String c = (itemcorsore.getString(2).toString());
                if (a.equals(d + ":" + c)) {
                    String b = (itemcorsore.getString(0).toString());
                    price = b;

                }

            }
            while (itemcorsore.moveToNext());

        }
        return price;
    }

    private String getNewSelectedname(String a) {
        String newSN = "";
        try {
// //           String GetSQliteQuery = "SELECT * FROM SupplierTable";
//   //         SQLITEDATABASE = openOrCreateDatabase("MyShopDB", Context.MODE_PRIVATE, null);
//     //       itemcorsore = SQLITEDATABASE.rawQuery(GetSQliteQuery, null);

            itemcorsore = db.getSupplierNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }
        if (itemcorsore.moveToFirst()) {
            do {
                String id = (itemcorsore.getString(0).toString());
                String d = (itemcorsore.getString(1).toString());
                String c = (itemcorsore.getString(3).toString());
                // Toast.makeText(getApplicationContext(),""+a+"//"+id,Toast.LENGTH_LONG).show();
                if (a.equals(id)) {
                    newSN = d + " : " + c;
                    //Toast.makeText(getApplicationContext(),""+newSN+"//"+userid,Toast.LENGTH_LONG).show();
                }

            }
            while (itemcorsore.moveToNext());

        }
        return newSN;
    }

    private void TempereryList(String a, String strNOI) {
        itemid.add(a);//this adds an id to the list.
        NumberOItem.add(strNOI);//this adds an id to the list.

    }

    private String getNumber(String a) {
        String price = "";
        try {
// //           String GetSQliteQuery = "SELECT * FROM itemsTable";
//   //         SQLITEDATABASE = openOrCreateDatabase("MyShopDB", Context.MODE_PRIVATE, null);
//     //       itemcorsore = SQLITEDATABASE.rawQuery(GetSQliteQuery, null);

            itemcorsore = db.getItemNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }
        if (itemcorsore.moveToFirst()) {
            do {
                String d = (itemcorsore.getString(1).toString());
                String c = (itemcorsore.getString(2).toString());
                if (a.equals(d + ":" + c)) {
                    String b = (itemcorsore.getString(3).toString());
                    price = b;

                }

            }
            while (itemcorsore.moveToNext());

        }
        return price;
    }

    private String getPrice(String a) {
        String price = "";
        try {
//  //          String GetSQliteQuery = "SELECT * FROM itemsTable";
//    //        SQLITEDATABASE = openOrCreateDatabase("MyShopDB", Context.MODE_PRIVATE, null);
//      //      itemcorsore = SQLITEDATABASE.rawQuery(GetSQliteQuery, null);

       itemcorsore = db.getItemNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }
        if (itemcorsore.moveToFirst()) {
            do {
                String d = (itemcorsore.getString(1).toString());
                String c = (itemcorsore.getString(2).toString());
                if (a.equals(d + ":" + c)) {
                    String b = (itemcorsore.getString(5).toString());
                    tempitemid = itemcorsore.getString(1).toString();
                    price = b;
                }

            }
            while (itemcorsore.moveToNext());

        }
        return price;
    }

    private void showtotalPuranaQarza(String selectedname) {
////        String GetSQliteQuery = "SELECT * FROM SupplierTable";
//  //      String GetSQliteQuery2 = "SELECT * FROM CustomerTable";
//    //    SQLITEDATABASE = openOrCreateDatabase("MyShopDB", Context.MODE_PRIVATE, null);
//      //  Cursor cursor = SQLITEDATABASE.rawQuery(GetSQliteQuery, null);
//        // Cursor cursor2 = SQLITEDATABASE.rawQuery(GetSQliteQuery2, null);

                Cursor cursor = db.getSupplierNames();
                Cursor cursor2 = db.getCustomerNames();

        int z = 0;
        int count = 1;
        boolean found = false;

        if (cursor.moveToFirst()) {
            do {
                String a = (cursor.getString(0).toString());

                String b = (cursor.getString(1).toString());

                String c = (cursor.getString(2).toString());

                String d = (cursor.getString(3).toString());
                if ((b + " : " + d).equals(selectedname)) {
                    tvtotalqarza.setText(tvtotalqarzaText.getText().toString()+":"+d);
                    tvtotalqarzaText.setText("name: "+b + "\n"+"phone: "+c);
                    userid = a;
                }
                count++;
                totalbuy = totalbuy + Integer.parseInt(d);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (cursor2.moveToFirst()) {
            do {

                String d = (cursor2.getString(3).toString());
                totalsell = totalsell + Integer.parseInt(d);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void ShowItemNames() {
        //itemName="";
        try {
////            String GetSQliteQuery = "SELECT * FROM itemsTable";
//  //          SQLITEDATABASE = openOrCreateDatabase("MyShopDB", Context.MODE_PRIVATE, null);
//    //        itemcorsore = SQLITEDATABASE.rawQuery(GetSQliteQuery, null);

            itemcorsore = db.getItemNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }

        // custom dialog
        dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        List<String> stringList = new ArrayList<>();  // here is list
        int count = 0;
        int z = 0;
        boolean found = false;
        if (itemcorsore.moveToFirst()) {
            do {

                String b = (itemcorsore.getString(1).toString());
                String c = (itemcorsore.getString(2).toString());

                stringList.add(b);
            }
            while (itemcorsore.moveToNext());

        }
        itemcorsore.close();
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(getApplicationContext()); // dynamically creating RadioButton and adding to RadioGroup.
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
                        etUsername.setText(etUsername.getText().toString() + btn.getText().toString());

                    }
                    dialog.dismiss();
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), Supplier_Record.class);
        startActivity(i);
    }
}
