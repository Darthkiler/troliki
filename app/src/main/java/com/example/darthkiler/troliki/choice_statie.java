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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class choice_statie extends AppCompatActivity {
    private String user;
    private String menu;
    private String ruta;
    private qHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_statie);
        user=(getIntent().getStringExtra("user"));
        menu=(getIntent().getStringExtra("menu"));
        ruta=(getIntent().getStringExtra("ruta"));
        ((TextView)findViewById(R.id.textView7)).setText("Вы выбрали опцию");
        ((TextView)findViewById(R.id.textView19)).setText(menu);
        ((TextView)findViewById(R.id.textView6)).setText("Номер маршрута - "+ruta);
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


        Spinner spinner = (Spinner)findViewById(R.id.activity_choise_statie_spinner_statie);
        ArrayList<String> objects=new ArrayList<>();
        Cursor cursor = mDb.rawQuery("select statie from oprire where trol="+ruta, null);
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
        Button button=(Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(menu.equals("Вывод рассписания остановки"))
                    goToWriteOrar(v);
                else
                    if(menu.equals("Вывод расстояния между остановками"))
                        goToSecondStatie(v);
                else
                        goToChoiceTimp(v);
            }
        });
    }
    public void backToChoiceRuta(View View)
    {


        Intent i = new Intent(this, choice_ruta.class);
        i.putExtra("user", user);
        i.putExtra("menu",menu);
        this.finish();
        startActivity(i);



    }
    public void goToWriteOrar(View View)
    {


        Intent i = new Intent(this, write_orar.class);
        i.putExtra("user", user);
        i.putExtra("menu",menu);
        i.putExtra("ruta",ruta);
        i.putExtra("statie",((Spinner)findViewById(R.id.activity_choise_statie_spinner_statie)).getSelectedItem().toString());
        this.finish();
        startActivity(i);

    }
    public void goToSecondStatie(View View)
    {
        Intent i = new Intent(this, choice_second_statie.class);
        i.putExtra("user", user);
        i.putExtra("menu",menu);
        i.putExtra("ruta",ruta);
        i.putExtra("statie",((Spinner)findViewById(R.id.activity_choise_statie_spinner_statie)).getSelectedItem().toString());
        this.finish();
        startActivity(i);
    }
    public void goToChoiceTimp(View View)
    {
        Intent i = new Intent(this, choice_timp.class);
        i.putExtra("user", user);
        i.putExtra("menu",menu);
        i.putExtra("ruta",ruta);
        i.putExtra("statie",((Spinner)findViewById(R.id.activity_choise_statie_spinner_statie)).getSelectedItem().toString());
        this.finish();
        startActivity(i);
    }
    public void toLogin(View View)
    {
        Intent i = new Intent(this, login.class);
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
    public void backToMenu(View View)
    {
        Intent i = new Intent(this, menu.class);
        i.putExtra("user", user);
        this.finish();
        startActivity(i);
    }
}
