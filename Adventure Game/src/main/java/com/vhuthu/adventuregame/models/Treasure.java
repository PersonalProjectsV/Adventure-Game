package com.vhuthu.adventuregame.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class Treasure extends Item {
    public Treasure() throws Exception{
        makeCoin();
    }
    private Integer worth;
    private ImageView treaSureCoin;
    public void setWorth(Integer worth){
        this.worth=worth;
    }
    public Integer getWorth(){
        return worth;
    }
    private void makeCoin() throws Exception{
        FileInputStream inputstream = new FileInputStream("images/moneybag.png");
        Image image = new Image(inputstream);
        treaSureCoin = new ImageView(image);
        treaSureCoin.setFitHeight(20);
        treaSureCoin.setFitWidth(20);
    }
    public ImageView getTreaSureCoin(){
        return treaSureCoin;
    }
}