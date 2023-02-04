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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sell_On_Customer extends Activity {
    String selectedname = "";
    TextView tvtotalqarza, tvtotalqarzaText;
    TextView tvorder, tvtotal;
    Button btnsellnewitem, btnsubmit;
    String a;
    String itemName;
    String tempitemid = "";
    String userid;
    CheckBox sendsms;
    Dialog dialog;
    Cursor itemcorsore, cursortransectio,supcorsore;
    Boolean add = false;
    TextView edtpaid;
    int totalsell = 0, totalbuy = 0;
    int NumberOfProducts = 1, Total = 0, itemOrderno = 1;
    String GetSQliteQuery, GetSQLiteQueryItems;
    int ItemsRemaining;
    ArrayList<String> itemid = new ArrayList<String>();
    ArrayList<String> NumberOItem = new ArrayList<String>();
//    Button btnpaid;
    Button btnRecDetail;
    TextView tvbaqaya;
    String lastdate,lasttotalbuy;
    int counter = 1;
    String cusOrderdetail = "";
    EditText etUsername;
    TextView tvpaidtext;
    String selectednameId = "";
    String oldamonut = "";
    String oldprofit="";
    int profit=0;
    int Delivery_Charges=0;
    String delivery_Charges="";
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sell__on__customer);
        Intent i = getIntent();
        selectedname = i.getStringExtra("name");
        tvtotalqarza = (TextView) findViewById(R.id.totalqarza);
        tvtotalqarzaText = (TextView) findViewById(R.id.totalqarzatext);
        edtpaid = (TextView) findViewById(R.id.edtpaid);
        tvbaqaya = (TextView) findViewById(R.id.tvBaqaya);
        btnsellnewitem = (Button) findViewById(R.id.btnsellitem);
        tvtotal = (TextView) findViewById(R.id.tvtotalbill);
        tvorder = (TextView) findViewById(R.id.tvorder);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
//        btnpaid = (Button) findViewById(R.id.btnpaid);
        btnRecDetail = (Button) findViewById(R.id.btnrecDetail);
        tvpaidtext = (TextView) findViewById(R.id.tvPaidText);

        db = new DatabaseHelper(this);
        //Check input user name
        if (selectedname.equals("کیش : کسٹمر")) {
            tvtotalqarzaText.setText("");
            tvtotalqarza.setText("" + selectedname);
            View v = findViewById(R.id.lcheck);
            v.setVisibility(View.INVISIBLE);
        }
        //Load Puran Qarza
        if ((selectedname != "") || selectedname != "کیش : کسٹمر") {
            sendsms = (CheckBox) findViewById(R.id.checkbox3);
            sendsms.setChecked(true);
            showtotalPuranaQarza(selectedname);
        }
        btnRecDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Customer_Record_Detail.class);
                if (selectedname.equals("کیش : کسٹمر")) {
                    i.putExtra("name", selectedname);
                } else {
                    String getnewSN = getNewSelectedname(userid);
                    i.putExtra("name", getnewSN);
                }
                i.putExtra("inputcatagory", "Customer");
                String userphone = getNewSelectedphone(userid);
                i.putExtra("userID", userphone);
                //String newSN=getNewSelectedname(userid);
                i.putExtra("cashCus", selectedname);

                startActivity(i);
            }
        });
        supcorsore=db.getSupplierNames ();
