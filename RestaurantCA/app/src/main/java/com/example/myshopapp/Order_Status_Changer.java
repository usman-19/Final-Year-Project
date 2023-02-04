package com.example.myshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Order_Status_Changer extends AppCompatActivity {
    String selectedid;
    TextView tvorder,tvtotal,tvbelance;
    Button btnconfirm;
    Cursor cursor;
    String cusphone;
    private DatabaseHelper db;
    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__status__changer);
        tvorder=(TextView)findViewById(R.id.tvorderdetail);
        tvtotal=(TextView)findViewById(R.id.tvordertotal);
        tvbelance=(TextView)findViewById(R.id.tvorderbelance);
        btnconfirm=(Button)findViewById(R.id.btnconfirm);
        btnconfirm.setText("Calculate");
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    int id=Integer.parseInt( getNewSelectedphone(cusphone));
                   // Toast.makeText(Order_confirmation.this, id+""+cusphone, Toast.LENGTH_SHORT).show();
                    int oid=Integer.parseInt(selectedid);
                    if(status.equals ( "In Progress" )) {
                        updateOrderToDelivered ( "Delivered" , 0 , 0 , 1 , oid );
                    }
                    else if(status.equals ( "Delivered" )){
                        updateOrderToDelivered ( "Paid" , 0 , 0 , 1 , oid );
                    }

                Intent intent=new Intent ( getApplicationContext (),Orders_Managment.class );
                startActivity ( intent );
            }
        });

        Intent i = getIntent();
        selectedid = i.getStringExtra("orderid");
        db = new DatabaseHelper(this);
        GetRecord("");
    }

    private void updateOrderToDelivered(String delivered , int i , int i1 , int i2 , int oid) {
        db.UpdateOrderToDeliverd(delivered,i,i1,i2, oid);
    }


    private String getNewSelectedphone(String a) {
        String newSN = "";

        Cursor  Cutcorsore = db.getCustomerNames();

        if (Cutcorsore.moveToFirst()) {
            do {
                String id = (Cutcorsore.getString(0).toString());
                String d = (Cutcorsore.getString(2).toString());
                // Toast.makeText(getApplicationContext(),""+a+"//"+id,Toast.LENGTH_LONG).show();
                if (a.equals(d)) {
                    newSN = id;
                    //Toast.makeText(getApplicationContext(),""+newSN+"//"+userid,Toast.LENGTH_LONG).show();
                }

            }
            while (Cutcorsore.moveToNext());

        }
        return newSN;
    }
    private void GetRecord(String s) {
        cursor = db.getSellDetailData();
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
                String os = (cursor.getString(9).toString());
                // Toast.makeText(this, s+":"+os, Toast.LENGTH_SHORT).show();
                if (id.equals(selectedid)) {
                    cusphone=cid;
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
                        status=os;
                        if(os.equals ( "In Progress" ))
                        {
                            btnconfirm.setText ( "Delivered" );
                            pass=pass+"\n"+ cursor.getString(8).toString();
                        }
                        else  if(os.equals ( "Delivered" ))
                        {
                            btnconfirm.setText ( "Paided" );
                            pass=pass+"\n"+ cursor.getString(8).toString();
                        }
                        else {
                            btnconfirm.setVisibility(View.GONE);
                        }

                        pass = pass + "\n" + "Order_Status    :" + cursor.getString(9).toString();

                    }
                    tvorder.setText(pass);
                }

            } while (cursor.moveToPrevious());
        }

    }
}