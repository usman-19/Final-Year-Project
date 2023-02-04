package com.example.myshopapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "MyShopDB";
    public static final String TABLE_CUSTOMER = "CustomerTable";
    public static final String TABLE_SUPPLIER = "SupplierTable";
    public static final String TABLE_ITEMS = "itemsTable";
    public static final String TABLE_TRANSECTION = "TransectionTable";
    public static final String TABLE_SELLDETAIL = "SellDetailTable";
    public static final String TABLE_BUYDETAIL = "BuyDetailTable";
    public static final String SELL_CUSID = "cusid";
    public static final String BUY_SUPID = "supid";
    public static final String DATE = "date";
    public static final String DESCRIPTION = "description";
    public static final String DELIVERY_PRICE = "dprice";
    public static final String PREVT = "prevTotal";
    public static final String NEWT = "newTotal";
    public static final String PAID = "paid";
    public static final String BALANCE = "balance";
    public static final String TTYPE = "typeOT";
    public static final String TBILLID = "billid";
    public static final String TDATE = "date";
    public static final String TAMOUNT = "amount";
    public static final String TBALANCE = "balance";
    public static final String TPROFIT = "profit";
    public static final String ITEM_NAME = "iname";
    public static final String ITEM_DES = "iDes";
    public static final String ITEM_Q = "numberOI";
    public static final String ITEM_BP = "buyingP";
    public static final String ITEM_SP = "sellP";
    public static final String COLUMN_ID = "id";
    public static final String CUSTOMER_NAME = "cname";
    public static final String PHONEN = "phoneN";
    public static final String QARZA = "qarza";
    public static final String ADDRESS = "address";
    public static final String CATAGORY = "catagory";
    public static final String D_CHARGES = "dcharges";
    public static final String ORDER_STATUS = "ostatus";
    public static final String SUPPLIER_NAME = "sname";
    public static final String INSERT_STATUS = "insertStatus";
    public static final String DELETE_STATUS = "deleteStatus";
    public static final String UPDATE_STATUS = "updateStatus";
    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_BUYDETAIL = "CREATE TABLE " + TABLE_BUYDETAIL
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + BUY_SUPID +
            " VARCHAR, " + DATE +
            " VARCHAR, " + DESCRIPTION +
            " VARCHAR, " + PREVT+
            " VARCHAR, " + NEWT+
            " VARCHAR, " + PAID+
            " VARCHAR, " + BALANCE+
            " VARCHAR, "+ INSERT_STATUS +
            " TINYINT, " +DELETE_STATUS +
            " TINYINT, " + UPDATE_STATUS +
            " TINYINT);";

    private static final String CREATE_TABLE_SELLDETAIL = "CREATE TABLE " + TABLE_SELLDETAIL
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + SELL_CUSID +
            " VARCHAR, " + DATE +
            " VARCHAR, " + DESCRIPTION +
            " VARCHAR, " + PREVT+
            " VARCHAR, " + NEWT+
            " VARCHAR, " + PAID+
            " VARCHAR, " + BALANCE+
            " VARCHAR, " + D_CHARGES+
            " VARCHAR, " + ORDER_STATUS+
            " VARCHAR, "+ INSERT_STATUS +
            " TINYINT, " +DELETE_STATUS +
            " TINYINT, " + UPDATE_STATUS +
            " TINYINT);";

    private static final String CREATE_TABLE_TRANSECTION = "CREATE TABLE " + TABLE_TRANSECTION
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + TTYPE +
            " VARCHAR, " + TBILLID +
            " VARCHAR, " + TDATE +
            " VARCHAR, " + TAMOUNT +
            " VARCHAR, " + TBALANCE+
            " VARCHAR, " + TPROFIT+
            " VARCHAR, "+ INSERT_STATUS +
            " TINYINT, " +DELETE_STATUS +
            " TINYINT, " + UPDATE_STATUS +
            " TINYINT);";



    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE " + TABLE_ITEMS
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM_NAME +
            " VARCHAR, " + ITEM_DES +
            " VARCHAR, " + ITEM_Q +
            " VARCHAR, " + ITEM_BP +
            " VARCHAR, " + ITEM_SP+
            " VARCHAR, " + CATAGORY+
            " VARCHAR, " + DELIVERY_PRICE+
            " VARCHAR, "+ INSERT_STATUS +
            " TINYINT, " +DELETE_STATUS +
            " TINYINT, " + UPDATE_STATUS +
            " TINYINT);";


    private static final String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + CUSTOMER_NAME +
            " VARCHAR, " + PHONEN +
            " VARCHAR, " + QARZA +
            " VARCHAR, " + ADDRESS +
            " VARCHAR, "+ INSERT_STATUS +
            " TINYINT, " +DELETE_STATUS +
            " TINYINT, " + UPDATE_STATUS +
            " TINYINT,UNIQUE(PHONEN) ON CONFLICT REPLACE);";

    private static final String CREATE_TABLE_SUPPLIER = "CREATE TABLE " + TABLE_SUPPLIER
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUPPLIER_NAME +
            " VARCHAR, " + PHONEN +
            " VARCHAR, " + QARZA +
            " VARCHAR, " + ADDRESS +
            " VARCHAR, " + CATAGORY +
            " VARCHAR, "+ INSERT_STATUS +
            " TINYINT, " +DELETE_STATUS +
            " TINYINT, " + UPDATE_STATUS +
            " TINYINT);";


    public DatabaseHelper(Context context){

        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_CUSTOMER);
        db.execSQL(CREATE_TABLE_SUPPLIER);
        db.execSQL(CREATE_TABLE_ITEMS);
        db.execSQL(CREATE_TABLE_TRANSECTION);
        db.execSQL(CREATE_TABLE_SELLDETAIL);
        db.execSQL(CREATE_TABLE_BUYDETAIL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       // String sql = "DROP TABLE IF EXISTS CustomerTable";
        db.execSQL("DROP TABLE IF EXISTS'" +TABLE_CUSTOMER+"'");
        db.execSQL("DROP TABLE IF EXISTS'" +TABLE_SUPPLIER+"'");
        db.execSQL("DROP TABLE IF EXISTS'" +TABLE_ITEMS+"'");
        db.execSQL("DROP TABLE IF EXISTS'" +TABLE_TRANSECTION+"'");
        db.execSQL("DROP TABLE IF EXISTS'" +TABLE_SELLDETAIL+"'");
        db.execSQL("DROP TABLE IF EXISTS'" +TABLE_BUYDETAIL+"'");
        onCreate(db);
    }

    public boolean addCustomer(String Cname , String Cphone , String Cqarza , String Caddress , int insertStatus , int deleteStatus , int updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTOMER_NAME, Cname);
        contentValues.put(PHONEN, Cphone);
        contentValues.put(QARZA, Cqarza);
        contentValues.put(ADDRESS, Caddress);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close();
        return true;
    }

    public Boolean UpdateCustomer(String Cname , String Cphone , String Cqarza , String Caddress , int insertStatus , int deleteStatus , int updateStatus , int UserID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTOMER_NAME, Cname);
        contentValues.put(PHONEN, Cphone);
        contentValues.put(QARZA, Cqarza);
        contentValues.put(ADDRESS, Caddress);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_CUSTOMER, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }

    public Cursor getCustomerNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CUSTOMER + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedInsertNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + INSERT_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean updateInsertNameStatus(int insertID, int insertStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INSERT_STATUS, insertStatus);
        db.update(TABLE_CUSTOMER, contentValues, COLUMN_ID + "=" + insertID, null);
        db.close();
        return true;
    }

    public Cursor getUnsyncedUpdateNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + UPDATE_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean changeUpdateNameStatus(int updateID, int updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_CUSTOMER, contentValues, COLUMN_ID + "=" + updateID, null);
        db.close();
        return true;
    }

    public Boolean deleteCustomerStatus(int insertStatus, int deleteStatus, int updateStatus, int UserID) {

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_CUSTOMER, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }
    public Cursor getUnsyncedDeleteNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + DELETE_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void deleteCustomer(int deleteID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMER,COLUMN_ID + "=" + deleteID,null);
    }


 ///    //    //    //     ///      ///     ////     ///     ///     ////     ///     ///      ///    ///    ///    ////     ///     ///
    ///    //    //    //     ///      ///     ////     ///     ///     ////     ///     ///      ///    ///    ///    ////     ///     ///


    public boolean addSupplier(String Sname , String Sphone , String Sqarza , String address , String catagory , int insertStatus , int deleteStatus , int updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SUPPLIER_NAME, Sname);
        contentValues.put(PHONEN, Sphone);
        contentValues.put(QARZA, Sqarza);
        contentValues.put(ADDRESS, address);
        contentValues.put(CATAGORY, catagory);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.insert(TABLE_SUPPLIER, null, contentValues);
        db.close();
        return true;
    }

    public Boolean UpdateSupplier(String sname , String sphone , String sqarza , String address , String catagory , int insertStatus , int deleteStatus , int updateStatus , int UserID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SUPPLIER_NAME, sname);
        contentValues.put(PHONEN, sphone);
        contentValues.put(QARZA, sqarza);
        contentValues.put(ADDRESS, address);
        contentValues.put(CATAGORY, catagory);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_SUPPLIER, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }

    public Cursor getSupplierNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_SUPPLIER + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedSuppInsertNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_SUPPLIER + " WHERE " + INSERT_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean updateInsertSuppNameStatus(int insertID, int insertStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INSERT_STATUS, insertStatus);
        db.update(TABLE_SUPPLIER, contentValues, COLUMN_ID + "=" + insertID, null);
        db.close();
        return true;
    }

    public Cursor getUnsyncedSuppUpdateNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_SUPPLIER + " WHERE " + UPDATE_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean changeUpdateSuppNameStatus(int updateID, int updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_SUPPLIER, contentValues, COLUMN_ID + "=" + updateID, null);
        db.close();
        return true;
    }

    public Boolean deleteSupplierStatus(int insertStatus, int deleteStatus, int updateStatus, int UserID) {

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_SUPPLIER, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }
    public Cursor getUnsyncedSuppDeleteNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_SUPPLIER + " WHERE " + DELETE_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void deleteSupplier(int deleteID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUPPLIER,COLUMN_ID + "=" + deleteID,null);
    }


    ///    //    //    //     ///      ///     ////     ///     ///     ////     ///     ///      ///    ///    ///    ////     ///     ///
    ///    //    //    //     ///      ///     ////     ///     ///     ////     ///     ///      ///    ///    ///    ////     ///     ///


    public boolean addTransection(String typeOT, String billid,String date,String amount,String balance,String profit,int insertStatus,int deleteStatus,int updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TTYPE, typeOT);
        contentValues.put(TBILLID, billid);
        contentValues.put(TDATE, date);
        contentValues.put(TAMOUNT, amount);
        contentValues.put(TBALANCE, balance);
        contentValues.put(TPROFIT, profit);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.insert(TABLE_TRANSECTION, null, contentValues);
        db.close();
        return true;
    }

    public Boolean updateTransection(String Tamount, String Tbalance, String Tprofit,int updateStatus, int UserID) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TAMOUNT, Tamount);
        contentValues.put(TBALANCE, Tbalance);
        contentValues.put(TPROFIT, Tprofit);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_TRANSECTION, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;

    }
    public Cursor getTransectionData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_TRANSECTION + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedTransectionInsertNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_TRANSECTION + " WHERE " + INSERT_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean updateInsertTransectionNameStatus(int insertID, int insertStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INSERT_STATUS, insertStatus);
        db.update(TABLE_TRANSECTION, contentValues, COLUMN_ID + "=" + insertID, null);
        db.close();
        return true;
    }

    public Cursor getUnsyncedTransectionUpdateNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_TRANSECTION + " WHERE " + UPDATE_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean changeUpdateTransectionNameStatus(int updateID, int updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_TRANSECTION, contentValues, COLUMN_ID + "=" + updateID, null);
        db.close();
        return true;
    }

    public Boolean deleteTransectionStatus(int insertStatus, int deleteStatus, int updateStatus, int UserID) {

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_TRANSECTION, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }
    public Cursor getUnsyncedTransectionDeleteNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_TRANSECTION + " WHERE " + DELETE_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void deleteTransection(int deleteID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSECTION,COLUMN_ID + "=" + deleteID,null);
    }

    public boolean updateItemsFT(String numberOI, int insertStatus, int deleteStatus, int updateStatus, int UserID) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ITEM_Q, numberOI);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_ITEMS, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }


    ///    //    //    //     ///      ///     ////     ///     ///     ////     ///     ///      ///    ///    ///    ////     ///     ///
    ///    //    //    //     ///      ///     ////     ///     ///     ////     ///     ///      ///    ///    ///    ////     ///     ///


    public boolean addItem(String Iname , String Ides , String itemQ , String itemSP , String itemBP , String iCatagory , String iDp , int insertStatus , int deleteStatus , int updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ITEM_NAME, Iname);
        contentValues.put(ITEM_DES, Ides);
        contentValues.put(ITEM_Q, itemQ);
        contentValues.put(ITEM_SP, itemSP);
        contentValues.put(ITEM_BP, itemBP);
        contentValues.put(CATAGORY, iCatagory);
        contentValues.put(DELIVERY_PRICE, iDp);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.insert(TABLE_ITEMS, null, contentValues);
        db.close();
        return true;
    }

    public Boolean UpdateItem(String Iname, String Ides, String itemQ, String itemBP, String itemSP, int insertStatus, int deleteStatus, int updateStatus, int UserID) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ITEM_NAME, Iname);
        contentValues.put(ITEM_DES, Ides);
        contentValues.put(ITEM_Q, itemQ);
        contentValues.put(ITEM_BP, itemBP);
        contentValues.put(ITEM_SP, itemSP);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_ITEMS, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;

    }
    public Cursor getItemNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_ITEMS + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedItemInsertNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + INSERT_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean updateInsertItemNameStatus(int insertID, int insertStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INSERT_STATUS, insertStatus);
        db.update(TABLE_ITEMS, contentValues, COLUMN_ID + "=" + insertID, null);
        db.close();
        return true;
    }

    public Cursor getUnsyncedItemUpdateNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + UPDATE_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean changeUpdateItemNameStatus(int updateID, int updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_ITEMS, contentValues, COLUMN_ID + "=" + updateID, null);
        db.close();
        return true;
    }

    public Boolean deleteItemStatus(int insertStatus, int deleteStatus, int updateStatus, int UserID) {

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_ITEMS, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }
    public Cursor getUnsyncedItemDeleteNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + DELETE_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void deleteItem(int deleteID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS,COLUMN_ID + "=" + deleteID,null);
    }


    ///    //    //    //     ///      ///     ////     ///     ///     ////     ///     ///      ///    ///    ///    ////     ///     ///
    ///    //    //    //     ///      ///     ////     ///     ///     ////     ///     ///      ///    ///    ///    ////     ///     ///



    public boolean addBuy(String supid, String date,String description ,String prevTotal,String newTotal,String Paid, String Balance,int insertStatus,int deleteStatus,int updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BUY_SUPID, supid);
        contentValues.put(DATE, date);
        contentValues.put(DESCRIPTION, description);
        contentValues.put(PREVT, prevTotal);
        contentValues.put(NEWT, newTotal);
        contentValues.put(PAID, Paid);
        contentValues.put(BALANCE, Balance);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.insert(TABLE_BUYDETAIL, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getBuyDetailData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_BUYDETAIL + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedBuyInsertNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_BUYDETAIL + " WHERE " + INSERT_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean updateInsertBuyNameStatus(int insertID, int insertStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INSERT_STATUS, insertStatus);
        db.update(TABLE_BUYDETAIL, contentValues, COLUMN_ID + "=" + insertID, null);
        db.close();
        return true;
    }

    public Boolean deleteBuyStatus(int insertStatus, int deleteStatus, int updateStatus, int UserID) {

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_BUYDETAIL, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }
    public Cursor getUnsyncedBuyDeleteNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_BUYDETAIL + " WHERE " + DELETE_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void deleteBuy(int deleteID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BUYDETAIL,COLUMN_ID + "=" + deleteID,null);
    }


