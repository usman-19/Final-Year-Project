package com.example.myshopapp;

import androidx.appcompat.app.AppCompatActivity;

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

public class add_update_supplier_rec extends AppCompatActivity {

    EditText edtname,edtphone,edtqarza,edtaddress,edtcatagory;
    Button btnsave;
    String selectedname;
    String selectednameId;
    Dialog dialog;
    private DatabaseHelper db;
    private static final int INSERT_SYNCED = 0;
    private static final int INSERT_NOT_SYNCED= 1;
    private static final int UPDATE_NOT_SYNCED= 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_supplier_rec);
        edtname = (EditText) findViewById(R.id.edtname);
        edtphone = (EditText) findViewById(R.id.edtdescription);
        edtaddress = (EditText) findViewById(R.id.edtaddress);
        edtcatagory = (EditText) findViewById(R.id.edtcatgory);
        edtphone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});
        edtqarza = (EditText) findViewById(R.id.edtItemNo);
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
        Cursor phones = null;
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
        dialog = new Dialog(add_update_supplier_rec.this);
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
            RadioButton rb = new RadioButton(add_update_supplier_rec.this); // dynamically creating RadioButton and adding to RadioGroup.
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
            Cursor cursor = db.getSupplierNames();

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
                    if((b+" : "+d+" \n"+e+" : "+f).equals(selectedname)) {
                        edtname.setText(""+b);
                        edtphone.setText(""+c);
                        edtqarza.setText(""+d);
                        edtaddress.setText(""+e);
                        edtcatagory.setText(""+f);
                        btnsave.setText("اپ ڈیٹ");
                        selectednameId=a;
                    }
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
        final String catagory = edtcatagory.getText().toString().trim();
        if (edtname.getText().toString().trim().equalsIgnoreCase("")) {
            edtname.setError("This field can not be blank");
        }
        else if (edtphone.getText().toString().trim().equalsIgnoreCase("")|| (phone.length()<11)) {
            edtphone.setError("This field can not be blank");
        }
        else if (edtqarza.getText().toString().trim().equalsIgnoreCase("")) {
            edtqarza.setError("This field can not be blank");
        }
        else if (edtaddress.getText().toString().trim().equalsIgnoreCase("")) {
            edtaddress.setError("This field can not be blank");
        }
        else if (edtcatagory.getText().toString().trim().equalsIgnoreCase("")) {
            edtcatagory.setError("This field can not be blank");
        }
        else
        {
            if(selectedname!=null)
            {
                int UserID = Integer.parseInt(selectednameId);
                updateCurrent(name,phone,qarza,address,catagory,0,0,UPDATE_NOT_SYNCED,UserID);

                Intent intent = new Intent(getApplicationContext(),Supplier_Record.class);
                startActivity(intent);

            }
            else {


                StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.SUPPLIER_INSERT_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")) {
                                        saveSuppToLocalStorage(name, phone, qarza, address, catagory, INSERT_SYNCED, 0, 0);
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getApplicationContext(),Supplier_Record.class);
                                        startActivity(intent);

                                    }else {
                                        saveSuppToLocalStorage(name,phone,qarza, address , catagory , INSERT_NOT_SYNCED,0,0);
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getApplicationContext(),Supplier_Record.class);
                                        startActivity(intent);

                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        saveSuppToLocalStorage(name,phone,qarza, address , catagory , INSERT_NOT_SYNCED,0,0);
                        Intent intent=new Intent(getApplicationContext(),Supplier_Record.class);
                        startActivity(intent);
                        Toast.makeText ( getApplicationContext () , "error volly" , Toast.LENGTH_SHORT ).show ( );
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
                        params.put("catagory", catagory);


                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        }

    }

    private void updateCurrent(String Sname , String Sphone , String Sqarza , String address , String catagory , int insertStatus , int deleteStatus , int updateStatus , int UserID) {
        db.UpdateSupplier(Sname, Sphone, Sqarza,address,catagory, insertStatus, deleteStatus, updateStatus, UserID);

        Toast.makeText(getApplicationContext(), "Supplier Record is Updated", Toast.LENGTH_LONG).show();
         }

    private void saveSuppToLocalStorage(String Sname , String Sphone , String Sqarza , String address , String catagory , int insertStatus , int deleteStatus , int updateStatus)
                {
                 db.addSupplier(Sname, Sphone,Sqarza,address,catagory,insertStatus,deleteStatus,updateStatus);
                }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Supplier_Record.class);
        startActivity(intent);
    }
}