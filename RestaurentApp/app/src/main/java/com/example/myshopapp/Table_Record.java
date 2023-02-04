package com.example.myshopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Table_Record extends AppCompatActivity {
Button btnnew;
    ListView list;
    final List<String> myString = new ArrayList<String>();

    private static final String GETTABLESDATA_URL = "https://sohailapp.000webhostapp.com/Restaurant_App_API/GetUnbookedTablesData.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table__record);
        btnnew=(Button)findViewById(R.id.btnNew);
        list = (ListView) findViewById(R.id.listView1);
        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),add_update_table_record.class);
                startActivity(intent);
            }
        });
        GetTablesDataServer();
    }
    private void GetTablesDataServer() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTABLESDATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject obj = array.getJSONObject(i);


                                    String tableno=    obj.getString("tableno");
                                     String place=   obj.getString("tableplace");
                                      String seats=  obj.getString("tableseats");
                                       String status= obj.getString("tablestatus");
                                      String id=  obj.getString("tableid");
                                myString.add("table : "+tableno+"\n"+place+"\n"+seats);

                            }



                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "catch error "+e, Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                        showdatainlist();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(),"onErrorResponse "+error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void showdatainlist() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myString);
        list.setAdapter(adapter);
    }
}





