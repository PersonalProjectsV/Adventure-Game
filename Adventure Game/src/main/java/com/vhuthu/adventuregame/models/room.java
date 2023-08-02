package com.vhuthu.adventuregame.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;


import java.util.ArrayList;

public class room extends Rectangle implements GamePlace {
    private boolean visible;
    private Player player;
    private Character identifier;
    private String roomDesrc;
    private ArrayList<Item> inventory;
    private boolean coin;
    private Door[] doors=new Door[4];
    private BooleanProperty playerInRoom=new SimpleBooleanProperty(false);
    private BooleanProperty dark=new SimpleBooleanProperty(false);
    BooleanProperty roomHasL=new SimpleBooleanProperty(false);
    private int demand=0;
    private boolean neverOnn=false;
    public room(Character identifier){
        this.identifier=identifier;
        visible=false;
        inventory=new ArrayList<>();
        coin=false;
        doAll();

        playerInListen();

    }

    public void setPlayer(Player player){
        this.player= player;
    }
    public void never(){
        neverOnn=true;
    }
    public void setDemand(){
        if(getIdentifier()=='I'){
            demand=60;
        }
    }
    public void pay(){demand=0;}
    public int getDemand(){
        return demand;
    }
    private void playerInListen(){
        playerInRoom.addListener((observable, oldValue, newValue) -> {
            if (!oldValue &&roomHasLamp()&&!neverOnn){
                this.setFill(Color.LIGHTYELLOW);
            }
            else if (newValue&&player.hasLamp()&&player.lampOn()&&dark.getValue()){
                this.setFill(Color.LIGHTYELLOW);
            }
            else if (!newValue&&!roomHasLamp()&&dark.getValue()){this.setFill(Color.BLACK);}

        });
    }

    public void leaveRoom(){
        playerInRoom.setValue(false);
    }
    public void enterRoom(){
        playerInRoom.setValue(true);
    }



    public void setDark(boolean dark){
        this.dark.setValue(dark);
    }
    public boolean getDark(){return dark.getValue();}
    public Character getIdentifier(){
        return identifier;
    }

    public void setCoinActive() {

        coin = true;
    }
    public void visited(){
        coin=false;
    }
    public String getDescr(){
        return roomDesrc;
    }

    public void removeItem(Item theItem) {
        inventory.remove(theItem);
    }
    public void addItem(Item theItem) {
        inventory.add(theItem);
    }

    public void doDescrTooltip(){
        Tooltip lo=new Tooltip();
        lo.setTextAlignment(TextAlignment.CENTER);
        lo.setText(toString());
        lo.setStyle("-fx-background-color:red;");
        Tooltip.install(
                this,
                lo
        );

    }
    public void setRoomDesrc(String roomDesrc){
        this.roomDesrc=roomDesrc;

    }
    private void doAll(){
        for (int x=0;x<doors.length;x++){
            doors[x]=new Door(this);
        }
    }
    public Door getDoor(Character dir){
        if(dir.equals('E')){ return getDoorE(); }
        if(dir.equals('N')){ return getDoorN(); }
        if(dir.equals('S')){return getDoorS();}
        if(dir.equals('W')){return getDoorW();}
        return null;
    }

    public Door getDoorN(){
        return doors[0];
    }
    public Door getDoorS(){
        return doors[1];
    }
    public Door getDoorE(){
        return doors[2];
    }
    public Door getDoorW(){
        return doors[3];
    }
    @Override
    public String toString(){
        String string=this.getDescr()+"\n"+"Items in room:  "+"\n"+itemList();
        return string;
    }
    public String getInvenS(){

        String s="";

        for (int x=0;x<inventory.size();x++){
            s+=inventory.get(x).getIdentifier()+",";
        }
        return s;
    }
    private String itemList(){
        String string="";
        for (int x=0;x<inventory.size();x++){
            String cur="There is a/an "+inventory.get(x).getDesc()+"\n";
            string+=cur;
        }
        return string;
    }
    public Item getItem(String y){
        for (int x=0;x<inventory.size();x++){
            Item cur=inventory.get(x);
            if(cur.getDesc().startsWith(y)){
                return cur;
            }
        }
        return null;
    }
    public void lockAll(){
        for (int x=0;x<doors.length;x++){
            doors[x].lock();
        }
    }
    public void unlockAll(){
        for (int x=0;x<doors.length;x++){
            doors[x].unlock();
        }
    }
    public void clear(){
        inventory.clear();
    }
    public boolean roomHasLamp(){
        for (int x=0;x<inventory.size();x++){
            if(inventory.get(x) instanceof lamp) {
                System.out.println("there is lamp in this room");
                roomHasL.setValue(true);
                return true;
            }

        }
        System.out.println("There is no lamp  "+getIdentifier());
        roomHasL.setValue(false);
        return false;
    }
}
