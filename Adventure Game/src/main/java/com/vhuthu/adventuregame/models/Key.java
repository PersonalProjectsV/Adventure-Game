package com.vhuthu.adventuregame.models;

public class Key extends Item {
    private Character keyFor;
    public void setKeyFor(Character a){
        keyFor=a;
    }
    public Character getKeyFor(){
        return keyFor;
    }
}
