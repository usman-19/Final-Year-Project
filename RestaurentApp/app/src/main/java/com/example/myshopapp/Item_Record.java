package com.example.myshopapp;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class Item_Record extends Activity {

    ListView list;
    String  selectednameId;
    Cursor cursor,itemcorsore;
    Boolean longpress=false;
    String selectedname="";
    TextView tvTotalqarza;
    SharedPreferences sp;
    int counter=0;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setIcon(R.drawable.icon);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_item_record);
        // tvTotalqarza=(TextView)findViewById(R.id.btntotalQarza);
        //tvTotalqarza.setText(tvTotalqarza.getText().toString()+countTotalQarza());
        List<String> myString = new ArrayList<String>();
        List<String> myString2 = new ArrayList<String>();
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        list = (ListView) findViewById(R.id.listView1);
        Button btnaddnew=(Button)findViewById(R.id.btnNew);
        Button btntotalstock=(Button)findViewById(R.id.btnamount);
        db = new DatabaseHelper(this);
        btntotalstock.setOnClickListener(new View.OnClickListener() {
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
        btnaddnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),add_update_item.class);
                startActivity(i);
            }
        });

        try

        {

            cursor = db.getItemNames();

            int z = 0;
            int count = 0;
            boolean found = false;

            if (cursor.moveToFirst()) {
                do {
                    String a = (cursor.getString(0).toString());

                    String b = (cursor.getString(1).toString());

                    String c = (cursor.getString(2).toString());

                    String d = (cursor.getString(3).toString());
                    myString2.add( b+"/"+c+" : "+d);
                    counter++;
                } while (cursor.moveToNext());
            }
            IgnoreCaseComparator icc = new IgnoreCaseComparator();

            java.util.Collections.sort(myString2,icc);

            for (int i=0; i<myString2.size();i++)
            {
                myString.add((i+1)+") "+myString2.get(i));
            }

            if(myString2.isEmpty())
            {
                GetDataFromServer();
            }



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myString);
            list.setAdapter(adapter);

//            cursor.close();

        } catch (Exception e) {


            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
        }
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                //code below when user long press on item
                String strWithcounter=list.getItemAtPosition(index).toString();
                selectedname="";
                // Toast.makeText(getApplicationContext(),"index "+index,Toast.LENGTH_SHORT).show();
                if (index < 10&&index>=0) {
                    for (int i = 3; i < strWithcounter.length(); i++) {
                        selectedname = selectedname + strWithcounter.charAt(i);
                    }
                }else if(index>9&&index<100)
                {
                    for (int i = 4; i < strWithcounter.length(); i++) {
                        selectedname = selectedname + strWithcounter.charAt(i);
                    }
                    //Toast.makeText(getApplicationContext(),"index "+index,Toast.LENGTH_SHORT).show();
                }
                else {
                    for (int i = 5; i < strWithcounter.length(); i++) {
                        selectedname = selectedname + strWithcounter.charAt(i);
                    }
                }

                //Toast.makeText(getApplicationContext(), "selected item"+selectedname, Toast.LENGTH_LONG).show();
                // convertToOrgnal();
                AlertDialog.Builder builder2=new AlertDialog.Builder(Item_Record.this);
                builder2.setMessage(""+selectedname);
                builder2.setPositiveButton("Update",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Intent i=new Intent(getApplicationContext(),add_update_item.class);
                        i.putExtra("name",selectedname);
                        startActivity(i);

                    }

                });

                builder2.setNegativeButton("Delete", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {



                        // Toast.makeText(getApplicationContext(), "U Clicked delete ", Toast.LENGTH_LONG).show();
                        showTheRec(selectedname);
                       int UserID = Integer.parseInt(getID(selectedname));
                        deleteCurrent(0,1,0,UserID);
                        Intent i=new Intent(getApplicationContext(),Item_Record.class);
                        startActivity(i);

                    }

                });

                builder2.show();
//                Toast.makeText(getApplicationContext(), "long press:" + selectedname, Toast.LENGTH_LONG).show();
                return true;

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //code below when user press item
                //Toast.makeText(getApplicationContext(), "press" + list.getItemAtPosition(arg2).toString(), Toast.LENGTH_LONG).show();
//                selectedname=list.getItemAtPosition(arg2).toString();
//                Intent i=new Intent(getApplicationContext(),Sell_on_Customer.class);
//                i.putExtra("name",selectedname);
//                startActivity(i);

            }
        });

    }

    private String getID(String a) {

       String price="";
        int count=1;
        try
        {
            itemcorsore = db.getItemNames();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Pleas Add Items to The Item List First",Toast.LENGTH_LONG).show();
        }
        if (itemcorsore.moveToFirst()) {
            do {
                String name=(itemcorsore.getString(1).toString());
                String des=""+(itemcorsore.getString(2).toString());
                String iQtt=""+(itemcorsore.getString(3).toString());
                String strtable=name+":"+des;
                //Toast.makeText(getApplicationContext(),a+"====="+strtable,Toast.LENGTH_LONG).show();
                if((name+"/"+des+" : "+iQtt).equals(a))
                {
                    String b = (itemcorsore.getString(0).toString());
                    price = b;

                }
                count++;

            }
            while (itemcorsore.moveToNext());

        }
       return price;
    }

    private void showTheRec(String selectedname) {
        try

        {
            Cursor cursor = db.getItemNames();
            int z = 0;
            int count = 1;
            boolean found = false;

            if (cursor.moveToFirst()) {
                do {
                    String a = (cursor.getString(0).toString());

                    String b = (cursor.getString(1).toString());

                    String c = (cursor.getString(2).toString());

                    String d = (cursor.getString(3).toString());
                    if((b+"/"+c+" : "+d).equals(selectedname)) {
                        selectednameId=a;
                    }
                    count++;
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
        }
    }

    private void deleteCurrent(int insertStatus,int deleteStatus,int updateStatus,int UserID) {
        db.deleteItemStatus(insertStatus, deleteStatus, updateStatus, UserID);

        Toast.makeText(getApplicationContext(), "Item Record is Deleted", Toast.LENGTH_LONG).show();
    }

    private void GetDataFromServer() {

        final String iName = "iName";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.ITEM_SERVER_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++)
                            {

                                JSONObject obj = array.getJSONObject(i);

                                obj.getString("iName");
                                obj.getString("iDes");
                                obj.getString("iQ");
                                obj.getString("iBp");
                                obj.getString("iSp");
                                obj.getString("iCatagory");
                                obj.getString("iDp");


                                Toast.makeText(getApplicationContext(), ""+obj.getString("iName"), Toast.LENGTH_SHORT).show();

                                saveItemToLocalStorage(obj.getString("iName"),obj.getString("iDes"),obj.getString("iQ"),obj.getString("iBp"),obj.getString("iSp")
                                        ,obj.getString("iCatagory"),obj.getString("iDp"),0,0,0);

                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

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
                params.put("iName", iName);


                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void ShowPassDialoge(View v) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(v.getContext());
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
                    Intent i=new Intent(getApplicationContext(),Total_Stock_amount.class);
                    i.putExtra("type","buy");
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


    private void saveItemToLocalStorage(String Iname , String Ides , String itemQ , String itemBP , String itemSP , String iCatagory , String iDp , int insertStatus , int deleteStatus , int updateStatus)
    {
        db.addItem(Iname, Ides,itemQ,itemBP,itemSP, iCatagory , iDp , insertStatus,deleteStatus,updateStatus);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
