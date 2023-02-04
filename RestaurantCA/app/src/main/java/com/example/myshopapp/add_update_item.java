package com.example.myshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

public class add_update_item extends AppCompatActivity {

    EditText edtname, edtdescription, edtnumberofitm, edtbyingprice, edtsellprice,edtdprice;
    Button btnsave, btncatagory;
    String selectedname;
    String selectednameId;
    TextView tvcatagory;
    Cursor itemcorsore;
    boolean catagoryselection=false;
    Dialog dialog;
    private DatabaseHelper db;
    private static final int INSERT_SYNCED = 0;
    private static final int INSERT_NOT_SYNCED = 1;
    private static final int UPDATE_NOT_SYNCED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        this.requestWindowFeature ( Window.FEATURE_NO_TITLE );
        setContentView ( R.layout.activity_add__update__item__rec );
        Intent i = getIntent ( );
        selectedname = i.getStringExtra ( "name" );
        edtname = (EditText) findViewById ( R.id.edtname );
        edtdescription = (EditText) findViewById ( R.id.edtdescription );
        edtnumberofitm = (EditText) findViewById ( R.id.edtItemNo );
        edtbyingprice = (EditText) findViewById ( R.id.edtbuyingprice );
        edtdprice = (EditText) findViewById ( R.id.edtprice );
        btnsave = (Button) findViewById ( R.id.btnsave );
        btncatagory = (Button) findViewById ( R.id.btncatagory );
        tvcatagory = (TextView) findViewById ( R.id.tvcatagory );
        db = new DatabaseHelper ( this );
        edtsellprice = (EditText) findViewById ( R.id.edtsellprice );
        if (selectedname != "") {
            showTheRec ( selectedname );
        }

