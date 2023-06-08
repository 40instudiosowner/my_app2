package com.example.my_app;

import java.util.ArrayList;

public class Courier {
    private String name;
    private double income;
    private String capabilities;
    private ArrayList<Order> orders;
    public Courier(String name, double income, String capabilities, ArrayList<Order> orders){
        this.name = name;
        this.income = income;
        this.capabilities = capabilities;
        this.orders = orders;
    }
    public String get_info(){
        return "Имя: " + name + "\nДоход: " + Double.toString(income) + "\nВозможности: " + capabilities;
    }
}
