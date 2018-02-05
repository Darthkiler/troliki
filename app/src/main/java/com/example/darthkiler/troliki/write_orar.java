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
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class write_orar extends AppCompatActivity {
    private String user;
    private String menu;
    private qHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String ruta;
    private String statie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_orar);
        user=(getIntent().getStringExtra("user"));
        menu=(getIntent().getStringExtra("menu"));
        ruta=(getIntent().getStringExtra("ruta"));
        statie=(getIntent().getStringExtra("statie"));
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


        ArrayList<String> days=new ArrayList<>();
        Cursor cursor;
        days.add("l");
        days.add("s");
        days.add("d");
        for(int j=0;j<days.size();j++) {
            ArrayList<Integer> objects = new ArrayList<>();
            cursor = mDb.rawQuery("select * from orar where zi='"+days.get(j)+"' and cod=" + ruta, null);
            cursor.moveToFirst();
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
            for (int i = 0; i < objects.size(); i++) {
                if(j==0)
                ((TextView) findViewById(R.id.tw1)).setText(((TextView) findViewById(R.id.tw1)).getText() + "\n   " + (objects.get(i) + y) / 60 + ":" + (objects.get(i) + y) % 60);
                if(j==1)

                    ((TextView) findViewById(R.id.tw2)).setText(((TextView) findViewById(R.id.tw2)).getText() + "\n   " + (objects.get(i) + y) / 60 + ":" + (objects.get(i) + y) % 60);
                if(j==2)
                ((TextView) findViewById(R.id.tw3)).setText(((TextView) findViewById(R.id.tw3)).getText() + "\n   " + (objects.get(i) + y) / 60 + ":" + (objects.get(i) + y) % 60);
            }
            cursor.close();
        }
    }
    int time(int ora,int min)//принимает часы и минуты и выводи количество минут
    {
        return (ora*60+min);
    }
    public void backToChoiceStatie(View View)
    {


        Intent i = new Intent(this, choice_statie.class);
        i.putExtra("user", user);
        i.putExtra("menu",menu);
        i.putExtra("ruta",ruta);
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