        btncatagory.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                showDialoge ( v );
            }
        } );
        btnsave.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {

                SaveToServer ( );
            }
        } );

    }

    private void showTheRec(String selectedname) {
        try {
            Cursor cursor = db.getItemNames ( );
            int z = 0;
            int count = 1;
            boolean found = false;

            if (cursor.moveToFirst ( )) {
                do {
                    String a = (cursor.getString ( 0 ).toString ( ));

                    String b = (cursor.getString ( 1 ).toString ( ));

                    String c = (cursor.getString ( 2 ).toString ( ));

                    String d = (cursor.getString ( 3 ).toString ( ));
                    String e = (cursor.getString ( 4 ).toString ( ));
                    String f = (cursor.getString ( 5 ).toString ( ));
                    String g = (cursor.getString ( 6 ).toString ( ));
                    String h = (cursor.getString ( 7 ).toString ( ));
                    if ((b + "/" + c + " : " + d).equals ( selectedname )) {
                        edtname.setText ( "" + b );
                        edtdescription.setText ( "" + c );
                        edtnumberofitm.setText ( "" + d );
                        edtbyingprice.setText ( "" + f );
                        edtsellprice.setText ( "" + e );
                        tvcatagory.setText ( "" + g );
                        edtdprice.setText ( "" + h );

                        btnsave.setText ( "Update" );
                        selectednameId = a;
                    }
                    count++;
                } while (cursor.moveToNext ( ));
            }
            cursor.close ( );

        } catch (Exception e) {


            Toast.makeText ( getApplicationContext ( ) , "No Data Found" , Toast.LENGTH_LONG ).show ( );
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
        }
    }


    private void SaveToServer() {


        final String iName = edtname.getText ( ).toString ( ).trim ( );
        final String iDes = edtdescription.getText ( ).toString ( ).trim ( );
        final String iQtt = edtnumberofitm.getText ( ).toString ( ).trim ( );
        final String iBp = edtbyingprice.getText ( ).toString ( ).trim ( );
        final String iSp = edtsellprice.getText ( ).toString ( ).trim ( );
        final String iCatagory = tvcatagory.getText ( ).toString ( ).trim ( );
        final String iDp = edtdprice.getText ( ).toString ( ).trim ( );

        if (iName.equalsIgnoreCase ( "" )) {
            edtname.setError ( "This field can not be blank" );
        } else if (iDes.equalsIgnoreCase ( "" )) {
            edtdescription.setError ( "This field can not be blank" );
        } else if (iQtt.equalsIgnoreCase ( "" )) {
            edtnumberofitm.setError ( "This field can not be blank" );
        } else if (iBp.equalsIgnoreCase ( "" )) {
            edtbyingprice.setError ( "This field can not be blank" );
        } else if (iSp.equalsIgnoreCase ( "" )) {
            edtsellprice.setError ( "This field can not be blank" );
        }
     else if (iDp.equalsIgnoreCase ( "" )) {
            edtdprice.setError ( "This field can not be blank" );
        }
        else if(catagoryselection==false)
        {
            tvcatagory.setError ("Category must be selected"  );
        }
    else {
            if (selectedname != null) {

                if (!itempresent ( ) == true) {
                    StringRequest stringRequest = new StringRequest ( Request.Method.POST , NetworkMonitor.ITEM_UPDATE_URL ,
                            new Response.Listener<String> ( ) {
                                @Override
                                public void onResponse(String response) {
                                    int UserID = Integer.parseInt ( selectednameId );
                                    try {
                                        JSONObject jsonObject = new JSONObject ( response );
                                        if (!jsonObject.getBoolean ( "error" )) {
                                            updateCurrent ( iName , iDes , iQtt , edtsellprice.getText ( ).toString ( ).trim ( ) , edtbyingprice.getText ( ).toString ( ).trim ( ),iCatagory,iDp , 0 , 0 , 0 , UserID );

                                            Toast.makeText ( getApplicationContext ( ) , jsonObject.getString ( "message" ) , Toast.LENGTH_SHORT ).show ( );
                                        } else {
                                            updateCurrent ( iName , iDes , iQtt , edtsellprice.getText ( ).toString ( ).trim ( ) , edtbyingprice.getText ( ).toString ( ).trim ( ) , iCatagory , iDp , 0 , 0 , 1 , UserID );

                                            Toast.makeText ( getApplicationContext ( ) , jsonObject.getString ( "message" ) , Toast.LENGTH_SHORT ).show ( );

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace ( );
                                    }
                                }
                            } , new Response.ErrorListener ( ) {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            int UserID = Integer.parseInt ( selectednameId );
                            updateCurrent ( iName , iDes , iQtt , edtsellprice.getText ( ).toString ( ).trim ( ) , edtbyingprice.getText ( ).toString ( ).trim ( ) , iCatagory , iDp , 0 , 0 , 1 , UserID );
                            Toast.makeText ( add_update_item.this , "Record updtaed" , Toast.LENGTH_SHORT ).show ( );

                        }
                    } ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String> ( );
                            params.put ( "iName" , iName );
                            params.put ( "iDes" , iDes );
                            params.put ( "iQtt" , iQtt );
                            params.put ( "iBp" , edtsellprice.getText ( ).toString ( ).trim ( ) );
                            params.put ( "iSp" , edtbyingprice.getText ( ).toString ( ).trim ( ) );
                            params.put ( "iCatagory" , iCatagory );
                            params.put ( "iDp" , iDp );


                            return params;
                        }
                    };
                    MySingleton.getInstance ( getApplicationContext ( ) ).addToRequestQueue ( stringRequest );
                    Intent i = new Intent ( getApplicationContext ( ) , Item_Record.class );
                    startActivity ( i );
                } else {
                    Toast.makeText ( this , "This item is already present" , Toast.LENGTH_SHORT ).show ( );
                }

            } else {

                if (!itempresent ( ) == true) {
                    StringRequest stringRequest = new StringRequest ( Request.Method.POST , NetworkMonitor.ITEM_INSERT_URL ,
                            new Response.Listener<String> ( ) {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject ( response );
                                        if (!jsonObject.getBoolean ( "error" )) {
                                            saveItemToLocalStorage ( iName , iDes , iQtt , iBp , iSp,iCatagory,iDp , INSERT_SYNCED , 0 , 0 );
                                            Toast.makeText ( getApplicationContext ( ) , jsonObject.getString ( "message" ) , Toast.LENGTH_SHORT ).show ( );
                                        } else {
                                            saveItemToLocalStorage ( iName , iDes , iQtt , iBp , iSp , iCatagory , iDp , INSERT_NOT_SYNCED , 0 , 0 );
                                            Toast.makeText ( getApplicationContext ( ) , jsonObject.getString ( "message" ) , Toast.LENGTH_SHORT ).show ( );
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace ( );
                                    }
                                }
                            } , new Response.ErrorListener ( ) {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            saveItemToLocalStorage ( iName , iDes , iQtt , iBp , iSp , iCatagory , iDp , INSERT_NOT_SYNCED , 0 , 0 );
                            Toast.makeText ( add_update_item.this , "Record saved" , Toast.LENGTH_SHORT ).show ( );

                        }
                    } ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String> ( );
                            params.put ( "iName" , iName );
                            params.put ( "iDes" , iDes );
                            params.put ( "iQtt" , iQtt );
                            params.put ( "iBp" , iBp );
                            params.put ( "iSp" , iSp );
                            params.put ( "iCatagory" , iCatagory );
                            params.put ( "iDp" , iDp );


                            return params;
                        }
                    };
                    MySingleton.getInstance ( getApplicationContext ( ) ).addToRequestQueue ( stringRequest );
                    Intent i = new Intent ( getApplicationContext ( ) , Item_Record.class );
                    startActivity ( i );
                } else {
                    Toast.makeText ( this , "This item is already present" , Toast.LENGTH_SHORT ).show ( );
                }
            }


        }
    }

    private boolean itempresent() {
        Boolean find = false;
        try {
            Cursor cursor = db.getItemNames ( );
            int z = 0;
            int count = 1;
            boolean found = false;

            if (cursor.moveToFirst ( )) {
                do {
                    String a = (cursor.getString ( 0 ).toString ( ));

                    String b = (cursor.getString ( 1 ).toString ( ));

                    String c = (cursor.getString ( 2 ).toString ( ));

                    String d = (cursor.getString ( 3 ).toString ( ));
                    String e = (cursor.getString ( 4 ).toString ( ));
                    String f = (cursor.getString ( 5 ).toString ( ));
                    if ((b + "" + c).equals ( edtname.getText ( ).toString ( ) + edtdescription.getText ( ).toString ( ) ) && !a.equals ( selectednameId )) {
                        find = true;
                    }
                    count++;
                } while (cursor.moveToNext ( ));
            }
            cursor.close ( );

        } catch (
                Exception e) {


            Toast.makeText ( getApplicationContext ( ) , "No Data Found" , Toast.LENGTH_LONG ).show ( );
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
        }

        return find;
    }

    private void updateCurrent(String Iname , String Ides , String itemQ , String itemBP , String itemSP , String iCatagory , String iDp , int insertStatus , int deleteStatus , int updateStatus , int UserID) {
        db.UpdateItem ( Iname , Ides , itemQ , itemBP , itemSP , insertStatus , deleteStatus , updateStatus , UserID );

        Toast.makeText ( getApplicationContext ( ) , "Items Record is Updated" , Toast.LENGTH_LONG ).show ( );
    }

    private void saveItemToLocalStorage(String Iname , String Ides , String itemQ , String itemBP , String itemSP , String iCatagory , String iDp , int insertStatus , int deleteStatus , int updateStatus) {
        db.addItem ( Iname , Ides , itemQ , itemBP , itemSP,iCatagory,iDp , insertStatus , deleteStatus , updateStatus );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ( );
        Intent intent = new Intent ( getApplicationContext ( ) , Item_Record.class );
        startActivity ( intent );
    }

    private String showDialoge(final View view) {
        try {

            itemcorsore = db.getSupplierNames ( );

        } catch (Exception e) {
            Toast.makeText ( getApplicationContext ( ) , "Pleas Add Items to The Item List First" , Toast.LENGTH_LONG ).show ( );
        }

        // custom dialog
        dialog = new Dialog ( add_update_item.this );
        dialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
        dialog.setContentView ( R.layout.radiobutton_dialog );
        List<String> myString2 = new ArrayList<> ( );
        if (itemcorsore.moveToFirst ( )) {
            do {
                String c = (itemcorsore.getString ( 5 ).toString ( ));

                myString2.add ( c );
            }
            while (itemcorsore.moveToNext ( ));

        }
        itemcorsore.close ( );
        RadioGroup rg = (RadioGroup) dialog.findViewById ( R.id.radio_group );
        IgnoreCaseComparator icc = new IgnoreCaseComparator ( );

        java.util.Collections.sort ( myString2 , icc );
        for (int i = 0; i < myString2.size ( ); i++) {
            RadioButton rb = new RadioButton ( add_update_item.this ); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText ( myString2.get ( i ) );
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
                        tvcatagory.setText ( a );
                        catagoryselection=true;
                        dialog.dismiss ();
                    }
                }

            }
        } );
    return null;
    }
}
