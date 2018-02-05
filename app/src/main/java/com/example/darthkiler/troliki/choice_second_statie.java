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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class choice_second_statie extends AppCompatActivity {
    private String user;
    private String menu;
    private qHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String ruta;
    private String statie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_second_statie);
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
        ((TextView)findViewById(R.id.textView20)).setText("Вы выбрали опцию");
        ((TextView)findViewById(R.id.textView7)).setText(menu);
        ((TextView)findViewById(R.id.textView22)).setText("Номер маршрута - "+ruta);
        ((TextView)findViewById(R.id.textView21)).setText("Первая остановка - "+statie);
        Spinner spinner = (Spinner)findViewById(R.id.activity_choise_statie_spinner_statie);
        ArrayList<String> objects=new ArrayList<>();
        int cod=0;
        Cursor cursor = mDb.rawQuery("select cod from oprire where trol="+ruta+" and statie='"+statie+"';", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cod=cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor = mDb.rawQuery("select statie from oprire where trol="+ruta+" and cod>="+cod, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            objects.add(cursor.getString(0));
            cursor.moveToNext();
        }
        ArrayAdapter adapter = new ArrayAdapter(
                this,android.R.layout.simple_list_item_1 ,objects.toArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        cursor.close();
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
    public void goToTimpIntreStatii(View View)
    {
        Intent i=new Intent(this,timp_intre_statii.class);
        i.putExtra("user",user);
        i.putExtra("menu",menu);
        i.putExtra("ruta",ruta);
        i.putExtra("statie",statie);
        i.putExtra("statie2",((Spinner)findViewById(R.id.activity_choise_statie_spinner_statie)).getSelectedItem().toString());
        this.finish();
        startActivity(i);
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