//    public Boolean UpdateItem(String Iname, String Ides, String itemQ, String itemBP, String itemSP, int insertStatus, int deleteStatus, int updateStatus, int UserID) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(ITEM_NAME, Iname);
//        contentValues.put(ITEM_DES, Ides);
//        contentValues.put(ITEM_Q, itemQ);
//        contentValues.put(ITEM_BP, itemBP);
//        contentValues.put(ITEM_SP, itemSP);
//        contentValues.put(INSERT_STATUS, insertStatus);
//        contentValues.put(DELETE_STATUS, deleteStatus);
//        contentValues.put(UPDATE_STATUS, updateStatus);
//        db.update(TABLE_ITEMS, contentValues,COLUMN_ID+"="+UserID,null);
//        db.close();
//        return true;
//
//    }

///    //    //    //     ///      ///     ////     ///     ///     ////     ///     ///      ///    ///    ///    ////     ///     ///
    ///    //    //    //     ///      ///     ////     ///     ///     ////     ///     ///      ///    ///    ///    ////     ///     ///


    public boolean addSell(String cusid , String date , String description , String prevTotal , String newTotal , String Paid ,
                           String Balance , String delivery_Cahges , String orderStatus , int insertStatus , int deleteStatus , int updateStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SELL_CUSID, cusid);
        contentValues.put(DATE, date);
        contentValues.put(DESCRIPTION, description);
        contentValues.put(PREVT, prevTotal);
        contentValues.put(NEWT, newTotal);
        contentValues.put(PAID, Paid);
        contentValues.put(BALANCE, Balance);
        contentValues.put(D_CHARGES, delivery_Cahges);
        contentValues.put(ORDER_STATUS, orderStatus);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.insert(TABLE_SELLDETAIL, null, contentValues);
        db.close();
        return true;
    }



    public Cursor getSellDetailData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_SELLDETAIL + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedSellInsertNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_SELLDETAIL + " WHERE " + INSERT_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean updateInsertSellNameStatus(int insertID, int insertStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INSERT_STATUS, insertStatus);
        db.update(TABLE_SELLDETAIL, contentValues, COLUMN_ID + "=" + insertID, null);
        db.close();
        return true;
    }
