package com.example.my_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my_app.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class myAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<HashMap<String, Object>> objects;

    public myAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        ctx = context;
        objects = data;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<HashMap<String, Object>> get_objects() {
        return objects;
    }

    public void addItem(String item){
        HashMap<String, Object> map = new HashMap<>();
        map.put("Order", item);
        map.put("IsChecked", false);
        objects.add(map);
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item, parent, false);
        }

        HashMap<String, Object> p = getMap(position);

        ((TextView) view.findViewById(R.id.textView35)).setText((String) p.get("Order"));

        CheckBox cbBuy = (CheckBox) view.findViewById(R.id.checkBox4);
        // присваиваем чекбоксу обработчик
        cbBuy.setOnCheckedChangeListener(myCheckChangeList);
        cbBuy.setTag(position);
        cbBuy.setChecked((Boolean) p.get("IsChecked"));
        return view;
    }

    HashMap<String, Object> getMap(int position) {
        return ((HashMap<String, Object>) getItem(position));
    }


    ArrayList<HashMap<String, Object>> getBox() {
        ArrayList<HashMap<String, Object>> box = new ArrayList<HashMap<String, Object>>();
        for (HashMap<String, Object> p : objects) {
            if ((Boolean) p.get("IsChecked"))
                box.add(p);
        }
        return box;
    }

    // обработчик для чекбоксов
    CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Order", objects.get((Integer) buttonView.getTag()).get("Order"));
            map.put("IsChecked", isChecked);
            objects.set((Integer) buttonView.getTag(), map);
        }
    };
}
