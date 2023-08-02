package com.vhuthu.adventuregame.models;
public class Item {

    private String description;
    private int identifier;

    public Item(String description){this.description=description;}
    public Item(){}
    public void setDesc(String description) {
        this.description = description;
    }

    public String getDesc() {
        return description;
    }
    public  void setIdentifier(int id){
        identifier=id;
    }
    public int getIdentifier(){
        return identifier;
    }
    @Override
    public String toString(){
        return description;
    }
}
