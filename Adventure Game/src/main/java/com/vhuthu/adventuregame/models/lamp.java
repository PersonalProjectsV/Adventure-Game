package com.vhuthu.adventuregame.models;

public class lamp extends Item {
    private boolean isOn=false;
    public void setOff(){
        isOn=false;
    }
    public void setOn(){
        isOn=true;
    }
    public boolean isOn(){
        return isOn;
    }
}
