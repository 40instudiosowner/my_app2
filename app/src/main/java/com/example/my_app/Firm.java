package com.example.my_app;

import java.io.Serializable;

public class Firm implements Serializable {
    final private String address;
    final private String name;
    public Firm(String address, String name){
        this.address = address;
        this.name = name;
    }
    public String get_firm(){
        return name + ", " + address;
    }
}
