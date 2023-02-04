package com.example.myshopapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account_Section extends Activity {
    SQLiteDatabase SQLITEDATABASE;
    ListView list;
    String GetSQliteQuery;
    Cursor cursor,transCursor;
    String selectedname="";
    TextView tvTotalqarza;
    int counter=0;
    private DatabaseHelper db;
    TextView tvTSloan,tvTCloan,tvbalance,tvProfit;
    Button btnstart,btnend,btnsearch;
    Dialog dialog;
    String a="";
    Boolean btnbspreess=false,btnsearchpree=false;
    List<String> myString2 = new ArrayList<String>();
    List<String> TransectionT = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setIcon(R.drawable.icon);
        setContentView(R.layout.activity_account__section);
        tvTotalqarza=(TextView)findViewById(R.id.btntotalQarza);
        //tvTotalqarza.setText(tvTotalqarza.getText().toString()+countTotalQarza());
        list = (ListView) findViewById(R.id.listView1);
        tvTSloan=(TextView)findViewById(R.id.tvTotalSupLoan);
        tvTCloan=(TextView)findViewById(R.id.tvTotalCusLoan);
        tvProfit=(TextView)findViewById(R.id.tvTotalprofit);
        tvbalance=(TextView)findViewById(R.id.tvTotalBalance);
        btnstart=(Button)findViewById(R.id.btnstart);
        btnend=(Button)findViewById(R.id.btnend);
        btnsearch=(Button)findViewById(R.id.btnsearch);
        db = new DatabaseHelper(this);
        registerReceiver(new NetworkMonitor(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        tvTSloan.setText(""+countTotalQarza());
        tvTCloan.setText(""+countTotalcQarza());
        tvbalance.setText(""+(Integer.parseInt(tvTCloan.getText().toString())-Integer.parseInt(tvTSloan.getText().toString())));
        try
        {

            cursor = db.getTransectionData();
            cursor.moveToFirst();
            btnstart.setText(cursor.getString(3).toString());
            cursor.moveToLast();
            btnend.setText(cursor.getString(3).toString());
            cursor.moveToFirst();
            showDetailinList();

        }catch (Exception e){
//            if(myString2.isEmpty())
//            {
                GetDataFromServer();
            //}
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnbspreess=false;
                showDialoge(v,1);
            }
        });

        btnend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnbspreess==true)
                {
                    showDialoge(v, 2);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Select start Date First",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnsearchpree=true;
                showDetailinList();
            }
        });


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                //code below when user long press on item
                final String strWithcounter=list.getItemAtPosition(index).toString();
                AlertDialog.Builder builder2=new AlertDialog.Builder(Account_Section.this);
                builder2.setMessage(""+strWithcounter);
                builder2.setPositiveButton("Cancel",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                });

                builder2.setNegativeButton("Delete", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {


                        //Toast.makeText(getApplicationContext(), "U Clicked delete ", Toast.LENGTH_LONG).show();
                        int UserID = Integer.parseInt(getid(strWithcounter));
                        deleteCurrent(0,1,0,UserID);


                        Toast.makeText(getApplicationContext(), "Customer record Deleted Successfully", Toast.LENGTH_LONG).show();

                           Intent i=new Intent(getApplicationContext(),Account_Section.class);
                        startActivity(i);

                    }

                });

                builder2.show();
