package com.example.myshopapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Total_Stock_amount extends Activity {
    SQLiteDatabase SQLITEDATABASE;
    ListView list;
    String GetSQliteQuery;
    String selectednameId;
    Cursor cursor;
    Boolean longpress=false;
    String type;
    String selectedname="";
    TextView tvTotalqarza;
    int counter=0;
    int totalamount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setIcon(R.drawable.icon);
        setContentView(R.layout.activity_total__stock_amount);
        // tvTotalqarza=(TextView)findViewById(R.id.btntotalQarza);
        //tvTotalqarza.setText(tvTotalqarza.getText().toString()+countTotalQarza());
        List<String> myString = new ArrayList<String>();
        final List<String> myString2 = new ArrayList<String>();
        list = (ListView) findViewById(R.id.listView1);
        final Button btnaddnew=(Button)findViewById(R.id.btnNew);
        final Intent intent=getIntent();
        type=intent.getStringExtra("type");
        tvTotalqarza=(TextView)findViewById(R.id.tvTotalAmount);
        btnaddnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("buy"))
                {
                  Intent intent1=new Intent(getApplicationContext(),Total_Stock_amount.class);
                  intent.putExtra("type","sell");
                  startActivity(intent);
                }
                else {
                    Intent intent1=new Intent(getApplicationContext(),Total_Stock_amount.class);
                    intent.putExtra("type","buy");
                    startActivity(intent);
                }
            }
        });

        try

        {
            GetSQliteQuery = "SELECT * FROM ItemsTable";

            SQLITEDATABASE = openOrCreateDatabase("MyShopDB", Context.MODE_PRIVATE, null);

            cursor = SQLITEDATABASE.rawQuery(GetSQliteQuery, null);

            int z = 0;
            int count = 0;
            boolean found = false;

            if (cursor.moveToFirst()) {
                do {
                    String a = (cursor.getString(0).toString());

                    String b = (cursor.getString(1).toString());

                    String c = (cursor.getString(2).toString());

                    String d = (cursor.getString(3).toString());
                    String e = (cursor.getString(4).toString());
                    String f = (cursor.getString(5).toString());
                  if(type.equals("buy"))
                  {
                      btnaddnew.setText("Switch to Selling Amount");
                        myString2.add(b + " \\" + c + " : " + d + "*" + f + "=" + (Integer.parseInt(d) * Integer.parseInt(f)));
                        totalamount=totalamount+(Integer.parseInt(d) * Integer.parseInt(f));
                    }
                  else {
                      btnaddnew.setText("Switch to Buying Amount");
                      myString2.add(b + " \\" + c + " : " + d + "*" + e + "=" + (Integer.parseInt(d) * Integer.parseInt(e)));
                      totalamount=totalamount+(Integer.parseInt(d) * Integer.parseInt(e));
                  }
                    counter++;
                } while (cursor.moveToNext());
            }
            IgnoreCaseComparator icc = new IgnoreCaseComparator();

            java.util.Collections.sort(myString2,icc);

            for (int i=0; i<myString2.size();i++)
            {
                myString.add((i+1)+") "+myString2.get(i));
            }



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myString);
            list.setAdapter(adapter);

//            cursor.close();

        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
        }
        tvTotalqarza.setText("("+totalamount+")");
//        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            public boolean onItemLongClick(AdapterView<?> arg0, View v,
//                                           int index, long arg3) {
//                //code below when user long press on item
//                String strWithcounter=list.getItemAtPosition(index).toString();
//                selectedname="";
//                // Toast.makeText(getApplicationContext(),"index "+index,Toast.LENGTH_SHORT).show();
//                if (index < 10&&index>=0) {
//                    for (int i = 3; i < strWithcounter.length(); i++) {
//                        selectedname = selectedname + strWithcounter.charAt(i);
//                    }
//                }else if(index>9&&index<100)
//                {
//                    for (int i = 4; i < strWithcounter.length(); i++) {
//                        selectedname = selectedname + strWithcounter.charAt(i);
//                    }
//                    //Toast.makeText(getApplicationContext(),"index "+index,Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    for (int i = 5; i < strWithcounter.length(); i++) {
//                        selectedname = selectedname + strWithcounter.charAt(i);
//                    }
//                }
//
//                //Toast.makeText(getApplicationContext(), "selected item"+selectedname, Toast.LENGTH_LONG).show();
//                // convertToOrgnal();
//                AlertDialog.Builder builder2=new AlertDialog.Builder(Total_Stock_amount.this);
//                builder2.setMessage(""+selectedname);
//                builder2.setPositiveButton("Update",new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//// TODO Auto-generated method stub
//                        Intent i=new Intent(getApplicationContext(),Add_Update_Item_Rec.class);
//                        i.putExtra("name",selectedname);
//                        startActivity(i);
//
//                    }
//
//                });
//
//                builder2.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
//
//                    @Override
//
//                    public void onClick(DialogInterface dialog, int which) {
//
//// TODO Auto-generated method stub
//
//                        // Toast.makeText(getApplicationContext(), "U Clicked delete ", Toast.LENGTH_LONG).show();
//                        showTheRec(selectedname);
//                        int UserID = Integer.parseInt(selectednameId);
//
//                        String DeleteQuery = "DELETE FROM itemsTable WHERE id=" + UserID + ";";
//
//                        SQLITEDATABASE.execSQL(DeleteQuery);
//
//                        Toast.makeText(getApplicationContext(), "Item record Deleted Successfully", Toast.LENGTH_LONG).show();
//
//                        cursor = SQLITEDATABASE.rawQuery(GetSQliteQuery, null);
//                        Intent i=new Intent(getApplicationContext(),Item_Rec.class);
//                        startActivity(i);
//
//                    }
//
//                });
//
//                builder2.show();
////                Toast.makeText(getApplicationContext(), "long press:" + selectedname, Toast.LENGTH_LONG).show();
//                return true;
//
//            }
//        });
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                //code below when user press item
//                //Toast.makeText(getApplicationContext(), "press" + list.getItemAtPosition(arg2).toString(), Toast.LENGTH_LONG).show();
////                selectedname=list.getItemAtPosition(arg2).toString();
////                Intent i=new Intent(getApplicationContext(),Sell_on_Customer.class);
////                i.putExtra("name",selectedname);
////                startActivity(i);
//
//            }
//        });

    }

    private void showTheRec(String selectedname) {
        try

        {
            String GetSQliteQuery = "SELECT * FROM itemsTable";

            SQLITEDATABASE = openOrCreateDatabase("MyShopDB", Context.MODE_PRIVATE, null);

            Cursor cursor = SQLITEDATABASE.rawQuery(GetSQliteQuery, null);

            int z = 0;
            int count = 1;
            boolean found = false;

            if (cursor.moveToFirst()) {
                do {
                    String a = (cursor.getString(0).toString());

                    String b = (cursor.getString(1).toString());

                    String c = (cursor.getString(2).toString());

                    String d = (cursor.getString(3).toString());
                    if((b+" \\"+c+" : "+d).equals(selectedname)) {
                        selectednameId=a;
                    }
                    count++;
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Item_Record.class);
        startActivity(intent);
    }
}
