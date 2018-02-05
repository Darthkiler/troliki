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

public class choice_ruta extends AppCompatActivity {
    private String user;
    private String menu;
    private qHelper mDBHelper;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_ruta);
        user=(getIntent().getStringExtra("user"));
        menu=(getIntent().getStringExtra("menu"));
        ((TextView)findViewById(R.id.textView14)).setText("Вы выбрали опцию");
        ((TextView)findViewById(R.id.text)).setText(menu);

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

        Spinner spinner = (Spinner)findViewById(R.id.activity_coice_ruta_spenner_ruta);
        ArrayList<String> objects=new ArrayList<>();
        Cursor cursor = mDb.rawQuery("select trol from oprire where cod=1 order by trol", null);
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
        Button button=(Button) findViewById(R.id.activity_choice_ruta_button_go_next);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(menu.equals("Вывод маршрута"))
                    goToWriteRuta(v);
                else
                    goToChoiceStatie(v);
            }
        });


    }

    public void goToChoiceStatie(View View)
    {

        Intent i=new Intent(this,choice_statie.class);
        i.putExtra("user",user);
        i.putExtra("menu",menu);
        i.putExtra("ruta",((Spinner)findViewById(R.id.activity_coice_ruta_spenner_ruta)).getSelectedItem().toString());
        this.finish();
        startActivity(i);
    }
    public void goToWriteRuta(View view)
    {

        Intent i=new Intent(this,write_ruta.class);
        i.putExtra("user",user);
        i.putExtra("menu",menu);
        i.putExtra("ruta",((Spinner)findViewById(R.id.activity_coice_ruta_spenner_ruta)).getSelectedItem().toString());
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
}
