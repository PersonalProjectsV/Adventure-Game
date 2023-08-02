package com.vhuthu.adventuregame.models;

import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class Player extends Polygon implements GamePlace {
    private ArrayList<Item> carryingInven;
    private room curRoom;
    public Player(){
        carryingInven=new ArrayList<>();
    }
    public void setRoom(room newRoom){
        this.curRoom=newRoom;
    }
    public room getCurRoom(){
        return  curRoom;
    }
    public void pickUp(Item i) {
            carryingInven.add(i);
    }
    public boolean hasKey(Character character){
        for (int x=0;x<carryingInven.size();x++){
            Item cur=carryingInven.get(x);
            if(cur instanceof Key){
                Key key=(Key) cur;
                if(key.getKeyFor().equals(character))
                {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean hasLamp(){
        for(int x=0;x<carryingInven.size();x++){
            Item cur=carryingInven.get(x);
            if(cur instanceof lamp){
                return true;
            }
        }
        return false;
    }
    public void swithchLampOn(){

        for(int x=0;x<carryingInven.size();x++){
            Item cur=carryingInven.get(x);
            if(cur instanceof lamp){
                lamp l=(lamp)cur;
                l.setOn();
            }
        }
    }
    public void swithchLampOff(){

        for(int x=0;x<carryingInven.size();x++){
            Item cur=carryingInven.get(x);
            if(cur instanceof lamp){
                lamp l=(lamp)cur;
                l.setOff();
            }
        }
    }
    public boolean lampOn(){

        for(int x=0;x<carryingInven.size();x++){
            Item cur=carryingInven.get(x);
            if(cur instanceof lamp){
                lamp l=(lamp)cur;
                if(l.isOn()){return true;}
            }
        }
        return false;
    }
    public boolean hasDog(){

        for(int x=0;x<carryingInven.size();x++) {
            Item cur = carryingInven.get(x);
            if (cur instanceof Dog) {
                return true;
            }
        }
        return false;
    }
    public Dog getDog(){

        for(int x=0;x<carryingInven.size();x++) {
            Item cur = carryingInven.get(x);
            if (cur instanceof Dog) {
                return (Dog) cur;
            }
        }
        return null;
    }
    public Item getLamp(){
        for(int x=0;x<carryingInven.size();x++){
            Item cur=carryingInven.get(x);
            if(cur instanceof lamp){
                return cur;
            }
        }
        return null;
    }
    public String carryingString(){
        String carryS="";
        for(int x=0;x<carryingInven.size();x++){
            Item y=carryingInven.get(x);
            carryS+=y.getIdentifier()+",";
        }
        return carryS;
    }
    public int drop(String itemD) {
        for (int x=0;x<carryingInven.size();x++){
            Item cur=carryingInven.get(x);
            if(cur.getDesc().startsWith(itemD)){

                if(cur instanceof Treasure){
                    Treasure treasure=(Treasure)cur;
                    curRoom.addItem(treasure);
                    return treasure.getWorth();
                }
                else {
                    curRoom.addItem(cur);
                    carryingInven.remove(x);
                }
            }
        }
        return 0;
    }
    public void dropAll(){
        carryingInven.removeAll(carryingInven.subList(0,carryingInven.size()));
    }
    @Override
    public String toString(){
        String points="";
        for (int x=0;x<getPoints().size();x++){
            points+=getPoints().get(x)+",";
        }
        return points;
    }
}

