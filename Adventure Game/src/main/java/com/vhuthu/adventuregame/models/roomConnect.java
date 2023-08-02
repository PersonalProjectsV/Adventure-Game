package com.vhuthu.adventuregame.models;

import javafx.scene.shape.Line;

public class roomConnect extends Line {
    private Character from=null;
    private Character to=null;
    private int identifier;

    public roomConnect(int id){setVisible(false); identifier=id;}
    public String  pathValue(){
        return from+""+to;
    }
    public void setFromTo(Character from,Character to){
        this.from=from;
        this.to=to;
    }
    public int getIdentifier(){
        return identifier;
    }

}
