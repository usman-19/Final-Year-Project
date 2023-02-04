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

public class Customer_Record_Detail extends Activity {
   ListView list;
    String selectednameId;
    Cursor cursor;
    Boolean longpress = false;
    String selectedname;
    TextView tvTotalqarza;
    String inputcatgory,userid;
    TextView btnnam;
    int pos;
    private DatabaseHelper db;
    String cashCus;
    final List<String> myString = new ArrayList<String>();
    final List<String> ListIDS = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_record__detail);
        registerReceiver(new NetworkMonitor(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        tvTotalqarza = (TextView) findViewById(R.id.btntotalQarza);
        Intent i = getIntent();
        selectedname = i.getStringExtra("name");
        inputcatgory = i.getStringExtra("inputcatagory");
        userid = i.getStringExtra("userID");
        cashCus = i.getStringExtra("cashCus");
        btnnam=(TextView)findViewById(R.id.btnnaam);
        btnnam.setText("");
        btnnam.setText("ٹوٹل پرانہ قرضہ:"+selectedname);
        //Toast.makeText(this, ""+userid, Toast.LENGTH_SHORT).show();
        //tvTotalqarza.setText(tvTotalqarza.getText().toString()+countTotalQarza());
        list = (ListView) findViewById(R.id.listView1);
        db = new DatabaseHelper(this);
        GetDataFromServer(selectedname);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, final View v,
                                           int index, long arg3) {
                //code below when user long press on item
                selectedname = list.getItemAtPosition(index).toString();
                AlertDialog.Builder builder2 = new AlertDialog.Builder(com.example.myshopapp.Customer_Record_Detail.this);
                builder2.setMessage("" + selectedname);
                builder2.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        Intent i=new Intent(getApplicationContext(),Add_Update_Item_Rec.class);
//                        i.putExtra("name",selectedname);
//                        startActivity(i);

                    }

                });

                builder2.setNegativeButton("Delete", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {


                        pos = list.getPositionForView(v);

                        // Toast.makeText(getApplicationContext(), "index : "+pos+":"+ListIDS.get(pos), Toast.LENGTH_LONG).show();

                        //showTheRec(selectedname);

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Enter passowrd");
                        //builder.setIcon(R.drawable.ic_success);
// Set up the input
                        final EditText input = new EditText(v.getContext());
                        input.setText("");
                        builder.setView(input);
// Set up the buttons
                        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
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

                                    if (savepass.equals(strNOI)) {

                                        int UserID = Integer.parseInt(ListIDS.get(pos));
                                        deleteCurrent(0,1,0,UserID);

                                        Toast.makeText(getApplicationContext(), "Customer record Deleted Successfully", Toast.LENGTH_LONG).show();

//                                    Intent i=new Intent(getApplicationContext(),Customer_R_Detail.class);
//                                    i.putExtra("name",btnnam.getText().toString());
//                                    btnnam.setText("");
//                                        i.putExtra("inputcatagory", "sup");
//                                        i.putExtra("cashCus", "کیش : سپلائیر");
//                                    i.putExtra("userID",userid);
//                                    startActivity(i);
                                        list.setAdapter(null);
                                        myString.clear();
                                        ListIDS.clear();
                                        GetRecord();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();

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

            }
        });

    }

    private void GetRecord() {
//try {
    cursor = db.getSellDetailData();

    if (cursor.moveToLast()) {
        do {
            String id = (cursor.getString(0).toString());
            String cid = (cursor.getString(1).toString());
            String date = (cursor.getString(2).toString());
            String descp = (cursor.getString(3).toString());
            String pt = (cursor.getString(4).toString());
            String nt = (cursor.getString(5).toString());
            String paid = (cursor.getString(6).toString());
            String belance = (cursor.getString(7).toString());
            String os = (cursor.getString(9).toString());
            String pass="";
            String rpaid="";
            for(int j=1;j<paid.length();j++) {
                rpaid=rpaid+paid.charAt(j);
            }
            if(cashCus.equals("کیش : کسٹمر"))
            {
                if (cid.equals("کیش : کسٹمر")) {
                    pass = pass + "\n" + "تاریخ :" + date;
                    pass = pass + "\n" + "تفصیل :" + descp +
                            "\n" + "ٹوٹل :" + nt;
                    pass=pass+"\n"+"Order_Status    :"+cursor.getString(9).toString();
                    myString.add(pass);
                    ListIDS.add(id);
                }
            }
            else {
                //Toast.makeText(getApplicationContext(),"user id"+cid+userid,Toast.LENGTH_SHORT).show();
                if (cid.equals(userid)) {

                    pass = "\n" + "پرانہ کھاتہ :" + pt +
                            "\n" + "تاریخ :" + date;
                    pass = pass + "\n" + " تفصیل :" + descp;

                    if (nt .equals ("0")) {
                        pass=pass+ "\n" + "میزان :" + belance;
                    }
                    else {
                        pass = pass + "\n" + " ٹوٹل :" + nt;
                        //paid detail
                        if(paid.equals("")) {
                        }
                        else {
                            String fc = Character.toString(paid.charAt(0));
                            if (fc.equals("P")) {
                                pass = pass + "\n" + "وصول :" + rpaid
                                        +"\n" + "بقایا :" + (Integer.parseInt(nt) - Integer.parseInt(rpaid));
                            }
                        }
                        pass=pass+ "\n" + "میزان :" + belance;
                        if(os.equals("New Orders")) {
                        }
                        else {
                            pass = pass + "\n" + "Delivery_Charges    :" + cursor.getString(8).toString();
                        }
                        pass=pass+"\n"+"Order_Status    :"+cursor.getString(9).toString();
                    }

                    myString.add(pass);
                    ListIDS.add(id);
                }
            }




        } while (cursor.moveToPrevious());
}
    else{
        GetDataFromServer();
    }
//        }catch (Exception e){
//    GetDataFromServer();
//    Toast.makeText ( this , "data not found" , Toast.LENGTH_SHORT ).show ( );
//}
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myString);
        list.setAdapter(adapter);
    }
    private void GetDataFromServer(final String a) {

        final String Phone = "phone";
        StringRequest stringRequest = new StringRequest( Request.Method.POST, NetworkMonitor.SELL_SERVER_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++)
                            {

                                JSONObject obj = array.getJSONObject(i);
                                String id=obj.getString("Cusid");
                                String date =obj.getString("Sdate");
                                String descp =obj.getString("Sdes");
                                String pt=obj.getString("Sprevtotal");
                                String nt = obj.getString("Snewtotal");
                                String paid = obj.getString("Spaid");
                                String belance = obj.getString("Sbalance");
                                String dcharges=obj.getString("Sdcharges");
                                String os = obj.getString("Sostatus") ;
                                String pass="";
                                String rpaid="";
                                for(int j=1;j<paid.length();j++) {
                                    rpaid=rpaid+paid.charAt(j);
                                }

                                    //Toast.makeText(getApplicationContext(),"user id"+cid+userid,Toast.LENGTH_SHORT).show();
                                    if (id.equals(userid)) {

                                        pass = "\n" + "پرانہ کھاتہ :" + pt +
                                                "\n" + "تاریخ :" + date;
                                        pass = pass + "\n" + " تفصیل :" + descp;

                                        if (nt.equals("0")) {
                                            pass = pass + "\n" + "میزان :" + belance;
                                        } else {
                                            pass = pass + "\n" + " ٹوٹل :" + nt;
                                            //paid detail
                                            if (paid.equals("")) {
                                            } else {
                                                String fc = Character.toString(paid.charAt(0));
                                                if (fc.equals("P")) {
                                                    pass = pass + "\n" + "وصول :" + rpaid
                                                            + "\n" + "بقایا :" + (Integer.parseInt(nt) - Integer.parseInt(rpaid));
                                                }
                                            }
                                            pass = pass + "\n" + "میزان :" + belance;
                                            if (os.equals("New Orders")) {
                                            } else {
                                                pass = pass + "\n" + "Delivery_Charges    :" + dcharges;
                                            }
                                            pass = pass + "\n" + "Order_Status    :" + os;
                                        }
                                        myString.add(pass);
                                        ListIDS.add(obj.getString("Id"));
                                    }
                            }
                            showdatainlist();
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
                Map<String, String> params = new HashMap<> ();
                //params.put("id", id);
                params.put("phone", Phone);


                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void showdatainlist() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myString);
        list.setAdapter(adapter);
    }
    private void deleteCurrent(int insertStatus,int deleteStatus,int updateStatus,int UserID) {
        db.deleteSellStatus(insertStatus, deleteStatus, updateStatus, UserID);

        Toast.makeText(getApplicationContext(), "Sell Record is Deleted", Toast.LENGTH_LONG).show();
    }
    private void GetDataFromServer() {

        final String Phone = "phone";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.SELL_SERVER_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++)
                            {

                                JSONObject obj = array.getJSONObject(i);

                                obj.getString("Cusid");
                                obj.getString("Sdate");
                                obj.getString("Sdes");
                                obj.getString("Sprevtotal");
                                obj.getString("Snewtotal");
                                obj.getString("Spaid");
                                obj.getString("Sbalance");
                                obj.getString("Sdcharges");
                                        obj.getString("Sostatus");

                                Toast.makeText(getApplicationContext(), ""+obj.getString("Cusid"), Toast.LENGTH_SHORT).show();

                                saveSellToLocalStorage(obj.getString("Cusid"),
                                        obj.getString("Sdate"),
                                        obj.getString("Sdes"),
                                        obj.getString("Sprevtotal"),
                                        obj.getString("Snewtotal"),
                                        obj.getString("Spaid"),
                                        obj.getString("Sbalance"),
                                        obj.getString("Sdcharges"),
                                        obj.getString("Sostatus"),0,0,0
                                );

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

    private void saveSellToLocalStorage(String cusid , String date , String description , String prevTotal , String newTotal , String Paid , String Balance , String sdcharges , String sostatus , int insertStatus , int deleteStatus , int updateStatus)
    {
        db.addSell(cusid,date,description,prevTotal,newTotal,Paid,Balance, sdcharges , sostatus , insertStatus,deleteStatus,updateStatus);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (inputcatgory.equals("Customer")) {
            Intent i=new Intent(getApplicationContext(),Sell_On_Customer.class);
            i.putExtra("name",selectedname);
            startActivity(i);
        } else {
            Intent i=new Intent(getApplicationContext(),Buy_From_Supplier.class);
            i.putExtra("name",selectedname);
            startActivity(i);
        }

    }
}
//            Intent intent = new Intent(