//
//    public Cursor getUnsyncedItemUpdateNames() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String sql = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + UPDATE_STATUS + " = 1;";
//        Cursor c = db.rawQuery(sql, null);
//        return c;
//    }
//    public boolean changeUpdateItemNameStatus(int updateID, int updateStatus) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(UPDATE_STATUS, updateStatus);
//        db.update(TABLE_ITEMS, contentValues, COLUMN_ID + "=" + updateID, null);
//        db.close();
//        return true;
//    }
//
    public Boolean deleteSellStatus(int insertStatus, int deleteStatus, int updateStatus, int UserID) {

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_SELLDETAIL, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }
    public Cursor getUnsyncedSellDeleteNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_SELLDETAIL + " WHERE " + DELETE_STATUS + " = 1;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void deleteSell(int deleteID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SELLDETAIL,COLUMN_ID + "=" + deleteID,null);
    }

    public boolean UpdateCusFS(String cname, String phoneN, String qarza, int insertStatus, int deleteStatus, int updateStatus, int UserID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTOMER_NAME, cname);
        contentValues.put(PHONEN, phoneN);
        contentValues.put(QARZA, qarza);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_CUSTOMER, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }
    public boolean UpdateCusBelance(String qarza, int insertStatus, int deleteStatus, int updateStatus, int UserID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QARZA, qarza);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_CUSTOMER, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }
    public boolean UpdateOrder(String s, String s1, String s2, String in_progress, int i, int i1, int i2, int oid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWT, s);
        contentValues.put(BALANCE, s1);
        contentValues.put(D_CHARGES, s2);
        contentValues.put(ORDER_STATUS, in_progress);
        contentValues.put(INSERT_STATUS, i);
        contentValues.put(DELETE_STATUS, i1);
        contentValues.put(UPDATE_STATUS, i2);
        db.update(TABLE_SELLDETAIL, contentValues,COLUMN_ID+"="+oid,null);
        db.close();
        return true;
    }

    public boolean updateTransection2(String TBillID, String Tbalance,int updateStatus, int UserID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TBILLID, TBillID);
        contentValues.put(TBALANCE, Tbalance);
         contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_TRANSECTION, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;
    }

    public boolean UpdateSuppFB(String cname, String phoneN, String qarza, int insertStatus, int deleteStatus, int updateStatus, int UserID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SUPPLIER_NAME, cname);
        contentValues.put(PHONEN, phoneN);
        contentValues.put(QARZA, qarza);
        contentValues.put(INSERT_STATUS, insertStatus);
        contentValues.put(DELETE_STATUS, deleteStatus);
        contentValues.put(UPDATE_STATUS, updateStatus);
        db.update(TABLE_SUPPLIER, contentValues,COLUMN_ID+"="+UserID,null);
        db.close();
        return true;}

    public boolean UpdateOrderToDeliverd(String delivered , int i , int i1 , int i2 , int oid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDER_STATUS, delivered);
        contentValues.put(INSERT_STATUS, i);
        contentValues.put(DELETE_STATUS, i1);
        contentValues.put(UPDATE_STATUS, i2);
        db.update(TABLE_SELLDETAIL, contentValues,COLUMN_ID+"="+oid,null);
        db.close();
        return true;
    }
}

