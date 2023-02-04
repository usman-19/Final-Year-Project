package com.example.myshopapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
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


public class Supplier_Record extends Activity {
    ListView list;
    String  selectednameId;
    Cursor cursor,itemcorsore;
    Boolean longpress=false;
    String selectedname="";
    TextView tvTotalqarza;
    int counter=1;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_supplier__record);
        tvTotalqarza=(TextView)findViewById(R.id.btntotalQarza);
        tvTotalqarza.setText(tvTotalqarza.getText().toString()+countTotalQarza());
        db = new DatabaseHelper(this);
        list = (ListView) findViewById(R.id.listView1);

        registerReceiver(new NetworkMonitor(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        loadSupplierNames();


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, final View v,
                                           int index, long arg3) {
                //code below when user long press on item
                String strWithcounter=list.getItemAtPosition(index).toString();
                if(strWithcounter.equals("کیش : سپلائیر"))
                {
                    selectedname=(strWithcounter);
                }
                else {
                    selectedname="";

                    if (index < 10&&index>0) {
                        for (int i = 3; i < strWithcounter.length(); i++) {
                            selectedname = selectedname + strWithcounter.charAt(i);
                        }
                        //Toast.makeText(getApplicationContext(),"index "+index,Toast.LENGTH_SHORT).show();
                    }else if(index>9&&index<100)
                    {
                        for (int i = 4; i < strWithcounter.length(); i++) {
                            selectedname = selectedname + strWithcounter.charAt(i);
                        }
                        //Toast.makeText(getApplicationContext(),"index "+index,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for (int i = 5; i < strWithcounter.length(); i++) {
                            selectedname = selectedname + strWithcounter.charAt(i);
                        }
                    }
                }
                AlertDialog.Builder builder2=new AlertDialog.Builder(Supplier_Record.this);
                builder2.setMessage(""+selectedname);
                builder2.setPositiveButton("Update",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Intent i=new Intent(getApplicationContext(),add_update_supplier_rec.class);
                        i.putExtra("name",selectedname);
                        startActivity(i);

                    }

                });

                builder2.setNegativeButton("Delete", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {




                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Are you sure to Delete Record");

// Set up the input
                        final EditText input = new EditText(v.getContext());
                         input.setText("");
                         builder.setView(input);
// Set up the buttons
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strNOI = input.getText().toString();
                                SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                String savepass=sp.getString("password","");
                                Boolean passset=true;
                                if(savepass.equals(""))
                                {
                                    passset=false;

                                }
                                if (passset==false)
                                {
                                    Toast.makeText(getApplicationContext(),"please set your password",Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    if(savepass.equals(strNOI)) {
                                        //                        showTheRec(selectedname);
//                                        try {
                                            int UserID = Integer.parseInt(getID(selectedname));
                                            deleteCurrent(0,1,0,UserID);

                                            Intent i = new Intent(getApplicationContext(), Supplier_Record.class);
                                            startActivity(i);
//                                        } catch (Exception e) {
//                                            Toast.makeText(getApplicationContext(), "System File", Toast.LENGTH_SHORT).show();
//                                        }
                                    }
                                }

                            }
                        });
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface abc, int which) {
                               String strNOI = input.getText().toString();
                            }
                        });

                        builder.show();

                        dialog.dismiss();

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
                String strWithcounter=list.getItemAtPosition(arg2).toString();
                if(strWithcounter.equals("کیش : سپلائیر"))
                {
                    selectedname=(strWithcounter);
                }
                else {
                    selectedname="";

                    if (arg2 < 10&&arg2>0) {
                        for (int i = 3; i < strWithcounter.length(); i++) {
                            selectedname = selectedname + strWithcounter.charAt(i);
                        }
                        //Toast.makeText(getApplicationContext(),"index "+index,Toast.LENGTH_SHORT).show();
                    }else if(arg2>9&&arg2<100)
                    {
                        for (int i = 4; i < strWithcounter.length(); i++) {
                            selectedname = selectedname + strWithcounter.charAt(i);
                        }
                        //Toast.makeText(getApplicationContext(),"index "+index,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for (int i = 5; i < strWithcounter.length(); i++) {
                            selectedname = selectedname + strWithcounter.charAt(i);
                        }
                    }
                }
                Intent i=new Intent(getApplicationContext(),Buy_From_Supplier.class);
                if(strWithcounter.equals("کیش : سپلائیر"))
                {
                    i.putExtra("name","کیش : سپلائیر");
                }
                else {
                    i.putExtra("name",selectedname);
                }
                startActivity(i);


            }
        });
        Button btnaddnew=(Button)findViewById(R.id.btnNew);
        btnaddnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),add_update_supplier_rec.class);
                startActivity(i);
            }
        });

    }


    private void loadSupplierNames(){
        List<String> myString = new ArrayList<String>();
        List<String> myString2 = new ArrayList<String>();


        int z = 0;
        int count = 0;
        boolean found = false;
        myString.add("کیش : سپلائیر");

        Cursor cursor = db.getSupplierNames();

        if (cursor.moveToFirst()) {
            do {
                String a = (cursor.getString(0).toString());

                String b = (cursor.getString(1).toString());

                String c = (cursor.getString(2).toString());

                String d = (cursor.getString(3).toString());
                String e = (cursor.getString(4).toString());
                String f = (cursor.getString(5).toString());
                myString2.add(b+" : "+d+" \n"+e+" : "+f);
                counter++;
            } while (cursor.moveToNext());
        }
              IgnoreCaseComparator icc = new IgnoreCaseComparator();

              java.util.Collections.sort(myString2,icc);

        for (int i=0; i<myString2.size();i++)
        {
            myString.add((i+1)+") "+myString2.get(i));
//            Toast.makeText ( this , "full" , Toast.LENGTH_SHORT ).show ( );
        }
        if(myString2.isEmpty())
        {
            GetDataFromServer();
//            Toast.makeText ( this , "empty" , Toast.LENGTH_SHORT ).show ( );

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myString);
        list.setAdapter(adapter);
        cursor.close();

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
            // TODO: handle exception

         //   Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
        }
        return totalq;
    }


    private String getID(String a) {
        String price="";
        int count=1;
        try
        {

            itemcorsore = db.getSupplierNames();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Pleas Add Items to The Item List First",Toast.LENGTH_LONG).show();
        }
        if (itemcorsore.moveToFirst()) {
            do {
                String name=(itemcorsore.getString(1).toString());
                String des=""+(itemcorsore.getString(3).toString());
                String address=""+(itemcorsore.getString(4).toString());
                String catagory=""+(itemcorsore.getString(5).toString());
               // Toast.makeText(getApplicationContext(),a+"====="+name+" : "+des+"\n"+address+" : "+catagory,Toast.LENGTH_LONG).show();
                if((name+" : "+des+" \n"+address+" : "+catagory).equals(a))
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

private void deleteCurrent(int insertStatus,int deleteStatus,int updateStatus,int UserID) {
        db.deleteSupplierStatus(insertStatus, deleteStatus, updateStatus, UserID);

        Toast.makeText(getApplicationContext(), "Customer Record is Deleted", Toast.LENGTH_LONG).show();
        }


    private void GetDataFromServer() {

        final String Phone = "phone";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.SUPPLIER_SERVER_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++)
                            {

                                JSONObject obj = array.getJSONObject(i);

                                obj.getString("name");
                                obj.getString("phone");
                                obj.getString("qarza");
                                obj.getString("address");
                                obj.getString("catagory");

                               Toast.makeText(getApplicationContext(), ":"+obj.getString("name"), Toast.LENGTH_SHORT).show();

                                saveSuppToLocalStorage(obj.getString("name"),obj.getString("phone"),obj.getString("qarza"),obj.getString("address"),obj.getString("catagory"),0,0,0);

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
                params.put("phone", Phone);


                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }



    private void saveSuppToLocalStorage(String Sname, String Sphone, String Sqarza, String address, String catagory, int insertStatus, int deleteStatus, int updateStatus)
    {
        db.addSupplier(Sname, Sphone,Sqarza, address , catagory , insertStatus,deleteStatus,updateStatus);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
          startActivity(intent);
    }
}

