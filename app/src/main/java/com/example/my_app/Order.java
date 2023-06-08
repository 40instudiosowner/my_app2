package com.example.my_app;

import android.widget.TextView;

import java.io.Serializable;

public class Order implements Serializable {
    final private Firm firm;
    final private Package _package;
    final private String delivery_address;
    final private String picking_address;
    final private double price;
    public Order(Firm firm, Package _package, String delivery_address, String picking_address, double price){
        this.firm = firm;
        this._package = _package;
        this.delivery_address = delivery_address;
        this.picking_address = picking_address;
        this.price = price;
    }

    public Firm get_firm(){
        return firm;
    }
    public Package get_package(){
        return _package;
    }
    public String get_delivery_address(){
        return delivery_address;
    }
    public String get_picking_address(){
        return picking_address;
    }
    public double get_price(){
        return price;
    }
    public String get_order_str(){
        return this.get_firm().get_firm() + this.get_package() + this.get_delivery_address() + this.get_picking_address() + this.get_price();
    }
    public void push(TextView text_view){

    }
    public void pop(TextView text_view){

    }
}
