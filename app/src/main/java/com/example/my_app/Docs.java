package com.example.my_app;

public class Docs extends Package {
    public Docs(String size, boolean fragility, String requirement){
        super(size, fragility, requirement);
    }
    public String get_type(){
        return "Документы ";
    }
}
