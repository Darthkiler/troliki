package com.example.darthkiler.troliki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class menu extends AppCompatActivity {
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        user=(getIntent().getStringExtra("user"));
        Spinner spinner = (Spinner)findViewById(R.id.menu);
        String[] s={"Вывод маршрута","Вывод рассписания остановки","Вывод расстояния между остановками","Вывод времени ожидания троллейбуса"};
        ArrayAdapter adapter = new ArrayAdapter(
                this,android.R.layout.simple_list_item_1 ,s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    public void backToLogin(View View)
    {
        this.finish();
        startActivity(new Intent(menu.this,login.class));
    }
    public void goToChoiceRuta(View View)
    {


        Intent i=new Intent(menu.this,choice_ruta.class);
        i.putExtra("user",user);
        i.putExtra("menu",((Spinner)findViewById(R.id.menu)).getSelectedItem().toString());
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
