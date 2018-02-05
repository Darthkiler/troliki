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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class timp_de_asteptare extends AppCompatActivity {
    private String user;
    private String menu;
    private qHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String ruta;
    private String statie;
    private String ora;
    private String mins;
    private String zi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timp_de_asteptare);
        user=(getIntent().getStringExtra("user"));
        menu=(getIntent().getStringExtra("menu"));
        ruta=(getIntent().getStringExtra("ruta"));
        statie=(getIntent().getStringExtra("statie"));
        ora=(getIntent().getStringExtra("ora"));
        mins=(getIntent().getStringExtra("mins"));
        if(getIntent().getStringExtra("zi").equals("Рабочий"))
        zi="l";
        else if(getIntent().getStringExtra("zi").equals("Суббота"))
            zi="s";
        else if(getIntent().getStringExtra("zi").equals("Воскресенье"))
            zi="d";

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
        String tr="Озвините, но сегодня троллейбусов больше не будет";
        Cursor cursor = mDb.rawQuery("select * from orar where zi='"+zi+"' and cod=" + ruta, null);
        cursor.moveToFirst();
        ArrayList<Integer> objects = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            objects.add(time(cursor.getInt(1), cursor.getInt(2)));
            cursor.moveToNext();

        }
        for (int i = 0; i < objects.size(); i++)
            for (int m = i; m < objects.size(); m++)
                if (objects.get(i) > (objects.get(m))) {
                    int w = objects.get(i);
                    objects.set(i, objects.get(m));
                    objects.set(m, w);
                }
        int cod = 0;
        int y = 0;
        int m=0;
        cursor = mDb.rawQuery("select cod from oprire where statie='" + statie + "' and trol=" + ruta, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cod = cursor.getInt(0);
            cursor.moveToNext();
        }
        for (int i = 0; i < cod; i++) {
            String t;
            if (i < 9) t = "0" + i;
            else t = i + "";
            cursor = mDb.rawQuery("select urm from timp,oprire where timp.ctimp=oprire.ctime and oprire.ctime=" + ruta + "" + t, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                y += cursor.getInt(0);
                cursor.moveToNext();

            }
        }
        for(int i=0;i<objects.size();i++)
        {
            objects.set(i,objects.get(i)+y);
        }
        boolean b=true;
        for(m=0;m<objects.size()&&objects.get(m)<(Integer.valueOf(ora)*60+Integer.valueOf(mins));m++);
        if(m!=objects.size())
        tr=((objects.get(m)-((Integer.valueOf( ora)*60+Integer.valueOf((mins)))))+" мин");
        ((TextView)findViewById(R.id.textView28)).setText("Время ожидания на остановке");
        ((TextView)findViewById(R.id.textView27)).setText(statie);
        ((TextView)findViewById(R.id.textView11)).setText(ruta+" маршрута - ");
        ((TextView)findViewById(R.id.textView29)).setText(tr);
    }
    int time(int ora,int min)//принимает часы и минуты и выводи количество минут
    {
        return (ora*60+min);
    }
    public void backToChoiseTimp(View View)
    {
        Intent i = new Intent(this, choice_timp.class);
        i.putExtra("user", user);
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
