package cn.tedu.submarine;

import javax.swing.ImageIcon;
import java.util.Random;

//鱼雷潜艇
public class TorpedoSubmarine extends SeaObject implements EnemyScore{


    public TorpedoSubmarine() {
       super(64,20);
    }

    public void move(){
        x+=speed;   //x+(向右)
    }

    @Override
    public ImageIcon getImage() {
        return Images.torpesubm;
    }

    public int getScore(){
        return 40;
    }
}
