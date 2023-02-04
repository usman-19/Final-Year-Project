package com.example.myshopapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkMonitor extends BroadcastReceiver {

    private static final int INSERT_SYNCED = 0;
    private static final int UPDATE_SYNCED = 0;

    public static final String DATA_SAVED_BROADCAST = "net.example.datasaved";
    // i have remove public static final from all 000webhost
  // public static final String CUSTOMER_URL="http://192.168.43.36/MyShopAppApi/";saidsuchyar12@gmail.com/suchyar1202
    public static final String CUSTOMER_URL="https://sohailapp.000webhostapp.com/Restaurant_App_API/";
    public static final String CUSTOMER_INSERT_URL =CUSTOMER_URL+"CustomerInsert.php";
    public static final String CUSTOMER_UPDATE_URL =CUSTOMER_URL+"CustomerUpdate.php";
    public static final String CUSTOMER_DELETE_URL =CUSTOMER_URL+"CustomerDelete.php";
    public static final String CUSTOMER_SERVER_DATA_URL =CUSTOMER_URL+"CustomerGetServerData.php";
    public static final String SUPPLIER_INSERT_URL =CUSTOMER_URL+"SupplierInsert.php";
    public static final String SUPPLIER_UPDATE_URL =CUSTOMER_URL+"SupplierUpdate.php";
    public static final String SUPPLIER_DELETE_URL =CUSTOMER_URL+"SupplierDelete.php";
    public static final String SUPPLIER_SERVER_DATA_URL =CUSTOMER_URL+"SupplierGetServerData.php";
    public static final String ITEM_INSERT_URL =CUSTOMER_URL+"ItemInsert.php";
    public static final String ITEM_UPDATE_URL =CUSTOMER_URL+"ItemUpdate.php";
    public static final String ITEM_DELETE_URL =CUSTOMER_URL+"ItemDelete.php";
    public static final String ITEM_SERVER_DATA_URL =CUSTOMER_URL+"ItemGetServerData.php";
    public static final String TRANSECTION_INSERT_URL =CUSTOMER_URL+"TransectionInsert.php";
    public static final String TRANSECTION_ALL_UPDATE_URL =CUSTOMER_URL+"TransectionAllUpdate.php";
    public static final String BUYING_TRANSECTION_PART_UPDATE_URL =CUSTOMER_URL+"BuyingTransectionPartUpdate.php";
    public static final String SELLING_TRANSECTION_PART_UPDATE_URL =CUSTOMER_URL+"SellingTransectionPartUpdate.php";
    public static final String TRANSECTION_DELETE_URL =CUSTOMER_URL+"TransectionDelete.php";
    public static final String TRANSECTION_SERVER_DATA_URL =CUSTOMER_URL+"TransectionGetServerData.php";
    public static final String SELL_INSERT_URL =CUSTOMER_URL+"SellInsert.php";
    public static final String SEll_UPDATE_URL =CUSTOMER_URL+"SellUpdate.php";
    public static final String SELL_DELETE_URL =CUSTOMER_URL+"SellDelete.php";
    public static final String SELL_SERVER_DATA_URL =CUSTOMER_URL+"SellGetServerData.php";
    public static final String BUY_INSERT_URL =CUSTOMER_URL+"BuyInsert.php";
    public static final String BUY_DELETE_URL =CUSTOMER_URL+"BuyDelete.php";
    //rough url path
    public static final String FEEDBACKS_INSERT_URL =CUSTOMER_URL+"BuyDelete.php";
    public static final String  FEEDBACKS_SERVER_DATA_URL =CUSTOMER_URL+"BuyDelete.php";

    public static final String BUY_SERVER_DATA_URL =CUSTOMER_URL+"BuyGetServerData.php";

    public static final String D_CHARGES = "dcharges";
    public static final String ORDER_STATUS = "ostatus";
    private Context context;
    private DatabaseHelper db;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new DatabaseHelper(context);

        List<String> myStringid = new ArrayList<String>();
        List<String> myStringname= new ArrayList<String>();
        List<String> myStringphone = new ArrayList<String>();
        List<String> myStringqarza = new ArrayList<String>();
        List<String> myStringaddress = new ArrayList<String>();
        List<String> myStringcatagory = new ArrayList<String>();
        List<String> myStringiDes = new ArrayList<String>();
        List<String> myStringiQtt = new ArrayList<String>();
        List<String> myStringiBp = new ArrayList<String>();
        List<String> myStringiSp = new ArrayList<String>();
        List<String> myStringT = new ArrayList<String>();
        List<String> myStringS = new ArrayList<String>();
        List<String> myStringDcharges = new ArrayList<String>();
        List<String> myStringOstatus = new ArrayList<String>();
        List<String> myStringCatagory = new ArrayList<String>();
        List<String> myStringDP = new ArrayList<String>();

        if (checkNetworkConnection(context))
        {
            Cursor cursorCus = db.getUnsyncedInsertNames();
            Cursor cursorU = db.getUnsyncedUpdateNames();
            Cursor cursorD = db.getUnsyncedDeleteNames();
            Cursor cursorSupp = db.getUnsyncedSuppInsertNames();
            Cursor cursorSU = db.getUnsyncedSuppUpdateNames();
            Cursor cursorSD = db.getUnsyncedSuppDeleteNames();
            Cursor cursorItm = db.getUnsyncedItemInsertNames();
            Cursor cursorItmU = db.getUnsyncedItemUpdateNames();
            Cursor cursorItmD = db.getUnsyncedItemDeleteNames();
            Cursor cursorTD = db.getUnsyncedTransectionInsertNames();
            Cursor cursorTUD = db.getUnsyncedTransectionUpdateNames();
            Cursor cursorTDlt = db.getUnsyncedTransectionDeleteNames();
            Cursor cursorSellD = db.getUnsyncedSellInsertNames();
            Cursor cursorSellDlt=db.getUnsyncedSellDeleteNames();
            Cursor cursorBuyD = db.getUnsyncedBuyInsertNames();
            Cursor cursorBuyDlt=db.getUnsyncedBuyDeleteNames();


            if (cursorCus.moveToFirst()) {
               do {
                   myStringid.add(""+cursorCus.getInt(cursorCus.getColumnIndex(db.COLUMN_ID)));
                   myStringname.add( cursorCus.getString(cursorCus.getColumnIndex(db.CUSTOMER_NAME)));
                   myStringphone.add( cursorCus.getString(cursorCus.getColumnIndex(db.PHONEN)));
                   myStringqarza.add( cursorCus.getString(cursorCus.getColumnIndex(db.QARZA)));

                } while (cursorCus.moveToNext());

                for (int i = 0; i < myStringname.size(); i++)
                {
                    InsertUnsyncData(
                            Integer.parseInt(myStringid.get(i)),myStringname.get(i),myStringphone.get(i),myStringqarza.get(i)
                    );
                }
            }


            else if (cursorU.moveToFirst()) {
                do {
                    myStringid.add(""+cursorU.getInt(cursorU.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorU.getString(cursorU.getColumnIndex(db.CUSTOMER_NAME)));
                    myStringphone.add( cursorU.getString(cursorU.getColumnIndex(db.PHONEN)));
                    myStringqarza.add( cursorU.getString(cursorU.getColumnIndex(db.QARZA)));

                } while (cursorU.moveToNext());

                for (int iu = 0; iu < myStringname.size(); iu++)
                {
                    UpdateUnsyncData(
                            Integer.parseInt(myStringid.get(iu)),myStringname.get(iu),myStringphone.get(iu),myStringqarza.get(iu)
                    );
                }
            }


           else if (cursorD.moveToFirst()) {
                do {
                    myStringid.add(""+cursorD.getInt(cursorD.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorD.getString(cursorD.getColumnIndex(db.CUSTOMER_NAME)));
                    myStringphone.add( cursorD.getString(cursorD.getColumnIndex(db.PHONEN)));
                    myStringqarza.add( cursorD.getString(cursorD.getColumnIndex(db.QARZA)));

                } while (cursorD.moveToNext());
                for (int idel = 0; idel < myStringname.size(); idel++)
                {
                    DeleteUnsyncData(
                            Integer.parseInt(myStringid.get(idel)),myStringname.get(idel),myStringphone.get(idel),myStringqarza.get(idel)
                    );
                }
           }

            else if (cursorSupp.moveToFirst()) {
                do {
                    myStringid.add(""+cursorSupp.getInt(cursorSupp.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorSupp.getString(cursorSupp.getColumnIndex(db.SUPPLIER_NAME)));
                    myStringphone.add( cursorSupp.getString(cursorSupp.getColumnIndex(db.PHONEN)));
                    myStringqarza.add( cursorSupp.getString(cursorSupp.getColumnIndex(db.QARZA)));
                    myStringaddress.add( cursorSupp.getString(cursorSupp.getColumnIndex(db.ADDRESS)));
                    myStringcatagory.add( cursorSupp.getString(cursorSupp.getColumnIndex(db.CATAGORY)));

                } while (cursorSupp.moveToNext());
                for (int is = 0; is < myStringname.size(); is++)
                {
                    InsertUnsyncSuppData(
                            Integer.parseInt(myStringid.get(is)),myStringname.get(is),myStringphone.get(is),myStringqarza.get(is),myStringaddress.get(is),myStringcatagory.get(is)
                    );
                }
            }

            else if (cursorSU.moveToFirst()) {
                do {
                    myStringid.add(""+cursorSU.getInt(cursorSU.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorSU.getString(cursorSU.getColumnIndex(db.SUPPLIER_NAME)));
                    myStringphone.add( cursorSU.getString(cursorSU.getColumnIndex(db.PHONEN)));
                    myStringqarza.add( cursorSU.getString(cursorSU.getColumnIndex(db.QARZA)));
                    myStringaddress.add( cursorSU.getString(cursorSU.getColumnIndex(db.ADDRESS)));
                    myStringcatagory.add( cursorSU.getString(cursorSU.getColumnIndex(db.CATAGORY)));

                } while (cursorSU.moveToNext());
                for (int isu = 0; isu < myStringname.size(); isu++)
                {
                    UpdateUnsyncSuppData(
                            Integer.parseInt(myStringid.get(isu)),myStringname.get(isu),myStringphone.get(isu),myStringqarza.get(isu)
                            ,myStringaddress.get(isu),myStringcatagory.get(isu)
                    );
                }
            }


           else if (cursorSD.moveToFirst()) {
                do {
                    myStringid.add(""+cursorSD.getInt(cursorSD.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorSD.getString(cursorSD.getColumnIndex(db.SUPPLIER_NAME)));
                    myStringphone.add( cursorSD.getString(cursorSD.getColumnIndex(db.PHONEN)));
                    myStringqarza.add( cursorSD.getString(cursorSD.getColumnIndex(db.QARZA)));

                } while (cursorSD.moveToNext());
                for (int isd = 0; isd < myStringname.size(); isd++)
                {
                    DeleteUnsyncSuppData(
                            Integer.parseInt(myStringid.get(isd)),myStringname.get(isd),myStringphone.get(isd),myStringqarza.get(isd)
                    );
                }
           }

            else if (cursorItm.moveToFirst()) {
                do {
                    myStringid.add(""+cursorItm.getInt(cursorItm.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorItm.getString(cursorItm.getColumnIndex(db.ITEM_NAME)));
                    myStringiDes.add( cursorItm.getString(cursorItm.getColumnIndex(db.ITEM_DES)));
                    myStringiQtt.add( cursorItm.getString(cursorItm.getColumnIndex(db.ITEM_Q)));
                    myStringiBp.add( cursorItm.getString(cursorItm.getColumnIndex(db.ITEM_BP)));
                    myStringiSp.add( cursorItm.getString(cursorItm.getColumnIndex(db.ITEM_SP)));
                    myStringCatagory.add( cursorItm.getString(cursorItm.getColumnIndex(db.CATAGORY)));
                    myStringDP.add( cursorItm.getString ( 7 ).toString ( ));

                } while (cursorItm.moveToNext());

                for (int iu = 0; iu < myStringname.size(); iu++)
                {
                    Toast.makeText(context, "yes unsynched", Toast.LENGTH_SHORT).show();
                    InsertUnsyncItems(
                            Integer.parseInt(myStringid.get(iu)),myStringname.get(iu),myStringiDes.get(iu),
                            myStringiQtt.get(iu),myStringiBp.get(iu),myStringiSp.get(iu),myStringCatagory.get(iu)
                            ,myStringDP.get(iu));
                }
            }

            else if (cursorItmU.moveToFirst()) {
                do {
                    myStringid.add(""+cursorItmU.getInt(cursorItmU.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorItmU.getString(cursorItmU.getColumnIndex(db.ITEM_NAME)));
                    myStringiDes.add( cursorItmU.getString(cursorItmU.getColumnIndex(db.ITEM_DES)));
                    myStringiQtt.add( cursorItmU.getString(cursorItmU.getColumnIndex(db.ITEM_Q)));
                    myStringiBp.add( cursorItmU.getString(cursorItmU.getColumnIndex(db.ITEM_BP)));
                    myStringiSp.add( cursorItmU.getString(cursorItmU.getColumnIndex(db.ITEM_SP)));

                } while (cursorItmU.moveToNext());

                for (int iu = 0; iu < myStringname.size(); iu++)
                {
                    UpdateUnsyncItems(
                            Integer.parseInt(myStringid.get(iu)),myStringname.get(iu),myStringiDes.get(iu),
                            myStringiQtt.get(iu),myStringiBp.get(iu),myStringiSp.get(iu)
                    );
                }
            }

            else if (cursorItmD.moveToFirst()) {
                do {
                    myStringid.add(""+cursorItmD.getInt(cursorItmD.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorItmD.getString(cursorItmD.getColumnIndex(db.ITEM_NAME)));
                    myStringiDes.add( cursorItmD.getString(cursorItmD.getColumnIndex(db.ITEM_DES)));
                    myStringiQtt.add( cursorItmD.getString(cursorItmD.getColumnIndex(db.ITEM_Q)));
                    myStringiBp.add( cursorItmD.getString(cursorItmD.getColumnIndex(db.ITEM_BP)));
                    myStringiSp.add( cursorItmD.getString(cursorItmD.getColumnIndex(db.ITEM_SP)));
                    myStringCatagory.add( cursorItmD.getString(cursorItmD.getColumnIndex(db.CATAGORY)));
                   // myStringDP.add( cursorItmD.getString(cursorItmD.getColumnIndex(db.D_CHARGES)));

                } while (cursorItmD.moveToNext());

                for (int iu = 0; iu < myStringname.size(); iu++)
                {
                    DeleteUnsyncItems(
                            Integer.parseInt(myStringid.get(iu)),myStringname.get(iu),myStringiDes.get(iu),
                            myStringiQtt.get(iu),myStringiBp.get(iu),myStringiSp.get(iu),myStringCatagory.get(iu)
                    );
                }
            }

            else if (cursorTD.moveToFirst()) {
                do {
                    myStringid.add(""+cursorTD.getInt(cursorTD.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorTD.getString(cursorTD.getColumnIndex(db.TTYPE)));
                    myStringiDes.add( cursorTD.getString(cursorTD.getColumnIndex(db.TBILLID)));
                    myStringiQtt.add( cursorTD.getString(cursorTD.getColumnIndex(db.TDATE)));
                    myStringiBp.add( cursorTD.getString(cursorTD.getColumnIndex(db.TAMOUNT)));
                    myStringiSp.add( cursorTD.getString(cursorTD.getColumnIndex(db.TBALANCE)));
                    myStringT.add( cursorTD.getString(cursorTD.getColumnIndex(db.TPROFIT)));

                } while (cursorTD.moveToNext());

                for (int iu = 0; iu < myStringname.size(); iu++)
                {
                    InsertUnsyncTransectionData(
                            Integer.parseInt(myStringid.get(iu)),myStringname.get(iu),myStringiDes.get(iu),
                            myStringiQtt.get(iu),myStringiBp.get(iu),myStringiSp.get(iu),myStringT.get(iu)
                    );
                }
            }

            else if (cursorTUD.moveToFirst()) {
                do {
                    myStringid.add(""+cursorTUD.getInt(cursorTUD.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorTUD.getString(cursorTUD.getColumnIndex(db.TTYPE)));
                    myStringiDes.add( cursorTUD.getString(cursorTUD.getColumnIndex(db.TBILLID)));
                    myStringiQtt.add( cursorTUD.getString(cursorTUD.getColumnIndex(db.TDATE)));
                    myStringiBp.add( cursorTUD.getString(cursorTUD.getColumnIndex(db.TAMOUNT)));
                    myStringiSp.add( cursorTUD.getString(cursorTUD.getColumnIndex(db.TBALANCE)));
                    myStringT.add( cursorTUD.getString(cursorTUD.getColumnIndex(db.TPROFIT)));

                } while (cursorTUD.moveToNext());

                for (int iu = 0; iu < myStringname.size(); iu++)
                {
                    UpdateUnsyncTransectionData(
                            Integer.parseInt(myStringid.get(iu)),myStringname.get(iu),myStringiDes.get(iu),
                            myStringiQtt.get(iu),myStringiBp.get(iu),myStringiSp.get(iu),myStringT.get(iu)
                    );
                }
            }
            else if (cursorTDlt.moveToFirst()) {
                do {
                    myStringid.add(""+cursorTDlt.getInt(cursorTDlt.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorTDlt.getString(cursorTDlt.getColumnIndex(db.TTYPE)));
                    myStringiDes.add( cursorTDlt.getString(cursorTDlt.getColumnIndex(db.TBILLID)));
                    myStringiQtt.add( cursorTDlt.getString(cursorTDlt.getColumnIndex(db.TDATE)));
                    myStringiBp.add( cursorTDlt.getString(cursorTDlt.getColumnIndex(db.TAMOUNT)));
                    myStringiSp.add( cursorTDlt.getString(cursorTDlt.getColumnIndex(db.TBALANCE)));
                    myStringT.add( cursorTDlt.getString(cursorTDlt.getColumnIndex(db.TPROFIT)));

                } while (cursorTDlt.moveToNext());

                for (int iu = 0; iu < myStringname.size(); iu++)
                {
                    DeleteUnsyncTransectionData(
                            Integer.parseInt(myStringid.get(iu)),myStringname.get(iu),myStringiDes.get(iu),
                            myStringiQtt.get(iu),myStringiBp.get(iu),myStringiSp.get(iu),myStringT.get(iu)
                    );
                }
            }
            else if (cursorSellD.moveToFirst()) {
                do {
                    myStringid.add(""+cursorSellD.getInt(cursorSellD.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorSellD.getString(cursorSellD.getColumnIndex(db.SELL_CUSID)));
                    myStringiDes.add( cursorSellD.getString(cursorSellD.getColumnIndex(db.DATE)));
                    myStringiQtt.add( cursorSellD.getString(cursorSellD.getColumnIndex(db.DESCRIPTION)));
                    myStringiBp.add( cursorSellD.getString(cursorSellD.getColumnIndex(db.PREVT)));
                    myStringiSp.add( cursorSellD.getString(cursorSellD.getColumnIndex(db.NEWT)));
                    myStringT.add( cursorSellD.getString(cursorSellD.getColumnIndex(db.PAID)));
                    myStringS.add( cursorSellD.getString(cursorSellD.getColumnIndex(db.BALANCE)));
                    myStringDcharges.add( cursorSellD.getString(cursorSellD.getColumnIndex(db.D_CHARGES)));
                    myStringOstatus.add( cursorSellD.getString(cursorSellD.getColumnIndex(db.ORDER_STATUS)));

                } while (cursorSellD.moveToNext());

                for (int iu = 0; iu < myStringname.size(); iu++)
                {
                    Toast.makeText ( context , "synch sell data" , Toast.LENGTH_SHORT ).show ( );
                   InsertUnsyncSellData (
                            Integer.parseInt(myStringid.get(iu)),myStringname.get(iu),myStringiDes.get(iu),
                            myStringiQtt.get(iu),myStringiBp.get(iu),myStringiSp.get(iu),myStringT.get(iu),myStringS.get(iu)
                           ,myStringDcharges.get(iu),myStringOstatus.get(iu)
                    );
                }
            }
            else if (cursorSellDlt.moveToFirst()) {
                do {
                    myStringid.add(""+cursorSellDlt.getInt(cursorSellDlt.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add( cursorSellDlt.getString(cursorSellDlt.getColumnIndex(db.SELL_CUSID)));
                    myStringiDes.add( cursorSellDlt.getString(cursorSellDlt.getColumnIndex(db.DATE)));
                    myStringiQtt.add( cursorSellDlt.getString(cursorSellDlt.getColumnIndex(db.DESCRIPTION)));
                    myStringiBp.add( cursorSellDlt.getString(cursorSellDlt.getColumnIndex(db.PREVT)));
                    myStringiSp.add(cursorSellDlt.getString(cursorSellDlt.getColumnIndex(db.NEWT)));
                    myStringT.add(cursorSellDlt.getString(cursorSellDlt.getColumnIndex(db.PAID)));
                    myStringS.add(cursorSellDlt.getString(cursorSellDlt.getColumnIndex(db.BALANCE)));

                } while (cursorSellDlt.moveToNext());

                for (int iu = 0; iu < myStringname.size(); iu++)
                {
                    DeleteUnsyncSellData(
                            Integer.parseInt(myStringid.get(iu)),myStringname.get(iu),myStringiDes.get(iu),
                            myStringiQtt.get(iu),myStringiBp.get(iu),myStringiSp.get(iu),myStringT.get(iu),myStringS.get(iu)
                    );
                }
            }
            else if (cursorBuyD.moveToFirst()) {
                do {
                    myStringid.add("" + cursorBuyD.getInt(cursorBuyD.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add(cursorBuyD.getString(cursorBuyD.getColumnIndex(db.BUY_SUPID)));
                    myStringiDes.add(cursorBuyD.getString(cursorBuyD.getColumnIndex(db.DATE)));
                    myStringiQtt.add(cursorBuyD.getString(cursorBuyD.getColumnIndex(db.DESCRIPTION)));
                    myStringiBp.add(cursorBuyD.getString(cursorBuyD.getColumnIndex(db.PREVT)));
                    myStringiSp.add(cursorBuyD.getString(cursorBuyD.getColumnIndex(db.NEWT)));
                    myStringT.add(cursorBuyD.getString(cursorBuyD.getColumnIndex(db.PAID)));
                    myStringS.add(cursorBuyD.getString(cursorBuyD.getColumnIndex(db.BALANCE)));

                } while (cursorBuyD.moveToNext());

                for (int iu = 0; iu < myStringname.size(); iu++) {
                    InsertUnsyncBuyData(
                            Integer.parseInt(myStringid.get(iu)), myStringname.get(iu), myStringiDes.get(iu),
                            myStringiQtt.get(iu), myStringiBp.get(iu), myStringiSp.get(iu), myStringT.get(iu), myStringS.get(iu)
                    );
                }
            }
            else if (cursorBuyDlt.moveToFirst()) {
                do {
                    //Toast.makeText(context, "buyDlt", Toast.LENGTH_SHORT).show();
                    myStringid.add(""+cursorBuyDlt.getInt(cursorBuyDlt.getColumnIndex(db.COLUMN_ID)));
                    myStringname.add(cursorBuyDlt.getString(cursorBuyDlt.getColumnIndex(db.BUY_SUPID)));
                    myStringiDes.add(cursorBuyDlt.getString(cursorBuyDlt.getColumnIndex(db.DATE)));
                    myStringiQtt.add(cursorBuyDlt.getString(cursorBuyDlt.getColumnIndex(db.DESCRIPTION)));
                    myStringiBp.add(cursorBuyDlt.getString(cursorBuyDlt.getColumnIndex(db.PREVT)));
                    myStringiSp.add(cursorBuyDlt.getString(cursorBuyDlt.getColumnIndex(db.NEWT)));
                    myStringT.add(cursorBuyDlt.getString(cursorBuyDlt.getColumnIndex(db.PAID)));
                    myStringS.add(cursorBuyDlt.getString(cursorBuyDlt.getColumnIndex(db.BALANCE)));

                } while (cursorBuyDlt.moveToNext());

                for (int iu = 0; iu < myStringname.size(); iu++)
                {
                    //Toast.makeText(context, ":"+myStringiDes.get(iu)+":", Toast.LENGTH_SHORT).show();
                    DeleteUnsyncBuyData(
                            Integer.parseInt(myStringid.get(iu)),myStringname.get(iu),myStringiDes.get(iu),
                            myStringiQtt.get(iu),myStringiBp.get(iu),myStringiSp.get(iu),myStringT.get(iu),myStringS.get(iu)
                    );
                }
            }
            }

        }

    private void DeleteUnsyncData(final int deleteID, final String name,final String phone,final String qarza) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CUSTOMER_DELETE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")) {

                        db.deleteCustomer(deleteID);
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("name", name);
                params.put("phone", phone);
                params.put("qarza", qarza);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    private void InsertUnsyncData(final int insertID, final String name,final String phone,final String qarza) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CUSTOMER_INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateInsertNameStatus(insertID, INSERT_SYNCED);
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                // Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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

                params.put("name", name);
                params.put("phone", phone);
                params.put("qarza", qarza);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void UpdateUnsyncData(final int updateID, final String name,final String phone,final String qarza) {
       // final String id= ""+updateID;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CUSTOMER_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                db.changeUpdateNameStatus(updateID, UPDATE_SYNCED );

                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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
                params.put("name", name);
                params.put("phone", phone);
                params.put("qarza", qarza);


                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void UpdateUnsyncTransectionData(final int updateID, final String typeOT, final String billid, final String Date, final String amount, final String balance, final String profit) {
        // final String id= ""+updateID;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TRANSECTION_ALL_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                db.changeUpdateTransectionNameStatus(updateID, UPDATE_SYNCED );

                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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
                params.put("TypeOT", typeOT);
                params.put("Billid", billid);
                params.put("Date", Date);
                params.put("Amount", amount);
                params.put("Balance", balance);
                params.put("Profit", profit);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
    private void DeleteUnsyncTransectionData(final int deleteID, final String typeOT, final String billid, final String Date, final String amount, final String balance, final String profit) {
        // final String id= ""+updateID;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TRANSECTION_DELETE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                db.deleteTransection(deleteID);

                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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
                params.put("TypeOT", typeOT);
                params.put("Billid", billid);
                params.put("Date", Date);
                params.put("Amount", amount);
                params.put("Balance", balance);
                params.put("Profit", profit);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void InsertUnsyncTransectionData(final int insertID, final String typeOT, final String billid, final String Date, final String amount, final String balance, final String profit) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, TRANSECTION_INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateInsertTransectionNameStatus(insertID, INSERT_SYNCED);
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                // Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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

                params.put("TypeOT", typeOT);
                params.put("Billid", billid);
                params.put("Date", Date);
                params.put("Amount", amount);
                params.put("Balance", balance);
                params.put("Profit", profit);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void InsertUnsyncSellData(final int insertID , final String cusid , final String date , final String description , final String prevTotal , final String newTotal , final String Paid , final String Balance, final String Dcharges , final String Ostatuse) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SELL_INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateInsertSellNameStatus(insertID, INSERT_SYNCED);
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                Toast.makeText(context, ""+cusid, Toast.LENGTH_SHORT).show();

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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

                params.put("Cusid", cusid);
                params.put("Date", date);
                params.put("Des", description);
                params.put("PrevT", prevTotal);
                params.put("NewT", newTotal);
                params.put("Paid", Paid);
                params.put("Balance", Balance);
                params.put("Dcharges",Dcharges );
                params.put("Ostatus", Ostatuse);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void DeleteUnsyncSellData(final int deleteID,final String cusid,final String date,final String description ,final String prevTotal,final String newTotal,final String Paid,final String Balance) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SELL_DELETE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.deleteSell(deleteID);
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                // Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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

                params.put("Cusid", cusid);
                params.put("Date", date);
                params.put("Des", description);
                params.put("PrevT", prevTotal);
                params.put("NewT", newTotal);
                params.put("Paid", Paid);
                params.put("Balance", Balance);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void InsertUnsyncBuyData(final int insertID,final String supid,final String date,final String description ,final String prevTotal,final String newTotal,final String Paid,final String Balance) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BUY_INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateInsertBuyNameStatus(insertID, INSERT_SYNCED);
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                // Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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

                params.put("Supid", supid);
                params.put("Date", date);
                params.put("Des", description);
                params.put("PrevT", prevTotal);
                params.put("NewT", newTotal);
                params.put("Paid", Paid);
                params.put("Balance", Balance);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void DeleteUnsyncBuyData(final int deleteID,final String supid,final String date,final String description ,final String prevTotal,final String newTotal,final String Paid,final String Balance) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BUY_DELETE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(context, ""+date, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")) {

                        db.deleteBuy(deleteID);
                        Toast.makeText(context, "yes :"+obj.getString("message"), Toast.LENGTH_SHORT).show();

                        context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("Supid", supid);
                params.put("Date", date);
                params.put("Des", description);
                params.put("PrevT", prevTotal);
                params.put("NewT", newTotal);
                params.put("Paid", Paid);
                params.put("Balance", Balance);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    private void InsertUnsyncItems(final int insertID, final String iName,final String iDes,final String iQtt,final String iBp,final String iSp,
                                   final String iCatagory,final String iDp) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ITEM_INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateInsertItemNameStatus(insertID, INSERT_SYNCED);
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                // Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();
                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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

                params.put("iName", iName);
                params.put("iDes", iDes);
                params.put("iQtt", iQtt);
                params.put("iBp", iBp);
                params.put("iSp", iSp);
                params.put("iCatagory", iCatagory);
                params.put("iDp", iDp);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void UpdateUnsyncItems(final int updateID, final String iName,final String iDes,final String iQtt,final String iBp,final String iSp) {
        // final String id= ""+updateID;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ITEM_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                db.changeUpdateItemNameStatus(updateID, UPDATE_SYNCED );

                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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
                params.put("iName", iName);
                params.put("iDes", iDes);
                params.put("iQtt", iQtt);
                params.put("iBp", iBp);
                params.put("iSp", iSp);


                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void DeleteUnsyncItems(final int deleteID , final String iName , final String iDes , final String iQtt , final String iBp , final String iSp , final String iCatagory) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ITEM_DELETE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")) {

                        db.deleteItem(deleteID);
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("iName", iName);
                params.put("iDes", iDes);
                params.put("iQtt", iQtt);
                params.put("iBp", iBp);
                params.put("iSp", iSp);
                params.put("iCatagory", iCatagory);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    private void InsertUnsyncSuppData(final int insertID, final String name,final String phone,final String qarza,final String address,final String catagory) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SUPPLIER_INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateInsertSuppNameStatus(insertID, INSERT_SYNCED);
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                // Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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

                params.put("name", name);
                params.put("phone", phone);
                params.put("qarza", qarza);
                params.put("address", address);
                params.put("catagory", catagory);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void UpdateUnsyncSuppData(final int updateID, final String name,final String phone,final String qarza,final String address,final String catagory) {
        // final String id= ""+updateID;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SUPPLIER_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                db.changeUpdateSuppNameStatus(updateID, UPDATE_SYNCED );

                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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
                params.put("name", name);
                params.put("phone", phone);
                params.put("qarza", qarza);
                params.put("address", address);
                params.put("catagory", catagory);


                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void DeleteUnsyncSuppData(final int deleteID, final String name,final String phone,final String qarza) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SUPPLIER_DELETE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")) {

                        db.deleteSupplier(deleteID);
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("name", name);
                params.put("phone", phone);
                params.put("qarza", qarza);

                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  (networkInfo!=null && networkInfo.isConnected());
    }
}
