package com.example.my_app;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.my_app.databinding.ActivityMainBinding;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import android.view.Menu;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private final int MAX_ORDERS = 5;
    private double price = 0;
    private myAdapter myadapter;
    private ListView listView;
    ArrayList<String> list_str;
    private ArrayList<Order> orders;
    private ArrayList<Package> packages;
    private ArrayList<Firm> firms;
    private boolean on_pause;
    private int order_counter = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreated");
        // String[] orders = {"a", "b" };
        on_pause = false;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        firms = new ArrayList<>();
        firms.add(new Firm("New York", "Apple"));
        firms.add(new Firm("Washington, DC", "The White House"));
        firms.add(new Firm("Albuquerque", "The Laundry"));
        firms.add(new Firm("Los Angeles", "The Walt Disney corp."));

        packages = new ArrayList<>();
        packages.add(new BigPackage("200x200", true, "Please, faster", 1000.0));
        packages.add(new BigPackage("400x400", false, "Please, slower", 800.0));
        packages.add(new SmallPackage("40x40", true, "You gotta be cautious"));
        packages.add(new Docs("A4", true, "Keep it away dry"));


        orders = new ArrayList<>();
        for (int i = 0; i < MAX_ORDERS - 1; i++) {
            orders.add(new Order(firms.get(i), packages.get(i), "A", "B", 100 * (i + 1)));
        }
        Courier courier = new Courier("Егор Николаевич Петренко", 100,
                "Яндекс еда, велик отсутствует, т.к. его обещали " +
                        "вернуть, но все заруинили и и пришлось стать коллектором, чтобы выбить 5к", orders);

        TextView info = findViewById(R.id.info);
        info.setText(getResources().getString(R.string.info) + courier.get_info());

        // находим список
        listView = findViewById(R.id.listView);
        list_str = new ArrayList<>();

        for (order_counter = 0; order_counter < MAX_ORDERS - 1; order_counter++) {
           list_str.add("Order " + String.valueOf(order_counter) + "\n\tFirm: " + firms.get(order_counter).get_firm() + "\n\tType: "+
                   packages.get(order_counter).get_type() + "\n\tАдрес Отправки: " + orders.get(order_counter).get_picking_address() +
                   "\n\tАдрес конечный: " + orders.get(order_counter).get_delivery_address() + "\n\tСтоимость: " +
                   String.valueOf(orders.get(order_counter).get_price()) + "\n");
        }

        HashMap<String, Object> map;
        // Упаковываем данные
        ArrayList<HashMap<String, Object>> data = new ArrayList<>(list_str.size());

        for (int i = 0; i < list_str.size(); i++) {
            map = new HashMap<>();
            map.put("Order", list_str.get(i));
            map.put("IsChecked", false);
            data.add(map);
        }

        myadapter = new myAdapter(this, data);
        // присваиваем адаптер списку
        listView.setAdapter(myadapter);

        Button button_ok = findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = 0;
                price = 0.0;
                for (HashMap<String, Object> p : myadapter.get_objects()){
                    if((Boolean) p.get("IsChecked")) {
                        price += orders.get(counter).get_price();
                    }
                    counter++;
                }
               Toast.makeText(listView.getContext(), String.valueOf(price), Toast.LENGTH_SHORT).show();
            }
        });

        Button button_clear = findViewById(R.id.button_clear);
        button_clear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list_str.size(); i++) {
                    data.get(i).put("Order", list_str.get(i));
                    data.get(i).put("IsChecked", false);
                }
                price = 0.0;
                myadapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       super.onCreateOptionsMenu(menu);
       getMenuInflater().inflate(R.menu.main_menu, menu);
      //  menu.add(getResources().getString(R.string.admin_mode));

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.admin_mode:
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                intent.putExtra(Order.class.getSimpleName(), orders);
                intent.putExtra(Package.class.getSimpleName(), packages);
                intent.putExtra(Firm.class.getSimpleName(), firms);

                on_pause = true;
               // startActivity(intent);
                startActivityForResult(intent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStarted");

    }
    @Override
    protected void onPause(){
        super.onPause();
        on_pause = true;
        Log.d("MainActivity", "onDestroy");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("MainActivity", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("MainActivity", "onStop");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("MainActivity", "onResume");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("MainActivity", "onRestart");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) {return;}
        Bundle arguments = intent.getExtras();
        on_pause = arguments.getBoolean("onPause");
        if (on_pause) {
            listView = findViewById(R.id.listView);
            orders = new ArrayList<>();
            orders = (ArrayList<Order>) arguments.getSerializable(Order.class.getSimpleName());
            packages = new ArrayList<>();
            packages = (ArrayList<Package>) arguments.getSerializable(Package.class.getSimpleName());
            firms = new ArrayList<>();
            firms = (ArrayList<Firm>) arguments.getSerializable(Firm.class.getSimpleName());
            ArrayList<String> list_str = new ArrayList<>();

            for (int order_counter = 0; order_counter < orders.size(); order_counter++) {
                list_str.add("Order " + String.valueOf(order_counter) + "\n\tFirm: " + firms.get(order_counter).get_firm() + "\n\tType: " +
                        packages.get(order_counter).get_type() + "\n\tАдрес Отправки: " + orders.get(order_counter).get_picking_address() +
                        "\n\tАдрес конечный: " + orders.get(order_counter).get_delivery_address() + "\n\tСтоимость: " +
                        String.valueOf(orders.get(order_counter).get_price()) + "\n");
            }

            HashMap<String, Object> map;
            // Упаковываем данные
            ArrayList<HashMap<String, Object>> data = new ArrayList<>(list_str.size());

            for (int i = 0; i < list_str.size(); i++) {
                map = new HashMap<>();
                map.put("Order", list_str.get(i));
                map.put("IsChecked", false);
                data.add(map);
            }
            myadapter = new myAdapter(this, data);
            listView.setAdapter(myadapter);
            on_pause = false;
        }
    }
}