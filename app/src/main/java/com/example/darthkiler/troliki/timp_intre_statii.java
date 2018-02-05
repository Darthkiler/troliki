package com.example.darthkiler.troliki;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;

public class timp_intre_statii extends AppCompatActivity {
    private String user;
    private String menu;
    private qHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String ruta;
    private String statie;
    private String statie2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timp_intre_statii);
        user=(getIntent().getStringExtra("user"));
        menu=(getIntent().getStringExtra("menu"));
        ruta=(getIntent().getStringExtra("ruta"));
        statie=(getIntent().getStringExtra("statie"));
        statie2=(getIntent().getStringExtra("statie2"));
        mDBHelper = new qHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        int cod1=0;
        int cod2=0;
        int t=0;
        Cursor cursor = mDb.rawQuery("select cod from oprire where statie='"+statie+"' and trol="+ruta, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            cod1=cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        cursor = mDb.rawQuery("select cod from oprire where statie='"+statie2+"' and trol="+ruta, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            cod2=cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        cursor = mDb.rawQuery("select urm from timp,oprire where cod>="+cod1+" and cod<"+cod2+" and ctimp=ctime and trol="+ruta, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            t+=cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        ((TextView)findViewById(R.id.textView26)).setText("Расстояние между остановкой");
        ((TextView)findViewById(R.id.textView25)).setText(statie);
        ((TextView)findViewById(R.id.textView24)).setText("и остановкой");
        ((TextView)findViewById(R.id.textView23)).setText(statie2);
        ((TextView)findViewById(R.id.textView8)).setText(ruta+" маршрута");
        ((TextView)findViewById(R.id.textView11)).setText(t+"");

    }
    public void backToChoiceSecondStatie(View View)
    {
        Intent i=new Intent(this,choice_second_statie.class);
        i.putExtra("user",user);
        i.putExtra("menu",menu);
        i.putExtra("ruta",ruta);
        i.putExtra("statie",statie);
        this.finish();
        startActivity(i);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit:
                toLogin(null);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void toLogin(View View)
    {
        Intent i = new Intent(this, login.class);
        this.finish();
        startActivity(i);
    }
    public void backToMenu(View View)
    {
        Intent i = new Intent(this, menu.class);
        i.putExtra("user", user);
        this.finish();
        startActivity(i);
    }
}
