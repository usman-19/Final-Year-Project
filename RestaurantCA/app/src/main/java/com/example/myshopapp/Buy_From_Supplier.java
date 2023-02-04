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
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.app.AlertDialog;

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

public class Buy_From_Supplier extends Activity {
    String selectedname = "";
    SQLiteDatabase SQLITEDATABASE;
    TextView tvtotalqarza,tvtotalqarzaText;
    TextView tvorder, tvtotal;
    String userid;
    Button btnsellnewitem, btnsubmit;
    String a;
    String tempitemid = "";
    EditText etUsername;
    String myString;
    String lasttotalsell;
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
    CheckBox sendsms;
    String selectednameId="";
    String lastdate,lastprofit,lastbillid;
    String oldamonut="";
    private DatabaseHelper db;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_buy__from__supplier);
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
            sendsms=(CheckBox)findViewById(R.id.checkbox3);
            sendsms.setChecked(true);
            showtotalPuranaQarza(selectedname);
        }
        if (selectedname.equals("کیش : سپلائیر")) {
            tvtotalqarzaText.setText("");
            tvtotalqarza.setText("" + selectedname);
            View v = findViewById(R.id.lcheck);
            v.setVisibility(View.INVISIBLE);
        }
        Button btnorder=(Button)findViewById(R.id.btnorder);
        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Order_Place.class);
                i.putExtra("name",selectedname);
                startActivity(i);
            }
        });
        Button btnRecDetail = (Button) findViewById(R.id.btnrecDetail);
        btnRecDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Supplier_Record_Detail.class);

                if (selectedname.equals("کیش : سپلائیر")) {
                    i.putExtra("name", tvtotalqarza.getText().toString());
                } else {
                    String getnewSN = getNewSelectedname(userid);
                    i.putExtra("name", getnewSN);
                }
                i.putExtra("inputcatagory", "Supplier");
                String phone = getNewSelectedphone(userid);
                i.putExtra("userID", phone);
                i.putExtra("cashCus", selectedname);
                startActivity(i);
            }
        });
        Button btnloan = (Button) findViewById(R.id.btnLoan);
        btnloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvpaidtext.getText().toString().equals("")) {
                    OtherItemsDialoge();
                } else {
                    Toast.makeText(getApplicationContext(), "First Submit the Record", Toast.LENGTH_LONG).show();
                }
            }
        });
        Button btnpaid = (Button) findViewById(R.id.btnpaid);
        btnpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvpaidtext.getText().toString().equals("")) {
                    LayoutInflater inflater = getLayoutInflater();
                    View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
                    final EditText etUsername = alertLayout.findViewById(R.id.et_username);
                    final EditText etEmail = alertLayout.findViewById(R.id.et_email);

                    AlertDialog.Builder alert = new AlertDialog.Builder(Buy_From_Supplier.this);
                    alert.setTitle("Enter the Cash Loan Rs");
                    alert.setIcon(R.drawable.images);
               //      this is set the view from XML inside AlertDialog
                   alert.setView(alertLayout);
                //     disallow cancel of AlertDialog on click of back button and outside touch
                    alert.setCancelable(false);
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String user = etUsername.getText().toString();
                          String pass = etEmail.getText().toString();
                            if (pass.equals("")) {
                                Toast.makeText(getApplicationContext(), "All the fields must be filled", Toast.LENGTH_LONG).show();
                            } else {
                                tvbaqaya.setText("" + (Total - Integer.parseInt(pass)));

                                tvtotal.setText("ٹوٹل بل :" + Total);
                                TextView tvtext = (TextView) findViewById(R.id.tvBaqayaText);
                                tvtext.setText("بقایا :");

                                edtpaid.setText(pass);
                                add = true;
                                if (tvorder.getText().toString().equals("") ) {
                                    if (user.equals(""))
                                    {
                                        tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + "ادائیگی:" + " : " + pass);
                                        tvpaidtext.setText("ادائیگی:");
                                        cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + "ادائیگی:" + user + " : " + pass + "";
                                    }
                                    else {
                                        tvpaidtext.setText("ادائیگی:");
                                        cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") "  + user + " : " + pass + "";
                                        tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") "  + user + " : " + pass);
                                    }

                                }
                                else {
                                    if (user.equals(""))
                                    {
                                        tvpaidtext.setText("ادائیگی:");
                                    }
                                    else
                                    {
                                        tvpaidtext.setText("ادائیگی:");
                                        cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") "  + user + " : " + pass + "";
                                        tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") "  + user + " : " + pass);
                                    }

                                }

                                itemOrderno = itemOrderno + 1;
                            }
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();

                } else {
                    Toast.makeText(getApplicationContext(), "First Submit the record", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add == true) {

                    Cursor cursor = db.getSupplierNames();
                    if (cursor.moveToFirst()) {
                        do {
                            String a = (cursor.getString(0).toString());

                            String b = (cursor.getString(1).toString());

                            String c = (cursor.getString(2).toString());

                            String d = (cursor.getString(3).toString());
                            if ((b + " : " + d).equals(selectedname)) {
                                updateStock();
                                int newtotal = SAveSupplierBuyDetail(a, b, c, d);
                                if(sendsms.isChecked())
                                {
                                    SendSMSandsaveLastOrder(b, c, d, newtotal);
                                }
                            }

                            counter++;
                        } while (cursor.moveToNext());
                    }
                    cursor.close();

                    if (selectedname.equals("کیش : سپلائیر")) {
                        updateStock();
                        CashSAveSupplierBuyDetail();
                    } else {
                        saveTransectionDetail();
                    }
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

    //Transection save method
    private void saveTransectionDetail() {
        int newTotalbuy = totalbuy + Integer.parseInt(tvbaqaya.getText().toString());
        final int Balance = totalsell - newTotalbuy;

        String billid = "";
        try {

            Cursor cursorid = db.getSellDetailData();

            cursorid.moveToLast();
            billid = cursorid.getString(0);
            cursorid.close();
        } catch (Exception e) {

        }

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
        if(last==false) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.TRANSECTION_INSERT_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    //updating the status in sqlite
                                    saveTransectionToLocalStorage("Buy",tvbaqaya.getText().toString(),thisDate,"0",""+Balance,"0",0,0,0);
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                }else {
                                    saveTransectionToLocalStorage("Buy",tvbaqaya.getText().toString(),thisDate,"0",""+Balance,"0",1,0,0);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                       saveTransectionToLocalStorage("Buy",tvbaqaya.getText().toString(),thisDate,"0",""+Balance,"0",1,0,0);


                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("TypeOT", "Buy");
                    params.put("Billid", tvbaqaya.getText().toString());
                    params.put("Date", thisDate);
                    params.put("Amount", "0");
                    params.put("Balance", ""+Balance);
                    params.put("Profit", "0");

                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


         }
        else
        {
            final int UserID = Integer.parseInt(selectednameId);
           final String amount=""+(Integer.parseInt(oldamonut)+Integer.parseInt(tvbaqaya.getText().toString()));

            StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.BUYING_TRANSECTION_PART_UPDATE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {

                                    updateTransection2(amount,""+Balance,0,UserID);

                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                }else {
                                    updateTransection2(amount,""+Balance,1,UserID);
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Buy_From_Supplier.this, ""+amount+";"+Balance+","+lastprofit+","+lastdate, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            updateTransection2(amount,""+Balance,1,UserID);


                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("Billid", amount);
                    params.put("Balance", ""+Balance);
                    params.put("Date", ""+lastdate);

                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
   }


    }
    private Boolean CheckLastTransectionDate(String thisDate) {
        Boolean last=false;
        try{

          cursortransectio = db.getTransectionData();

            cursortransectio.moveToLast(); {
                lastdate = (cursortransectio.getString(3).toString());
                lastprofit = (cursortransectio.getString(6).toString());
                lastbillid = (cursortransectio.getString(2).toString());
                lasttotalsell = (cursortransectio.getString(4).toString());
                if(lastdate.equals(thisDate))
                {
                    last=true;
                    selectednameId=cursortransectio.getString(0).toString();
                    oldamonut=cursortransectio.getString(2).toString();
                }

            }
            cursortransectio.close();

        } catch (Exception e) {

            last=false;
        }

        return  last;
    }

    private int SAveSupplierBuyDetail(final String a, final String b, final String c, final String d) {

        //save data to Sell detail table
        final String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        TextView tvbaqaya;
        tvbaqaya = (TextView) findViewById(R.id.tvBaqaya);
        final int balance = Integer.parseInt(d) + Integer.parseInt(tvbaqaya.getText().toString());

                if (tvpaidtext.getText().toString().equals("ادائیگی:")) {
                         StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.BUY_INSERT_URL,
                        new Response.Listener<String>() {
                             @Override
                               public void onResponse(String response) {

                                 try {
                                     JSONObject jsonObject = new JSONObject(response);
                                     if (!jsonObject.getBoolean("error")) {
                                         saveBuyToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,"P"+edtpaid.getText().toString(),""+balance,0,0,0);

//                                         Toast.makeText(getApplicationContext(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                     }else {

                                         saveBuyToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,"P"+edtpaid.getText().toString(),""+balance,1,0,0);
//                                         Toast.makeText(getApplicationContext(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                     }
                                 }catch (JSONException e){
                                     e.printStackTrace();
                                 }
                             }
                             }, new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError error) {

                                 saveBuyToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,"P"+edtpaid.getText().toString(),""+balance,1,0,0);
                             }
                         })
                         {
                             @Override
                             protected Map<String, String> getParams() {
                                 Map<String,String> params = new HashMap<String, String>();
                                 params.put("Supid", c);
                                 params.put("Date", mydate);
                                 params.put("Des", tvorder.getText().toString());
                                 params.put("PrevT", d);
                                 params.put("NewT", ""+Total);
                                 params.put("Paid", "P"+edtpaid.getText().toString());
                                 params.put("Balance", ""+balance);

                                 return params;
                             }
                         };
                         MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                } else if (tvpaidtext.getText().toString().equals(" قرض :")) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.BUY_INSERT_URL,
                            new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    saveBuyToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,"L"+edtpaid.getText().toString(),""+balance,0,0,0);

                                }else {
                                    saveBuyToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,"L"+edtpaid.getText().toString(),""+balance,1,0,0);

                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            saveBuyToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,"L"+edtpaid.getText().toString(),""+balance,1,0,0);

                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Supid", c);
                            params.put("Date", mydate);
                            params.put("Des", tvorder.getText().toString());
                            params.put("PrevT", d);
                            params.put("NewT", ""+Total);
                            params.put("Paid", "L"+edtpaid.getText().toString());
                            params.put("Balance", ""+balance);
                            return params;
                        }
                    };
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                } else if (tvpaidtext.getText().toString().equals("")){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.BUY_INSERT_URL,
                            new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    saveBuyToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,edtpaid.getText().toString(),""+balance,0,0,0);

                                }else {
                                    saveBuyToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,edtpaid.getText().toString(),""+balance,1,0,0);

                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            saveBuyToLocalStorage(c,mydate,tvorder.getText().toString(),d,""+Total,edtpaid.getText().toString(),""+balance,1,0,0);

                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Supid", c);
                            params.put("Date", mydate);
                            params.put("Des", tvorder.getText().toString());
                            params.put("PrevT", d);
                            params.put("NewT", ""+Total);
                            params.put("Paid", edtpaid.getText().toString());
                            params.put("Balance", ""+balance);
                            return params;
                        }
                    };
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                }

        int UserID = Integer.parseInt(a);

        updateCurrent(b, c, "" + balance,0,0,1,UserID);

        //make intent for reCall
        Intent i = new Intent(getApplicationContext(), Buy_From_Supplier.class);
        //Toast.makeText(getApplicationContext(),""+selectedname+"//"+userid,Toast.LENGTH_LONG).show();
        String newSN = getNewSelectedname(userid);
        i.putExtra("name", newSN);
        startActivity(i);

        return balance;
    }

    //save cash customer detail
    private void CashSAveSupplierBuyDetail() {

        //save data to Sell detail table
        final String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        TextView tvbaqaya;
        tvbaqaya = (TextView) findViewById(R.id.tvBaqaya);
        //int belance=Integer.parseInt(d)+Integer.parseInt(tvbaqaya.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.BUY_INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")) {
                                saveBuyToLocalStorage(selectedname,mydate,tvorder.getText().toString(),"0",""+Total,edtpaid.getText().toString(),"0",0,0,0);

                            }else {
                                saveBuyToLocalStorage(selectedname,mydate,tvorder.getText().toString(),"0",""+Total,edtpaid.getText().toString(),"0",1,0,0);

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                saveBuyToLocalStorage(selectedname,mydate,tvorder.getText().toString(),"0",""+Total,edtpaid.getText().toString(),"0",1,0,0);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Supid", selectedname);
                params.put("Date", mydate);
                params.put("Des", tvorder.getText().toString());
                params.put("PrevT", "0");
                params.put("NewT", ""+Total);
                params.put("Paid", edtpaid.getText().toString());
                params.put("Balance", "0");
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        //updateCurrent(a,b,c,""+belance);
        Intent i = new Intent(getApplicationContext(), Buy_From_Supplier.class);
        i.putExtra("name", selectedname);
        startActivity(i);


    }

    private void SendSMSandsaveLastOrder(String b, String c, String d, int newtotal) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String pass = "جناب :" + b + "\n" + "اپکاپرانہ کھاتہ :" + d;
            if (Total == 0) {
                pass = pass + cusOrderdetail;
                pass = pass + "\n" + "ادائیگی:" + edtpaid.getText().toString();
            } else {
                //paid detail
                pass = pass + "\n" + "نیاارڈر :" + cusOrderdetail;
                pass = pass + "\n" + tvtotal.getText().toString();
                if (edtpaid.getText().toString().equals("")) {
                } else {

                    pass = pass + "\n" + "ادائیگی:" + edtpaid.getText().toString()
                            + "\n" + "بقایا :" + (Total - Integer.parseInt(edtpaid.getText().toString()));
                }
            }
            pass = pass + "\n" + "میزان :" + newtotal + "\n" + getResources().getString(R.string.app_name);

            // Toast.makeText(getApplicationContext(),""+pass,Toast.LENGTH_LONG).show();
            ArrayList<String> parts = smsManager.divideMessage(pass);
            smsManager.sendMultipartTextMessage(c, null, parts, null, null);
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

    private void OtherItemsDialoge() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final EditText etUsername = alertLayout.findViewById(R.id.et_username);
        final EditText etEmail = alertLayout.findViewById(R.id.et_email);

        AlertDialog.Builder alert = new AlertDialog.Builder(Buy_From_Supplier.this);
        alert.setTitle("Enter the Cash Loan Rs");
        alert.setIcon(R.drawable.images);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

           alert.setPositiveButton("Finished", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = etUsername.getText().toString();
                String pass = etEmail.getText().toString();
                if (pass.equals("")) {
                    Toast.makeText(getApplicationContext(), "All the fields must be filled", Toast.LENGTH_LONG).show();
                } else {
                    tvbaqaya.setText("" + (Total + Integer.parseInt(pass)));
            //                    Total=Total+Integer.parseInt(pass);
                    tvtotal.setText("ٹوٹل بل :" + (Total + Integer.parseInt(pass)));
                    TextView tvtext = (TextView) findViewById(R.id.tvBaqayaText);
                    tvtext.setText("بقایا :");
                    Total = Total + Integer.parseInt(pass);
                    //edtpaid.setText(pass);
                    cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + ", " + " قرض :" + user + " : " + pass + "";
                    add = true;
                    if (user.equals("")) {
                        tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + " قرض :" + " : " + pass);
                        tvpaidtext.setText(" قرض :");
                    } else if (user != ("")) {
                        tvpaidtext.setText(" قرض :");
                        tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + " قرض :" + user + " : " + pass);
                    } else {

                    }

                    itemOrderno = itemOrderno + 1;
                }
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void updateStock(){

        for (int i = 0; i < itemid.size(); i++) {
            //Toast.makeText(getApplicationContext(),""+itemid.get(i),Toast.LENGTH_SHORT).show();
            int UserID = Integer.parseInt(itemid.get(i));

            updateItemsForS(NumberOItem.get(i),0,0,1,UserID);

          Cursor cursor = db.getSupplierNames();

            cursor.moveToPosition(UserID);
        }
    }


    private String showDialoge(final View view) {
        try {

         itemcorsore = db.getItemNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }

        // custom dialog
        dialog = new Dialog(Buy_From_Supplier.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        List<String> stringList = new ArrayList<>();  // here is list
        List<String> myString2 = new ArrayList<>();
        int count = 0;
        int z = 0;
        boolean found = false;
        stringList.add("دیگر:اشیاء");
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
            RadioButton rb = new RadioButton(Buy_From_Supplier.this); // dynamically creating RadioButton and adding to RadioGroup.
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
                          if (a.equals("دیگر:اشیاء")) {
                             if (tvorder.getText().toString() != ("وصول:")) {
                                  LayoutInflater inflater = getLayoutInflater();
                            View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
                            etUsername = alertLayout.findViewById(R.id.et_username);
                            final EditText etEmail = alertLayout.findViewById(R.id.et_email);
                            final TextView tvname = alertLayout.findViewById(R.id.tvnaam);
                            final TextView tvqamat = alertLayout.findViewById(R.id.tvraqam);
                            tvname.setText("تفصیل :");
                            tvqamat.setText("رقم :");
                            final Button btnadditem = alertLayout.findViewById(R.id.btnaddname);
                            SpannableString content = new SpannableString("ADD Item Name From the list");
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            btnadditem.setText(content);
                            btnadditem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ShowItemNames();
                                }
                            });
                            AlertDialog.Builder alert = new AlertDialog.Builder(Buy_From_Supplier.this);
                            alert.setTitle("دیگر:اشیاء کا تفصیل اور قیمت درج کریں۔");
                            alert.setIcon(R.drawable.images);
                            // this is set the view from XML inside AlertDialog
                            alert.setView(alertLayout);
//                                 disallow cancel of AlertDialog on click of back button and outside touch
                            alert.setCancelable(false);
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                                }
                            });

                                    alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                                        @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String user = etUsername.getText().toString();
                                    String pass = etEmail.getText().toString();
                                    if (pass.equals("")) {
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
                        } else {
                            Toast.makeText(getApplicationContext(), "First Submit the Record", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setTitle("How many: " + a + "?");

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
//                                if(Integer.parseInt(strNOI)<=Integer.parseInt(getNumber(a))) {
                                    NumberOfProducts = Integer.parseInt(strNOI);
                                    tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + ", " + a + " :" + NumberOfProducts + "*" + getPrice(a) + " = " + NumberOfProducts * (Integer.parseInt(getPrice(a))));
                                    cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + ", " + a + "     : " + NumberOfProducts * (Integer.parseInt(getPrice(a)));
                                    itemOrderno = itemOrderno + 1;
                                    int remainingitem = Integer.parseInt(getNumber(a)) + NumberOfProducts;
                                     TempereryList("" + getID(a), "" + remainingitem);
                                    //Toast.makeText(getApplicationContext(),""+getPrice(),Toast.LENGTH_SHORT).show();
                                       Total = Total + NumberOfProducts * (Integer.parseInt(getPrice(a)));
                                    tvtotal.setText("ٹوٹل بل :" + Total);
                                    add = true;
//                                }else {
//                                    // setup the alert builder
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(Buy_From_Supplier.this);
//                                    builder.setTitle("Oops!");
//                                    builder.setIcon(R.drawable.errorimg);
//                                    builder.setMessage(strNOI+" "+a+" is not present with you only "+getNumber(a)+" items remaining");
//
//                                    // add a button
//                                    builder.setPositiveButton("OK", null);
//
//                                    // create and show the alert dialog
//                                    AlertDialog dialog2 = builder.create();
//                                    dialog2.show();
//                                    //Toast.makeText(getApplicationContext(),strNOI+" "+a+" is not present with you only "+getNumber(a)+" items remaining",Toast.LENGTH_SHORT).show();
//                                }
                                    TextView tvbaqaya;
                                    tvbaqaya = (TextView) findViewById(R.id.tvBaqaya);
                                    tvbaqaya.setText("" + (Total));
                                }
                            });
                            builder.setNegativeButton("AddNew", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface abc, int which) {
                                    String strNOI = input.getText().toString();
                                    // if(Integer.parseInt(strNOI)<=Integer.parseInt(getNumber(a))) {

                                    NumberOfProducts = Integer.parseInt(strNOI);
                                    tvorder.setText(tvorder.getText().toString() + "\n" + itemOrderno + ") " + ", " + a + " :" + NumberOfProducts + "*" + getPrice(a) + " = " + NumberOfProducts * (Integer.parseInt(getPrice(a))));
                                    itemOrderno = itemOrderno + 1;
                                    cusOrderdetail = cusOrderdetail + "\n" + itemOrderno + ") " + ", " + a + "        : " + NumberOfProducts * (Integer.parseInt(getPrice(a)));
                                           Total = Total + NumberOfProducts * (Integer.parseInt(getPrice(a)));
                                     int remainingitem = Integer.parseInt(getNumber(a)) + NumberOfProducts;
                                      TempereryList("" + getID(a), "" + remainingitem);
                                    // Toast.makeText(getApplicationContext(),""+getID(a),Toast.LENGTH_SHORT).show();
                                    tvtotal.setText("ٹوٹل بل :" + Total);
                                    add = true;
                                    TextView tvbaqaya;
                                    tvbaqaya = (TextView) findViewById(R.id.tvBaqaya);
                                    tvbaqaya.setText("" + (Total));
                                    showDialoge(view);
//                                }
//                                else
//                                {
                                    //Toast.makeText(getApplicationContext(),strNOI+" "+a+" is not present with you only "+getNumber(a)+" items remaining",Toast.LENGTH_SHORT).show();
                                    // setup the alert builder
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(Buy_From_Supplier.this);
//                                    builder.setTitle("Oops!");
//                                    builder.setIcon(R.drawable.errorimg);
//                                    builder.setMessage(strNOI+" "+a+" is not present with you only "+getNumber(a)+" items remaining");
//
//                                    // add a button
//                                    builder.setPositiveButton("OK", null);
//
//                                    // create and show the alert dialog
//                                    AlertDialog dialog2 = builder.create();
//                                    dialog2.show();
//                                }
                                }
                            });

                            builder.show();

                            dialog.dismiss();
         }
                }
            }
          }

              });
        return a;
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

    private String getNewSelectedphone(String a) {
        String newSN = "";
        try {

            itemcorsore = db.getSupplierNames();

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
                    newSN =d;
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
                String e = (cursor.getString(4).toString());
                String f = (cursor.getString(5).toString());
                if ((b+" : "+d+" \n"+e+" : "+f).equals(selectedname)) {
                    tvtotalqarza.setText(tvtotalqarzaText.getText().toString()+":"+d);
                    tvtotalqarzaText.setText("name: "+b + "\n"+"phone: "+c
                            + "\n"+"Address: "+e
                            + "\n"+"catagory: "+f);
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

            itemcorsore = db.getItemNames();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Pleas Add Items to The Item List First", Toast.LENGTH_LONG).show();
        }

        // custom dialog
        dialog = new Dialog(Buy_From_Supplier.this);
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
            RadioButton rb = new RadioButton(Buy_From_Supplier.this); // dynamically creating RadioButton and adding to RadioGroup.
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

 private void saveTransectionToLocalStorage(String typeOT, String billid,String date,String amount,String balance,String profit,int insertStatus,int deleteStatus,int updateStatus)
    {
        db.addTransection(typeOT, billid,date,amount,balance,profit,insertStatus,deleteStatus,updateStatus);
    }

private void updateTransection2(String TBILLID, String Tbalance,int updateStatus,int UserID) {
        db.updateTransection2(TBILLID, Tbalance,updateStatus, UserID);
    }

private void saveBuyToLocalStorage(String supid, String date,String description ,String prevTotal,String newTotal,String Paid, String Balance,int insertStatus,int deleteStatus,int updateStatus)
    {
        db.addBuy(supid,date,description,prevTotal,newTotal,Paid,Balance,insertStatus,deleteStatus,updateStatus);
    }

    private void updateCurrent(String cname, String phoneN, String qarza,int insertStatus,int deleteStatus,int updateStatus,int UserID) {
        db.UpdateSuppFB(cname, phoneN,qarza, insertStatus, deleteStatus, updateStatus, UserID);

    }

    private void updateItemsForS(String numberOI,int insertStatus,int deleteStatus,int updateStatus,int UserID)
    {
        db.updateItemsFT(numberOI,insertStatus,deleteStatus,updateStatus,UserID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), Supplier_Record.class);
        startActivity(i);
    }
}
