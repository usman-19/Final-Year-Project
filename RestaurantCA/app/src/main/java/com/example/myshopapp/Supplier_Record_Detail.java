package  com.example.myshopapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class Supplier_Record_Detail extends Activity {
    SQLiteDatabase SQLITEDATABASE;
    ListView list;
    String GetSQliteQuery;
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
        tvTotalqarza = (TextView) findViewById(R.id.btntotalQarza);
        registerReceiver(new NetworkMonitor(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        Intent i = getIntent();
        selectedname="";
        selectedname = i.getStringExtra("name");
        inputcatgory = i.getStringExtra("inputcatagory");
        userid = i.getStringExtra("userID");
        cashCus = i.getStringExtra("cashCus");
        btnnam=(TextView)findViewById(R.id.btnnaam);
        btnnam.setText("ٹوٹل پرانہ قرضہ:"+selectedname);
        list = (ListView) findViewById(R.id.listView1);
        db = new DatabaseHelper(this);
        getRecord();
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, final View v,
                                           int index, long arg3) {
                //code below when user long press on item
                selectedname = list.getItemAtPosition(index).toString();
                AlertDialog.Builder builder2 = new AlertDialog.Builder(Supplier_Record_Detail.this);
                builder2.setMessage("" + selectedname);
                builder2.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


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
                                SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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

                                        list.setAdapter(null);
                                        myString.clear();
                                        ListIDS.clear();
                                        getRecord();
//                                    Intent i=new Intent(getApplicationContext(),Supplier_R_Detail.class);
//                                    i.putExtra("name",btnnam.getText().toString());
//                                    btnnam.setText("");
//                                        i.putExtra("inputcatagory", "sup");
//                                        i.putExtra("cashCus", "کیش : سپلائیر");
//                                    i.putExtra("userID",userid);
//                                    startActivity(i);
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
                //code below when user press item
                //Toast.makeText(getApplicationContext(), "press" + list.getItemAtPosition(arg2).toString(), Toast.LENGTH_LONG).show();
//                selectedname=list.getItemAtPosition(arg2).toString();
//                Intent i=new Intent(getApplicationContext(),Sell_on_Customer.class);
//                i.putExtra("name",selectedname);
//                startActivity(i);

            }
        });
//        Button btnaddnew=(Button)findViewById(R.id.btnNew);
//        btnaddnew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i=new Intent(getApplicationContext(),Add_Update_Item_Rec.class);
//                startActivity(i);
//            }
//        });

    }

    private void getRecord() {

        try {
            cursor = db.getBuyDetailData();

            int z = 0;
            int count = 0;
            boolean found = false;

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
                    String pass="";
                    String rpaid="";
                    for(int j=1;j<paid.length();j++) {
                        rpaid=rpaid+paid.charAt(j);
                    }
                    if(cashCus.equals("کیش : سپلائیر"))
                    {
                        if (cid.equals("کیش : سپلائیر")) {
                            pass = pass + "\n" + "تاریخ :" + date;
                            pass = pass + "\n" + "تفصیل :" + descp +
                                    "\n" + "ٹوٹل :" + nt;
                            myString.add(pass);
                            ListIDS.add(id);
                        }
                    }
                    else {
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
                                        pass = pass + "\n" + "ادائیگی:" + rpaid
                                                +"\n" + "بقایا :" + (Integer.parseInt(nt) - Integer.parseInt(rpaid));
                                    }
                                }
                                pass=pass+ "\n" + "میزان :" + belance;
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
        }catch (Exception e){
            GetDataFromServer();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myString);
        list.setAdapter(adapter);
    }

    private void deleteCurrent(int insertStatus,int deleteStatus,int updateStatus,int UserID) {
        db.deleteBuyStatus(insertStatus, deleteStatus, updateStatus, UserID);

        Toast.makeText(getApplicationContext(), "Buy Record is Deleted", Toast.LENGTH_LONG).show();
    }

    private void GetDataFromServer() {

        final String Phone = "phone";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkMonitor.BUY_SERVER_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++)
                            {

                                JSONObject obj = array.getJSONObject(i);

                                obj.getString("Supid");
                                obj.getString("Bdate");
                                obj.getString("Bdes");
                                obj.getString("Bprevtotal");
                                obj.getString("Bnewtotal");
                                obj.getString("Bpaid");
                                obj.getString("Bbalance");

                                Toast.makeText(getApplicationContext(), ""+obj.getString("Supid"), Toast.LENGTH_SHORT).show();

                                saveBuyToLocalStorage(obj.getString("Supid"),
                                        obj.getString("Bdate"),
                                        obj.getString("Bdes"),
                                        obj.getString("Bprevtotal"),
                                        obj.getString("Bnewtotal"),
                                        obj.getString("Bpaid"),
                                        obj.getString("Bbalance"),0,0,0
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

    private void saveBuyToLocalStorage(String supid, String date,String description ,String prevTotal,String newTotal,String Paid, String Balance,int insertStatus,int deleteStatus,int updateStatus)
    {
        db.addBuy(supid,date,description,prevTotal,newTotal,Paid,Balance,insertStatus,deleteStatus,updateStatus);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),Buy_From_Supplier.class);
        i.putExtra("name",selectedname);
        startActivity(i);

    }
}
//            Intent intent = new Intent(ge