//        Button btnloan = (Button) findViewById(R.id.btnLoan);
//        btnloan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (tvpaidtext.getText().toString().equals("")) {
//                    LayoutInflater inflater = getLayoutInflater();
//                      View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
//                     final EditText etUsername = alertLayout.findViewById(R.id.et_username);
//                      final EditText etEmail = alertLayout.findViewById(R.id.et_email);
//
//                    AlertDialog.Builder alert = new AlertDialog.Builder(Sell_On_Customer.this);
//                    alert.setTitle("Enter the Cash Loan Rs");
//                      alert.setIcon(R.drawable.images);
//                    // this is set the view from XML inside AlertDialog
//                      alert.setView(alertLayout);
//                    // disallow cancel of AlertDialog on click of back button and outside touch
//                    alert.setCancelable(false);
//                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                    alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            String user = etUsername.getText().toString();
//                            String pass = etEmail.getText().toString();
//                            if (pass.equals("")) {
//                                Toast.makeText(getApplicationContext(), "All the fields must be filled", Toast.LENGTH_LONG).show();
//                           } else {
//                                tvbaqaya.setText("" + (Total + Integer.parseInt(pass)));
////                                Total=Total+Integer.parseInt(pass);
//                                tvtotal.setText("ٹوٹل بل :" + (Total + Integer.parseInt(pass)));
//                                Total = Total + Integer.parseInt(pass);
//                                TextView tvtext = (TextView) findViewById(R.id.tvBaqayaText);
//                                tvtext.setText("بقایا :");
//
//                                //edtpaid.setText(pass);
//                                cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + ", " + " قرض :" + user + " : " + pass + "";
//                                add = true;
//                                if (user.equals("")) {
//                                    tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + " قرض :" + " : " + pass);
//                                    tvpaidtext.setText(" قرض :");
//                                } else if (user != ("")) {
//                                    tvpaidtext.setText(" قرض :");
//                                    tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + " قرض :" + user + " : " + pass);
//                                } else {
//
//                                }
//
//                                itemOrderno = itemOrderno + 1;
//                            }
//                        }
//                    });
//                    AlertDialog dialog = alert.create();
//                    dialog.show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "First Submit the Record", Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });
//        btnpaid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (tvpaidtext.getText().toString().equals("")) {
//                    LayoutInflater inflater = getLayoutInflater();
//                    View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
//                    final EditText etUsername = alertLayout.findViewById(R.id.et_username);
//                    final EditText etEmail = alertLayout.findViewById(R.id.et_email);
//
//                    AlertDialog.Builder alert = new AlertDialog.Builder(Sell_On_Customer.this);
//                    alert.setTitle("Enter the Cash Paid Rs");
//                    alert.setIcon(R.drawable.images);
//                    // this is set the view from XML inside AlertDialog
//                    alert.setView(alertLayout);
//                    // disallow cancel of AlertDialog on click of back button and outside touch
//                    alert.setCancelable(false);
//                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                    alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            String user = etUsername.getText().toString();
//                            String pass = etEmail.getText().toString();
//                            if (pass.equals("")) {
//                                Toast.makeText(getApplicationContext(), "All the fields must be filled", Toast.LENGTH_LONG).show();
//                            } else {
//                                tvbaqaya.setText("" + (Total - Integer.parseInt(pass)));
//
//                                tvtotal.setText("ٹوٹل بل :" + Total);
//                                TextView tvtext = (TextView) findViewById(R.id.tvBaqayaText);
//                                tvtext.setText("بقایا :");
//
//                                edtpaid.setText(pass);
//                                add = true;
//                                if (tvorder.getText().toString().equals("")) {
//                                    if(user.equals("")) {
//                                        tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + "وصول:" + " : " + pass);
//                                        cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + "وصول:" + pass + "";
//                                        tvpaidtext.setText("وصول:");
//                                    }
//                                    else {
//                                        tvpaidtext.setText("وصول:");
//                                        cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " +  user + pass + "";
//                                        tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + user + " : " + pass);
//                                    }
//                                } else {
//                                    if(user.equals("")) {
//                                        tvpaidtext.setText("وصول:");
//                                    }
//                                    else {
//                                        tvpaidtext.setText("وصول:");
//                                        cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " +  user + pass + "";
//                                        tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + user + " : " + pass);
//                                    }
//
//                                }
//
//                                itemOrderno = itemOrderno + 1;
//                            }
//                        }
//                    });
//                    AlertDialog dialog = alert.create();
//                    dialog.show();
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "First Submit the record", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        // GetSQliteQuery = "SELECT * FROM CustomerTable" ;

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add == true) {

                    Cursor cursor = db.getCustomerNames();

                    if (cursor.moveToFirst()) {
                        do {
                            String a = (cursor.getString(0).toString());

                            String b = (cursor.getString(1).toString());

                            String c = (cursor.getString(2).toString());

                            String d = (cursor.getString(3).toString());
                            String e = (cursor.getString(4).toString());
                            //Toast.makeText(this, ""+selectedname, Toast.LENGTH_SHORT).show();
                            if ((b + " : " + d+"\n"+e).equals(selectedname)) {
                                // Toast.makeText(getApplicationContext(),b+" : "+d+". . ."+selectedname,Toast.LENGTH_SHORT).show();
                                updateStock();
                                delivery_Charges=""+Delivery_Charges;
                                int newtotal = SAveCustomerSellDetail(a, b, c, d,Delivery_Charges , "New Orders" );
                                    SendSMSandsaveLastOrder(b, c, d, newtotal);


                            }


                            counter++;
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    if (selectedname.equals("کیش : کسٹمر")) {
                        updateStock();
                        //CashSAveCustomerSellDetail();
                    } else {
                        TransectionDetail();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Add Atleast One Item First", Toast.LENGTH_LONG).show();
                }
            }

        });


        btnsellnewitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCatagory ( v );
            }
        });

    }

    //Transection save method
    private void TransectionDetail() {
        int newTotalsell = totalsell + Integer.parseInt(tvbaqaya.getText().toString());
        final int Balance = newTotalsell - totalbuy;

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        final String thisDate = currentDate.format(todayDate);
        //Toast.makeText(getApplicationContext(),"Sell : "+billid+" : "+thisDate+" : "+tvbaqaya.getText().toString()+" : "+Belence,Toast.LENGTH_LONG).show();
        Boolean last=false;
        try {
            last = CheckLastTransectionDate(thisDate);
        }
        catch (Exception e)
        {
            last=false;
        }
        if (last == false) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.TRANSECTION_INSERT_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    //updating the status in sqlite
                                    saveTransectionToLocalStorage("sell","0",thisDate,tvbaqaya.getText().toString(),""+Balance,""+profit,0,0,0);

                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                }else {
                                    saveTransectionToLocalStorage("sell","0",thisDate,tvbaqaya.getText().toString(),""+Balance,""+profit,1,0,0);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            saveTransectionToLocalStorage("sell","0",thisDate,tvbaqaya.getText().toString(),""+Balance,""+profit,1,0,0);


                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("TypeOT", "sell");
                    params.put("Billid", "0");
                    params.put("Date", thisDate);
                    params.put("Amount", tvbaqaya.getText().toString());
                    params.put("Balance", ""+Balance);
                    params.put("Profit", ""+profit);

                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } else {
            final int UserID = Integer.parseInt(selectednameId);
            //Toast.makeText(getApplicationContext(), "old a" + oldamonut, Toast.LENGTH_LONG).show();
           final String amount = "" + (Integer.parseInt(oldamonut) + Integer.parseInt(tvbaqaya.getText().toString()));
           final String newProfit=""+(Integer.parseInt(oldprofit)+profit);
            Toast.makeText(getApplicationContext(),""+newProfit, Toast.LENGTH_LONG).show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.SELLING_TRANSECTION_PART_UPDATE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {

                                    updateTransection(amount,""+Balance,newProfit,0,UserID);
                                  //  Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                                }else {
                                    updateTransection(amount,""+Balance,newProfit,1,UserID);
                                  //  Toast.makeText(getApplicationContext(), "Errrorrrr 22222", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            updateTransection(amount,""+Balance,newProfit,1,UserID);
                           // Toast.makeText(getApplicationContext(), "Errrorrrr 3", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("Amount", amount);
                    params.put("Balance", ""+Balance);
                    params.put("Profit", newProfit);
                    params.put("Date", ""+lastdate);
                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            //Toast.makeText(getApplicationContext(),"Customer Record is Updated", Toast.LENGTH_LONG).show();
        }

    }

    private Boolean CheckLastTransectionDate(String thisDate) {
        Boolean last = false;
//        try {

        cursortransectio = db.getTransectionData();

        cursortransectio.moveToLast();
        {
             lastdate = (cursortransectio.getString(3).toString());
            lasttotalbuy = (cursortransectio.getString(2).toString());
            oldamonut="0";
            if (lastdate.equals(thisDate)) {
                last = true;
                selectednameId = cursortransectio.getString(0).toString();
                oldprofit=cursortransectio.getString(6).toString();
                oldamonut = cursortransectio.getString(4).toString();
                //Toast.makeText(getApplicationContext(), "old a" + cursortransectio.getString(0).toString()+":"+cursortransectio.getString(1).toString()+":"+cursortransectio.getString(2).toString()+":"+cursortransectio.getString(3).toString()+":"+cursortransectio.getString(4).toString(), Toast.LENGTH_LONG).show();

            }

        }
        cursortransectio.close();

//        } catch (Exception e) {

//            last = false;
//        }

        return last;
    }


    //SMS methode
    private void SendSMSandsaveLastOrder(String b, String c, String d, int newtotal) {
try {

    String pass = "جناب :" + b + "\n" + "اپکاپرانہ کھاتہ :" + d;
    if (Total == 0) {
        pass = pass + cusOrderdetail;
        pass = pass + "\n" + "وصول :" + edtpaid.getText().toString();
    } else {
        //paid detail
        pass = pass + "\n" + "نیاارڈر :" + cusOrderdetail;
        pass = pass + "\n" + tvtotal.getText().toString();
        if (edtpaid.getText().toString().equals("")) {
        } else {

            pass = pass + "\n" + "وصول :" + edtpaid.getText().toString()
                    + "\n" + "بقایا :" + (Total - Integer.parseInt(edtpaid.getText().toString()));
        }
    }
    pass = pass + "\n" + "میزان :" + newtotal + "\n" + getResources().getString(R.string.app_name);
    //Toast.makeText(getApplicationContext(),""+pass,Toast.LENGTH_LONG).show();
    SmsManager smsManager = SmsManager.getDefault();
    if (sendsms.isChecked()) {
        ArrayList<String> parts = smsManager.divideMessage ( pass );
        smsManager.sendMultipartTextMessage ( c , null , parts , null , null );
    }
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    SharedPreferences.Editor spedit = sp.edit();
    spedit.putString("lastOrder", pass);
    spedit.commit();
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

    private void updateStock() {


        for (int i = 0; i < itemid.size(); i++) {
            //Toast.makeText(getApplicationContext(),""+itemid.get(i),Toast.LENGTH_SHORT).show();
            int UserID = Integer.parseInt(itemid.get(i));

            updateItemsForT(NumberOItem.get(i),0,0,1,UserID);

        }
    }

    //save detail method
    private int SAveCustomerSellDetail(final String a , final String b , final String c , final String d , final int delivery_Charges , final String os) {

        //save data to Sell detail table
        final String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        final int balance = Integer.parseInt(d) + Integer.parseInt(tvbaqaya.getText().toString());

//
//        if (tvpaidtext.getText().toString().equals("وصول:")) {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.SELL_INSERT_URL,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            String orderStatus="sended";
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                if (!jsonObject.getBoolean("error")) {
//
//                                    saveSellToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,"P"+edtpaid.getText().toString(),""+balance,""+delivery_Charges,orderStatus,0,0,0);
//
//                                    //Toast.makeText(getApplicationContext(), "here", Toast.LENGTH_SHORT).show();
//
//                                }else {
//                                    saveSellToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,"P"+edtpaid.getText().toString(),""+balance, ""+delivery_Charges , orderStatus , 1,0,0);
//
//
//                                }
//                            }catch (JSONException e){
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    String orderStatus="sended";
//
//                    saveSellToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,"P"+edtpaid.getText().toString(),""+balance, ""+delivery_Charges , orderStatus , 1,0,0);
//                }
//            })
//            {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String,String> params = new HashMap<String, String>();
//                    params.put("Cusid", c);
//                    params.put("Date", mydate);
//                    params.put("Des", tvorder.getText().toString());
//                    params.put("PrevT", d);
//                    params.put("NewT", ""+Total);
//                    params.put("Paid", "P"+edtpaid.getText().toString());
//                    params.put("Balance",""+balance);
//                    params.put("Dcharges",""+delivery_Charges);
//                    params.put("Ostatus","sended");
//
//                    return params;
//                }
//            };
//            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
//
//        }
//        else if (tvpaidtext.getText().toString().equals(" قرض :")) {
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.SELL_INSERT_URL,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                                            if (!jsonObject.getBoolean("error")) {
//                                    saveSellToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,"L"+edtpaid.getText().toString(),""+balance, ""+delivery_Charges , "sended" , 0,0,0);
//
//
//                                }else {
//                                    saveSellToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,"L"+edtpaid.getText().toString(),""+balance, ""+delivery_Charges , "sended" , 1,0,0);
//
//
//                                }
//                            }catch (JSONException e){
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//
//                    saveSellToLocalStorage(a,mydate,tvorder.getText().toString(),d,""+Total,"L"+edtpaid.getText().toString(),""+balance, ""+delivery_Charges , "sended" , 1,0,0);
//
//
//
//                }
//            })
//            {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String,String> params = new HashMap<String, String>();
//                    params.put("Cusid", c);
//                    params.put("Date", mydate);
//                    params.put("Des", tvorder.getText().toString());
//                    params.put("PrevT", d);
//                    params.put("NewT", ""+Total);
//                    params.put("Paid", "L"+edtpaid.getText().toString());
//                    params.put("Balance",""+balance);
//                    params.put("Dcharges",""+""+delivery_Charges);
//                    params.put("Ostatus","sended");
//
//                    return params;
//                }
//            };
//            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
//
//        }
//        else
            if (tvpaidtext.getText().toString().equals("")) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.SELL_INSERT_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {


                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    Toast.makeText ( Sell_On_Customer.this , "saved successfully"+delivery_Charges , Toast.LENGTH_SHORT ).show ( );
                                    saveSellToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,edtpaid.getText().toString(),
                                            ""+balance, ""+delivery_Charges , "New Orders" , 0,0,0);


                                }else {
                                    Toast.makeText ( Sell_On_Customer.this , "error" , Toast.LENGTH_SHORT ).show ( );
                                    saveSellToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,edtpaid.getText().toString(),
                                            ""+balance, ""+delivery_Charges , "New Orders" , 1,0,0);


                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                                Toast.makeText ( Sell_On_Customer.this , "app stopped" , Toast.LENGTH_SHORT ).show ( );
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                 Toast.makeText ( Sell_On_Customer.this , "volly error" , Toast.LENGTH_SHORT ).show ( );
                    saveSellToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,edtpaid.getText().toString(),""+balance,
                            ""+delivery_Charges , "New Orders" , 1,0,0);

                }
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Cusid", c);
                    params.put("Date", mydate);
                    params.put("Des", tvorder.getText().toString());
                    params.put("PrevT", d);
                    params.put("NewT", ""+Total);
                    params.put("Paid", edtpaid.getText().toString());
                    params.put("Balance",""+balance);
                    params.put("Dcharges",""+delivery_Charges);
                    params.put("Ostatus",os);
                //   Toast.makeText ( Sell_On_Customer.this , "control is here" , Toast.LENGTH_SHORT ).show ( );
                    return params;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        }

        int UserID = Integer.parseInt(a);
        updateCurrent(b,c,""+balance,0,0,1,UserID);
        Toast.makeText(getApplicationContext(),"Customer Record is Updated", Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), Sell_On_Customer.class);
        String newSN = getNewSelectedname(userid);
        i.putExtra("name", newSN);
        startActivity(i);

        return balance;
    }

    private String getNewSelectedname(String a) {
        String newSN = "";
        try {

        itemcorsore = db.getCustomerNames();

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
    private String getNewSelectedphone(String a) {
        String newSN = "";
        try {

            itemcorsore = db.getCustomerNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }
        if (itemcorsore.moveToFirst()) {
            do {
                String id = (itemcorsore.getString(0).toString());
                String d = (itemcorsore.getString(2).toString());
                String c = (itemcorsore.getString(3).toString());
                // Toast.makeText(getApplicationContext(),""+a+"//"+id,Toast.LENGTH_LONG).show();
                if (a.equals(id)) {
                    newSN = d;
                    //Toast.makeText(getApplicationContext(),""+newSN+"//"+userid,Toast.LENGTH_LONG).show();
                }

            }
            while (itemcorsore.moveToNext());

        }
        return newSN;
    }

    private String showDialoge(final View view , String a) {
        final String cat=a;
        try {

            itemcorsore = db.getItemNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }

        // custom dialog
        dialog = new Dialog(Sell_On_Customer.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        List<String> stringList = new ArrayList<>();
        List<String> myString2 = new ArrayList<>();  // here is list
        int count = 0;
        int z = 0;
        boolean found = false;
      //  stringList.add("دیگر:اشیاء");
        if (itemcorsore.moveToFirst()) {
            do {

                String b = (itemcorsore.getString(1).toString());
                String c = (itemcorsore.getString(2).toString());
//                Toast.makeText ( this , a+":"+itemcorsore.getString(0).toString()+itemcorsore.getString(1).toString()
//                        +"\n"+itemcorsore.getString(3).toString()+itemcorsore.getString(4).toString()
//                        +"\n"+itemcorsore.getString(5).toString()+itemcorsore.getString(6).toString()
//                        +"\n"+itemcorsore.getString(7).toString()+itemcorsore.getString(8).toString()
//                        +"\n"+itemcorsore.getString(9).toString()+itemcorsore.getString(10).toString(), Toast.LENGTH_SHORT ).show ( );
if(itemcorsore.getString(6).toString().equals ( a ))
{
    myString2.add ( b + ":" + c);
}
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
            RadioButton rb = new RadioButton(Sell_On_Customer.this); // dynamically creating RadioButton and adding to RadioGroup.
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
                        Sell_On_Customer.this.a = btn.getText().toString();
                        if (Sell_On_Customer.this.a.equals("دیگر:اشیاء")) {
                            if (tvorder.getText().toString() != ("وصول:")) {
                                OtherItemDialoge();
                            } else {
                                Toast.makeText(getApplicationContext(), "First Submit the Record", Toast.LENGTH_LONG).show();
                            }
                            dialog.dismiss();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setTitle("How many: " + Sell_On_Customer.this.a + "?");

// Set up the input
                            final EditText input = new EditText(view.getContext());
                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                            input.setText("1");
                            builder.setView(input);
// Set up the buttons
                            builder.setPositiveButton("Finished", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String strNOI = input.getText().toString();
                                    if (Integer.parseInt(strNOI) <= Integer.parseInt(getNumber( Sell_On_Customer.this.a ))) {
                                        NumberOfProducts = Integer.parseInt(strNOI);
                                        tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + ", " + Sell_On_Customer.this.a + " :" + NumberOfProducts + "*" + getPrice( Sell_On_Customer.this.a ) + " = " + NumberOfProducts * (Integer.parseInt(getPrice( Sell_On_Customer.this.a ))));
                                        cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + ", " + Sell_On_Customer.this.a + "     : " + NumberOfProducts * (Integer.parseInt(getPrice( Sell_On_Customer.this.a )));
                                        itemOrderno = itemOrderno + 1;
                                        int remainingitem = Integer.parseInt(getNumber( Sell_On_Customer.this.a )) - NumberOfProducts;
                                        TempereryList("" + getID( Sell_On_Customer.this.a ), "" + remainingitem);
                                        //Toast.makeText(getApplicationContext(),""+getPrice(),Toast.LENGTH_SHORT).show();
                                        Total = Total + (NumberOfProducts * (Integer.parseInt(getPrice( Sell_On_Customer.this.a ))));
                                        Delivery_Charges=Delivery_Charges+(NumberOfProducts * (Integer.parseInt(getDcharges( Sell_On_Customer.this.a ))));;
                                       ///////Delivery charges test
                                        Toast.makeText ( Sell_On_Customer.this , ""+Delivery_Charges , Toast.LENGTH_SHORT ).show ( );
                                        tvtotal.setText("ٹوٹل بل :" + Total);
                                        TextView tvtext = (TextView) findViewById(R.id.tvBaqayaText);
                                        tvtext.setText("بقایا :");
                                        add = true;
                                        profit=profit+(NumberOfProducts*getProfit( Sell_On_Customer.this.a ));
                                    } else {
                                        // setup the alert builder
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Sell_On_Customer.this);
                                        builder.setTitle("Oops!");
                                        builder.setIcon(R.drawable.errorimg);
                                        builder.setMessage(strNOI + " " + Sell_On_Customer.this.a + " is not present with you only " + getNumber( Sell_On_Customer.this.a ) + " items remaining");

                                        // add a button
                                        builder.setPositiveButton("OK", null);

                                        // create and show the alert dialog
                                        AlertDialog dialog2 = builder.create();
                                        dialog2.show();
                                        //Toast.makeText(getApplicationContext(),strNOI+" "+a+" is not present with you only "+getNumber(a)+" items remaining",Toast.LENGTH_SHORT).show();
                                    }
                                    TextView tvbaqaya;
                                    tvbaqaya = (TextView) findViewById(R.id.tvBaqaya);
                                    tvbaqaya.setText("" + (Total));

                                }
                            });
                            builder.setNegativeButton("AddNew", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface abc, int which) {
                                    String strNOI = input.getText().toString();
                                    if (Integer.parseInt(strNOI) <= Integer.parseInt(getNumber( Sell_On_Customer.this.a ))) {

                                        NumberOfProducts = Integer.parseInt(strNOI);
                                        tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + ", " + Sell_On_Customer.this.a + " :" + NumberOfProducts + "*" + getPrice( Sell_On_Customer.this.a ) + " = " + NumberOfProducts * (Integer.parseInt(getPrice( Sell_On_Customer.this.a ))));
                                        cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + ", " + Sell_On_Customer.this.a + "        : " + NumberOfProducts * (Integer.parseInt(getPrice( Sell_On_Customer.this.a )));
                                        itemOrderno = itemOrderno + 1;

                                        Total = Total + NumberOfProducts * (Integer.parseInt(getPrice( Sell_On_Customer.this.a )));
                                        Delivery_Charges=Delivery_Charges+(NumberOfProducts * (Integer.parseInt(getDcharges( Sell_On_Customer.this.a ))));;
                                        int remainingitem = Integer.parseInt(getNumber( Sell_On_Customer.this.a )) - NumberOfProducts;
                                        TempereryList("" + getID( Sell_On_Customer.this.a ), "" + remainingitem);
                                        // Toast.makeText(getApplicationContext(),""+getID(a),Toast.LENGTH_SHORT).show();
                                        tvtotal.setText("ٹوٹل بل :" + Total);
                                        add = true;
                                        TextView tvtext = (TextView) findViewById(R.id.tvBaqayaText);
                                        tvtext.setText("بقایا :");
                                        profit=profit+(NumberOfProducts*getProfit( Sell_On_Customer.this.a ));
                                        showDialoge(view , cat );

                                    } else {
                                        //Toast.makeText(getApplicationContext(),strNOI+" "+a+" is not present with you only "+getNumber(a)+" items remaining",Toast.LENGTH_SHORT).show();
                                        // setup the alert builder
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Sell_On_Customer.this);
                                        builder.setTitle("Oops!");
                                        builder.setIcon(R.drawable.errorimg);
                                        builder.setMessage(strNOI + " " + Sell_On_Customer.this.a + " is not present with you only " + getNumber( Sell_On_Customer.this.a ) + " items remaining");

                                        // add a button
                                        builder.setPositiveButton("OK", null);

                                        // create and show the alert dialog
                                        AlertDialog dialog2 = builder.create();
                                        dialog2.show();
                                    }
                                    TextView tvbaqaya;
                                    tvbaqaya = (TextView) findViewById(R.id.tvBaqaya);
                                    tvbaqaya.setText("" + (Total));
                                }
                            });

                            builder.show();

                            dialog.dismiss();
                        }
                    }
                }
            }

            private String getDcharges(String a) {
                String price = "";
                try {

                    itemcorsore = db.getItemNames();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
                }
                if (itemcorsore.moveToFirst()) {
                    do {
                        if (a.equals((itemcorsore.getString(1).toString()) + ":" + itemcorsore.getString(2).toString())) {
                            String b = (itemcorsore.getString(7).toString());
                            tempitemid = itemcorsore.getString(1).toString();
                            price = b;
                        }

                    }
                    while (itemcorsore.moveToNext());

                }
                return price;
            }

        });
        return this.a;
    }

    private int getProfit(String a) {
        int prof=0;
        try {

         itemcorsore = db.getItemNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }
        if (itemcorsore.moveToFirst()) {
            do {
                if (a.equals((itemcorsore.getString(1).toString()) + ":" + itemcorsore.getString(2).toString())) {
                    String b = (itemcorsore.getString(4).toString());
                    String c = (itemcorsore.getString(5).toString());
                    tempitemid = itemcorsore.getString(1).toString();
                    prof=Integer.parseInt(b)-Integer.parseInt(c);
                }

            }
            while (itemcorsore.moveToNext());

        }


        return prof;
    }

    private void OtherItemDialoge() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        etUsername = alertLayout.findViewById(R.id.et_username);
        final EditText etEmail = alertLayout.findViewById(R.id.et_email);
        final Button btnadditem = alertLayout.findViewById(R.id.btnaddname);
        SpannableString content = new SpannableString("ADD Item Name From the list");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        btnadditem.setText(content);
        final TextView tvname = alertLayout.findViewById(R.id.tvnaam);
        final TextView tvqamat = alertLayout.findViewById(R.id.tvraqam);
        tvname.setText("تفصیل :");
        tvqamat.setText("رقم :");

        final AlertDialog.Builder alert = new AlertDialog.Builder(Sell_On_Customer.this);
        alert.setTitle("دیگر:اشیاء کا تفصیل اور قیمت درج کریں۔");
        alert.setIcon(R.drawable.images);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        btnadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowItemNames();
            }
        });
        alert.setNegativeButton("Addnew", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = etUsername.getText().toString();
                String pass = etEmail.getText().toString();
                if (pass.equals("") || user.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fill All the Fields", Toast.LENGTH_SHORT).show();
                } else {
                    Total = Total + Integer.parseInt(pass);
                    tvbaqaya.setText("" + (Total));

                    tvtotal.setText("ٹوٹل بل :" + Total);
                    TextView tvtext = (TextView) findViewById(R.id.tvBaqayaText);
                    tvtext.setText("بقایا :");
                    TextView tvpaidtext = (TextView) findViewById(R.id.tvPaidText);
                    //tvpaidtext.setText(" قرض :");
                    //edtpaid.setText(pass);
                    cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + ", " + user + " : " + pass + "";
                    add = true;
                    tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + user + " : " + pass);
                    itemOrderno = itemOrderno + 1;
                }
                OtherItemDialoge();
            }
        });

        alert.setPositiveButton("Finished", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = etUsername.getText().toString();
                String pass = etEmail.getText().toString();
                if (pass.equals("") || user.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fill All the Fields", Toast.LENGTH_SHORT).show();
                } else {
                    Total = Total + Integer.parseInt(pass);
                    tvbaqaya.setText("" + (Total));

                    tvtotal.setText("ٹوٹل بل :" + Total);
                    TextView tvtext = (TextView) findViewById(R.id.tvBaqayaText);
                    tvtext.setText("بقایا :");
                    TextView tvpaidtext = (TextView) findViewById(R.id.tvPaidText);
                    //tvpaidtext.setText(" قرض :");
                    //edtpaid.setText(pass);
                    cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + ", " + user + " : " + pass + "";
                    add = true;
                    tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + user + " : " + pass);
                    itemOrderno = itemOrderno + 1;
                }
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
     }

    private void ShowItemNames() {
        itemName = "";
        try {

          itemcorsore = db.getItemNames();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }

        // custom dialog
        dialog = new Dialog(Sell_On_Customer.this);
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
            RadioButton rb = new RadioButton(Sell_On_Customer.this); // dynamically creating RadioButton and adding to RadioGroup.
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
                        etUsername.setText(etUsername.getText().toString() + " " + btn.getText().toString());

                    }
                    dialog.dismiss();
                }
            }

        });
    }

    private String getID(String a) {
        String price = "";
        try {

         itemcorsore = db.getItemNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }
        if (itemcorsore.moveToFirst()) {
            do {
                String name = (itemcorsore.getString(1).toString());
                String des = "" + (itemcorsore.getString(2).toString());
                String strtable = name + ":" + des;
                //Toast.makeText(getApplicationContext(),a+"====="+strtable,Toast.LENGTH_LONG).show();
                if (a.equals(name + ":" + des)) {
                    String b = (itemcorsore.getString(0).toString());
                    price = b;

                }

            }
            while (itemcorsore.moveToNext());

        }
        return price;
    }

    private void TempereryList(String a, String strNOI) {
        itemid.add(a);//this adds an id to the list.
        NumberOItem.add(strNOI);//this adds an id to the list.

    }

    private String getNumber(String a) {
        String price = "";
        try {

            itemcorsore = db.getItemNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }
        if (itemcorsore.moveToFirst()) {
            do {
                if (a.equals((itemcorsore.getString(1).toString()) + ":" + (itemcorsore.getString(2).toString()))) {
                    String b = (itemcorsore.getString(4).toString());
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

           itemcorsore = db.getItemNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }
        if (itemcorsore.moveToFirst()) {
            do {
                if (a.equals((itemcorsore.getString(1).toString()) + ":" + itemcorsore.getString(2).toString())) {
                    String b = (itemcorsore.getString(4).toString());
                    tempitemid = itemcorsore.getString(1).toString();
                    price = b;
                }

            }
            while (itemcorsore.moveToNext());

        }
        return price;
    }

    private void showtotalPuranaQarza(String selectedname) {

       Cursor cursor = db.getCustomerNames();
        Cursor cursor2 = db.getSupplierNames();
        int z = 0;
        int count = 0;
        boolean found = false;
        int counter = 1;
        if (cursor.moveToFirst()) {
            do {
                String a = (cursor.getString(0).toString());

                String b = (cursor.getString(1).toString());

                String c = (cursor.getString(2).toString());

                String d = (cursor.getString(3).toString());
                String e = (cursor.getString(4).toString());
                //Toast.makeText(this, ""+selectedname, Toast.LENGTH_SHORT).show();
                if ((b + " : " + d+"\n"+e).equals(selectedname)) {
                    tvtotalqarza.setText(tvtotalqarzaText.getText().toString()+":"+d);
                    tvtotalqarzaText.setText("name: "+b + "\n"+"phone: "+c+"\n"+e);
                    userid = a;
                }
                counter++;
                totalsell = totalsell + Integer.parseInt(d);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //count belance;
        if (cursor2.moveToFirst()) {
            do {

                String d = (cursor2.getString(3).toString());
                totalbuy = totalbuy + Integer.parseInt(d);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), Customer_Record.class);
        startActivity(i);
    }

    private void updateTransection(String Tamount, String Tbalance,String Tprofit,int updateStatus,int UserID) {
        db.updateTransection(Tamount, Tbalance,Tprofit, updateStatus, UserID);

    }

    private void saveTransectionToLocalStorage(String typeOT, String billid,String date,String amount,String balance,String profit,int insertStatus,int deleteStatus,int updateStatus)
    {
        db.addTransection(typeOT, billid,date,amount,balance,profit,insertStatus,deleteStatus,updateStatus);
    }

    private void updateItemsForT(String numberOI,int insertStatus,int deleteStatus,int updateStatus,int UserID)
    {
        db.updateItemsFT(numberOI,insertStatus,deleteStatus,updateStatus,UserID);
    }

    private void saveSellToLocalStorage(String cusid , String date , String description , String prevTotal , String newTotal , String Paid , String Balance , String delivery_Cahges , String orderStatus , int insertStatus , int deleteStatus , int updateStatus)
    {
        db.addSell(cusid,date,description,prevTotal,newTotal,Paid,Balance,delivery_Cahges,orderStatus,insertStatus,deleteStatus,updateStatus);
    }

    private void updateCurrent(String cname, String phoneN, String qarza,int insertStatus,int deleteStatus,int updateStatus,int UserID) {
        db.UpdateCusFS(cname, phoneN,qarza, insertStatus, deleteStatus, updateStatus, UserID);

    }
    private String showCatagory(final View view) {
        try {

            supcorsore = db.getSupplierNames ( );

        } catch (Exception e) {
            Toast.makeText ( getApplicationContext ( ) , "Category list is empty" , Toast.LENGTH_LONG ).show ( );
        }

        // custom dialog
        dialog = new Dialog ( Sell_On_Customer.this );
        dialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
        dialog.setContentView ( R.layout.radiobutton_dialog );
        List<String> myString2 = new ArrayList<> ( );
        if (supcorsore.moveToFirst ( )) {
            do {
                String c = (supcorsore.getString ( 5 ).toString ( ));
                myString2.add ( c );
            }
            while (supcorsore.moveToNext ( ));

        }
        supcorsore.close ( );
        RadioGroup rg = (RadioGroup) dialog.findViewById ( R.id.radio_group );
       // rg.setBackgroundColor ( getResources().getColor( android.R.color.background_dark)) ;
        IgnoreCaseComparator icc = new IgnoreCaseComparator ( );

        java.util.Collections.sort ( myString2 , icc );
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
//                        tvcatagory.setText ( a );
//                        catagoryselection=true;
                        dialog.dismiss ();
                        showDialoge ( view, a);
                    }
                }

            }
        } );
        return null;
    }
}
