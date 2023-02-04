package com.example.myshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myshopapp.MySingleton;
//import com.example.myshopapp.unbookedTables;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class add_update_table_record extends AppCompatActivity {
//changes inurl replace the url of sohail app
    public static final String ADDTABLE_URL = "https://sohailapp.000webhostapp.com/Restaurant_App_API/AddTable.php";
    EditText edtTableNo, edtTbPlace, edtTbCapacity;
    Button btnAddTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_table_record);


        edtTableNo = findViewById(R.id.edtTbNumber);
        edtTbPlace = findViewById(R.id.edtTablePlace);
        edtTbCapacity = findViewById(R.id.edtCapacity);
        btnAddTable = findViewById(R.id.btnAddTable);

        btnAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTable();
            }
        });

    }

    private void AddTable() {

        final String TableNo = edtTableNo.getText().toString().trim();
        final String TableCapacity = edtTbCapacity.getText().toString().trim();
        final String TablePlace = edtTbPlace.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADDTABLE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")) {

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Table_Record.class));
                            } else {

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> details = new HashMap<String, String>();
                details.put("tableno", TableNo);
                details.put("tableseats", TableCapacity);
                details.put("tableplace", TablePlace);
                details.put("tablestatus", "Paid");

                return details;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}