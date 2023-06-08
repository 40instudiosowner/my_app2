package com.example.my_app;

import java.io.Serializable;

public abstract class Package implements Serializable {
    final private String size;
    final private boolean fragility;
    final private String requirement;
    public Package(String size, boolean fragility, String requirement){
        this.fragility = fragility;
        this.requirement = requirement;
        this.size = size;
    }
    public String get_size(){
        return "Размер: " + size + "\n";
    }
    public String get_requirement(){
        return "Требования: " + requirement + "\n";
    }
    public String get_fragility(){
        if (fragility)
        return "Хрупкая: да\n";
        else return "Хрупкая: нет\n";
    }
    public String get_type(){
        return "";
    }
}