//                Toast.makeText(getApplicationContext(), "long press:" + selectedname, Toast.LENGTH_LONG).show();
                return true;

            }
        });
    }

    private void showDetailinList() {
        myString2.clear();
//        try
//
//        {
//            try
//            {
        cursor = db.getTransectionData();

//            }catch (Exception e){}
//            try
//            {


        int totalselling=0,totalbuying=0,balance=0;
        int totalprofit=0;
        if (cursor.moveToLast()) {
            do {
                String a = (cursor.getString(0).toString());

                String b = (cursor.getString(2).toString());

                String c = (cursor.getString(3).toString());

                String d = (cursor.getString(4).toString());
                String e = (cursor.getString(5).toString());
                boolean find=checkForStartandEnd(c);

                if(find==true) {
                    if(d.equals(""))
                    {
                        d="0";
                    }
                    myString2.add("\nDate :" + c
                            +"\nTotal profit :"+cursor.getString(6).toString()+ "\nTotal selling :" + d + "\nTotal buying :" + b + "\nBalance:" + e);
                    totalselling=totalselling+Integer.parseInt(d);
                    totalbuying=totalbuying+Integer.parseInt(b);
                    totalprofit=totalprofit+Integer.parseInt(cursor.getString(6).toString());
                    TransectionT.add(b);
                }
                counter++;
            } while (cursor.moveToPrevious());
            balance=totalselling-totalbuying;
            tvProfit.setText(""+totalprofit);
            if(btnsearchpree==true)
            {
                tvbalance.setText(""+balance);
                tvTCloan.setText(""+totalselling);
                tvTSloan.setText(""+totalbuying);
                tvProfit.setText(""+totalprofit);
                btnsearchpree=false;
            }
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myString2);
        list.setAdapter(adapter);
        cursor.close();

//        } catch (Exception e) {
//
//            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
//            Intent intent =                                   new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//        }
//    }catch (Exception e)
//            {}
    }

    private boolean checkForStartandEnd(String c) {
        //Toast.makeText(getApplicationContext(), "check called for :"+c, Toast.LENGTH_LONG).show();
        Boolean find=false;
        String btnstartText=btnstart.getText().toString();
        int strtday=Integer.parseInt(Character.toString(btnstartText.charAt(0))+Character.toString(btnstartText.charAt(1)));
        int strtmonth=Integer.parseInt(Character.toString(btnstartText.charAt(3))+Character.toString(btnstartText.charAt(4)));
        int strtyear=Integer.parseInt(Character.toString(btnstartText.charAt(6))+Character.toString(btnstartText.charAt(7))+Character.toString(btnstartText.charAt(8))+Character.toString(btnstartText.charAt(9)));


        int inday=Integer.parseInt(Character.toString(c.charAt(0))+Character.toString(c.charAt(1)));
        int inmonth=Integer.parseInt(Character.toString(c.charAt(3))+Character.toString(c.charAt(4)));
        int inyear=Integer.parseInt(Character.toString(c.charAt(6))+Character.toString(c.charAt(7))+Character.toString(c.charAt(8))+Character.toString(c.charAt(9)));

        String btnendtText=btnend.getText().toString();
        int endday=Integer.parseInt(Character.toString(btnendtText.charAt(0))+Character.toString(btnendtText.charAt(1)));
        int endmonth=Integer.parseInt(Character.toString(btnendtText.charAt(3))+Character.toString(btnendtText.charAt(4)));
        int endyear=Integer.parseInt(Character.toString(btnendtText.charAt(6))+Character.toString(btnendtText.charAt(7))+Character.toString(btnendtText.charAt(8))+Character.toString(btnendtText.charAt(9)));

//        Toast.makeText(getApplicationContext(), "start :"+strtday+"/"+strtmonth+"/"+strtyear, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), "in :"+inday+"/"+inmonth+"/"+inyear, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), "end :"+endday+"/"+endmonth+"/"+endyear, Toast.LENGTH_LONG).show();

        if(inyear>=strtyear&&inyear<=endyear)
        {
            if(inmonth>=strtmonth&&inmonth<=endmonth)
            {
                if(inday>=strtday&&inday<=endday)
                {
                    find=true;
                    //Toast.makeText(getApplicationContext(), "check find", Toast.LENGTH_LONG).show();
                }
            }
        }

        return  find;
    }

    private void showDialoge(final View view, final int btnno) {

        try {

            transCursor = db.getTransectionData();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }

        // custom dialog
        dialog = new Dialog(Account_Section.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        List<String> stringList = new ArrayList<>();
        List<String> myString2 = new ArrayList<>();  // here is list
        Boolean addToList=false;
        if (transCursor.moveToFirst()) {
            do {

                String b = (transCursor.getString(3).toString());
                if (btnbspreess == true) {
                    if (btnstart.getText().toString().equals(b)) {
                        addToList = true;
                    }
                    if (addToList == true) {
                        myString2.add(b);
                    }
                }
                else {
                    myString2.add(b);
                }
            }
            while (transCursor.moveToNext());

        }
        transCursor.close();
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        IgnoreCaseComparator icc = new IgnoreCaseComparator();

        java.util.Collections.sort(myString2, icc);

        for (int i = 0; i < myString2.size(); i++) {
            stringList.add(myString2.get(i));
        }
        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(Account_Section.this); // dynamically creating RadioButton and adding to RadioGroup.
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
                        if (btnno==1)
                        {
                            btnstart.setText(btn.getText().toString());
                            btnbspreess=true;
                        }
                        else {
                            btnend.setText(btn.getText().toString());
                            btnbspreess=false;
                        }


                        dialog.dismiss();
                    }
                }
            }

        });
    }

    private String getid(String strWithcounter) {
        String id="";
        try

        {

            Cursor cursor = db.getTransectionData();

            if (cursor.moveToFirst()) {
                do {
                    String a = (cursor.getString(0).toString());

                    String b = (cursor.getString(2).toString());

                    String c = (cursor.getString(3).toString());

                    String d = (cursor.getString(4).toString());
                    String e = (cursor.getString(5).toString());
                    String f = (cursor.getString(6).toString());
                    if(d.equals(""))
                    {
                        d="0";
                    }
                    String myString2=("\nDate :"+c+"\nTotal profit :"+f
                            +"\nTotal selling :"+d+"\nTotal buying :"+b+"\nBelance:"+e);
                    if(myString2.equals(strWithcounter)) {
                        id=a;
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
        }
        return id;
    }

    private int countTotalcQarza() {
        int totalq=0;
        try

        {

            Cursor cursor = db.getCustomerNames();

            int z = 0;
            int count = 0;
            boolean found = false;

            if (cursor.moveToFirst()) {
                do {
                    String a = (cursor.getString(0).toString());

                    String b = (cursor.getString(1).toString());

                    String c = (cursor.getString(2).toString());

                    String d = (cursor.getString(3).toString());
                    totalq=totalq+(Integer.parseInt(d));
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
        }
        return totalq;
    }

    private int countTotalQarza() {
        int totalq=0;
        try

        {

            Cursor cursor = db.getSupplierNames();

            int z = 0;
            int count = 0;
            boolean found = false;

            if (cursor.moveToFirst()) {
                do {
                    String a = (cursor.getString(0).toString());

                    String b = (cursor.getString(1).toString());

                    String c = (cursor.getString(2).toString());

                    String d = (cursor.getString(3).toString());
                    totalq=totalq+(Integer.parseInt(d));
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
        }
        return totalq;
    }

    private void deleteCurrent(int insertStatus,int deleteStatus,int updateStatus,int UserID) {
        db.deleteTransectionStatus(insertStatus, deleteStatus, updateStatus, UserID);

        Toast.makeText(getApplicationContext(), "Sell Record is Deleted", Toast.LENGTH_LONG).show();
    }

    private void GetDataFromServer() {

        final String Phone = "phone";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.TRANSECTION_SERVER_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++)
                            {

                                JSONObject obj = array.getJSONObject(i);

                                obj.getString("ttype");
                                obj.getString("tbillid");
                                obj.getString("tdate");
                                obj.getString("tamount");
                                obj.getString("tbalance");
                                obj.getString("tprofit");

                                Toast.makeText(getApplicationContext(), ""+obj.getString("ttype"), Toast.LENGTH_SHORT).show();
                       saveTransectionToLocalStorage(
                               obj.getString("ttype"),
                               obj.getString("tbillid"),
                               obj.getString("tdate"),
                               obj.getString("tamount"),
                               obj.getString("tbalance"),
                               obj.getString("tprofit"),0,0,0);

                            }
                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("id", id);
                params.put("phone", Phone);


                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void saveTransectionToLocalStorage(String typeOT, String billid,String date,String amount,String balance,String profit,int insertStatus,int deleteStatus,int updateStatus)
    {
        db.addTransection(typeOT, billid,date,amount,balance,profit,insertStatus,deleteStatus,updateStatus);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
