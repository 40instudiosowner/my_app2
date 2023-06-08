package com.example.my_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class AdminActivity extends Activity {

    private  Button addbtn;
    private Button deletebtn;
    private EditText editfirm;
    private EditText editprice;
    private EditText editao;
    private EditText editak;
    private EditText edittype;
    ArrayList<HashMap<String, Object>> data;
    private myAdapter myadapter;
    private ListView listView;
    private ArrayList<Package> packages;
    private ArrayList<Firm> firms;
    private ArrayList<Order> orders;
    private boolean on_pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Log.d("AdminActivity", "onCreated");

        on_pause = true;
        addbtn = findViewById(R.id.bt_add);
        deletebtn = findViewById(R.id.bt_delete);
        editfirm = findViewById(R.id.editFirm);
        editprice = findViewById(R.id.editPrice);
        editao = findViewById(R.id.editAO);
        editak = findViewById(R.id.editAK);
        edittype = findViewById(R.id.editType);
        listView = findViewById(R.id.listView);

        Bundle arguments = getIntent().getExtras();

        orders = (ArrayList<Order>) arguments.getSerializable(Order.class.getSimpleName());
        packages = (ArrayList<Package>) arguments.getSerializable(Package.class.getSimpleName());
        firms = (ArrayList<Firm>) arguments.getSerializable(Firm.class.getSimpleName());

        ArrayList<String> list_str = new ArrayList<>();

        for (int order_counter = 0; order_counter < orders.size(); order_counter++) {
            list_str.add("Order " + String.valueOf(order_counter) + "\n\tFirm: " + firms.get(order_counter).get_firm() + "\n\tType: "+
                    packages.get(order_counter).get_type() + "\n\tАдрес Отправки: " + orders.get(order_counter).get_picking_address() +
                    "\n\tАдрес конечный: " + orders.get(order_counter).get_delivery_address() + "\n\tСтоимость: " +
                    String.valueOf(orders.get(order_counter).get_price()) + "\n");
        }

        HashMap<String, Object> map;
        // Упаковываем данные
        data = new ArrayList<>(list_str.size());

        for (int i = 0; i < list_str.size(); i++) {
            map = new HashMap<>();
            map.put("Order", list_str.get(i));
            map.put("IsChecked", false);
            data.add(map);
        }
        myadapter = new myAdapter(this, data);
        listView.setAdapter(myadapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AdminActivity", "onResume");
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edittype.getText().toString().equals("Большая"))
                    packages.add(new BigPackage("100x100", true, "!", 200.0));
                else if (edittype.getText().toString().equals("Малая"))
                    packages.add(new SmallPackage("100x100", true, "!"));
                else packages.add(new Docs("10x10", false, "!!!"));

                firms.add(new Firm("Random", editfirm.getText().toString()));
                orders.add(new Order(firms.get(firms.size() - 1), packages.get(packages.size() - 1),
                        editao.getText().toString(), editak.getText().toString(), Double.parseDouble(editprice.getText().toString())));
                myadapter.addItem("Order " + myadapter.getCount() + "\n\tFirm: " + firms.get(firms.size() - 1).get_firm() + "\n\tType: "+
                        edittype.getText().toString() + "\n\tАдрес Отправки: " + orders.get(orders.size() - 1).get_delivery_address() +
                        "\n\tАдрес конечный: " + orders.get(orders.size() - 1).get_picking_address() + "\n\tСтоимость: " +
                        orders.get(orders.size() - 1).get_price() + "\n");
                listView.setAdapter(myadapter);
            }
        });
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int counter = 0;
                for (int counter = 0; counter < myadapter.getCount(); counter++){
                    if((Boolean) myadapter.getMap(counter).get("IsChecked")) {
                        myadapter.objects.remove(counter);
                        firms.remove(counter);
                        orders.remove(counter);
                        packages.remove(counter);

                    }

                }
               myadapter.notifyDataSetChanged();
                listView.setAdapter(myadapter);
            }
        });
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("AdminActivity", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("AdminActivity", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d("AdminActivity", "onStart");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("AdminActivity", "onPause");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("AdminActivity", "onRestart");
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
        intent.putExtra(Order.class.getSimpleName(), orders);
        intent.putExtra(Package.class.getSimpleName(), packages);
        intent.putExtra(Firm.class.getSimpleName(), firms);
        intent.putExtra("onPause", on_pause);
        setResult(RESULT_OK, intent);
        //startActivity(intent);
        finish(); // закрываем эту активити
    }
}
