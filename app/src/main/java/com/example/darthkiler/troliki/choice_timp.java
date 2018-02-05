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
import java.util.Date;

public class choice_timp extends AppCompatActivity {
    private String user;
    private String menu;
    private qHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String ruta;
    private String statie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_timp);
        user=(getIntent().getStringExtra("user"));
        menu=(getIntent().getStringExtra("menu"));
        ruta=(getIntent().getStringExtra("ruta"));
        statie=(getIntent().getStringExtra("statie"));
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String ora[]=new String[24];
        for(int i=0;i<24;i++)
            ora[i]=i+"";
        ArrayAdapter adapter = new ArrayAdapter(
                this,android.R.layout.simple_list_item_1 ,ora);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        String mins[]=new String[60];
        for(int i=0;i<60;i++)
            mins[i]=i+"";
        ArrayAdapter adapter2 = new ArrayAdapter(
                this,android.R.layout.simple_list_item_1 ,mins);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner.setSelection(Integer.valueOf(timpulcurent()[0]));
        spinner2.setSelection(Integer.valueOf(timpulcurent()[1]));
        ((TextView)findViewById(R.id.textView9)).setText("Вы выбрали опцию");
        ((TextView)findViewById(R.id.textView10)).setText(menu);
        ((TextView)findViewById(R.id.textView11)).setText("Номер маршрута - "+ ruta);
        ((TextView)findViewById(R.id.textView12)).setText("Остановка - "+statie);
        ((TextView)findViewById(R.id.textView13)).setText("Введите время и день");
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
        ArrayList<String> objects=new ArrayList<>();
        Cursor cursor = mDb.rawQuery("select zi from orar  where cod="+ruta+" group by zi", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(cursor.getString(0).equals("l"))
            objects.add("Рабочий");
            else if(cursor.getString(0).equals("s"))
                objects.add("Суббота");
            else if(cursor.getString(0).equals("d"))
             objects.add("Воскресенье");
            cursor.moveToNext();
        }
        cursor.close();
        ArrayAdapter adapter3 = new ArrayAdapter(
                this,android.R.layout.simple_list_item_1 ,objects.toArray());
        Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
        spinner3.setAdapter(adapter3);


    }
    public void goToTimpDeAsteptare(View View)
    {
        Intent i=new Intent(this,timp_de_asteptare.class);
        i.putExtra("user",user);
        i.putExtra("menu",menu);
        i.putExtra("ruta",ruta);
        i.putExtra("statie",statie);
        i.putExtra("ora",((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString());
        i.putExtra("mins",((Spinner)findViewById(R.id.spinner2)).getSelectedItem().toString());
        i.putExtra("zi",((Spinner)findViewById(R.id.spinner3)).getSelectedItem().toString());
        this.finish();
        startActivity(i);
    }
    static String[] timpulcurent()//возвращает массив который содержит текущее время
    {
        String data=new Date().toString();
        ArrayList<String> tmp = new ArrayList<String>();
        int i = 0;

        for (int j = 0; j < data.length(); j++) {
            if (data.charAt(j) == ' ') {
                if (j > i) {
                    tmp.add(data.substring(i, j));
                }
                i = j + 1;
            }
        }
        if (i < data.length()) {
            tmp.add(data.substring(i));
        }
        String t1[]=new String[tmp.size()];
        tmp.toArray(t1);

        tmp.clear();
        i = 0;
        for (int j = 0; j < t1[3].length(); j++) {
            if (t1[3].charAt(j) == ':') {
                if (j > i) {
                    tmp.add(t1[3].substring(i, j));
                }
                i = j + 1;
            }
        }
        if (i < t1[3].length()) {
            tmp.add(t1[3].substring(i));
        }
        tmp.remove(2);
        return tmp.toArray(new String[tmp.size()]);
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
