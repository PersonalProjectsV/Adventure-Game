package com.vhuthu.adventuregame.models;

import javafx.scene.shape.Line;

public class Door implements GamePlace {
    /** In this implementation doors are always locked.
     A player must have the correct key to get through
     a door. Doors automatically lock after the player
     passes through.
     **/
    private Character roomID;
    private Line showDoor;
    room belongsTo;

    private boolean locked;

    /** We can construct a door at the site. */
    public Door(room belongsTo){
        this.belongsTo=belongsTo;

        locked=false;
    }
    public room getBelongsTo(){
        return belongsTo;
    }
    public boolean getLocked(){
        return locked;
    }
    public void unlock(){

        locked=false;
        if(showDoor!=null) {
            showDoor.setVisible(false);
        }
    }
    public void lock(){
        locked=true;

    }
    public void setShowDoor(Line line){
        showDoor=line;
        line.setVisible(false);
    }
    public Line getShowDoor(){
        return showDoor;
    }


}
