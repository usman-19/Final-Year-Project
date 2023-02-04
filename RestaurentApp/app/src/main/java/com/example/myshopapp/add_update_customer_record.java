package com.example.myshopapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class add_update_customer_record extends Activity {
    EditText edtname,edtphone,edtqarza,edtaddress;
    Button btnsave;
    String selectedname="";
    String selectednameId;
    private DatabaseHelper db;
    Dialog dialog;
    private static final int INSERT_SYNCED = 0;
    private static final int INSERT_NOT_SYNCED= 1;
    private static final int UPDATE_NOT_SYNCED= 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__update__customer__rec);
        edtname = (EditText) findViewById(R.id.edtname);
        edtphone = (EditText) findViewById(R.id.edtdescription);
       edtphone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});
        edtqarza = (EditText) findViewById(R.id.edtItemNo);
        edtaddress = (EditText) findViewById(R.id.edtaddress);
        btnsave=(Button)findViewById(R.id.btnsave);
        Intent i=getIntent();

        db = new DatabaseHelper(this);

        selectedname=i.getStringExtra("name");
        if(selectedname!="")
        {
            showTheRec(selectedname);
        }

        final TextView textView = (TextView) findViewById(R.id.tvphonecontacts);
        SpannableString content = new SpannableString("Add Phone Number from Phone Book");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setTextColor(Color.parseColor("#bdbdbd"));
                showDialoge(v);
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SaveToServer();
            }
        });

    }

    private void showDialoge(View v) {
        Cursor   phones = null;
        List<String> myString = new ArrayList<>();
        final List<String> myStringphone = new ArrayList<>();

        try {
            phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        }catch (Exception e)
        {
            Intent intentp = new Intent();
            intentp.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intentp.setData(uri);
            startActivity(intentp);
        }
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            myString.add(phoneNumber+"\n"+name);
            myStringphone.add(phoneNumber);
        }
        phones.close();

        // custom dialog
        dialog = new Dialog(add_update_customer_record.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);

        Boolean addToList=false;
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        IgnoreCaseComparator icc = new IgnoreCaseComparator();
        java.util.Collections.sort(myString, icc);
//        for (int i = 0; i < myString.size(); i++) {
//            stringList.add(myString2.get(i));
//        }
        for (int i = 0; i < myString.size(); i++) {
            RadioButton rb = new RadioButton(add_update_customer_record.this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(myString.get(i));
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
                        edtphone.setText(myStringphone.get(x));
                        dialog.dismiss();
                    }
                }
            }

        });
    }

    private void showTheRec(String selectedname) {
        try

        {
            Cursor cursor = db.getCustomerNames();
            int z = 0;
            int count = 0;
            boolean found = false;
            int counter=1;
            if (cursor.moveToFirst()) {
                do {
                    String a = (cursor.getString(0).toString());

                    String b = (cursor.getString(1).toString());

                    String c = (cursor.getString(2).toString());

                    String d = (cursor.getString(3).toString());
                    String e = (cursor.getString(4).toString());
                   // Toast.makeText ( this , b+" : "+d+"\n"+e+"/////"+selectedname , Toast.LENGTH_SHORT ).show ( );
                    if((b+" : "+d+"\n"+e).equals(selectedname)) {

                        edtname.setText(""+b);
                        edtphone.setText(""+c);
                        edtqarza.setText(""+d);
                        edtaddress.setText(""+e);
                        btnsave.setText("Update");
                        selectednameId=a;
                    }
                    counter++;
                } while (cursor.moveToNext());
         }
            cursor.close();

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
        }
    }

    private void SaveToServer() {

        final String name = edtname.getText().toString().trim();
        final String phone = edtphone.getText().toString().trim();
        final String qarza = edtqarza.getText().toString().trim();
        final String address = edtaddress.getText().toString().trim();

        if (name.equalsIgnoreCase("")) {
            edtname.setError("This field can not be blank");
        }
        else if (phone.equalsIgnoreCase("")|| (phone.length()<11)) {
            edtphone.setError("This field can not be blank");
        }
        else if (address.equalsIgnoreCase("")|| (phone.length()<11)) {
            edtaddress.setError("This field can not be blank");
        }
        else if (qarza.equalsIgnoreCase("")) {
            edtqarza.setError("This field can not be blank");
        }
        else
        {
            if(selectedname!=null)
            {
               int UserID = Integer.parseInt(selectednameId);
               updateCurrent(name,phone,qarza,address,0,0,UPDATE_NOT_SYNCED,UserID);

            }
            else {


                StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.CUSTOMER_INSERT_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")) {
                                        saveCusToLocalStorage(name, phone, qarza,address, INSERT_SYNCED, 0, 0);
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("message")
                                                , Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(getApplicationContext(),Customer_Record.class);
                                        startActivity(i);
                                    }else {
                                        saveCusToLocalStorage(name,phone,qarza, address , INSERT_NOT_SYNCED,0,0);
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(getApplicationContext(),Customer_Record.class);
                                        startActivity(i);
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        saveCusToLocalStorage(name,phone,qarza, address , INSERT_NOT_SYNCED,0,0);
                        Intent i=new Intent(getApplicationContext(),Customer_Record.class);
                        startActivity(i);

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("name", name);
                        params.put("phone", phone);
                        params.put("qarza", qarza);
                        params.put("address", address);


                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        }

}

    private void updateCurrent(String Cname , String Cphone , String Cqarza , String Caddress , int insertStatus , int deleteStatus , int updateStatus , int UserID) {
        db.UpdateCustomer(Cname, Cphone, Cqarza,Caddress, insertStatus, deleteStatus, updateStatus, UserID);

        Toast.makeText(getApplicationContext(), "Customer Record is Updated", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Customer_Record.class);
        startActivity(intent);
    }


    private void saveCusToLocalStorage(String Cname , String Cphone , String Cqarza , String Caddress , int insertStatus , int deleteStatus , int updateStatus)
    {
        db.addCustomer(Cname, Cphone,Cqarza,Caddress,insertStatus,deleteStatus,updateStatus);
    }

}
