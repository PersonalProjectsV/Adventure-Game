package com.vhuthu.adventuregame.models;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Dog extends Item {
    private Media cry;
    private Media bark;

    public Dog(){
        cry= new Media(new File("sound/cry.mp3").toURI().toString());
        bark = new Media(new File("sound/bark.mp3").toURI().toString());
    }
    public void bark(){
        MediaPlayer barkMediaPlayer = new MediaPlayer(bark);
        barkMediaPlayer.setStopTime(Duration.seconds(3));
        barkMediaPlayer.play();
    }
    public void cry(){
        MediaPlayer cryMediaPlayer=new MediaPlayer(cry);
        cryMediaPlayer.setStopTime(Duration.seconds(4));
        cryMediaPlayer.play();
    }
}
