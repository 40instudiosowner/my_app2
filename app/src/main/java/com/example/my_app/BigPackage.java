package com.example.my_app;

public class BigPackage extends Package{
    final private double weight;
    public BigPackage(String size, boolean fragility, String requirement, double weight){
        super(size, fragility, requirement);
        this.weight = weight;
    }
    public double get_weight(){
        return weight;
    }
    @Override
    public String get_type(){
        return "Большая ";
    }
}
