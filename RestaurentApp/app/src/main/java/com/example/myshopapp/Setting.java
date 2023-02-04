package com.example.myshopapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends Activity {
    Button remoute, importc, exportc, btnLs;
    Dialog dialog;
    SharedPreferences sp;
    TextView tvlang;
    SQLiteDatabase SQLITEDATABASE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        btnLs = (Button) findViewById(R.id.btnlanguage);
        tvlang=(TextView)findViewById(R.id.tvlang);
        final EditText edt1 = (EditText) findViewById(R.id.editText);
        final EditText edt2 = (EditText) findViewById(R.id.editText2);
        final EditText edtpn = (EditText) findViewById(R.id.edtPhoneNumber);
        final EditText edtshopname = (EditText) findViewById(R.id.edtsignatur);
        edtpn.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});
        edt1.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
        edt2.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
        SpannableString contentLS = new SpannableString("Language Setting");
        contentLS.setSpan(new UnderlineSpan(), 0, contentLS.length(), 0);
        sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lang=sp.getString("lang","");
        if(lang.equals(""))
        {
            showDialoge();
        }
        else {

            tvlang.setText((lang));
        }
        btnLs.setText(contentLS);
        btnLs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialoge();
            }
        });
        importc = (Button) findViewById(R.id.btnImport);
        SpannableString content = new SpannableString("Import BackUp");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        importc.setText(content);
        importc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);

                builder.setTitle("Import Alert!");
                builder.setIcon(R.drawable.errorimg);
                builder.setMessage("بیک اپ 'Import'کرنے سےاپ کاپرانہ ریکارڈ محفوظ ہوجائگا۔");

                // add a button
                builder.setPositiveButton("Import", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        importDB();
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog2 = builder.create();
                dialog2.show();
                //Toast.makeText(getApplicationContext(),strNOI+" "+a+" is not present with you only "+getNumber(a)+" items remaining",Toast.LENGTH_SHORT).show();
            }
        });
        exportc = (Button) findViewById(R.id.btnExport);
        SpannableString content2 = new SpannableString("Export BackUp");
        content2.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        exportc.setText(content2);
        exportc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);

                builder.setTitle("Import Alert!");
                builder.setIcon(R.drawable.errorimg);
                builder.setMessage("بیک اپ 'Export'کرنے سےاپ کاموجودہ ریکارڈ محفوظ ہوجائگا۔");

                // add a button
                builder.setPositiveButton("Export", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exportDB();
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog2 = builder.create();
                dialog2.show();
            }
        });

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edtpn.setText(sp.getString("userPhoneN", ""));
        edt1.setText(sp.getString("password", ""));
        edt2.setText(sp.getString("password", ""));
        edtshopname.setText(sp.getString("userShopName", ""));



        Button btnsave = (Button) findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtpn.getText().toString().equals("") || edt1.getText().toString().equals("")|| (edtpn.length()<11)) {
                    Toast.makeText(getApplicationContext(), "All the fields must be correctly Filled", Toast.LENGTH_SHORT).show();

                } else {
                    if (edt1.getText().toString().equals(edt2.getText().toString())) {
                        SharedPreferences sppass = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor spEdit = sppass.edit();
                        spEdit.putString("password", edt1.getText().toString());
                        spEdit.putString("userPhoneN", edtpn.getText().toString());
                        spEdit.putString("userShopName", edtshopname.getText().toString());
                        spEdit.commit();
                        Toast.makeText(getApplicationContext(), "password saved Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Password not match try again", Toast.LENGTH_SHORT).show();
                        edt1.setText("");
                        edt2.setText("");
                    }
                }
            }
        });
    }

    //importing database
    private void importDB() {
        // TODO Auto-generated method stub

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.twoaim.saidm.shopApp"
                        + "//databases//" + "MyShopDB";
                String backupDBPath = "/Android/MyShopDB";
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getBaseContext(), backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    //exporting database
    private void exportDB() {
        // TODO Auto-generated method stub

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.twoaim.saidm.shopApp"
                        + "//databases//" + "MyShopDB";
                String backupDBPath = "/Android/MyShopDB";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getBaseContext(), backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();

        }

    }

    private void showDialoge() {
        dialog = new Dialog(Setting.this);
        dialog.setContentView(R.layout.radiobutton_dialog);
        dialog.setTitle("Pleas Select App Language");
        List<String> stringList = new ArrayList<>();
        List<String> myString2 = new ArrayList<>();  // here is list
        int count = 0;
        int z = 0;
        boolean found = false;
        stringList.add("Urdu");
        stringList.add("English");
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(Setting.this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
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
                        String selectedL = btn.getText().toString();
                        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor spe=sp.edit();
                        spe.putString("lang",selectedL);
                        spe.commit();
                        tvlang.setText(selectedL);
                    }
                }
                dialog.dismiss();
            }

        });
    }


}

