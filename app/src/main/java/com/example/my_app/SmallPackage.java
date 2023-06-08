package com.example.my_app;

public class SmallPackage extends Package{
    public SmallPackage(String size, boolean fragility, String requirement){
        super(size, fragility, requirement);
    }
    public String get_type(){
        return "Малая ";
    }
}
