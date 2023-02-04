package com.example.myshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

import static com.example.myshopapp.NetworkMonitor.DATA_SAVED_BROADCAST;
import static com.example.myshopapp.NetworkMonitor.ITEM_UPDATE_URL;
import static com.example.myshopapp.NetworkMonitor.SEll_UPDATE_URL;

public class Order_confirmation extends AppCompatActivity {
String selectedid;
TextView tvorder,tvtotal,tvbelance;
EditText edtDeliverycharges;
Button btnconfirm;
    Cursor cursor;
    String cusphone;
    ProgressBar simpleProgressBar;
    private DatabaseHelper db;
    boolean calculate=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        tvorder=(TextView)findViewById(R.id.tvorderdetail);
        tvtotal=(TextView)findViewById(R.id.tvordertotal);
        tvbelance=(TextView)findViewById(R.id.tvorderbelance);
        edtDeliverycharges=(EditText)findViewById(R.id.edtdeliverycharges);
        btnconfirm=(Button)findViewById(R.id.btnconfirm);
        btnconfirm.setText("Calculate");
        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        Intent i = getIntent();
        selectedid = i.getStringExtra("orderid");
        db = new DatabaseHelper(this);
        GetDataFromServer("");
        simpleProgressBar.setVisibility(View.VISIBLE);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtDeliverycharges.getText().toString().equals(""))
                {
                    edtDeliverycharges.setError("Delivery Charges most be entered");
                }
                else  if(calculate==false)
                {
                    int intdc=Integer.parseInt(edtDeliverycharges.getText().toString());
                    int total=Integer.parseInt(tvtotal.getText().toString())+intdc;
                    int balance=Integer.parseInt(tvbelance.getText().toString())+intdc;
                    tvtotal.setText(""+total);
                    tvbelance.setText(""+balance);
                    calculate=true;
                    edtDeliverycharges.setKeyListener(null);
                    btnconfirm.setText("Confirm");
                   int id=Integer.parseInt ( selectedid );
                    //Toast.makeText(Order_confirmation.this, id+""+cusphone, Toast.LENGTH_SHORT).show();
                    updateCurrent(""+balance,0,0,1,id);
                    Toast.makeText ( Order_confirmation.this , "selected id"+selectedid , Toast.LENGTH_SHORT ).show ( );
                    UpdateSellrecord(""+total,""+balance,""+intdc,"In Progress",id);
                    Intent intent=new Intent ( getApplicationContext (),Orders_Managment.class );
                    startActivity ( intent );
                }
                else {



                }
            }
        });

    }

    private void updateOrder(String s, String s1, String s2, String in_progress, int i, int i1, int i2, int oid) {
        db.UpdateOrder(s, s1, s2, in_progress,i,i1,i2, oid);
    }
    private void UpdateSellrecord(final String s, final String s1, final String s2, final String in_progress, int oid) {
        // final String id= ""+updateID;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEll_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                //db.changeUpdateItemNameStatus(updateID, UPDATE_SYNCED );

                                getApplicationContext().sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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
                params.put("total", s);
                params.put("balance", s1);
                params.put("dc", s2);
                params.put("os", in_progress);
                params.put("id", selectedid);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void GetDataFromServer(final String a) {

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
                                String id=obj.getString("Id");
                                String date =obj.getString("Sdate");
                                String descp =obj.getString("Sdes");
                                String pt=obj.getString("Sprevtotal");
                                String nt = obj.getString("Snewtotal");
                                String paid = obj.getString("Spaid");
                                String belance = obj.getString("Sbalance");
                                String dcharges=obj.getString("Sdcharges");
                                String os = obj.getString("Sostatus") ;

                                if (id.equals(selectedid)) {
                                    Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                                    //cusphone=cid;
                                    String pass = "";
                                    String rpaid = "";
                                    for (int j = 1; j < paid.length(); j++) {
                                        rpaid = rpaid + paid.charAt(j);
                                    }

                                    pass = "\n" + "پرانہ کھاتہ :" + pt +
                                            "\n" + "تاریخ :" + date;
                                    pass = pass + "\n" + " تفصیل :" + descp;

                                    if (nt.equals("0")) {
                                        tvbelance.setText(belance);
                                    } else {
                                        tvtotal.setText(nt);
                                        //paid detail
                                        if (paid.equals("")) {
                                        } else {
                                            String fc = Character.toString(paid.charAt(0));
                                            if (fc.equals("P")) {
                                                pass = pass + "\n" + "وصول :" + rpaid
                                                        + "\n" + "بقایا :" + (Integer.parseInt(nt) - Integer.parseInt(rpaid));
                                            }
                                        }
                                        tvbelance.setText(belance);
                                        edtDeliverycharges.setText(dcharges);
                                        pass = pass + "\n" + "Order_Status    :" +os;

                                    }
                                    tvorder.setText(pass);
                                    simpleProgressBar.setVisibility(View.GONE);
                                }

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
    private void updateCurrent(String qarza,int insertStatus,int deleteStatus,int updateStatus,int UserID) {
        db.UpdateCusBelance(qarza, insertStatus, deleteStatus, updateStatus, UserID);

    }
}