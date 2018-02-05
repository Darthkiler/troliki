package com.example.darthkiler.troliki;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class login extends AppCompatActivity {
    private qHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.activity_login_editText_login).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if(isFocused&&(((EditText)findViewById(R.id.activity_login_editText_login)).getText()+"").equals("Логин"))
                {
                    ((EditText)findViewById(R.id.activity_login_editText_login)).setText("");

                }

                if(!isFocused&&(((EditText)findViewById(R.id.activity_login_editText_login)).getText()+"").equals(""))
                {
                    ((EditText)findViewById(R.id.activity_login_editText_login)).setText("Логин");
                }
            }

        });
        findViewById(R.id.activity_login_editText_password).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if(isFocused&&(((EditText)findViewById(R.id.activity_login_editText_password)).getText()+"").equals("Пароль"))
                {
                    ((EditText)findViewById(R.id.activity_login_editText_password)).setText("");

                }

                if(!isFocused&&(((EditText)findViewById(R.id.activity_login_editText_password)).getText()+"").equals(""))
                {
                    ((EditText)findViewById(R.id.activity_login_editText_password)).setText("Пароль");
                }
            }

        });
        ((LinearLayout)(findViewById(R.id.layoutContainer123))).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);   // handle the event first
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  // hide the soft keyboard
                    ((EditText)(findViewById(R.id.activity_login_editText_login))).clearFocus();
                    ((EditText)(findViewById(R.id.activity_login_editText_password))).clearFocus();
                }

                return true;
            }
        });
    }
    public void goToMenu(View View)
    {

        mDBHelper = new qHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {

        }
        int r=0;
        String s=((TextView)findViewById(R.id.activity_login_editText_login)).getText()+""+((TextView)findViewById(R.id.activity_login_editText_password)).getText();

        Cursor cursor = mDb.rawQuery("SELECT * FROM login", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if((cursor.getString(0) + ""+cursor.getString(1)).equals(s)) r++;
            cursor.moveToNext();
        }
        cursor.close();
        if(r!=0) {
            Intent i = new Intent(this, menu.class);
            i.putExtra("user", ((EditText) findViewById(R.id.activity_login_editText_login)).getText().toString());
            this.finish();
            startActivity(i);

        }
        else
        {
            (Toast.makeText(getApplicationContext(),"Неверный логин или пароль",Toast.LENGTH_LONG)).show();
        }
    }
    public void goToRegistration(View View)
    {
         /* Connection con;
          Statement stmt;
          ResultSet rs;
        String query2 = "select count(*) from qwe";*/
        this.finish();
        startActivity(new Intent(this,registration.class));
        //String url = "jdbc:sqlserver://sql6002.site4now.net:1433;databaseName=DB_A2B017_cucoreanu";
        //String log="DB_A2B017_cucoreanu_admin";
        //String pass="c9LRWs77";
        /*String url ="jdbc:postgresql://192.168.1.220:5432/postgres";
        String log="postgres";
        String pass="123";
        (Toast.makeText(getApplicationContext(),"Проверка",Toast.LENGTH_SHORT)).show();
        try {
            Class.forName("org.postgresql.Driver");
            (Toast.makeText(getApplicationContext(),"Проверка v2",Toast.LENGTH_SHORT)).show();
            con=DriverManager.getConnection(url, log, pass);
            (Toast.makeText(getApplicationContext(),"Создалось соединение",Toast.LENGTH_SHORT)).show();
            stmt = con.createStatement();
            (Toast.makeText(getApplicationContext(),"Состояние",Toast.LENGTH_SHORT)).show();

            rs = stmt.executeQuery(query2);
            while (rs.next()) {
                int count = rs.getInt(0);
                (Toast.makeText(getApplicationContext(),"Кол строк",Toast.LENGTH_SHORT)).show();
            }
        }
        catch (Exception e){(Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG)).show();
             }*/
    }
}
