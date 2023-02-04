package com.example.myshopapp;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orders_Managment extends Activity {
    String selectedname = "";
    TextView tvtotalqarza;
    ListView list;
    ArrayList<String> OrdersList = new ArrayList<String>();
    //    Button btnpaid;
    Button btnRecDetail;
    Dialog  dialog;
    Cursor cursor;
    private DatabaseHelper db;
    final List<String> myString = new ArrayList<String>();
    final List<String> ListIDS = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_orders__managment);
        list = (ListView) findViewById(R.id.listview);
        db = new DatabaseHelper(this);
        GetRecord("");
        tvtotalqarza = (TextView) findViewById(R.id.totalqarza);
        btnRecDetail=(Button)findViewById ( R.id.btnrecDetail );
        btnRecDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCatagory (  );
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             int   pos = list.getPositionForView(view);
                int orderID = Integer.parseInt(ListIDS.get(pos));
                if(tvtotalqarza.getText().toString().equals("New Orders")) {
                    Intent intent = new Intent(getApplicationContext(), Order_confirmation.class);
                    intent.putExtra("orderid", "" + orderID);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), Order_Status_Changer.class);
                    intent.putExtra("orderid", "" + orderID);
                    startActivity(intent);
                }
            }
        });
    }

    private void GetRecord(String s) {
        cursor = db.getSellDetailData();
myString.clear();
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
                //Toast.makeText(this, s+":"+os, Toast.LENGTH_SHORT).show();
                if (s.equals("") || s.equals(os)) {
                    String pass = "";
                    String rpaid = "";
                    for (int j = 1; j < paid.length(); j++) {
                        rpaid = rpaid + paid.charAt(j);
                    }

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
                        pass = pass + "\n" + "Delivery_Charges    :" + cursor.getString(8).toString();
                        pass = pass + "\n" + "Order_Status    :" + cursor.getString(9).toString();
                    }
                    myString.add(pass);
                    ListIDS.add(id);
                }

            } while (cursor.moveToPrevious());
        }
        else{
            GetDataFromServer();
        }
        cursor.moveToLast();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myString);
        list.setAdapter(adapter);
    }

    private String showCatagory() {
        // custom dialog
        dialog = new Dialog ( Orders_Managment.this );
        dialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
        dialog.setContentView ( R.layout.radiobutton_dialog );
        List<String> myString2 = new ArrayList<> ( );
                myString2.add ( "New Orders" );
        myString2.add ( "In Progress" );
        myString2.add ( "Delivered" );
        myString2.add ( "Paid" );
        RadioGroup rg = (RadioGroup) dialog.findViewById ( R.id.radio_group );
        //rg.setBackgroundColor ( getResources().getColor( android.R.color.background_dark)) ;
        for (int i = 0; i < myString2.size ( ); i++) {
            RadioButton rb = new RadioButton ( getApplicationContext () ); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText ( myString2.get ( i ) );
            // Toast.makeText ( this , ""+myString2.get ( i )  , Toast.LENGTH_SHORT ).show ( );
            rg.addView ( rb );
        }

        dialog.show ( );

        rg.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener ( ) {

            @Override
            public void onCheckedChanged(RadioGroup group , int checkedId) {
                int childCount = group.getChildCount ( );
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt ( x );
                    if (btn.getId ( ) == checkedId) {

                        String a = btn.getText ( ).toString ( );
 tvtotalqarza.setText ( a );
GetRecord(a);                        dialog.dismiss ();
                    }
                }

            }
        } );
        return null;
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

}
