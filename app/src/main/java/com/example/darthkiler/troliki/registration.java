package com.example.darthkiler.troliki;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.database.SQLException;
import android.widget.Toast;

import java.io.IOException;

public class registration extends AppCompatActivity {

    private qHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ((LinearLayout)(findViewById(R.id.layoutContainer666))).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);   // handle the event first
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  // hide the soft keyboard
                    ((EditText)(findViewById(R.id.activity_registration_editText))).clearFocus();
                    ((EditText)(findViewById(R.id.activity_registration_editText2))).clearFocus();
                    ((EditText)(findViewById(R.id.activity_registration_editText3))).clearFocus();
                }

                return true;
            }
        });
    }

    public void backToRegistration(View View)
    {
        this.finish();
        startActivity(new Intent(this,login.class));
    }

    public void Registration(View View)
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
            throw mSQLException;
        }
        int r=0;
        String s=((TextView)findViewById(R.id.activity_registration_editText)).getText()+"";
        Cursor cursor = mDb.rawQuery("SELECT * FROM login", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if((cursor.getString(0)).equals(s)) r++;
            cursor.moveToNext();
        }
        cursor.close();
        if(r!=0) {
            (Toast.makeText(getApplicationContext(),"Пользователь уже существует", Toast.LENGTH_SHORT)).show();
        }
        else
        if(!(((TextView)findViewById(R.id.activity_registration_editText2)).getText()+"").equals(((TextView)findViewById(R.id.activity_registration_editText3)).getText()+"")) {
            (Toast.makeText(getApplicationContext(),"Пароли не совпадают",Toast.LENGTH_SHORT)).show();
        }
        else {
            mDb.execSQL("insert into login values('" + ((TextView) findViewById(R.id.activity_registration_editText)).getText() + "','" +
                    ((TextView) findViewById(R.id.activity_registration_editText2)).getText() + "');");
            (Toast.makeText(getApplicationContext(),"Вы успешно зерегистрировались",Toast.LENGTH_SHORT)).show();
        }
    }
}
