package com.example.myshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
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

public class LogIn extends AppCompatActivity {
EditText edtpn,edtpass;
TextView tvfp;
DatabaseHelper db;
Button btnsginin,btnsginup;
SharedPreferences sp;
    ProgressBar simpleProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        edtpn=(EditText)findViewById(R.id.edtpn);
        edtpass=(EditText)findViewById(R.id.edtpass);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edtpn.setText(sp.getString("loginphone",""));
        edtpass.setText(sp.getString("loginpass",""));
        tvfp=(TextView)findViewById(R.id.tvfp);
        db = new DatabaseHelper(this);
        btnsginin=(Button)findViewById(R.id.buttonsginin);
        btnsginup=(Button)findViewById(R.id.buttonsginup);
        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        btnsginup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),add_update_customer_record.class);
                startActivity(i);
            }
        });
        btnsginin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtpn.getText().toString().equals(""))
                {
                  edtpn.setError("Enter Phone");
                }
                else if(edtpass.getText().toString().equals("")) {
                    edtpass.setError("enter password");
                }
                else {
                    GetDataFromServer(edtpn.getText().toString(),edtpass.getText().toString());
                    simpleProgressBar.setVisibility(View.VISIBLE);
                }
                SharedPreferences.Editor spEdit = sp.edit();
                spEdit.putString("loginphone", edtpn.getText().toString());
                spEdit.putString("loginpass", edtpass.getText().toString());
                spEdit.commit();
            }
        });
    }
    private void GetDataFromServer(final String s, final String pass) {

        final String Phone = "phone";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.CUSTOMER_SERVER_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{ boolean found=false;
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++)
                            {

                                JSONObject obj = array.getJSONObject(i);
if(obj.getString("phone").equals(s) )
{
    if (obj.getString("pass").equals(pass)) {
        Intent intent = new Intent(getApplicationContext(), Sell_On_Customer.class);
        intent.putExtra("name", obj.getString("phone"));
        intent.putExtra("customer", "name:"+obj.getString("name")+"\nphone: "+obj.getString("phone")+"\naddress: "+obj.getString("address"));
        GetDataFromServer();
        simpleProgressBar.setVisibility(View.INVISIBLE);
        startActivity(intent);
        found = true;
    }
}

                            }
                            if(found==false)
                            {
                                Toast.makeText(LogIn.this, "Incorrect user name or password", Toast.LENGTH_SHORT).show();
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
    private void GetDataFromServer() {

        final String Phone = "phone";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.CUSTOMER_SERVER_DATA_URL,
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

                               // Toast.makeText(getApplicationContext(), ""+obj.getString("name"), Toast.LENGTH_SHORT).show();

                                //saveCusToLocalStorage(obj.getString("name"),obj.getString("phone"),obj.getString("qarza"),obj.getString("address"),0,0,0);

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
    private void saveCusToLocalStorage(String Cname , String Cphone , String Cqarza , String Caddress , int insertStatus , int deleteStatus , int updateStatus)
    {
        db.addCustomer(Cname, Cphone,Cqarza,Caddress , insertStatus,deleteStatus,updateStatus);
    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();




        AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
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
}